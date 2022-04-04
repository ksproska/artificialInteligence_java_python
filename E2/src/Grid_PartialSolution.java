import consts.HeuristicEnum;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class Grid_PartialSolution<P, E extends Enum, D extends P, H extends HeuristicEnum> implements CSP_PartialSolution<P, D, H> {
    public Grid_Problem<P, E, D, H> gridProblem;
    public ArrayList<P> partialSolution;
    public ArrayList<ArrayList<P>> rows, columns;
    protected int changedItemX, changedItemY;
    public ArrayList<CSP_Variable<D>> cspVariables;

    protected Grid_PartialSolution() {}

    public <G extends Grid_Problem<P, E, D, H>> Grid_PartialSolution(G gridProblem) {
        this.gridProblem = gridProblem;
        this.partialSolution = new ArrayList<>(gridProblem.problem);
        setRowsAndColumns(gridProblem.x, gridProblem.y);
        setupVariables();
    }

    @Override
    public ArrayList<CSP_Variable<D>> getCspVariables() {
        return cspVariables;
    }

    private void setupVariables() {
        cspVariables = new ArrayList<>();
        for (var variableIndex : gridProblem.indexesToFill) {
            var newVariable = new CSP_Variable<D>(variableIndex);
            cspVariables.add(newVariable);
            for (var domainItem : gridProblem.overallDomain) {
                var isCorrectAfterLastChange = setNewValueAtIndexOf(domainItem, variableIndex);
                if(isCorrectAfterLastChange) {
                    newVariable.add(domainItem);
                }
                removeValueAtIndexOf(variableIndex);
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

    public void copyTo(Grid_PartialSolution<P, E, D, H> copiedItem) {
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
                for (Iterator<D> domainIterator = cspNextVariable.getVariableDomain().iterator(); domainIterator.hasNext(); ) {
                    D domainItem = domainIterator.next();
                    var isCorrect = setNewValueAtIndexOf(domainItem, cspNextVariable.variableIndex);
                    if(!isCorrect) {
                        domainIterator.remove();
                    }
                    removeValueAtIndexOf(cspNextVariable.variableIndex);
                }
                if(!cspNextVariable.wasVariableUsed && cspNextVariable.getVariableDomain().isEmpty()) {
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
    public void removeValueAtIndexOf(Integer variableItem) {
        partialSolution.set(variableItem, null);
        changedItemX = getX(variableItem);
        changedItemY = getY(variableItem);
        rows.get(changedItemY).set(changedItemX, null);
        columns.get(changedItemX).set(changedItemY, null);
    }

    @Override
    public Integer getNextVariableIndex(H chosenHeuristic, Integer variableIndex) {
        throw new IllegalStateException("Method not implemented");
    }

    @Override
    public boolean setNewValueAtIndexOf(D domainItem, Integer variableItem) {
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
        return gridProblem.chosenProblem + "\n" + gridProblem.toDisplay(partialSolution, changedItemY * gridProblem.x + changedItemX) + areConstraintsNotBrokenAfterLastChange();
    }

    @Override
    public boolean checkConstraintsAfterLastChange() { throw new IllegalStateException("Method not implemented"); }
}