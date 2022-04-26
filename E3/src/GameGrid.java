import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameGrid {
    ArrayList<ArrayList<GridItem>> fullGrid;
    ArrayList<Move> history;

    static class Move {
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
//        getGridItem("f6").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));

        getGridItem("d4").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        move("c1", "d2");
    }
    public void exampleSetup3() {
        getGridItem("b4").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("b6").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("e5").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
        getGridItem("d2").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
        getGridItem("h4").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));

        getGridItem("d8").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("h2").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));
        getGridItem("h8").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));

//        move("h4", "g3");
    }

    public PlayerColor getNextPlayer() {
        if (history.size() % 2 == 1) return PlayerColor.BLACK;
        return PlayerColor.WHITE;
    }

    public Move getLastMove() {
        if (history.isEmpty()) return null;
        return history.get(history.size() - 1);
    }

    @Override
    public String toString() {
        var fullStr = "";
        fullStr += "\nNEXT PLAYER: " + getNextPlayer() + "\n";

        var allJumpedTo = new ArrayList<GridItem>();
        if (getLastMove() != null) {
//            System.out.println(getLastMove().getAllJumpedTo());
            allJumpedTo = getLastMove().getAllJumpedTo();
        }
        for (var row : fullGrid) {
            fullStr += row.get(0).number + " ";
            for (var item : row) {
                boolean specialFlat = false;
                for (var special :
                        allJumpedTo) {
                    if (special.columnId == item.columnId && special.rowId == item.rowId) {
                        specialFlat = true;
                    }
                }
                fullStr += item.getItem(specialFlat);
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

    public ArrayList<GridItem> allPlayerFigures() {
        var allFigures = new ArrayList<GridItem>();
        for (var row : fullGrid) {
            for (var item : row) {
                if (!item.isEmpty() && item.figure.playerColor == getNextPlayer()) {
                    allFigures.add(item);
                }
            }
        }
        return allFigures;
    }

    public ArrayList<Move> getAllMoves() {
        var allPlayerFigures = allPlayerFigures();
        var allMoves = new ArrayList<Move>();
        var allJumps = new ArrayList<Jump>();
        for (var item : allPlayerFigures) {
            var obligatory = getObligatoryJumps(item);
            allJumps.addAll(obligatory);
        }

        if (!allJumps.isEmpty()) {
            var maxLen = allJumps.stream().max(Comparator.comparingInt(Jump::size)).get().size();
            for (var jump: allJumps) {
                if (jump.size() == maxLen && maxLen != 1) {
                    allMoves.add(jump);
                }
            }
        }


        if (allMoves.isEmpty()) {
            for (var item : allPlayerFigures) {
                var voluntaryMovements = getVoluntaryMovements(item);
                allMoves.addAll(voluntaryMovements);
            }
        }
        return allMoves;
    }

    public ArrayList<Move> getAllMoves(String shortcut) {
        var item = getGridItem(shortcut);
        var allMoves = new ArrayList<Move>();
        var obligatory = getObligatoryJumps(item);
        allMoves.addAll(obligatory);

        if (allMoves.isEmpty()) {
            var voluntaryMovements = getVoluntaryMovements(item);
            allMoves.addAll(voluntaryMovements);
        }
        return allMoves;
    }

    public void executeMove(Move move) {
        history.add(move.copy());

        var from = move.startingPoint;
        var to = move.toJumpItems.get(move.toJumpItems.size() - 1);

        to.setFigure(from.getFigure());
        if (to.number == 8 && from.getPlayerColor() == PlayerColor.WHITE) {
            to.getFigure().setFigureType(FigureType.CROWNED);
        }
        if (to.number == 1 && from.getPlayerColor() == PlayerColor.BLACK) {
            to.getFigure().setFigureType(FigureType.CROWNED);
        }
        from.setFigure(null);
        if (move.getClass() == Jump.class) {
            for (var toRemoveItems : ((Jump) move).jumpOverItems) {
                toRemoveItems.setFigure(null);
            }
        }
    }

    public void collectGridItems(GridItem gridItem, int rowOffset, int columnOffset, ArrayList<GridItem> acc) {
        var nextItem = getGridItem(gridItem.rowId + rowOffset, gridItem.columnId + columnOffset);
        if (nextItem != null && nextItem.isEmpty()) {
            acc.add(nextItem);
            collectGridItems(nextItem, rowOffset, columnOffset, acc);
        }
    }

    public ArrayList<Move> getVoluntaryMovements(GridItem item) {
        var directions = new ArrayList<int[]>(){
            {
                add(new int[]{1, 1});
                add(new int[]{1, -1});
                add(new int[]{-1, 1});
                add(new int[]{-1, -1});
            }
        };
        var allMoves = new ArrayList<Move>();
//        var item = fullGrid.get(getRow(gridItemId)).get(getColumn(gridItemId));
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
                    allMoves.add(new Move(item, move));
                }
            }
        }
        else {
            var allNext = new ArrayList<GridItem>();
            for (var dimension : directions) {
                collectGridItems(item, dimension[0], dimension[1], allNext);
            }
            for (var gridItem : allNext) {
                allMoves.add(new Move(item, gridItem));
            }
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

    public class Jump extends Move {
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

    public void getNormalJump(PlayerColor jumpingPlayer, GridItem item, Jump jump, ArrayList<Jump> allPaths) {
        var directions = new ArrayList<int[]>(){
            {
                add(new int[]{1, 1});
                add(new int[]{1, -1});
                add(new int[]{-1, 1});
                add(new int[]{-1, -1});
            }
        };
        var countPossibleDirections = 0;
        for (var direction : directions) {
            var gridItems = checkJumpInDirection(jumpingPlayer, item, direction[0], direction[1]);
            if (gridItems != null && !jump.contains(gridItems[1])
//            !jump.contains(direction[1]) && jump.getStartingPoint() != direction[1]
            ) {
                countPossibleDirections += 1;
                jump.add(gridItems[0], gridItems[1]);
                getNormalJump(jumpingPlayer, gridItems[1], jump.shallowCopy(), allPaths);
            }
        }
        if (countPossibleDirections == 0 && jump.size() > 1 && !allPaths.contains(jump)) {
            allPaths.add(jump);
        }
    }

    public static <T> ArrayList<T> getArrayListFromStream(Stream<T> stream)
    {
        List<T> list = stream.toList();
        return new ArrayList<T>(list);
    }

    public GridItem getNextToJumpOver(PlayerColor jumpingPlayer, GridItem item, int rowDir, int columnDir) {
        var nextItem = getGridItem(item.rowId + rowDir, item.columnId + columnDir);
//        System.out.println(nextItem);
        if (nextItem == null) {
            return null;
        }
        if (!nextItem.isEmpty()) {
            if (nextItem.figure.playerColor == jumpingPlayer) return null;
            return nextItem;
        }
        return getNextToJumpOver(jumpingPlayer, nextItem, rowDir, columnDir);
    }

    public ArrayList<GridItem> getPlacesToLand(GridItem prev, int rowDir, int columnDir, ArrayList<GridItem> acc) {
        var nextItem = getGridItem(prev.rowId + rowDir, prev.columnId + columnDir);
        if (nextItem == null) {
            return acc;
        }
        if (nextItem.isEmpty()) {
            acc.add(nextItem);
            getPlacesToLand(nextItem, rowDir, columnDir, acc);
        }
        return acc;
    }

    public ArrayList<Jump> getCrownedJumps(PlayerColor jumpingPlayer, GridItem item, Jump jump, ArrayList<Jump> allPaths) {
        var directions = new ArrayList<int[]>(){
            {
                add(new int[]{1, 1});
                add(new int[]{1, -1});
                add(new int[]{-1, 1});
                add(new int[]{-1, -1});
            }
        };
        for (var direction : directions) {
            var nextToJumpOver = getNextToJumpOver(jumpingPlayer, item, direction[0], direction[1]);
            if (nextToJumpOver != null && !jump.wasAlreadyJumpedOver(nextToJumpOver)) {
//                System.out.println(nextToJumpOver);
                var potentialToLand = getPlacesToLand(nextToJumpOver, direction[0], direction[1], new ArrayList<>());
//                System.out.println(potentialToLand);
                for (var potentialPlaceToLand : potentialToLand) {
                    if(!jump.contains(potentialPlaceToLand)) {
                        var copied = jump.shallowCopy();
                        copied.add(nextToJumpOver, potentialPlaceToLand);
                        allPaths.add(copied);
                        getCrownedJumps(jumpingPlayer, potentialPlaceToLand, copied, allPaths);
                    }
                }
            }
        }
        return allPaths;
    }

    public ArrayList<Jump> getObligatoryJumps(GridItem item) {
        var allJumps = new ArrayList<Jump>();
        if (item.figure == null) {
            return allJumps;
        }
//        allJumps.add(new Jump(item));
        if(item.figure.getFigureType() == FigureType.NORMAL) {
//            allJumps = new ArrayList<>();
            getNormalJump(item.figure.playerColor, item, new Jump(item), allJumps);

        }
        else {
            getCrownedJumps(item.figure.playerColor, item, new Jump(item), allJumps);
        }
        var ofMaxLength = new ArrayList<Jump>();
        if (allJumps.isEmpty()) return ofMaxLength;
        var maxLen = allJumps.stream().max(Comparator.comparingInt(Jump::size)).get().size();
//        System.out.println(maxLen);
        for (var jump: allJumps) {
            if (jump.size() == maxLen && maxLen != 1) {
                ofMaxLength.add(jump);
            }
        }
        return ofMaxLength;
    }

    public static void main(String[] args) {
        var grid = new GameGrid();
        grid.basicSetup();
        System.out.println(grid);

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ArrayList<Move> allMoves = grid.getAllMoves();
        while (!allMoves.isEmpty()) {
            try {
                for (int i = 0; i < allMoves.size(); i++) {
                    System.out.println(i + ": " + allMoves.get(i));
                }
                System.out.print("\n=> ");
                String nextMove = scanner.nextLine();
                if (nextMove.equals("")) {
                    Move selected = allMoves.get(random.nextInt(allMoves.size()));
                    System.out.println(selected);
                    grid.executeMove(selected);
                    System.out.println(grid);
                }
                    else {
                    grid.executeMove(allMoves.get(Integer.parseInt(nextMove)));
                    System.out.println(grid);
                }
                allMoves = grid.getAllMoves();
            }
            catch (Exception ignore) {}
        }
    }
}

class GameGridTest {
    @Test
    void displayTest() {
        var grid = new GameGrid();
//        grid.basicSetup();
        grid.exampleSetup2();
        System.out.println(grid);

        grid.executeMove(grid.getAllMoves().get(12));
        System.out.println(grid);
        System.out.println(grid.getAllMoves());
    }

    @Test
    void displayTest2() {
        var grid = new GameGrid();
//        grid.basicSetup();
        grid.exampleSetup3();
        System.out.println(grid);

        var nextMove = grid.getAllMoves("d2").get(1);
        grid.executeMove(nextMove);
        System.out.println(grid);

        nextMove = grid.getAllMoves("h2").get(0);
        grid.executeMove(nextMove);
        System.out.println(grid);
    }

    @Test
    void displayTest3() {


//        var nextMove = grid.getAllMoves("d2").get(1);
//        grid.executeMove(nextMove);
//        System.out.println(grid);
//
//        nextMove = grid.getAllMoves("h2").get(0);
//        grid.executeMove(nextMove);
//        System.out.println(grid);
    }
}
