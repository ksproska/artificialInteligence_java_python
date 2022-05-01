import java.util.ArrayList;


class Move {
    final PlayerColor playerColor;
    final protected GridItemSnapshot startingPoint;
    protected ArrayList<GridItemSnapshot> toJumpItems;

    public Move(GridItemSnapshot startingPoint) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
    }

    public Move(GridItemSnapshot startingPoint, GridItemSnapshot next) {
        this.startingPoint = startingPoint;
        this.playerColor = startingPoint.getPlayerColor();
        toJumpItems = new ArrayList<>();
        toJumpItems.add(next);
    }

    public ArrayList<GridItemSnapshot> getAllJumpedTo() {
        return new ArrayList<GridItemSnapshot>() {{
            add(startingPoint);
            addAll(toJumpItems);
        }};
    }

    @Override
    public String toString() { return "Move{ " + startingPoint + " -> " + toJumpItems + " }"; }
    public GridItemSnapshot getStartingPoint() { return startingPoint; }
    public void add(GridItemSnapshot toJump) { toJumpItems.add(toJump); }

    public Move copy() {
        var copied = new Move(startingPoint.copy());
        for (var toJump : toJumpItems) {
            copied.add(toJump.copy());
        }
        return copied;
    }
}
