package com.hs3.lotts.ssc.star3.any;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar3AnyGroup6PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar3AnyGroup6Player();

    @Test
    public void testIfOpenWin() {
        String content = "万千百个|1,2,3,5,8";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
