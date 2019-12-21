package org.tinygame.herostory.rank;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MainThreadProcessor;
import org.tinygame.herostory.async.AsyncOperationProcessor;
import org.tinygame.herostory.async.IAsyncOperation;
import org.tinygame.herostory.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Deacription TODO
 * @Author BarryLee
 * @Date 2019/12/20 21:48
 */
public final class RankService {
  private RankService() {
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(RankService.class);
  private static RankService instance = new RankService();

  public static RankService getInstance() {
    if(instance==null) {
      throw new RuntimeException("RankService 尚未初始化");
    }

    return instance;
  }

  public void getRank(Consumer<List<RankItem>> callback) {
    IAsyncOperation asyncOp = new AsyncGetRank(){
      @Override
      public void doFinish() {
        callback.accept(this.getRank());
      }
    };

    AsyncOperationProcessor.getInstance().process(asyncOp);
  }

  private class AsyncGetRank implements IAsyncOperation {
    private List<RankItem> list = new ArrayList<>();

    public List<RankItem> getRank() {
      return list;
    }

    @Override
    public void doAsync() {
      // 看看当前线程
      LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

      try(Jedis redis = RedisUtil.getJedis()) {
        Set<Tuple> ranks = redis.zrangeWithScores("Rank", 0, 9);
        LOGGER.info("ranks.size={}", ranks.size());
        int rankId = 0;
        for (Tuple rank : ranks) {
          LOGGER.info("userId={}, win={}",rank.getElement(), rank.getScore());
          String userId = rank.getElement();
          if(userId == null || userId.isEmpty()) {
            continue;
          }

          String jsonStr = redis.hget("User_" + userId, "BasicInfo");
          LOGGER.info("jsonString={}",jsonStr);
          if(jsonStr == null || jsonStr.isEmpty()) {
            continue;
          }

          RankItem item = new RankItem();
          JSONObject jsonObj = JSONObject.parseObject(jsonStr);
          item.setUserName(jsonObj.getString("username"));
          item.setHeroAvatar(jsonObj.getString("heroAvatar"));
          item.setRankId(++rankId);
          item.setUserId(Integer.parseInt(userId));
          item.setWin((int) rank.getScore());

          list.add(item);
        }
      } catch (Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
      }
    }
  }
}
