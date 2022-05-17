from input_classes import BookHandler, Book
from input_methods import get_books_with_any_genres_and_description


if __name__ == '__main__':
    splitted_lines = get_books_with_any_genres_and_description("booksummaries/booksummaries.txt")
    for line in splitted_lines[:1]:
        next_book = Book(*line)
        print(next_book.description)