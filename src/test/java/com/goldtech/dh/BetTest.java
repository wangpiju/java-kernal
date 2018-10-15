package com.goldtech.dh;

import com.alibaba.fastjson.JSON;
import com.hs3.lotts.NumberView;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.k3.star3.K3Star3AndPlayer;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-20 11:22
 **/
public class BetTest {

    @Test
    public void k3() {
        List<Integer> openNums = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            openNums.add(new Random().nextInt(6) + 1);
        }
        System.out.println(openNums.toString());
        PlayerBase pl = new K3Star3AndPlayer();
//        System.out.println(JSON.toJSONString(pl.getNumView()));
        String betNums = "大";
        BigDecimal win = pl.getWinPlus(betNums, openNums);
        System.out.println(win);
    }
}
