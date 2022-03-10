package zad1;

import java.util.HashMap;

public class FactorySetupVals {
    public static final String EASY = "easy", FLAT = "flat", HARD = "hard";
    public static final String cost = "cost", flow = "flow";
    public static final HashMap<String, int[]> sizes = new HashMap<>() {
        {
            put(EASY, new int[]{3, 3});
            put(FLAT, new int[]{1, 12});
            put(HARD, new int[]{5, 6});
        }
    };
    public static final HashMap<String, Integer> numbOfMachines = new HashMap<>() {
        {
            put(EASY, 9);
            put(FLAT, 12);
            put(HARD, 24);
        }
    };
}
