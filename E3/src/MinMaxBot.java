import java.util.ArrayList;

public class MinMaxBot extends Bot {
    protected MinMaxBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        super(accessor, playerColor, maxDepth);
    }

    @Override
    public ArrayList<MoveWithEstimation> getAllMovesWithEstimations(CheckersGridHandler checkersGridHandler) {
        ArrayList<MoveWithEstimation> movesWithEstimation = new ArrayList<>();
        var possibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        for (var move : possibleMoves) {
            var copied = checkersGridHandler.getCheckersGrid().copy();
            copied.executeMove(move);
            var estimation = minOrMax(copied, maxDepth - 1, MinMaxEnum.MIN);
            movesWithEstimation.add(new MoveWithEstimation(move, estimation));
            System.out.println("LAST: " + move + ": " + estimation);
        }
        return movesWithEstimation;
    }

    public int minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum) {
        if (depth == 0) {
            return accessor.accessCheckersGrid(checkersGrid, playerColor, null);
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        Integer chosenEstimation = null;
        switch (minMaxEnum) {
            case MIN -> {
                if (allPossibleMoves.isEmpty()) {
                    return Integer.MAX_VALUE;
                }
                for (var move: allPossibleMoves) {
                    var copied = checkersGrid.copy();
                    copied.executeMove(move);
                    var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MAX);
                    if (chosenEstimation == null || chosenEstimation > estimation) {
                        chosenEstimation = estimation;
                    }
                }
            }
            case MAX -> {
                if (allPossibleMoves.isEmpty()) {
                    return Integer.MIN_VALUE;
                }
                for (var move: allPossibleMoves) {
                    var copied = checkersGrid.copy();
                    copied.executeMove(move);
                    var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MIN);
                    if (chosenEstimation == null || chosenEstimation < estimation) {
                        chosenEstimation = estimation;
                    }
                }
            }
        }
        return chosenEstimation;
    }
}
