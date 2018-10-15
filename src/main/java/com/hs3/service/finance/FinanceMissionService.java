package com.hs3.service.finance;

import com.hs3.dao.finance.FinanceMissionDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceMission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("financeMissionService")
public class FinanceMissionService {
    @Autowired
    private FinanceMissionDao financeMissionDao;

    public void save(FinanceMission m) {
        this.financeMissionDao.save(m);
    }

    public List<FinanceMission> list(Page p) {
        return this.financeMissionDao.list(p);
    }

    public List<FinanceMission> listWithOrder(Page p) {
        return this.financeMissionDao.listWithOrder(p);
    }

    public FinanceMission find(Integer id) {
        return (FinanceMission) this.financeMissionDao.find(id);
    }

    public int update(FinanceMission m) {
        return this.financeMissionDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.financeMissionDao.delete(ids);
    }
}
