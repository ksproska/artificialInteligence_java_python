package zad1;

import java.util.HashMap;

public class FactorySetupVals {
    enum DataType {
        COST, FLOW
    }
    public enum InstanceType {
        EASY, FLAT, HARD
    }

//    public static final String EASY = "easy", FLAT = "flat", HARD = "hard";
//    public static final String cost = "cost", flow = "flow";

    public static final HashMap<DataType, String> dataTypeStringHashMap = new HashMap<>() {
        {
            put(DataType.COST, "cost");
            put(DataType.FLOW, "flow");
        }
    };
    public static final HashMap<InstanceType, String> instanceTypeStringHashMap = new HashMap<>() {
        {
            put(InstanceType.EASY, "easy");
            put(InstanceType.FLAT, "flat");
            put(InstanceType.HARD, "hard");
        }
    };

    public static final HashMap<InstanceType, int[]> sizes = new HashMap<>() {
        {
            put(InstanceType.EASY, new int[]{3, 3});
            put(InstanceType.FLAT, new int[]{1, 12});
            put(InstanceType.HARD, new int[]{5, 6});
        }
    };
    public static final HashMap<InstanceType, Integer> numbOfMachines = new HashMap<>() {
        {
            put(InstanceType.EASY, 9);
            put(InstanceType.FLAT, 12);
            put(InstanceType.HARD, 24);
        }
    };
}
