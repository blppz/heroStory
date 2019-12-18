import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/16 11:21
 */
public class MyExecutorService2 {
  BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
  Thread thread;
  MyExecutorService2() {
    thread = new Thread(() -> {
      while(true) {
        try {
          Runnable r = blockingQueue.take();
          if(r != null) {
            r.run();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
  }
  void submit(Runnable r) {
    if(r != null) {
      blockingQueue.offer(r);
    }
  }
}
