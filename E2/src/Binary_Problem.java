import consts.BinaryConsts;
import consts.BinaryEnum;
import consts.ProjectConsts;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Binary_Problem extends Grid_Problem<Integer, BinaryEnum, Integer> {
    public Binary_Problem(BinaryEnum chosenProblem) {
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

    @Override
    public Binary_PartialSolution getInitialSolution() {
        return new Binary_PartialSolution(this);
    }
}
