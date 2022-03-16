package zad1;

import java.util.HashMap;

public class FactorySetupVals{
    private static final HashMap<DataEnum, String> dataTypeStringHashMap = new HashMap<>() {
        {
            put(DataEnum.COST, "cost");
            put(DataEnum.FLOW, "flow");
        }
    };
    private static final HashMap<InstanceEnum, String> instanceTypeStringHashMap = new HashMap<>() {
        {
            put(InstanceEnum.EASY, "easy");
            put(InstanceEnum.FLAT, "flat");
            put(InstanceEnum.HARD, "hard");
            put(InstanceEnum.TEST, "test");
        }
    };
    private static final HashMap<InstanceEnum, int[]> sizes = new HashMap<>() {
        {
            put(InstanceEnum.EASY, new int[]{3, 3});
            put(InstanceEnum.FLAT, new int[]{1, 12});
            put(InstanceEnum.HARD, new int[]{5, 6});
            put(InstanceEnum.TEST, new int[]{1, 8});
        }
    };
    private static final HashMap<InstanceEnum, Integer> numbOfMachines = new HashMap<>() {
        {
            put(InstanceEnum.EASY, 9);
            put(InstanceEnum.FLAT, 12);
            put(InstanceEnum.HARD, 24);
            put(InstanceEnum.TEST, 6);
        }
    };

    public static String getDataEnumString(DataEnum dataEnum) {
        return dataTypeStringHashMap.get(dataEnum);
    }
    public static String getInstanceEnumString(InstanceEnum instanceEnum) {
        return instanceTypeStringHashMap.get(instanceEnum);
    }

    public static int[] getSize(InstanceEnum instanceEnum) {
        return sizes.get(instanceEnum);
    }
    public static Integer getNumberOfMachines(InstanceEnum instanceEnum) {
        return numbOfMachines.get(instanceEnum);
    }
}
