package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Deacription 消息编码器
 * @Author BarryLee
 * @Date 2019/12/8 23:35
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {
  final Logger logger = LoggerFactory.getLogger(GameMsgEncoder.class);
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    // 判空、判断是否为GeneratedMessageV3
    if(!(msg instanceof GeneratedMessageV3)) {
      super.write(ctx, msg, promise);
      return ;
    }

    int msgCode = GameMsgRecognizer.getMsgCodeByMsg(msg.getClass());
    if(msgCode==-1) {
      return;
    }

    // 获取消息体
    byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();
    ByteBuf byteBuf = ctx.alloc().buffer();

    // 消息体
    byteBuf.writeShort((short)0);
    // 消息编号
    byteBuf.writeShort((short)msgCode);
    byteBuf.writeBytes(msgBody);

    BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
    super.write(ctx, frame, promise);
  }
}
