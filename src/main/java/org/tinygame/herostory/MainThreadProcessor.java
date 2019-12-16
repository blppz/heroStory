package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Deacription 单线程处理cmd
 * @Author BarryLee
 * @Date 2019/12/16 9:43
 */
public final class MainThreadProcessor {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);
  // 单例对象
  private static MainThreadProcessor instance = new MainThreadProcessor();
  private static final ExecutorService es = Executors.newSingleThreadExecutor(
      r -> new Thread(r, "MainThread")
    );

  // 私有化类默认构造器
  private MainThreadProcessor() {
  }

  public static MainThreadProcessor getInstance() {
    return instance;
  }

  public void process(ChannelHandlerContext context, GeneratedMessageV3 msg) {
    es.submit(() -> {
      Class<?> msgClazz = msg.getClass();
      LOGGER.info(
          "收到客户端消息，clazz={}，msg={}",
          msgClazz.getName(),
          msg
      );
      LOGGER.info("当前线程=={}",Thread.currentThread().getName());

      // 消息处理器不需要知道指令处理器是谁，所以下边的代码都要抽抽抽
      // 简单工厂
      ICmdHandler<? extends GeneratedMessageV3> cmd = CmdHandlerFactory.getHandler(msgClazz);

      if(cmd == null) {
        LOGGER.error(
            "未找到相对应的指令处理器 msgClazz={}",
            msgClazz.getName()
        );
        return;
      }

      cmd.handle(context, cast(msg));
    });
  }

  // 强转，可以骗过编译器
  private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
    if(msg==null) {
      return null;
    }
    return (TCmd)msg;
  }
}
