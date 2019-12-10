package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:53
 */
public final class CmdHandlerFactory {
  private CmdHandlerFactory() {
  }
  private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>>map = new HashMap<>();
  public static void init() {
    map.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
    map.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
    map.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());
  }
  public static ICmdHandler<? extends GeneratedMessageV3> getHandler(Class<?> clazz) {
    if(clazz==null) {
      return null;
    }

    return map.get(clazz);
  }
}
