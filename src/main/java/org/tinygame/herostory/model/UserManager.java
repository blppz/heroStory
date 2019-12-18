package org.tinygame.herostory.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/9 17:15
 */
public final class UserManager {
  private UserManager() {
  }

  // 用户字典
  private static final ConcurrentMap<Integer, User> _userMap = new ConcurrentHashMap<>();

  public static void addUser(User user) {
    _userMap.put(user.getUserId(),user);
  }

  public static void removeUser(Integer userId) {
    _userMap.remove(userId);
  }

  public static Map<Integer,User>getUserMap() {
    return _userMap;
  }

  public static User getUserById(Integer userId) {
    return _userMap.get(userId);
  }
}
