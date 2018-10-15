package com.hs3.lotts.ssc.star3.any;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar3AnyPlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar3AnyPlayer();

    @Test
    public void testIfOpenWin() {
        String content = "123456,-,15976,123456,789456";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
