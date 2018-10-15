package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityBet;

import java.util.List;

public class ActivityBetModel
        extends Activity {
    private List<ActivityBet> bonusList;

    public List<ActivityBet> getBonusList() {
        return this.bonusList;
    }

    public void setBonusList(List<ActivityBet> bonusList) {
        this.bonusList = bonusList;
    }
}
