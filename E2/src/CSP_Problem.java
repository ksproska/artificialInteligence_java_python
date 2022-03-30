import java.util.ArrayList;

public interface CSP_Problem<P, D extends P> {
    CSP_PartialSolution<P, D> getInitialSolution();
}
