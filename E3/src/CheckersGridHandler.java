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
    private ArrayList<ArrayList<GridItemSnapshot>> fullGrid;
    private ArrayList<Move> allCurrentPossibleMoves;
    private PlayerColor playerColor;

    public CheckersGrid() {
        fullGrid = new ArrayList<>();
        allCurrentPossibleMoves = new ArrayList<>();
        var nextColor = GridItemColor.WHITE;
        int gridSize = 8;
        for (int i = gridSize; i > 0; i--) {
            var newList = new ArrayList<GridItemSnapshot>();
            int counter = 0;
            for (var letter : GridItemLetter.values()) {
                newList.add(new GridItemSnapshot(new GridItem(nextColor, letter, i, gridSize - i, counter)));
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
//        System.out.println(fullGrid);
        playerColor = PlayerColor.WHITE;
    }

    @Override
    public String toString() {
        return "CheckersGrid{" +
                "fullGrid=" + fullGrid +
                ", \n\sallCurrentPossibleMoves=" + allCurrentPossibleMoves +
                ", \n\splayerColor=" + playerColor +
                '}';
    }

    public CheckersGrid(ArrayList<ArrayList<GridItemSnapshot>> fullGrid, ArrayList<Move> allCurrentPossibleMoves, PlayerColor playerColor) {
        this.fullGrid = new ArrayList<>();
        for (var row : fullGrid) {
            var tempArray = new ArrayList<GridItemSnapshot>();
            for (var item : row) {
                tempArray.add(item.copy());
            }
            this.fullGrid.add(tempArray);
        }
        this.allCurrentPossibleMoves = new ArrayList<>();
//        for (var move : allCurrentPossibleMoves) {
//            this.allCurrentPossibleMoves.add(move.copy());
//        }
        this.playerColor = playerColor;
        setAllCurrentPossibleMoves();
    }

    public void executeMove(int moveInx) {
        var move = getAllCurrentPossibleMoves().get(moveInx);
        executeMove(move);
    }

    public PlayerColor getOppositePlayer(PlayerColor color) {
        return switch (color) {
            case WHITE -> PlayerColor.BLACK;
            case BLACK -> PlayerColor.WHITE;
        };
    }

    public CheckersGrid copy() {
        return new CheckersGrid(fullGrid, allCurrentPossibleMoves, playerColor);
    }

    public ArrayList<GridItemSnapshot> getAllFilledItems() {
        var all = new ArrayList<GridItemSnapshot>();
        for (var row : fullGrid) {
            for (var item : row) {
                if (!item.isEmpty()) {
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

    public GridItemSnapshot getGridItem(int rowId, int columnId) {
        if (rowId < 0 || columnId < 0 || rowId >= fullGrid.size() || columnId >= GridItem.lettersIndexes.size()) {
            return null;
        }
        return fullGrid.get(rowId).get(columnId);
    }
    public GridItemSnapshot getGridItem(String shortcut) {
        var rowId = getRow(shortcut);
        var columnId = getColumn(shortcut);
        return fullGrid.get(rowId).get(columnId);
    }

    private void setAllCurrentPossibleMoves() {
        allCurrentPossibleMoves = getAllMoves();
    }

    public ArrayList<Move> getAllCurrentPossibleMoves() { return allCurrentPossibleMoves; }

    public void basicSetup() {
        playerColor = PlayerColor.WHITE;
        for (var row : new int[]{1, 2}) {
            for (var item : fullGrid.get(row - 1)) {
                if(item.getGridItemColor() == GridItemColor.BLACK) {
                    item.setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
                }
            }
        }
        for (var row : new int[]{7, 8}) {
            for (var item : fullGrid.get(row - 1)) {
                if(item.getGridItemColor() == GridItemColor.BLACK) {
                    item.setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));
                }
            }
        }
//        System.out.println(fullGrid);
        setAllCurrentPossibleMoves();
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
        setAllCurrentPossibleMoves();
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
        setAllCurrentPossibleMoves();
//        move("h4", "g3");
    }
    public void exampleSetup4() {
        getGridItem("b8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));
        getGridItem("f4").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));

        getGridItem("b6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("d6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        setAllCurrentPossibleMoves();
    }
    public void exampleSetup5() {
        getGridItem("e5").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));

        getGridItem("b6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("d6").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("d2").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("d4").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("f4").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        setAllCurrentPossibleMoves();
    }
    public void exampleSetup6() {
        getGridItem("h8").setFigure(new Figure(PlayerColor.WHITE, FigureType.CROWNED));

        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("c5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("c7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        setAllCurrentPossibleMoves();
    }
    public void exampleSetup7() {
        getGridItem("h8").setFigure(new Figure(PlayerColor.WHITE, FigureType.NORMAL));

        getGridItem("c3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("c5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("c7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("e7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g3").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g5").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        getGridItem("g7").setFigure(new Figure(PlayerColor.BLACK, FigureType.NORMAL));
        setAllCurrentPossibleMoves();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public ArrayList<GridItemSnapshot> getAllPlayerFigures() {
        var allFigures = new ArrayList<GridItemSnapshot>();
        for (var row : fullGrid) {
            for (var item : row) {
                if (!item.isEmpty() && item.getFigure().playerColor == playerColor) {
                    allFigures.add(item);
                }
            }
        }
        return allFigures;
    }

    public ArrayList<GridItemSnapshot> getAllFigures() {
        var allFigures = new ArrayList<GridItemSnapshot>();
        for (var row : fullGrid) {
            for (var item : row) {
                if (!item.isEmpty()) {
                    allFigures.add(item);
                }
            }
        }
        return allFigures;
    }

    private ArrayList<Move> getAllMoves() {
        var allPlayerFigures = getAllPlayerFigures();
//        System.out.println(allPlayerFigures);
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

    public void executeMove(Move move) {
        var from = move.startingPoint;
        var to = move.toJumpItems.get(move.toJumpItems.size() - 1);

        to.setFigure(from.getFigure());
        if (to.getNumber() == 8 && from.getPlayerColor() == PlayerColor.WHITE) {
            to.getFigure().setFigureType(FigureType.CROWNED);
        }
        if (to.getNumber() == 1 && from.getPlayerColor() == PlayerColor.BLACK) {
            to.getFigure().setFigureType(FigureType.CROWNED);
        }
        from.setFigure(null);
        if (move.getClass() == Jump.class) {
            for (var toRemoveItems : ((Jump) move).jumpOverItems) {
                toRemoveItems.setFigure(null);
            }
        }
        this.playerColor = getOppositePlayer(this.playerColor);
        setAllCurrentPossibleMoves();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public void getPlacesToMoveCrownedInDirection(GridItemSnapshot gridItemSnapshot, int rowOffset, int columnOffset, ArrayList<GridItemSnapshot> acc) {
        var nextItem = getGridItem(gridItemSnapshot.getRowId() + rowOffset, gridItemSnapshot.getColumnId() + columnOffset);
        if (nextItem != null && nextItem.isEmpty()) {
            acc.add(nextItem);
            getPlacesToMoveCrownedInDirection(nextItem, rowOffset, columnOffset, acc);
        }
    }

    private GridItemSnapshot[] getNormalJumpInDirection(PlayerColor jumpingPlayer, GridItemSnapshot sourceItem, int rowDir, int columnDir) {
        var multiplier = 2;
        var jumpOver = getGridItem(sourceItem.getRowId() + rowDir, sourceItem.getColumnId() + columnDir);
        if (jumpOver == null || jumpOver.isEmpty() || jumpOver.getFigure().playerColor == jumpingPlayer) return null;
        var toJump = getGridItem(sourceItem.getRowId() + rowDir * multiplier, sourceItem.getColumnId() + columnDir * multiplier);
        if (toJump != null && toJump.isEmpty()) {
            return new GridItemSnapshot[]{jumpOver, toJump};
        }
        return null;
    }

    private GridItemSnapshot getNextToCrownedJumpOver(PlayerColor jumpingPlayer, GridItemSnapshot item, int rowDir, int columnDir) {
        var nextItem = getGridItem(item.getRowId() + rowDir, item.getColumnId() + columnDir);
        if (nextItem == null) { return null; }
        if (!nextItem.isEmpty()) {
            if (nextItem.getFigure().playerColor == jumpingPlayer) return null;
            return nextItem;
        }
        return getNextToCrownedJumpOver(jumpingPlayer, nextItem, rowDir, columnDir);
    }

    private ArrayList<GridItemSnapshot> getPlacesToCrownedLand(GridItemSnapshot prev, int rowDir, int columnDir, ArrayList<GridItemSnapshot> acc) {
        var nextItem = getGridItem(prev.getRowId() + rowDir, prev.getColumnId() + columnDir);
        if (nextItem == null) { return acc; }
        if (nextItem.isEmpty()) {
            acc.add(nextItem);
            getPlacesToCrownedLand(nextItem, rowDir, columnDir, acc);
        }
        return acc;
    }

    private void getAllNormalJumps(PlayerColor jumpingPlayer, GridItemSnapshot item, Jump jump, ArrayList<Jump> allPaths) {
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

    private void getAllCrownedJumps(PlayerColor jumpingPlayer, GridItemSnapshot item, Jump jump, ArrayList<Jump> allPaths) {
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

    private ArrayList<Jump> getObligatoryJumps(GridItemSnapshot item) {
        var allJumps = new ArrayList<Jump>();
        if (item.getFigure() == null) { return allJumps; }

        switch (item.getFigure().getFigureType()) {
            case NORMAL -> getAllNormalJumps(item.getFigure().playerColor, item, new Jump(item), allJumps);
            case CROWNED -> getAllCrownedJumps(item.getFigure().playerColor, item, new Jump(item), allJumps);
        }
        return allJumps;
    }

    private ArrayList<Move> getVoluntaryMovements(GridItemSnapshot item) {
        var allMoves = new ArrayList<Move>();
        if (item.getFigure() == null) { return allMoves; }

        switch (item.getFigure().getFigureType()) {
            case NORMAL -> {
                var voluntaryMoves = new ArrayList<GridItemSnapshot>();
                switch (item.getFigure().playerColor) {
                    case WHITE -> {
                        voluntaryMoves.add(getGridItem(item.getRowId() - 1, item.getColumnId() - 1));
                        voluntaryMoves.add(getGridItem(item.getRowId() - 1, item.getColumnId() + 1));
                    }
                    case BLACK -> {
                        voluntaryMoves.add(getGridItem(item.getRowId() + 1, item.getColumnId() - 1));
                        voluntaryMoves.add(getGridItem(item.getRowId() + 1, item.getColumnId() + 1));
                    }
                }
                for (var move : voluntaryMoves) {
                    if(move != null && move.isEmpty()) { allMoves.add(new Move(item, move)); }
                }
            }
            case CROWNED -> {
                var allNext = new ArrayList<GridItemSnapshot>();
                for (var dimension : allPossibleDirections) {
                    getPlacesToMoveCrownedInDirection(item, dimension[0], dimension[1], allNext);
                }
                for (var gridItem : allNext) {
                    allMoves.add(new Move(item, gridItem));
                }
            }
        }
        return allMoves;
    }

    public ArrayList<ArrayList<GridItemSnapshot>> getFullGrid() { return fullGrid; }
}

public class CheckersGridHandler {
    CheckersGrid checkersGrid;
    ArrayList<Move> history;

    public void basicSetup() {
        checkersGrid.basicSetup();
    }

    public ArrayList<GridItemSnapshot> getAllPlayerFigures() { return checkersGrid.getAllPlayerFigures(); }

    public CheckersGrid getCheckersGrid() {
        return checkersGrid;
    }

    public void executeMove(Move move) {
        history.add(move.copy());
        checkersGrid.executeMove(move);
    }

    public ArrayList<Move> getAllCurrentPossibleMoves() { return checkersGrid.getAllCurrentPossibleMoves(); }

//    public CheckersGridHandler copy() {
//        return new CheckersGridHandler(checkersGrid.getFullGrid(), history);
//    }

    public CheckersGridHandler() {
        checkersGrid = new CheckersGrid();
        history = new ArrayList<>();
    }

    public ArrayList<GridItemSnapshot> getAllFilledItems() {
        return checkersGrid.getAllFilledItems();
    }

//    public CheckersGridHandler(ArrayList<ArrayList<GridItemSnapshot>> fullGrid, ArrayList<Move> history) {
//        this.history = new ArrayList<>(history);
//        this.fullGrid = new ArrayList<>();
//        for (var row : fullGrid) {
//            var tempArray = new ArrayList<GridItemSnapshot>();
//            for (var item : row) {
//                tempArray.add(item.copy());
//            }
//            this.fullGrid.add(tempArray);
//        }
//        setAllCurrentPossibleMoves();
//    }

    public ArrayList<GridItemSnapshot> getAllFigures() { return checkersGrid.getAllFigures(); }

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

    @Override
    public String toString() {
        var fullStr = "";
        fullStr += "\nNEXT PLAYER: " + getCurrentPlayer() + "\n";

        var allLastJumpedTo = new ArrayList<GridItemSnapshot>();
        var allLastLastJumpedTo = new ArrayList<GridItemSnapshot>();
        if (getLastMove() != null) {
//            System.out.println(getLastMove().getAllJumpedTo());
            allLastJumpedTo = getLastMove().getAllJumpedTo();
        }
        if (getLastLastMove() != null) {
//            System.out.println(getLastMove().getAllJumpedTo());
            allLastLastJumpedTo = getLastLastMove().getAllJumpedTo();
        }
        for (var row : checkersGrid.getFullGrid()) {
            fullStr += row.get(0).getNumber() + " ";
            for (var item : row) {
                boolean lastFlag = false;
                for (var special : allLastJumpedTo) {
                    if (special.getColumnId() == item.getColumnId() && special.getRowId() == item.getRowId()) {
                        lastFlag = true;
                    }
                }
                boolean lastLastFlag = false;
                for (var special : allLastLastJumpedTo) {
                    if (special.getColumnId() == item.getColumnId() && special.getRowId() == item.getRowId()) {
                        lastLastFlag = true;
                    }
                }
//                if (getCurrentPlayer() == PlayerColor.WHITE) {
//                    var temp = allLastJumpedTo;
//                    allLastJumpedTo = allLastLastJumpedTo;
//                    allLastLastJumpedTo = temp;
//                }
                fullStr += item.getItem(lastFlag, lastLastFlag);
            }
            fullStr += '\n';
        }
        fullStr += "  ";
        for (var item : checkersGrid.getFullGrid().get(0)) {
            fullStr += " " + item.getLetter() + " ";
        }
        return fullStr;
    }

    public String currentState() {
        var state = "NEXT: " + getCurrentPlayer() + "\n";
        for (var element : checkersGrid.getAllFigures()) {
            state += "" + element.getLetter() + element.getNumber() + " " + element.getFigure().playerColor.name().split("")[0] + " " + element.getFigure().getFigureType().name().split("")[0] + "\n";
        }
        return state;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public boolean wasDrawn() {
        var lastXMovesWereCrowns = 15;
        if (history.size() < lastXMovesWereCrowns) return false;
        for (int i = 0; i < lastXMovesWereCrowns; i++) {
            if (history.get(history.size() - 1 - i).startingPoint.getFigure().getFigureType() == FigureType.NORMAL) {
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

