import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FutoshikiProblem {
    public final FutoshikiEnum chosenProblem;
    public final int x, y;
    public final int gridX, gridY;
    public final ArrayList<Object> problem;
//    public final ArrayList<Integer> indexesToFill;

    public FutoshikiProblem(FutoshikiEnum chosenProblem) {
        this.chosenProblem = chosenProblem;
        this.x = FutoshikiConsts.getSize(chosenProblem)[0];
        this.y = FutoshikiConsts.getSize(chosenProblem)[1];
        gridX = this.x * 2 - 1;
        gridY = this.y * 2 - 1;
        this.problem = new ArrayList<>();
//        indexesToFill = new ArrayList<>();
        readProblem(ProjectConsts.folderPath);
//        setIndexesToFill();
    }

    private void readProblem(String folderPath) {
        String filename = String.format("%s\\%s_%sx%s", folderPath, "futoshiki", x, y);
        try {
            Scanner scanner = new Scanner(new File(filename));
            int rowCounter = 0;
            while (scanner.hasNextLine()) {
                var nextLine = scanner.nextLine().split("");
                for (int i = 0; i < nextLine.length; i++) {
                    var nextChar = nextLine[i];
                    Object nextValue = nextChar;
                    try {
                        nextValue = Integer.valueOf(nextChar);
                    }
                    catch (NumberFormatException ignored) {}
                    if(nextValue == "x") {
                        nextValue = new Integer(null);
                    }
                    problem.add(nextValue);
                    if(rowCounter % 2 == 1 && i < x - 1) {
                        problem.add(null);
                    }
                }
                rowCounter += 1;
            }
            scanner.close();
        } catch (IOException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
    }

    public String toDisplay(ArrayList<Object> grid) {
        var allToDisplay = "";
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i) == null
                    || grid.get(i).equals("-")
            ) {
                allToDisplay += " ";
            }
            else {
                allToDisplay += grid.get(i);
            }
            allToDisplay += "   ";
            if((i + 1) % gridX == 0) {
                allToDisplay += "\n";
            }
        }
        allToDisplay = allToDisplay.substring(0, allToDisplay.length() - 3);
        return allToDisplay;
    }

    @Override
    public String toString() { return chosenProblem + "\n" + toDisplay(problem); }
}

class FutoshikiTests {
    static FutoshikiProblem futoshikiProblem;

    @Test
    void test1() {
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F4x4);
        System.out.println(futoshikiProblem);
    }
}