package zad1;

import java.util.HashMap;

public class FactorySetupVals {

    public static final HashMap<DataEnum, String> dataTypeStringHashMap = new HashMap<>() {
        {
            put(DataEnum.COST, "cost");
            put(DataEnum.FLOW, "flow");
        }
    };
    public static final HashMap<InstanceEnum, String> instanceTypeStringHashMap = new HashMap<>() {
        {
            put(InstanceEnum.EASY, "easy");
            put(InstanceEnum.FLAT, "flat");
            put(InstanceEnum.HARD, "hard");
        }
    };

    public static final HashMap<InstanceEnum, int[]> sizes = new HashMap<>() {
        {
            put(InstanceEnum.EASY, new int[]{3, 3});
            put(InstanceEnum.FLAT, new int[]{1, 12});
            put(InstanceEnum.HARD, new int[]{5, 6});
        }
    };
    public static final HashMap<InstanceEnum, Integer> numbOfMachines = new HashMap<>() {
        {
            put(InstanceEnum.EASY, 9);
            put(InstanceEnum.FLAT, 12);
            put(InstanceEnum.HARD, 24);
        }
    };
}
