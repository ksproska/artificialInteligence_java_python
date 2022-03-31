import java.util.ArrayList;

public class CSP_BacktrackingSolver <P, D extends P, T extends CSP_Problem<P, D>, S extends CSP_PartialSolution<P, D>> {
    private final T cspProblem;
    public CSP_BacktrackingSolver(T cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<S> getResults() {
        ArrayList<S> accumulator = new ArrayList<>();
        var initialPartialSolution = cspProblem.getInitialPartialSolution();

        getResultsRecursive((S) initialPartialSolution, accumulator, 0);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
                                     ArrayList<S> accumulator,
                                     int currentVariableInx) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution.deepClone());
            return;
        }
        if(cspPartialSolution.getCspVariables().size() <= currentVariableInx) {
            return;
        }
        var nextVariable = cspPartialSolution.getCspVariables().get(currentVariableInx);

        for (var domainItem : cspProblem.getDomain()) {
            var changedVariableInx = nextVariable.variableIndex;
            cspPartialSolution.setNewValueAtIndexOf(domainItem, changedVariableInx);
//            System.out.println("vi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(cspPartialSolution);
            boolean areValuesCorrect = cspPartialSolution.checkConstraintsAfterLastChange();
            if(areValuesCorrect) {
                getResultsRecursive(cspPartialSolution, accumulator, currentVariableInx + 1);
            }
            cspPartialSolution.removeValueAtIndexOf(changedVariableInx);
        }
    }
}