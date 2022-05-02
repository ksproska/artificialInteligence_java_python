import java.util.ArrayList;
import java.util.stream.Collectors;

class Jump extends Move {
    protected ArrayList<GridItem> jumpOverItems;

    public Jump(FigureType figureType, GridItem startingPoint) {
        super(figureType, startingPoint);
        jumpOverItems = new ArrayList<>();
    }

    public void add(GridItem jumpOver, GridItem toJump) {
        jumpOverItems.add(jumpOver);
        toJumpItems.add(toJump);
    }

    public Jump(FigureType figureType, GridItem startingPoint, ArrayList<GridItem> jumpOverItems, ArrayList<GridItem> toJumpItems) {
        super(figureType, startingPoint);
        this.jumpOverItems = new ArrayList<>(jumpOverItems);
        this.toJumpItems = new ArrayList<>(toJumpItems);
    }

    public Jump shallowCopy() {
        return new Jump(figureType, startingPoint, jumpOverItems, toJumpItems);
    }

    @Override
    public String toString() {
        return "Jump (" + size() + ") {" + startingPoint +
                "-> " + toJumpItems.stream().map(Object::toString).collect(Collectors.joining("-> ")) +
                " over: " + jumpOverItems +
                '}';
    }

    public int size() { return toJumpItems.size() + 1; }
    public boolean wasAlreadyJumpedOver(GridItem jumpedOver) { return jumpOverItems.contains(jumpedOver); }
}
