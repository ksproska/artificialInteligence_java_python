from datetime import datetime

import pandas as pd
from scipy.sparse import csr_matrix
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.feature_selection import chi2, SelectKBest
from sklearn.metrics import accuracy_score
from sklearn.model_selection import StratifiedShuffleSplit
from sklearn.naive_bayes import GaussianNB
from sklearn.model_selection import train_test_split
# from nltk.corpus import stopwords
import matplotlib.pyplot as plt
import numpy as np

# import nltk
# nltk.download('stopwords')


def get_model_with_vectorizer(model, k, n_splits=5):
    start_time = datetime.now()
    vectorizer = CountVectorizer(stop_words='english')

    print(f"Start time for {model}: {start_time}")
    df = pd.read_csv("preprocessed_genres.csv", encoding="utf-8", sep='\t')
    categories = list(df.drop_duplicates('category').sort_values('categoryId')['category'].values)
    # categories_mx = df.drop_duplicates('category').sort_values('categoryId')['category'].values
    corpus = df["description"]
    # Xfeatures: csr_matrix = vectorizer.fit_transform(corpus)
    # Xfeatures = Xfeatures.toarray()

    # vectorizer = TfidfVectorizer(sublinear_tf=True, max_df=0.5, min_df=1, stop_words='english')
    Xfeatures = vectorizer.fit_transform(corpus)
    # print(Xfeatures.shape)
    # print(categories_mx.shape)

    selector = SelectKBest(score_func=chi2, k=k)
    y = df['categoryId']
    selector.fit(Xfeatures, y)

    Xfeatures = selector.transform(Xfeatures).toarray()

    sss = StratifiedShuffleSplit(n_splits=n_splits, test_size=0.3, random_state=0)
    sss.get_n_splits(Xfeatures, y)
    score = None
    for train_index, test_index in sss.split(Xfeatures, y):
        X_train, X_test = Xfeatures[train_index], Xfeatures[test_index]
        y_train, y_test = y[train_index], y[test_index]
        model.fit(X_train, y_train)
        prediction = model.predict(X_test)
        score = accuracy_score(y_test, prediction)
        print(f"Score of split: {score}")
    end_time = datetime.now()
    summary = "\t".join([str(model), str(score), str((end_time - start_time).seconds), str(n_splits), str(k)])
    print("\t".join(["model", "accuracy_score", "seconds", "number_of_splits", "k"]))
    print(summary)

    with open(f"selection.csv", "a", encoding="utf8") as f:
        f.write(summary + '\n')
    return model, vectorizer, categories
