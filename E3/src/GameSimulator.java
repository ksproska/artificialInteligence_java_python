import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameSimulator {
    public static void main(String[] args) {
        var playWithBot = false;
        var grid = new CheckersGrid();
        grid.basicSetup();
        System.out.println(grid);

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ArrayList<Move> allMoves = grid.getAllCurrentPossibleMoves();
        while (!grid.isGameFinished()) {
            try {
                for (int i = 0; i < allMoves.size(); i++) {
                    System.out.println(i + ": " + allMoves.get(i));
                }
                System.out.print("\n=> ");
                String nextMove = "";
                if (!playWithBot || PlayerColor.WHITE == grid.getCurrentPlayer()) {
                    nextMove = scanner.nextLine();
                }
                if (nextMove.equals("")) {
                    Move selected = allMoves.get(random.nextInt(allMoves.size()));
                    System.out.println(selected);
                    grid.executeMove(selected);
                    System.out.println(grid);
                }
                else {
                    grid.executeMove(allMoves.get(Integer.parseInt(nextMove)));
                    System.out.println(grid);
                }
                allMoves = grid.getAllCurrentPossibleMoves();
            }
            catch (Exception ignore) {}
        }
        System.out.println("WINNER: " + grid.getWinner());
    }
}
