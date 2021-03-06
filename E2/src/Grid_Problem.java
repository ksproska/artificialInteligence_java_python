import consts.HeuristicEnum;

import java.util.ArrayList;


public abstract class Grid_Problem<P, E extends Enum, D extends P, H extends HeuristicEnum> implements CSP_Problem<P, D, H> {
    public final E chosenProblem;
    public final ArrayList<D> overallDomain;
    public final int x, y;
    public final ArrayList<P> problem = new ArrayList<>();
    public final ArrayList<Integer> indexesToFill = new ArrayList<>();
    protected String displaySplitter = " | ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN  = "\033[0;92m";
    public static final String ANSI_RED  = "\033[1;91m";
    public static final String ANSI_YELLOW = "\033[0;93m";
    public static final String ANSI_PURPLE = "\033[1;95m";

    protected Grid_Problem(E chosenProblem, int x, int y, ArrayList<D> overallDomain) {
        this.chosenProblem = chosenProblem;
        this.x = x;
        this.y = y;
        this.overallDomain = overallDomain;
    }

    protected void setIndexesToFill() {
        for (int i = 0; i < problem.size(); i++) {
            if(problem.get(i) == null) { indexesToFill.add(i); }
        }
    }

    @Override
    public String toString() { return chosenProblem + " - (" + this.indexesToFill.size() + "\\" + this.problem.size() + ")\n" + toDisplay(problem); }

    @Override
    public ArrayList<D> getDomain() { return overallDomain; }

    public String toDisplay(ArrayList<P> grid) { throw new IllegalStateException("not implemented"); }
    public String toDisplay(ArrayList<P> grid, int changedItemInx) { throw new IllegalStateException("not implemented"); }
}