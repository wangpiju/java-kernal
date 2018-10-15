package com.hs3.service.activity;

import com.hs3.dao.activity.ActivityDao;
import com.hs3.db.Page;
import com.hs3.entity.activity.Activity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class ActivityService {
    @Autowired
    private ActivityDao activityDao;

    public int update(Activity activity) {
        return this.activityDao.update(activity);
    }

    public Activity find(Integer id) {
        return this.activityDao.find(id);
    }

    public List<Activity> list(Page p) {
        return this.activityDao.list(p);
    }

    public List<Activity> listOrderBy(Page p) {
        return this.activityDao.listOrderBy(p);
    }

    public List<Activity> listByActive(Integer userType) {
        return this.activityDao.listByActive(userType);
    }

    public int saveOrUpdate(Activity a) {
        if (a.getId() != null)
            return activityDao.update(a);
        else
            return activityDao.save(a);
    }

    public int del(Integer id) {
        return activityDao.del(id);
    }
}
