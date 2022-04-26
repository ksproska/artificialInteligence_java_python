import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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

class Move {
    final PlayerColor playerColor;
    final protected GridItem startingPoint;
    protected ArrayList<GridItem> toJumpItems;

    public Move(GridItem startingPoint) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
    }

    public GridItem getStartingPoint() {
        return startingPoint;
    }

    public Move(GridItem startingPoint, GridItem next) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
        toJumpItems.add(next);
    }

    public ArrayList<GridItem> getAllJumpedTo() {
        return new ArrayList<GridItem>(){{
            add(startingPoint);
            addAll(toJumpItems);
        }};
    }

    public void add(GridItem toJump) {
        toJumpItems.add(toJump);
    }

    @Override
    public String toString() {
        return "Move{ " + startingPoint + " -> " + toJumpItems + " }";
    }

    public Move copy() {
        var copied = new Move(startingPoint.copy());
        for (var toJump : toJumpItems) {
            copied.add(toJump.copy());
        }
        return copied;
    }
}

class Jump extends Move {
    protected ArrayList<GridItem> jumpOverItems;

    public Jump(GridItem startingPoint) {
        super(startingPoint);
        jumpOverItems = new ArrayList<>();
    }

    public void add(GridItem jumpOver, GridItem toJump) {
        jumpOverItems.add(jumpOver);
        toJumpItems.add(toJump);
    }

    public Jump shallowCopy() {
        var copied = new Jump(startingPoint);
        copied.jumpOverItems = new ArrayList<>(jumpOverItems);
        copied.toJumpItems = new ArrayList<>(toJumpItems);
        return copied;
    }

    @Override
    public Jump copy() {
        var copied = new Jump(startingPoint.copy());
        for (var item : toJumpItems) {
            copied.add(item.copy());
        }
        for (var item : jumpOverItems) {
            copied.jumpOverItems.add(item.copy());
        }
        return copied;
    }

    @Override
    public String toString() {
        return "Jump (" + size() + ") {" + startingPoint +
                "-> " + toJumpItems.stream().map(Object::toString).collect(Collectors.joining("-> ")) +
                " over: " + jumpOverItems +
                '}';
    }

    public boolean contains(GridItem toJump) {
        return toJumpItems.contains(toJump);
    }

    public int size() {
        return toJumpItems.size() + 1;
    }

    public boolean wasAlreadyJumpedOver(GridItem jumpedOver) {
        return jumpOverItems.contains(jumpedOver);
    }
}
