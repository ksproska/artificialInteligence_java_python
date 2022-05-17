from preprocessing_classes import PreprocessingHandler
from preprocessing_methods import description_preprocessing

preprocessing_handler = PreprocessingHandler(description_preprocessing)
preprocessing_handler.load_file("normalised_genres_2022-05-17 21-43-09.909693.csv")
print(preprocessing_handler)
preprocessing_handler.preprocess_all()
preprocessing_handler.set_words_from_all_books()
# words = preprocessing_handler.get_words_with_if(lambda x: 1000 > x)[10:20]
# preprocessing_handler.display_for_words(*words)
preprocessing_handler.display_for_words('kiss', 'magic', 'artificial', 'teen', 'strange')
# preprocessing_handler.write_to_csv("each_word_normalized.csv")
