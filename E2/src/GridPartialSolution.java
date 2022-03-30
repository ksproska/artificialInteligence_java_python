import java.util.ArrayList;

public abstract class GridPartialSolution<P, E extends Enum, D extends P> implements CspPartialSolution<P, D> {
    public GridProblem<P, E, D> gridProblem;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected Integer lastChangedPosition;
    protected boolean isCorrectAfterLastChange;
    protected int itemX, itemY;

    public <G extends GridProblem> GridPartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<P>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        isCorrectAfterLastChange = true;
    }

    public int getX(int position) { return position % gridProblem.x; }
    public int getY(int position) { return position / gridProblem.x; }

    protected GridPartialSolution() {}

    protected void setRowsAndColumns(int x, int y) {
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            columns.add(new ArrayList<>());
        }
        for (int i = 0; i < y; i++) {
            rows.add(new ArrayList<>());
        }
        for (int i = 0; i < this.partialSolution.size(); i++) {
            var tempX = getX(i);
            var tempY = getY(i);
            rows.get(tempY).add(tempX, partialSolution.get(i));
            columns.get(tempX).add(tempY, partialSolution.get(i));
        }
    }

    @Override
    public void setNewValue(D domainItem, Integer variableItem) {
        if(!isCorrectAfterLastChange) {
            throw new IllegalStateException("Solution incorrect after last change");
        }
//        if(partialSolution.get(variableItem) != null) {
//            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
//        }
        lastChangedPosition = variableItem;
        partialSolution.set(variableItem, domainItem);
        itemX = getX(variableItem);
        itemY = getY(variableItem);
        rows.get(itemY).set(itemX, domainItem);
        columns.get(itemX).set(itemY, domainItem);
        isCorrectAfterLastChange = this.checkConstraintsAfterLastChange();
    }

    @Override
    public boolean isSatisfied() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public boolean checkConstraintsAfterLastChange() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public D[] getDomain() {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public boolean areConstraintsNotBrokenAfterLastChange() { return isCorrectAfterLastChange; }

    @Override
    public ArrayList<P> getPartialSolution() { return partialSolution; }

    @Override
    public String toString() {
        return gridProblem.chosenProblem + "\n" + gridProblem.toDisplay(partialSolution) + isCorrectAfterLastChange;
    }
}