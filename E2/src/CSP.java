import java.util.ArrayList;

class CSP <D, V> {
    private final CspProblem<D, V> cspProblem;
    public CSP(CspProblem<D, V> cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<CspPartialSolution<D, V>> getResults() {
        var accumulator = new ArrayList<CspPartialSolution<D, V>>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(CspPartialSolution<D, V> cspPartialSolution,
                                    int currentVariable,
                                    ArrayList<CspPartialSolution<D, V>> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
                accumulator.add(cspPartialSolution);
            }
            return;
        }
        if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
            for (var domainItem : cspPartialSolution.getDomain()) {
                var solutionCopy = cspPartialSolution.copy();
                solutionCopy.setNewValue(domainItem, cspProblem.getVariables().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}
