package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityRecharge;

import java.util.List;

public class ActivityRechargeModel
        extends Activity {
    private List<ActivityRecharge> bonusList;

    public List<ActivityRecharge> getBonusList() {
        return this.bonusList;
    }

    public void setBonusList(List<ActivityRecharge> bonusList) {
        this.bonusList = bonusList;
    }
}
