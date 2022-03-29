import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class BinaryProblem implements CspProblem<Integer, Integer> {
    public final BinaryEnum chosenProblem;
    public final int x, y;
    public final ArrayList<Integer> problem;
    public final ArrayList<Integer> indexesToFill;

    public BinaryProblem(BinaryEnum chosenProblem) {
        this.chosenProblem = chosenProblem;
        this.x = BinaryConsts.getSize(chosenProblem)[0];
        this.y = BinaryConsts.getSize(chosenProblem)[1];
        this.problem = new ArrayList<>();
        indexesToFill = new ArrayList<>();
        readProblem(ProjectConsts.folderPath);
        setIndexesToFill();
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
    public String toString() { return chosenProblem + "\n" + toDisplay(problem); }

    @Override
    public ArrayList<Integer> getVariables() { return indexesToFill; }

    @Override
    public CspPartialSolution<Integer, Integer> getInitialSolution() {
        return new BinaryPartialSolution(this);
    }
}
