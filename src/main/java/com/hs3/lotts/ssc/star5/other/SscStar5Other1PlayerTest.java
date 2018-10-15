package com.hs3.lotts.ssc.star5.other;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar5Other1PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar5Other1Player();

    @Test
    public void testIfOpenWin() {
        String content = "0,1,2,3,4,5,6,7";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
