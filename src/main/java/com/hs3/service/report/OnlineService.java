package com.hs3.service.report;

import com.hs3.dao.report.OnlineDao;
import com.hs3.db.Page;
import com.hs3.models.report.Online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("onlineService")
public class OnlineService {
    @Autowired
    private OnlineDao onlineDao;

    public List<Online> list(Page page, String account, Integer userType, String ip, String info) {
        return this.onlineDao.list(page, account, userType, ip, info);
    }
}
