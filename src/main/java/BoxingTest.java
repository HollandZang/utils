import java.util.concurrent.Semaphore;

public class BoxingTest {

    public static void main(String[] args) throws InterruptedException {
        final Semaphore o = new Semaphore(2);
        final Semaphore h = new Semaphore(1);


    }

    public void test() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;

        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));
    }
}
