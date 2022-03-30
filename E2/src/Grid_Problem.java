import java.util.ArrayList;

public abstract class Grid_Problem<P, E extends Enum, D extends P> implements CSP_Problem<P, D> {
    public final E chosenProblem;
    public final int x, y;
    public final ArrayList<P> problem = new ArrayList<>();
    public final ArrayList<Integer> indexesToFill = new ArrayList<>();
    protected String displaySplitter = " | ";

    protected Grid_Problem(E chosenProblem, int x, int y) {
        this.chosenProblem = chosenProblem;
        this.x = x;
        this.y = y;
    }

    @Override
    public Grid_PartialSolution<P, E, D> getInitialSolution() {
        return null;
    }

    public String toDisplay(ArrayList<P> grid) {
        var allToDisplay = displaySplitter;
        for (int i = 0; i < grid.size(); i++) {
            if (grid.get(i) == null) {
                allToDisplay += " ";
            }
            else {
                allToDisplay += grid.get(i);
            }
            allToDisplay += displaySplitter;
            if((i + 1) % x == 0) {
                allToDisplay += "\n" + displaySplitter;
            }
        }
        allToDisplay = allToDisplay.substring(0, allToDisplay.length() - 3);
        return allToDisplay;
    }

    @Override
    public String toString() { return chosenProblem + "\n" + toDisplay(problem); }

    @Override
    public ArrayList<Integer> getVariablesIndexes() { return indexesToFill; }
}