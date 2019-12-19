package org.tinygame.herostory.async;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/18 21:45
 */
public interface IAsyncOperation {
  /**
   * 异步操作
   */
  void doAsync();

  /**
   * 完成异步操作，然后调用此方法
   */
  default void doFinish() {
  }

  /**
   *
   * @return
   */
  default int bindId() {
    return 0;
  }
}
