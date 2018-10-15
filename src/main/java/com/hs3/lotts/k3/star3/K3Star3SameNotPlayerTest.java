package com.hs3.lotts.k3.star3;

import com.hs3.lotts.PlayerBase;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class K3Star3SameNotPlayerTest {
    private PlayerBase p = new K3Star3SameNotPlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testGetWin() {
        try {
            BigDecimal bonus = this.p.getWin("123", Arrays.asList(1, 2, 3));
            System.out.println("bonus: " + bonus);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
