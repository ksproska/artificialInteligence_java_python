import consts.HeuristicEnum;
import org.junit.rules.Stopwatch;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;


public class CSP_SolverForwardChecking<P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D, E>, S extends CSP_PartialSolution<P, D, E>> implements CSP_Solver<P, D, E, T, S> {
    private final T cspProblem;
    private int visitedNodesCounter, tillFirstVisitedNodesCounter, returnsCounter, tillFirstReturnsCounter;
    public CSP_SolverForwardChecking(T cspProblem) {
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
        initialPartialSolution.updateAllVariables();
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
        if (currentVariableInx == null) {
            returnsCounter++;
            if(accumulator.isEmpty()) { tillFirstReturnsCounter++; }
            return;
        }
        var nextVariable = cspPartialSolution.getCspVariables().get(currentVariableInx);
        nextVariable = new CSP_Variable<D>(nextVariable);
        var searchedDomain = new ArrayList<>(nextVariable.getVariableDomain());
        for (int i = 0; i < searchedDomain.size(); i++) {
            visitedNodesCounter += 1;
            if(accumulator.isEmpty()) { tillFirstVisitedNodesCounter++; }
            var domainItem = searchedDomain.get(i);
            var solutionCopy = (S) cspPartialSolution.deepClone();

            var changedVariableInx = nextVariable.variableIndex;
            solutionCopy.setNewValueAtIndexOf(domainItem, changedVariableInx);

//            System.out.println("\nvi:" + changedVariableInx + " d: " + domainItem);
//            System.out.println(solutionCopy);
//            System.out.println("ALL  Nodes: " + visitedNodesCounter + "\tReturns: " + returnsCounter);
//            System.out.println("TILL Nodes: " + tillFirstVisitedNodesCounter + "\tReturns: " + tillFirstReturnsCounter);
//
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            boolean areValuesCorrect = solutionCopy.updateVariables(changedVariableInx);
            if(areValuesCorrect) {
                var nextVariableIndex = solutionCopy.getNextVariableIndex(chosenHeuristic, currentVariableInx);
                getResultsRecursive(solutionCopy, nextVariableIndex);
                solutionCopy.removeValueAtIndexOf(changedVariableInx);
//                cspPartialSolution.setVariableReleased(changedVariableInx);
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

    @Override
    public String toString() {
        return "Forward checking\t" +
                chosenHeuristic +
//                "\n" + accumulator.get(0) +
//                "\nFound:      1/" + accumulator.size() +
                "\t" + visitedNodesCounter + "\t" + returnsCounter +
                "\t" + tillFirstVisitedNodesCounter + "\t" + tillFirstReturnsCounter +
                "\t" + timeCounter.toMillis()*0.001;
    }
}
