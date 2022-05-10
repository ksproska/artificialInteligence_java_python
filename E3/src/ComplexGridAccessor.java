public class ComplexGridAccessor extends CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;

    private double getAreaNumber(GridItem gridItem) {
        if (gridItem.letter == GridItemLetter.A || gridItem.letter == GridItemLetter.H) {
            if (gridItem.number == 1 || gridItem.number == 8) {
                return 1.3;
            }
        }
        if (gridItem.letter == GridItemLetter.B || gridItem.letter == GridItemLetter.G) {
            if (gridItem.number == 2 || gridItem.number == 7) {
                return 1.1;
            }
        }
        return 1;
    }

    @Override
    public int accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor, MinMaxEnum whoseTurn) {
//        if (whoseTurn != null) {
//            return switch (whoseTurn) {
//                case MIN -> Integer.MAX_VALUE;
//                case MAX -> Integer.MIN_VALUE;
//            };
//        }

        int countCurrent = 0;

        for (var row : checkersGrid.getFullGrid()) {
            for (var item : row) {
                var figure = checkersGrid.getFigure(item);
                if (figure != null) {
                    var multiplier = 1;
                    if (figure.playerColor != playerColor) {
                        multiplier = -1;
                    }
                    multiplier *= getAreaNumber(item);
                    if (figure.figureType == FigureType.NORMAL) {
                        countCurrent += normalWeight * multiplier;
                    }
                    else {
                        countCurrent += crownedWeight * multiplier;
                    }
                }
            }
        }
        return countCurrent;
    }
}