package com.hs3.lotts.ssc.star1;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SscStar1DwdPlayerTest {
    private List<Integer> a = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(6), Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(9)});
    private SscStar1DwdPlayer p = new SscStar1DwdPlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testGetCount() {
        int c1 = this.p.getCount("0123456,-,025896,-,-").intValue();
        Assert.assertEquals(13L, c1);
    }

    @Test
    public void testIfOpenWin() {
        int c1 = this.p.ifOpenWin("0123456,-,025896,-,-").size();
        Assert.assertEquals(13L, c1);
    }

    @Test
    public void testGetWin() {
        BigDecimal c1 = this.p.getWin("0123456,-,025896,-,-", this.a);
        Assert.assertEquals(c1, new BigDecimal("40"));
    }
}
