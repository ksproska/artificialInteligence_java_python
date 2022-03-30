import consts.BinaryEnum;

import java.util.Collections;
import java.util.Objects;


public class Binary_PartialSolution extends Grid_PartialSolution<Integer, BinaryEnum, Integer> {
    public static final Integer[] domain = new Integer[]{0, 1};

    private Binary_PartialSolution() {}

    public Binary_PartialSolution(Binary_Problem binaryProblem) {
        super(binaryProblem);
    }

    private boolean constraint_areAllUnique() {
        if(!columns.get(changedItemX).contains(null)) {
            for (var column : columns) {
                if (!column.contains(null)) {
                    var freq = Collections.frequency(columns, column);
                    if (freq > 1) {
                        return false;
                    }
                }
            }
        }
        if(!rows.get(changedItemY).contains(null)) {
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
        var freq0 = Collections.frequency(columns.get(changedItemX), 0);
        var freq1 = Collections.frequency(columns.get(changedItemX), 1);
        if (freq0 > gridProblem.y/2 || freq1 > gridProblem.y/2) {
            return false;
        }

        freq0 = Collections.frequency(rows.get(changedItemY), 0);
        freq1 = Collections.frequency(rows.get(changedItemY), 1);
        if (freq0 > gridProblem.x/2 || freq1 > gridProblem.x/2) {
            return false;
        }
        return true;
    }

    private boolean constraint_areValuesRepeatedMax2TimesInARow() {
        Integer previous = null;
        int counter = 0;
        for (var elem : columns.get(changedItemX)) {
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
        for (var elem : rows.get(changedItemY)) {
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
        return constraint_areAllUnique() && constraint_isHalf0AndHalf1() && constraint_areValuesRepeatedMax2TimesInARow();
    }

    public Binary_PartialSolution copyBinary() {
        var copiedItem =  new Binary_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}