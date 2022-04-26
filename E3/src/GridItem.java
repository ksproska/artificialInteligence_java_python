import java.util.HashMap;

enum GridItemColor { WHITE, BLACK }
enum GridItemLetter { A, B, C, D, E, F, G, H }

enum PlayerColor { BLACK, WHITE }
enum FigureType { CROWNED, NORMAL }

class GridItem {
    public static HashMap<GridItemLetter, Integer> lettersIndexes = new HashMap<>(){
        {
            var counter = 0;
            for (var letter : GridItemLetter.values()) {
                put(letter, counter);
                counter ++;
            }
        }
    };

    final GridItemColor gridItemColor;
    final GridItemLetter letter;
    final int number;
    final int rowId, columnId;
    Figure figure;

    public GridItem copy() {
        var copied = new GridItem(gridItemColor, letter, number, rowId, columnId);
        copied.setFigure(figure);
        return copied;
    }

    GridItem(GridItemColor color, GridItemLetter letter, int number, int rowId, int columnId) {
        this.gridItemColor = color;
        this.letter = letter;
        this.number = number;
        this.rowId = rowId;
        this.columnId = columnId;
    }

    public Figure getFigure() {
        return figure;
    }

    public PlayerColor getPlayerColor() {
        return figure.playerColor;
    }

    public String getItem(boolean colored) {
        var gridItemElemToDisplay = "   ";
        if (figure != null) {
            gridItemElemToDisplay = " " + figure.getFigureDisplayChar() + " ";
        }

        if (colored) {
            if (figure != null  && figure.playerColor == PlayerColor.WHITE) {
                gridItemElemToDisplay = "\u001B[44m" + gridItemElemToDisplay + "\u001B[0m";
            }
            else {
                gridItemElemToDisplay = "\u001B[44;30m" + gridItemElemToDisplay + "\u001B[0m";
            }
        }
        else if(gridItemColor == GridItemColor.WHITE) {
            gridItemElemToDisplay = "\u001B[47;30m" + gridItemElemToDisplay + "\u001B[0m";
        }
        else if(figure != null && figure.playerColor == PlayerColor.BLACK) {
            gridItemElemToDisplay = "\u001B[30m" + gridItemElemToDisplay + "\u001B[0m";
        }

        return gridItemElemToDisplay;
    }

    @Override
    public String toString() {
        return "" + letter + number //+ "=(" + columnId + ", " + rowId + ")"
                ;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public boolean isEmpty() {
        return figure == null;
    }
}