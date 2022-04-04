import consts.HeuristicEnum;

import java.util.ArrayList;

public interface CSP_Problem<P, D extends P, H extends HeuristicEnum> {
    <T extends CSP_PartialSolution<P, D, H>> T getInitialPartialSolution();
    ArrayList<D> getDomain();
}
