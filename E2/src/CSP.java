import java.util.ArrayList;

public class CSP<T extends CSP_Problem, S extends CSP_PartialSolution> {
    private final T cspProblem;
    public CSP(T cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<S> getResults() {
        ArrayList<S> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive((S) cspProblemInitialSolution, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
                                     ArrayList<S> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution);
            return;
        }
        var nextVariable = cspPartialSolution.getNextVariable();
        if (nextVariable == null) return;

        var searchedDomain = new ArrayList<>(nextVariable.getDomain());
        for (int i = 0; i < searchedDomain.size(); i++) {
            var domainItem = searchedDomain.get(i);
            var solutionCopy = cspPartialSolution;
            if(i != searchedDomain.size() - 1) {
                solutionCopy = (S) cspPartialSolution.deepClone();
            }
            var changedVariableInx = nextVariable.variableIndex;
            solutionCopy.setNewValue(domainItem, changedVariableInx);
            boolean areValuesCorrect = solutionCopy.updateVariables(changedVariableInx);
            if(areValuesCorrect) {
                getResultsRecursive(solutionCopy, accumulator);
            }
        }
    }
}
