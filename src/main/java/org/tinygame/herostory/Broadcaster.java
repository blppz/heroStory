package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Deacription 广播消息
 *
 * @Author BarryLee
 * @Date 2019/12/9 16:49
 */
public final class Broadcaster {
  private Broadcaster() {
  }

  // 客户端信道数组, 一定要使用 static, 否则无法实现群发
  private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  public static void addChannel(Channel channel) {
    _channelGroup.add(channel);
  }

  public static void removeChannel(Channel channel) {
    _channelGroup.remove(channel);
  }

  public static void broadcaster(Object obj) {
    _channelGroup.writeAndFlush(obj);
  }
}
