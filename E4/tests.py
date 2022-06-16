from sklearn.feature_selection import SelectKBest
from sklearn.feature_selection import chi2, f_regression
from sklearn.datasets import load_boston
from sklearn.datasets import load_iris
from numpy import array

iris = load_iris()
x = iris.data
y = iris.target

print("Feature data dimension: ", x.shape)

select = SelectKBest(score_func=chi2, k=3)
print(x)
print(y)
z = select.fit_transform(x, y)
print("After selecting best 3 features:", z.shape)