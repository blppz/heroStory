package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.rank.RankItem;
import org.tinygame.herostory.rank.RankService;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/20 21:46
 */
public class GetRankCmdHandler implements ICmdHandler<GameMsgProtocol.GetRankCmd> {
  private static final Logger LOGGER = LoggerFactory.getLogger(GetRankCmdHandler.class);
  @Override
  public void handle(ChannelHandlerContext context, GameMsgProtocol.GetRankCmd cmd) {
    if(context==null || cmd==null) {
      return;
    }

    GameMsgProtocol.GetRankResult.Builder resultBuilder = GameMsgProtocol.GetRankResult.newBuilder();
    RankService.getInstance().getRank((rankList)->{
      if(rankList == null || rankList.isEmpty()) {
        return ;
      }

      for (RankItem rankItem : rankList) {
        LOGGER.info("rankItem={}",rankItem.toString());
        GameMsgProtocol.GetRankResult.RankItem.Builder rankItemBuilder
            = GameMsgProtocol.GetRankResult.RankItem.newBuilder();
        rankItemBuilder.setRankId(rankItem.getRankId());
        rankItemBuilder.setUserId(rankItem.getUserId());
        rankItemBuilder.setUserName(rankItem.getUserName());
        rankItemBuilder.setHeroAvatar(rankItem.getHeroAvatar());

        // 不用 build()
        resultBuilder.addRankItem(rankItemBuilder);
      }

      // 这里犯下低级错误：将消息发送放到了异步操作外面，所以发送消息的时候 resultBuilder 还是空的
      context.writeAndFlush(resultBuilder.build());
    });

  }
}
