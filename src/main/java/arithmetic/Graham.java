package arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 二维凸包计算
 */
public class Graham {

    private static List<Node> of(Node... nodes) {
        return Arrays.stream(nodes).collect(Collectors.toList());
    }

    /**
     * 测试数据
     */
    public static void main(String[] args) {
        final List<Node> nodeList =
                of(new Node(0, 0)
                        , new Node(3, 3)
                        , new Node(-2, 6)
                        , new Node(0, 8)
                        , new Node(1, 8)
                        , new Node(4, 6)
                        , new Node(-4, 0)
                        , new Node(-6, 10)
                        , new Node(1, 10)
                        , new Node(-4, 5)
                        , new Node(5, 5)
                        , new Node(5, 6)
                        , new Node(-5, 7)
                        , new Node(-5, 8)
                        , new Node(1, 1)
                        , new Node(1, 2)
                );

        final Stack<Node> stack = getConvexHell(nodeList);
        stack.stream().forEach(System.out::println);
        System.out.println("\n");

        final double convexPolygonDiameter = RotateCalipers.getConvexPolygonDiameter(stack);
        System.out.println(convexPolygonDiameter);
        Arrays.stream(RotateCalipers.getConvexPolygonDiameter_MC_Node(stack))
                .forEach(System.out::println);
    }

    /**
     * 求凸包顶点集合
     * 这里不会排除重复点，预先的动作在外面去做
     *
     * @param nodeList 二维坐标系点，至少要有3个点才行
     * @return 凸包顶点集合
     */
    public static Stack<Node> getConvexHell(List<Node> nodeList) throws ArithmeticException {
        if (nodeList == null) throw new ArithmeticException("二维坐标点集合nodeList不能为null");
        if (nodeList.size() < 3) throw new ArithmeticException("计算凸包顶点至少要有三个二维坐标点才行，但是只有" + nodeList.size() + "个");

        //凸包顶点
        final Node p1 = findButtonOfLeftNode(nodeList);

        final List<Node> collect = nodeList.stream()
                .filter(node -> !node.equals(p1))
                .sorted((t1, t2) -> Graham.sortBySlop(t1, t2, p1))
                .collect(Collectors.toList());
        final Node p2 = collect.get(0);
        final Node p3 = collect.get(1);

        final Stack<Node> stack = new Stack<>();
        stack.push(p1);
        stack.push(p2);
        stack.push(p3);

        for (int i = 2; i < collect.size(); i++) {
            Graham.pushAndPop(collect.get(i), stack);
        }
        //末尾加入起始点，才成一个完整的多边形
        stack.push(p1);

        return stack;
    }

    /**
     * 寻找顶点，执行具体判断逻辑
     * <p>
     * 参考文章 https://www.jvruo.com/archives/38/
     * 参考文章 https://developer.aliyun.com/article/441831
     */
    public static void pushAndPop(Node t2, Stack<Node> stack) {
        final int size = stack.size();
        final Node t1 = stack.get(size - 1);
        final Node node0 = stack.get(size - 2);
        if (multi(t1, t2, node0).compareTo(BigDecimal.ZERO) < 0) {
            //右转 那么t1出栈，继续递归判断上一个点是否要出栈
            stack.pop();
            pushAndPop(t2, stack);
        } else {
            //左转 直接添加，然后跳过循环，去判断下一个点了
            stack.push(t2);
            return;
        }
    }

    /**
     * 查找y轴最小的点，如果有多个，则取其中x轴最小的那个点
     */
    public static Node findButtonOfLeftNode(List<Node> nodeList) throws ArithmeticException {
        final Node tempNode1 = nodeList.get(0);
        final Node tempNode2 = nodeList.get(1);
        //是否共线
        boolean isCollineation = true;
        Node p1 = null;
        for (int i = 0, len = nodeList.size(); i < len; i++) {
            final Node currNode = nodeList.get(i);
            if (isCollineation && i >= 2) {
                //判断所有点是不是都在tempNode1、tempNode2这条线上，是的话就成了线段，而不是多边形
                final BigDecimal multi = multi(tempNode1, tempNode2, currNode);
                isCollineation = multi.compareTo(BigDecimal.ZERO) == 0;
            }

            //查找y轴最小的点，如果有多个，则取其中x轴最小的那个点
            if (p1 == null) {
                p1 = currNode;
            } else {
                final int j = currNode.y.compareTo(p1.y);
                if (j < 0) {
                    p1 = currNode;
                } else if (j > 0) {
                } else {
                    //都在最底下，取左边那个
                    p1 = currNode.x.compareTo(p1.x) < 0 ? currNode : p1;
                }
            }

        }
        if (isCollineation) throw new ArithmeticException("所有点都在一条直线上，不能形成多边形");
        return p1;
    }

