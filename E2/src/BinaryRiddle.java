import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static java.util.Arrays.asList;

class BinarySuggestedSolution implements ProblemSuggestedSolution<Integer, Integer> {
    public BinaryRiddle binaryRiddle;
    public static final Integer[] domain = new Integer[]{0, 1};
    public ArrayList<Integer> partialSolution;
    public ArrayList<ArrayList<Integer>> rows, columns;
    private Integer lastChangedPosition;
    private boolean isCorrectAfterLastChange;
    private int itemX, itemY;

    @Override
    public ArrayList<Integer> getPartialSolution() {
        return partialSolution;
    }

    public int getX(int position) { return position % binaryRiddle.x; }

    public int getY(int position) { return position / binaryRiddle.x; }

    public BinarySuggestedSolution() {}

    public BinarySuggestedSolution(BinaryRiddle binaryRiddle) {
        this.binaryRiddle = binaryRiddle;
        this.partialSolution = new ArrayList<>(binaryRiddle.problem);

        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < binaryRiddle.x; i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < binaryRiddle.y; i++) {
            rows.add(new ArrayList<>());
        }
        for (int i = 0; i < this.partialSolution.size(); i++) {
            var tempX = getX(i);
            var tempY = getY(i);
            rows.get(tempY).add(tempX, partialSolution.get(i));
            columns.get(tempX).add(tempY, partialSolution.get(i));
        }

        isCorrectAfterLastChange = true;
    }

    public BinarySuggestedSolution copy() {
        var copiedItem = new BinarySuggestedSolution();
        copiedItem.binaryRiddle = binaryRiddle;
        copiedItem.partialSolution = new ArrayList<>(partialSolution);
        copiedItem.rows = new ArrayList<ArrayList<Integer>>();
        for (var row : rows) {
            copiedItem.rows.add(new ArrayList<Integer>(row));
        }
        copiedItem.columns = new ArrayList<ArrayList<Integer>>();
        for (var column : columns) {
            copiedItem.columns.add(new ArrayList<Integer>(column));
        }
        copiedItem.lastChangedPosition = lastChangedPosition;
        copiedItem.isCorrectAfterLastChange = isCorrectAfterLastChange;
        return copiedItem;
    }

    public void setValue(Integer domainItem, Integer variableItem) {
        if(!isCorrectAfterLastChange) {
            throw new IllegalStateException("Solution incorrect after last change");
        }
        if(partialSolution.get(variableItem) != null) {
            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
        }
        lastChangedPosition = variableItem;
        partialSolution.set(variableItem, domainItem);
        itemX = getX(variableItem);
        itemY = getY(variableItem);
        rows.get(itemY).set(itemX, domainItem);
        columns.get(itemX).set(itemY, domainItem);
        isCorrectAfterLastChange = wereNegativelyAffected();
    }

    private boolean areAllUnique() {
        if(!columns.get(itemX).contains(null)) {
            for (var column : columns) {
                if (!column.contains(null)) {
                    var freq = Collections.frequency(columns, column);
                    if (freq > 1) {
                        return false;
                    }
                }
            }
        }
        if(!rows.get(itemY).contains(null)) {
            for (var row : rows) {
                if (!row.contains(null)) {
                    var freq = Collections.frequency(rows, row);
                    if (freq > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isHalf0AndHalf1() {
        var freq0 = Collections.frequency(columns.get(itemX), 0);
        var freq1 = Collections.frequency(columns.get(itemX), 1);
        if (freq0 > binaryRiddle.y/2 || freq1 > binaryRiddle.y/2) {
            return false;
        }

        freq0 = Collections.frequency(rows.get(itemY), 0);
        freq1 = Collections.frequency(rows.get(itemY), 1);
        if (freq0 > binaryRiddle.x/2 || freq1 > binaryRiddle.x/2) {
            return false;
        }
        return true;
    }

    private boolean areValuesRepeatedMax2TimesInARow() {
        Integer previous = null;
        int counter = 0;
        for (var elem : columns.get(itemX)) {
            if(elem != null) {
                if(Objects.equals(previous, elem)) {
                    counter ++;
                    if(counter > 2) {
                        return false;
                    }
                }
                else {
                    counter = 1;
                }
            }
            previous = elem;
        }

        previous = null;
        counter = 0;
        for (var elem : rows.get(itemY)) {
            if(elem != null) {
                if(Objects.equals(previous, elem)) {
                    counter ++;
                    if(counter > 2) {
                        return false;
                    }
                }
                else {
                    counter = 1;
                }
            }
            previous = elem;
        }
        return true;
    }

    public boolean wereNegativelyAffected() {
        return areAllUnique() && isHalf0AndHalf1() && areValuesRepeatedMax2TimesInARow();
    }

    @Override
    public Integer[] getDomain() {
        return domain;
    }

    public boolean isCurrentCorrect() {
        return isCorrectAfterLastChange;
    }

    @Override
    public boolean isCompleted() {
        return !partialSolution.contains(null);
    }

    @Override
    public String toString() {
        return binaryRiddle.chosenProblem + "\n" + binaryRiddle.toDisplay(partialSolution) + isCorrectAfterLastChange;
    }
}

public class BinaryRiddle implements ProblemToSolve<Integer, Integer> {
    public final BinaryEnum chosenProblem;
    public final int x, y;
    public final ArrayList<Integer> problem;
    public final ArrayList<Integer> indexesToFill;

    public BinaryRiddle(BinaryEnum chosenProblem) {
        this.chosenProblem = chosenProblem;
        this.x = BinaryConsts.getSize(chosenProblem)[0];
        this.y = BinaryConsts.getSize(chosenProblem)[1];
        this.problem = new ArrayList<>();
        readProblem(ProjectConsts.folderPath);

        indexesToFill = new ArrayList<>();
        for (int i = 0; i < problem.size(); i++) {
            if(problem.get(i) == null) {
                indexesToFill.add(i);
            }
        }
    }

    @Override
    public void readProblem(String folderPath) {
        String filename = String.format("%s\\%s_%sx%s",
                folderPath,
                "binary",
                x,
                y
        );

        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                var nextLine = scanner.nextLine().split("");
                for (int i = 0; i < x; i++) {
                    Integer selectedVal = switch (nextLine[i]) {
                        case "x" -> null;
                        case "1" -> 1;
                        case "0" -> 0;
                        default -> throw new IllegalArgumentException("Incorrect input file");
                    };
                    problem.add(selectedVal);
                }
            }
            scanner.close();
        } catch (IOException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
    }

    @Override
    public ArrayList<Integer> getVariables() {
        return indexesToFill;
    }

    @Override
    public ProblemSuggestedSolution<Integer, Integer> getInitialSolution() {
        return new BinarySuggestedSolution(this);
    }

    public String toDisplay(ArrayList<Integer> grid) {
        var allToDisplay = " | ";
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i) == null) {
                allToDisplay += " ";
            }
            else {
                allToDisplay += grid.get(i);
            }
            allToDisplay += " | ";
            if((i + 1) % x == 0) {
                allToDisplay += "\n | ";
            }
        }
        allToDisplay = allToDisplay.substring(0, allToDisplay.length() - 3);
        return allToDisplay;
    }

    @Override
    public String toString() {
        return chosenProblem + "\n" + toDisplay(problem);
    }
}

