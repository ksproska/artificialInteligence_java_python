import java.util.HashMap;

enum GridItemColor { WHITE, BLACK }
enum GridItemLetter { A, B, C, D, E, F, G, H }

class GridItem {
    public static HashMap<GridItemLetter, Integer> lettersIndexes = new HashMap<>() {
        {
            var counter = 0;
            for (var letter : GridItemLetter.values()) {
                put(letter, counter);
                counter++;
            }
        }
    };
    final GridItemColor gridItemColor;
    final GridItemLetter letter;
    final int number;
    final int rowId, columnId;

    GridItem(GridItemColor gridItemColor, GridItemLetter letter, int number, int rowId, int columnId) {
        this.gridItemColor = gridItemColor;
        this.letter = letter;
        this.number = number;
        this.rowId = rowId;
        this.columnId = columnId;
    }

    @Override
    public String toString() {
        return "" + letter + number /* + " - " + figure*/;
    }
}
