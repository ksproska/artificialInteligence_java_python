from datetime import datetime

from sklearn.linear_model import SGDClassifier
from sklearn.svm import LinearSVC, LinearSVR, NuSVC, NuSVR, SVC, OneClassSVM
from sklearn.naive_bayes import GaussianNB, BernoulliNB, ComplementNB, CategoricalNB, MultinomialNB
from sklearn.tree import DecisionTreeClassifier, DecisionTreeRegressor, ExtraTreeClassifier, ExtraTreeRegressor
from creating_models import get_model_with_vectorizer
from preprocessing_methods import description_preprocessing
from nltk.corpus import stopwords

# https://scikit-learn.org/stable/modules/svm.html
# https://scikit-learn.org/stable/modules/naive_bayes.html

# Kernel Function - method used to take data as input and transform it
# into the required form of processing data

models_NB = [
    GaussianNB(),       # general-purpose kernel
    MultinomialNB(),    # P(spam|money) = P(spam) * P(money|spam) / P(money) - Bayes theorem
    BernoulliNB(),      # same as MultinomialNB, but binary
    ComplementNB(),     # particularly suited for imbalanced data sets
    # CategoricalNB(min_categories=1), # each feature, has its own categorical distribution,
]
# SVM parameters:
# C - how much avoid misclassifying; the bigger value the smaller margin (the more strict classification)
# penalty - norm used in the penalization
# dual - either solve the dual or primal optimization problem
models_SVM = [
    SVC(C=2),
    SVC(C=3),
    SVC(C=4),
    SVC(kernel='rbf', degree=1),
    SVC(kernel='rbf', degree=2),
    SVC(kernel='rbf', degree=3),
    SVC(kernel='rbf', degree=4), # - pl wielomian, with parameter d for polynomial degree
    SVC(kernel='rbf', degree=5),
    SVC(kernel='rbf', degree=6),
    # LinearSVC(C=0.1, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            # uses hinge loss, supports only linear karnel
    # LinearSVC(C=0.3, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=0.5, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=0.7, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=1, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=2, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=3, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=4, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=5, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    # LinearSVC(C=6, penalty="l2", dual=False, loss='squared_hinge', class_weight="balanced"),            #
    SVC(kernel="rbf"), # Gaussian radial basis function (RBF) - in Gaussian no sigma parameter, here there is
    SVC(kernel="sigmoid"), # f(x) = 1/(1+e^-x) - for each x returns value between (0, 1)
    SVC(kernel="precomputed"),
    NuSVC(),

    SGDClassifier(),
]


for m in models_SVM:
    model, vectorizer, categories = get_model_with_vectorizer(model=m)
    print("-----------------------------------------------------------------")

    sample_text = "Inspector Harry Hole of the Oslo Crime Squad is dispatched to Sydney to observe a murder case. Harry " \
                  "is free to offer assistance, but he has firm instructions to stay out of trouble. The victim is a " \
                  "twenty-three year old Norwegian woman who is a minor celebrity back home. Never one to sit on the " \
                  "sidelines, Harry befriends one of the lead detectives, and one of the witnesses, as he is drawn deeper " \
                  "into the case. Together, they discover that this is only the latest in a string of unsolved murders, " \
                  "and the pattern points toward a psychopath working his way across the country. As they circle closer " \
                  "and closer to the killer, Harry begins to fear that no one is safe, least of all those investigating " \
                  "the case. "
    print("\nTested example: ", sample_text[:70], '...')
    should_be_label = "Crime fiction"
    preprocessed_text = description_preprocessing(sample_text)
    text_features = vectorizer.transform([preprocessed_text]).toarray()
    prediction = model.predict(text_features)[0]
    print(f"Should be: {should_be_label}")
    print(f"Predicted: {categories[prediction]}")
