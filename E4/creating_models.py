from datetime import datetime

import pandas as pd
from scipy.sparse import csr_matrix
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.metrics import accuracy_score
from sklearn.naive_bayes import GaussianNB
from sklearn.model_selection import StratifiedShuffleSplit
# from nltk.corpus import stopwords

# import nltk
# nltk.download('stopwords')

def get_model_with_vectorizer(model):
    start_time = datetime.now()
    vectorizer=CountVectorizer(stop_words='english')

    print(f"Start time: {start_time}")
    print('Fetching data...')
    df = pd.read_csv("preprocessed_genres.csv", encoding="utf-8", sep='\t')
    # categoryIds = list(set(df['categoryId'].values))
    categories = list(df.drop_duplicates('category').sort_values('categoryId')['category'].values)
    corpus = df["description"]

    # vectorizer = TfidfVectorizer()
    Xfeatures: csr_matrix = vectorizer.fit_transform(corpus)
    Xfeatures = Xfeatures.toarray()

    y = df['categoryId']
    sss = StratifiedShuffleSplit(n_splits=1, test_size=0.3, random_state=0)
    sss.get_n_splits(Xfeatures, y)
    print("Splitting data...")
    train_index, test_index = next(sss.split(Xfeatures, y))
    X_train, X_test = Xfeatures[train_index], Xfeatures[test_index]
    y_train, y_test = y[train_index], y[test_index]
    print("Fitting...")
    model.fit(X_train, y_train)
    print("Running test...")
    prediction = model.predict(X_test)
    score = accuracy_score(y_test, prediction)
    end_time = datetime.now()
    summary = f"{model.__class__.__name__}\t{vectorizer.__class__.__name__}" \
              f"\t{score}\t{(end_time - start_time).seconds}"
    print("\t".join(["model", "vectorizer", "accuracy_score", "seconds"]))
    print(summary)

    with open("comparison.txt", "a", encoding="utf8") as f:
        f.write(summary + '\n')
    return model, vectorizer, categories
