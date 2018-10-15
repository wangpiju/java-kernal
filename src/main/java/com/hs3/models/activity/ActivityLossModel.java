package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityLoss;

import java.util.List;

public class ActivityLossModel
        extends Activity {
    private List<ActivityLoss> bonusList;

    public List<ActivityLoss> getBonusList() {
        return this.bonusList;
    }

    public void setBonusList(List<ActivityLoss> bonusList) {
        this.bonusList = bonusList;
    }
}
