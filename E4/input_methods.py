import re


def get_books_with_any_genres_and_description(filename):
    splitted_lines = []
    with open(filename, encoding='utf8') as f:
        for line in f.readlines():
            line_splitted = line.split('\t')
            id = line_splitted[1]
            title = f"{line_splitted[2]}"
            jsonGenre = line_splitted[5]
            description = line_splitted[6]
            if jsonGenre != "" and description != "":
                genres = re.findall('\".*?\"', jsonGenre)[1::2]
                genres = [g[1:-1] for g in genres]
                splitted_lines.append([id, title, description, genres])
    return splitted_lines
