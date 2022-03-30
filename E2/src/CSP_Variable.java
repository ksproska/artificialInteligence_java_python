import java.util.ArrayList;

public class CSP_Variable<T> {
    public final Integer variableIndex;
    private ArrayList<T> domain;
    public boolean wasVariableUsed;

    public CSP_Variable(Integer variableIndex) {
        this.variableIndex = variableIndex;
        this.domain = new ArrayList<>();
        wasVariableUsed = false;
    }

    public CSP_Variable(CSP_Variable<T> other) {
        this.variableIndex = other.variableIndex;
        this.domain = new ArrayList<>(other.domain);
        wasVariableUsed = other.wasVariableUsed;
    }

    public void add(T variable) { domain.add(variable); }
    public void removeAll() { domain = new ArrayList<>(); }
    public ArrayList<T> getDomain() { return domain; }

    @Override
    public String toString() { return variableIndex + ":" + domain; }
}
