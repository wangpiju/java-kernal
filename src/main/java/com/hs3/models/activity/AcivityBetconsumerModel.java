package com.hs3.models.activity;

import com.hs3.entity.activity.Activity;
import com.hs3.entity.activity.ActivityBetconsumer;

import java.util.List;

public class AcivityBetconsumerModel
        extends Activity {
    List<ActivityBetconsumer> items;

    public List<ActivityBetconsumer> getItems() {
        return this.items;
    }

    public void setItems(List<ActivityBetconsumer> items) {
        this.items = items;
    }
}