class CSP <D, V> {
    private ProblemToSolve<D, V> problem;
    public CSP(ProblemToSolve<D, V> problem) {
        this.problem = problem;
    }

    public ArrayList<ProblemSuggestedSolution<D, V>> getResults() {
        var accumulator = new ArrayList<ProblemSuggestedSolution<D, V>>();
        var solution = problem.getInitialSolution();
        getResultsRecursive(solution, 0, accumulator);
        return accumulator;
    }

    public void getResultsRecursive(ProblemSuggestedSolution suggestedSolution, int currentVariable, ArrayList<ProblemSuggestedSolution<D, V>> accumulator) {
        if(suggestedSolution.isCompleted()) {
            if(suggestedSolution.isCurrentCorrect()) {
                accumulator.add(suggestedSolution);
            }
            return;
        }
        if(suggestedSolution.isCurrentCorrect()) {
            for (var domainItem : suggestedSolution.getDomain()) {
                var solutionCopy = suggestedSolution.copy();
                solutionCopy.setValue(domainItem, problem.getVariables().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }
}


class BinaryRiddleTest {
    static BinaryRiddle binaryRiddle;
    static CSP<Integer, Integer> csp;

    @Test
    void testCorrections() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B6x6);
        var suggestion = new BinarySuggestedSolution(binaryRiddle);
        suggestion.setValue(0, 0);
        suggestion.setValue(1, 6);
        suggestion.setValue(0, 18);
        suggestion.setValue(1, 24);

        suggestion.setValue(0, 5);
        suggestion.setValue(0, 17);
        suggestion.setValue(0, 23);
        suggestion.setValue(1, 29);
        suggestion.setValue(1, 35);
        Assertions.assertFalse(suggestion.isCurrentCorrect());
    }

    @Test
    void test6x6() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B6x6);
        csp = new CSP<>(binaryRiddle);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                0, 1, 0, 1, 1, 0,
                1, 0, 0, 1, 0, 1,
                0, 1, 1, 0, 0, 1,
                0, 1, 1, 0, 1, 0,
                1, 0, 0, 1, 1, 0,
                1, 0, 1, 0, 0, 1

        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }

    @Test
    void test8x8() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B8x8);
        csp = new CSP<>(binaryRiddle);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                1, 1, 0, 0, 1, 0, 1, 0,
                1, 0, 1, 1, 0, 0, 1, 0,
                0, 1, 0, 0, 1, 1, 0, 1,
                0, 0, 1, 1, 0, 1, 1, 0,
                1, 0, 1, 0, 1, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1,
                0, 1, 1, 0, 1, 0, 1, 0,
                1, 0, 0, 1, 0, 1, 0, 1

        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }

    @Test
    void test10x10() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B10x10);
        csp = new CSP<>(binaryRiddle);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                0, 1, 1, 0, 0, 1, 0, 1, 0, 1,
                1, 0, 0, 1, 1, 0, 0, 1, 1, 0,
                1, 0, 0, 1, 1, 0, 1, 0, 1, 0,
                0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 1, 0, 1, 0, 0, 1, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 1, 0,
                1, 0, 0, 1, 1, 0, 1, 0, 0, 1,
                0, 1, 1, 0, 1, 1, 0, 1, 0, 0,
                1, 0, 1, 0, 0, 1, 1, 0, 1, 0,
                0, 1, 0, 1, 0, 0, 1, 0, 1, 1
        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }
}