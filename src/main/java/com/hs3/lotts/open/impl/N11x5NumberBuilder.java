package com.hs3.lotts.open.impl;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.open.BaseNumberBuilder;
import com.hs3.service.lotts.LotteryLoseWinService;
import com.hs3.utils.ListUtils;
import com.hs3.utils.NumUtils;
import com.hs3.web.utils.SpringContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

/**
 * 11选5
 */
public class N11x5NumberBuilder extends BaseNumberBuilder {
    private static final Logger logger = LoggerFactory.getLogger(N11x5NumberBuilder.class);

    public String getTitle() {
        return "n11x5";
    }

    public String getRemark() {
        return "11选5算法";
    }

    protected List<Integer> getList() {
        return new ArrayList<>(Arrays.asList(10, 2, 5,
                6, 9, 1, 4, 11,
                7, 3, 8));
    }

    private List<Integer> create1(Random ran) {
        List<Integer> nums = getNums(getNums(ran), ran);

        List<Integer> rel = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int index = NumUtils.getRandom(ran, 0, nums.size() - 1);
            rel.add(nums.remove(index));
        }
        return rel;
    }

    private List<Integer> autoCreate(Random ran) {
        return create1(ran);
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
        season.setN4(last.get(3));
        season.setN5(last.get(4));
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

            BigDecimal lastWinAndBetAmount = null;
            BigDecimal lastWinAmount = null;
            for (Integer i : getNums(ran)) {
                BigDecimal amount1 = toAmount(maps.get(String.format("%d,-,-,-,-", i)));
                if (amount1.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                    List<Integer> list_j = getNums(ran);
                    list_j.remove(i);
                    for (Integer j : list_j) {
                        BigDecimal amount2 = toAmount(maps, "-,%d,-,-,-", new Object[]{j})
                                .add(toAmount(maps, "%d,%d,-,-,-", i, j));

                        BigDecimal winAmount2 = amount1.add(amount2);
                        if (winAmount2.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                            List<Integer> list_k = getNums(ran);
                            list_k.remove(i);
                            list_k.remove(j);
                            for (Integer k : list_k) {
                                BigDecimal amount3 = toAmount(maps, "-,-,%d,-,-", new Object[]{k})
                                        .add(toAmount(maps, "%d,-,%d,-,-", i, k))
                                        .add(toAmount(maps, "-,%d,%d,-,-", j, k))
                                        .add(toAmount(maps, "%d,%d,%d,-,-", i, j, k));

                                BigDecimal winAmount3 = winAmount2.add(amount3);
                                if (winAmount3.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                                    List<Integer> list_m = getNums(ran);
                                    list_m.remove(i);
                                    list_m.remove(j);
                                    list_m.remove(k);
                                    for (Integer m : list_m) {
                                        BigDecimal amount4 = toAmount(maps, "-,-,-,%d,-", new Object[]{m})
                                                .add(toAmount(maps, "%d,-,-,%d,-", i, m))
                                                .add(toAmount(maps, "-,%d,-,%d,-", j, m))
                                                .add(toAmount(maps, "-,-,%d,%d,-", k, m))
                                                .add(toAmount(maps, "-,%d,%d,%d,-", j, k, m))
                                                .add(toAmount(maps, "%d,-,%d,%d,-", i, k, m))
                                                .add(toAmount(maps, "%d,%d,-,%d,-", i, j, m))
                                                .add(toAmount(maps, "%d,%d,%d,%d,-", i, j, k, m));

                                        BigDecimal winAmount4 = winAmount3.add(amount4);
                                        if (winAmount4.divide(betAmount, 2, 1).compareTo(ratio) <= 0) {
                                            List<Integer> list_n = getNums(ran);
                                            list_n.remove(i);
                                            list_n.remove(j);
                                            list_n.remove(k);
                                            list_n.remove(m);
                                            for (Integer n : list_n) {
                                                BigDecimal amount5 = toAmount(maps, "-,-,-,-,%d", new Object[]{n})
                                                        .add(toAmount(maps, "%d,-,-,-,%d", i, n))
                                                        .add(toAmount(maps, "-,%d,-,-,%d", j, n))
                                                        .add(toAmount(maps, "-,-,%d,-,%d", k, n))
                                                        .add(toAmount(maps, "-,-,-,%d,%d", m, n))
                                                        .add(toAmount(maps, "%d,%d,-,-,%d", i, j, n))
                                                        .add(toAmount(maps, "%d,-,%d,-,%d", i, k, n))
                                                        .add(toAmount(maps, "%d,-,-,%d,%d", i, m, n))
                                                        .add(toAmount(maps, "-,%d,%d,-,%d", j, k, n))
                                                        .add(toAmount(maps, "-,%d,-,%d,%d", j, m, n))
                                                        .add(toAmount(maps, "-,-,%d,%d,%d", k, m, n))
                                                        .add(toAmount(maps, "-,%d,%d,%d,%d", j, k, m, n))
                                                        .add(toAmount(maps, "%d,-,%d,%d,%d", i, k, m, n))
                                                        .add(toAmount(maps, "%d,%d,-,%d,%d", i, j, m, n))
                                                        .add(toAmount(maps, "%d,%d,%d,-,%d", i, j, k, n))
                                                        .add(toAmount(maps, "%d,%d,%d,%d,%d", i, j, k, m, n));

                                                BigDecimal winAmount = winAmount4.add(amount5);

                                                BigDecimal winAndBetAmount = winAmount.divide(betAmount, 2, 1);
                                                if (winAndBetAmount.compareTo(ratio) <= 0) {
                                                    if ((lastWinAndBetAmount == null)
                                                            || (winAndBetAmount.compareTo(lastWinAndBetAmount) > 0)) {
                                                        lastWinAndBetAmount = winAndBetAmount;
                                                        lastWinAmount = winAmount;
                                                        list = new ArrayList<>();
                                                        list.add(i);
                                                        list.add(j);
                                                        list.add(k);
                                                        list.add(m);
                                                        list.add(n);
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
