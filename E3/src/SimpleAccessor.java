public class SimpleAccessor extends CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;

    @Override
    public int accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor, MinMaxEnum whoseTurn) {
//        if (whoseTurn != null) {
//            return switch (whoseTurn) {
//                case MIN -> Integer.MIN_VALUE;
//                case MAX -> Integer.MAX_VALUE;
//            };
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
