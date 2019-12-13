package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/13 10:53
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

  @Override
  public void handle(ChannelHandlerContext context, GameMsgProtocol.UserAttkCmd cmd) {

    // 攻击用户id
    Integer attkUserId = (Integer) context.channel().attr(AttributeKey.valueOf("userId")).get();
    // 被攻击用户id
    int targetUserId = cmd.getTargetUserId();
    if(targetUserId<=0) {
      return;
    }

    GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
    resultBuilder.setAttkUserId(attkUserId);
    resultBuilder.setTargetUserId(targetUserId);

    GameMsgProtocol.UserAttkResult result = resultBuilder.build();
    Broadcaster.broadcaster(result);

    ///////////
    GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder2 = GameMsgProtocol.UserSubtractHpResult.newBuilder();
    resultBuilder2.setSubtractHp(10086);
    resultBuilder2.setTargetUserId(targetUserId);

    GameMsgProtocol.UserSubtractHpResult result2 = resultBuilder2.build();
    Broadcaster.broadcaster(result2);
  }
}
