package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:26
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

  public void handle(ChannelHandlerContext context, GameMsgProtocol.UserEntryCmd cmd) {
    // 返回结果信息
    int userId = cmd.getUserId();
    String heroAvatar = cmd.getHeroAvatar();

    // 将新进来的用户加到_userMap用户字典
    User user = new User();
    user.setUserId(userId);
    user.setHeroAvatar(heroAvatar);
    user.setCurrHp(100680);
    UserManager.addUser(user);

    // 将userId附着到channel
    context.channel().attr(AttributeKey.valueOf("userId")).set(userId);

    // 构建resultBuilder
    GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
    resultBuilder.setUserId(userId);
    resultBuilder.setHeroAvatar(heroAvatar);

    // 构建结果并发送
    GameMsgProtocol.UserEntryResult result = resultBuilder.build();
    Broadcaster.broadcaster(result);
  }
}
