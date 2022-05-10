import java.util.ArrayList;

public class MinMaxBot extends Bot {
    protected MinMaxBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        super(accessor, playerColor, maxDepth);
    }

    @Override
    public ArrayList<MoveWithEstimation> getAllMovesWithEstimations(CheckersGridHandler checkersGridHandler) {
        ArrayList<MoveWithEstimation> movesWithEstimation = new ArrayList<>();
        for (var move : checkersGridHandler.getAllCurrentPossibleMoves()) {
            var estimation = minOrMax(checkersGridHandler.getCheckersGrid(), maxDepth - 1, MinMaxEnum.MIN);
            movesWithEstimation.add(new MoveWithEstimation(move, estimation));
        }
        return movesWithEstimation;
    }

    public double minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum) {
        if (depth == 0) {
            var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor, minMaxEnum);
            return currentEstimation;
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor, minMaxEnum);
            return switch (minMaxEnum) {
                case MIN -> currentEstimation - maxDepth + depth;
                case MAX -> currentEstimation + maxDepth - depth;
            };
        }

        Double chosenEstimation = null;
        for (var move: allPossibleMoves) {
            var copied = checkersGrid.copy();
            copied.executeMove(move);
            switch (minMaxEnum) {
                case MIN -> {
                    var currentEstimation = minOrMax(copied, depth - 1, MinMaxEnum.MAX);
                    if (chosenEstimation == null || chosenEstimation > currentEstimation) {
                        chosenEstimation = currentEstimation;
                    }
                }
                case MAX -> {
                    var currentEstimation = minOrMax(copied, depth - 1, MinMaxEnum.MIN);
                    if (chosenEstimation == null || chosenEstimation < currentEstimation) {
                        chosenEstimation = currentEstimation;
                    }
                }
            }
        }
        lastMoveCount += 1;
        return chosenEstimation;
    }
}
