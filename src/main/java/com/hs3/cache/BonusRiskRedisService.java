package com.hs3.cache;

import com.hs3.commons.RedisCons;
import com.hs3.commons.Whether;
import com.hs3.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BonusRiskRedisService {

    private static final Logger logger = LoggerFactory.getLogger(BonusRiskRedisService.class);

    public Long incrBonusPool(String lotteryId, Long bonus) {
        try {
            Long result = RedisUtils.hincr(RedisCons.BONUS_POOL, lotteryId, bonus);
            logger.info(String.format("--> incrBonusPool , lotteryId:%s, bonus:%s, result : %s", lotteryId, bonus, result));
            return result;
        } catch (Exception e) {
            logger.error(String.format("--> incrBonusPool error, lotteryId: %s, bonus : %s", lotteryId, bonus), e);
        }
        return 0L;
    }

    public void setBonusPool(String lotteryId, Long bonus) {
        try {
            Boolean result = RedisUtils.hset(RedisCons.BONUS_POOL, lotteryId, bonus + "");
            logger.info(String.format("--> setBonusPool , lotteryId:%s, bonus:%s, result : %s", lotteryId, bonus, result));
        } catch (Exception e) {
            logger.error(String.format("--> setBonusPool error, lotteryId : %s, bonus : %s", lotteryId, bonus) ,e);
        }
    }

    public void incrBonusPoolPlus(String lotteryId, Long bonus) {
        RedisUtils.hincr(RedisCons.BONUS_POOL, lotteryId, bonus * 10);
    }


    public Long getBonusPool(String lotteryId) {
        String result = RedisUtils.hget(RedisCons.BONUS_POOL, lotteryId);
        logger.info(String.format("--> getBonusPool , lotteryId:%s, result : %s", lotteryId, result));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long clearBonusPool(String lotteryId) {
        try {
            return RedisUtils.hdel(RedisCons.BONUS_POOL, lotteryId);
        } catch (Exception e) {
            logger.error("--> clearBonusPool error, lotteryId : "+lotteryId, e);
        }
        return 0L;
    }

    public boolean delBonusPool() {
        return RedisUtils.del(RedisCons.BONUS_POOL);
    }

    public boolean delFirstBonusPoolFull() {
        return RedisUtils.del(RedisCons.FIRST_BONUS_POOL_FULL);
    }

    public boolean delBetAmountPool() {
        return RedisUtils.del(RedisCons.BET_AMOUNT_POOL);
    }

    public Map<String, String> getAllBonusPool() {
        try {
            return RedisUtils.hgetAll(RedisCons.BONUS_POOL);
        } catch (Exception e) {
            logger.error("--> getAllBonusPool error ," ,e);
        }
        return new HashMap<>();
    }

    public Map<String, String> getAllBetAmountPool() {
        return RedisUtils.hgetAll(RedisCons.BET_AMOUNT_POOL);
    }

    public Long incrBetAmountPool(String lotteryId, Long betAmount) {
        try {
            Long result = RedisUtils.hincr(RedisCons.BET_AMOUNT_POOL, lotteryId, betAmount);
            logger.info(String.format("--> incrBetAmountPool , lotteryId:%s, betAmount:%s, result : %s", lotteryId, betAmount, result));
            return result;
        } catch (Exception e) {
            logger.error(String.format("--> incrBetAmountPool error , lotteryId : %s, betAmount : %s", lotteryId, betAmount), e);
        }
        return 0L;
    }

    public void setFirstBonusPoolFull(String lotteryId) {
        boolean result = RedisUtils.hset(RedisCons.FIRST_BONUS_POOL_FULL, lotteryId, Whether.yes.getStatus() + "");
        logger.info(String.format("--> setFirstBonusPoolFull , lotteryId:%s, result : %s", lotteryId, result));
    }

    public boolean getFirstBonusPoolFull(String lotteryId) {
        String status = RedisUtils.hget(RedisCons.FIRST_BONUS_POOL_FULL, lotteryId);
        logger.info(String.format("--> getFirstBonusPoolFull , lotteryId:%s, result : %s", lotteryId, status));
        return StringUtils.isNotBlank(status) && Integer.parseInt(status) == Whether.yes.getStatus();
    }

    /* 风控 v4*/

    /**
     * 增加清理次数
     */
    public int incrCollectCount(String lotteryId, Long incr) {
        try {
            Long collectCount = RedisUtils.hincr(RedisCons.BONUS_COLLECT_COUNT, lotteryId, incr);
            logger.info(String.format("--> incrCollectCount, lotteryId : %s, result : %s", lotteryId, collectCount));
            return collectCount == null ? 0 : collectCount.intValue();
        } catch (Exception e) {
            logger.error("--> incrCollectCount error, lotteryId : "+lotteryId, e);
        }
        return 0;
    }

    /**
     * 获取清理次数
     * @param lotteryId
     * @return
     */
    public int getCollectCount(String lotteryId) {
        try {
            String result =RedisUtils.hget(RedisCons.BONUS_COLLECT_COUNT, lotteryId);
            logger.info(String.format("--> getCollectCount , lotteryId : %s, result : %s",lotteryId, result));
            return StringUtils.isNotBlank(result)?Integer.parseInt(result):0;
        } catch (NumberFormatException e) {
            logger.error("--> getCollectCount error, lotteryId : "+lotteryId, e);
        }
        return 0;
    }

    public boolean delCollectCount() {
        try {
            return RedisUtils.del(RedisCons.BONUS_COLLECT_COUNT);
        } catch (Exception e) {
            logger.error("--> delCollectCount error ...", e);
        }
        return false;
    }
}
