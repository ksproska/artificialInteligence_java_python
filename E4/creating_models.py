import pandas as pd
from scipy.sparse import csr_matrix
from sklearn import svm
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.metrics import accuracy_score
from sklearn.naive_bayes import GaussianNB


def get_model_with_vectorizer():
    print('Fetching data...')
    df = pd.read_csv("preprocessed_genres.csv", encoding="utf-8", sep='\t')
    categoryIds = list(set(df['categoryId'].values))
    categories = list(df.drop_duplicates('category').sort_values('categoryId')['category'].values)
    corpus = df["description"]

    vectorizer = CountVectorizer()
    # vectorizer = TfidfVectorizer()
    Xfeatures: csr_matrix = vectorizer.fit_transform(corpus)
    Xfeatures = Xfeatures.toarray()

    y = df['categoryId']
    from sklearn.model_selection import StratifiedShuffleSplit
    sss = StratifiedShuffleSplit(n_splits=1, test_size=0.3, random_state=0)
    sss.get_n_splits(Xfeatures, y)
    print("Creating model...")
    model = GaussianNB()
    # model = svm.SVC()
    print("Model created.")
    print("Splitting data...")
    for train_index, test_index in sss.split(Xfeatures, y):
        X_train, X_test = Xfeatures[train_index], Xfeatures[test_index]
        y_train, y_test = y[train_index], y[test_index]
        print("Fitting...")
        model.fit(X_train, y_train)
        print("Fit done.")
        print("Running test...")
        prediction = model.predict(X_test)
        print(f"Accuracy score: {accuracy_score(y_test, prediction)}")
    return model, vectorizer, categories
