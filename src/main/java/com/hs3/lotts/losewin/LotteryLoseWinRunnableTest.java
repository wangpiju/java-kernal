package com.hs3.lotts.losewin;

import com.hs3.entity.lotts.Bet;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class LotteryLoseWinRunnableTest {
    private static JedisPool pool = null;

    @Before
    public void setUp() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxWaitMillis(5000L);
        config.setMaxTotal(2000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        pool = new JedisPool(config, "127.0.0.1", 6379);
    }

    @Test
    public void test() {
        LotteryLoseWinRunnable run = new LotteryLoseWinRunnable("cqssc", pool);
        int h = 1;
        for (int i = 0; i < 10; i++) {
            Bet b = new Bet();
            b.setLotteryId("cqssc");
            b.setSeasonId("20160702-00" + h);
            b.setGroupName("时时彩");
            b.setUnit(new BigDecimal("2"));
            b.setPrice(1);
            b.setPlayerId("ssc_star5");
            b.setContent("0123456789,0123456789,0123456789,0123456789,0123456789");
            b.setAmount(new BigDecimal("200000"));
            b.setId("s_" + i);
            if (h == 1) {
                h = 2;
            } else {
                h = 1;
            }
            run.put(b, true);
        }
        run.start();
    }
}
