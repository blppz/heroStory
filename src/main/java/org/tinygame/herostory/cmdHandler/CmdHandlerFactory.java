package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 19:53
 */
public final class CmdHandlerFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(CmdHandlerFactory.class);
  private CmdHandlerFactory() {
  }
  private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>>map = new HashMap<>();
  public static void init() {
    String packageName = ICmdHandler.class.getPackage().getName();
    // 使用工具类获取到包下所有实现ICmdHandler接口的类
    Set<Class<?>> clazzSet = PackageUtil.listSubClazz(
        packageName,
        true,
        ICmdHandler.class
    );

    for (Class<?> clazz : clazzSet) {
      // 过滤掉接口
      if(clazz.isInterface()) {
        continue;
      }

      Method[] methods = clazz.getMethods();
      Class<?> type = null;
      for (Method method : methods) {
        if(!"handle".equals(method.getName())) {
          continue;
        }

        Class<?>[] paramTypes = method.getParameterTypes();
        // 过滤不符合条件的方法
        if(paramTypes.length<2 ||
           paramTypes[1].equals(GeneratedMessageV3.class) ||
           !GeneratedMessageV3.class.isAssignableFrom(paramTypes[1])
        ) {
          continue;
        }

        type = paramTypes[1];
      }

      LOGGER.info("完成关联: {} == {}", type, clazz.getName());

      try {
        map.put(
            type,
            (ICmdHandler<?>) clazz.newInstance()
        );

      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }
  public static ICmdHandler<? extends GeneratedMessageV3> getHandler(Class<?> clazz) {
    if(clazz==null) {
      return null;
    }

    return map.get(clazz);
  }
}
