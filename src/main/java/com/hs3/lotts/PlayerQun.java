package com.hs3.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlayerQun {
    private String title;
    private List<PlayerGroup> groups = new ArrayList();

    public PlayerQun(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public List<PlayerGroup> getGroups() {
        return this.groups;
    }

    public int getPlayerCount() {
        int count = 0;
        for (PlayerGroup g : this.groups) {
            count += g.getPlayerCount().intValue();
        }
        return count;
    }

    public PlayerGroup addChild(String title) {
        PlayerGroup group = new PlayerGroup(title);
        this.groups.add(group);
        return group;
    }

    public PlayerQun removePlayer(String id) {
        for (PlayerGroup p : this.groups) {
            p.removeChild(id);
        }
        return this;
    }
}
