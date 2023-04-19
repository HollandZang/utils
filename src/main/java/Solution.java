import file.FileUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Solution {

    public static void main(String[] args) {

        HashMap<String, Integer> m = new HashMap<>();
        Arrays.stream(FileUtil.INSTANCE.readFile4Line("/Users/holland/Downloads", "t.out"))
                .forEach(it -> {
                    int s = it.indexOf("Topic: ");
                    int e = it.indexOf(", BrokersSent");
                    String k = it.substring(s + "Topic: ".length(), e);
                    m.compute(k, (key, v) -> {
                        return (v == null) ? 0 : v + 1;
                    });
                });
        m.entrySet().forEach(stringIntegerEntry -> {
            System.out.println(stringIntegerEntry.getKey() + ": " + stringIntegerEntry.getValue());
        });
        System.out.println(m.size());
    }
}
