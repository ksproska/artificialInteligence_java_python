public class Figure {
    public final PlayerColor playerColor;
    private FigureType figureType;

    public Figure(PlayerColor playerColor, FigureType figureType) {
        this.playerColor = playerColor;
        this.figureType = figureType;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public void setFigureType(FigureType figureType) {
        this.figureType = figureType;
    }

    public char getFigureDisplayChar() {
        if (figureType == FigureType.NORMAL) {
            return '●';
        }
        return 'ᛗ';
    }

    @Override
    public String toString() {
        return playerColor + " - " + getFigureDisplayChar();
    }
}
