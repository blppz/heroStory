package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/4 8:42
 */
public class ServerMain {
  static private final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

  // http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=169.254.142.48:12345&userId=1
  public static void main(String[] args) {
    CmdHandlerFactory.init();
    GameMsgRecognizer.init();
    /*
     * bossGroup、workerGroup是两个线程池
     * bossGroup是负责处理客户端连接的，有连接的时候会建立SocketChannel
     * 建立好连接之后将这个channel给了workerGroup，他来负责处理客户端的读写消息
     * 一个负责连接，一个负责业务
     *
     * NioEventLoopGroup：Event好了之后举个爪告诉它
     *
     * Netty是一个通讯框架
     * Reactor是一个模型，刚好可以利用Netty来构建这个模型
     */
    EventLoopGroup bossGroup = new NioEventLoopGroup(); // 拉客的美女
    EventLoopGroup workerGroup = new NioEventLoopGroup(); // 上菜的服务生

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup,workerGroup);
    bootstrap.channel(NioServerSocketChannel.class); // 服务器信道的处理方式
    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
            new HttpServerCodec(),//服务器编解码器
            new HttpObjectAggregator(65535),//消息长度限制
            // WebSocket 协议处理器, 在这里处理握手、ping、pong 等消息
            new WebSocketServerProtocolHandler("/websocket"),
            new GameMsgDecoder(),//自定义消息解码器
            new GameMsgEncoder(),//自定义消息编码器
            new GameMsgHandler()//自定义消息处理器
        );
      }
    });

    try {
      ChannelFuture future = bootstrap.bind(12345).sync();
      if(future.isSuccess()) {
        LOGGER.info("服务器启动成功");
      }
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
