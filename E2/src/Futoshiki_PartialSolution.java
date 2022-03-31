import consts.FutoshikiEnum;
import java.util.Collections;


public class Futoshiki_PartialSolution extends Grid_PartialSolution<Object, FutoshikiEnum, Integer> {

    private Futoshiki_PartialSolution() {}
    public Futoshiki_PartialSolution(Futoshiki_Problem futoshikiProblem) {
        super(futoshikiProblem);
    }

    public boolean constraint_areThereNoRepetitions() {
        for (var domainItem : gridProblem.overallDomain) {
            var freq = Collections.frequency(columns.get(changedItemX), domainItem);
            if(freq > 1) { return false; }

            freq = Collections.frequency(rows.get(changedItemY), domainItem);
            if(freq > 1) { return false; }
        }
        return true;
    }

    public boolean constraint_isRightCorrect() {
        if(changedItemX + 1 < gridProblem.x) {
            var row = rows.get(changedItemY);
            var value = (Integer) row.get(changedItemX);
            var sign = row.get(changedItemX + 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var nextValue = row.get(changedItemX + 2);
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
        if(0 < changedItemX) {
            var row = rows.get(changedItemY);
            var value = (Integer) row.get(changedItemX);
            var sign = row.get(changedItemX - 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var prevValue = row.get(changedItemX - 2);
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
        if(0 < changedItemY) {
            var column = columns.get(changedItemX);
            var value = (Integer) column.get(changedItemY);
            var sign = column.get(changedItemY - 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var prevValue = column.get(changedItemY - 2);
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
        if(changedItemY + 1 < gridProblem.y) {
            var column = columns.get(changedItemX);
            var value = (Integer) column.get(changedItemY);
            var sign = column.get(changedItemY + 1);
            if(sign.equals(Futoshiki_Problem.neutral)) {
                return true;
            }
            var nextValue = column.get(changedItemY + 2);
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

    @Override
    public Futoshiki_PartialSolution deepClone() {
        var copiedItem =  new Futoshiki_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}