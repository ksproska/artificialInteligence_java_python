import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RandomPlayer implements Player {
    public final PlayerColor playerColor;
    private static Random random = new Random();

    public RandomPlayer(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public Move getChosenMove(CheckersGridHandler checkersGridHandler) {
        ArrayList<Move> allMoves = checkersGridHandler.checkersGrid.getAllCurrentPossibleMoves();
        return allMoves.get(random.nextInt(allMoves.size()));
    }

    @Override
    public PlayerColor getPlayerColor() {
        return playerColor;
    }
}