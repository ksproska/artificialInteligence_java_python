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
    private Integer startEstimation;

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
        startEstimation = accessor.accessCheckersGrid(checkersGridHandler.checkersGrid, playerColor);
        System.out.println("Start: " + startEstimation);
        long start = System.currentTimeMillis();
        if (checkersGridHandler.getCurrentPlayer() != playerColor) {
            return null;
        }
        var allPossibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) return null;
        if (allPossibleMoves.size() == 1) return allPossibleMoves.get(0);
        int bestResult = 0;
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (var move : allPossibleMoves) {
            var copied = checkersGridHandler.getCheckersGrid().copy();
            copied.executeMove(move);
            var estimation = min(copied, maxDepth);
            System.out.println(move + ": " + estimation);
//            if (startEstimation - accessor.maxOffset > estimation) {
//                throw new IllegalStateException("?");
//            }
            if (bestMoves.isEmpty() || bestResult == estimation) {
                bestMoves.add(move);
                bestResult = estimation;
            }
            else if (bestResult < estimation) {
                bestMoves.clear();
                bestMoves.add(move);
                bestResult = estimation;
            }
        }
        long end = System.currentTimeMillis();
        lastMoveTime = (int) (end - start);
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    public int max(CheckersGrid checkersGrid, int depth) {
        var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor);
        if (depth == 0) {
            return currentEstimation;
        }
//        if (startEstimation > currentEstimation) {
//            System.out.println(currentEstimation);
//        }
        if (startEstimation - accessor.maxOffset >= currentEstimation) {
//            System.out.println(startEstimation);
//            System.out.println(currentEstimation);
//            System.out.println(accessor.maxOffset);
            return currentEstimation;
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MIN_VALUE + depth;
            return Integer.MIN_VALUE;
        }
        Integer bestEstimation = null;
        for (var move: allPossibleMoves) {
            var copied = checkersGrid.copy();
            copied.executeMove(move);
            var estimation = min(copied, depth - 1);
            if (bestEstimation == null || bestEstimation < estimation) {
                bestEstimation = estimation;
            }
        }
        lastMoveCount += 1;
        return bestEstimation;
    }

    public int min(CheckersGrid checkersGrid, int depth) {
        var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor);
        if (depth == 0) {
            return currentEstimation;
        }
//        if (startEstimation > currentEstimation) {
//            System.out.println(currentEstimation);
//        }
        if (startEstimation - accessor.maxOffset >= currentEstimation) {
//            System.out.println(startEstimation);
//            System.out.println(accessor.maxOffset);
//            System.out.println(currentEstimation);
            return currentEstimation;
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) {
            if (depth > 0) return Integer.MAX_VALUE - depth;
            return Integer.MAX_VALUE;
        }
        Integer worstEstimation = null;
        for (var move: allPossibleMoves) {
            var copied = checkersGrid.copy();
            copied.executeMove(move);
            var estimation = max(copied, depth - 1);
            if (worstEstimation == null || worstEstimation > estimation) {
                worstEstimation = estimation;
            }
        }
        lastMoveCount += 1;
        return worstEstimation;
    }

    public void printStats() {
        if (lastMoveCount != 0) {
            System.out.println("Last:");
            System.out.println("\tNodes: " + lastMoveCount);
            System.out.println("\tTime:  " + ((double) lastMoveTime / 1000) + " sec");
        }
        printAverages();
    }

    public void printAverages() {
        if (counter > 0) {
            System.out.println("Average:");
            System.out.println("\tNodes: " + (double) totalMoveCount / counter);
            System.out.println("\tTime:  " + (double) totalMoveTime / (1000 * counter) + " sec");
        }
    }
}
