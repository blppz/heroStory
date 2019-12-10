package org.tinygame.herostory.model;

/**
 * 用户
 */
public class User {
    /**
     * 用户 Id
     */
    public int userId;

    /**
     * 英雄形象
     */
    public String heroAvatar;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}