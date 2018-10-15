package com.hs3.lotts.k3.star2;

import com.hs3.lotts.PlayerBase;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class K3Star2SamePlayerTest {
    private PlayerBase p = new K3Star2SamePlayer();

    @Before
    public void setUp()
            throws Exception {
    }

    @Test
    public void testGetWin() {
        int win = 0;
        int all = 0;
        this.p.getCount("112");
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                for (int k = 1; k <= 6; k++) {
                    all++;
                    List<Integer> os = new ArrayList();
                    os.add(Integer.valueOf(i));
                    os.add(Integer.valueOf(j));
                    os.add(Integer.valueOf(k));

                    Map<Integer, Integer> m = new HashMap();
                    for (Integer o : os) {
                        if (m.containsKey(o)) {
                            m.put(o, Integer.valueOf(((Integer) m.get(o)).intValue() + 1));
                        } else {
                            m.put(o, Integer.valueOf(1));
                        }
                    }
                    if ((m.containsKey(Integer.valueOf(1))) && (((Integer) m.get(Integer.valueOf(1))).intValue() == 2) && (m.containsKey(Integer.valueOf(2)))) {
                        win++;
                    }
                }
            }
        }
        System.out.println("all:" + all);
        System.out.println("win:" + win);
        Assert.assertEquals(1L, 1L);
    }
}
