# https://towardsdatascience.com/multi-label-text-classification-with-scikit-learn-30714b7819c5
# Define a pipeline combining a text feature extractor with multi lable classifier
# %matplotlib inline
import re
import matplotlib
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from scipy.sparse import csr_matrix
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score
from sklearn.naive_bayes import GaussianNB
from sklearn.multiclass import OneVsRestClassifier
# import nltk
# nltk.download('stopwords')
from nltk.corpus import stopwords
from preprocessing_methods import description_preprocessing

stop_words = set(stopwords.words('english'))
from sklearn.svm import LinearSVC
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline

df = pd.read_csv("preprocessed_genres.csv", encoding="utf-8", sep='\t')
categoryIds = list(set(df['categoryId'].values))
categories = list(df.drop_duplicates('category').sort_values('categoryId')['category'].values)

print(categoryIds)
print(categories)
corpus = df["description"]
print(corpus)

tfidf = TfidfVectorizer() # countvectorizer
# countVect = CountVectorizer()
Xfeatures: csr_matrix = tfidf.fit_transform(corpus)
# print(Xfeatures[0])
Xfeatures = Xfeatures.toarray()
print(Xfeatures)

y = df['categoryId']
X_train, X_test, y_train, y_test = train_test_split(Xfeatures, y, random_state=42, test_size=0.33, shuffle=True)
# print(X_train)
# print(y_train)
# print(X_test)
# print(y_test)

gaussianNB = GaussianNB()
print("Model created.")
gaussianNB.fit(X_train, y_train)
print("Fit done.")
print(X_test)
prediction = gaussianNB.predict(X_test)
print(prediction)
print(accuracy_score(y_test, prediction))

# # input("Press ENTER to continue...")
# for category in categories:
#     print(f"{list(train[category]).count(1)}/{len(list(train[category]))}")
#     NB_pipeline.fit(X_train, train[category])
#     prediction: np.ndarray = NB_pipeline.predict(X_test)
#     # print(test[category])
#     # print(prediction.tolist().count(0))
#     print(prediction.tolist().count(1))
#     print(test[category].tolist().count(1))
#     print(len(test[category].tolist()))
#     # print(len(prediction.tolist()))
#     print(f'{category.ljust(30)}{accuracy_score(test[category], prediction)}')

# sample_text = '''
# Old Major, the old boar on the Manor Farm, calls the animals on the farm for a meeting, where he compares the humans to parasites and teaches the animals a revolutionary song, 'Beasts of England'. When Major dies, two young pigs, Snowball and Napoleon, assume command and turn his dream into a philosophy. The animals revolt and drive the drunken and irresponsible Mr Jones from the farm, renaming it "Animal Farm". They adopt Seven Commandments of Animal-ism, the most important of which is, "All animals are equal". Snowball attempts to teach the animals reading and writing
# '''
inx_of_test = 100
should_be_label = df.category[inx_of_test]
sample_text = df.description[inx_of_test]
print(sample_text)
# preprocessed_text = description_preprocessing(sample_text)
text_features = tfidf.transform([sample_text]).toarray()
print(text_features)
prediction = gaussianNB.predict(text_features)[0]
print(f"Should be: {should_be_label}")
print(f"Predicted: {categories[prediction]}")
