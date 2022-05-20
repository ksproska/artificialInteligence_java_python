from creating_models import get_model_with_vectorizer
from preprocessing_methods import description_preprocessing

model, vectorizer, categories = get_model_with_vectorizer()

sample_text = "Just after midnight, a snowdrift stops the Orient Express in its tracks as it travels through the " \
              "mountainous Balkans. The luxurious train is surprisingly full for the time of the year but, " \
              "by the morning, it is one passenger fewer. An American tycoon lies dead in his compartment, " \
              "stabbed a dozen times, his door locked from the inside. One of the passengers is none other than " \
              "detective Hercule Poirot. On vacation. Isolated and with a killer in their midst, Poirot must identify " \
              "the murderer—in case he or she decides to strike again "
should_be_label = "Crime"
preprocessed_text = description_preprocessing(sample_text)
text_features = vectorizer.transform([preprocessed_text]).toarray()
prediction = model.predict(text_features)[0]
print(f"Should be: {should_be_label}")
print(f"Predicted: {categories[prediction]}")
