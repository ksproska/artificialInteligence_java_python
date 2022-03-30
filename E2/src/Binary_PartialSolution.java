import consts.BinaryEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;


public class Binary_PartialSolution extends Grid_PartialSolution<Integer, BinaryEnum, Integer> {
    public static final Integer[] domain = new Integer[]{0, 1};

    private Binary_PartialSolution() {}

    public Binary_PartialSolution(Binary_Problem binaryProblem) {
        super(binaryProblem);
    }

    public boolean updateVariables(Integer variableItem) {
        var variableX = getX(variableItem);
        var variableY = getY(variableItem);
        for (var variableInArray : variables) {
            var tempX = getX(variableInArray.variableIndex);
            var tempY = getY(variableInArray.variableIndex);
            if((tempX == variableX || tempY == variableY) && !variableInArray.variableIndex.equals(variableItem)) {
                for (Iterator<Integer> iterator = variableInArray.getDomain().iterator(); iterator.hasNext(); ) {
                    Integer domainItem = iterator.next();
                    setNewValue(domainItem, variableInArray.variableIndex);
                    if(!isCorrectAfterLastChange) {
                        iterator.remove();
                    }
                    removeNewValue(variableInArray.variableIndex);
                }
                if(!variableInArray.wasVariableUsed && variableInArray.getDomain().isEmpty()) {
                    return false;
                }
            }
            else if (variableInArray.variableIndex.equals(variableItem)) {
                variableInArray.removeAll();
                variableInArray.wasVariableUsed = true;
            }
        }
        return true;
    }

    public CSP_Variable<Integer> getNextFreeVariable() {
        for (var variab : variables) {
            if (!variab.getDomain().isEmpty()) {
                return variab;
            }
        }
        return null;
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
        return constraint_areAllUnique() && constraint_isHalf0AndHalf1() && constraint_areValuesRepeatedMax2TimesInARow();
    }

    public Binary_PartialSolution copyBinary() {
        var copiedItem =  new Binary_PartialSolution();
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
        copiedItem.variables = new ArrayList<>();
        for (var variab : variables) {
            var newVar = new CSP_Variable<Integer>(variab.variableIndex);
            newVar.wasVariableUsed = variab.wasVariableUsed;
            for (var dV : variab.getDomain()) {
                newVar.add(dV);
            }
            copiedItem.variables.add(newVar);
        }
        return copiedItem;
    }

    @Override
    public Integer[] getDomain() { return domain; }

    @Override
    public boolean isSatisfied() { return !partialSolution.contains(null); }
}