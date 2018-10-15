package com.hs3.lotts.k3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryTrend;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.PlayerGroup;
import com.hs3.lotts.PlayerQun;
import com.hs3.lotts.k3.star1.K3Star1Player;
import com.hs3.lotts.k3.star2.K3Star2SameNotPlayer;
import com.hs3.lotts.k3.star2.K3Star2SamePlayer;
import com.hs3.lotts.k3.star3.K3Star3AndPlayer;
import com.hs3.lotts.k3.star3.K3Star3BigOddPlayer;
import com.hs3.lotts.k3.star3.K3Star3LinkPlayer;
import com.hs3.lotts.k3.star3.K3Star3SameNotPlayer;
import com.hs3.lotts.k3.star3.K3Star3SamePlayer;
import com.hs3.lotts.k3.trend.K3Star3Trend;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

public class K3Lottery extends LotteryBase {
    public K3Lottery() {
        this.title = "快3";
        this.groupId = "k3";
        PlayerQun star1 = new PlayerQun("单骰");
        PlayerQun star2 = new PlayerQun("二骰");
        PlayerQun star3 = new PlayerQun("三骰");

        star1.addChild("单骰").addChild(new K3Star1Player());

        star2.addChild("二骰").addChild(new K3Star2SameNotPlayer()).addChild(new K3Star2SamePlayer());

        star3.addChild("三骰").addChild(new K3Star3AndPlayer()).addChild(new K3Star3LinkPlayer())
                .addChild(new K3Star3SameNotPlayer()).addChild(new K3Star3SamePlayer())
                .addChild(new K3Star3BigOddPlayer());

        this.qun.add(star1);
        this.qun.add(star2);
        this.qun.add(star3);

        /**
         * jd-gui Iterator localIterator2; for (Iterator localIterator1 =
         * this.qun.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
         * PlayerQun q = (PlayerQun)localIterator1.next(); localIterator2 =
         * q.getGroups().iterator(); continue;PlayerGroup g =
         * (PlayerGroup)localIterator2.next(); for (PlayerBase p : g.getPlayers()) {
         * this.player.put(p.getId(), p); } }
         */

        for (PlayerQun q :qun) {
            for (PlayerGroup g: q.getGroups() ) {
                for (PlayerBase p:g.getPlayers() ){
                    player.put(p.getId(), p);
                }
            }
        }
    }

    private String getStatusStr(Set<Integer> n) {
        if (n.size() == 1) {
            return "三同号";
        }
        if (n.size() == 2) {
            return "二不同";
        }
        return "三不同";
    }

    public List<String> getOpenStatus(LotterySeason season) {
        List<String> rel = new ArrayList<>();
        Set<Integer> list = new HashSet<>();

        list.add(season.getN1());
        list.add(season.getN2());
        list.add(season.getN3());
        rel.add(getStatusStr(list));

        return rel;
    }

    public SeasonOpen getSeasonOpen(LotterySeason season) {
        SeasonOpen rel = new SeasonOpen();
        rel.setSeasonId(season.getSeasonId());
        rel.setOpenTime(season.getOpenTime());
        rel.getNums().add(StrUtils.parseLength(season.getN1(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN2(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN3(), getNumLen()));
        return rel;
    }

    public int getOpenCount() {
        return 3;
    }

    public LotteryTrend getTrend() {
        return new K3Star3Trend();
    }

    public String getNumList() {
        return "1,2,3,4,5,6,大,小,单,双";
    }

    public boolean checkNumber(String num) {
        List<Integer> lst = ListUtils.toIntList(num);
        if (lst.size() != 3) {
            return false;
        }
        for (Integer i : lst) {
            if (1 > i || i > 6) {
                return false;
            }
        }
        return true;
    }
}
