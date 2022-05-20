import time
import numpy as np


class PreprocessingBook:
    OKBLUE = '\033[94m'
    ENDC = '\033[0m'
    OKCYAN = '\033[96m'

    def __init__(self, id, title, description, *genres):
        self.id = id
        self.title = title
        self.genres = genres
        self.description = description
        self.words_dictionary = {}

    def preprocess_description(self, preprocessing_fun):
        self.words_dictionary = preprocessing_fun(self.description)

    def get_colored_genres(self, joiner='\t'):
        return joiner.join([str(x) for x in self.genres]).replace("0", "◦").replace("1", self.OKBLUE + "X" + self.ENDC)

    def get_colored_genre_letters(self, letters, joiner='\t'):
        to_str = ""
        for i in range(len(self.genres)):
            if self.genres[i] == 1:
                to_str += self.OKBLUE + letters[i] + self.ENDC + joiner
            else:
                to_str += "◦" + joiner
        return to_str

    def __str__(self):
        return f"{self.id.ljust(10)} " + self.title.ljust(20)[:20] \
               + f"\t" + self.get_colored_genres() \
               + f"\t{self.description.ljust(40)[:40]}..."

    def get_colored_for_words(self, just_len, letters, joiner, *words):
        all_string = ""
        for word in words:
            if self.words_dictionary.get(word) is None:
                all_string += "◦".ljust(just_len)
            else:
                all_string += self.OKCYAN + word.ljust(just_len) + self.ENDC
        return self.title.ljust(20)[:20] + "  " + all_string + "\t" + self.get_colored_genre_letters(letters, joiner)

    def contains_word(self, word):
        return self.words_dictionary.get(word) is not None


class PreprocessingHandler:
    def __init__(self, preprocessing_fun):
        self.book_collection: list[PreprocessingBook] = []
        self.preprocessing_fun = preprocessing_fun
        self.genres = []
        self.all_words = {}

    def load_file(self, filename, part, num_of_parts):
        with open(filename, encoding='utf8') as f:
            f.readline()
            self.genres = f.readline().split('\t')[2:-1]
            lines = f.readlines()
            for line in np.array_split(lines, num_of_parts)[part - 1]:
                # print(line)
                splitted = line.split('\t')
                next_book = PreprocessingBook(*splitted[:2], splitted[-1], *[int(x) for x in splitted[2:-1]])
                self.add_book(next_book)

    def add_book(self, preprocessing_book):
        self.book_collection.append(preprocessing_book)

    def preprocess_all(self):
        print("Processing all book descriptions...")
        start = time.time()
        counter = 0
        refresh_rate = 80
        print('_' * int(len(self.book_collection) / refresh_rate))
        for book in self.book_collection:
            book.preprocess_description(self.preprocessing_fun)
            counter += 1
            if counter % refresh_rate == 0:
                print('»', end='')
        end = time.time()
        print(f"\nProcessing finished after {(end - start)/60} minutes")

    def print_loading(self, counter):
        print(f"{counter}/{len(self.book_collection)}\t" + "*" * (int(60 * counter / len(self.book_collection)))
              + "-" * (int(60 * (len(self.book_collection) - counter) / len(self.book_collection))))

    def set_words_from_all_books(self):
        # counter = 0
        # print('-' * 60)
        for book in self.book_collection:
            for word in book.words_dictionary:
                if self.all_words.get(word) is None:
                    self.all_words[word] = 1
                else:
                    self.all_words[word] += 1
        self.all_words = {k: v for k, v in sorted(self.all_words.items(), key=lambda item: item[1], reverse=True)}
            # counter += 1
            # if counter % 200 == 0:
            #     print('»', end='')

    def __str__(self):
        return f"Number of books: {len(self.book_collection)}\n" + "\n".join([f"{g[0]} = {g}" for g in self.genres]) + "\n" + "id".ljust(10) + " title".ljust(22) \
               + "\t".join([g[0] for g in self.genres]) + "\t description" + "\n" + "\n".join([str(x) for x in self.book_collection]) + "\n"

    def get_words_with_if(self, string_fun):
        words = []
        for word in self.all_words:
            if string_fun(self.all_words[word]):
                words.append(word)
        return words

    def display_for_words(self, *words):
        print(f"Number of books: {len(self.book_collection)}\n" + "\n".join([f"{g[0]} = {g}" for g in self.genres]))
        max_len = max([len(w) for w in words]) + 2
        print((' '* 22 + ''.join([str(w).ljust(max_len) for w in words])).ljust(22 + max_len*len(words)) + '  '
              + " ".join([g[0] for g in self.genres]))
        for book in self.book_collection:
            print(book.get_colored_for_words(max_len, [g[0] for g in self.genres], ' ', *words))

    def write_to_csv(self, filename):
        print(f"Saving to {filename}...")
        start = time.time()
        counter = 0
        refresh_rate = 80
        print('_' * int(len(self.book_collection) / refresh_rate))
        with open(filename, "w", encoding="utf8") as f:
            f.write("sep=\t\n")
            f.write("id\ttitle\t")
            for word in self.all_words:
                f.write(f"{word}\t")
            f.write("description\n")
            for book in self.book_collection:
                f.write(book.id)
                f.write('\t' + book.title)
                for word in self.all_words:
                    if book.contains_word(word):
                        f.write('\t1')
                    else:
                        f.write('\t0')
                counter += 1
                if counter % refresh_rate == 0:
                    print('»', end='')
        end = time.time()
        print(f"\nProcessing finished after {(end - start) / 60} minutes")

    def load_preprocessed(self, filename):
        print(f"Loading preprocessed from {filename}...")
        start = time.time()
        counter = 0
        refresh_rate = 80
        print('_' * int(len(self.book_collection) / refresh_rate))
        with open(filename, encoding='utf8') as f:
            f.readline()
            words = f.readline().replace('\n', '').split('\t')[2:]
            for word in words:
                self.all_words[word] = 0
            for line in f:
                is_word = line.replace('\n', '').split('\t')
                for i in range(len(is_word)):
                    if is_word == '1':
                        word = words[i]
                        self.all_words[word] += 1
                counter += 1
                if counter % refresh_rate == 0:
                    print('»', end='')
            end = time.time()
            print(f"\nProcessing finished after {(end - start) / 60} minutes")
