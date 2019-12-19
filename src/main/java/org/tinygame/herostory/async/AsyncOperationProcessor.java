package org.tinygame.herostory.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MainThreadProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/18 22:05
 */
public final class AsyncOperationProcessor {
  // 单例
  private static final AsyncOperationProcessor instance = new AsyncOperationProcessor();
  // 日志
  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncOperationProcessor.class);

  // 线程池
  private /*static*/ final ExecutorService[] es = new ExecutorService[8];

  // 私有化类默认构造器
  private AsyncOperationProcessor() {
    for (int i = 0; i < es.length; i++) {
      final String threadName = "AsyncOperationProcessor_" + i;
      es[i] = Executors.newSingleThreadExecutor(
          (r)-> new Thread(r, threadName)
      );
    }
  }

  // 获取单例实体
  public static AsyncOperationProcessor getInstance() {
    return instance;
  }

  /**
   * 协助进行异步操作
   * @param asyncOp 异步操作具体实现
   */
  public void process(IAsyncOperation asyncOp) {
    if(asyncOp == null) {
      return;
    }

    final int bindId = asyncOp.bindId();
    int i = bindId % 8;

    es[i].submit(() -> {
       try {
         // 执行异步逻辑
         asyncOp.doAsync();

         //Thread.sleep(1000);

         System.out.println("=============doFinish============");
         // 返回主线程执行完成逻辑
         MainThreadProcessor.getInstance().process(asyncOp::doFinish);
       } catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
       }
    });
  }
}
