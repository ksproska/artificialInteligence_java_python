import java.util.ArrayList;
import java.util.Iterator;


public abstract class Grid_PartialSolution<P, E extends Enum, D extends P> implements CSP_PartialSolution<P, D> {
    public Grid_Problem<P, E, D> gridProblem;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected Integer lastChangedPosition;
    protected boolean isCorrectAfterLastChange;
    protected int itemX, itemY;
    public ArrayList<CSP_Variable<D>> variables;

    public <G extends Grid_Problem> Grid_PartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<P>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        isCorrectAfterLastChange = true;
        variables = new ArrayList<>();
        for (var variableIndex : gridProblem.indexesToFill) {
            var newVariable = new CSP_Variable<D>((Integer) variableIndex);
            variables.add(newVariable);
            for (var domainItem : gridProblem.overallDomain) {
                setNewValue((D) domainItem, (Integer) variableIndex);
                if(isCorrectAfterLastChange) {
                    newVariable.add((D) domainItem);
                }
                removeNewValue((Integer) variableIndex);
            }
        }
    }

    public int getX(int position) { return position % gridProblem.x; }
    public int getY(int position) { return position / gridProblem.x; }

    protected Grid_PartialSolution() {}

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
    public boolean updateVariables(Integer variableItem) {
        var variableX = getX(variableItem);
        var variableY = getY(variableItem);
        for (var variableInArray : variables) {
            var tempX = getX(variableInArray.variableIndex);
            var tempY = getY(variableInArray.variableIndex);
            if((tempX == variableX || tempY == variableY) && !variableInArray.variableIndex.equals(variableItem)) {
                for (Iterator<D> iterator = variableInArray.getDomain().iterator(); iterator.hasNext(); ) {
                    D domainItem = iterator.next();
                    setNewValue(domainItem, variableInArray.variableIndex);
                    if(!isCorrectAfterLastChange) {
                        iterator.remove();
                    }
                    removeNewValue(variableInArray.variableIndex);
                }
                if(!variableInArray.wasVariableUsed && variableInArray.getDomain().isEmpty()) {
                    return false;
                }
            }
            else if (variableInArray.variableIndex.equals(variableItem)) {
                variableInArray.removeAll();
                variableInArray.wasVariableUsed = true;
            }
        }
        return true;
    }

    @Override
    public void removeNewValue(Integer variableItem) {
        lastChangedPosition = variableItem;
        partialSolution.set(variableItem, null);
        itemX = getX(variableItem);
        itemY = getY(variableItem);
        rows.get(itemY).set(itemX, null);
        columns.get(itemX).set(itemY, null);
        isCorrectAfterLastChange = true;
    }

    public CSP_Variable<D> getNextFreeVariable() {
        for (var variab : variables) {
            if (!variab.getDomain().isEmpty()) {
                return variab;
            }
        }
        return null;
    }

    @Override
    public void setNewValue(D domainItem, Integer variableItem) {
//        if(!isCorrectAfterLastChange) {
//            throw new IllegalStateException("Solution incorrect after last change");
//        }
        if(partialSolution.get(variableItem) != null) {
            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
        }
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