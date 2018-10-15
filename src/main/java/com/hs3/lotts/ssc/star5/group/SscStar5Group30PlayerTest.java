package com.hs3.lotts.ssc.star5.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar5Group30PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar5Group30Player();

    @Test
    public void testIfOpenWin() {
        String content = "01,234";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
