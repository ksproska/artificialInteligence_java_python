import java.util.ArrayList;
import java.util.stream.Collectors;

class Jump extends Move {
    protected ArrayList<GridItem> jumpOverItems;

    public Jump(GridItem startingPoint) {
        super(startingPoint);
        jumpOverItems = new ArrayList<>();
    }

    public void add(GridItem jumpOver, GridItem toJump) {
        jumpOverItems.add(jumpOver);
        toJumpItems.add(toJump);
    }

    public Jump shallowCopy() {
        var copied = new Jump(startingPoint);
        copied.jumpOverItems = new ArrayList<>(jumpOverItems);
        copied.toJumpItems = new ArrayList<>(toJumpItems);
        return copied;
    }

    @Override
    public Jump copy() {
        var copied = new Jump(startingPoint.copy());
        for (var item : toJumpItems) {
            copied.add(item.copy());
        }
        for (var item : jumpOverItems) {
            copied.jumpOverItems.add(item.copy());
        }
        return copied;
    }

    @Override
    public String toString() {
        return "Jump (" + size() + ") {" + startingPoint +
                "-> " + toJumpItems.stream().map(Object::toString).collect(Collectors.joining("-> ")) +
                " over: " + jumpOverItems +
                '}';
    }

    public boolean contains(GridItem toJump) { return toJumpItems.contains(toJump); }
    public int size() { return toJumpItems.size() + 1; }
    public boolean wasAlreadyJumpedOver(GridItem jumpedOver) { return jumpOverItems.contains(jumpedOver); }

    public ArrayList<GridItem> getJumpOverItems() {
        return jumpOverItems;
    }
}
