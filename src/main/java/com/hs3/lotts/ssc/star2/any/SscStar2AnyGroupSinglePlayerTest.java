package com.hs3.lotts.ssc.star2.any;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar2AnyGroupSinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2AnyGroupSinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "万千百|12 13 14 13 21 15 14 98";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
