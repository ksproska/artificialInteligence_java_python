import consts.HeuristicEnum;

import java.util.ArrayList;
import java.util.Scanner;

public class CSP_SolverBacktracking<P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D, E>, S extends CSP_PartialSolution<P, D, E>> implements CSP_Solver<P, D, E, T, S> {
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
        var firstVariableInx = initialPartialSolution.getNextVariableIndex(chosenHeuristic, -1);
//        System.out.println(firstVariable);
        getResultsRecursive((S) initialPartialSolution, accumulator, firstVariableInx);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
                                     ArrayList<S> accumulator,
                                     Integer currentVariableInx) {
        if(cspPartialSolution.isSatisfied()) {
            accumulator.add(cspPartialSolution.deepClone());
            returnsCounter++;
            return;
        }
        if(currentVariableInx == null) {
            returnsCounter++;
            if(accumulator.isEmpty()) { tillFirstReturnsCounter++; }
            return;
        }
        var currentVariable = cspPartialSolution.getCspVariables().get(currentVariableInx);
//        System.out.println(currentVariable.variableIndex);
        for (var domainItem : cspProblem.getDomain()) {
            visitedNodesCounter++;
            if(accumulator.isEmpty()) { tillFirstVisitedNodesCounter++; }
            var changedVariableInx = currentVariable.variableIndex;
            boolean areValuesCorrect = cspPartialSolution.setNewValueAtIndexOf(domainItem, changedVariableInx);
            cspPartialSolution.setVariableUsed(changedVariableInx);
//            System.out.println("\nvi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(cspPartialSolution);
//            System.out.println("ALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter);
//            System.out.println("TILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter);

            if(areValuesCorrect) {
                var nextVariableIndex = cspPartialSolution.getNextVariableIndex(chosenHeuristic, currentVariableInx);
                if (nextVariableIndex == null) {
                    getResultsRecursive(cspPartialSolution, accumulator, null);
                }
                else {
                    getResultsRecursive(cspPartialSolution, accumulator, nextVariableIndex);
                }
            }
            cspPartialSolution.removeValueAtIndexOf(changedVariableInx);
            cspPartialSolution.setVariableReleased(changedVariableInx);
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

    @Override
    public String toString() {
        return "HEURISTIC:  " + chosenHeuristic + "\n" +
                cspProblem +
                "\nALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter +
                "\nTILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter + "\n";
    }
}