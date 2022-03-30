import java.util.ArrayList;

public class Futoshiki_CSP {
    private final Futoshiki_Problem cspProblem;
    public Futoshiki_CSP(Futoshiki_Problem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<Futoshiki_PartialSolution> getResults() {
        ArrayList<Futoshiki_PartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
        getResultsRecursive(cspProblemInitialSolution, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(Futoshiki_PartialSolution cspPartialSolution,
                                     ArrayList<Futoshiki_PartialSolution> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution);
            return;
        }
        var nextVariable = cspPartialSolution.getNextVariable();
        if (nextVariable == null) return;

        for (var domainItem : new ArrayList<>(nextVariable.getDomain())) {
            var solutionCopy = cspPartialSolution.copyFutoshiki();
            var changedVariableInx = nextVariable.variableIndex;
            solutionCopy.setNewValue(domainItem, changedVariableInx);
            boolean areValuesCorrect = solutionCopy.updateVariables(changedVariableInx);
            if(areValuesCorrect) {
                getResultsRecursive(solutionCopy, accumulator);
            }
        }
    }
}
