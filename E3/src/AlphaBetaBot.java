import java.util.ArrayList;

public class AlphaBetaBot extends Bot {
    protected AlphaBetaBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        super(accessor, playerColor, maxDepth);
    }

    @Override
    public ArrayList<MoveWithEstimation> getAllMovesWithEstimations(CheckersGridHandler checkersGridHandler) {
        var alpha = Integer.MIN_VALUE;
        var beta = Integer.MAX_VALUE;
        var chosenEstimation = Integer.MIN_VALUE;
        ArrayList<MoveWithEstimation> movesWithEstimation = new ArrayList<>();
        for (var move : checkersGridHandler.getAllCurrentPossibleMoves()) {
//            if (chosenEstimation <= beta) {
                var copied = checkersGridHandler.getCheckersGrid().copy();
                copied.executeMove(move);
                var estimation = minOrMax(copied, maxDepth - 1, MinMaxEnum.MIN, alpha, beta);
                movesWithEstimation.add(new MoveWithEstimation(move, estimation));
                System.out.println("LAST: " + move + ": " + estimation);
                if (chosenEstimation < estimation) {
                    chosenEstimation = estimation;
                }
                if (alpha < estimation) { alpha = estimation; }
//            }
        }
        return movesWithEstimation;
    }

    public int minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum, double alpha, double beta) {
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

        Integer chosenEstimation = null;
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