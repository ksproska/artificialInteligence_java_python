import java.util.ArrayList;

public class CSP_Variable<T> {
    public final Integer variableIndex;
    private ArrayList<T> domain;

    public CSP_Variable(Integer variableIndex) {
        this.variableIndex = variableIndex;
        this.domain = new ArrayList<>();
    }

    public void add(T variable) {
        domain.add(variable);
    }

    public void remove(T variable) {
        domain.remove(variable);
    }

    public void removeAll() {
        domain = new ArrayList<>();
    }

    public ArrayList<T> getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return variableIndex + ":" + domain;
    }
}
