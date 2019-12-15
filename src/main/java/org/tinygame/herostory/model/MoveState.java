package org.tinygame.herostory.model;

import java.io.Serializable;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/15 10:28
 */
public class MoveState implements Serializable {
  private float fromPosX;
  private float fromPosY;
  private float toPosX;
  private float toPosY;
  private long startTime;

  public MoveState() {
  }

  public MoveState(float fromPosX, float fromPosY, float toPosX, float toPosY, long startTime) {
    this.fromPosX = fromPosX;
    this.fromPosY = fromPosY;
    this.toPosX = toPosX;
    this.toPosY = toPosY;
    this.startTime = startTime;
  }

  @Override
  public String toString() {
    return "MoveState{" +
        "fromPosX=" + fromPosX +
        ", fromPosY=" + fromPosY +
        ", toPosX=" + toPosX +
        ", toPosY=" + toPosY +
        ", startTime=" + startTime +
        '}';
  }

  public float getFromPosX() {
    return fromPosX;
  }

  public void setFromPosX(float fromPosX) {
    this.fromPosX = fromPosX;
  }

  public float getFromPosY() {
    return fromPosY;
  }

  public void setFromPosY(float fromPosY) {
    this.fromPosY = fromPosY;
  }

  public float getToPosX() {
    return toPosX;
  }

  public void setToPosX(float toPosX) {
    this.toPosX = toPosX;
  }

  public float getToPosY() {
    return toPosY;
  }

  public void setToPosY(float toPosY) {
    this.toPosY = toPosY;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }
}
