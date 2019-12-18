package org.tinygame.herostory.login;

import java.io.Serializable;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/17 15:34
 */
public class UserEntity implements Serializable {
  private Integer userId;
  private String userName;
  private String password;
  private String heroAvatar;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHeroAvatar() {
    return heroAvatar;
  }

  public void setHeroAvatar(String heroAvatar) {
    this.heroAvatar = heroAvatar;
  }

  @Override
  public String toString() {
    return "UserEntity{" +
        "id=" + userId +
        ", userName='" + userName + '\'' +
        ", password='" + password + '\'' +
        ", heroAvatar='" + heroAvatar + '\'' +
        '}';
  }

  public UserEntity(Integer userId, String userName, String password, String heroAvatar) {
    this.userId = userId;
    this.userName = userName;
    this.password = password;
    this.heroAvatar = heroAvatar;
  }

  public UserEntity() {
  }
}
