import java.util.ArrayList;
import java.util.Iterator;


public abstract class Grid_PartialSolution<P, E extends Enum, D extends P> implements CSP_PartialSolution<P, D> {
    public Grid_Problem<P, E, D> gridProblem;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected int changedItemX, changedItemY;
    public ArrayList<CSP_Variable<D>> cspVariables;

    protected Grid_PartialSolution() {}

    public <G extends Grid_Problem<P, E, D>> Grid_PartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        setupVariables();
    }

    private void setupVariables() {
        cspVariables = new ArrayList<>();
        for (var variableIndex : gridProblem.indexesToFill) {
            var newVariable = new CSP_Variable<D>(variableIndex);
            cspVariables.add(newVariable);
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

    protected void setRowsAndColumns(int x, int y) {
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < x; i++) { columns.add(new ArrayList<>()); }
        for (int i = 0; i < y; i++) { rows.add(new ArrayList<>()); }
        for (int i = 0; i < this.partialSolution.size(); i++) {
            var tempX = getX(i);
            var tempY = getY(i);
            rows.get(tempY).add(tempX, partialSolution.get(i));
            columns.get(tempX).add(tempY, partialSolution.get(i));
        }
    }

    public <Q> ArrayList<ArrayList<Q>> copyListOfLists(ArrayList<ArrayList<Q>> listOfLists) {
        var copied = new ArrayList<ArrayList<Q>>();
        for (var list : listOfLists) { copied.add(new ArrayList<>(list)); }
        return copied;
    }

    public void copyTo(Grid_PartialSolution<P, E, D> copiedItem) {
        copiedItem.gridProblem = gridProblem;
        copiedItem.partialSolution = new ArrayList<>(partialSolution);
        copiedItem.rows = copyListOfLists(rows);
        copiedItem.columns = copyListOfLists(columns);
        copiedItem.cspVariables = new ArrayList<>() {{
            for (var variable : cspVariables) {
                add(new CSP_Variable<>(variable));
            }
        }};
    }

    @Override
    public boolean updateVariables(Integer variableIndex) {
        var variableX = getX(variableIndex);
        var variableY = getY(variableIndex);
        for (var cspNextVariable : cspVariables) {
            var nextX = getX(cspNextVariable.variableIndex);
            var nextY = getY(cspNextVariable.variableIndex);
            if((nextX == variableX || nextY == variableY)
                    && !cspNextVariable.variableIndex.equals(variableIndex)) {
                for (Iterator<D> domainIterator = cspNextVariable.getDomain().iterator(); domainIterator.hasNext(); ) {
                    D domainItem = domainIterator.next();
                    var isCorrect = setNewValue(domainItem, cspNextVariable.variableIndex);
                    if(!isCorrect) {
                        domainIterator.remove();
                    }
                    removeValue(cspNextVariable.variableIndex);
                }
                if(!cspNextVariable.wasVariableUsed && cspNextVariable.getDomain().isEmpty()) {
                    return false;
                }
            }
            else if (cspNextVariable.variableIndex.equals(variableIndex)) {
                cspNextVariable.removeAll();
                cspNextVariable.wasVariableUsed = true;
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

    @Override
    public CSP_Variable<D> getNextVariable() {
        CSP_Variable<D> chosen = null;
        for (var variab : cspVariables) {
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
    public boolean areConstraintsNotBrokenAfterLastChange() { return this.checkConstraintsAfterLastChange(); }

    @Override
    public ArrayList<P> getPartialSolution() { return partialSolution; }

    @Override
    public String toString() {
        return gridProblem.chosenProblem + "\n" + gridProblem.toDisplay(partialSolution) + areConstraintsNotBrokenAfterLastChange();
    }

    @Override
    public boolean checkConstraintsAfterLastChange() { throw new IllegalStateException("Method not implemented"); }
}