package com.hs3.lotts.pk10.star2;

import com.hs3.lotts.PlayerBase;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pk10Star2DjPlayerTest {
    private PlayerBase p = new Pk10Star2DjPlayer();

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
        BigDecimal win = this.p.getWin("010203,040506", openList);
        System.out.println(win);

    }

}
