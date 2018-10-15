package com.hs3.lotts.open.impl;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.open.BaseNumberBuilder;
import com.hs3.service.lotts.LotteryLoseWinService;
import com.hs3.utils.CollectionUtils;
import com.hs3.utils.ListUtils;
import com.hs3.utils.NumUtils;
import com.hs3.utils.RedisUtils;
import com.hs3.web.utils.SpringContext;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class K3NumberBuilder extends BaseNumberBuilder {
    private static final Logger logger = LoggerFactory.getLogger(K3NumberBuilder.class);

    public String getTitle() {
        return "k3";
    }

    public String getRemark() {
        return "快3算法";
    }

    private static final String ALL_SAME_COUNTER = "k3_allsame_counter";
    private static final String LAST_ALL_SAME = "k3_last_allsame";

    protected List<Integer> getList() {
        return new ArrayList<>(Arrays.asList(2, 5, 6,
                1, 4, 3));
    }

    public static void main(String[] args) {
        RedisUtils.incr("test123");
        RedisUtils.incr("test123");
        System.out.print(RedisUtils.get("test123"));
    }

    private List<Integer> create1(Random ran) {
        List<Integer> nums = getNums(getNums(ran), ran);
        List<Integer> last = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int n = NumUtils.getRandom(ran, 0, 5);
            last.add(nums.get(n));
        }
        return last;
    }

    private List<Integer> create2(Random ran) {
        List<Integer> nums = new ArrayList<>();
        List<Integer> last = new ArrayList<>();

        int times = NumUtils.getRandom(ran, 200, 1000);
        for (int i = 0; i < times; i++) {
            nums.add(NumUtils.getRandom(ran, 0, 5));
        }
        for (int i = 0; i < 3; i++) {
            int n = NumUtils.getRandom(ran, 0, nums.size() - 1);
            last.add(nums.remove(n));
        }
        return last;
    }

    public  List<Integer> autoCreate(Random ran) {
        List<Integer> last = new ArrayList<>();
        last.add(NumUtils.getRandom(ran, 1, 6));
        last.add(NumUtils.getRandom(ran, 1, 6));
        last.add(NumUtils.getRandom(ran, 1, 6));
        //TODO 注释掉重开的判断规则  檢查是否一小時內已經開過3次
//        try {
//            //如果是豹子
//            if (CollectionUtils.checkAllSame(last)) {
//                //檢查是否連開
//                if (null != RedisUtils.get(LAST_ALL_SAME)) {
//                    ran = new Random();
//                    last.clear();
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                    RedisUtils.del(LAST_ALL_SAME);
//                }
//                else
//                    RedisUtils.set(LAST_ALL_SAME, "true");
//
//                boolean needReAutoCreate = false;
//
//                if (null != RedisUtils.get(ALL_SAME_COUNTER)) {
//                    Integer count = Integer.valueOf(RedisUtils.get(ALL_SAME_COUNTER));
//                    //大於三次則重開
//                    if (count >= 3)
//                        needReAutoCreate = true;
//                    else{
//                        RedisUtils.incr(ALL_SAME_COUNTER);
//                    }
//                } else {
//                    //一小時時效的計數器
//                    RedisUtils.incr(ALL_SAME_COUNTER);
//                    RedisUtils.expire(ALL_SAME_COUNTER, 60 * 60);
//                }
//                //重開
//                if (needReAutoCreate) {
//                    ran = new Random();
//                    last.clear();
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                    last.add(NumUtils.getRandom(ran, 1, 6));
//                }
//            }
//        } catch (Exception e) {
//            logger.error("check k3 all same error: " + e.getMessage(), e);
//        }
        Collections.sort(last);
        return last;
    }

    public LotterySeason create(String lotteryId, String seasonId, BigDecimal ratio, BigDecimal deviation, Random ran) {
        LotterySeason season = new LotterySeason();
        List<Integer> last = null;
        if (ran == null) {
            ran = NumUtils.getRandomInstance();
        }
        if (ratio != null) {
            last = createByTable(lotteryId, seasonId, ratio, deviation, ran);
        } else {
            last = autoCreate(ran);
        }
        season.setN1(last.get(0));
        season.setN2(last.get(1));
        season.setN3(last.get(2));
        season.setLotteryId(lotteryId);
        season.setSeasonId(seasonId);
        logger.info("生成的号码：" + lotteryId + "_" + seasonId + "_" + ListUtils.toString(last));
        return season;
    }

    private List<Integer> createByTable(String lotteryId, String seasonId, BigDecimal ratio, BigDecimal deviation,
                                        Random ran) {
        List<Integer> list = null;
        String key = lotteryId + "_" + seasonId;
        try {
            LotteryLoseWinService service = (LotteryLoseWinService) SpringContext.getBean("lotteryLoseWinService");
            Map<String, String> maps = service.getMap(lotteryId, seasonId);
            if (maps == null) {
                list = autoCreate(ran);
                logger.info("自主彩种" + key + ":内存无法连接，使用随机生成号码：" + ListUtils.toString(list));
                return list;
            }
            if (maps.size() == 0) {
                list = autoCreate(ran);
                logger.info("自主彩种" + key + ":没有投注,随机生成：" + ListUtils.toString(list));
                return list;
            }
            if (maps.containsKey("ERROR")) {
                list = autoCreate(ran);
                logger.info("自主彩种" + key + ":保存时有异常,随机生成：" + ListUtils.toString(list));
                service.remove(key);
                return list;
            }
            service.remove(key);

            BigDecimal betAmount = new BigDecimal(maps.get("BET_AMOUNT"));

            List<Integer> list_i = getNums(ran);
            List<Integer> list_j = getNums(ran);
            List<Integer> list_k = getNums(ran);

            BigDecimal lastWinAndBetAmount = null;
            BigDecimal lastWinAmount = null;
            for (Integer i : list_i ) {
                BigDecimal amount1 = toAmount(maps, "%d--", i);
                if (amount1.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                    for (int j : list_j ) {

                        BigDecimal amount2 = toAmount(maps, "-%d-", new Object[]{j})
                                .add(toAmount(maps, "%%d-", i, j));

                        BigDecimal winAmount2 = amount1.add(amount2);
                        if (winAmount2.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                            for (int k: list_k) {
                                BigDecimal amount3 = toAmount(maps, "--%d", new Object[]{k})
                                        .add(toAmount(maps, "%d-%d", i, k))
                                        .add(toAmount(maps, "-%d%d", j, k))
                                        .add(toAmount(maps, "%d%d%d", i, j, k));

                                BigDecimal winAmount3 = winAmount2.add(amount3);

                                BigDecimal winAndBetAmount = winAmount3.divide(betAmount, 2, 1);
                                if (winAndBetAmount.compareTo(ratio) <= 0) {
                                    if ((lastWinAndBetAmount == null)
                                            || (winAndBetAmount.compareTo(lastWinAndBetAmount) > 0)) {
                                        lastWinAndBetAmount = winAndBetAmount;
                                        lastWinAmount = winAmount3;
                                        list = new ArrayList<>();
                                        list.add(i);
                                        list.add(j);
                                        list.add(k);
                                        if (ratio.subtract(winAndBetAmount).compareTo(deviation) <= 0) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (list == null) {
                list = autoCreate(ran);
                logger.info("自主彩种" + key + ":全部亏损,随机生成：" + ListUtils.toString(list));
                return list;
            }
            logger.info("自主彩种" + key + ":根据封锁得到结果：" + ListUtils.toString(list) + "。投注：" + betAmount + ",中奖："
                    + lastWinAmount + ",中投比：" + lastWinAndBetAmount.multiply(new BigDecimal("100")) + "%");
            return list;
        } catch (Exception ex) {
            list = autoCreate(ran);
            logger.error("自主彩种" + key + ":杀数时异常，使用随机生成号码：" + ListUtils.toString(list) + ":" + ex.getMessage(), ex);
        }
        return list;
    }
}
