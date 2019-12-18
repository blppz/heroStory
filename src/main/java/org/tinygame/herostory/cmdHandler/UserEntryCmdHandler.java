package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:26
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

  public void handle(ChannelHandlerContext context, GameMsgProtocol.UserEntryCmd cmd) {
    if(context==null || cmd==null) {
      return;
    }

    // 获取用户Id
    Integer userId = (Integer)context.channel().attr(AttributeKey.valueOf("userId")).get();
    if(userId==null) {
      return;
    }

    // 获取进场用户
    User user = UserManager.getUserById(userId);
    if(user==null) {
      return;
    }
    LOGGER.info("用户进场user={}",user);

    // 构建resultBuilder
    GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
    resultBuilder.setUserId(userId);
    resultBuilder.setUserName(user.getName());
    resultBuilder.setHeroAvatar(user.getHeroAvatar());

    // 构建结果并发送
    GameMsgProtocol.UserEntryResult result = resultBuilder.build();
    Broadcaster.broadcaster(result);
  }
}
