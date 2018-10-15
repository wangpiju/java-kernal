package com.hs3.lotts.ssc.star2.front;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar2FrontSinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2FrontSinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "12 34 51 15";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d---", new Object[]{openNum.get(0), openNum.get(1)});
    }
}
