import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {
    public static void main(String[] args) {
        var playWithBot = true;
        var botBlack = new CheckersBot(new SimpleAccessor(), PlayerColor.BLACK, 6);
        var botWhite = new CheckersBot(new SimpleAccessor(), PlayerColor.WHITE, 6);
        var grid = new CheckersGridHandler();
        grid.basicSetup();
        System.out.println(grid);

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ArrayList<Move> allMoves = grid.getAllCurrentPossibleMoves();
        while (!grid.isGameFinished()) {
//            try {

                scanner.nextLine();
                System.out.println("Counting...");
            if (PlayerColor.WHITE == grid.getCurrentPlayer()) {
//                    System.out.println("test");
                    var selectedMove = botWhite.getBestMove(grid);
                    System.out.println("BOT white: " + selectedMove);
                    grid.executeMove(selectedMove);
//                    System.out.print("\n=> ");
//                    String nextMove = scanner.nextLine();
//                    if (nextMove.equals("")) {
//                        Move selected = allMoves.get(random.nextInt(allMoves.size()));
//                        System.out.println(selected);
//                        grid.executeMove(selected);
//                        System.out.println(grid);
//                    }
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
//            }
//            catch (Exception ignore) {}
        }
        System.out.println("WINNER: " + grid.getWinner());
    }
}
