package com.hs3.lotts.ssc.star4.front;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar4FrontSinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar4FrontSinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "0134 1523 4523 4252";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d%d%d-", new Object[]{openNum.get(0), openNum.get(1), openNum.get(2), openNum.get(3)});
    }
}
