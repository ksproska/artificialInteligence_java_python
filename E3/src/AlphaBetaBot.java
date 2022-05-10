import java.util.ArrayList;

public class AlphaBetaBot extends Bot {
    protected AlphaBetaBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        super(accessor, playerColor, maxDepth);
    }

    @Override
    public ArrayList<MoveWithEstimation> getAllMovesWithEstimations(CheckersGridHandler checkersGridHandler) {
        var alpha = Double.MIN_VALUE;
        var beta = Double.MAX_VALUE;
        var chosenEstimation = Double.MIN_VALUE;
        ArrayList<MoveWithEstimation> movesWithEstimation = new ArrayList<>();
        for (var move : checkersGridHandler.getAllCurrentPossibleMoves()) {
            if (chosenEstimation <= beta) {
                var estimation = minOrMax(checkersGridHandler.getCheckersGrid(), maxDepth - 1, MinMaxEnum.MIN, alpha, beta);
                movesWithEstimation.add(new MoveWithEstimation(move, estimation));
                if (chosenEstimation < estimation) {
                    chosenEstimation = estimation;
                }
                if (alpha < estimation) { alpha = estimation; }
            }
        }
        return movesWithEstimation;
    }

    public double minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum, double alpha, double beta) {
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
                    if (chosenEstimation == null || chosenEstimation >= alpha) {
                        var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MAX, alpha, beta);
                        if (chosenEstimation == null || chosenEstimation > estimation) {
                            chosenEstimation = estimation;
                        }

                        if (beta > estimation) { beta = estimation; }
                    }
                }
                case MAX -> {
                    if (chosenEstimation == null || chosenEstimation <= beta) {
                        var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MIN, alpha, beta);
                        if (chosenEstimation == null || chosenEstimation < estimation) {
                            chosenEstimation = estimation;
                        }

                        if (alpha < estimation) { alpha = estimation; }
                    }
                }
            }
        }
        lastMoveCount += 1;
        return chosenEstimation;
    }
}