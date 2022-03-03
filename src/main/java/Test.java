import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        final Map<String, Integer> collect = Arrays.stream(new String[]{"a", "a", "b", "c"})
                .distinct()
                .collect(Collectors.toMap(s -> s, s -> 1, (v1, v2) -> v1 + v2, () -> new ConcurrentHashMap<>()));
        System.out.println(collect);
    }


}
