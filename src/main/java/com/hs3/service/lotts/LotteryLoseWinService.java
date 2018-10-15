package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryLockDao;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.LotteryLock;
import com.hs3.lotts.losewin.LotteryLoseWinRunnable;
import com.hs3.web.utils.SpringContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class LotteryLoseWinService {
    private static final Logger logger = LoggerFactory.getLogger(LotteryLoseWinService.class);
    private static Map<String, LotteryLoseWinRunnable> maps = new HashMap();
    private static ExecutorService es = Executors.newFixedThreadPool(50);
    public static final String BET_AMOUNT = "BET_AMOUNT";
    private JedisPool jedisPool;
    @Autowired
    private LotteryLockDao lotteryLockDao;

    private boolean getConfig() {
        try {
            if (this.jedisPool == null) {
                this.jedisPool = ((JedisPool) SpringContext.getBean("jedisPool"));
                if (this.jedisPool == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    private boolean check(Bet bet) {
        try {
            String lotteryId = bet.getLotteryId();
            if (!getConfig()) {
                return false;
            }
            if (bet.getTest().intValue() != 0) {
                return false;
            }
            LotteryLock lock = this.lotteryLockDao.findByLotteryId(lotteryId);
            if ((lock == null) || (lock.getStatus().intValue() != 0)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("封锁检查失败:" + e.getMessage(), e);
        }
        return false;
    }

    private Object locks = new Object();

    private LotteryLoseWinRunnable getRunnable(String lotteryId) {
        synchronized (this.locks) {
            LotteryLoseWinRunnable runn = null;
            if (maps.containsKey(lotteryId)) {
                runn = (LotteryLoseWinRunnable) maps.get(lotteryId);
            } else {
                runn = new LotteryLoseWinRunnable(lotteryId, this.jedisPool);

                maps.put(lotteryId, runn);
                runn.start();
            }
            return runn;
        }
    }

    public void addLoseAndWin(final Bet bet) {
        if (check(bet)) {
            es.execute(new Runnable() {
                public void run() {
                    try {
                        LotteryLoseWinService.this.getRunnable(bet.getLotteryId()).put(bet, true);
                    } catch (Exception e) {
                        LotteryLoseWinService.logger.error(e.getMessage(), e);
                    }
                }
            });
        }
    }

    public void subtractLoseAndWin(final Bet bet) {
        if (check(bet)) {
            es.execute(new Runnable() {
                public void run() {
                    try {
                        LotteryLoseWinService.this.getRunnable(bet.getLotteryId()).put(bet, false);
                    } catch (Exception e) {
                        LotteryLoseWinService.logger.error(e.getMessage(), e);
                    }
                }
            });
        }
    }

    public Map<String, String> getMap(String lotteryId, String seasonId) {
        if (!getConfig()) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            return jedis.hgetAll(lotteryId + "_" + seasonId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }

    public void remove(String key) {
        if (!getConfig()) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.del(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return;
        } finally {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        }
    }
}
