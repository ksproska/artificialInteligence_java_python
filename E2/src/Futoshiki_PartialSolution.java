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

    private int countConstraints(Integer variableIndex) {
        var countConstraints = 0;
        if(getX(variableIndex) + 1 < gridProblem.x) {
            var list = rows.get(getY(variableIndex));
            if(!Futoshiki_Problem.neutral.equals(list.get(getX(variableIndex) + 1))) {
                countConstraints ++;
            }
        }
        if(0 < getX(variableIndex)) {
            var list = rows.get(getY(variableIndex));
            if(!Futoshiki_Problem.neutral.equals(list.get(getX(variableIndex) - 1))) {
                countConstraints ++;
            }
        }
        if(getY(variableIndex) + 1 < gridProblem.y) {
            var list = columns.get(getX(variableIndex));
            if(!Futoshiki_Problem.neutral.equals(list.get(getY(variableIndex) + 1))) {
                countConstraints ++;
            }
        }
        if(0 < getY(variableIndex)) {
            var list = columns.get(getX(variableIndex));
            if(!Futoshiki_Problem.neutral.equals(list.get(getY(variableIndex) - 1))) {
                countConstraints ++;
            }
        }
//        var colLessThan = Collections.frequency(columns.get(getX(variableIndex)), Futoshiki_Problem.lessThan);
//        var colMoreThan = Collections.frequency(columns.get(getX(variableIndex)), Futoshiki_Problem.moreThan);
//        var rowLessThan = Collections.frequency(rows.get(getY(variableIndex)), Futoshiki_Problem.lessThan);
//        var rowMoreThan = Collections.frequency(rows.get(getY(variableIndex)), Futoshiki_Problem.moreThan);
        return countConstraints;
    }

    @Override
    public Integer getNextVariableIndex(FutoshikiHeuristicEnum chosenHeuristic, Integer variableIndex) {
        if(chosenHeuristic == FutoshikiHeuristicEnum.FH_IN_ORDER) {
            if(cspVariables.size() <= variableIndex) return null;
            return variableIndex + 1;
        }
        else if (chosenHeuristic == FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS) {
            CSP_Variable<Integer> chosen = null;
            Integer chosenCounter = null;
            for (var cspVariable : cspVariables) {
//                System.out.println(chosen);
                if (!cspVariable.wasVariableUsed) {
                    if (chosen == null) {
                        chosen = cspVariable;
                        chosenCounter = countConstraints(chosen.variableIndex);
//                        System.out.println(chosenCounter);
                    } else {
                        var nextCount = countConstraints(cspVariable.variableIndex);
                        if (nextCount > chosenCounter) {
                            chosen = cspVariable;
                            chosenCounter = nextCount;
                        }
                    }
                }
            }
            if (chosen == null) return null;
            return cspVariables.indexOf(chosen);
        }
        else if(chosenHeuristic == FutoshikiHeuristicEnum.FH_SMALLEST_DOMAIN) {
            CSP_Variable<Integer> chosen = null;
            for (var cspVariable : cspVariables) {
                if (!cspVariable.wasVariableUsed) {
                    if (!cspVariable.getVariableDomain().isEmpty()) {
                        if (chosen == null || cspVariable.getVariableDomain().size() < chosen.getVariableDomain().size()) {
                            chosen = cspVariable;
                        }
                    }
                }
            }
            if (chosen == null) return null;
            return cspVariables.indexOf(chosen);
        }
        else {
            throw new IllegalStateException("Wrong enum: " + chosenHeuristic);
        }
    }

    @Override
    public Futoshiki_PartialSolution deepClone() {
        var copiedItem =  new Futoshiki_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}