import java.util.Arrays;

class Solution {

    public static void main(String[] args) {
        int[] widths = {3, 4, 10, 4, 8, 7, 3, 3, 4, 9, 8, 2, 9, 6, 2, 8, 4, 9, 9, 10, 2, 4, 9, 10, 8, 2};
        String s = "mqblbtpvicqhbrejb";
        System.out.println(Arrays.toString(new Solution().numberOfLines(widths, s)));
    }

    public int[] numberOfLines(int[] widths, String s) {
        int restWindow = 100;
        int currLine = 1;

        final char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            final int need = widths[c - 97];

            if ((restWindow -= need) <= -1) {
                currLine++;
                restWindow = 100 - need;
            }
            System.out.println(s.substring(0, i + 1) + "\t" + currLine + "\t" + (100 - restWindow));
        }
        return new int[]{currLine, 100 - restWindow};
    }
}