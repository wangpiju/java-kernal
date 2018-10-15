package com.hs3.lotts.pk10.sides;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Pk10SideDsPlayerTest {
    private Pk10SideDsPlayer p = new Pk10SideDsPlayer();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetCount() {
        int i = this.p.getCount("大,小,-,双小,-,-,-,-,-,-").intValue();
        Assert.assertEquals(i, 4L);
    }

    @Test
    public void testGetWin() {
        List<Integer> nums = new ArrayList();
        nums.add(Integer.valueOf(6));
        nums.add(Integer.valueOf(1));
        nums.add(Integer.valueOf(2));
        nums.add(Integer.valueOf(3));
        nums.add(Integer.valueOf(4));
        nums.add(Integer.valueOf(5));
        nums.add(Integer.valueOf(7));
        nums.add(Integer.valueOf(8));
        nums.add(Integer.valueOf(9));
        nums.add(Integer.valueOf(10));
        BigDecimal win = this.p.getWin("大,小,-,双小,-,-,-,-,-,-", nums);

        Assert.assertEquals(win, new BigDecimal("12"));
    }
}
