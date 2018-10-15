package com.hs3.lotts.ssc.star5;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar5SinglePlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar5SinglePlayer();

    @Test
    public void testIfOpenWin() {
        String content = "01234 56078 02135 01234";
        doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
