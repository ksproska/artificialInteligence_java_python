import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

class BinaryPartialSolution implements CspPartialSolution<Integer, Integer> {
    public BinaryProblem binaryProblem;
    public static final Integer[] domain = new Integer[]{0, 1};
    public ArrayList<Integer> partialSolution;
    public ArrayList<ArrayList<Integer>> rows, columns;
    private Integer lastChangedPosition;
    private boolean isCorrectAfterLastChange;
    private int itemX, itemY;

    public int getX(int position) { return position % binaryProblem.x; }
    public int getY(int position) { return position / binaryProblem.x; }

    public BinaryPartialSolution() {}

    public BinaryPartialSolution(BinaryProblem binaryProblem) {
        this.binaryProblem = binaryProblem;
        this.partialSolution = new ArrayList<>(binaryProblem.problem);

        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < binaryProblem.x; i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < binaryProblem.y; i++) {
            rows.add(new ArrayList<>());
        }
        for (int i = 0; i < this.partialSolution.size(); i++) {
            var tempX = getX(i);
            var tempY = getY(i);
            rows.get(tempY).add(tempX, partialSolution.get(i));
            columns.get(tempX).add(tempY, partialSolution.get(i));
        }
        isCorrectAfterLastChange = true;
    }

    @Override
    public BinaryPartialSolution copy() {
        var copiedItem = new BinaryPartialSolution();
        copiedItem.binaryProblem = binaryProblem;
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
    public void setNewValue(Integer domainItem, Integer variableItem) {
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
        isCorrectAfterLastChange = wereNegativelyAffected();
    }

    private boolean areAllUnique() {
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

    private boolean isHalf0AndHalf1() {
        var freq0 = Collections.frequency(columns.get(itemX), 0);
        var freq1 = Collections.frequency(columns.get(itemX), 1);
        if (freq0 > binaryProblem.y/2 || freq1 > binaryProblem.y/2) {
            return false;
        }

        freq0 = Collections.frequency(rows.get(itemY), 0);
        freq1 = Collections.frequency(rows.get(itemY), 1);
        if (freq0 > binaryProblem.x/2 || freq1 > binaryProblem.x/2) {
            return false;
        }
        return true;
    }

    private boolean areValuesRepeatedMax2TimesInARow() {
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
    public boolean wereNegativelyAffected() {
        return areAllUnique() && isHalf0AndHalf1() && areValuesRepeatedMax2TimesInARow();
    }

    @Override
    public Integer[] getDomain() { return domain; }

    @Override
    public boolean areConstraintsNotBrokenAfterLastChange() { return isCorrectAfterLastChange; }

    @Override
    public boolean isSatisfied() { return !partialSolution.contains(null); }

    @Override
    public ArrayList<Integer> getPartialSolution() { return partialSolution; }

    @Override
    public String toString() {
        return binaryProblem.chosenProblem + "\n" + binaryProblem.toDisplay(partialSolution) + isCorrectAfterLastChange;
    }
}