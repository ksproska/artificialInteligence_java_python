import java.util.ArrayList;


public class CSP_Variable<T> {
    public final Integer variableIndex;
    private ArrayList<T> variableDomain;
    public boolean wasVariableUsed;

    public CSP_Variable(Integer variableIndex) {
        this.variableIndex = variableIndex;
        this.variableDomain = new ArrayList<>();
        wasVariableUsed = false;
    }

    public CSP_Variable(CSP_Variable<T> other) {
        this.variableIndex = other.variableIndex;
        this.variableDomain = new ArrayList<>(other.variableDomain);
        wasVariableUsed = other.wasVariableUsed;
    }

    public void add(T variable) { variableDomain.add(variable); }
    public void removeAll() { variableDomain = new ArrayList<>(); }
    public ArrayList<T> getVariableDomain() { return variableDomain; }

    public void setVariableDomain(ArrayList<T> variableDomain) {
        this.variableDomain = variableDomain;
    }

    @Override
    public String toString() { return variableIndex + ":" + variableDomain; }
}
