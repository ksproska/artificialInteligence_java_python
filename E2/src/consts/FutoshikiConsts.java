package consts;

import java.util.HashMap;

public class FutoshikiConsts {
    private static final HashMap<FutoshikiEnum, int[]> sizes = new HashMap<>() {
        {
            put(FutoshikiEnum.F2x2, new int[] {2, 2});
            put(FutoshikiEnum.F3x3, new int[] {3, 3});
            put(FutoshikiEnum.F4x4, new int[] {4, 4});
            put(FutoshikiEnum.F5x5, new int[] {5, 5});
            put(FutoshikiEnum.F6x6, new int[] {6, 6});
            put(FutoshikiEnum.F7x7, new int[] {7, 7});
        }
    };

    public static int[] getSize(FutoshikiEnum futoshikiEnum) {
        return sizes.get(futoshikiEnum);
    }
}
