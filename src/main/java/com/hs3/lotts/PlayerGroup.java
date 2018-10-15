package com.hs3.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlayerGroup {
    private String title;
    private Map<String, PlayerBase> players = new LinkedHashMap();

    public PlayerGroup(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public Collection<PlayerBase> getPlayers() {
        return this.players.values();
    }

    public Integer getPlayerCount() {
        return Integer.valueOf(this.players.size());
    }

    public PlayerGroup addChild(PlayerBase play) {
        this.players.put(play.getId(), play);
        return this;
    }

    public PlayerGroup removeChild(String id) {
        this.players.remove(id);
        return this;
    }
}
