package com.hs3.lotts.pk10.star3;

import com.hs3.lotts.PlayerBase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pk10Star3PlayerTest {
    private PlayerBase p = new Pk10Star3DjPlayer();

    @Test
    public void testCount() {

        Integer count = this.p.getCount("010203,010203,010203");
        System.out.println(count);

    }

    @Test
    public void testGetWin() {
        List<Integer> openList = new ArrayList<>();
        openList.add(1);
        openList.add(2);
        openList.add(3);
        openList.add(4);
        openList.add(5);
        openList.add(6);
        openList.add(7);
        openList.add(8);
        openList.add(9);
        openList.add(10);
        BigDecimal win = this.p.getWin("010203,010203,010203", openList);
        System.out.println(win);

    }


}
