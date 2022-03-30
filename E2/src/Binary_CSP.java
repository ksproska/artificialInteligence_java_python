import java.util.ArrayList;

class Binary_CSP {
    private final Binary_Problem cspProblem;
    public Binary_CSP(Binary_Problem cspProblem) {
        this.cspProblem = cspProblem;
    }

    public ArrayList<Binary_PartialSolution> getResults() {
        ArrayList<Binary_PartialSolution> accumulator = new ArrayList<>();
        var cspProblemInitialSolution = cspProblem.getInitialSolution();
//        System.out.println(cspProblemInitialSolution.variables);
        getResultsRecursive(cspProblemInitialSolution, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(Binary_PartialSolution cspPartialSolution,
                                     ArrayList<Binary_PartialSolution> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution);
//            System.out.println("founded");
            return;
        }
        var nextVariable = cspPartialSolution.getNextFreeVariable();
        if (nextVariable == null) return;

//        System.out.println("xx" + nextVariable);
        for (var domainItem : new ArrayList<>(nextVariable.getDomain())) {
            var solutionCopy = cspPartialSolution.copyBinary();
            var changedVariableInx = nextVariable.variableIndex;
            solutionCopy.setNewValue(domainItem, changedVariableInx);
            boolean areValuesCorrect = solutionCopy.updateVariables(changedVariableInx);
//            System.out.println("aft" + solutionCopy.variables);
            if(areValuesCorrect) {
                getResultsRecursive(solutionCopy, accumulator);
            }
        }
    }
}
