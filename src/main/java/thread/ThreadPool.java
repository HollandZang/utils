package thread;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPool {
    /**
     * newCachedThreadPool
     */
    private final ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            3L, TimeUnit.MINUTES,
            new SynchronousQueue<>(),
            new ThreadFactory() {
                private final String namePrefix = "SyncJob";
                private final ThreadGroup group = new ThreadGroup("SYNC");
                private final AtomicLong count = new AtomicLong(0);

                @Override
                public Thread newThread(Runnable target) {
                    final Thread thread = new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
                    thread.setUncaughtExceptionHandler((t, e) -> {
                        System.out.println(e);
                        e.printStackTrace();
                    });
                    return thread;
                }
            });

    public static void main(String[] args) throws InterruptedException {
        final ThreadPool threadPool = new ThreadPool();
        final Runnable runnable = () -> {
            try {
                throw new IOException("aaa");
            } catch (Exception e) {
                System.out.println("CATCH");
                throw new RuntimeException("a wrapper exception", e);
            }
        };
        try {
//            threadPool.executorService.submit(runnable);
            new Thread(runnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);
        System.out.println("END");
        threadPool.executorService.shutdown();
    }
}
