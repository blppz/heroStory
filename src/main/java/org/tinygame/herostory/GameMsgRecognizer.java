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
    Class<?>[] innerClazzArr = GameMsgProtocol.class.getDeclaredClasses();
    for(Class<?> innerClazz: innerClazzArr) {
      String clazzName = innerClazz.getSimpleName().toLowerCase();

      for(GameMsgProtocol.MsgCode msgCode: GameMsgProtocol.MsgCode.values()) {
        String strMsgCode = msgCode.name().replaceAll("_", "").toLowerCase();
        if(strMsgCode.startsWith(clazzName)) {
          try {
            Object returnVal = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
            msgCodeAndMsgBodyMap.put(msgCode.getNumber(), (GeneratedMessageV3)returnVal);
            msgAndMsgCodeMap.put(innerClazz,msgCode.getNumber());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
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
