package com.hs3.service.activity;

import com.hs3.commons.ActivityType;
import com.hs3.entity.activity.Activity;
import com.hs3.models.Jsoner;
import com.hs3.utils.StrUtils;

public abstract class ActivityBaseService {
    public abstract Jsoner add(String paramString, boolean paramBoolean);

    public abstract Jsoner addBonus(String paramString1, boolean paramBoolean, String paramString2);

    public abstract void setStatus(Activity paramActivity, String paramString, boolean paramBoolean);

    public String getRemark(Activity activity) {
        return StrUtils.hasEmpty(activity.getChangeRemark()) ? ActivityType.parse(activity.getId()).getAlias() : activity.getChangeRemark();
    }
}
