public class SimpleAccessor implements CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;
    @Override
    public int accessCheckersGrid(CheckersGridHandler checkersGridHandler, PlayerColor playerColor) {
        if (checkersGridHandler.isGameFinished()) {
            if (checkersGridHandler.wasDrawn()) return 0;
            if (checkersGridHandler.getWinner() == playerColor) return Integer.MAX_VALUE;
            return Integer.MIN_VALUE;
        }
        int countCurrent = 0;

        for (var item : checkersGridHandler.getAllFilledItems()) {
            var multiplier = 1;
            if (item.getPlayerColor() != playerColor) {
                multiplier = -1;
            }
            if (item.getFigure().getFigureType() == FigureType.NORMAL) {
                countCurrent += normalWeight * multiplier;
            }
            else {
                countCurrent += crownedWeight * multiplier;
            }
        }
        return countCurrent;
    }

    @Override
    public int accessMove(Move move, PlayerColor playerColor) {
        int countCurrent = 0;
        if (move.getClass() == Jump.class) {
            for (var item : ((Jump) move).getJumpOverItems()) {
                var multiplier = 1;
                if (item.getPlayerColor() != playerColor) {
                    multiplier = -1;
                }
                if (item.getFigure().getFigureType() == FigureType.NORMAL) {
                    countCurrent += normalWeight * multiplier;
                }
                else {
                    countCurrent += crownedWeight * multiplier;
                }
            }
        }
        return countCurrent;
    }
}
