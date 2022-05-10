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

    public MinMaxBot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        totalMoveCount = 0;
        totalMoveTime = 0;
        counter = 0;
        this.accessor = accessor;
        this.playerColor = playerColor;
        this.maxDepth = maxDepth;
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
        for (var move : allPossibleMoves) {
            var estimation = minOrMax(checkersGridHandler.getCheckersGrid(), maxDepth - 1, MinMaxEnum.MIN);
            movesWithEstimation.add(new MoveWithEstimation(move, estimation));
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
        long end = System.currentTimeMillis();
        lastMoveTime = (int) (end - start);
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
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
