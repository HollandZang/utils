import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 二维凸包计算
 */
public class Graham {

    public static void main(String[] args) {
        final List<Node> nodeList =
                List.of(new Node(0, 0)
                        , new Node(3, 3)
                        , new Node(1, 8)
                        , new Node(4, 6)
                        , new Node(10, 5)
                        , new Node(-2, 6)
                        , new Node(-10, 7)
                );

        final Node buttonNode = findButtonNode(nodeList);

        System.out.println(buttonNode);

        final List<Node> collect = nodeList.stream()
                .filter(node -> node.equals(buttonNode))
                .sorted((t1, t2) -> sortBySlop(t1, t2, buttonNode))
                .collect(Collectors.toList());
    }

    private static Node findButtonNode(List<Node> nodeList) {
        return nodeList.stream().reduce((t1, t2) -> t1.y.compareTo(t2.y) <= 0 ? t1 : t2).get();
    }

    private static int sortBySlop(Node t1, Node t2, Node node0) {
        return 0;
    }

    public static class Node {
        private final BigDecimal x, y;

        public Node(double x, double y) {
            this.x = BigDecimal.valueOf(x);
            this.y = BigDecimal.valueOf(y);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
