package arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 一元线性回归预测
 * 最小二乘法
 */
public class LinearRegression {

    private final BigDecimal m;
    private final BigDecimal c;

    public static void main(String[] args) {
        final List<Node> list = new ArrayList<>();
        list.add(new Node(1, 2));
        list.add(new Node(2, 4));
        final BigDecimal y = new LinearRegression(list).getY(BigDecimal.valueOf(20));
        System.out.println(y);
    }

    /**
     * @param sample 数据集, key: X, value: Y
     */
    public LinearRegression(List<Node> sample) {
        final BigDecimal size = BigDecimal.valueOf(sample.size());
        final BigDecimal x = sample.stream().map(Node::getX).reduce(BigDecimal::add).get().divide(size, 10, RoundingMode.HALF_UP);
        final BigDecimal y = sample.stream().map(Node::getY).reduce(BigDecimal::add).get().divide(size, 10, RoundingMode.HALF_UP);
        final BigDecimal xy = sample.stream().map(o -> o.getX().multiply(o.getY())).reduce(BigDecimal::add).get().divide(size, 10, RoundingMode.HALF_UP);
        final BigDecimal sqrX = sample.stream().map(o -> o.getX().multiply(o.getX())).reduce(BigDecimal::add).get().divide(size, 10, RoundingMode.HALF_UP);
        m = xy.subtract(x.multiply(y))
                .divide(sqrX.subtract(x.multiply(x)), 10, RoundingMode.HALF_UP);
        c = y.subtract(x.multiply(m));
    }

    /**
     * @param x 特征值
     * @return y     结果值
     */
    public BigDecimal getY(BigDecimal x) {
        return x.multiply(m).add(c).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 二维坐标系的点数据结构
     */
    public static class Node {
        public final BigDecimal x, y;

        public Node(double x, double y) {
            this.x = BigDecimal.valueOf(x);
            this.y = BigDecimal.valueOf(y);
        }

        public Node(String x, String y) {
            this.x = BigDecimal.valueOf(Double.parseDouble(x));
            this.y = BigDecimal.valueOf(Double.parseDouble(y));
        }

        public BigDecimal getX() {
            return x;
        }

        public BigDecimal getY() {
            return y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(x, node.x) && Objects.equals(y, node.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