    @SuppressWarnings("DuplicateExpressions")
    public static int sortBySlop(Node t1, Node t2, Node node0) {
        if (t1.x.compareTo(t2.x) == 0) {
            return t1.y.compareTo(t2.y);
        }
        if (t1.x.compareTo(node0.x) == 0) {
            final BigDecimal k = t2.y.subtract(node0.y).divide(t2.x.subtract(node0.x), RoundingMode.HALF_DOWN);
            return k.compareTo(BigDecimal.ZERO);
        }
        if (t2.x.compareTo(node0.x) == 0) {
            final BigDecimal k = t1.y.subtract(node0.y).divide(t1.x.subtract(node0.x), RoundingMode.HALF_DOWN);
            return -k.compareTo(BigDecimal.ZERO);
        }

        final BigDecimal k1 = t1.y.subtract(node0.y).divide(t1.x.subtract(node0.x), RoundingMode.HALF_DOWN);
        final BigDecimal k2 = t2.y.subtract(node0.y).divide(t2.x.subtract(node0.x), RoundingMode.HALF_DOWN);
        final int compare1 = k1.compareTo(BigDecimal.ZERO);
        if (compare1 > 0) {
            final int compare2 = k2.compareTo(BigDecimal.ZERO);
            if (compare2 > 0) {
                return k1.compareTo(k2);
            }
            if (compare2 < 0) {
                return -1;
            }
        }
        if (compare1 < 0) {
            final int compare2 = k2.compareTo(BigDecimal.ZERO);
            if (compare2 > 0) {
                return 1;
            }
            if (compare2 < 0) {
                return k1.compareTo(k2);
            }
        }
        return 0;
    }

    /**
     * 叉乘运算、向量积
     * <p>
     * 向量x 叉乘 向量y = x1*y2 - x2*y1 = 所构成平行四边形面积
     *
     * @param t1    凑成第一个向量x,从node0到t1
     * @param t2    凑成第二个向量y,从node0到t2
     * @param node0 向量起始点
     * @return 如果为正数则向量y在向量x的逆时针方向，如果为负数则向量y在向量x顺时针方向
     */
    public static BigDecimal multi(Node t1, Node t2, Node node0) {
        return t1.x.subtract(node0.x).multiply(t2.y.subtract(node0.y))
                .subtract(t2.x.subtract(node0.x).multiply(t1.y.subtract(node0.y)));
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

    public static class RotateCalipers {

        /**
         * 计算凸包直径
         * <p>
         * 参考文章：https://www.jvruo.com/archives/79/
         * 参考文章：https://www.jianshu.com/p/74c25c0772d6
         *
         * @param nodeList 凸包顶点集合
         * @return 直径长度
         */
        public static double getConvexPolygonDiameter(List<Node> nodeList) {
            double diameter = 0;
            final int size = nodeList.size();
            for (int i = 1; i < size; i++) {
                BigDecimal lastMulti = null;
                final Node p1 = nodeList.get(i - 1);
                final Node p2 = nodeList.get(i);
                final Node heelPoints;
                //从构成平行线的点的下一个点开始循环，以保证面积单调递增
                int j = i + 1 < size - 1 ? i + 1 : 0;
                while (true) {
                    final Node p3 = nodeList.get(j);
                    final int k = j > 0 ? j - 1 : size - 1;
                    if (lastMulti == null) {
                        lastMulti = multi(p1, p2, p3).abs();
                    } else {
                        final BigDecimal thisMulti = multi(p1, p2, p3).abs();
                        if (lastMulti.compareTo(thisMulti) >= 0) {
                            heelPoints = nodeList.get(k);
                            break;
                        } else {
                            lastMulti = thisMulti;
                        }
                    }
                    j = j < size - 1 ? j + 1 : 1;
                }

                final double line1 = Math.sqrt(Math.pow(heelPoints.x.subtract(p1.x).doubleValue(), 2)
                        + Math.pow(heelPoints.y.subtract(p1.y).doubleValue(), 2)
                );
                final double line2 = Math.sqrt(Math.pow(heelPoints.x.subtract(p2.x).doubleValue(), 2)
                        + Math.pow(heelPoints.y.subtract(p2.y).doubleValue(), 2)
                );
                diameter = Math.max(diameter, Math.max(line1, line2));
            }
            return diameter;
        }

        /**
         * 魔改版，返回直径通过的两个点
         */
        public static Node[] getConvexPolygonDiameter_MC_Node(List<Node> nodeList) {
            Node[] nodes = new Node[2];
            double diameter = 0;
            final int size = nodeList.size();
            for (int i = 1; i < size; i++) {
                BigDecimal lastMulti = null;
                final Node p1 = nodeList.get(i - 1);
                final Node p2 = nodeList.get(i);
                final Node heelPoints;
                //从构成平行线的点的下一个点开始循环，以保证面积单调递增
                int j = i + 1 < size - 1 ? i + 1 : 0;
                while (true) {
                    final Node p3 = nodeList.get(j);
                    final int k = j > 0 ? j - 1 : size - 1;
                    if (lastMulti == null) {
                        lastMulti = multi(p1, p2, p3).abs();
                    } else {
                        final BigDecimal thisMulti = multi(p1, p2, p3).abs();
                        if (lastMulti.compareTo(thisMulti) >= 0) {
                            heelPoints = nodeList.get(k);
                            break;
                        } else {
                            lastMulti = thisMulti;
                        }
                    }
                    j = j < size - 1 ? j + 1 : 1;
                }

                final double line1 = Math.sqrt(Math.pow(heelPoints.x.subtract(p1.x).doubleValue(), 2)
                        + Math.pow(heelPoints.y.subtract(p1.y).doubleValue(), 2)
                );
                final double line2 = Math.sqrt(Math.pow(heelPoints.x.subtract(p2.x).doubleValue(), 2)
                        + Math.pow(heelPoints.y.subtract(p2.y).doubleValue(), 2)
                );
                if (line1 >= line2) {
                    if (diameter >= line1) {

                    } else {
                        nodes[0] = p1;
                        nodes[1] = heelPoints;
                    }
                } else {
                    if (diameter >= line2) {

                    } else {
                        nodes[0] = p2;
                        nodes[1] = heelPoints;
                    }
                }
                diameter = Math.max(diameter, Math.max(line1, line2));
            }
            return nodes;
        }

    }
}
