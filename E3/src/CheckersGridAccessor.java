abstract class CheckersGridAccessor {
    public final double maxOffset;

    protected CheckersGridAccessor(double maxOffset) {
        this.maxOffset = maxOffset;
    }

    double accessCheckersGrid(CheckersGrid checkersGrid, PlayerColor playerColor) { throw new IllegalStateException("Method not implemented"); }
}
