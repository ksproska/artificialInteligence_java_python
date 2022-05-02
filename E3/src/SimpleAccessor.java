import java.util.ArrayList;

public class SimpleAccessor implements CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;
    @Override
    public int accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor) {
//        if (checkersGrid.isGameFinished()) {
//            if (checkersGrid.wasDrawn()) return 0;
//            if (checkersGrid.getWinner() == playerColor) return Integer.MAX_VALUE;
//            return Integer.MIN_VALUE;
//        }
        int countCurrent = 0;

        for (var figure : checkersGrid.getAllFilledItems()) {
            if (figure != null) {
                var multiplier = 1;
                if (figure.playerColor != playerColor) {
                    multiplier = -1;
                }
                if (figure.figureType == FigureType.NORMAL) {
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
