import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

class StatsContainer {
    public final double whiteAverageNodes, whiteAverageTime;
    public final double blackAverageNodes, blackAverageTime;
    public final int whiteMaxDepth, blackMaxDepth;
    public final CheckersGridAccessor whiteAccessor, blackAccessor;
    public final PlayerColor winner;

    StatsContainer(double whiteAverageNodes, double whiteAverageTime, int whiteMaxDepth, CheckersGridAccessor whiteAccessor,
                   double blackAverageNodes, double blackAverageTime, int blackMaxDepth, CheckersGridAccessor blackAccessor,
                   PlayerColor winner) {
        this.whiteAverageNodes = whiteAverageNodes;
        this.whiteAverageTime = whiteAverageTime;
        this.blackAverageNodes = blackAverageNodes;
        this.blackAverageTime = blackAverageTime;
        this.whiteMaxDepth = whiteMaxDepth;
        this.blackMaxDepth = blackMaxDepth;
        this.whiteAccessor = whiteAccessor;
        this.blackAccessor = blackAccessor;
        this.winner = winner;
    }

    public static String columnsNames() {
        return "whiteAverageNodes" +
                "\t" + "blackAverageNodes" +
                "\t" + "whiteAverageTime" +
                "\t" + "blackAverageTime" +
                "\t" + "whiteMaxDepth" +
                "\t" + "blackMaxDepth" +
                "\t" + "whiteAccessor" +
                "\t" + "blackAccessor" +
                "\t" + "winner";
    }

    @Override
    public String toString() {
        return whiteAverageNodes +
                "\t" + blackAverageNodes +
                "\t" + whiteAverageTime +
                "\t" + blackAverageTime +
                "\t" + whiteMaxDepth +
                "\t" + blackMaxDepth +
                "\t" + whiteAccessor.getClass().getName() +
                "\t" + blackAccessor.getClass().getName() +
                "\t" + winner;
    }
}

public class GameSimulatorConsole {
    public static StatsContainer runGame(Player white, Player black) {
        if (white.getPlayerColor() != PlayerColor.WHITE || black.getPlayerColor() != PlayerColor.BLACK) {
            throw new IllegalStateException("Wrong player colors!");
        }
        var grid = new CheckersGridHandler();
        grid.basicSetup();

        ArrayList<Move> allMoves;
        while (!grid.isGameFinished()) {
            allMoves = grid.getAllCurrentPossibleMoves();
            System.out.println(grid);
//            System.out.print("Press ENTER to continue...");
//            scanner.nextLine();
            System.out.println("Counting... (" + allMoves.size() + ")");
            Move selectedMove;
            if (PlayerColor.WHITE == grid.getCurrentPlayer()) {
                selectedMove = white.getChosenMove(grid);
            }
            else {
                selectedMove = black.getChosenMove(grid);
            }
            System.out.println("\n----------------------------------------");
            System.out.println(grid.getCurrentPlayer() + ": " + selectedMove);
            grid.executeMove(selectedMove);
        }
//        System.out.println("----------------------------------------\n");
        System.out.println(grid);
        System.out.println("WINNER: " + grid.getWinner());
        System.out.println("----------------------------------------\n");
        if (white instanceof Bot) {
            System.out.println("Stats white bot:");
            ((Bot) white).printStats();
        }
        if (black instanceof Bot) {
            System.out.println("Stats black bot:");
            ((Bot) black).printStats();
        }
        if (black instanceof Bot && white instanceof Bot) {
            return new StatsContainer(
                    ((Bot) white).getAverageNodesVisited(), ((Bot) white).getAverageTimeForMove(), ((Bot) white).maxDepth, ((Bot) white).accessor,
                    ((Bot) black).getAverageNodesVisited(), ((Bot) black).getAverageTimeForMove(), ((Bot) black).maxDepth, ((Bot) black).accessor,
                    grid.getWinner());
        }
        if (white instanceof Bot) {
            return new StatsContainer(
                    ((Bot) white).getAverageNodesVisited(), ((Bot) white).getAverageTimeForMove(), ((Bot) white).maxDepth, ((Bot) white).accessor,
                    0, 0, 0, ((Bot) white).accessor,
                    grid.getWinner());
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        var human = new Human(PlayerColor.BLACK);
//        var randomPlayer = new RandomPlayer(PlayerColor.BLACK);
//        PrintWriter writer = new PrintWriter("F:\\sztuczna_inteligencja\\E3\\src\\stats\\stats_" + (new Timestamp(System.currentTimeMillis())).toString().replace(":", ".") + ".csv", "UTF-8");
//        writer.println("sep=\t");
//        writer.println(StatsContainer.columnsNames());
        for (var depth : new int[]{7}) {
            var botWhite = new BotAlphaBeta(new SimpleAccessor(), PlayerColor.WHITE, depth);
            var botBlack = new BotAlphaBeta(new ComplexGridAccessor(), PlayerColor.BLACK, depth);
            System.out.println("DEPTH: " + depth);
            for (int i = 0; i < 30; i++) {
                System.out.println("CURRENT: " + i);
                var stats = runGame(botWhite, botBlack);
//                writer.println(stats.toString().replace(".", ","));
            }
//            writer.println();
        }
//        writer.close();
    }
}
