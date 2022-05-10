import java.util.ArrayList;
import java.util.Scanner;

public class Human implements Player {
    static Scanner scanner = new Scanner(System.in);

    @Override
    public Move getChosenMove(CheckersGridHandler checkersGridHandler) {
        while (true) {
            try {
                ArrayList<Move> allMoves = checkersGridHandler.checkersGrid.getAllCurrentPossibleMoves();
                for (int i = 0; i < allMoves.size(); i++) {
                    System.out.println(i + ": " + allMoves.get(i));
                }
                System.out.print("=> ");
                var choice = scanner.nextLine();
                return allMoves.get(Integer.parseInt(choice));
            }
            catch (Exception ignore) {}
        }
    }
}
