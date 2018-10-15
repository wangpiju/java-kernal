package com.hs3.lotts.open;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.service.lotts.LotteryLoseWinService;
import com.hs3.utils.NumUtils;
import com.hs3.web.utils.SpringContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class BaseNumberBuilder implements INumberBuilder {
    private LotteryLoseWinService lotteryLoseWinService;

    protected LotteryLoseWinService getService() {
        if (this.lotteryLoseWinService == null) {
            this.lotteryLoseWinService = ((LotteryLoseWinService) SpringContext.getBean("lotteryLoseWinService"));
        }
        return this.lotteryLoseWinService;
    }

    public LotterySeason create(String lotteryId, String seasonId) {
        Random ran = NumUtils.getRandomInstance();
        return create(lotteryId, seasonId, null, null, ran);
    }

    public LotterySeason create(String lotteryId, String seasonId, Random ran) {
        return create(lotteryId, seasonId, null, null, ran);
    }

    public LotterySeason create(String lotteryId, String seasonId, BigDecimal ratio, BigDecimal deviation) {
        Random ran = NumUtils.getRandomInstance();
        return create(lotteryId, seasonId, ratio, deviation, ran);
    }

    protected abstract List<Integer> getList();

    protected List<Integer> getNums(Random ran) {
        return getNums(null, ran);
    }

    protected List<Integer> getNums(List<Integer> nums, Random ran) {
        if (nums == null) {
            nums = getList();
        }
        int times = NumUtils.getRandom(ran, 50, 100);
        for (int j = 0; j < 10 + times; j++) {
            int n = NumUtils.getRandom(ran, 0, nums.size() - 1);
            nums.add(0, (Integer) nums.remove(n));
        }
        return nums;
    }

    protected BigDecimal getWinAndBet(BigDecimal winAmount, BigDecimal betAmount) {
        return winAmount.divide(betAmount, 2, 1);
    }

    protected BigDecimal toAmount(Object v) {
        BigDecimal a = null;
        if (v != null) {
            a = new BigDecimal(v.toString());
        } else {
            a = BigDecimal.ZERO;
        }
        return a;
    }

    protected BigDecimal toAmount(Map<String, String> map, String format, Object... num) {
        String k = String.format(format, num);
        String v = map.get(k);
        BigDecimal a = null;
        if (v != null) {
            a = new BigDecimal(v);
        } else {
            a = BigDecimal.ZERO;
        }
        return a;
    }
}
