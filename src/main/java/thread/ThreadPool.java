package thread;

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
                    return new Thread(this.group, target, this.namePrefix + "-" + this.count.incrementAndGet());
                }
            });
}
