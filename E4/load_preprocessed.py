from preprocessing_classes import PreprocessingHandler
from preprocessing_methods import description_preprocessing

preprocessing_handler = PreprocessingHandler(description_preprocessing)
preprocessing_handler.load_file("normalised_genres_2022-05-17 21-43-09.909693.csv")
print(preprocessing_handler)
preprocessing_handler.load_preprocessed("each_word_normalized.csv")

