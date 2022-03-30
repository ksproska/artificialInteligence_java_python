import java.util.ArrayList;
public class FutoshikiCSP {
    private final FutoshikiProblem cspProblem;
    public FutoshikiCSP(FutoshikiProblem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<FutoshikiPartialSolution> getResults() {
        ArrayList<FutoshikiPartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(FutoshikiPartialSolution cspPartialSolution,
                                     int currentVariable,
                                     ArrayList<FutoshikiPartialSolution> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
                accumulator.add(cspPartialSolution);
            }
            return;
        }
        if(cspPartialSolution.areConstraintsNotBrokenAfterLastChange()) {
            for (var domainItem : cspPartialSolution.getDomain()) {
                var solutionCopy = cspPartialSolution.copyFutoshiki();
                solutionCopy.setNewValue(domainItem, cspProblem.getVariablesIndexes().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}
