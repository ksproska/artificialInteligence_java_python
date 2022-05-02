import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class CheckersBot {
    static Random random = new Random();
    final private CheckersGridAccessor accessor;
    final private PlayerColor playerColor;
    final Integer maxDepth;
    private int lastMoveCount, lastMoveTime;
    private int totalMoveCount, totalMoveTime, counter;

    public CheckersBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        totalMoveCount = 0;
        totalMoveTime = 0;
        counter = 0;
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
        lastMoveTime = 0;
        lastMoveCount = 0;
        long start = System.currentTimeMillis();
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
            copied.executeMove(i);
            var estimation = min(copied, maxDepth);
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
        long end = System.currentTimeMillis();
        lastMoveTime = (int) (end - start);
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
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
        lastMoveCount += 1;
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
        lastMoveCount += 1;
        return worstEstimation;
    }

    public void printStats() {
        System.out.println("Last:");
        System.out.println("Nodes: " + lastMoveCount);
        System.out.println("Time:  " + ((double) lastMoveTime / 1000) + " sec");
        if (counter > 0) {
            System.out.println("Average:");
            System.out.println("Nodes: " + (double) totalMoveCount / counter);
            System.out.println("Time:  " + (double) totalMoveTime / (1000 * counter) + " sec");
        }
    }
}
