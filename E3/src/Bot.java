import java.util.ArrayList;
import java.util.Random;

public abstract class Bot implements Player {
    static Random random = new Random();
    final protected CheckersGridAccessor accessor;
    final protected PlayerColor playerColor;
    final protected Integer maxDepth;
    protected int lastMoveCount, lastMoveTime;
    protected int totalMoveCount, totalMoveTime, counter;

    protected Bot(CheckersGridAccessor accessor, PlayerColor playerColor, Integer maxDepth) {
        totalMoveCount = 0;
        totalMoveTime = 0;
        counter = 0;
        this.accessor = accessor;
        this.playerColor = playerColor;
        this.maxDepth = maxDepth;
    }

    @Override
    public Move getChosenMove(CheckersGridHandler checkersGridHandler) {
        return getBestMove(checkersGridHandler);
    }

    public ArrayList<MoveWithEstimation> getAllMovesWithEstimations(CheckersGridHandler checkersGridHandler) {
        throw new IllegalStateException("Method not implemented");
    }

    public void restartForNewGame() {
        totalMoveCount = 0;
        totalMoveTime = 0;
        counter = 0;
    }

    public Move getBestMove(CheckersGridHandler checkersGridHandler) {
        lastMoveTime = 0;
        lastMoveCount = 0;
        long start = System.currentTimeMillis();
        if (checkersGridHandler.getCurrentPlayer() != playerColor) { return null; }
        var allPossibleMoves = checkersGridHandler.getAllCurrentPossibleMoves();
        if (allPossibleMoves.isEmpty()) return null;
        if (allPossibleMoves.size() == 1) return allPossibleMoves.get(0);

        ArrayList<MoveWithEstimation> movesWithEstimation = getAllMovesWithEstimations(checkersGridHandler);

        double bestResult = 0;
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (var moveWithEstimation : movesWithEstimation) {
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
//        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
    }

    public void printStats() {
        System.out.printf("Nodes:      %.2f %n", (double) totalMoveCount / counter);
        System.out.printf("Time (ses): %.3f %n", (double) totalMoveTime / (1000 * counter));
    }

    public void printAverages() {
        if (counter > 0) {
            System.out.println("Average:");
            System.out.printf("\tNodes:      %.2f %n", (double) totalMoveCount / counter);
            System.out.printf("\tTime (ses): %.3f %n", (double) totalMoveTime / (1000 * counter));
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public double getAverageNodesVisited() {
        return round((double) totalMoveCount / counter, 2);
    }

    public double getAverageTimeForMove() {
        return round((double) totalMoveTime / (1000 * counter), 2);
    }


    public Integer getMaxDepth() {
        return maxDepth;
    }

    public CheckersGridAccessor getAccessor() {
        return accessor;
    }

    @Override
    public PlayerColor getPlayerColor() {
        return playerColor;
    }
}
