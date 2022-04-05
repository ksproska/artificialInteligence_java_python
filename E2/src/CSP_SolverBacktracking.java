import consts.HeuristicEnum;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;


public class CSP_SolverBacktracking<P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D, E>, S extends CSP_PartialSolution<P, D, E>> implements CSP_Solver<P, D, E, T, S> {
    private final T cspProblem;
    private int visitedNodesCounter, tillFirstVisitedNodesCounter, returnsCounter, tillFirstReturnsCounter;
    public CSP_SolverBacktracking(T cspProblem) {
        this.cspProblem = cspProblem;
    }
    private E chosenHeuristic;
    private ArrayList<S> accumulator;
    private Duration timeCounter;

    public ArrayList<S> getResults(E chosenHeuristic) {
        this.chosenHeuristic = chosenHeuristic;
        visitedNodesCounter = 0;
        tillFirstReturnsCounter = 0;
        returnsCounter = 0;
        tillFirstVisitedNodesCounter = 0;
        accumulator = new ArrayList<>();
        Instant start = Instant.now();
        var initialPartialSolution = cspProblem.getInitialPartialSolution();
        var firstVariableInx = initialPartialSolution.getNextVariableIndex(chosenHeuristic, -1);
        getResultsRecursive((S) initialPartialSolution, firstVariableInx);
        Instant end = Instant.now();
        timeCounter = Duration.between(start, end);
        return accumulator;
    }

    private void getResultsRecursive(S cspPartialSolution,
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
        for (var domainItem : currentVariable.getVariableDomain()) {
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
                    getResultsRecursive(cspPartialSolution, null);
                }
                else {
                    getResultsRecursive(cspPartialSolution, nextVariableIndex);
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
        return "CSP_Solver: Backtracking" +
                "\nHEURISTIC:  " + chosenHeuristic +
//                "\n" + accumulator.get(0) +
//                "\nFound:      1/" + accumulator.size() +
                "\nALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter +
                "\nTILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter +
                "\nDURATION:   " + timeCounter.toMillis()*0.001 + " sec" + "\n";
    }
}