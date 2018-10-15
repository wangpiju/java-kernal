package com.hs3.commons;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-21 11:23
 **/
public class RedisCons {
    /**
     * 奖池
     */
    public static final String BONUS_POOL = "bonus_pool_";
    public static final String FIRST_BONUS_POOL_FULL = "first_bonus_pool_full";

    public static final String BET_AMOUNT_POOL = "bet_amount_pool_";

    public static final String BONUS_COLLECT_COUNT = "bonus_collect_count";

    public static String getBonusPool(Integer pMonth) {
        return BONUS_POOL + pMonth;
    }
    public static String getBetAmountPool(Integer pMonth) {
        return BET_AMOUNT_POOL + pMonth;
    }

}
