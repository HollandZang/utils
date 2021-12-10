package MapUtils;

public class MapUtils {
    /**
     * 根据List长度计算Map初始化容量
     *
     * @param i List长度
     * @return Map初始化容量
     */
    public static int getInitialCapacity(int i) {
        if (i <= 1) return 2;
        if (i <= 3) return 4;
        int numberOfLeadingZeros = Integer.numberOfLeadingZeros(i);
        if (i >> 32 - numberOfLeadingZeros - 2 == 3
                && i << (numberOfLeadingZeros + 2) != 0) {
            return 1 << 32 - numberOfLeadingZeros + 1;
        } else {
            return 1 << 32 - numberOfLeadingZeros;
        }
    }

    public static void main(String[] args) {
        System.out.println(getInitialCapacity(882));
    }
}
