enum FigureType { CROWNED, NORMAL }
enum PlayerColor { BLACK, WHITE }

public class Figure {
    public final PlayerColor playerColor;
    public final FigureType figureType;

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

    @Override
    public String toString() { return playerColor + " - " + getFigureDisplayChar(); }
}
