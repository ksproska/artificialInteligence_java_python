import java.util.ArrayList;
import java.util.Random;

public abstract class Bot implements Player {
    static Random random = new Random();
    final protected CheckersGridAccessor accessor;
    final protected PlayerColor playerColor;
    final Integer maxDepth;
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
        printStats();
        totalMoveTime += lastMoveTime;
        totalMoveCount += lastMoveCount;
        counter += 1;
        return bestMoves.get(random.nextInt(bestMoves.size()));
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
