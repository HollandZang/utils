public class IntegerUtils {
    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final int i1 = get0(i);
            final int i2 = Integer.numberOfTrailingZeros(i);
            if (i1 != i2) {
                System.out.println(i1 + "!=" + i2 + ",i=" + i);
                return;
            }
        }
        System.out.println("OK");

        new Thread(() -> {
            final long l = System.currentTimeMillis();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                Integer.numberOfTrailingZeros(i);
            }
            System.out.println("Integer.numberOfTrailingZeros::" + (System.currentTimeMillis() - l));
        }).start();

        new Thread(() -> {
            final long l = System.currentTimeMillis();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                get0(i);
            }
            System.out.println("get0::" + (System.currentTimeMillis() - l));
        }).start();

        new Thread(() -> {
            final long l = System.currentTimeMillis();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                get1(i);
            }
            System.out.println("get1::" + (System.currentTimeMillis() - l));
        }).start();

        new Thread(() -> {
            final long l = System.currentTimeMillis();
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                get2(i);
            }
            System.out.println("get2::" + (System.currentTimeMillis() - l));
        }).start();
    }

    public static int get0(int i) {
        if (i == 0) return 32;
        if (i == 1) return 0;
        for (int j = 1; j < 32; j++) {
            if (i << j == 0) return 32 - j;
        }
        return 0;
    }

    public static int get1(int i) {
        if (i == 0) return 32;
        if (i == 1) return 0;
        for (int j = 1; j < 32; j++) {
            if (i >> j << j == i) {
                continue;
            } else {
                return j - 1;
            }
        }
        return -1;
    }

    public static int get2(int i) {
        if (i >= 65536) {
            return get0(i);
        } else {
            return get1(i);
        }
    }
}
