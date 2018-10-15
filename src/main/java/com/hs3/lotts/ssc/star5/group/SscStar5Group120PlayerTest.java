package com.hs3.lotts.ssc.star5.group;

import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.ssc.SscBaseTest;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.junit.Test;

public class SscStar5Group120PlayerTest
        extends SscBaseTest {
    private PlayerBase p = new SscStar5Group120Player();

    @Test
    public void testIfOpenWin() {
//    String content = "0,1,2,3,4,5,6,7";
//    doTest(this.p, content);
    }

    protected String getKey(List<Integer> openNum) {
        return ListUtils.toString(openNum, "");
    }

    @Test
    public void testGetCount() {
        String content = "1,2,3,4,5";
        System.out.println(p.getCount(content));

    }

}
