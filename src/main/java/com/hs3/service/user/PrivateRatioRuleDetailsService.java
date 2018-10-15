package com.hs3.service.user;

import com.hs3.dao.user.PrivateRatioRuleDetailsDao;
import com.hs3.entity.users.PrivateRatioRuleDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateRatioRuleDetailsService {
    @Autowired
    private PrivateRatioRuleDetailsDao privateRatioRuleDetailsDao;

    public PrivateRatioRuleDetails find(Integer id) {
        return (PrivateRatioRuleDetails) this.privateRatioRuleDetailsDao.find(id);
    }

    public int delete(List<Integer> id) {
        return this.privateRatioRuleDetailsDao.delete(id);
    }

    public int save(PrivateRatioRuleDetails m) {
        return this.privateRatioRuleDetailsDao.saveAuto(m);
    }

    public int update(PrivateRatioRuleDetails m) {
        return this.privateRatioRuleDetailsDao.updateByIdAuto(m, m.getId());
    }

    public List<PrivateRatioRuleDetails> listByPid(Integer pid) {
        return this.privateRatioRuleDetailsDao.listByPid(pid);
    }
}
