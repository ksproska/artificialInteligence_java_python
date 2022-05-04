import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class CheckersBot {
    static Random random = new Random();
    final private CheckersGridAccessor accessor;
    final private PlayerColor playerColor;
    final Integer maxDepth;
    private int lastMoveCount, lastMoveTime;
    private int totalMoveCount, totalMoveTime, counter;
    private Integer startEstimation;

    private class MoveWithEstimation {
        public final Move move;
        public final int estimation;

        private MoveWithEstimation(Move move, int estimation) {
            this.move = move;
            this.estimation = estimation;
        }
    }

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
        Vector<MoveWithEstimation> movesWithEstimation = new Vector<>();
        Vector<Thread> threads = new Vector<>();
        for (var move : allPossibleMoves) {
            var nextThread = new MinThread(checkersGridHandler.getCheckersGrid(), move, maxDepth, movesWithEstimation);
            threads.add(nextThread);
            nextThread.start();
        }
        for (var nextThread : threads) {
            try { nextThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (movesWithEstimation.size() != allPossibleMoves.size()) {
            throw new IllegalStateException("?");
        }
        for (var moveWithEstimation :
                movesWithEstimation) {
            if (bestMoves.isEmpty() || bestResult == moveWithEstimation.estimation) {
                bestMoves.add(moveWithEstimation.move);
                bestResult = moveWithEstimation.estimation;
            } else if (bestResult < moveWithEstimation.estimation) {
                bestMoves.clear();
                bestMoves.add(moveWithEstimation.move);
                bestResult = moveWithEstimation.estimation;
            }
        }
//            if (startEstimation - accessor.maxOffset > estimation) {
//                throw new IllegalStateException("?");
//            }
        long end = System.currentTimeMillis();
        lastMoveTime = (int) (end - start);
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    public class MinThread extends Thread {
        CheckersGrid copied;
        Move move;
        int depth;
        Vector<MoveWithEstimation> moveWithEstimations;

        public MinThread(CheckersGrid checkersGrid, Move move, int depth, Vector<MoveWithEstimation> moveWithEstimations) {
            copied = checkersGrid.copy();
            this.move = move;
            this.depth = depth;
            this.moveWithEstimations = moveWithEstimations;
        }

        @Override
        public void run() {
            copied.executeMove(move);
            var estimation = minOrMax(copied, depth, MinMaxEnum.MIN);
            moveWithEstimations.add(new MoveWithEstimation(move, estimation));
            System.out.println(move + ": " + estimation);
        }
    }

    enum MinMaxEnum {
        MIN, MAX
    }

    public int minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum) {
        var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor);
        if (depth == 0) {
            return currentEstimation;
        }
        if (startEstimation - accessor.maxOffset >= currentEstimation) {
            return currentEstimation;
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        Integer chosenEstimation = null;
        switch (minMaxEnum) {
            case MIN -> {
                if (allPossibleMoves.isEmpty()) {
                    if (depth > 0) return Integer.MAX_VALUE - depth;
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
                    if (depth > 0) return Integer.MIN_VALUE + depth;
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
        lastMoveCount += 1;
        return chosenEstimation;
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
