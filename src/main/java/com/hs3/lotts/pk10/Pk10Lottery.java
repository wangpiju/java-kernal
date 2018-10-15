package com.hs3.lotts.pk10;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryTrend;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.PlayerGroup;
import com.hs3.lotts.PlayerQun;
import com.hs3.lotts.pk10.sides.Pk10SideDsPlayer;
import com.hs3.lotts.pk10.sides.Pk10SideGyAndPlayer;
import com.hs3.lotts.pk10.sides.Pk10SideLhPlayer;
import com.hs3.lotts.pk10.star1.Pk10Star1DwdLastPlayer;
import com.hs3.lotts.pk10.star1.Pk10Star1DwdPlayer;
import com.hs3.lotts.pk10.star2.Pk10Star2AndPlayer;
import com.hs3.lotts.pk10.star2.Pk10Star2DjPlayer;
import com.hs3.lotts.pk10.star2.Pk10Star2Player;
import com.hs3.lotts.pk10.star3.Pk10Star3DjPlayer;
import com.hs3.lotts.pk10.star3.Pk10Star3Player;
import com.hs3.lotts.pk10.star3.Pk10Star3SinglePlayer;
import com.hs3.lotts.pk10.star4.Pk10Star4DjPlayer;
import com.hs3.lotts.pk10.star4.Pk10Star4Player;
import com.hs3.lotts.pk10.star4.Pk10Star4SinglePlayer;
import com.hs3.lotts.pk10.star5.Pk10Star5DjPlayer;
import com.hs3.lotts.pk10.star5.Pk10Star5Player;
import com.hs3.lotts.pk10.star5.Pk10Star5SinglePlayer;
import com.hs3.lotts.pk10.trend.Pk10Star5Trend;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Pk10Lottery
        extends LotteryBase {
    public Pk10Lottery() {
        this.title = "北京赛车";
        this.groupId = "pk10";
        PlayerQun sides = new PlayerQun("两面盘");
        PlayerQun star1 = new PlayerQun("定位胆");
        PlayerQun star2 = new PlayerQun("前二");
        PlayerQun star3 = new PlayerQun("前三");
        PlayerQun star4 = new PlayerQun("前四");
        PlayerQun star5 = new PlayerQun("前五");

        sides.addChild("车位").addChild(new Pk10SideLhPlayer()).addChild(new Pk10SideGyAndPlayer()).addChild(new Pk10SideDsPlayer());

        star1.addChild("定位胆").addChild(new Pk10Star1DwdPlayer()).addChild(new Pk10Star1DwdLastPlayer());

        star2.addChild("前二").addChild(new Pk10Star2AndPlayer()).addChild(new Pk10Star2Player()).addChild(new Pk10Star2DjPlayer());

        star3.addChild("前三").addChild(new Pk10Star3Player()).addChild(new Pk10Star3SinglePlayer()).addChild(new Pk10Star3DjPlayer());

        star4.addChild("前四").addChild(new Pk10Star4Player()).addChild(new Pk10Star4SinglePlayer()).addChild(new Pk10Star4DjPlayer());

        star5.addChild("前五").addChild(new Pk10Star5Player()).addChild(new Pk10Star5SinglePlayer()).addChild(new Pk10Star5DjPlayer());

        this.qun.add(sides);
        this.qun.add(star1);
        this.qun.add(star2);
        this.qun.add(star3);
        this.qun.add(star4);
        this.qun.add(star5);

        /**jd-gui
         Iterator localIterator2;
         for (Iterator localIterator1 = this.qun.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         PlayerQun q = (PlayerQun)localIterator1.next();
         localIterator2 = q.getGroups().iterator(); continue;PlayerGroup g = (PlayerGroup)localIterator2.next();
         for (PlayerBase p : g.getPlayers()) {
         this.player.put(p.getId(), p);
         }
         }*/

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

    protected int getNumLen() {
        return 2;
    }

    public List<String> getOpenStatus(LotterySeason season) {
        return null;
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
        rel.getNums().add(StrUtils.parseLength(season.getN6(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN7(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN8(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN9(), getNumLen()));
        rel.getNums().add(StrUtils.parseLength(season.getN10(), getNumLen()));
        return rel;
    }

    public int getOpenCount() {
        return 10;
    }

    public LotteryTrend getTrend() {
        return new Pk10Star5Trend();
    }

    public String getNumList() {
        return "01,02,03,04,05,06,07,08,09,10";
    }

    public boolean checkNumber(String num) {
        List<Integer> lst = ListUtils.toIntList(num);
        if (lst.size() != 10) {
            return false;
        }
        for (Integer i : lst) {
            if ((1 > i.intValue()) || (i.intValue() > 10)) {
                return false;
            }
        }
        if (ListUtils.hasSame(lst)) {
            return false;
        }
        return true;
    }
}
