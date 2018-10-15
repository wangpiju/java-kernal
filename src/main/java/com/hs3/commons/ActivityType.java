package com.hs3.commons;

import com.hs3.entity.activity.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动类型
 */
public enum ActivityType {
    bet(1, "bet"),
    betconsumer(2, "betconsumer"),
    firstrecharge(3, "firstrecharge"),
    loss(4, "loss"),
    recharge(5, "recharge"),
    register(6, "register"),
    sign(7, "sign"),
    custom(8, "custom");

    private int type;
    private String alias;

    private static final Map<Integer, ActivityType> map = new HashMap<>();

    static {
        for (ActivityType t : ActivityType.values()) {
            map.put(t.type, t);
        }
    }

    ActivityType(int type, String alias) {
        this.type = type;
        this.alias = alias;
    }

    public Integer getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }

    public static ActivityType parse(Integer type) {
        ActivityType activityType = map.get(type);
        if (activityType == null) {
            return ActivityType.custom;
        }
        return map.get(type);
    }
}
