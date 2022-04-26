import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameGrid {
    ArrayList<ArrayList<GridItem>> fullGrid;
    ArrayList<Move> history;

    static class Move {
        final String from, to;
        final PlayerColor playerColor;

        Move(String from, String to, PlayerColor playerColor) {
            this.from = from;
            this.to = to;
            this.playerColor = playerColor;
        }

        @Override
        public String toString() {
            return from + " -> " + to + ";" + playerColor;
        }
    }

    public GameGrid() {
        history = new ArrayList<>();
        fullGrid = new ArrayList<>();
        var nextColor = GridItemColor.WHITE;
        int gridSize = 8;
        for (int i = gridSize; i > 0; i--) {
            var newList = new ArrayList<GridItem>();
            int counter = 0;
            for (var letter : GridItemLetter.values()) {
                newList.add(new GridItem(nextColor, letter, i, gridSize - i, counter));
                counter ++;
                if(nextColor == GridItemColor.BLACK) {
                    nextColor = GridItemColor.WHITE;
                }
                else {
                    nextColor = GridItemColor.BLACK;
                }
            }
            fullGrid.add(newList);
            if(nextColor == GridItemColor.BLACK) {
                nextColor = GridItemColor.WHITE;
            }
            else {
                nextColor = GridItemColor.BLACK;
            }
        }
    }

    public void basicSetup() {
        for (var row : new int[]{1, 2}) {
            for (var item : fullGrid.get(row - 1)) {
                if(item.gridItemColor == GridItemColor.BLACK) {
                    item.setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
                }
            }
        }
        for (var row : new int[]{7, 8}) {
            for (var item : fullGrid.get(row - 1)) {
                if(item.gridItemColor == GridItemColor.BLACK) {
                    item.setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
                }
            }
        }
    }

    public void exampleSetup2() {
        getGridItem("c1").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("b2").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("b6").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
        getGridItem("d6").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("f8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));

        getGridItem("a3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("h6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("f6").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));

        getGridItem("d4").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
    }

    public PlayerColor getNextPlayer() {
        if (history.size() % 2 == 1) return PlayerColor.BLACK;
        return PlayerColor.WHITE;
    }

    @Override
    public String toString() {
        var fullStr = "";
        fullStr += "\nNEXT PLAYER: " + getNextPlayer() + "\n";

        for (var row : fullGrid) {
            fullStr += row.get(0).number + " ";
            for (var item : row) {
                fullStr += item.getItem();
            }
            fullStr += '\n';
        }
        fullStr += "  ";
        for (var item : fullGrid.get(0)) {
            fullStr += " " + item.letter + " ";
        }
        return fullStr;
    }

    public int getRow(String shortcut) {
        return GridItemLetter.values().length - Integer.parseInt(shortcut.split("")[1]);
    }

    public int getColumn(String shortcut) {
        var letter = shortcut.split("")[0];
        var chosenLetter = GridItemLetter.valueOf(letter.toUpperCase(Locale.ROOT));
        return GridItem.lettersIndexes.get(chosenLetter);
    }

    public GridItem getGridItem(int rowId, int columnId) {
        if (rowId < 0 || columnId < 0 || rowId >= fullGrid.size() || columnId >= GridItem.lettersIndexes.size()) {
            return null;
        }
        return fullGrid.get(rowId).get(columnId);
    }
    public GridItem getGridItem(String shortcut) {
        var rowId = getRow(shortcut);
        var columnId = getColumn(shortcut);
        return fullGrid.get(rowId).get(columnId);
    }

    public void move(String from, String to) {
        var item1 = fullGrid.get(getRow(from)).get(getColumn(from));
        var item2 = fullGrid.get(getRow(to)).get(getColumn(to));

        System.out.println(item1);
        System.out.println(item2);

        if(item2.getFigure() == null && item1.getFigure() != null) {
            item2.setFigure(item1.getFigure());
            item1.setFigure(null);
            history.add(new Move(from, to, item2.getPlayerColor()));
        }
    }

    public void collectGridItems(GridItem gridItem, int rowOffset, int columnOffset, ArrayList<GridItem> acc) {
        var nextItem = getGridItem(gridItem.rowId + rowOffset, gridItem.columnId + columnOffset);
        if (nextItem != null && nextItem.isEmpty()) {
            acc.add(nextItem);
            collectGridItems(nextItem, rowOffset, columnOffset, acc);
        }
    }

    public ArrayList<GridItem> getVoluntaryMovements(String gridItemId) {
        var allMoves = new ArrayList<GridItem>();
        var item = fullGrid.get(getRow(gridItemId)).get(getColumn(gridItemId));
        if (item.figure == null) {
            return allMoves;
        }
        if(item.figure.getFigureType() == FigureType.NORMAL) {
            var voluntaryMoves = new ArrayList<GridItem>();
            if (item.figure.playerColor == PlayerColor.WHITE) {
                voluntaryMoves.add(getGridItem(item.rowId - 1, item.columnId - 1));
                voluntaryMoves.add(getGridItem(item.rowId - 1, item.columnId + 1));
            }
            else {
                voluntaryMoves.add(getGridItem(item.rowId + 1, item.columnId - 1));
                voluntaryMoves.add(getGridItem(item.rowId + 1, item.columnId + 1));
            }
            for (var move : voluntaryMoves) {
                if(move != null && move.isEmpty()) {
                    allMoves.add(move);
                }
            }
        }
        else {
            collectGridItems(item, 1, 1, allMoves);
            collectGridItems(item, 1, -1, allMoves);
            collectGridItems(item, -1, 1, allMoves);
            collectGridItems(item, -1, -1, allMoves);
        }
        return allMoves;
    }

    public GridItem[] checkJumpInDirection(PlayerColor jumpingPlayer, GridItem sourceItem, int rowDir, int columnDir) {
        var multiplier = 2;
        var jumpOver = getGridItem(sourceItem.rowId + rowDir, sourceItem.columnId + columnDir);
        if (jumpOver == null || jumpOver.isEmpty() || jumpOver.figure.playerColor == jumpingPlayer) return null;
        var toJump = getGridItem(sourceItem.rowId + rowDir * multiplier, sourceItem.columnId + columnDir * multiplier);
        if (toJump != null && toJump.isEmpty()) {
            return new GridItem[]{jumpOver, toJump};
        }
        return null;
    }

    public class Jump {
        final private GridItem startingPoint;
        private ArrayList<GridItem> toJumpItems;
        private ArrayList<GridItem> jumpOverItems;

        public Jump(GridItem startingPoint) {
            this.startingPoint = startingPoint;
            toJumpItems = new ArrayList<>();
            jumpOverItems = new ArrayList<>();
        }

        public void add(GridItem jumpOver, GridItem toJump) {
            jumpOverItems.add(jumpOver);
            toJumpItems.add(toJump);
        }

        public Jump copy() {
            var copied = new Jump(startingPoint);
            copied.jumpOverItems = new ArrayList<>(jumpOverItems);
            copied.toJumpItems = new ArrayList<>(toJumpItems);
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
    }

    public void getNormalJump(PlayerColor jumpingPlayer, GridItem item, Jump currentPath, ArrayList<Jump> allPaths) {
//        currentPath.add(item);
        var allDirectionsToCheck = new ArrayList<GridItem[]>();
        var d1 = checkJumpInDirection(jumpingPlayer, item, 1, 1);
        if (d1 != null && !currentPath.contains(d1[1])) { allDirectionsToCheck.add(d1); }
        d1 = checkJumpInDirection(jumpingPlayer, item, 1, -1);
        if (d1 != null && !currentPath.contains(d1[1])) { allDirectionsToCheck.add(d1); }
        d1 = checkJumpInDirection(jumpingPlayer, item, -1, 1);
        if (d1 != null && !currentPath.contains(d1[1])) { allDirectionsToCheck.add(d1); }
        d1 = checkJumpInDirection(jumpingPlayer, item, -1, -1);
        if (d1 != null && !currentPath.contains(d1[1])) { allDirectionsToCheck.add(d1); }
        for (var direction : allDirectionsToCheck) {
            currentPath.add(direction[0], direction[1]);
            getNormalJump(jumpingPlayer, direction[1], currentPath.copy(), allPaths);
        }
        if (currentPath.size() > 1) {
            allPaths.add(currentPath);
        }
    }

    public static <T> ArrayList<T> getArrayListFromStream(Stream<T> stream)
    {
        List<T> list = stream.toList();
        return new ArrayList<T>(list);
    }

    public ArrayList<Jump> getObligatoryJumps(String gridItemId) {
        var allJumps = new ArrayList<Jump>();
        var item = fullGrid.get(getRow(gridItemId)).get(getColumn(gridItemId));
        if (item.figure == null) {
            return allJumps;
        }
        if(item.figure.getFigureType() == FigureType.NORMAL) {
            allJumps.add(new Jump(item));
            getNormalJump(item.figure.playerColor, item, allJumps.get(0), allJumps);
            var maxLen = allJumps.stream().max(Comparator.comparingInt(Jump::size)).get().size();
            var ofMaxLength = new ArrayList<Jump>();
            for (var jump: allJumps) {
                if (jump.size() == maxLen) {
                    ofMaxLength.add(jump);
                }
            }
            return ofMaxLength;
        }
        else {

        }
        return allJumps;
    }
}

class GameGridTest {
    @Test
    void displayTest() {
        var grid = new GameGrid();
//        grid.basicSetup();
        grid.exampleSetup2();
        System.out.println(grid);
//        grid.move("b6", "d4");
        grid.move("c1", "d2");
        System.out.println(grid);
        System.out.println(grid.getVoluntaryMovements("a3"));
        System.out.println(grid.getObligatoryJumps("a3"));
    }
}
