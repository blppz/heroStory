package org.tinygame.herostory.login;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.util.MySqlSessionFactory;

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
   * @return
   */
  public UserEntity userLogin(String name, String password) {
    if(name==null || password==null) {
      return null;
    }

    try(SqlSession session = MySqlSessionFactory.openSession()) {
      // 看看当前线程
      LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

      IUserDao mapper = session.getMapper(IUserDao.class);

      /*// test
      final List<UserEntity> all = mapper.findAll();
      System.out.println("===============test==============");
      for (UserEntity u : all) {
        System.out.println(u);
      }
      System.out.println("===============test==============");
      //////////////*/

      UserEntity user = mapper.findByName(name);

      if(user!=null) {// 有这个用户，需要检查密码是否正确
        if(password.equals(user.getPassword())) {
          LOGGER.info("用户登录，用户ID={}，用户名={}",user.getUserId(),user.getUserName());
          return user;
        }

        LOGGER.error("用户密码输入有误==>userId={},userName={}",user.getUserId(),user.getUserName());
        throw new RuntimeException("密码错误");
      } else { // 没有这个用户，则注册
        UserEntity newUser = new UserEntity();
        newUser.setUserName(name);
        newUser.setPassword(password);
        newUser.setHeroAvatar("Hero_Shaman"); // 默认角色 - 萨满

        mapper.addUserEntity(newUser);
        LOGGER.info("用户注册-->{}",newUser);
        return newUser;
      }
    }catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return null;
  }
}
