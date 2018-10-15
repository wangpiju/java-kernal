package com.hs3.lotts.ssc.star4.front.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;

import java.util.List;

import org.junit.Test;

public class SscStar4FrontGroup12PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar4FrontGroup12Player();

    @Test
    public void testIfOpenWin() {
        String content = "012,012";
        doTest(this.p, content);
    }

    @Test
    public void testGetCount() {
        String content = "0123,123";
        System.out.println("count: " + p.getCount(content));
    }


    protected String getKey(List<Integer> openNum) {
        return String.format("%d%d%d%d-", new Object[]{openNum.get(0), openNum.get(1), openNum.get(2), openNum.get(3)});
    }
}
