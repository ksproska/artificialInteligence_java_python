enum FigureType { CROWNED, NORMAL }
enum PlayerColor { BLACK, WHITE }

public class Figure {
    public final PlayerColor playerColor;
    private FigureType figureType;

    public Figure(PlayerColor playerColor, FigureType figureType) {
        this.playerColor = playerColor;
        this.figureType = figureType;
    }

    public char getFigureDisplayChar() {
        return switch (figureType) {
            case CROWNED ->'ᛗ';
            case NORMAL -> '●';
        };
    }

    public Figure copy() {
        return new Figure(playerColor, figureType);
    }

    @Override
    public String toString() { return playerColor + " - " + getFigureDisplayChar(); }
    public FigureType getFigureType() { return figureType; }
    public void setFigureType(FigureType figureType) { this.figureType = figureType; }
}
