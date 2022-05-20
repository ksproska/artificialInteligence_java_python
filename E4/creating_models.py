import pandas as pd
from scipy.sparse import csr_matrix
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.metrics import accuracy_score
from sklearn.naive_bayes import GaussianNB


def get_model_with_vectorizer():
    df = pd.read_csv("preprocessed_genres.csv", encoding="utf-8", sep='\t')
    categoryIds = list(set(df['categoryId'].values))
    categories = list(df.drop_duplicates('category').sort_values('categoryId')['category'].values)
    corpus = df["description"]

    vectorizer = CountVectorizer()
    # vectorizer = TfidfVectorizer()
    Xfeatures: csr_matrix = vectorizer.fit_transform(corpus)
    Xfeatures = Xfeatures.toarray()

    y = df['categoryId']
    X_train, X_test, y_train, y_test = train_test_split(Xfeatures, y, random_state=42, test_size=0.33, shuffle=True)

    model = GaussianNB()
    print("Model created.")
    model.fit(X_train, y_train)
    print("Fit done.")
    prediction = model.predict(X_test)
    print(f"Accuracy score: {accuracy_score(y_test, prediction)}")
    return model, vectorizer, categories
