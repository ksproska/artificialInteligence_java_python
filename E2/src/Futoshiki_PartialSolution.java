import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;

import java.util.ArrayList;
import java.util.Arrays;
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

    private int[] countConstraints(Integer variableIndex) {
        var countLessConstraints = 0;
        var countMoreConstraints = 0;
        if(getX(variableIndex) + 1 < gridProblem.x) {
            var list = rows.get(getY(variableIndex));
            var next = list.get(getX(variableIndex) + 1);
            if(Futoshiki_Problem.lessThan.equals(next)) {
                countLessConstraints ++;
            }
            if(Futoshiki_Problem.moreThan.equals(next)) {
                countMoreConstraints ++;
            }
        }
        if(0 < getX(variableIndex)) {
            var list = rows.get(getY(variableIndex));
            var prev = list.get(getX(variableIndex) - 1);
            if(Futoshiki_Problem.lessThan.equals(prev)) {
                countMoreConstraints ++;
            }
            if(Futoshiki_Problem.moreThan.equals(prev)) {
                countLessConstraints ++;
            }
        }
        if(getY(variableIndex) + 1 < gridProblem.y) {
            var list = columns.get(getX(variableIndex));
            var next = list.get(getY(variableIndex) + 1);
            if(Futoshiki_Problem.lessThan.equals(next)) {
                countLessConstraints ++;
            }
            if(Futoshiki_Problem.moreThan.equals(next)) {
                countMoreConstraints ++;
            }
        }
        if(0 < getY(variableIndex)) {
            var list = columns.get(getX(variableIndex));
            var prev = list.get(getY(variableIndex) - 1);
            if(Futoshiki_Problem.lessThan.equals(prev)) {
                countMoreConstraints ++;
            }
            if(Futoshiki_Problem.moreThan.equals(prev)) {
                countLessConstraints ++;
            }
        }
        return new int[]{countLessConstraints, countMoreConstraints};
    }

    @Override
    public Integer getNextVariableIndex(FutoshikiHeuristicEnum chosenHeuristic, Integer variableIndex) {
//        System.out.println(chosenHeuristic);
        switch (chosenHeuristic) {
            case FH_IN_ORDER -> {
                if(cspVariables.size() <= variableIndex) return null;
                return variableIndex + 1;
            }
            case FH_MOST_CONSTRAINTS -> {
                CSP_Variable<Integer> chosen = null;
                Integer chosenCounter = null;
                for (var cspVariable : cspVariables) {
//                System.out.println(chosen);
                    if (!cspVariable.wasVariableUsed) {
                        if (chosen == null) {
                            chosen = cspVariable;
                            chosenCounter = Arrays.stream(countConstraints(chosen.variableIndex)).sum();
//                        System.out.println(chosenCounter);
                        } else {
                            var nextCount = Arrays.stream(countConstraints(cspVariable.variableIndex)).sum();
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
            case FH_MOST_CONSTRAINTS_AND_CHANGE_DOMAIN_ORDER -> {
                CSP_Variable<Integer> chosen = null;
                int[] chosenCounter = null;
                for (var cspVariable : cspVariables) {
//                System.out.println(chosen);
                    if (!cspVariable.wasVariableUsed) {
                        if (chosen == null) {
                            chosen = cspVariable;
                            chosenCounter = countConstraints(chosen.variableIndex);
//                        System.out.println(chosenCounter);
                        } else {
                            var nextCount = countConstraints(cspVariable.variableIndex);
                            if (Arrays.stream(nextCount).sum() > Arrays.stream(chosenCounter).sum()) {
                                chosen = cspVariable;
                                chosenCounter = nextCount;
                            }
                        }
                    }
                }
                if (chosen == null) return null;
//                System.out.println(Arrays.toString(chosenCounter));
                if(chosenCounter[0] < chosenCounter[1]) {
                    Collections.reverse(chosen.getVariableDomain());
//                    System.out.println(chosen.getVariableDomain());
                }
                return cspVariables.indexOf(chosen);
            }
            case FH_SMALLEST_DOMAIN -> {
                CSP_Variable<Integer> chosen = null;
//                System.out.println(cspVariables);
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
//                System.out.println("next: " + chosen);
                return cspVariables.indexOf(chosen);
            }
            case FH_SMALLEST_DOMAIN_AND_CHANGE_DOMAIN_ORDER -> {
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
                if(chosen.getVariableDomain().size() > 1) {
                    var chosenCounter = countConstraints(chosen.variableIndex);
                    if(chosenCounter[0] < chosenCounter[1]) {
                        Collections.reverse(chosen.getVariableDomain());
//                    System.out.println(chosen.getVariableDomain());
                    }
                }
                return cspVariables.indexOf(chosen);
            }
        }
        throw new IllegalStateException("Wrong enum: " + chosenHeuristic);
    }

    @Override
    public Futoshiki_PartialSolution deepClone() {
        var copiedItem =  new Futoshiki_PartialSolution();
        copyTo(copiedItem);
        return copiedItem;
    }
}