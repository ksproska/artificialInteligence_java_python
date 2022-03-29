import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

class BinarySuggestedSolution implements ProblemSuggestedSolution<Integer, Integer> {
    public final ArrayList<Integer> problem;
    public final ArrayList<ArrayList<Integer>> rows, columns;
    public final BinaryRiddle binaryRiddle;
    private Integer lastChangedPosition;
    private boolean isCorrectAfterLastChange;
    private int itemX, itemY;

    public BinarySuggestedSolution(BinaryRiddle binaryRiddle) {
        this.binaryRiddle = binaryRiddle;
        this.problem = new ArrayList<>(binaryRiddle.problem);
        this.rows = new ArrayList<>(binaryRiddle.rows);
        this.columns = new ArrayList<>(binaryRiddle.columns);
        isCorrectAfterLastChange = true;
    }

    public BinarySuggestedSolution(BinarySuggestedSolution binarySuggestedSolution) {
        this.binaryRiddle = binarySuggestedSolution.binaryRiddle;
        this.problem = new ArrayList<>(binarySuggestedSolution.problem);
        this.rows = new ArrayList<ArrayList<Integer>>();
        for (var row : binarySuggestedSolution.rows) {
            rows.add(new ArrayList<Integer>(row));
        }
        this.columns = new ArrayList<ArrayList<Integer>>();
        for (var column : binarySuggestedSolution.columns) {
            columns.add(new ArrayList<Integer>(column));
        }
        lastChangedPosition = binarySuggestedSolution.lastChangedPosition;
        isCorrectAfterLastChange = binarySuggestedSolution.isCorrectAfterLastChange;
    }

    public void setValue(Integer domainItem, Integer variableItem) {
        if(!isCorrectAfterLastChange) {
            throw new IllegalStateException("Solution incorrect after last change");
        }
        if(problem.get(variableItem) != null) {
            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
        }
        lastChangedPosition = variableItem;
        problem.set(variableItem, domainItem);
        itemX = binaryRiddle.getX(variableItem);
        itemY = binaryRiddle.getY(variableItem);
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

    public boolean isCurrentCorrect() {
        return isCorrectAfterLastChange;
    }

    @Override
    public boolean isCompleted() {
        return !problem.contains(null);
    }

    @Override
    public String toString() {
        return binaryRiddle.chosenProblem + "\n" + binaryRiddle.toDisplay(problem) + isCorrectAfterLastChange;
    }
}

public class BinaryRiddle implements ProblemToSolve<Integer, Integer> {
    public final BinaryEnum chosenProblem;
    public static final Integer[] domain = new Integer[]{0, 1};
    public final int x, y;
    public final ArrayList<Integer> problem;
    public final ArrayList<ArrayList<Integer>> rows, columns;
    public final ArrayList<Integer> indexesToFill;

    public BinaryRiddle(BinaryEnum chosenProblem) {
        this.chosenProblem = chosenProblem;
        this.x = BinaryConsts.getSize(chosenProblem)[0];
        this.y = BinaryConsts.getSize(chosenProblem)[1];
        this.problem = new ArrayList<>();
        readProblem(ProjectConsts.folderPath);

        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < y; i++) {
            rows.add(new ArrayList<>());
        }
        for (int i = 0; i < this.problem.size(); i++) {
            var itemX = getX(i);
            var itemY = getY(i);
            rows.get(itemY).add(itemX, problem.get(i));
            columns.get(itemX).add(itemY, problem.get(i));
        }

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
    public Integer[] getDomain() {
        return domain;
    }

    @Override
    public ArrayList<Integer> getVariables() {
        return indexesToFill;
    }

    @Override
    public ArrayList<BinarySuggestedSolution> getResults() {
        var accumulator = new ArrayList<BinarySuggestedSolution>();
        var solution = new BinarySuggestedSolution(this);
        getResultsRecursive(solution, 0, accumulator);
        return accumulator;
    }

    public void getResultsRecursive(BinarySuggestedSolution suggestedSolution, int currentVariable, ArrayList<BinarySuggestedSolution> accumulator) {
        if(suggestedSolution.isCompleted()) {
            if(suggestedSolution.isCurrentCorrect()) {
                accumulator.add(suggestedSolution);
            }
            return;
        }
        if(suggestedSolution.isCurrentCorrect()) {
            for (var domainItem : getDomain()) {
                var solutionCopy = new BinarySuggestedSolution(suggestedSolution);
                solutionCopy.setValue(domainItem, getVariables().get(currentVariable));
                getResultsRecursive(solutionCopy, currentVariable + 1, accumulator);
            }
        }
    }

    public int getX(int position) { return position % x; }
    public int getY(int position) { return position / x; }

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


class BinaryRiddleTest {
    static BinaryRiddle binaryRiddle;

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
        var results = binaryRiddle.getResults();
        Assertions.assertEquals(1, results.size());
        System.out.println(results.get(0).problem);
        var expected = new ArrayList<Integer>(asList(
                0, 1, 0, 1, 1, 0,
                1, 0, 0, 1, 0, 1,
                0, 1, 1, 0, 0, 1,
                0, 1, 1, 0, 1, 0,
                1, 0, 0, 1, 1, 0,
                1, 0, 1, 0, 0, 1

        ));
        Assertions.assertIterableEquals(expected, results.get(0).problem);
    }

    @Test
    void test8x8() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B8x8);
        var results = binaryRiddle.getResults();
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
        Assertions.assertIterableEquals(expected, results.get(0).problem);
    }

    @Test
    void test10x10() {
        binaryRiddle = new BinaryRiddle(BinaryEnum.B10x10);
        var results = binaryRiddle.getResults();
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
        Assertions.assertIterableEquals(expected, results.get(0).problem);
    }
}