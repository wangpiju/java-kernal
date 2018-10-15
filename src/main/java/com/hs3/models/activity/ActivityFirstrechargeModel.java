package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityFirstrecharge;

import java.util.List;

public class ActivityFirstrechargeModel
        extends Activity {
    private List<ActivityFirstrecharge> bonusList;

    public List<ActivityFirstrecharge> getBonusList() {
        return this.bonusList;
    }

    public void setBonusList(List<ActivityFirstrecharge> bonusList) {
        this.bonusList = bonusList;
    }
}
