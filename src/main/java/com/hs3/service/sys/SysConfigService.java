package com.hs3.service.sys;

import com.hs3.dao.sys.SysConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigService {
    public static final String YESTERDAY_REGISTER = "YESTERDAY";
    public static final String PROJECT_NAME = "PROJECT_NAME";
    public static final String RECHARGE_BANK_ARTICLE_ID = "RECHARGE_BANK_ARTICLE_ID";
    public static final String RECHARGE_BANK_REMARK = "RECHARGE_BANK_REMARK";
    @Autowired
    private SysConfigDao sysConfigDao;

    public List<SysConfig> list(Page p) {
        return this.sysConfigDao.list(p);
    }

    public void save(SysConfig m) {
        this.sysConfigDao.save(m);
    }

    public boolean update(SysConfig m) {
        return 1 == this.sysConfigDao.update(m);
    }

    public int delete(List<String> ids) {
        return this.sysConfigDao.delete(ids);
    }

    public SysConfig find(String id) {
        return (SysConfig) this.sysConfigDao.find(id);
    }
}
