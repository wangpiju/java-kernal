package com.hs3.lotts.k3.star2;

import com.hs3.lotts.PlayerBase;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class K3Star2SameNotPlayerTest {
    private PlayerBase p = new K3Star2SameNotPlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testCount() {
        Integer count = this.p.getCount("12");
        Assert.assertEquals(1, count);

    }

    @Test
    public void testGetWin() {
        try {
            BigDecimal bonus = this.p.getWin("2", Arrays.asList(2, 2, 2));
            System.out.println("bonus: " + bonus);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testGetWinIfOpenWin() {
        Map<String, BigDecimal> map = this.p.ifOpenWin("12");
        System.out.println(map.toString());
    }
}
