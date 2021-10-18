package MapUtils;

public class MapUtils {
    /**
     * 根据List长度计算Map初始化容量
     *
     * @param i List长度
     * @return Map初始化容量
     */
    private static int getInitialCapacity(int i) {
        if (i <= 1) return 2;
        if (i <= 3) return 4;
        final String b = Integer.toBinaryString(i);

        return "11".equals(b.substring(0, 2)) &&
                b.substring(2).contains("1")
                ? 1 << (b.length() + 1)
                : 1 << b.length();
    }
}
