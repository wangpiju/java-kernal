package com.hs3.service.report;

import com.hs3.dao.report.UserStateDao;
import com.hs3.db.Page;
import com.hs3.entity.report.UserState;
import com.hs3.entity.report.ZongDaiState;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userStateService")
public class UserStateService {
    @Autowired
    private UserStateDao userStateDao;

    public UserState getUserState() {
        return this.userStateDao.getUserState();
    }

    public List<ZongDaiState> getZongDaiState(Page page) {
        return this.userStateDao.getZongDaiState(page);
    }
}
