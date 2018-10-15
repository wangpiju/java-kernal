package com.hs3.lotts.ssc.star4.front.none;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar4FrontNone2PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar4FrontNone2Player();

    @Test
    public void testIfOpenWin() {
        String content = "0,1";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d%d%d-", new Object[]{openNum.get(0), openNum.get(1), openNum.get(2), openNum.get(3)});
    }
}
