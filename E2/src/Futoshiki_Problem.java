import consts.FutoshikiConsts;
import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;
import consts.ProjectConsts;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Futoshiki_Problem extends Grid_Problem<Object, FutoshikiEnum, Integer, FutoshikiHeuristicEnum> {
    public final int gridX, gridY;
    public static final String neutral = "-", lessThan = "<", moreThan = ">";

    public Futoshiki_Problem(FutoshikiEnum chosenProblem) {
        super(chosenProblem,
                FutoshikiConsts.getSize(chosenProblem)[0] * 2 - 1,
                FutoshikiConsts.getSize(chosenProblem)[1] * 2 - 1,
                new ArrayList<>(){{
                    for (int i = 0; i < FutoshikiConsts.getSize(chosenProblem)[0]; i++) {
                        add(i + 1);
                    }
                }}
        );
        gridX = FutoshikiConsts.getSize(chosenProblem)[0];
        gridY = FutoshikiConsts.getSize(chosenProblem)[1];
        readProblem(ProjectConsts.folderPath);
        setIndexesToFill();
    }

    private void readProblem(String folderPath) {
        String filename = String.format("%s\\%s_%sx%s", folderPath, "futoshiki", gridX, gridY);
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
                    if(nextValue.equals("x")) {
                        nextValue = (Integer) null;
                    }
                    problem.add(nextValue);
                    if(rowCounter % 2 == 1 && i < gridX - 1) {
                        problem.add("-");
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
        return toDisplay(grid, -1);
    }

    @Override
    public String toDisplay(ArrayList<Object> grid, int changedItemInx) {
        var allToDisplay = "";
        for (int i = 0; i < grid.size(); i++) {
            if(Futoshiki_Problem.lessThan.equals(grid.get(i)) || Futoshiki_Problem.moreThan.equals(grid.get(i))) {
                allToDisplay += ANSI_PURPLE;
            }
            else if(i == changedItemInx) {
                allToDisplay += ANSI_RED;
            }
            else if(!indexesToFill.contains(i)) {
                allToDisplay += ANSI_GREEN;
            }

            if (grid.get(i) == null ) { allToDisplay += ANSI_YELLOW + "x" + ANSI_RESET; }
            else if (grid.get(i).equals("-") ) { allToDisplay += " "; }
            else { allToDisplay += grid.get(i); }
            if(!indexesToFill.contains(i) || i == changedItemInx) {
                allToDisplay += ANSI_RESET;
            }
            allToDisplay += "   ";
            if((i + 1) % x == 0) { allToDisplay += "\n"; }
        }
        allToDisplay = allToDisplay.substring(0, allToDisplay.length() - 3);
        return allToDisplay;
    }

    @Override
    public Futoshiki_PartialSolution getInitialPartialSolution() {
        return new Futoshiki_PartialSolution(this);
    }
}
