package com.hs3.lotts.pk10.sides;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Pk10SideGyAndPlayerTest {
    private Pk10SideGyAndPlayer p = new Pk10SideGyAndPlayer();
    private List<Integer> nums = new ArrayList();
    private String bet = "大,单";

    @Before
    public void setUp() throws Exception {
        this.nums.add(Integer.valueOf(6));
        this.nums.add(Integer.valueOf(1));
        this.nums.add(Integer.valueOf(2));
        this.nums.add(Integer.valueOf(3));
        this.nums.add(Integer.valueOf(4));
        this.nums.add(Integer.valueOf(5));
        this.nums.add(Integer.valueOf(7));
        this.nums.add(Integer.valueOf(8));
        this.nums.add(Integer.valueOf(9));
        this.nums.add(Integer.valueOf(10));
    }

    @Test
    public void testGetCount() {
        int count = this.p.getCount(this.bet).intValue();
        Assert.assertEquals(count, 2L);
    }

    @Test
    public void testGetWin() {
        BigDecimal win = this.p.getWin(this.bet, this.nums);
        Assert.assertEquals(win, new BigDecimal("3.6"));
    }
}
