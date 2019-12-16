package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription 自定义的消息处理类
 * @Author BarryLee
 * @Date 2019/12/4 9:29
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    Broadcaster.addChannel(ctx.channel());
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    super.handlerRemoved(ctx);
    Broadcaster.removeChannel(ctx.channel());

    Integer userId = (Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
    if(userId==null) {
      return;
    }
    UserManager.removeUser(userId);
    // 通知其他用户有人离场
    GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
    resultBuilder.setQuitUserId(userId);
    GameMsgProtocol.UserQuitResult result = resultBuilder.build();
    Broadcaster.broadcaster(result);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {
    if(context==null || msg==null) {
      return;
    }

    if(msg instanceof GeneratedMessageV3) {
      MainThreadProcessor.getInstance().process(context, (GeneratedMessageV3)msg);
    }
  }


}
