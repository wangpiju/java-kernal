package com.hs3.lotts.ssc;

import com.hs3.lotts.LotteryUtils;
import com.hs3.lotts.PlayerBase;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class SscBaseTest {
    protected void doTest(PlayerBase p, String content) {
        int count = p.getCount(content).intValue();
        Assert.assertEquals(Boolean.valueOf(count > 0), Boolean.valueOf(true));
        long a1 = System.currentTimeMillis();
        Map<String, BigDecimal> m = p.ifOpenWin(content);
        System.out.println(System.currentTimeMillis() - a1);

        List<Integer> openNum = new ArrayList(Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)}));
        do {
            BigDecimal win = p.getWin(content, openNum);
            String key = getKey(openNum);
            toTest(m, win, key);
        } while (LotteryUtils.listHasNext(openNum, 5, 0, 9));
    }

    protected abstract String getKey(List<Integer> paramList);

    protected void toTest(Map<String, BigDecimal> m, BigDecimal win, String key) {
        if (m.containsKey(key)) {
            Assert.assertEquals(((BigDecimal) m.get(key)).compareTo(win), 0L);
        } else {
            Assert.assertEquals(BigDecimal.ZERO.compareTo(win), 0L);
        }
    }
}
