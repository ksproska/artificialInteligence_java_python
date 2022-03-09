package old;

public class FlattenArrays {
    public static final int COMMON_PART = 2;
    public static Long[][] flattenExerciseArrays(Long[][] flows, Long[][] costs) {
        int elemsInFlows = flows[0].length;
        int elemsInCosts = costs[0].length;
        int newLength = elemsInFlows + elemsInCosts - COMMON_PART;
        if (flows.length != costs.length) {
            throw new IllegalArgumentException("Tables are of different sizes");
        }

        Long[][] flattened = new Long[flows.length][newLength];
        for (int i = 0; i < flows.length; i++) {
            System.arraycopy(flows[i], 0, flattened[i], 0, elemsInFlows);
            for (int j = 0; j < elemsInCosts - COMMON_PART; j++) {
                flattened[i][elemsInFlows + j] = costs[i][COMMON_PART + j];
            }
        }
        return flattened;
    }
}
