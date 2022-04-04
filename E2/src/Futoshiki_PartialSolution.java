import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;

import java.util.ArrayList;
import java.util.Collections;


public class Futoshiki_PartialSolution extends Grid_PartialSolution<Object, FutoshikiEnum, Integer, FutoshikiHeuristicEnum> {

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

    private boolean areCorrectOrder(Integer valuePrev, Object sign, Integer valueNext) {
        if(sign.equals(Futoshiki_Problem.lessThan) && valuePrev < valueNext) return true;
        if(sign.equals(Futoshiki_Problem.moreThan) && valuePrev > valueNext) return true;
        return false;
    }

    private boolean isNextCorrect(ArrayList<Object> list, int index) {
        var valuePrev = (Integer) list.get(index);

        var sign = list.get(index + 1);
        if(sign.equals(Futoshiki_Problem.neutral)) { return true; }

        var valueNext = list.get(index + 2);
        if(!(valueNext instanceof Integer)) { return true; }

        return areCorrectOrder(valuePrev, sign, (Integer) valueNext);
    }

    private boolean isPrevCorrect(ArrayList<Object> list, int index) {
        var valueNext = (Integer) list.get(index);

        var sign = list.get(index - 1);
        if(sign.equals(Futoshiki_Problem.neutral)) { return true; }

        var valuePrev = list.get(index - 2);
        if(!(valuePrev instanceof Integer)) { return true; }

        return areCorrectOrder((Integer) valuePrev, sign, valueNext);
    }

    public boolean constraint_isRightCorrect() {
        if(changedItemX + 1 < gridProblem.x) {
            var list = rows.get(changedItemY);
            return isNextCorrect(list, changedItemX);
        }
        return true;
    }

    public boolean constraint_isLeftCorrect() {
        if(0 < changedItemX) {
            var list = rows.get(changedItemY);
            return isPrevCorrect(list, changedItemX);
        }
        return true;
    }

    public boolean constraint_isUpCorrect() {
        if(0 < changedItemY) {
            var list = columns.get(changedItemX);
            return isPrevCorrect(list, changedItemY);
        }
        return true;
    }

    public boolean constraint_isDownCorrect() {
        if(changedItemY + 1 < gridProblem.y) {
            var list = columns.get(changedItemX);
            return isNextCorrect(list, changedItemY);
        }
        return true;
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        return constraint_areThereNoRepetitions() &&
                constraint_isUpCorrect() && constraint_isDownCorrect() &&
                constraint_isLeftCorrect() && constraint_isRightCorrect();
    }

    @Override
    public Futoshiki_PartialSolution deepClone() {
        var copiedItem =  new Futoshiki_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}