import consts.HeuristicEnum;

import java.util.ArrayList;

public class CSP_SolverBacktracking<P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D>, S extends CSP_PartialSolution<P, D>> implements CSP_Solver<P, D, E, T, S> {
    private final T cspProblem;
    private int visitedNodesCounter, tillFirstVisitedNodesCounter, returnsCounter, tillFirstReturnsCounter;
    public CSP_SolverBacktracking(T cspProblem) {
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

        getResultsRecursive((S) initialPartialSolution, accumulator, 0);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
                                     ArrayList<S> accumulator,
                                     int currentVariableInx) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution.deepClone());
            returnsCounter++;
            return;
        }
        if(cspPartialSolution.getCspVariables().size() <= currentVariableInx) {
            returnsCounter++;
            if(accumulator.isEmpty()) { tillFirstReturnsCounter++; }
            return;
        }
        var nextVariable = cspPartialSolution.getCspVariables().get(currentVariableInx);
        for (var domainItem : cspProblem.getDomain()) {
            visitedNodesCounter++;
            if(accumulator.isEmpty()) { tillFirstVisitedNodesCounter++; }
            var changedVariableInx = nextVariable.variableIndex;
            boolean areValuesCorrect = cspPartialSolution.setNewValueAtIndexOf(domainItem, changedVariableInx);
//            System.out.println("\nvi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(cspPartialSolution);
//            System.out.println("ALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter);
//            System.out.println("TILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter);
            if(areValuesCorrect) {
                getResultsRecursive(cspPartialSolution, accumulator, currentVariableInx + 1);
            }
            cspPartialSolution.removeValueAtIndexOf(changedVariableInx);
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