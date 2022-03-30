import consts.FutoshikiEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Futoshiki_PartialSolution extends Grid_PartialSolution<Object, FutoshikiEnum, Integer> {
    public static Integer[] domain; //todo

    private Futoshiki_PartialSolution() {}
    public Futoshiki_PartialSolution(Futoshiki_Problem futoshikiProblem) {
        super(futoshikiProblem);
        domain = new Integer[futoshikiProblem.gridX];
        Arrays.setAll(domain, i -> i + 1);
    }

    public boolean constraint_areThereNoRepetitions() {
        for (var domainItem : gridProblem.overallDomain) {
            var freq = Collections.frequency(columns.get(itemX), domainItem);
            if(freq > 1) {
                return false;
            }
            freq = Collections.frequency(rows.get(itemY), domainItem);
            if(freq > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean constraint_isRightCorrect() {
        if(itemX + 1 < gridProblem.x) {
            var row = rows.get(itemY);
            var value = (Integer) row.get(itemX);
            var sign = row.get(itemX + 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var nextValue = row.get(itemX + 2);
            if(!(nextValue instanceof Integer)) {
                return true;
            }
            if(sign.equals(Futoshiki_Problem.lessThan) && value < (Integer) nextValue) return true;
            if(sign.equals(Futoshiki_Problem.moreThan) && value > (Integer) nextValue) return true;
            return false;
        }
        return true;
    }

    public boolean constraint_isLeftCorrect() {
        if(0 < itemX) {
            var row = rows.get(itemY);
            var value = (Integer) row.get(itemX);
            var sign = row.get(itemX - 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var prevValue = row.get(itemX - 2);
            if(!(prevValue instanceof Integer)) {
                return true;
            }
            if(sign.equals(Futoshiki_Problem.lessThan) && (Integer) prevValue < value) return true;
            if(sign.equals(Futoshiki_Problem.moreThan) && (Integer) prevValue > value) return true;
            return false;
        }
        return true;
    }

    public boolean constraint_isUpCorrect() {
        if(0 < itemY) {
            var column = columns.get(itemX);
            var value = (Integer) column.get(itemY);
            var sign = column.get(itemY - 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var prevValue = column.get(itemY - 2);
            if(!(prevValue instanceof Integer)) {
                return true;
            }
            if(sign.equals(Futoshiki_Problem.lessThan) && (Integer) prevValue < value) return true;
            if(sign.equals(Futoshiki_Problem.moreThan) && (Integer) prevValue > value) return true;
            return false;
        }
        return true;
    }

    public boolean constraint_isDownCorrect() {
        if(itemY + 1 < gridProblem.y) {
            var column = columns.get(itemX);
            var value = (Integer) column.get(itemY);
            var sign = column.get(itemY + 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var nextValue = column.get(itemY + 2);
            if(!(nextValue instanceof Integer)) {
                return true;
            }
            if(sign.equals(Futoshiki_Problem.lessThan) && value < (Integer) nextValue) return true;
            if(sign.equals(Futoshiki_Problem.moreThan) && value > (Integer) nextValue) return true;
            return false;
        }
        return true;
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        return constraint_areThereNoRepetitions() && constraint_isUpCorrect() &&
                constraint_isDownCorrect() && constraint_isLeftCorrect() && constraint_isRightCorrect();
    }

    public Futoshiki_PartialSolution copyFutoshiki() {
        var copiedItem =  new Futoshiki_PartialSolution();
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
    public Integer[] getDomain() { return domain; }

    @Override
    public boolean isSatisfied() { return !partialSolution.contains(null); }
}