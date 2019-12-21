package org.tinygame.herostory.rank;

/**
 * @Deacription 排名实体类
 * @Author BarryLee
 * @Date 2019/12/20 21:54
 */
public class RankItem {
  /**
   * 排名 Id
   */
  private Integer rankId;

  /**
   * 用户 Id
   */
  private Integer userId;

  /**
   * 用户名称
   */
  private String userName;

  /**
   * 英雄形象
   */
  private String heroAvatar;

  /**
   * 胜利次数
   */
  private Integer win;

  @Override
  public String toString() {
    return "RankItem{" +
        "rankId=" + rankId +
        ", userId=" + userId +
        ", userName='" + userName + '\'' +
        ", heroAvatar='" + heroAvatar + '\'' +
        ", win=" + win +
        '}';
  }

  public Integer getRankId() {
    return rankId;
  }

  public void setRankId(Integer rankId) {
    this.rankId = rankId;
  }

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

  public String getHeroAvatar() {
    return heroAvatar;
  }

  public void setHeroAvatar(String heroAvatar) {
    this.heroAvatar = heroAvatar;
  }

  public Integer getWin() {
    return win;
  }

  public void setWin(Integer win) {
    this.win = win;
  }
}
