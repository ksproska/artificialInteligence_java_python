import java.util.ArrayList;


class Move {
    final PlayerColor playerColor;
    final protected GridItem startingPoint;
    protected ArrayList<GridItem> toJumpItems;

    public Move(GridItem startingPoint) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
    }

    public Move(GridItem startingPoint, GridItem next) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
        toJumpItems.add(next);
    }

    public ArrayList<GridItem> getAllJumpedTo() {
        return new ArrayList<GridItem>() {{
            add(startingPoint);
            addAll(toJumpItems);
        }};
    }

    @Override
    public String toString() { return "Move{ " + startingPoint + " -> " + toJumpItems + " }"; }
    public GridItem getStartingPoint() { return startingPoint; }
    public void add(GridItem toJump) { toJumpItems.add(toJump); }

    public Move copy() {
        var copied = new Move(startingPoint.copy());
        for (var toJump : toJumpItems) {
            copied.add(toJump.copy());
        }
        return copied;
    }
}
