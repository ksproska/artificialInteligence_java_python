import java.util.ArrayList;


public abstract class Grid_Problem<P, E extends Enum, D extends P> implements CSP_Problem<P, D> {
    public final E chosenProblem;
    public final ArrayList<D> overallDomain;
    public final int x, y;
    public final ArrayList<P> problem = new ArrayList<>();
    public final ArrayList<Integer> indexesToFill = new ArrayList<>();
    protected String displaySplitter = " | ";

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

    public String toDisplay(ArrayList<P> grid) { throw new IllegalStateException("not implemented"); }

    @Override
    public String toString() { return chosenProblem + "\n" + toDisplay(problem); }

}