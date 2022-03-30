import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class BinaryProblem extends GridProblem<Integer, BinaryEnum, Integer> {
    public class BinaryPartialSolution extends GridProblem<Integer, BinaryEnum, Integer>.GridPartialSolution<Integer, BinaryEnum, Integer> {
        public static final Integer[] domain = new Integer[]{0, 1};

        private BinaryPartialSolution() {}

        public BinaryPartialSolution(BinaryProblem binaryProblem) {
            super(binaryProblem);
        }

        private boolean constraint_areAllUnique() {
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

        private boolean constraint_isHalf0AndHalf1() {
            var freq0 = Collections.frequency(columns.get(itemX), 0);
            var freq1 = Collections.frequency(columns.get(itemX), 1);
            if (freq0 > gridProblem.y/2 || freq1 > gridProblem.y/2) {
                return false;
            }

            freq0 = Collections.frequency(rows.get(itemY), 0);
            freq1 = Collections.frequency(rows.get(itemY), 1);
            if (freq0 > gridProblem.x/2 || freq1 > gridProblem.x/2) {
                return false;
            }
            return true;
        }

        private boolean constraint_areValuesRepeatedMax2TimesInARow() {
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

        @Override
        public boolean checkConstraintsAfterLastChange() {
            return constraint_areAllUnique() && constraint_isHalf0AndHalf1() && constraint_areValuesRepeatedMax2TimesInARow();
        }

        public BinaryPartialSolution copyBinary() {
            var copiedItem =  new BinaryPartialSolution();
            copiedItem.gridProblem = gridProblem;
            copiedItem.partialSolution = new ArrayList<>(partialSolution);
            copiedItem.rows = new ArrayList<>();
            for (var row : rows) {
                copiedItem.rows.add(new ArrayList<>(row));
            }
            copiedItem.columns = new ArrayList<>();
            for (var column : columns) {
                copiedItem.columns.add(new ArrayList<>(column));
            }
            copiedItem.lastChangedPosition = lastChangedPosition;
            copiedItem.isCorrectAfterLastChange = isCorrectAfterLastChange;
            return copiedItem;
        }

        @Override
        public Integer[] getDomain() { return domain; }

        @Override
        public boolean isSatisfied() { return !partialSolution.contains(null); }
    }

    public BinaryProblem(BinaryEnum chosenProblem) {
        super(chosenProblem,
                BinaryConsts.getSize(chosenProblem)[0],
                BinaryConsts.getSize(chosenProblem)[1]
        );

        readProblem(ProjectConsts.folderPath);
        setIndexesToFill();
        displaySplitter = " | ";
    }

    private void readProblem(String folderPath) {
        String filename = String.format("%s\\%s_%sx%s", folderPath, "binary", x, y);
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

    private void setIndexesToFill() {
        for (int i = 0; i < problem.size(); i++) {
            if(problem.get(i) == null) {
                indexesToFill.add(i);
            }
        }
    }

    @Override
    public BinaryPartialSolution getInitialSolution() {
        return new BinaryPartialSolution(this);
    }
}
