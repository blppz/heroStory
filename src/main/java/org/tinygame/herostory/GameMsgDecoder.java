package org.tinygame.herostory;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Deacription 消息解码器
 * @Author BarryLee
 * @Date 2019/12/8 18:54
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
  Logger logger = LoggerFactory.getLogger(GameMsgDecoder.class);
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if(!(msg instanceof BinaryWebSocketFrame)) {
      return;
    }

    // WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
    BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
    ByteBuf content = frame.content();
    // 读取消息长度
    content.readShort();
    // 读取消息编号
    int msgCode = content.readShort();

    // 消息解码器跟消息需要解耦
    // !!划重点！！
    // 下边这句放在读取消息体的前面是因为 - 读取消息体需要new数组，如果没获取到msgBuilder
    // 则会造成资源的浪费
    Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
    if(msgBuilder==null) {
      logger.error("无法识别的消息msgCode=" + msgCode);
    }

    // 读取消息体
    byte[] msgBody = new byte[content.readableBytes()];
    content.readBytes(msgBody);

    if(msgBuilder != null) {
      msgBuilder.clear();
      msgBuilder.mergeFrom(msgBody);
      Message newMsg = msgBuilder.build();
      if(newMsg!=null) {
        ctx.fireChannelRead(newMsg);
      }
    }
  }
}
