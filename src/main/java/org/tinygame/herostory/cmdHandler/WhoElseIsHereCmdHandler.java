package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:28
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {
  public void handle(ChannelHandlerContext context, GameMsgProtocol.WhoElseIsHereCmd cmd) {
    GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

    for(User curUser: UserManager.getUserMap().values()) {
      // 判空，养成习惯
      if(curUser==null) {
        continue;
      }
      // 构建每一个用户的信息
      GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
      userInfoBuilder.setUserId(curUser.getUserId());
      userInfoBuilder.setHeroAvatar(curUser.getHeroAvatar());

      // 构建用户移动状态
      GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateBuilder =
          GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
      MoveState mvState = curUser.getMoveState();
      moveStateBuilder.setFromPosX(mvState.getFromPosX());
      moveStateBuilder.setFromPosY(mvState.getFromPosY());
      moveStateBuilder.setToPosX(mvState.getToPosX());
      moveStateBuilder.setToPosY(mvState.getToPosY());
      moveStateBuilder.setStartTime(mvState.getStartTime());

      userInfoBuilder.setMoveState(moveStateBuilder);

      resultBuilder.addUserInfo(userInfoBuilder);
    }
    GameMsgProtocol.WhoElseIsHereResult result = resultBuilder.build();
    context.writeAndFlush(result);
  }
}
