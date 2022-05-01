import java.util.ArrayList;
import java.util.Random;

public class CheckersBot {
    static Random random = new Random();
    final private CheckersGridAccessor accessor;
    final private PlayerColor playerColor;
    final Integer maxDepth;

    public CheckersBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        this.accessor = accessor;
        this.playerColor = playerColor;
        if (maxDepth == null) {
            this.maxDepth = -1;
        }
        else {
            this.maxDepth = maxDepth;

        }
    }

    public Move getBestMove(CheckersGridHandler checkersGridHandler) {
        if (checkersGridHandler.getCurrentPlayer() != playerColor) {
            return null;
        }
        var allPossibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) return null;
        if (allPossibleMoves.size() == 1) return allPossibleMoves.get(0);
        int bestResult = 0;
        ArrayList<Integer> bestMoves = new ArrayList<>();
        for (int i = 0; i < allPossibleMoves.size(); i++) {
//            System.out.println(checkersGrid);
            var copied = checkersGridHandler.copy();
            copied.executeMove(i);
            var estimation = min(copied, maxDepth);
            System.out.println(allPossibleMoves.get(i) + ": " + estimation);
            if (bestMoves.isEmpty() || bestResult == estimation) {
                bestMoves.add(i);
                bestResult = estimation;
            }
            else if (bestResult < estimation) {
                bestMoves.clear();
                bestMoves.add(i);
                bestResult = estimation;
            }
        }
        System.out.println("Best estimated result: " + bestResult);
        return allPossibleMoves.get(bestMoves.get(random.nextInt(bestMoves.size())));
    }

    public int max(CheckersGridHandler checkersGridHandler, int depth) {
        var allPossibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MIN_VALUE + depth;
            return Integer.MIN_VALUE;
        }
        if (depth == 0) {
            var currentGridEstimation = accessor.accessCheckersGrid(checkersGridHandler, playerColor);
            Integer bestMoveEstimation = null;
            for (var move : allPossibleMoves) {
                var moveEstimation = accessor.accessMove(move, playerColor);
                if (bestMoveEstimation == null || bestMoveEstimation < moveEstimation) {
                    bestMoveEstimation = moveEstimation;
                }
            }
            return currentGridEstimation + bestMoveEstimation;
        }

        Integer bestEstimation = null;
        for (int i = 0; i < allPossibleMoves.size(); i++) {
            var copied = checkersGridHandler.copy();
            copied.executeMove(i);
            var estimation = min(copied, depth - 1);
            if (bestEstimation == null || bestEstimation < estimation) {
                bestEstimation = estimation;
            }

        }
        return bestEstimation;
    }

    public int min(CheckersGridHandler checkersGridHandler, int depth) {
        var allPossibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MAX_VALUE - depth;
            return Integer.MAX_VALUE;
        }
        if (depth == 0) {
            var currentGridEstimation = accessor.accessCheckersGrid(checkersGridHandler, playerColor);
            Integer worstMoveEstimation = null;
            for (var move : allPossibleMoves) {
                var moveEstimation = accessor.accessMove(move, playerColor);
                if (worstMoveEstimation == null || worstMoveEstimation > moveEstimation) {
                    worstMoveEstimation = moveEstimation;
                }
            }
            return currentGridEstimation + worstMoveEstimation;
        }
        Integer worstEstimation = null;
        for (int i = 0; i < allPossibleMoves.size(); i++) {
            var copied = checkersGridHandler.copy();
            copied.executeMove(i);
            var estimation = max(copied, depth - 1);
            if (worstEstimation == null || worstEstimation > estimation) {
                worstEstimation = estimation;
            }
        }
        return worstEstimation;
    }
}
