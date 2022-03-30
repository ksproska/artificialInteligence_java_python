import java.util.ArrayList;

class CSP <P, D extends P, V> {
    private final CspProblem<P, D> cspProblem;
    public CSP(CspProblem<P, D> cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<CspPartialSolution<P, D>> getResults() {
        var accumulator = new ArrayList<CspPartialSolution<P, D>>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(CspPartialSolution<P, D> cspPartialSolution,
                                    int currentVariable,
                                    ArrayList<CspPartialSolution<P, D>> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
                accumulator.add(cspPartialSolution);
            }
            return;
        }
        if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
            for (var domainItem : cspPartialSolution.getDomain()) {
                var solutionCopy = cspPartialSolution.copy();
                solutionCopy.setNewValue(domainItem, cspProblem.getVariablesIndexes().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}
