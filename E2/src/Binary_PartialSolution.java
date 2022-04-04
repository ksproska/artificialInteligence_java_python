import consts.BinaryEnum;
import consts.BinaryHeuristicEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Binary_PartialSolution extends Grid_PartialSolution<Integer, BinaryEnum, Integer, BinaryHeuristicEnum> {
    private Binary_PartialSolution() {}

    public Binary_PartialSolution(Binary_Problem binaryProblem) {
        super(binaryProblem);
    }

    private boolean checkIfUnique(ArrayList<ArrayList<Integer>> list) {
        for (var elem : list) {
            if (!elem.contains(null)) {
                var freq = Collections.frequency(list, elem);
                if (freq > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean constraint_areAllUnique() {
        if(!columns.get(changedItemX).contains(null) && !checkIfUnique(columns)) {
            return false;
        }
        if(!rows.get(changedItemY).contains(null)) {
            return checkIfUnique(rows);
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

    private boolean areValuesRepeatedMax2TimesInARow(ArrayList<Integer> list) {
        Integer previous = null;
        int counter = 0;
        for (var elem : list) {
            if(elem != null) {
                if(Objects.equals(previous, elem)) {
                    counter ++;
                    if(counter > 2) { return false; }
                }
                else { counter = 1; }
            }
            previous = elem;
        }
        return true;
    }

    private boolean constraint_areValuesRepeatedMax2TimesInARow() {
        return areValuesRepeatedMax2TimesInARow(columns.get(changedItemX)) &&
                areValuesRepeatedMax2TimesInARow(rows.get(changedItemY));
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        return constraint_areAllUnique() && constraint_isHalf0AndHalf1() && constraint_areValuesRepeatedMax2TimesInARow();
    }

    @Override
    public Binary_PartialSolution deepClone() {
        var copiedItem =  new Binary_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}