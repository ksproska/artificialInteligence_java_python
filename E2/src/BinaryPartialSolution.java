import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class GridPartialSolution<P, E extends Enum, D extends P> implements CspPartialSolution<P, D> {
    public GridProblem<P, E, D, GridPartialSolution<P, E, D>> gridProblem;
//    protected static D[] domain;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected Integer lastChangedPosition;
    protected boolean isCorrectAfterLastChange;
    protected int itemX, itemY;

    public <G extends GridProblem> GridPartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<P>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        isCorrectAfterLastChange = true;
    }

    public int getX(int position) { return position % gridProblem.x; }
    public int getY(int position) { return position / gridProblem.x; }

    protected GridPartialSolution() {}

    protected void setRowsAndColumns(int x, int y) {
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < y; i++) {
            rows.add(new ArrayList<>());
        }
        for (int i = 0; i < this.partialSolution.size(); i++) {
            var tempX = getX(i);
            var tempY = getY(i);
            rows.get(tempY).add(tempX, partialSolution.get(i));
            columns.get(tempX).add(tempY, partialSolution.get(i));
        }
    }

    @Override
    public GridPartialSolution<P, E, D> copy() {
        var copiedItem = new GridPartialSolution<P, E, D>();
        copiedItem.gridProblem = gridProblem;
        copiedItem.partialSolution = new ArrayList<>(partialSolution);
        copiedItem.rows = new ArrayList<>();
        for (var row : rows) {
            copiedItem.rows.add(new ArrayList<>(row));
        }
        copiedItem.columns = new ArrayList<>();
        for (var column : columns) {
            copiedItem.columns.add(new ArrayList<>(column));
        }
        copiedItem.lastChangedPosition = lastChangedPosition;
        copiedItem.isCorrectAfterLastChange = isCorrectAfterLastChange;
        return copiedItem;
    }

    @Override
    public void setNewValue(D domainItem, Integer variableItem) {
        if(!isCorrectAfterLastChange) {
            throw new IllegalStateException("Solution incorrect after last change");
        }
        if(partialSolution.get(variableItem) != null) {
            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
        }
        lastChangedPosition = variableItem;
        partialSolution.set(variableItem, domainItem);
        itemX = getX(variableItem);
        itemY = getY(variableItem);
        rows.get(itemY).set(itemX, domainItem);
        columns.get(itemX).set(itemY, domainItem);
        isCorrectAfterLastChange = this.checkConstraintsAfterLastChange();
    }

    @Override
    public boolean isSatisfied() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public D[] getDomain() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public boolean areConstraintsNotBrokenAfterLastChange() { return isCorrectAfterLastChange; }

    @Override
    public ArrayList<P> getPartialSolution() { return partialSolution; }

    @Override
    public String toString() {
        return gridProblem.chosenProblem + "\n" + gridProblem.toDisplay(partialSolution) + isCorrectAfterLastChange;
    }
}

class BinaryPartialSolution extends GridPartialSolution<Integer, BinaryEnum, Integer> {
    public static final Integer[] domain = new Integer[]{0, 1};

    public BinaryPartialSolution(BinaryProblem binaryProblem) {
        super(binaryProblem);
    }

    private boolean constraint_areAllUnique() {
        if(!columns.get(itemX).contains(null)) {
            for (var column : columns) {
                if (!column.contains(null)) {
                    var freq = Collections.frequency(columns, column);
                    if (freq > 1) {
                        return false;
                    }
                }
            }
        }
        if(!rows.get(itemY).contains(null)) {
            for (var row : rows) {
                if (!row.contains(null)) {
                    var freq = Collections.frequency(rows, row);
                    if (freq > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean constraint_isHalf0AndHalf1() {
        var freq0 = Collections.frequency(columns.get(itemX), 0);
        var freq1 = Collections.frequency(columns.get(itemX), 1);
        if (freq0 > gridProblem.y/2 || freq1 > gridProblem.y/2) {
            return false;
        }

        freq0 = Collections.frequency(rows.get(itemY), 0);
        freq1 = Collections.frequency(rows.get(itemY), 1);
        if (freq0 > gridProblem.x/2 || freq1 > gridProblem.x/2) {
            return false;
        }
        return true;
    }

    private boolean constraint_areValuesRepeatedMax2TimesInARow() {
        Integer previous = null;
        int counter = 0;
        for (var elem : columns.get(itemX)) {
            if(elem != null) {
                if(Objects.equals(previous, elem)) {
                    counter ++;
                    if(counter > 2) {
                        return false;
                    }
                }
                else {
                    counter = 1;
                }
            }
            previous = elem;
        }

        previous = null;
        counter = 0;
        for (var elem : rows.get(itemY)) {
            if(elem != null) {
                if(Objects.equals(previous, elem)) {
                    counter ++;
                    if(counter > 2) {
                        return false;
                    }
                }
                else {
                    counter = 1;
                }
            }
            previous = elem;
        }
        return true;
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        System.out.println(constraint_areAllUnique());
        System.out.println(constraint_isHalf0AndHalf1());
        System.out.println(constraint_areValuesRepeatedMax2TimesInARow());
        return constraint_areAllUnique() && constraint_isHalf0AndHalf1() && constraint_areValuesRepeatedMax2TimesInARow();
    }

//    @Override
//    public void setNewValue(Integer domainItem, Integer variableItem) {
//        super.setNewValue(domainItem, variableItem);
//        isCorrectAfterLastChange = checkConstraintsAfterLastChange();
//    }

    @Override
    public Integer[] getDomain() { return domain; }

    @Override
    public boolean isSatisfied() { return !partialSolution.contains(null); }
}