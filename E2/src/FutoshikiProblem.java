import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FutoshikiProblem {
    public final FutoshikiEnum chosenProblem;
    public final int x, y;
    public final ArrayList<Object> problem;
//    public final ArrayList<Integer> indexesToFill;

    public FutoshikiProblem(FutoshikiEnum chosenProblem) {
        this.chosenProblem = chosenProblem;
        this.x = FutoshikiConsts.getSize(chosenProblem)[0];
        this.y = FutoshikiConsts.getSize(chosenProblem)[1];
        this.problem = new ArrayList<>();
//        indexesToFill = new ArrayList<>();
        readProblem(ProjectConsts.folderPath);
//        setIndexesToFill();
    }

    private void readProblem(String folderPath) {
        String filename = String.format("%s\\%s_%sx%s", folderPath, "futoshiki", x, y);
        try {
            Scanner scanner = new Scanner(new File(filename));
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
                    problem.add(i, nextValue);
                }
            }
            scanner.close();
        } catch (IOException e) {
            String errorMessage = String.format("Problem reading file \"%s\"", filename);
            System.out.println(errorMessage);
        }
    }
}

class FutoshikiTests {
    static FutoshikiProblem futoshikiProblem;

    @Test
    void test1() {
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F4x4);
        System.out.println(futoshikiProblem.problem);
    }
}