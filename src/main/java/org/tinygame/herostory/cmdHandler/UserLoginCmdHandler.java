package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.login.LoginService;
import org.tinygame.herostory.login.UserEntity;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/17 15:38
 */
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

  @Override
  public void handle(ChannelHandlerContext context, GameMsgProtocol.UserLoginCmd cmd) {
    if(context==null || cmd==null) {
      return ;
    }

    final String userName = cmd.getUserName();
    final String password = cmd.getPassword();

    LOGGER.info("用户登录 ==> userName={},password={}", userName,password);

    LoginService.getInstance().userLogin(userName,password,(user)->{
      LOGGER.info("当前线程 = {}",Thread.currentThread().getName());

      // 登录失败
      if(user == null) {
        LOGGER.error("用户登录失败,userName={}",userName);
        return null;
      }

      // 登录成功
      LOGGER.info("用户登录成功,userName={}",userName);

      // 新建用户
      User newUser = new User();
      newUser.setUserId(user.getUserId());
      newUser.setCurrHp(100860);
      newUser.setName(user.getUserName());
      newUser.setHeroAvatar(user.getHeroAvatar());
      // 并将用户加入到用户管理器
      UserManager.addUser(newUser);

      // 将用户id附着到channel
      context.channel().attr(AttributeKey.valueOf("userId")).set(newUser.getUserId());

      // 封装结果消息
      GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
      resultBuilder.setUserId(newUser.getUserId());
      resultBuilder.setUserName(newUser.getName());
      resultBuilder.setHeroAvatar(newUser.getHeroAvatar());

      // 构建结果并发送
      GameMsgProtocol.UserLoginResult result = resultBuilder.build();
      // 这里不要用broadcaster类，那个是广播给大家看的，而这里只是发送给当前用户
      context.writeAndFlush(result);

      return null;
    });
  }
}
