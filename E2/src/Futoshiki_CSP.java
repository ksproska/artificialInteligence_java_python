import java.util.ArrayList;
public class Futoshiki_CSP {
    private final Futoshiki_Problem cspProblem;
    public Futoshiki_CSP(Futoshiki_Problem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<Futoshiki_PartialSolution> getResults() {
        ArrayList<Futoshiki_PartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, 0, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(Futoshiki_PartialSolution cspPartialSolution,
                                     int currentVariable,
                                     ArrayList<Futoshiki_PartialSolution> accumulator) {
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
