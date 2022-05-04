import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {
    public static PlayerColor bot() {
        var botWhite = new CheckersBot(new SimpleAccessor(3), PlayerColor.WHITE, 6);
        var botBlack = new CheckersBot(new ComplexGridAccessor(20), PlayerColor.BLACK, 6);
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
                var selectedMove = botWhite.getBestMove(grid);
                System.out.println("White: " + selectedMove);
                grid.executeMove(selectedMove);
            }
            else {
                var selectedMove = botBlack.getBestMove(grid);
                System.out.println("Black: " + selectedMove);
                grid.executeMove(selectedMove);
            }
        }
        System.out.println(grid);
        System.out.println("WINNER: " + grid.getWinner());
        System.out.println("White bot:");
        botWhite.printAverages();
        System.out.println("Black bot:");
        botBlack.printStats();
        return grid.getWinner();
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
        var winnerws = new ArrayList<PlayerColor>();
        for (int i = 0; i < 100; i++) {
            var nextWinner = bot();
            winnerws.add(nextWinner);
        }
        System.out.println(Collections.frequency(winnerws, PlayerColor.WHITE));
        System.out.println(Collections.frequency(winnerws, PlayerColor.BLACK));
//        randoms();
    }
}
