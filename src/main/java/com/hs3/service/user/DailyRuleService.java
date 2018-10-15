package com.hs3.service.user;

import com.hs3.dao.user.DailyAccDao;
import com.hs3.dao.user.DailyRuleDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.users.DailyRule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dailyRuleService")
public class DailyRuleService {
    @Autowired
    private DailyRuleDao daliyRuleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DailyAccDao dailyAccDao;

    public List<DailyRule> listByCond(DailyRule m, Page page) {
        return this.daliyRuleDao.listByCond(m, page);
    }

    public List<DailyRule> listByStatus(Integer status) {
        DailyRule cond = new DailyRule();
        cond.setStatus(status);
        return listByCond(cond, null);
    }

    public void save(DailyRule m) {
        this.daliyRuleDao.save(m);
    }

    public List<DailyRule> list(Page p) {
        return this.daliyRuleDao.list(p);
    }

    public List<DailyRule> listWithOrder(Page p) {
        return this.daliyRuleDao.listWithOrder(p);
    }

    public DailyRule find(Integer id) {
        return (DailyRule) this.daliyRuleDao.find(id);
    }

    public int update(DailyRule m) {
        this.userDao.deleteDailyRuleByRuleId(m.getId());
        this.dailyAccDao.deleteByRuleId(m.getId());
        return this.daliyRuleDao.update(m);
    }

    public int delete(List<Integer> ids) {
        for (Integer id : ids) {
            this.userDao.deleteDailyRuleByRuleId(id);
            this.dailyAccDao.deleteByRuleId(id);
        }
        return this.daliyRuleDao.delete(ids);
    }
}
