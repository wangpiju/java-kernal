package com.hs3.lotts.ssc.star2.any;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar2AnyGroupPlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2AnyGroupPlayer();

    @Test
    public void testIfOpenWin() {
        String content = "万千百|1,2,3,4,5,6,7,8,9";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
