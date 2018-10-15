package com.hs3.lotts.pk10.star1;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Pk10Star1DwdPlayerTest {
    private List<Integer> a = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(6), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(7), Integer.valueOf(10)});
    private Pk10Star1DwdPlayer p = new Pk10Star1DwdPlayer();

    @Test
    public void testGetCount() {
        Assert.assertEquals(this.p.getCount("0102,-,-,-,-").intValue(), 2L);
    }

    @Test
    public void testGetWin() {
        Assert.assertEquals(this.p.getWin("0102,-,-,-,-", this.a), new BigDecimal("20"));
        Assert.assertEquals(this.p.getWin("0102,0102,-,-,-", this.a), new BigDecimal("40"));
    }
}
