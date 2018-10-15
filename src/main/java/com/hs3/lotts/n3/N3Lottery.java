package com.hs3.lotts.n3;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryTrend;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.PlayerGroup;
import com.hs3.lotts.PlayerQun;
import com.hs3.lotts.n3.star1.N3Star1DwdPlayer;
import com.hs3.lotts.n3.star2.front.N3Star2FrontAndPlayer;
import com.hs3.lotts.n3.star2.front.N3Star2FrontKdPlayer;
import com.hs3.lotts.n3.star2.front.N3Star2FrontPlayer;
import com.hs3.lotts.n3.star2.front.N3Star2FrontSinglePlayer;
import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupAndPlayer;
import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupContainsPlayer;
import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupPlayer;
import com.hs3.lotts.n3.star2.front.group.N3Star2FrontGroupSinglePlayer;
import com.hs3.lotts.n3.star2.last.N3Star2LastAndPlayer;
import com.hs3.lotts.n3.star2.last.N3Star2LastKdPlayer;
import com.hs3.lotts.n3.star2.last.N3Star2LastPlayer;
import com.hs3.lotts.n3.star2.last.N3Star2LastSinglePlayer;
import com.hs3.lotts.n3.star2.last.group.N3Star2LastGroupAndPlayer;
import com.hs3.lotts.n3.star2.last.group.N3Star2LastGroupContainsPlayer;
import com.hs3.lotts.n3.star2.last.group.N3Star2LastGroupPlayer;
import com.hs3.lotts.n3.star2.last.group.N3Star2LastGroupSinglePlayer;
import com.hs3.lotts.n3.star3.N3Star3AndPlayer;
import com.hs3.lotts.n3.star3.N3Star3KdPlayer;
import com.hs3.lotts.n3.star3.N3Star3Player;
import com.hs3.lotts.n3.star3.N3Star3SinglePlayer;
import com.hs3.lotts.n3.star3.group.N3Star3Group36SinglePlayer;
import com.hs3.lotts.n3.star3.group.N3Star3Group3Player;
import com.hs3.lotts.n3.star3.group.N3Star3Group3SinglePlayer;
import com.hs3.lotts.n3.star3.group.N3Star3Group6Player;
import com.hs3.lotts.n3.star3.group.N3Star3Group6SinglePlayer;
import com.hs3.lotts.n3.star3.group.N3Star3GroupAndPlayer;
import com.hs3.lotts.n3.star3.group.N3Star3GroupContainsPlayer;
import com.hs3.lotts.n3.star3.none.N3Star3None1Player;
import com.hs3.lotts.n3.star3.none.N3Star3None2Player;
import com.hs3.lotts.n3.trend.N3Star3Trend;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class N3Lottery extends LotteryBase {
    public N3Lottery() {

        /**
         * jd-gui this.title = "低频"; this.groupId = "3d"; PlayerQun star1 = new
         * PlayerQun("定位胆"); PlayerQun star21 = new PlayerQun("前二"); PlayerQun star22 =
         * new PlayerQun("后二"); PlayerQun star3 = new PlayerQun("三星");
         *
         *
         * star3.addChild("直选") .addChild(new N3Star3Player()) .addChild(new
         * N3Star3SinglePlayer()) .addChild(new N3Star3AndPlayer()) .addChild(new
         * N3Star3KdPlayer());
         *
         *
         * star3.addChild("组选") .addChild(new N3Star3Group3Player()) .addChild(new
         * N3Star3Group6Player()) .addChild(new N3Star3Group3SinglePlayer())
         * .addChild(new N3Star3Group6SinglePlayer()) .addChild(new
         * N3Star3GroupAndPlayer()) .addChild(new N3Star3GroupContainsPlayer())
         * .addChild(new N3Star3Group36SinglePlayer());
         *
         * star3.addChild("不定位") .addChild(new N3Star3None1Player()) .addChild(new
         * N3Star3None2Player());
         *
         *
         * star21.addChild("直选") .addChild(new N3Star2FrontPlayer()) .addChild(new
         * N3Star2FrontSinglePlayer()) .addChild(new N3Star2FrontAndPlayer())
         * .addChild(new N3Star2FrontKdPlayer());
         *
         * star21.addChild("组选") .addChild(new N3Star2FrontGroupPlayer()) .addChild(new
         * N3Star2FrontGroupSinglePlayer()) .addChild(new N3Star2FrontGroupAndPlayer())
         * .addChild(new N3Star2FrontGroupContainsPlayer());
         *
         *
         * star22.addChild("直选") .addChild(new N3Star2LastPlayer()) .addChild(new
         * N3Star2LastSinglePlayer()) .addChild(new N3Star2LastAndPlayer())
         * .addChild(new N3Star2LastKdPlayer());
         *
         * star22.addChild("组选") .addChild(new N3Star2LastGroupPlayer()) .addChild(new
         * N3Star2LastGroupSinglePlayer()) .addChild(new N3Star2LastGroupAndPlayer())
         * .addChild(new N3Star2LastGroupContainsPlayer());
         *
         * star1.addChild("定位胆") .addChild(new N3Star1DwdPlayer());
         *
         * this.qun.add(star3); this.qun.add(star22); this.qun.add(star21);
         * this.qun.add(star1); Iterator localIterator2; for (Iterator localIterator1 =
         * this.qun.iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
         * PlayerQun q = (PlayerQun)localIterator1.next(); localIterator2 =
         * q.getGroups().iterator(); continue;PlayerGroup g =
         * (PlayerGroup)localIterator2.next(); for (PlayerBase p : g.getPlayers()) {
         * this.player.put(p.getId(), p); } }
         */

        this.title = "低频";
        this.groupId = "3d";
        PlayerQun star1 = new PlayerQun("定位胆");
        PlayerQun star21 = new PlayerQun("前二");
        PlayerQun star22 = new PlayerQun("后二");
        PlayerQun star3 = new PlayerQun("三星");
        star3.addChild("直选").addChild(new N3Star3Player()).addChild(new N3Star3SinglePlayer())
                .addChild(new N3Star3AndPlayer()).addChild(new N3Star3KdPlayer());
        star3.addChild("组选").addChild(new N3Star3Group3Player()).addChild(new N3Star3Group6Player())
                .addChild(new N3Star3Group3SinglePlayer()).addChild(new N3Star3Group6SinglePlayer())
                .addChild(new N3Star3GroupAndPlayer()).addChild(new N3Star3GroupContainsPlayer())
                .addChild(new N3Star3Group36SinglePlayer());
        star3.addChild("不定位").addChild(new N3Star3None1Player()).addChild(new N3Star3None2Player());
        star21.addChild("直选").addChild(new N3Star2FrontPlayer()).addChild(new N3Star2FrontSinglePlayer())
                .addChild(new N3Star2FrontAndPlayer()).addChild(new N3Star2FrontKdPlayer());
        star21.addChild("组选").addChild(new N3Star2FrontGroupPlayer()).addChild(new N3Star2FrontGroupSinglePlayer())
                .addChild(new N3Star2FrontGroupAndPlayer()).addChild(new N3Star2FrontGroupContainsPlayer());
        star22.addChild("直选").addChild(new N3Star2LastPlayer()).addChild(new N3Star2LastSinglePlayer())
                .addChild(new N3Star2LastAndPlayer()).addChild(new N3Star2LastKdPlayer());
        star22.addChild("组选").addChild(new N3Star2LastGroupPlayer()).addChild(new N3Star2LastGroupSinglePlayer())
                .addChild(new N3Star2LastGroupAndPlayer()).addChild(new N3Star2LastGroupContainsPlayer());
        star1.addChild("定位胆").addChild(new N3Star1DwdPlayer());
        this.qun.add(star3);
        this.qun.add(star22);
        this.qun.add(star21);
        this.qun.add(star1);
        for (Iterator iterator = qun.iterator(); iterator.hasNext(); ) {
            PlayerQun q = (PlayerQun) iterator.next();
            for (Iterator iterator1 = q.getGroups().iterator(); iterator1.hasNext(); ) {
                PlayerGroup g = (PlayerGroup) iterator1.next();
                PlayerBase p;
                for (Iterator iterator2 = g.getPlayers().iterator(); iterator2.hasNext(); player.put(p.getId(), p))
                    p = (PlayerBase) iterator2.next();

            }

        }

    }

    private String getStatusStr(Set<Integer> n) {
        if (n.size() == 1) {
            return "豹子";
        }
        if (n.size() == 2) {
            return "组三";
        }
        return "组六";
    }

    public List<String> getOpenStatus(LotterySeason season) {
        List<String> rel = new ArrayList();
        Set<Integer> list = new HashSet();

        list.add(season.getN1());
        list.add(season.getN2());
        list.add(season.getN3());
        rel.add("组选：" + getStatusStr(list));
        rel.add("");
        int and = season.getN1().intValue() + season.getN2().intValue() + season.getN3().intValue();
        rel.add("和值：" + and);
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
        return new N3Star3Trend();
    }

    public String getNumList() {
        return "0,1,2,3,4,5,6,7,8,9";
    }

    public boolean checkNumber(String num) {
        List<Integer> lst = ListUtils.toIntList(num);
        if (lst.size() != 3) {
            return false;
        }
        for (Integer i : lst) {
            if ((i.intValue() < 0) || (i.intValue() > 9)) {
                return false;
            }
        }
        return true;
    }
}
