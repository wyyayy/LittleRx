package os;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Apple on 2016/10/16.
 */
public class Handler {

    private Queue<Runnable> queue = new LinkedList<>();

    public synchronized void loop() {
        for (; ; ) {
            Runnable runnable = queue.poll();
            if (runnable == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
                continue;
            }
            runnable.run();
        }
    }

    public synchronized void post(Runnable runnable) {
        queue.offer(runnable);
        notifyAll();
    }
}
