package org.tinygame.herostory.login.db;

import java.util.List;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/17 15:36
 */
public interface IUserDao {
  /**
   * 根据用户名查找用户
   * @param userName 用户名
   * @return UserEntity
   */
  UserEntity findByName(String userName);

  /**
   * 添加一个UserEntity
   */
  void addUserEntity(UserEntity userEntity);

  /**
   * 查找所有用户
   */
  List<UserEntity> findAll();
}
