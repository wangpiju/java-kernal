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
 * pk10
 */
public class Pk10NumberBuilder extends BaseNumberBuilder {
    private static final Logger logger = LoggerFactory.getLogger(Pk10NumberBuilder.class);

    public String getTitle() {
        return "pk10";
    }

    public String getRemark() {
        return "PK10算法";
    }

    protected List<Integer> getList() {
        return new ArrayList<>(Arrays.asList(10, 2, 5,
                6, 9, 1, 4, 7,
                3, 8));
    }

    private List<Integer> create1(Random ran) {
        return getNums(getNums(ran), ran);
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
        season.setN6(last.get(5));
        season.setN7(last.get(6));
        season.setN8(last.get(7));
        season.setN9(last.get(8));
        season.setN10(last.get(9));

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
            for (Integer n1 : getNums(ran)) {
                BigDecimal amount1 = toAmount(maps, "%d,-,-,-,-,-,-,-,-,-", n1);
                if (getWinAndBet(amount1, betAmount).compareTo(ratio) <= 0) {
                    List<Integer> n2List = getNums(ran);
                    n2List.remove(n1);
                    for (Integer n2 : n2List) {
                        BigDecimal amount2 = toAmount(maps, "-,%d,-,-,-,-,-,-,-,-", new Object[]{n2})
                                .add(toAmount(maps, "%d,%d,-,-,-,-,-,-,-,-", n1, n2)).add(amount1);
                        if (getWinAndBet(amount2, betAmount).compareTo(ratio) <= 0) {
                            List<Integer> n3List = getNums(ran);
                            n3List.remove(n1);
                            n3List.remove(n2);
                            for (Integer n3 : n3List) {
                                BigDecimal amount3 = toAmount(maps, "-,-,%d,-,-,-,-,-,-,-", new Object[]{n3})
                                        .add(toAmount(maps, "%d,-,%d,-,-,-,-,-,-,-", n1, n3))
                                        .add(toAmount(maps, "-,%d,%d,-,-,-,-,-,-,-", n2, n3))
                                        .add(toAmount(maps, "%d,%d,%d,-,-,-,-,-,-,-", n1, n2, n3))
                                        .add(amount2);
                                if (getWinAndBet(amount3, betAmount).compareTo(ratio) <= 0) {
                                    List<Integer> n4List = getNums(ran);
                                    n4List.remove(n1);
                                    n4List.remove(n2);
                                    n4List.remove(n3);
                                    for (Integer n4 : n4List) {
                                        BigDecimal amount4 = toAmount(maps, "-,-,-,%d,-,-,-,-,-,-", new Object[]{n4})
                                                .add(toAmount(maps, "%d,-,-,%d,-,-,-,-,-,-", n1, n4))
                                                .add(toAmount(maps, "-,%d,-,%d,-,-,-,-,-,-", n2, n4))
                                                .add(toAmount(maps, "-,-,%d,%d,-,-,-,-,-,-", n3, n4))
                                                .add(toAmount(maps, "-,%d,%d,%d,-,-,-,-,-,-", n2, n3, n4))
                                                .add(toAmount(maps, "%d,-,%d,%d,-,-,-,-,-,-", n1, n3, n4))
                                                .add(toAmount(maps, "%d,%d,-,%d,-,-,-,-,-,-", n1, n2, n4))
                                                .add(toAmount(maps, "%d,%d,%d,%d,-,-,-,-,-,-", n1, n2, n3, n4))
                                                .add(amount3);
                                        if (getWinAndBet(amount4, betAmount).compareTo(ratio) <= 0) {
                                            List<Integer> n5List = getNums(ran);
                                            n5List.remove(n1);
                                            n5List.remove(n2);
                                            n5List.remove(n3);
                                            n5List.remove(n4);
                                            for (Integer n5 : n5List) {
                                                BigDecimal amount5 = toAmount(maps, "-,-,-,-,%d,-,-,-,-,-", new Object[]{n5})
                                                        .add(toAmount(maps, "%d,-,-,-,%d,-,-,-,-,-", n1, n5))
                                                        .add(toAmount(maps, "-,%d,-,-,%d,-,-,-,-,-", n2, n5))
                                                        .add(toAmount(maps, "-,-,%d,-,%d,-,-,-,-,-", n3, n5))
                                                        .add(toAmount(maps, "-,-,-,%d,%d,-,-,-,-,-", n4, n5))
                                                        .add(toAmount(maps, "%d,%d,-,-,%d,-,-,-,-,-", n1, n2, n5))
                                                        .add(toAmount(maps, "%d,-,%d,-,%d,-,-,-,-,-", n1, n3, n5))
                                                        .add(toAmount(maps, "%d,-,-,%d,%d,-,-,-,-,-", n1, n4, n5))
                                                        .add(toAmount(maps, "-,%d,%d,-,%d,-,-,-,-,-", n2, n3, n5))
                                                        .add(toAmount(maps, "-,%d,-,%d,%d,-,-,-,-,-", n2, n4, n5))
                                                        .add(toAmount(maps, "-,-,%d,%d,%d,-,-,-,-,-", n3, n4, n5))
                                                        .add(toAmount(maps, "-,%d,%d,%d,%d,-,-,-,-,-", n2, n3, n4, n5))
                                                        .add(toAmount(maps, "%d,-,%d,%d,%d,-,-,-,-,-", n1, n3, n4, n5))
                                                        .add(toAmount(maps, "%d,%d,-,%d,%d,-,-,-,-,-", n1, n2, n4, n5))
                                                        .add(toAmount(maps, "%d,%d,%d,-,%d,-,-,-,-,-", n1, n2, n3, n5))
                                                        .add(toAmount(maps, "%d,%d,%d,%d,%d,-,-,-,-,-", n1, n2, n3, n4, n5))
                                                        .add(amount4);
                                                if (getWinAndBet(amount5, betAmount).compareTo(ratio) <= 0) {
                                                    List<Integer> n6List = getNums(ran);
                                                    n6List.remove(n1);
                                                    n6List.remove(n2);
                                                    n6List.remove(n3);
                                                    n6List.remove(n4);
                                                    n6List.remove(n5);
                                                    for (Integer n6 : n6List) {
                                                        BigDecimal amount6 = toAmount(maps, "-,-,-,-,%d,%d,-,-,-,-", new Object[]{n5, n6})
                                                                .add(toAmount(maps, "-,-,-,-,-,%d,-,-,-,-", n6))
                                                                .add(amount5);
                                                        if (getWinAndBet(amount6, betAmount).compareTo(ratio) <= 0) {
                                                            List<Integer> n7List = getNums(ran);
                                                            n7List.remove(n1);
                                                            n7List.remove(n2);
                                                            n7List.remove(n3);
                                                            n7List.remove(n4);
                                                            n7List.remove(n5);
                                                            n7List.remove(n6);
                                                            for (Integer n7 : n7List) {
                                                                BigDecimal amount7 = toAmount(maps, "-,-,-,%d,-,-,%d,-,-,-",
                                                                        new Object[]{n4, n7}).add(
                                                                        toAmount(maps, "-,-,-,-,-,-,%d,-,-,-", n7))
                                                                        .add(amount6);
                                                                if (getWinAndBet(amount7, betAmount)
                                                                        .compareTo(ratio) <= 0) {
                                                                    List<Integer> n8List = getNums(ran);
                                                                    n8List.remove(n1);
                                                                    n8List.remove(n2);
                                                                    n8List.remove(n3);
                                                                    n8List.remove(n4);
                                                                    n8List.remove(n5);
                                                                    n8List.remove(n6);
                                                                    n8List.remove(n7);
                                                                    for (Integer n8 : n8List) {
                                                                        BigDecimal amount8 = toAmount(maps,
                                                                                "-,-,%d,-,-,-,-,%d,-,-", new Object[]{n3, n8})
                                                                                .add(toAmount(maps, "-,-,-,-,-,-,-,%d,-,-", n8))
                                                                                .add(amount7);
                                                                        if (getWinAndBet(amount8, betAmount)
                                                                                .compareTo(ratio) <= 0) {
                                                                            List<Integer> n9List = getNums(ran);
                                                                            n9List.remove(n1);
                                                                            n9List.remove(n2);
                                                                            n9List.remove(n3);
                                                                            n9List.remove(n4);
                                                                            n9List.remove(n5);
                                                                            n9List.remove(n6);
                                                                            n9List.remove(n7);
                                                                            n9List.remove(n8);
                                                                            for (Integer n9 : n9List) {
                                                                                BigDecimal amount9 = toAmount(maps,
                                                                                        "-,%d,-,-,-,-,-,-,%d,-", new Object[]{n2, n9})
                                                                                        .add(toAmount(maps, "-,-,-,-,-,-,-,-,%d,-", n9))
                                                                                        .add(amount8);
                                                                                if (getWinAndBet(amount9, betAmount).compareTo(ratio) <= 0) {
                                                                                    Integer n10 = n9List.get(0);
                                                                                    if (n10.equals(n9)) {
                                                                                        n10 = n9List.get(1);
                                                                                    }
                                                                                    BigDecimal amount10 = toAmount(maps,
                                                                                            "%d,-,-,-,-,-,-,-,-,%d", new Object[]{n1, n10})
                                                                                            .add(toAmount(maps, "-,-,-,-,-,-,-,-,-,%d", n10))
                                                                                            .add(amount9);

                                                                                    BigDecimal winAndBetAmount = getWinAndBet(
                                                                                            amount10, betAmount);
                                                                                    if (winAndBetAmount
                                                                                            .compareTo(ratio) <= 0) {
                                                                                        if ((lastWinAndBetAmount == null) || (
                                                                                                winAndBetAmount.compareTo(lastWinAndBetAmount) > 0)) {
                                                                                            lastWinAndBetAmount = winAndBetAmount;
                                                                                            lastWinAmount = amount10;
                                                                                            list = new ArrayList<>();
                                                                                            list.add(n1);
                                                                                            list.add(n2);
                                                                                            list.add(n3);
                                                                                            list.add(n4);
                                                                                            list.add(n5);
                                                                                            list.add(n6);
                                                                                            list.add(n7);
                                                                                            list.add(n8);
                                                                                            list.add(n9);
                                                                                            list.add(n10);
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
