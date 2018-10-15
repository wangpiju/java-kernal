package com.hs3.lotts.pk10.star1;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Pk10Star1DwdLastPlayerTest {
    private List<Integer> a = Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(6), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(4), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(7), Integer.valueOf(10)});
    private Pk10Star1DwdLastPlayer p = new Pk10Star1DwdLastPlayer();

    @Test
    public void testGetCount() {
        Assert.assertEquals(this.p.getCount("0102,-,-,-,-").intValue(), 2L);
        Assert.assertEquals(this.p.getCount("0102,-,-,-,0809").intValue(), 4L);
    }

    @Test
    public void testGetWin() {
        Assert.assertEquals(this.p.getWin("0102,-,-,-,-", this.a), new BigDecimal("0"));
        Assert.assertEquals(this.p.getWin("0104,0802,-,-,-", this.a), new BigDecimal("40"));
        Assert.assertEquals(this.p.getWin("04,-,-,-,-", this.a), new BigDecimal("20"));
    }
}
