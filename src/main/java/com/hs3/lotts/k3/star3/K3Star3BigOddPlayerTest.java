package com.hs3.lotts.k3.star3;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hs3.lotts.PlayerBase;

public class K3Star3BigOddPlayerTest {
    private PlayerBase p = new K3Star3BigOddPlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testCount() {
        Integer count = this.p.getCount("大");
        Assert.assertEquals(1, count);
    }

    @Test
    public void testGetWin() {
        try {
            BigDecimal bonus = this.p.getWin("大", Arrays.asList(2, 2, 2));
            System.out.println("bonus: " + bonus);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
