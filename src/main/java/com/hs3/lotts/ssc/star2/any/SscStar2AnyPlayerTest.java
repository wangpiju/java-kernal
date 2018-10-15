package com.hs3.lotts.ssc.star2.any;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar2AnyPlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar2AnyPlayer();

    @Test
    public void testIfOpenWin() {
        String content = "123,-,-,789,123";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
