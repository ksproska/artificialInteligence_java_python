public class SimpleAccessor extends CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;

    @Override
    public double accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor, MinMaxBot.MinMaxEnum whoseTurn) {
        if (checkersGrid.getAllCurrentPossibleMoves().isEmpty()) {
            return switch (whoseTurn) {
                case MIN -> Double.MAX_VALUE;
                case MAX -> Double.MIN_VALUE;
            };
        }
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
