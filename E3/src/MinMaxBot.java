import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class MinMaxBot implements Player {
    static Random random = new Random();
    final private CheckersGridAccessor accessor;
    final private PlayerColor playerColor;
    final Integer maxDepth;
    private int lastMoveCount, lastMoveTime;
    private int totalMoveCount, totalMoveTime, counter;

    @Override
    public Move getChosenMove(CheckersGridHandler checkersGridHandler) {
        return getBestMove(checkersGridHandler);
    }

    private class MoveWithEstimation {
        public final Move move;
        public final double estimation;

        private MoveWithEstimation(Move move, double estimation) {
            this.move = move;
            this.estimation = estimation;
        }
    }

    public MinMaxBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
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
        double bestResult = 0;
        ArrayList<Move> bestMoves = new ArrayList<>();
        Vector<MoveWithEstimation> movesWithEstimation = new Vector<>();
//        Vector<Thread> threads = new Vector<>();
        for (var move : allPossibleMoves) {
            var estimation = minOrMax(checkersGridHandler.getCheckersGrid(), maxDepth - 1, MinMaxEnum.MIN);
            movesWithEstimation.add(new MoveWithEstimation(move, estimation));
//            var nextThread = new MinOrMaxThread(MinMaxEnum.MIN, checkersGridHandler.getCheckersGrid(), move, maxDepth - 1, movesWithEstimation);
//            threads.add(nextThread);
//            nextThread.start();
        }
//        for (var nextThread : threads) {
//            try { nextThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
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
        long end = System.currentTimeMillis();
        lastMoveTime = (int) (end - start);
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

//    public class MinOrMaxThread extends Thread {
//        CheckersGrid copied;
//        Move move;
//        int depth;
//        MinMaxEnum minMaxEnum;
//        Vector<MoveWithEstimation> moveWithEstimations;
//
//        public MinOrMaxThread(MinMaxEnum minMaxEnum, CheckersGrid checkersGrid, Move move, int depth, Vector<MoveWithEstimation> moveWithEstimations) {
//            copied = checkersGrid.copy();
//            this.move = move;
//            this.depth = depth;
//            this.moveWithEstimations = moveWithEstimations;
//            this.minMaxEnum = minMaxEnum;
//        }
//
//        @Override
//        public void run() {
//            copied.executeMove(move);
//            var estimation = minOrMax(copied, depth, minMaxEnum);
//            moveWithEstimations.add(new MoveWithEstimation(move, estimation));
//            System.out.println(move + ": " + estimation);
//        }
//    }

    enum MinMaxEnum {
        MIN, MAX
    }

    public double minOrMax(CheckersGrid checkersGrid, int depth, MinMaxEnum minMaxEnum) {
        if (depth == 0) {
            var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor, minMaxEnum);
            return currentEstimation;
        }
        var allPossibleMoves = checkersGrid.getAllCurrentPossibleMoves();
        Double chosenEstimation = null;
        if (allPossibleMoves.isEmpty()) {
            var currentEstimation = accessor.accessCheckersGrid(checkersGrid, playerColor, minMaxEnum);
            return switch (minMaxEnum) {
                case MIN -> currentEstimation - maxDepth + depth;
                case MAX -> currentEstimation + maxDepth - depth;
            };
        }

        for (var move: allPossibleMoves) {
            var copied = checkersGrid.copy();
            copied.executeMove(move);
            switch (minMaxEnum) {
                case MIN -> {
                    var estimation = minOrMax(copied, depth - 1, MinMaxEnum.MAX);
                    if (chosenEstimation == null || chosenEstimation > estimation) {
                        chosenEstimation = estimation;
                    }
                }
                case MAX -> {
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