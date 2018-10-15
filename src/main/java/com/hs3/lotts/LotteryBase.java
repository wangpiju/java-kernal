package com.hs3.lotts;

import com.hs3.entity.lotts.LotterySeason;
import com.hs3.models.lotts.PlayerBonus;
import com.hs3.models.lotts.SeasonOpen;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class LotteryBase {
    protected String title;
    protected String groupId;
    protected List<PlayerQun> qun = new ArrayList<>();
    protected Map<String, PlayerBase> player = new LinkedHashMap<>();
    private Map<String, PlayerBonus> playerBonus = new LinkedHashMap<>();

    protected int getNumLen() {
        return 1;
    }

    public abstract String getNumList();

    public String getTitle() {
        return this.title;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public List<PlayerQun> getQun() {
        return this.qun;
    }

    public PlayerBase getPlayer(String playerId) {
        if (this.player.containsKey(playerId)) {
            return this.player.get(playerId);
        }
        return null;
    }

    public Collection<PlayerBase> getPlayers() {
        return this.player.values();
    }

    public void removePlayer(String id) {
        for (PlayerQun q : this.qun) {
            q.removePlayer(id);
        }
        this.player.remove(id);
        this.playerBonus.remove(id);
    }

    public void loadPlayerBonus(List<PlayerBonus> ps) {
        for (PlayerBonus p : ps) {
            this.playerBonus.put(p.getId(), p);
        }
    }

    public PlayerBonus getPlayerBonus(String playerId) {
        if (this.playerBonus.containsKey(playerId)) {
            return (PlayerBonus) this.playerBonus.get(playerId);
        }
        return null;
    }

    public Collection<PlayerBonus> getPlayerBonusList() {
        return this.playerBonus.values();
    }

    public Map<String, PlayerBonus> getPlayerBonusMap() {
        return this.playerBonus;
    }

    public abstract SeasonOpen getSeasonOpen(LotterySeason paramLotterySeason);

    public abstract List<String> getOpenStatus(LotterySeason paramLotterySeason);

    public abstract int getOpenCount();

    public List<SeasonOpen> getSeasonOpen(List<LotterySeason> seasons) {
        List<SeasonOpen> list = new ArrayList<>();
        for (LotterySeason season : seasons) {
            list.add(getSeasonOpen(season));
        }
        return list;
    }

    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        for (Integer  i: list) {
           System.out.println(i);
        }

    }

    public abstract LotteryTrend getTrend();

    public abstract boolean checkNumber(String paramString);
}
