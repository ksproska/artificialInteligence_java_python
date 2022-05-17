from pprint import pprint


class Book:
    def __init__(self, id, title, description, genres):
        self.id = id
        self.title = title
        self.description = description.replace('\n', '')
        self.genres = sorted(genres)

    def __str__(self):
        return f"{self.id} - {self.title}".ljust(40)[:40] + f"{self.genres}"

    def remove_genre(self, genre):
        self.genres = [x for x in self.genres if x != genre]

    def replace_genre(self, to_replace, replacement):
        self.remove_genre(to_replace)
        self.genres.append(replacement)

    @property
    def number_of_genres(self):
        return len(self.genres)

    def contains_genre(self, genre):
        return self.genres.count(genre) != 0

    def print_description(self):
        print(self.description)


class BookHandler:
    def __init__(self):
        self.book_collection: list[Book] = []
        self.genres_dict = {}

    def add_book(self, other: Book):
        self.book_collection.append(other)
        for genre in other.genres:
            if self.genres_dict.get(genre) is None:
                self.genres_dict[genre] = 1
            else:
                self.genres_dict[genre] += 1

    def remove_genre(self, genre):
        del self.genres_dict[genre]
        for book in self.book_collection:
            book.remove_genre(genre)
        self.book_collection = [x for x in self.book_collection if x.number_of_genres > 0]

    def remove_genre_if_count(self, condition):
        genres_to_remove = []
        for genre in self.genres_dict:
            if condition(self.genres_dict[genre]):
                genres_to_remove.append(genre)
        for genre in genres_to_remove:
            self.remove_genre(genre)

    def __str__(self):
        return f"Number of books: {len(self.book_collection)}\nAverage number of genres per book {sum([x.number_of_genres for x in self.book_collection])/len(self.book_collection)}" \
               f"\nGenres ({len(self.genres_dict)}) count:\n" \
               + "\n".join([f"{e[0].ljust(42)}{e[1]}" for e in sorted(self.genres_dict.items(), key=lambda item: item[1], reverse=True)])

    def write_to_csv(self, filename):
        with open(filename, "w", encoding="utf8") as f:
            f.write("sep=\t\n")
            f.write("id\ttitle\t")
            for genre in self.genres_dict:
                f.write(f"{genre}\t")
            f.write("description\n")
            for book in self.book_collection:
                f.write(book.id)
                f.write('\t' + book.title)
                for genre in self.genres_dict:
                    if book.contains_genre(genre):
                        f.write('\t1')
                    else:
                        f.write('\t0')
                f.write(f'\t{book.description}\n')
        print(f"Saved to {filename}")

    def print_similar_genres(self):
        simmilar_genres = {}
        for book in self.book_collection:
            if book.number_of_genres > 1:
                for i in range(len(book.genres) - 1):
                    for j in range(len(book.genres) - 1 - i):
                        g1 = book.genres[i]
                        g2 = book.genres[j + i + 1]
                        if simmilar_genres.get(g1) is None:
                            simmilar_genres[g1] = [g2]
                        else:
                            simmilar_genres[g1].append(g2)
                        if simmilar_genres.get(g2) is None:
                            simmilar_genres[g2] = [g1]
                        else:
                            simmilar_genres[g2].append(g1)
        for genre in simmilar_genres:
            simmilar_genres[genre] = sorted([(g, simmilar_genres[genre].count(g)) for g in set(simmilar_genres[genre])], key=lambda item: item[1], reverse=True)
        pprint(simmilar_genres)

    def merge_synonims(self, genre1, genre2):
        for book in self.book_collection:
            if book.contains_genre(genre2) and book.contains_genre(genre1):
                book.remove_genre(genre2)
            elif book.contains_genre(genre2):
                book.replace_genre(genre2, genre1)
                self.genres_dict[genre1] += 1
        del self.genres_dict[genre2]

    def print_books_with_more_than_x_genres(self, x):
        for book in self.book_collection:
            if book.number_of_genres > x:
                print(book)

    def remove_books_with_all_genres(self, *genres):
        for book in self.book_collection:
            if all([book.contains_genre(g) for g in genres]):
                for g in genres:
                    book.remove_genre(g)
            if book.number_of_genres == 0:
                for g in genres:
                    self.genres_dict[g] -= 1
                    if self.genres_dict[g] == 0:
                        del self.genres_dict[g]
        self.book_collection = [b for b in self.book_collection if b.number_of_genres > 0]

    def get_words_frequency_in_books(self):
        all_words_frequency = {}
        for book in self.book_collection:
            for word in book.words:
                if all_words_frequency.get(word) is None:
                    all_words_frequency[word] = 1
                else:
                    all_words_frequency[word] += 1
        all_words_frequency = {k: v for k, v in sorted(all_words_frequency.items(), key=lambda item: item[1])}
        return all_words_frequency

    def remove_books_with_description_less_than(self, x):
        books_to_remove = [b for b in self.book_collection if len(b.description) < x]
        for book in books_to_remove:
            for g in book.genres:
                self.genres_dict[g] -= 1
                if self.genres_dict[g] == 0:
                    del self.genres_dict[g]
        self.book_collection = [b for b in self.book_collection if len(b.description) >= x]
