import java.util.ArrayList;

class BinaryCSP {
    private final BinaryProblem cspProblem;
    public BinaryCSP(BinaryProblem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<BinaryPartialSolution> getResults() {
        ArrayList<BinaryPartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(BinaryPartialSolution cspPartialSolution,
                                    int currentVariable,
                                    ArrayList<BinaryPartialSolution> accumulator) {
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
