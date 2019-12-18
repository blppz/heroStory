import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/16 11:21
 */
public class MyExecutorService {
  BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>();
  Thread thread;
  MyExecutorService() {
    thread = new Thread(() -> {
      while(true) {
        Integer val = null;
        try {
          val = blockingQueue.take();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(val);
      }
    });
    thread.start();
  }
  void submit(Integer val) {
    blockingQueue.offer(val);
  }
}
