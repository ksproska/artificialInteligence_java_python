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
# import nltk
# nltk.download('wordnet')
# import nltk
# nltk.download('omw-1.4')
# import nltk
# nltk.download('averaged_perceptron_tagger')
import nltk
from nltk.stem.wordnet import WordNetLemmatizer
# from pattern.text.en import singularize



def replace_all_with(text, replacement, *to_replace) -> str:
    for to_r in to_replace:
        text = text.replace(to_r, replacement)
    return text


def description_preprocessing(original_text: str):
    MODAL = 'MD'
    COMMA = ','
    DOT = '.'
    TO = 'TO'
    WHEN_ADVERB = 'WRB'
    NUMBER = 'CD'
    POS = 'POS' # '
    WHICH = 'WDT'
    PREPOSITION = 'PRP$'
    lemmatizer = WordNetLemmatizer()
    # text = replace_all_with(text, '',
    #                         '-\n', '\"', '\n', '\t', '.')
    # text = replace_all_with(text, " ",
    #                         ', ', '. ', ': ', ' # ', ' * ', ') ', ' (', '! ', ' \'', '\' ', '; ')
    # text = text.lower()
    # text = re.sub(" \d+ ", " ", text)
    non_repeating_words = {}
    tokenized_text = nltk.word_tokenize(original_text)
    for word_tokenized in nltk.pos_tag(tokenized_text):
        word = word_tokenized[0]
        word_type = word_tokenized[1]
        if word_type not in ['DT', 'IN', 'CC', 'PRP', NUMBER, WHEN_ADVERB, TO, MODAL, COMMA, DOT, POS, WHICH, PREPOSITION]:
            word_type_letter = word_type[0].lower()
            if word_type_letter in ['v', 'n', 'r']:
                word = lemmatizer.lemmatize(word, word_type_letter)
                # print(word)
            elif word_type_letter == 'j':
                word = lemmatizer.lemmatize(word, 'a')
            if all([letter.isalpha() or letter == '-' for letter in word]) and any([letter.isalpha() for letter in word]):
                word = word.lower()
                if non_repeating_words.get(word) is None:
                    non_repeating_words[word] = 1
                else:
                    non_repeating_words[word] += 1
    return non_repeating_words


