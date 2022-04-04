import java.util.ArrayList;

public class CSP_SolverBacktracking<P, D extends P, T extends CSP_Problem<P, D>, S extends CSP_PartialSolution<P, D>> implements CSP_Solver<P, D, T, S> {
    private final T cspProblem;
    private int stepsCounter;
    public CSP_SolverBacktracking(T cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<S> getResults() {
        stepsCounter = 0;
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
            stepsCounter += 1;
            var changedVariableInx = nextVariable.variableIndex;
            boolean areValuesCorrect = cspPartialSolution.setNewValueAtIndexOf(domainItem, changedVariableInx);
//            System.out.println("vi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(cspPartialSolution);
            if(areValuesCorrect) {
                getResultsRecursive(cspPartialSolution, accumulator, currentVariableInx + 1);
            }
            cspPartialSolution.removeValueAtIndexOf(changedVariableInx);
        }
    }

    @Override
    public int getStepsCounter() {
        return stepsCounter;
    }
}