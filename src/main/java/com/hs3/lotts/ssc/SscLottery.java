package com.hs3.lotts.ssc;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryTrend;
import com.hs3.lotts.PlayerBase;
import com.hs3.lotts.PlayerGroup;
import com.hs3.lotts.PlayerQun;
import com.hs3.lotts.ssc.sides.SscDxdsPlayer;
import com.hs3.lotts.ssc.sides.SscSideLhhPlayer;
import com.hs3.lotts.ssc.sides.SscSideTemaPlayer;
import com.hs3.lotts.ssc.star1.SscStar1DwdPlayer;
import com.hs3.lotts.ssc.star2.any.SscStar2AnyGroupPlayer;
import com.hs3.lotts.ssc.star2.any.SscStar2AnyGroupSinglePlayer;
import com.hs3.lotts.ssc.star2.any.SscStar2AnyPlayer;
import com.hs3.lotts.ssc.star2.any.SscStar2AnySinglePlayer;
import com.hs3.lotts.ssc.star2.front.SscStar2FrontAndPlayer;
import com.hs3.lotts.ssc.star2.front.SscStar2FrontKdPlayer;
import com.hs3.lotts.ssc.star2.front.SscStar2FrontPlayer;
import com.hs3.lotts.ssc.star2.front.SscStar2FrontSinglePlayer;
import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupAndPlayer;
import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupContainsPlayer;
import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupPlayer;
import com.hs3.lotts.ssc.star2.front.group.SscStar2FrontGroupSinglePlayer;
import com.hs3.lotts.ssc.star2.last.SscStar2LastAndPlayer;
import com.hs3.lotts.ssc.star2.last.SscStar2LastKdPlayer;
import com.hs3.lotts.ssc.star2.last.SscStar2LastPlayer;
import com.hs3.lotts.ssc.star2.last.SscStar2LastSinglePlayer;
import com.hs3.lotts.ssc.star2.last.group.SscStar2LastGroupAndPlayer;
import com.hs3.lotts.ssc.star2.last.group.SscStar2LastGroupContainsPlayer;
import com.hs3.lotts.ssc.star2.last.group.SscStar2LastGroupPlayer;
import com.hs3.lotts.ssc.star2.last.group.SscStar2LastGroupSinglePlayer;
import com.hs3.lotts.ssc.star3.any.SscStar3AnyGroup36SinglePlayer;
import com.hs3.lotts.ssc.star3.any.SscStar3AnyGroup3Player;
import com.hs3.lotts.ssc.star3.any.SscStar3AnyGroup6Player;
import com.hs3.lotts.ssc.star3.any.SscStar3AnyPlayer;
import com.hs3.lotts.ssc.star3.any.SscStar3AnySinglePlayer;
import com.hs3.lotts.ssc.star3.front.SscStar3FrontAndPlayer;
import com.hs3.lotts.ssc.star3.front.SscStar3FrontKdPlayer;
import com.hs3.lotts.ssc.star3.front.SscStar3FrontPlayer;
import com.hs3.lotts.ssc.star3.front.SscStar3FrontSinglePlayer;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup36SinglePlayer;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup3Player;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup3SinglePlayer;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup6Player;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroup6SinglePlayer;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroupAndPlayer;
import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroupContainsPlayer;
import com.hs3.lotts.ssc.star3.front.none.SscStar3FrontNone1Player;
import com.hs3.lotts.ssc.star3.front.none.SscStar3FrontNone2Player;
import com.hs3.lotts.ssc.star3.last.SscStar3LastAndPlayer;
import com.hs3.lotts.ssc.star3.last.SscStar3LastKdPlayer;
import com.hs3.lotts.ssc.star3.last.SscStar3LastPlayer;
import com.hs3.lotts.ssc.star3.last.SscStar3LastSinglePlayer;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroup36SinglePlayer;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroup3Player;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroup3SinglePlayer;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroup6Player;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroup6SinglePlayer;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroupAndPlayer;
import com.hs3.lotts.ssc.star3.last.group.SscStar3LastGroupContainsPlayer;
import com.hs3.lotts.ssc.star3.last.none.SscStar3LastNone1Player;
import com.hs3.lotts.ssc.star3.last.none.SscStar3LastNone2Player;
import com.hs3.lotts.ssc.star3.mid.SscStar3MidAndPlayer;
import com.hs3.lotts.ssc.star3.mid.SscStar3MidKdPlayer;
import com.hs3.lotts.ssc.star3.mid.SscStar3MidPlayer;
import com.hs3.lotts.ssc.star3.mid.SscStar3MidSinglePlayer;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroup36SinglePlayer;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroup3Player;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroup3SinglePlayer;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroup6Player;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroup6SinglePlayer;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroupAndPlayer;
import com.hs3.lotts.ssc.star3.mid.group.SscStar3MidGroupContainsPlayer;
import com.hs3.lotts.ssc.star3.mid.none.SscStar3MidNone1Player;
import com.hs3.lotts.ssc.star3.mid.none.SscStar3MidNone2Player;
import com.hs3.lotts.ssc.star4.any.SscStar4AnyPlayer;
import com.hs3.lotts.ssc.star4.any.SscStar4AnySinglePlayer;
import com.hs3.lotts.ssc.star4.front.SscStar4FrontPlayer;
import com.hs3.lotts.ssc.star4.front.SscStar4FrontSinglePlayer;
import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup12Player;
import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup24Player;
import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup4Player;
import com.hs3.lotts.ssc.star4.front.group.SscStar4FrontGroup6Player;
import com.hs3.lotts.ssc.star4.front.none.SscStar4FrontNone1Player;
import com.hs3.lotts.ssc.star4.front.none.SscStar4FrontNone2Player;
import com.hs3.lotts.ssc.star4.last.SscStar4LastPlayer;
import com.hs3.lotts.ssc.star4.last.SscStar4LastSinglePlayer;
import com.hs3.lotts.ssc.star4.last.group.SscStar4LastGroup12Player;
import com.hs3.lotts.ssc.star4.last.group.SscStar4LastGroup24Player;
import com.hs3.lotts.ssc.star4.last.group.SscStar4LastGroup4Player;
import com.hs3.lotts.ssc.star4.last.group.SscStar4LastGroup6Player;
import com.hs3.lotts.ssc.star4.last.none.SscStar4LastNone1Player;
import com.hs3.lotts.ssc.star4.last.none.SscStar4LastNone2Player;
import com.hs3.lotts.ssc.star5.SscStar5Player;
import com.hs3.lotts.ssc.star5.SscStar5SinglePlayer;
import com.hs3.lotts.ssc.star5.group.SscStar5Group10Player;
import com.hs3.lotts.ssc.star5.group.SscStar5Group120Player;
import com.hs3.lotts.ssc.star5.group.SscStar5Group20Player;
import com.hs3.lotts.ssc.star5.group.SscStar5Group30Player;
import com.hs3.lotts.ssc.star5.group.SscStar5Group5Player;
import com.hs3.lotts.ssc.star5.group.SscStar5Group60Player;
import com.hs3.lotts.ssc.star5.none.SscStar5None1Player;
import com.hs3.lotts.ssc.star5.none.SscStar5None2Player;
import com.hs3.lotts.ssc.star5.none.SscStar5None3Player;
import com.hs3.lotts.ssc.star5.other.SscStar5Other1Player;
import com.hs3.lotts.ssc.star5.other.SscStar5Other2Player;
import com.hs3.lotts.ssc.star5.other.SscStar5Other3Player;
import com.hs3.lotts.ssc.star5.other.SscStar5Other4Player;
import com.hs3.lotts.ssc.trend.SscStar5Trend;
import com.hs3.models.lotts.SeasonOpen;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SscLottery
        extends LotteryBase {
    public SscLottery() {
        /**jd-gui
         this.title = "时时彩";
         this.groupId = "ssc";
         PlayerQun wx = new PlayerQun("微信玩法");
         PlayerQun side = new PlayerQun("龙虎");
         PlayerQun starNone = new PlayerQun("任选");
         PlayerQun star1 = new PlayerQun("定位胆");
         PlayerQun star21 = new PlayerQun("前二");
         PlayerQun star22 = new PlayerQun("后二");
         PlayerQun star31 = new PlayerQun("前三");
         PlayerQun star32 = new PlayerQun("中三");
         PlayerQun star33 = new PlayerQun("后三");
         PlayerQun star41 = new PlayerQun("前四");
         PlayerQun star42 = new PlayerQun("后四");
         PlayerQun star5 = new PlayerQun("五星");


         star5.addChild("直选").addChild(new SscStar5Player()).addChild(new SscStar5SinglePlayer());

         star5.addChild("组选")
         .addChild(new SscStar5Group5Player())
         .addChild(new SscStar5Group10Player())
         .addChild(new SscStar5Group20Player())
         .addChild(new SscStar5Group30Player())
         .addChild(new SscStar5Group60Player())
         .addChild(new SscStar5Group120Player());

         star5.addChild("趣味").addChild(new SscStar5Other1Player()).addChild(new SscStar5Other2Player()).addChild(new SscStar5Other3Player())
         .addChild(new SscStar5Other4Player());

         star5.addChild("不定位").addChild(new SscStar5None1Player()).addChild(new SscStar5None2Player()).addChild(new SscStar5None3Player());


         star41.addChild("直选").addChild(new SscStar4FrontPlayer()).addChild(new SscStar4FrontSinglePlayer());


         star41.addChild("组选")
         .addChild(new SscStar4FrontGroup4Player())
         .addChild(new SscStar4FrontGroup6Player())
         .addChild(new SscStar4FrontGroup12Player())
         .addChild(new SscStar4FrontGroup24Player());

         star41.addChild("不定位").addChild(new SscStar4FrontNone1Player()).addChild(new SscStar4FrontNone2Player());


         star42.addChild("直选").addChild(new SscStar4LastPlayer()).addChild(new SscStar4LastSinglePlayer());


         star42.addChild("组选")
         .addChild(new SscStar4LastGroup4Player())
         .addChild(new SscStar4LastGroup6Player())
         .addChild(new SscStar4LastGroup12Player())
         .addChild(new SscStar4LastGroup24Player());

         star42.addChild("不定位").addChild(new SscStar4LastNone1Player()).addChild(new SscStar4LastNone2Player());


         star31.addChild("直选").addChild(new SscStar3FrontPlayer()).addChild(new SscStar3FrontSinglePlayer()).addChild(new SscStar3FrontAndPlayer())
         .addChild(new SscStar3FrontKdPlayer());


         star31.addChild("组选").addChild(new SscStar3FrontGroup3Player()).addChild(new SscStar3FrontGroup6Player())
         .addChild(new SscStar3FrontGroup3SinglePlayer()).addChild(new SscStar3FrontGroup6SinglePlayer())
         .addChild(new SscStar3FrontGroupAndPlayer()).addChild(new SscStar3FrontGroupContainsPlayer())
         .addChild(new SscStar3FrontGroup36SinglePlayer());


         star31.addChild("不定位").addChild(new SscStar3FrontNone1Player()).addChild(new SscStar3FrontNone2Player());


         star32.addChild("直选").addChild(new SscStar3MidPlayer()).addChild(new SscStar3MidSinglePlayer()).addChild(new SscStar3MidAndPlayer())
         .addChild(new SscStar3MidKdPlayer());

         star32.addChild("组选").addChild(new SscStar3MidGroup3Player()).addChild(new SscStar3MidGroup6Player())
         .addChild(new SscStar3MidGroup3SinglePlayer()).addChild(new SscStar3MidGroup6SinglePlayer())
         .addChild(new SscStar3MidGroupAndPlayer()).addChild(new SscStar3MidGroupContainsPlayer())
         .addChild(new SscStar3MidGroup36SinglePlayer());

         star32.addChild("不定位").addChild(new SscStar3MidNone1Player()).addChild(new SscStar3MidNone2Player());


         star33.addChild("直选").addChild(new SscStar3LastPlayer()).addChild(new SscStar3LastSinglePlayer()).addChild(new SscStar3LastAndPlayer())
         .addChild(new SscStar3LastKdPlayer());

         star33.addChild("组选").addChild(new SscStar3LastGroup3Player()).addChild(new SscStar3LastGroup6Player())
         .addChild(new SscStar3LastGroup3SinglePlayer()).addChild(new SscStar3LastGroup6SinglePlayer())
         .addChild(new SscStar3LastGroupAndPlayer()).addChild(new SscStar3LastGroupContainsPlayer())
         .addChild(new SscStar3LastGroup36SinglePlayer());

         star33.addChild("不定位").addChild(new SscStar3LastNone1Player()).addChild(new SscStar3LastNone2Player());


         star21.addChild("直选").addChild(new SscStar2FrontPlayer()).addChild(new SscStar2FrontSinglePlayer()).addChild(new SscStar2FrontAndPlayer())
         .addChild(new SscStar2FrontKdPlayer());
         star21.addChild("组选").addChild(new SscStar2FrontGroupPlayer()).addChild(new SscStar2FrontGroupSinglePlayer())
         .addChild(new SscStar2FrontGroupAndPlayer()).addChild(new SscStar2FrontGroupContainsPlayer());


         star22.addChild("直选").addChild(new SscStar2LastPlayer()).addChild(new SscStar2LastSinglePlayer()).addChild(new SscStar2LastAndPlayer())
         .addChild(new SscStar2LastKdPlayer());
         star22.addChild("组选").addChild(new SscStar2LastGroupPlayer()).addChild(new SscStar2LastGroupSinglePlayer())
         .addChild(new SscStar2LastGroupAndPlayer()).addChild(new SscStar2LastGroupContainsPlayer());

         star1.addChild("定位胆").addChild(new SscStar1DwdPlayer());

         starNone.addChild("任选二")
         .addChild(new SscStar2AnyPlayer())
         .addChild(new SscStar2AnySinglePlayer())
         .addChild(new SscStar2AnyGroupPlayer())
         .addChild(new SscStar2AnyGroupSinglePlayer());
         starNone.addChild("任选三")
         .addChild(new SscStar3AnyPlayer())
         .addChild(new SscStar3AnySinglePlayer())
         .addChild(new SscStar3AnyGroup3Player())
         .addChild(new SscStar3AnyGroup6Player())
         .addChild(new SscStar3AnyGroup36SinglePlayer());

         starNone.addChild("任选四")
         .addChild(new SscStar4AnyPlayer())
         .addChild(new SscStar4AnySinglePlayer());

         side.addChild("龙虎和")
         .addChild(new SscSideLhhPlayer());

         wx.addChild("大小单双")
         .addChild(new SscDxdsPlayer())
         .addChild(new SscSideTemaPlayer());

         this.qun.add(star5);

         this.qun.add(star41);
         this.qun.add(star42);

         this.qun.add(star31);
         this.qun.add(star32);
         this.qun.add(star33);

         this.qun.add(star21);
         this.qun.add(star22);

         this.qun.add(star1);

         this.qun.add(starNone);

         this.qun.add(side);
         this.qun.add(wx);
         Iterator localIterator2;
         for (Iterator localIterator1 = this.qun.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         PlayerQun q = (PlayerQun)localIterator1.next();
         localIterator2 = q.getGroups().iterator(); continue;PlayerGroup g = (PlayerGroup)localIterator2.next();
         for (PlayerBase p : g.getPlayers()) {
         this.player.put(p.getId(), p);
         }
         }
         */
        title = "时时彩";
        groupId = "ssc";
        PlayerQun wx = new PlayerQun("微信玩法");
        PlayerQun side = new PlayerQun("龙虎");
        PlayerQun starNone = new PlayerQun("任选");
        PlayerQun star1 = new PlayerQun("定位胆");
        PlayerQun star21 = new PlayerQun("前二");
        PlayerQun star22 = new PlayerQun("后二");
        PlayerQun star31 = new PlayerQun("前三");
        PlayerQun star32 = new PlayerQun("中三");
        PlayerQun star33 = new PlayerQun("后三");
        PlayerQun star41 = new PlayerQun("前四");
        PlayerQun star42 = new PlayerQun("后四");
        PlayerQun star5 = new PlayerQun("五星");
        star5.addChild("直选").addChild(new SscStar5Player()).addChild(new SscStar5SinglePlayer());
        star5.addChild("组选").addChild(new SscStar5Group5Player()).addChild(new SscStar5Group10Player()).addChild(new SscStar5Group20Player()).addChild(new SscStar5Group30Player()).addChild(new SscStar5Group60Player()).addChild(new SscStar5Group120Player());
        star5.addChild("趣味").addChild(new SscStar5Other1Player()).addChild(new SscStar5Other2Player()).addChild(new SscStar5Other3Player()).addChild(new SscStar5Other4Player());
        star5.addChild("不定位").addChild(new SscStar5None1Player()).addChild(new SscStar5None2Player()).addChild(new SscStar5None3Player());
        star41.addChild("直选").addChild(new SscStar4FrontPlayer()).addChild(new SscStar4FrontSinglePlayer());
        star41.addChild("组选").addChild(new SscStar4FrontGroup4Player()).addChild(new SscStar4FrontGroup6Player()).addChild(new SscStar4FrontGroup12Player()).addChild(new SscStar4FrontGroup24Player());
        star41.addChild("不定位").addChild(new SscStar4FrontNone1Player()).addChild(new SscStar4FrontNone2Player());
        star42.addChild("直选").addChild(new SscStar4LastPlayer()).addChild(new SscStar4LastSinglePlayer());
        star42.addChild("组选").addChild(new SscStar4LastGroup4Player()).addChild(new SscStar4LastGroup6Player()).addChild(new SscStar4LastGroup12Player()).addChild(new SscStar4LastGroup24Player());
        star42.addChild("不定位").addChild(new SscStar4LastNone1Player()).addChild(new SscStar4LastNone2Player());
        star31.addChild("直选").addChild(new SscStar3FrontPlayer()).addChild(new SscStar3FrontSinglePlayer()).addChild(new SscStar3FrontAndPlayer()).addChild(new SscStar3FrontKdPlayer());
        star31.addChild("组选").addChild(new SscStar3FrontGroup3Player()).addChild(new SscStar3FrontGroup6Player()).addChild(new SscStar3FrontGroup3SinglePlayer()).addChild(new SscStar3FrontGroup6SinglePlayer()).addChild(new SscStar3FrontGroupAndPlayer()).addChild(new SscStar3FrontGroupContainsPlayer()).addChild(new SscStar3FrontGroup36SinglePlayer());
        star31.addChild("不定位").addChild(new SscStar3FrontNone1Player()).addChild(new SscStar3FrontNone2Player());
        star32.addChild("直选").addChild(new SscStar3MidPlayer()).addChild(new SscStar3MidSinglePlayer()).addChild(new SscStar3MidAndPlayer()).addChild(new SscStar3MidKdPlayer());
        star32.addChild("组选").addChild(new SscStar3MidGroup3Player()).addChild(new SscStar3MidGroup6Player()).addChild(new SscStar3MidGroup3SinglePlayer()).addChild(new SscStar3MidGroup6SinglePlayer()).addChild(new SscStar3MidGroupAndPlayer()).addChild(new SscStar3MidGroupContainsPlayer()).addChild(new SscStar3MidGroup36SinglePlayer());
        star32.addChild("不定位").addChild(new SscStar3MidNone1Player()).addChild(new SscStar3MidNone2Player());
        star33.addChild("直选").addChild(new SscStar3LastPlayer()).addChild(new SscStar3LastSinglePlayer()).addChild(new SscStar3LastAndPlayer()).addChild(new SscStar3LastKdPlayer());
        star33.addChild("组选").addChild(new SscStar3LastGroup3Player()).addChild(new SscStar3LastGroup6Player()).addChild(new SscStar3LastGroup3SinglePlayer()).addChild(new SscStar3LastGroup6SinglePlayer()).addChild(new SscStar3LastGroupAndPlayer()).addChild(new SscStar3LastGroupContainsPlayer()).addChild(new SscStar3LastGroup36SinglePlayer());
        star33.addChild("不定位").addChild(new SscStar3LastNone1Player()).addChild(new SscStar3LastNone2Player());
        star21.addChild("直选").addChild(new SscStar2FrontPlayer()).addChild(new SscStar2FrontSinglePlayer()).addChild(new SscStar2FrontAndPlayer()).addChild(new SscStar2FrontKdPlayer());
        star21.addChild("组选").addChild(new SscStar2FrontGroupPlayer()).addChild(new SscStar2FrontGroupSinglePlayer()).addChild(new SscStar2FrontGroupAndPlayer()).addChild(new SscStar2FrontGroupContainsPlayer());
        star22.addChild("直选").addChild(new SscStar2LastPlayer()).addChild(new SscStar2LastSinglePlayer()).addChild(new SscStar2LastAndPlayer()).addChild(new SscStar2LastKdPlayer());
        star22.addChild("组选").addChild(new SscStar2LastGroupPlayer()).addChild(new SscStar2LastGroupSinglePlayer()).addChild(new SscStar2LastGroupAndPlayer()).addChild(new SscStar2LastGroupContainsPlayer());
        star1.addChild("定位胆").addChild(new SscStar1DwdPlayer());
        starNone.addChild("任选二").addChild(new SscStar2AnyPlayer()).addChild(new SscStar2AnySinglePlayer()).addChild(new SscStar2AnyGroupPlayer()).addChild(new SscStar2AnyGroupSinglePlayer());
        starNone.addChild("任选三").addChild(new SscStar3AnyPlayer()).addChild(new SscStar3AnySinglePlayer()).addChild(new SscStar3AnyGroup3Player()).addChild(new SscStar3AnyGroup6Player()).addChild(new SscStar3AnyGroup36SinglePlayer());
        starNone.addChild("任选四").addChild(new SscStar4AnyPlayer()).addChild(new SscStar4AnySinglePlayer());
        side.addChild("龙虎和").addChild(new SscSideLhhPlayer());
        wx.addChild("大小单双").addChild(new SscDxdsPlayer()).addChild(new SscSideTemaPlayer());
        qun.add(star5);
        qun.add(star41);
        qun.add(star42);
        qun.add(star31);
        qun.add(star32);
        qun.add(star33);
        qun.add(star21);
        qun.add(star22);
        qun.add(star1);
        qun.add(starNone);
        qun.add(side);
        qun.add(wx);
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
            return "<span style='color: red'>豹子</span>";
        }
        if (n.size() == 2) {
            return "<span style='color: purple'>组三</span>";
        }
        return "<span style='color: green'>组六</span>";
    }

    public List<String> getOpenStatus(LotterySeason season) {
        List<String> rel = new ArrayList();
        Set<Integer> list = new HashSet();

        list.add(season.getN1());
        list.add(season.getN2());
        list.add(season.getN3());
        rel.add("前三：" + getStatusStr(list));

        list.clear();
        list.add(season.getN2());
        list.add(season.getN3());
        list.add(season.getN4());
        rel.add("中三：" + getStatusStr(list));


        list.clear();
        list.add(season.getN3());
        list.add(season.getN4());
        list.add(season.getN5());
        rel.add("后三：" + getStatusStr(list));
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
        return new SscStar5Trend();
    }

    public String getNumList() {
        return "0,1,2,3,4,5,6,7,8,9";
    }

    public boolean checkNumber(String num) {
        List<Integer> lst = ListUtils.toIntList(num);
        if (lst.size() != 5) {
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
