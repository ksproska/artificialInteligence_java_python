import java.util.ArrayList;

public interface CSP_Problem<P, D extends P> {
    <T extends CSP_PartialSolution<P, D>> T getInitialSolution();
}
