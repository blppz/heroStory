import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @Deacription BlockQueue是一个可以多线程存取数据的阻塞式队列，
 * 存数据是非阻塞的，取数据是阻塞式的，如果没有数据，会一直等着
 * @Author BarryLee
 * @Date 2019/12/16 11:11
 */
public class TestBlockQueue {
  @Test
  public void testBlockQueue1() {
    BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>();

    // 创建三个线程，t1和t2负责放数据，t3负责取数据
    Thread t1 = new Thread(() -> {
      for(int i = 0; i < 10; i++) {
        try {
          TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        blockingQueue.offer(i);
      }
    });

    Thread t2 = new Thread(() -> {
      for(int i = 10; i < 20; i++) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        blockingQueue.offer(i);
      }
    });

    Thread t3 = new Thread(() -> {
      while(true) {
        try {
          int val = blockingQueue.take();
          System.out.println("T3取出 --> " + val);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread t4 = new Thread(() -> {
      while(true) {
        try {
          int val = blockingQueue.take();
          System.out.println("T4取出 --> " + val);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    // 启动三个线程
    t1.start();
    t2.start();
    t3.start();
    t4.start();
    // join
    try {
      t1.join();
      t2.join();
      t3.join();
      t4.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMyExecutorService() {
    MyExecutorService es = new MyExecutorService();

    // 创建三个线程，t1和t2负责放数据，t3负责取数据
    Thread t1 = new Thread(() -> {
      for(int i = 0; i < 10; i++) {
        try {
          TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        es.submit(i);
      }
    });

    Thread t2 = new Thread(() -> {
      for(int i = 10; i < 20; i++) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        es.submit(i);
      }
    });

    t1.start();
    t2.start();
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
