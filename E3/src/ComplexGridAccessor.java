public class ComplexGridAccessor extends CheckersGridAccessor {
    private static int normalWeight = 1, crownedWeight = 5;

    public ComplexGridAccessor(int maxOffset) {
        super(maxOffset);
    }

    int getAreaNumber(GridItem gridItem) {
        if (gridItem.letter == GridItemLetter.A || gridItem.letter == GridItemLetter.H) {
            if (gridItem.number == 1 || gridItem.number == 8) {
                return 3;
            }
        }
        if (gridItem.letter == GridItemLetter.B || gridItem.letter == GridItemLetter.G) {
            if (gridItem.number == 2 || gridItem.number == 7) {
                return 2;
            }
        }
        return 1;
    }

    @Override
    public int accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor) {
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