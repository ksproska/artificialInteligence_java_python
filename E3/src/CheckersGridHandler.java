import java.util.*;

class CheckersGrid {
    private static final ArrayList<int[]> allPossibleDirections = new ArrayList<int[]>(){
        {
            add(new int[]{1, 1});
            add(new int[]{1, -1});
            add(new int[]{-1, 1});
            add(new int[]{-1, -1});
        }
    };
    private static ArrayList<ArrayList<GridItem>> board;
    private ArrayList<ArrayList<Figure>> figures;
    private ArrayList<Move> allCurrentPossibleMoves;
    private PlayerColor playerColor;

    public CheckersGrid() {
        figures = new ArrayList<>();
        int gridSize = 8;
        for (int i = gridSize; i > 0; i--) {
            var emptyList = new ArrayList<Figure>();
            for (int j = 0; j < gridSize; j++) {
                emptyList.add(null);
            }
            figures.add(emptyList);
        }
        allCurrentPossibleMoves = new ArrayList<>();
        if (board == null) {
            board = new ArrayList<>();
            var nextColor = GridItemColor.WHITE;
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
                board.add(newList);
                if(nextColor == GridItemColor.BLACK) {
                    nextColor = GridItemColor.WHITE;
                }
                else {
                    nextColor = GridItemColor.BLACK;
                }
            }
            playerColor = PlayerColor.WHITE;
        }
    }

    public CheckersGrid(ArrayList<ArrayList<Figure>> figures, ArrayList<Move> allCurrentPossibleMoves, PlayerColor playerColor) {
        this.figures = new ArrayList<>();
        for (var row : figures) {
            var tempArray = new ArrayList<Figure>(row);
            this.figures.add(tempArray);
        }
        this.allCurrentPossibleMoves = new ArrayList<>(allCurrentPossibleMoves);
        this.playerColor = playerColor;
    }

    public PlayerColor getOppositePlayer(PlayerColor color) {
        return switch (color) {
            case WHITE -> PlayerColor.BLACK;
            case BLACK -> PlayerColor.WHITE;
        };
    }

    public CheckersGrid copy() {
        return new CheckersGrid(figures, allCurrentPossibleMoves, playerColor);
    }

    public ArrayList<Figure> getAllFilledItems() {
        var all = new ArrayList<Figure>();
        for (var row : figures) {
            for (var item : row) {
                if (item != null) {
                    all.add(item);
                }
            }
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getRow(String shortcut) {
        return GridItemLetter.values().length - Integer.parseInt(shortcut.split("")[1]);
    }
    public int getColumn(String shortcut) {
        var letter = shortcut.split("")[0];
        var chosenLetter = GridItemLetter.valueOf(letter.toUpperCase(Locale.ROOT));
        return GridItem.lettersIndexes.get(chosenLetter);
    }

    public GridItem getGridItem(int rowId, int columnId) {
        if (rowId < 0 || columnId < 0 || rowId >= board.size() || columnId >= GridItem.lettersIndexes.size()) {
            return null;
        }
        return board.get(rowId).get(columnId);
    }

    public GridItem getGridItem(String shortcut) {
        var rowId = getRow(shortcut);
        var columnId = getColumn(shortcut);
        return board.get(rowId).get(columnId);
    }

    public void setAllCurrentPossibleMoves() {
        allCurrentPossibleMoves = getAllMoves();
    }

    public ArrayList<Move> getAllCurrentPossibleMoves() {
        if (allCurrentPossibleMoves.isEmpty()) {
            setAllCurrentPossibleMoves();
        }
        return allCurrentPossibleMoves;
    }

    public void basicSetup() {
        playerColor = PlayerColor.WHITE;
        var figure = new Figure(PlayerColor.BLACK, FigureType.NORMAL);
        for (var row : new int[]{1, 2}) {
            for (int i = 0; i < figures.get(row - 1).size(); i++) {
                var item = board.get(row - 1).get(i);
                if (item.gridItemColor == GridItemColor.BLACK) {
                    figures.get(row - 1).set(i, figure);
                }
            }
        }
        figure = new Figure(PlayerColor.WHITE, FigureType.NORMAL);
        for (var row : new int[]{7, 8}) {
            for (int i = 0; i < figures.get(row - 1).size(); i++) {
                var item = board.get(row - 1).get(i);
                if (item.gridItemColor == GridItemColor.BLACK) {
                    figures.get(row - 1).set(i, figure);
                }
            }
        }
//        setAllCurrentPossibleMoves();
    }

//    public void exampleSetup2() {
//        getGridItem("c1").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("b2").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("b6").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//        getGridItem("d6").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("f8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//
//        getGridItem("a3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("h6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
////        getGridItem("f6").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));
//
//        getGridItem("d4").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        setAllCurrentPossibleMoves();
////        move("c1", "d2");
//    }
//    public void exampleSetup3() {
//        getGridItem("b4").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("b6").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("e5").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//        getGridItem("d2").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//        getGridItem("h4").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//
//        getGridItem("d8").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("h2").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));
//        getGridItem("h8").setFigure(new Figure(PlayerColor.BLACK, FigureType.CROWNED));
//        setAllCurrentPossibleMoves();
////        move("h4", "g3");
//    }
//    public void exampleSetup4() {
//        getGridItem("b8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//        getGridItem("f4").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//
//        getGridItem("b6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("d6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        setAllCurrentPossibleMoves();
//    }
//    public void exampleSetup5() {
//        getGridItem("e5").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//
//        getGridItem("b6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("d6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("d2").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("d4").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("f4").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        setAllCurrentPossibleMoves();
//    }
//    public void exampleSetup6() {
//        getGridItem("h8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
//
//        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("c5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("c7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        setAllCurrentPossibleMoves();
//    }
//    public void exampleSetup7() {
//        getGridItem("h8").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
//
//        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("c5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("c7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("e7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
//        setAllCurrentPossibleMoves();
//    }


    private ArrayList<Move> getAllMoves() {
        var allMoves = new ArrayList<Move>();
        var allJumps = new ArrayList<Jump>();
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                var figure = figures.get(i).get(j);
                if (figure != null && figure.playerColor == playerColor) {
                    var item = board.get(i).get(j);
                    var obligatory = getObligatoryJumps(item);
                    allJumps.addAll(obligatory);
                }
            }
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
            for (int i = 0; i < board.size(); i++) {
                for (int j = 0; j < board.get(i).size(); j++) {
                    var figure = figures.get(i).get(j);
                    if (figure != null && figure.playerColor == playerColor) {
                        var item = board.get(i).get(j);
                        var voluntaryMovements = getVoluntaryMovements(item);
                        allMoves.addAll(voluntaryMovements);
                    }
                }
            }
        }
        return allMoves;
    }

    public void setFigure(GridItem item, Figure figure) {
        figures.get(item.rowId).set(item.columnId, figure);
    }
    public void setFigure(GridItem item, FigureType figureType, PlayerColor playerColor) {
        figures.get(item.rowId).set(item.columnId, new Figure(playerColor, figureType));
    }

    public void executeMove(Move move) {
        var from = move.startingPoint;
        var to = move.toJumpItems.get(move.toJumpItems.size() - 1);
        var figureFrom = getFigure(from);
        figures.get(to.rowId).set(to.columnId, figureFrom);
        if (to.number == 8 && figureFrom.playerColor == PlayerColor.WHITE) {
            setFigure(to, FigureType.CROWNED, PlayerColor.WHITE);
        }
        if (to.number == 1 && figureFrom.playerColor == PlayerColor.BLACK) {
            setFigure(to, FigureType.CROWNED, PlayerColor.BLACK);
        }
        figures.get(from.rowId).set(from.columnId, null);
        if (move.getClass() == Jump.class) {
            for (var toRemoveItems : ((Jump) move).jumpOverItems) {
                figures.get(toRemoveItems.rowId).set(toRemoveItems.columnId, null);
            }
        }
        this.playerColor = getOppositePlayer(this.playerColor);
//        setAllCurrentPossibleMoves();
        allCurrentPossibleMoves = new ArrayList<>();
    }

    public boolean isItemEmpty(GridItem gridItem) {
        return figures.get(gridItem.rowId).get(gridItem.columnId) == null;
    }
    public Figure getFigure(GridItem gridItem) {
        return figures.get(gridItem.rowId).get(gridItem.columnId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void getPlacesToMoveCrownedInDirection(GridItem gridItemSnapshot, int rowOffset, int columnOffset, ArrayList<GridItem> acc) {
        var nextItem = getGridItem(gridItemSnapshot.rowId + rowOffset, gridItemSnapshot.columnId + columnOffset);
        if (nextItem != null && isItemEmpty(nextItem)) {
            acc.add(nextItem);
            getPlacesToMoveCrownedInDirection(nextItem, rowOffset, columnOffset, acc);
        }
    }

    private GridItem[] getNormalJumpInDirection(PlayerColor jumpingPlayer, GridItem sourceItem, int rowDir, int columnDir) {
        var multiplier = 2;
        var jumpOver = getGridItem(sourceItem.rowId + rowDir, sourceItem.columnId + columnDir);
        if (jumpOver == null || isItemEmpty(jumpOver) || getFigure(jumpOver).playerColor == jumpingPlayer) return null;
        var toJump = getGridItem(sourceItem.rowId + rowDir * multiplier, sourceItem.columnId + columnDir * multiplier);
        if (toJump != null && isItemEmpty(toJump)) {
            return new GridItem[]{jumpOver, toJump};
        }
        return null;
    }

    private GridItem getNextToCrownedJumpOver(PlayerColor jumpingPlayer, GridItem item, int rowDir, int columnDir) {
        var nextItem = getGridItem(item.rowId + rowDir, item.columnId + columnDir);
        if (nextItem == null) { return null; }
        if (!isItemEmpty(nextItem)) {
            if (getFigure(nextItem).playerColor == jumpingPlayer) return null;
            return nextItem;
        }
        return getNextToCrownedJumpOver(jumpingPlayer, nextItem, rowDir, columnDir);
    }

    private ArrayList<GridItem> getPlacesToCrownedLand(GridItem prev, int rowDir, int columnDir, ArrayList<GridItem> acc) {
        var nextItem = getGridItem(prev.rowId + rowDir, prev.columnId + columnDir);
        if (nextItem == null) { return acc; }
        if (isItemEmpty(nextItem)) {
            acc.add(nextItem);
            getPlacesToCrownedLand(nextItem, rowDir, columnDir, acc);
        }
        return acc;
    }

    private void getAllNormalJumps(PlayerColor jumpingPlayer, GridItem item, Jump jump, ArrayList<Jump> allPaths) {
        var countPossibleDirections = 0;
        for (var direction : allPossibleDirections) {
            var gridItems = getNormalJumpInDirection(jumpingPlayer, item, direction[0], direction[1]);
            if (gridItems != null && !jump.wasAlreadyJumpedOver(gridItems[0])) {
                countPossibleDirections += 1;
                var copied = jump.shallowCopy();
                copied.add(gridItems[0], gridItems[1]);
                getAllNormalJumps(jumpingPlayer, gridItems[1], copied, allPaths);
            }
        }
        if (countPossibleDirections == 0 && jump.size() > 1 && !allPaths.contains(jump)) {
            allPaths.add(jump);
        }
    }

    private void getAllCrownedJumps(PlayerColor jumpingPlayer, GridItem item, Jump jump, ArrayList<Jump> allPaths) {
        for (var direction : allPossibleDirections) {
            var nextToJumpOver = getNextToCrownedJumpOver(jumpingPlayer, item, direction[0], direction[1]);
            if (nextToJumpOver != null && !jump.wasAlreadyJumpedOver(nextToJumpOver)) {
                var potentialToLand = getPlacesToCrownedLand(nextToJumpOver, direction[0], direction[1], new ArrayList<>());
                for (var potentialPlaceToLand : potentialToLand) {
                    var copied = jump.shallowCopy();
                    copied.add(nextToJumpOver, potentialPlaceToLand);
                    allPaths.add(copied);
                    getAllCrownedJumps(jumpingPlayer, potentialPlaceToLand, copied, allPaths);
//                    }
                }
            }
        }
    }

    private ArrayList<Jump> getObligatoryJumps(GridItem item) {
        var allJumps = new ArrayList<Jump>();
        if (getFigure(item) == null) { return allJumps; }

        switch (getFigure(item).figureType) {
            case NORMAL -> getAllNormalJumps(getFigure(item).playerColor, item, new Jump(FigureType.NORMAL, item), allJumps);
            case CROWNED -> getAllCrownedJumps(getFigure(item).playerColor, item, new Jump(FigureType.CROWNED, item), allJumps);
        }
        return allJumps;
    }

    private ArrayList<Move> getVoluntaryMovements(GridItem item) {
        var allMoves = new ArrayList<Move>();
        if (getFigure(item) == null) { return allMoves; }

        switch (getFigure(item).figureType) {
            case NORMAL -> {
                var voluntaryMoves = new ArrayList<GridItem>();
                switch (getFigure(item).playerColor) {
                    case WHITE -> {
                        voluntaryMoves.add(getGridItem(item.rowId - 1, item.columnId - 1));
                        voluntaryMoves.add(getGridItem(item.rowId - 1, item.columnId + 1));
                    }
                    case BLACK -> {
                        voluntaryMoves.add(getGridItem(item.rowId + 1, item.columnId - 1));
                        voluntaryMoves.add(getGridItem(item.rowId + 1, item.columnId + 1));
                    }
                }
                for (var move : voluntaryMoves) {
                    if(move != null && isItemEmpty(move)) { allMoves.add(new Move(item, move, FigureType.NORMAL)); }
                }
            }
            case CROWNED -> {
                var allNext = new ArrayList<GridItem>();
                for (var dimension : allPossibleDirections) {
                    getPlacesToMoveCrownedInDirection(item, dimension[0], dimension[1], allNext);
                }
                for (var gridItem : allNext) {
                    allMoves.add(new Move(item, gridItem, FigureType.CROWNED));
                }
            }
        }
        return allMoves;
    }
    // -----------------------------------------------------------------------------------------------------------------

    public ArrayList<ArrayList<GridItem>> getFullGrid() { return board; }
}

public class CheckersGridHandler {
    CheckersGrid checkersGrid;
    ArrayList<Move> history;

    public void basicSetup() {
        checkersGrid.basicSetup();
    }

    public CheckersGrid getCheckersGrid() {
        return checkersGrid;
    }

    public void executeMove(Move move) {
        history.add(move);
        checkersGrid.executeMove(move);
    }

    public ArrayList<Move> getAllCurrentPossibleMoves() {
        return checkersGrid.getAllCurrentPossibleMoves();
    }


    public CheckersGridHandler() {
        checkersGrid = new CheckersGrid();
        history = new ArrayList<>();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public PlayerColor getCurrentPlayer() {
        if (history.size() % 2 == 1) return PlayerColor.BLACK;
        return PlayerColor.WHITE;
    }
    public PlayerColor getPrevPlayer() {
        if (history.size() % 2 == 0) return PlayerColor.BLACK;
        return PlayerColor.WHITE;
    }

    public Move getLastMove() {
        if (history.isEmpty()) return null;
        return history.get(history.size() - 1);
    }

    public Move getLastLastMove() {
        if (history.size() <= 1) return null;
        return history.get(history.size() - 2);
    }

    public String getItem(GridItem item, boolean colored, boolean colored2) {
        var figure = checkersGrid.getFigure(item);
        var gridItemElemToDisplay = "   ";
        if (figure != null) {
            gridItemElemToDisplay = " " + figure.getFigureDisplayChar() + " ";
        }

        if (colored) {
            if (figure != null  && figure.playerColor == PlayerColor.WHITE) {
                gridItemElemToDisplay = "\u001B[41m" + gridItemElemToDisplay + "\u001B[0m";
            }
            else {
                gridItemElemToDisplay = "\u001B[41;30m" + gridItemElemToDisplay + "\u001B[0m";
            }
        }
        if (colored2) {
            if (figure != null  && figure.playerColor == PlayerColor.WHITE) {
                gridItemElemToDisplay = "\u001B[44m" + gridItemElemToDisplay + "\u001B[0m";
            }
            else {
                gridItemElemToDisplay = "\u001B[44;30m" + gridItemElemToDisplay + "\u001B[0m";
            }
        }
        else if(item.gridItemColor == GridItemColor.WHITE) {
            gridItemElemToDisplay = "\u001B[47;30m" + gridItemElemToDisplay + "\u001B[0m";
        }
        else if(figure != null && figure.playerColor == PlayerColor.BLACK) {
            gridItemElemToDisplay = "\u001B[30m" + gridItemElemToDisplay + "\u001B[0m";
        }

    return gridItemElemToDisplay;
    }

    @Override
    public String toString() {
        var fullStr = "";
        fullStr += "\nNEXT PLAYER: ";
        if (getCurrentPlayer() == PlayerColor.WHITE) {
            fullStr += "\u001B[47;30m" + getCurrentPlayer() + "\u001B[0m";
        }
        else {
            fullStr += getCurrentPlayer();
        }
        fullStr += "\n";
        var allLastJumpedTo = new ArrayList<GridItem>();
        var allLastLastJumpedTo = new ArrayList<GridItem>();
        if (getLastMove() != null) {
            allLastJumpedTo = getLastMove().getAllJumpedTo();
        }
        if (getLastLastMove() != null) {
            allLastLastJumpedTo = getLastLastMove().getAllJumpedTo();
        }
        for (var row : checkersGrid.getFullGrid()) {
            fullStr += row.get(0).number + " ";
            for (var item : row) {
                boolean lastFlag = false;
                for (var special : allLastJumpedTo) {
                    if (special.columnId == item.columnId && special.rowId == item.rowId) {
                        lastFlag = true;
                    }
                }
                boolean lastLastFlag = false;
                for (var special : allLastLastJumpedTo) {
                    if (special.columnId == item.columnId && special.rowId == item.rowId) {
                        lastLastFlag = true;
                    }
                }
                fullStr += getItem(item, lastFlag, lastLastFlag);
            }
            fullStr += '\n';
        }
        fullStr += "  ";
        for (var item : checkersGrid.getFullGrid().get(0)) {
            fullStr += " " + item.letter + " ";
        }
        return fullStr;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public boolean wasDrawn() {
        var lastXMovesWereCrowns = 15;
        if (history.size() < lastXMovesWereCrowns) return false;
        for (int i = 0; i < lastXMovesWereCrowns; i++) {
            if (history.get(history.size() - 1 - i).figureType == FigureType.NORMAL) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameFinished() {
        if (checkersGrid.getAllCurrentPossibleMoves().isEmpty()) return true;
        return wasDrawn();
    }

    public PlayerColor getWinner() {
        if (!isGameFinished() || wasDrawn()) return null;
        return getPrevPlayer();
    }
}

