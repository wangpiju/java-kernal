package com.hs3.lotts.ssc.star5;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar5PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar5Player();

    @Test
    public void testgetCount() {
        String content = "0123,123,245,3456,4567";
        System.out.println(p.getCount("0,14,235,3,2"));
    }

    @Test
    public void testIfOpenWin() {
        String content = "0123,123,245,3456,4567";
        //doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }
}
