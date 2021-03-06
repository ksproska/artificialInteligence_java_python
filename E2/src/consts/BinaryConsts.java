package consts;

import java.util.HashMap;


public class BinaryConsts {
    private static final HashMap<BinaryEnum, int[]> sizes = new HashMap<>() {
        {
            put(BinaryEnum.B2x2, new int[] {2, 2});
            put(BinaryEnum.B4x4, new int[] {4, 4});
            put(BinaryEnum.B6x6, new int[] {6, 6});
            put(BinaryEnum.B8x8, new int[] {8, 8});
            put(BinaryEnum.B10x10, new int[] {10, 10});
        }
    };

    public static int[] getSize(BinaryEnum binaryEnum) {
        return sizes.get(binaryEnum);
    }
}
