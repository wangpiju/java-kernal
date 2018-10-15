package com.hs3.lotts.k3.star1;

import com.hs3.lotts.PlayerBase;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class K3Star1PlayerTest {
    private PlayerBase p = new K3Star1Player();

    @Test
    public void testCount() {

        Integer count = this.p.getCount("2");
        Assert.assertEquals(1, count);

    }

    @Test
    public void testGetWin() {
        try {
            BigDecimal bonus = this.p.getWin("1,2,3,4,5,6", Arrays.asList(1, 2, 2));
            System.out.println("bonus: " + String.valueOf(bonus.multiply(new BigDecimal(0.85))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGetWinIfOpenWin() {
        Map<String, BigDecimal> map = this.p.ifOpenWin("2,2,2");
        System.out.println(map.toString());
    }
}
