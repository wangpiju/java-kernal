package com.hs3.lotts.ssc.star2.front.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar2FrontGroupContainsPlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2FrontGroupContainsPlayer();

    @Test
    public void testIfOpenWin() {
        doTest(this.p, "0");
        doTest(this.p, "1");
        doTest(this.p, "2");
        doTest(this.p, "3");
        doTest(this.p, "4");
        doTest(this.p, "5");
        doTest(this.p, "6");
        doTest(this.p, "7");
        doTest(this.p, "8");
        doTest(this.p, "9");
    }

    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d---", new Object[]{openNum.get(0), openNum.get(1)});
    }
}
