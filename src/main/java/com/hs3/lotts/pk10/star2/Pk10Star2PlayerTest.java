package com.hs3.lotts.pk10.star2;

import com.hs3.lotts.PlayerBase;
import org.junit.Assert;
import org.junit.Test;

public class Pk10Star2PlayerTest {
    private PlayerBase p = new Pk10Star2Player();

    @Test
    public void testCount() {

        Integer count = this.p.getCount("0203,0506");
        Assert.assertEquals(1, count);

    }


}
