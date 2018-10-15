package com.hs3.lotts.ssc.sides;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SscSideLhhPlayerTest {
    private SscSideLhhPlayer p = new SscSideLhhPlayer();
    private List<Integer> nums = new ArrayList();
    private String bet = "[万千]龙,[万百]虎,-,-,-,-,-,-,-,[十个]和";

    @Before
    public void setUp()
            throws Exception {
        this.nums.add(Integer.valueOf(6));
        this.nums.add(Integer.valueOf(1));
        this.nums.add(Integer.valueOf(2));
        this.nums.add(Integer.valueOf(4));
        this.nums.add(Integer.valueOf(4));
    }

    @Test
    public void testGetCount() {
        int count = this.p.getCount(this.bet).intValue();
        Assert.assertEquals(count, 3L);
    }

    @Test
    public void testGetWin() {
        BigDecimal win = this.p.getWin(this.bet, this.nums);
        Assert.assertEquals(win, new BigDecimal("24.44"));
    }

    @Test
    public void testIfOpenWin() {
        Map<String, BigDecimal> res = this.p.ifOpenWin(this.bet);
        for (String k : res.keySet()) {
            System.out.println(k + ":" + res.get(k));
        }
        Assert.assertEquals(100L, res.size());
    }
}
