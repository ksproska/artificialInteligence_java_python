import java.util.ArrayList;

public class GuiHuman implements Player {
    final private CheckersGuiGame checkersGuiGame;

    public GuiHuman(CheckersGuiGame checkersGuiGame) {
        this.checkersGuiGame = checkersGuiGame;
    }

    public CheckersGuiGame getCheckersGuiGame() {
        return checkersGuiGame;
    }

    @Override
    public Move getChosenMove(CheckersGridHandler checkersGridHandler) {
        ArrayList<Move> allMoves = checkersGridHandler.checkersGrid.getAllCurrentPossibleMoves();
        while (true) {
            try {
                if (checkersGuiGame.getToItem() != null) {
                    for (var move : allMoves) {
                        if (move.startingPoint == checkersGuiGame.getFromItem()) {
                            if (move.toJumpItems.get(move.toJumpItems.size() - 1) == checkersGuiGame.getToItem()) {
                                return move;
                            }
                        }
                    }
                    System.out.println("INCORRECT INPUT\nPossible moves:");
                    for (var move : allMoves) {
                        System.out.println(move);
                    }
                    checkersGuiGame.resetInput();
                }
            }
            catch (Exception ignore) {}
        }
    }
}