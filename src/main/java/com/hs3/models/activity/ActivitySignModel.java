package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivitySign;

import java.util.List;

public class ActivitySignModel
        extends Activity {
    private List<ActivitySign> daysList;

    public List<ActivitySign> getDaysList() {
        return this.daysList;
    }

    public void setDaysList(List<ActivitySign> daysList) {
        this.daysList = daysList;
    }
}
