package com.hs3.lotts.k3.star3;

import com.hs3.lotts.PlayerBase;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class K3Star3AndPlayerTest {
    private PlayerBase p = new K3Star3AndPlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testGetWin() {
        List<Integer> openNums = new ArrayList<>();
        openNums.add(1);
        openNums.add(2);
        openNums.add(3);
        BigDecimal win = this.p.getWin("04,05,06,07,08,09", openNums);
        System.out.println(win);

    }
}
