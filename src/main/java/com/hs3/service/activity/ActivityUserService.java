package com.hs3.service.activity;

import com.hs3.dao.activity.ActivityUserDao;
import com.hs3.db.Page;
import com.hs3.entity.activity.ActivityUser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityUserService")
public class ActivityUserService {
    @Autowired
    private ActivityUserDao activityUserDao;

    public int update(ActivityUser m) {
        return this.activityUserDao.update(m);
    }

    public void save(ActivityUser m) {
        this.activityUserDao.save(m);
    }

    public ActivityUser find(Integer id) {
        return this.activityUserDao.find(id);
    }

    public List<ActivityUser> list(Integer activityId, Integer status, Page p) {
        return this.activityUserDao.list(activityId, status, p);
    }

    public boolean hasAccount(Integer activityId, String account) {
        return this.activityUserDao.getCountAccount(activityId, account) > 0;
    }

    public boolean setStatus(Integer id, Integer oldStatus, Integer newStatus, String operator) {
        return this.activityUserDao.setStatus(id, oldStatus, newStatus, operator) == 1;
    }
}
