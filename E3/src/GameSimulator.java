import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {

    public static PlayerColor runGame(Player white, Player black) {
        var grid = new CheckersGridHandler();
        grid.basicSetup();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Move> allMoves;
        while (!grid.isGameFinished()) {
            allMoves = grid.getAllCurrentPossibleMoves();
            System.out.println("-------------------------------\nAvailable moves count: " + allMoves.size());
            System.out.println(grid);

//            System.out.print("Press ENTER to continue...");
//            scanner.nextLine();
            System.out.println("Counting...");
            if (PlayerColor.WHITE == grid.getCurrentPlayer()) {
                var selectedMove = white.getChosenMove(grid);
                System.out.println("White: " + selectedMove);
                grid.executeMove(selectedMove);
            }
            else {
                var selectedMove = black.getChosenMove(grid);
                System.out.println("Black: " + selectedMove);
                grid.executeMove(selectedMove);
            }
        }
        System.out.println(grid);
        System.out.println("WINNER: " + grid.getWinner());
        if (white.getClass() == MinMaxBot.class) {
            System.out.println("White bot:");
            ((MinMaxBot) white).printAverages();
        }
        if (black.getClass() == MinMaxBot.class) {
            System.out.println("Black bot:");
            ((MinMaxBot) black).printAverages();
        }
        if (white.getClass() == AlphaBetaBot.class) {
            System.out.println("White bot:");
            ((AlphaBetaBot) white).printAverages();
        }
        if (black.getClass() == AlphaBetaBot.class) {
            System.out.println("Black bot:");
            ((AlphaBetaBot) black).printAverages();
        }
        return grid.getWinner();
    }

    public static void main(String[] args) {
        var botWhite = new MinMaxBot(new SimpleAccessor(), PlayerColor.WHITE, 7);
        var botBlack = new MinMaxBot(new ComplexGridAccessor(), PlayerColor.BLACK, 7);
        var botBlack2 = new AlphaBetaBot(new SimpleAccessor(), PlayerColor.BLACK, 7);
        var human = new Human();

        runGame(botWhite, botBlack2);
//        runGame(botWhite, human);
    }
}
