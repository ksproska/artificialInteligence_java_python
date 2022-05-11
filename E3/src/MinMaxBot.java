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
        if (allPossibleMoves.isEmpty()) {
            var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor, minMaxEnum);
            return switch (minMaxEnum) {
                case MIN -> currentEstimation - maxDepth + depth;
                case MAX -> currentEstimation + maxDepth - depth;
            };
        }
        int chosenEstimation = 0;
        switch (minMaxEnum) {
            case MIN -> chosenEstimation = Integer.MAX_VALUE;
            case MAX -> chosenEstimation = Integer.MIN_VALUE;
        }
        switch (minMaxEnum) {
            case MIN -> {
                for (var move: allPossibleMoves) {
                    var copied = checkersGrid.copy();
                    copied.executeMove(move);
                    var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MAX);
                    if (chosenEstimation > estimation) {
                        chosenEstimation = estimation;
                    }
                }
            }
            case MAX -> {
                for (var move: allPossibleMoves) {
                    var copied = checkersGrid.copy();
                    copied.executeMove(move);
                    var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MIN);
                    if (chosenEstimation < estimation) {
                        chosenEstimation = estimation;
                    }
                }
            }
        }
        return chosenEstimation;
    }
}
