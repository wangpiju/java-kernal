package com.hs3.lotts;

import com.hs3.lotts.k3.K3Lottery;
import com.hs3.lotts.n11x5.N11x5Lottery;
import com.hs3.lotts.n3.N3Lottery;
import com.hs3.lotts.pk10.Pk10Lottery;
import com.hs3.lotts.ssc.SscLottery;

import java.util.HashMap;
import java.util.Set;

public class LotteryFactory {
    private static HashMap<String, Class<? extends LotteryBase>> lotts = new HashMap();

    static {
        lotts.put("时时彩", SscLottery.class);
        lotts.put("11选5", N11x5Lottery.class);
        lotts.put("低频", N3Lottery.class);
        lotts.put("北京赛车", Pk10Lottery.class);
        lotts.put("快3", K3Lottery.class);
    }

    public static LotteryBase getInstance(String groupName) {
        if (lotts.containsKey(groupName)) {
            try {
                return (LotteryBase) ((Class) lotts.get(groupName)).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Set<String> getGroups() {
        return lotts.keySet();
    }
}
