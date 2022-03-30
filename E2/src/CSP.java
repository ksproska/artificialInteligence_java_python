import java.util.ArrayList;

class CSP {
    private final BinaryProblem cspProblem;
    public CSP(BinaryProblem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<BinaryProblem.BinaryPartialSolution> getResults() {
        ArrayList<BinaryProblem.BinaryPartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(BinaryProblem.BinaryPartialSolution cspPartialSolution,
                                    int currentVariable,
                                    ArrayList<BinaryProblem.BinaryPartialSolution> accumulator) {
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
