package concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadGate {
    // 条件谓词：opened-since(n) (isOpen || generation > n)
    private boolean isOpen;
    private int generation;

    public synchronized void close(){
        System.out.println("---------------close------------");
        isOpen = false;
    }

    public synchronized void open(){
        System.out.println("---------------open-------------");
        ++generation;
        isOpen = true;
        notifyAll();
    }

    /**
     *  阻塞并直到：opened-since(generation on entry)
     *
     * @throws InterruptedException
     */
    public synchronized void await() throws InterruptedException{
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation){
            wait();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadGate gate = new ThreadGate();
        AtomicLong count = new AtomicLong();

        Runnable runnable = ()->{
            System.out.println(Thread.currentThread().getName()+":启动");
            while (true){
                try {
                    gate.await();
                    count.incrementAndGet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                System.out.println(Thread.currentThread().getName()+": 运行中 -> "+count.get());
                try {
                    Thread.sleep(93);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }

        TimeUnit.SECONDS.sleep(1);
        boolean flag = true;

//        while (true){
//            if (flag){
//                flag = false;
                gate.open();
//            }else {
//                flag = true;
                gate.close();
//            }
//            Thread.sleep(50);
//        }
    }
}
