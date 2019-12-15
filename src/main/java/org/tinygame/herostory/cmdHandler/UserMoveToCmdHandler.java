package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:29
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
  public void handle(ChannelHandlerContext context, GameMsgProtocol.UserMoveToCmd cmd) {
    // 移动的那位战士的id
    // 为什么是Integer而不是int，因为有可能拿到空值
    // 为什么从channel中拿到用户id而不是客户端/前端发过来，因为如果这个id被修改了，那就是外挂了
    // 所以用户登录的时候直接将它的id附着在channel
    Integer userId = (Integer)context.channel().attr(AttributeKey.valueOf("userId")).get();
    if(userId==null) {
      return;
    }

    // 获取移动的用户
    User moveUser = UserManager.getUserById(userId);
    if(moveUser==null) {
      return;
    }

    // 只要用户移动，就重新设置它的移动状态等信息
    MoveState moveState = moveUser.getMoveState();
    moveState.setFromPosX(cmd.getMoveFromPosX());
    moveState.setFromPosY(cmd.getMoveFromPosY());
    moveState.setToPosX(cmd.getMoveToPosX());
    moveState.setToPosY(cmd.getMoveToPosY());
    moveState.setStartTime(System.currentTimeMillis());

    // 封装信息
    GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
    resultBuilder.setMoveUserId(userId);
    resultBuilder.setMoveFromPosX(moveState.getFromPosX());
    resultBuilder.setMoveFromPosY(moveState.getFromPosY());
    resultBuilder.setMoveToPosX(moveState.getToPosX());
    resultBuilder.setMoveToPosY(moveState.getToPosY());
    resultBuilder.setMoveStartTime(moveState.getStartTime());

    // 广播信息
    Broadcaster.broadcaster(resultBuilder.build());
  }
}
