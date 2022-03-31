import java.util.ArrayList;

class Binary_CSP {
    private final Binary_Problem cspProblem;
    public Binary_CSP(Binary_Problem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<Binary_PartialSolution> getResults() {
        ArrayList<Binary_PartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(Binary_PartialSolution cspPartialSolution,
                                     ArrayList<Binary_PartialSolution> accumulator) {
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
                solutionCopy = cspPartialSolution.copyBinary();
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
