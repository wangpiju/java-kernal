package com.hs3.lotts.losewin;

import com.hs3.entity.lotts.Bet;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.service.lotts.LotteryLoseWinService;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public class LotteryLoseWinRunnable extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(LotteryLoseWinService.class);
    private JedisPool jedisPool;
    private String lotteryId;
    private PriorityBlockingQueue<LoseWinInfo> queue;

    public LotteryLoseWinRunnable(String lotteryId, JedisPool jedisPool) {
        queue = new PriorityBlockingQueue<>(100, Comparator.comparing(LoseWinInfo::getKey));
        this.lotteryId = lotteryId;
        this.jedisPool = jedisPool;
    }

    private int getExpire() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(11);
        if (hour >= 5) {
            cal.add(5, 1);
        }
        cal.set(11, 5);
        cal.set(12, 0);
        cal.set(13, 0);
        int result = (int) ((cal.getTimeInMillis() - System.currentTimeMillis()) / 1000L);
        return result;
    }

    private void setError(String key) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.hset(key, "ERROR", "1");
            logger.info("封锁忽略成功：" + key);
        } catch (Exception e) {
            logger.error("封锁忽略失败：" + e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    private boolean hasError(String key) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            String v = jedis.hget(key, "ERROR");
            return !StrUtils.hasEmpty(v);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
        return true;
    }

    private BigDecimal update(LoseWinInfo info) throws InterruptedException {
        String lockKey = null;
        Jedis jedis = null;
        try {
            String key = info.getKey();
            lockKey = key + "_lock";

            jedis = this.jedisPool.getResource();

            long lock = jedis.setnx(lockKey, lockKey);
            while (lock != 1L) {
                Thread.sleep(10L);
                lock = jedis.setnx(lockKey, lockKey);
            }
            jedis.expire(lockKey, 5);

            Map<String, String> maps = jedis.hgetAll(key);
            Pipeline pipeline = jedis.pipelined();

            BigDecimal betA = info.getBetAmount();
            if (maps.containsKey("BET_AMOUNT")) {
                betA = betA.add(new BigDecimal(maps.get("BET_AMOUNT")));
            }
            pipeline.hset(key, "BET_AMOUNT", betA.toString());
            for (String k : info.getContent().keySet()) {
                BigDecimal v = info.getContent().get(k);
                if (maps.containsKey(k)) {
                    v = v.add(new BigDecimal(maps.get(k)));
                }
                pipeline.hset(key, k, v.toString());
            }
            pipeline.expire(key, getExpire());
            pipeline.sync();
            return betA;
        } finally {
            if (jedis != null) {
                jedis.del(lockKey);

                jedis.close();
                jedis = null;
            }
        }
    }

    public void put(Bet b, boolean odAdd) {
        try {
            if (!b.getLotteryId().equals(this.lotteryId)) {
                return;
            }
            LoseWinInfo info = new LoseWinInfo();

            info.setId(b.getId());

            info.setKey(b.getLotteryId() + "_" + b.getSeasonId());

            BigDecimal price = b.getUnit().multiply(new BigDecimal(b.getPrice().toString())).divide(new BigDecimal("2"),
                    4, 1);

            BigDecimal amount = b.getAmount();
            if (!odAdd) {
                amount = amount.multiply(new BigDecimal("-1"));
                price = price.multiply(new BigDecimal("-1"));
            }
            info.setBetAmount(amount);

            LotteryBase lottBase = LotteryFactory.getInstance(b.getGroupName());
            PlayerBase pb = lottBase.getPlayer(b.getPlayerId());
            Map<String, BigDecimal> win = pb.ifOpenWin(b.getContent());

            Map<String, BigDecimal> content = new HashMap<>();
            for (String k : win.keySet()) {
                content.put(k, win.get(k).multiply(price));
            }
            info.setContent(content);

            this.queue.add(info);
        } catch (Exception e) {
            logger.error("封锁队列失败：" + b.getLotteryId() + "_" + b.getSeasonId() + "_" + b.getId() + "," + e.getMessage(),
                    e);
        }
    }

    public void run() {
        for (; ; ) {
            LoseWinInfo info = null;
            try {
                info = this.queue.take();
                long s1 = System.currentTimeMillis();
                if (!hasError(info.getKey())) {
                    BigDecimal betA = update(info);
                    long s2 = System.currentTimeMillis();

                    String msg = "封锁成功:" + info.getKey() + "_" + info.getId() + ",投注金额:" + info.getBetAmount() + ",总金额："
                            + betA + ",耗时：" + (s2 - s1);

                    logger.info(msg);
                }
            } catch (Exception e) {
                if (info != null) {
                    logger.error("封锁失败_" + info.getKey() + "_" + info.getId() + "_" + info.getBetAmount() + ":"
                            + e.getMessage(), e);
                    setError(info.getKey());
                } else {
                    logger.error("封锁失败:" + e.getMessage(), e);
                }
            }
        }
    }
}
