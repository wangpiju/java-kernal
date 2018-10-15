package com.hs3.lotts.n11x5;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.*;
import com.hs3.lotts.n11x5.nx.*;
import com.hs3.lotts.n11x5.trend.N11x5Star5Trend;
import com.hs3.lotts.n11x5.x1.N11x5DwdPlayer;
import com.hs3.lotts.n11x5.x1.N11x5X1Player;
import com.hs3.lotts.n11x5.x1.N11x5X1SinglePlayer;
import com.hs3.lotts.n11x5.x2.N11x5Star2FrontPlayer;
import com.hs3.lotts.n11x5.x2.N11x5Star2FrontSinglePlayer;
import com.hs3.lotts.n11x5.x2.group.N11x5Star2GroupDtPlayer;
import com.hs3.lotts.n11x5.x2.group.N11x5Star2GroupPlayer;
import com.hs3.lotts.n11x5.x2.group.N11x5Star2GroupSinglePlayer;
import com.hs3.lotts.n11x5.x2.nx.N11x5X2DtPlayer;
import com.hs3.lotts.n11x5.x2.nx.N11x5X2Player;
import com.hs3.lotts.n11x5.x2.nx.N11x5X2SinglePlayer;
import com.hs3.lotts.n11x5.x3.N11x5Front3Nx1Player;
import com.hs3.lotts.n11x5.x3.N11x5Star3FrontPlayer;
import com.hs3.lotts.n11x5.x3.N11x5Star3FrontSinglePlayer;
import com.hs3.lotts.n11x5.x3.and.*;
import com.hs3.lotts.n11x5.x3.group.N11x5Star3GroupDtPlayer;
import com.hs3.lotts.n11x5.x3.group.N11x5Star3GroupPlayer;
import com.hs3.lotts.n11x5.x3.group.N11x5Star3GroupSinglePlayer;
import com.hs3.lotts.n11x5.x3.nx.N11x5X3DtPlayer;
import com.hs3.lotts.n11x5.x3.nx.N11x5X3Player;
import com.hs3.lotts.n11x5.x3.nx.N11x5X3SinglePlayer;
import com.hs3.lotts.n11x5.x4.N11x5X4DtPlayer;
import com.hs3.lotts.n11x5.x4.N11x5X4Player;
import com.hs3.lotts.n11x5.x4.N11x5X4SinglePlayer;
import com.hs3.lotts.n11x5.x5.N11x5X5DtPlayer;
import com.hs3.lotts.n11x5.x5.N11x5X5Player;
import com.hs3.lotts.n11x5.x5.N11x5X5SinglePlayer;
import com.hs3.lotts.n11x5.x6.N11x5X6DtPlayer;
import com.hs3.lotts.n11x5.x6.N11x5X6Player;
import com.hs3.lotts.n11x5.x6.N11x5X6SinglePlayer;
import com.hs3.lotts.n11x5.x7.N11x5X7DtPlayer;
import com.hs3.lotts.n11x5.x7.N11x5X7Player;
import com.hs3.lotts.n11x5.x7.N11x5X7SinglePlayer;
import com.hs3.lotts.n11x5.x8.N11x5X8DtPlayer;
import com.hs3.lotts.n11x5.x8.N11x5X8Player;
import com.hs3.lotts.n11x5.x8.N11x5X8SinglePlayer;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class N11x5Lottery extends LotteryBase {
    public N11x5Lottery() {
        this.title = "11选5";
        this.groupId = "11x5";
        PlayerQun nx1 = new PlayerQun("选一");
        PlayerQun nx2 = new PlayerQun("选二");
        PlayerQun nx3 = new PlayerQun("选三");
        PlayerQun nx4 = new PlayerQun("选四");
        PlayerQun nx5 = new PlayerQun("选五");
        PlayerQun nx6 = new PlayerQun("选六");
        PlayerQun nx7 = new PlayerQun("选七");
        PlayerQun nx8 = new PlayerQun("选八");
        PlayerQun dragon = new PlayerQun("龙虎斗");
        PlayerQun fun = new PlayerQun("趣味");

        nx1.addChild("任选一中一").addChild(new N11x5X1Player()).addChild(new N11x5X1SinglePlayer());

        nx1.addChild("定位胆").addChild(new N11x5DwdPlayer());

        nx1.addChild("不定位").addChild(new N11x5Front3Nx1Player());

        nx2.addChild("任选二中二").addChild(new N11x5X2Player()).addChild(new N11x5X2SinglePlayer())
                .addChild(new N11x5X2DtPlayer());

        nx2.addChild("直选").addChild(new N11x5Star2FrontPlayer()).addChild(new N11x5Star2FrontSinglePlayer());

        nx2.addChild("组选").addChild(new N11x5Star2GroupPlayer()).addChild(new N11x5Star2GroupSinglePlayer())
                .addChild(new N11x5Star2GroupDtPlayer());

        nx3.addChild("任选三中三").addChild(new N11x5X3Player()).addChild(new N11x5X3SinglePlayer())
                .addChild(new N11x5X3DtPlayer());

        nx3.addChild("直选").addChild(new N11x5Star3FrontPlayer()).addChild(new N11x5Star3FrontSinglePlayer());

        nx3.addChild("组选").addChild(new N11x5Star3GroupPlayer()).addChild(new N11x5Star3GroupSinglePlayer())
                .addChild(new N11x5Star3GroupDtPlayer());
        dragon.addChild("龙虎").addChild(new N11x5DragonLionPlayer());

        nx3.addChild("和值").addChild(new N11x5Star3AndPlayer()).addChild(new N11x5Star3BigPlayer()).addChild(new N11x5Star3SmallPlayer())
                .addChild(new N11x5Star3OddPlayer()).addChild(new N11x5Star3EvenPlayer());

        nx4.addChild("任选四中四").addChild(new N11x5X4Player()).addChild(new N11x5X4SinglePlayer())
                .addChild(new N11x5X4DtPlayer());

        nx5.addChild("任选五中五").addChild(new N11x5X5Player()).addChild(new N11x5X5SinglePlayer())
                .addChild(new N11x5X5DtPlayer());

        nx6.addChild("任选六中五").addChild(new N11x5X6Player()).addChild(new N11x5X6SinglePlayer())
                .addChild(new N11x5X6DtPlayer());

        nx7.addChild("任选七中五").addChild(new N11x5X7Player()).addChild(new N11x5X7SinglePlayer())
                .addChild(new N11x5X7DtPlayer());

        nx8.addChild("任选八中五").addChild(new N11x5X8Player()).addChild(new N11x5X8SinglePlayer())
                .addChild(new N11x5X8DtPlayer());

        fun.addChild("趣味").addChild(new N11x5CowCowPlayer()).addChild(new N11x5OddEvenCountPlayer())
                .addChild(new N11x5GuessMiddlePlayer()).addChild(new N11x5GuessNoWinPlayer());

        this.qun.add(nx1);

        this.qun.add(nx2);
        this.qun.add(nx3);

        this.qun.add(nx4);
        this.qun.add(nx5);
        this.qun.add(nx6);

        this.qun.add(nx7);
        this.qun.add(nx8);
        this.qun.add(dragon);
        this.qun.add(fun);


        for (PlayerQun q : qun) {
            for (PlayerGroup g : q.getGroups()) {
                PlayerBase p;
                for (Iterator iterator2 = g.getPlayers().iterator(); iterator2.hasNext(); player.put(p.getId(), p))
                    p = (PlayerBase) iterator2.next();

            }

        }

    }

    protected int getNumLen() {
        return 2;
    }

    public List<String> getOpenStatus(LotterySeason season) {
        List<String> rel = new ArrayList<>();
        int single = 0;
        int doubleNum = 0;

        List<Integer> nums = new ArrayList<>();
        nums.add(season.getN1());
        nums.add(season.getN2());
        nums.add(season.getN3());
        nums.add(season.getN4());
        nums.add(season.getN5());
        for (Integer n : nums) {
            if (n % 2 != 0) {
                single++;
            } else {
                doubleNum++;
            }
        }
        Collections.sort(nums);
        int mid = nums.get(2);
        String m;
        if (mid < 10) {
            m = (new StringBuilder("0")).append(mid).toString();
        } else {
            m = (new StringBuilder()).append(mid).toString();
        }
        rel.add("单双：" + single + "单" + doubleNum + "双");
        rel.add("");
        rel.add("中位：" + m);
        return rel;
    }

    public SeasonOpen getSeasonOpen(LotterySeason season) {
        SeasonOpen rel = new SeasonOpen();
        rel.setSeasonId(season.getSeasonId());
        rel.setOpenTime(season.getOpenTime());
        rel.getNums().add(StrUtils.parseLength(season.getN1(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN2(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN3(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN4(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN5(), getNumLen()));
        return rel;
    }

    public int getOpenCount() {
        return 5;
    }

    public LotteryTrend getTrend() {
        return new N11x5Star5Trend();
    }

    public String getNumList() {
        return "01,02,03,04,05,06,07,08,09,10,11";
    }

    public boolean checkNumber(String num) {
        List<Integer> lst = ListUtils.toIntList(num);
        if (lst.size() != 5) {
            return false;
        }
        for (Integer i : lst) {
            if ((1 > i) || (i > 11)) {
                return false;
            }
        }
        return !ListUtils.hasSame(lst);
    }
}
