package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
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
    if(attkUserId==null) {
      return;
    }

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

    // 被攻击者
    User targetUser = UserManager.getUserById(targetUserId);
    if(targetUser==null) {
      return;
    }

    // 减血
    int subtractHp = 10086;
    targetUser.currHp -= subtractHp;
    broadcastSubtractHp(targetUserId, subtractHp);

    // 是否死亡
    if(targetUser.currHp <= 0) {
      broadcastDie(targetUserId);
    }
  }

  /**
   * 广播减血消息
   * @param targetUserId
   * @param subtractHp
   */
  private void broadcastSubtractHp(int targetUserId, int subtractHp) {
    GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder2 = GameMsgProtocol.UserSubtractHpResult.newBuilder();
    resultBuilder2.setSubtractHp(subtractHp);
    resultBuilder2.setTargetUserId(targetUserId);
    GameMsgProtocol.UserSubtractHpResult result2 = resultBuilder2.build();
    Broadcaster.broadcaster(result2);
  }

  /**
   * 广播死亡消息
   * @param targetUserId
   */
  private void broadcastDie(int targetUserId) {
    GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
    resultBuilder.setTargetUserId(targetUserId);
    Broadcaster.broadcaster(resultBuilder.build());
  }
}
