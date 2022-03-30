import java.util.ArrayList;
import java.util.Arrays;

class CSP <P, D extends P, R extends CspProblem<P, D>, S extends CspPartialSolution<P, D>> {
    private final R cspProblem;
    public CSP(R cspProblem) {
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
                System.out.println(solutionCopy);
                System.out.println(currentVariable + " " + domainItem);
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}
