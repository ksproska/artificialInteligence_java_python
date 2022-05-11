import java.util.ArrayList;

public class GameSimulatorConsole {

    public static PlayerColor runGame(Player white, Player black) {
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
        return grid.getWinner();
    }

    public static void main(String[] args) {
        var botWhite = new BotMinMax(new SimpleAccessor(), PlayerColor.WHITE, 7);
        var botBlack = new BotMinMax(new ComplexGridAccessor(), PlayerColor.BLACK, 7);
        var botBlack2 = new BotAlphaBeta(new SimpleAccessor(), PlayerColor.BLACK, 9);
        var human = new Human(PlayerColor.BLACK);

        runGame(botWhite, botBlack2);
//        runGame(botWhite, human);
    }
}
