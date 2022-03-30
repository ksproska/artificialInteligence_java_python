import java.util.ArrayList;

class Binary_CSP {
    private final Binary_Problem cspProblem;
    public Binary_CSP(Binary_Problem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<Binary_PartialSolution> getResults() {
        ArrayList<Binary_PartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(Binary_PartialSolution cspPartialSolution,
                                     int currentVariable,
                                     ArrayList<Binary_PartialSolution> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
                accumulator.add(cspPartialSolution);
            }
            return;
        }
        if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
            for (var domainItem : cspPartialSolution.getDomain()) {
                var solutionCopy = cspPartialSolution.copyBinary();
                solutionCopy.setNewValue(domainItem, cspProblem.getVariablesIndexes().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}
