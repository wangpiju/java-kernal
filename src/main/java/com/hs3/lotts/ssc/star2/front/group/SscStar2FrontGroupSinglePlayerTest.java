package com.hs3.lotts.ssc.star2.front.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar2FrontGroupSinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2FrontGroupSinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "12 21 13 31 12";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d---", new Object[]{openNum.get(0), openNum.get(1)});
    }
}
