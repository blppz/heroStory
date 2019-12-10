package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @Deacription 消息识别器 -- 其实也是一个工厂
 *
 * @Author BarryLee
 * @Date 2019/12/9 21:40
 */
public final class GameMsgRecognizer {
  private static final Logger logger = LoggerFactory.getLogger(GameMsgRecognizer.class);
  private static final Map<Integer, GeneratedMessageV3>msgCodeAndMsgBodyMap = new HashMap<>();
  private static final Map<Class<?>,Integer>msgAndMsgCodeMap = new HashMap<>();
  private GameMsgRecognizer() {
  }

  public static void init() {

    msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE,GameMsgProtocol.UserEntryCmd.getDefaultInstance());
    msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE,GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
    msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE,GameMsgProtocol.UserMoveToCmd.getDefaultInstance());
    msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE,GameMsgProtocol.UserQuitResult.getDefaultInstance());

    msgAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class,GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
    msgAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class,GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
    msgAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class,GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
  }

  public static Message.Builder getBuilderByMsgCode(Integer msgCode) {
    if(msgCode==null) {
      return null;
    }
    GeneratedMessageV3 msg = msgCodeAndMsgBodyMap.get(msgCode);
    if(msg==null) {
      return null;
    }
    return msg.newBuilderForType();
  }

  public static Integer getMsgCodeByMsg(Class<?> clazz) {
    if(clazz==null) {
      return -1;
    }
    Integer msgCode = msgAndMsgCodeMap.get(clazz);
    if(msgCode==null) {
      return -1;
    }
    return msgCode;
  }
}
