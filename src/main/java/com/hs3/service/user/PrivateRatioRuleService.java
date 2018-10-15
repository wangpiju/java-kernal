package com.hs3.service.user;

import com.hs3.dao.user.PrivateRatioRuleDao;
import com.hs3.dao.user.PrivateRatioRuleDetailsDao;
import com.hs3.db.Page;
import com.hs3.entity.users.PrivateRatioRule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateRatioRuleService {
    @Autowired
    private PrivateRatioRuleDao privateRatioRuleDao;
    @Autowired
    private PrivateRatioRuleDetailsDao privateRatioRuleDetailsDao;

    public PrivateRatioRule find(Integer id) {
        return (PrivateRatioRule) this.privateRatioRuleDao.find(id);
    }

    public List<PrivateRatioRule> list(Page p) {
        return this.privateRatioRuleDao.list(p);
    }

    public int delete(List<Integer> id) {
        this.privateRatioRuleDetailsDao.deleteByPids(id);
        return this.privateRatioRuleDao.delete(id);
    }

    public int save(PrivateRatioRule m) {
        return this.privateRatioRuleDao.saveAuto(m);
    }

    public int update(PrivateRatioRule m) {
        return this.privateRatioRuleDao.updateByIdAuto(m, m.getId());
    }
}
