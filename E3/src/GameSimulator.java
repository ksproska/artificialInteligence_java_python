import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {
    public static void bot() {
        var playWithBot = true;
        var botBlack = new CheckersBot(new SimpleAccessor(), PlayerColor.BLACK, 6);
        var botWhite = new CheckersBot(new SimpleAccessor(), PlayerColor.WHITE, 6);
        var grid = new CheckersGridHandler();
        grid.basicSetup();
        System.out.println(grid);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Move> allMoves;
        while (!grid.isGameFinished()) {
            System.out.println("Press ENTER: ");
            scanner.nextLine();
            System.out.println("Counting...");
            if (PlayerColor.WHITE == grid.getCurrentPlayer()) {
                var selectedMove = botWhite.getBestMove(grid);
                System.out.println("BOT white: " + selectedMove);
                grid.executeMove(selectedMove);
            }
            else {
                var selectedMove = botBlack.getBestMove(grid);
                System.out.println("BOT black: " + selectedMove);
                grid.executeMove(selectedMove);
            }
            System.out.println("-------------------------------------------");
            allMoves = grid.getAllCurrentPossibleMoves();
            for (int i = 0; i < allMoves.size(); i++) {
                System.out.println(i + ": " + allMoves.get(i));
            }
            System.out.println(grid);
        }
        System.out.println("WINNER: " + grid.getWinner());
    }

    public static void randoms() {
        var grid = new CheckersGridHandler();
        grid.basicSetup();
        Random random = new Random();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Move> allMoves = grid.getAllCurrentPossibleMoves();
        while (!grid.isGameFinished()) {
            System.out.println(grid);
            for (int i = 0; i < allMoves.size(); i++) {
                System.out.println(i + ": " + allMoves.get(i));
            }
            scanner.nextLine();
            Move selected = allMoves.get(random.nextInt(allMoves.size()));
            System.out.println(selected);
            grid.executeMove(selected);
            allMoves = grid.getAllCurrentPossibleMoves();
        }
        System.out.println("WINNER: " + grid.getWinner());
    }

    public static void main(String[] args) {
        bot();
    }
}
