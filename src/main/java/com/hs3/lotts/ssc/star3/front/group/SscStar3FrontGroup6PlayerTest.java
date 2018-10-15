package com.hs3.lotts.ssc.star3.front.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar3FrontGroup6PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar3FrontGroup6Player();

    @Test
    public void testIfOpenWin() {
        String content = "0,1,2,3,4,5";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d%d--", new Object[]{openNum.get(0), openNum.get(1), openNum.get(2)});
    }
}
