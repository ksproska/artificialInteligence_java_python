from datetime import datetime

import pandas as pd
# import nltk
# nltk.download('stopwords')
from nltk.corpus import stopwords

from preprocessing_classes import PreprocessingHandler
from preprocessing_methods import description_preprocessing

# preprocessing_handler = PreprocessingHandler(description_preprocessing)
# preprocessing_handler.load_file("normalised_genres_2022-05-17 21-43-09.909693.csv", 1, 4)
# preprocessing_handler.preprocess_all()
# preprocessing_handler.set_words_from_all_books()
# preprocessing_handler.display_for_words('kiss', 'magic', 'artificial', 'teen', 'strange')

stop_words = set(stopwords.words('english'))
df = pd.read_csv("normalised_genres.csv", encoding="utf-8", sep='\t')
categories = df.keys()[2:-1]

print(categories)
df['category'] = ""
df['categoryId'] = ""
id_nr = 0
for category in categories:
    df.loc[df[category] == 1, 'category'] = category
    df.loc[df[category] == 1, 'categoryId'] = id_nr
    id_nr += 1
    # df['category'] = df[category].map(lambda x: category if x == 1 else df['category'])
# print(df)
df = df.drop(columns=['id', 'title', *categories])
df = df[['categoryId', 'category', 'description']]
print(df)
# print(df['description'][0])
# df['description'] = df['description'].map(lambda com: " ".join(description_preprocessing(com)))
df['description'] = df['description'].map(lambda com: description_preprocessing(com))
print(df)
df.to_csv(f"preprocessed_genres_{str(datetime.now()).replace(':', '-')}.csv",
          encoding="utf-8", sep='\t', index=False)
