import java.util.HashMap;

public class FutoshikiConsts {
    private static final HashMap<FutoshikiEnum, int[]> sizes = new HashMap<>() {
        {
            put(FutoshikiEnum.F4x4, new int[] {4, 4});
            put(FutoshikiEnum.F5x5, new int[] {5, 5});
            put(FutoshikiEnum.F6x6, new int[] {6, 6});
        }
    };

    public static int[] getSize(FutoshikiEnum futoshikiEnum) {
        return sizes.get(futoshikiEnum);
    }
}
