'''
Tag	Meaning	Examples
ADJ	adjective	new, good, high, special, big, local
ADV	adverb	really, already, still, early, now
CNJ	conjunction	and, or, but, if, while, although
DT	determiner	the, a, some, most, every, no
EX	existential	there, there's
FW	foreign word	dolce, ersatz, esprit, quo, maitre
MOD	modal verb	will, can, would, may, must, should
N	noun	year, home, costs, time, education
NP	proper noun	Alison, Africa, April, Washington
NUM	number	twenty-four, fourth, 1991, 14:24
PRO	pronoun	he, their, her, its, my, I, us
P	preposition	on, of, at, with, by, into, under
TO	the word to	to
UH	interjection	ah, bang, ha, whee, hmpf, oops
V	verb	is, has, get, do, make, see, run
VD	past tense	said, took, told, made, asked
VG	present participle	making, going, playing, working
VN	past participle	given, taken, begun, sung
WH	wh determiner	who, which, when, what, where, how
'''

import re
import nltk
# nltk.download('wordnet')
# nltk.download('omw-1.4')
# nltk.download('averaged_perceptron_tagger')
from nltk.stem.wordnet import WordNetLemmatizer
# from pattern.text.en import singularize


def replace_all_with(text, replacement, *to_replace) -> str:
    for to_r in to_replace:
        text = text.replace(to_r, replacement)
    return text


lemmatizer = WordNetLemmatizer()


def description_preprocessing(original_text: str) -> str:
    # text elements that should be excluded from text since they have no impact on the content
    MODAL = 'MD'
    COMMA = ','
    DOT = '.'
    TO = 'TO'
    WHEN_ADVERB = 'WRB'
    NUMBER = 'CD'
    POS = 'POS' # '
    WHICH = 'WDT'
    PREPOSITION = 'PRP$'
    DT = 'DT'
    IN = 'IN'
    CC = 'CC'
    PRP = 'PRP'
    repeating_words = []
    tokenized_text = nltk.word_tokenize(original_text)
    for word_tokenized in nltk.pos_tag(tokenized_text):
        word = word_tokenized[0]
        word_type = word_tokenized[1]
        if word_type not in [DT, IN, CC, PRP, NUMBER, WHEN_ADVERB,
                             TO, MODAL, COMMA, DOT, POS, WHICH, PREPOSITION]:
            word_type_letter = word_type[0].lower()
            if word_type_letter in ['v', 'n', 'r']: # verb, noun, adverb
                word = lemmatizer.lemmatize(word, word_type_letter)
            elif word_type_letter == 'j':           # adjective
                word = lemmatizer.lemmatize(word, 'a')
            if all([letter.isalpha() or letter == '-' for letter in word]) \
                    and any([letter.isalpha() for letter in word]):
                word = word.lower()
                repeating_words.append(word)
    return " ".join(repeating_words)
