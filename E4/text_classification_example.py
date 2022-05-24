from creating_models import get_model_with_vectorizer
from preprocessing_methods import description_preprocessing

model, vectorizer, categories = get_model_with_vectorizer()

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
