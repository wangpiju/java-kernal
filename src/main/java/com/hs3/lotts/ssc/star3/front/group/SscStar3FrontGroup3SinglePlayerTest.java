package com.hs3.lotts.ssc.star3.front.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar3FrontGroup3SinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar3FrontGroup3SinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "01 12 13 01 10";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d%d--", new Object[]{openNum.get(0), openNum.get(1), openNum.get(2)});
    }
}
