import consts.HeuristicEnum;

import java.util.ArrayList;

public class CSP_SolverForwardChecking<P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D, E>, S extends CSP_PartialSolution<P, D, E>> implements CSP_Solver<P, D, E, T, S> {
    private final T cspProblem;
    private int visitedNodesCounter, tillFirstVisitedNodesCounter, returnsCounter, tillFirstReturnsCounter;
    public CSP_SolverForwardChecking(T cspProblem) {
        this.cspProblem = cspProblem;
    }
    private E chosenHeuristic;

    public ArrayList<S> getResults(E chosenHeuristic) {
        this.chosenHeuristic = chosenHeuristic;
        visitedNodesCounter = 0;
        tillFirstReturnsCounter = 0;
        returnsCounter = 0;
        tillFirstVisitedNodesCounter = 0;
        ArrayList<S> accumulator = new ArrayList<>();
        var initialPartialSolution = cspProblem.getInitialPartialSolution();
        getResultsRecursive((S) initialPartialSolution, accumulator);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
                                     ArrayList<S> accumulator) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution);
            returnsCounter++;
            return;
        }
        var nextVariable = cspPartialSolution.getNextVariable();
        if (nextVariable == null) {
            returnsCounter++;
            if(accumulator.isEmpty()) { tillFirstReturnsCounter++; }
            return;
        }

        var searchedDomain = new ArrayList<>(nextVariable.getVariableDomain());
        for (int i = 0; i < searchedDomain.size(); i++) {
            visitedNodesCounter += 1;
            if(accumulator.isEmpty()) { tillFirstVisitedNodesCounter++; }
            var domainItem = searchedDomain.get(i);
            var solutionCopy = cspPartialSolution;
            if(i != searchedDomain.size() - 1) {
                solutionCopy = (S) cspPartialSolution.deepClone();
            }
            var changedVariableInx = nextVariable.variableIndex;
            solutionCopy.setNewValueAtIndexOf(domainItem, changedVariableInx);
            boolean areValuesCorrect = solutionCopy.updateVariables(changedVariableInx);
//            System.out.println("\nvi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(cspPartialSolution);
//            System.out.println("ALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter);
//            System.out.println("TILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter);
            if(areValuesCorrect) {
                getResultsRecursive(solutionCopy, accumulator);
            }
        }
        returnsCounter++;
        if(accumulator.isEmpty()) { tillFirstReturnsCounter++; }
    }

    @Override
    public int getVisitedNodesCounter() { return visitedNodesCounter; }

    @Override
    public int getTillFirstVisitedNodesCounter() { return tillFirstVisitedNodesCounter; }

    @Override
    public int getReturnsCounter() { return returnsCounter; }

    @Override
    public int getTillFirstReturnsCounter() { return tillFirstReturnsCounter; }
}
