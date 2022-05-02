import java.util.ArrayList;


class Move {
    final FigureType figureType;
    final protected GridItem startingPoint;
    protected ArrayList<GridItem> toJumpItems;

    public Move(FigureType figureType, GridItem startingPoint) {
        this.figureType = figureType;
        this.startingPoint = startingPoint;
        toJumpItems = new ArrayList<>();
    }

    public Move(GridItem startingPoint, GridItem next, FigureType figureType) {
        this.startingPoint = startingPoint;
        this.figureType = figureType;
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
    public void add(GridItem toJump) { toJumpItems.add(toJump); }
}
