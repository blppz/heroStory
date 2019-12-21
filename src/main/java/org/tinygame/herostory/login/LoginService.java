package org.tinygame.herostory.login;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.async.AsyncOperationProcessor;
import org.tinygame.herostory.async.IAsyncOperation;
import org.tinygame.herostory.login.db.IUserDao;
import org.tinygame.herostory.login.db.UserEntity;
import org.tinygame.herostory.util.MySqlSessionFactory;
import org.tinygame.herostory.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.function.Function;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/17 15:37
 */
public final class LoginService {
  private final static Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
  private final static LoginService instance = new LoginService();

  private LoginService() {
  }

  public static LoginService getInstance() {
    return instance;
  }

  /**
   * 用户登录
   * 需求：存在该用户名则检查密码，密码对就登录；不存在该用户名则注册
   * @param name 用户名
   * @param password 密码
   * @return UserEntity
   */
  public void userLogin(String name, String password, Function<UserEntity, Void> callback) {
    if(name==null || password==null) {
      return;
    }

    IAsyncOperation asyncOp = new AsyncGetUserByName(name,password) {
      @Override
      public void doFinish() {
        if(callback != null) {
          callback.apply(this.getUserEntity());
        }
      }

      @Override
      public int bindId() {
        return name.length()^name.hashCode()>>2;
      }
    };

    AsyncOperationProcessor.getInstance().process(asyncOp);
  }

  private class AsyncGetUserByName implements IAsyncOperation {
    private final String username;
    private final String password;
    private UserEntity userEntity;

    public AsyncGetUserByName(String username, String password) {
      this.username = username;
      this.password = password;
    }

    public UserEntity getUserEntity() {
      return userEntity;
    }

    @Override
    public void doAsync() {
      try(SqlSession session = MySqlSessionFactory.openSession()) {
        // 看看当前线程
        LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

        IUserDao mapper = session.getMapper(IUserDao.class);

        userEntity = mapper.findByName(username);

        if(userEntity!=null) {// 有这个用户，需要检查密码是否正确
          if(!password.equals(userEntity.getPassword())) {
            LOGGER.error("用户密码输入有误==>userId={},userName={}",userEntity.getUserId(),userEntity.getUserName());
            throw new RuntimeException("密码错误");
          }

          LOGGER.info("用户登录，用户ID={}，用户名={}",userEntity.getUserId(),userEntity.getUserName());
        } else { // 没有这个用户，则注册
          userEntity = new UserEntity();
          userEntity.setUserName(username);
          userEntity.setPassword(password);
          userEntity.setHeroAvatar("Hero_Shaman"); // 默认角色 - 萨满

          mapper.addUserEntity(userEntity);
          LOGGER.info("用户注册-->{}",userEntity);
        }

        // 更新 Redis 中用户信息
        LoginService.getInstance().updateUserBasicInfoInRedis(userEntity);
      }catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
  }

  /**
   * 更新 Redis 中的用户实体信息
   * @param userEntity 用户实体
   */
  private void updateUserBasicInfoInRedis(UserEntity userEntity) {
    if(userEntity==null || userEntity.getUserId()<0) {
      return;
    }

    try(Jedis redis = RedisUtil.getJedis()) {
      int userId = userEntity.getUserId();

      JSONObject json = new JSONObject();
      json.put("userId", userEntity.getUserId());
      json.put("username", userEntity.getUserName());
      json.put("heroAvatar", userEntity.getHeroAvatar());

      LOGGER.info("hset key={},field={},val={}","User_"+userId, "BasicInfo", json.toJSONString());
      redis.hset("User_" + userId, "BasicInfo", json.toJSONString());
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
