import java.util.ArrayList;
import java.util.Iterator;


public abstract class Grid_PartialSolution<P, E extends Enum, D extends P> implements CSP_PartialSolution<P, D> {
    public Grid_Problem<P, E, D> gridProblem;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected int changedItemX, changedItemY;
    public ArrayList<CSP_Variable<D>> variables;

    public <G extends Grid_Problem<P, E, D>> Grid_PartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        setupVariables();
    }

    private void setupVariables() {
        variables = new ArrayList<>();
        for (var variableIndex : gridProblem.indexesToFill) {
            var newVariable = new CSP_Variable<D>(variableIndex);
            variables.add(newVariable);
            for (var domainItem : gridProblem.overallDomain) {
                var isCorrectAfterLastChange = setNewValue(domainItem, variableIndex);
                if(isCorrectAfterLastChange) {
                    newVariable.add(domainItem);
                }
                removeValue(variableIndex);
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
                    var isCorrectAfterLastChange = setNewValue(domainItem, variableInArray.variableIndex);
                    if(!isCorrectAfterLastChange) {
                        iterator.remove();
                    }
                    removeValue(variableInArray.variableIndex);
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
    public void removeValue(Integer variableItem) {
        partialSolution.set(variableItem, null);
        changedItemX = getX(variableItem);
        changedItemY = getY(variableItem);
        rows.get(changedItemY).set(changedItemX, null);
        columns.get(changedItemX).set(changedItemY, null);
    }

    public CSP_Variable<D> getNextVariable() {
        CSP_Variable<D> chosen = null;
        for (var variab : variables) {
            if (!variab.getDomain().isEmpty()) {
                if(chosen == null || variab.getDomain().size() < chosen.getDomain().size()) {
                    chosen = variab;
                }
            }
        }
        return chosen;
    }

    @Override
    public boolean setNewValue(D domainItem, Integer variableItem) {
        if(partialSolution.get(variableItem) != null) {
            throw new IllegalArgumentException("Value already set at variableItem: " + variableItem);
        }
        partialSolution.set(variableItem, domainItem);
        changedItemX = getX(variableItem);
        changedItemY = getY(variableItem);
        rows.get(changedItemY).set(changedItemX, domainItem);
        columns.get(changedItemX).set(changedItemY, domainItem);
        return this.checkConstraintsAfterLastChange();
    }

    @Override
    public boolean isSatisfied() { return !partialSolution.contains(null); }

    @Override
    public boolean checkConstraintsAfterLastChange() { throw new IllegalStateException("Method not implemented"); }

    @Override
    public boolean areConstraintsNotBrokenAfterLastChange() { return this.checkConstraintsAfterLastChange(); }

    @Override
    public ArrayList<P> getPartialSolution() { return partialSolution; }

    @Override
    public String toString() {
        return gridProblem.chosenProblem + "\n" + gridProblem.toDisplay(partialSolution) + areConstraintsNotBrokenAfterLastChange();
    }

    public <Q> ArrayList<ArrayList<Q>> copyListOfLists(ArrayList<ArrayList<Q>> listOfLists) {
        var copied = new ArrayList<ArrayList<Q>>();
        for (var list : listOfLists) {
            copied.add(new ArrayList<>(list));
        }
        return copied;
    }

    public void copyTo(Grid_PartialSolution<P, E, D> copiedItem) {
        copiedItem.gridProblem = gridProblem;
        copiedItem.partialSolution = new ArrayList<>(partialSolution);
        copiedItem.rows = copyListOfLists(rows);
        copiedItem.columns = copyListOfLists(columns);
        copiedItem.variables = new ArrayList<>() {{
            for (var variable : variables) {
                add(new CSP_Variable<>(variable));
            }
        }};
    }
}