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
            var copied = checkersGridHandler.getCheckersGrid().copy();
//            System.out.println(copied);
            copied.executeMove(i);
//            System.out.println(copied);
//            System.out.println(accessor.accessCheckersGrid(copied, playerColor));
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

    public int max(CheckersGrid checkersGrid, int depth) {
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MIN_VALUE + depth;
            return Integer.MIN_VALUE;
        }
        if (depth == 0) {
            return accessor.accessCheckersGrid(checkersGrid, playerColor);
        }

        Integer bestEstimation = null;
        for (int i = 0; i < allPossibleMoves.size(); i++) {
            var copied = checkersGrid.copy();
            copied.executeMove(i);
            var estimation = min(copied, depth - 1);
            if (bestEstimation == null || bestEstimation < estimation) {
                bestEstimation = estimation;
            }
        }
        return bestEstimation;
    }

    public int min(CheckersGrid checkersGrid, int depth) {
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MAX_VALUE - depth;
            return Integer.MAX_VALUE;
        }
        if (depth == 0) {
            return accessor.accessCheckersGrid(checkersGrid, playerColor);
        }

        Integer worstEstimation = null;
        for (int i = 0; i < allPossibleMoves.size(); i++) {
            var copied = checkersGrid.copy();
            copied.executeMove(i);
            var estimation = max(copied, depth - 1);
            if (worstEstimation == null || worstEstimation > estimation) {
                worstEstimation = estimation;
            }
        }
        return worstEstimation;
    }
}
