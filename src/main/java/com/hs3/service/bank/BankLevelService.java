package com.hs3.service.bank;

import com.hs3.dao.bank.BankLevelDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankLevel;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankLevelService")
public class BankLevelService {
    @Autowired
    private BankLevelDao bankLevelDao;

    public int count(int minCount, BigDecimal minAmount) {
        return this.bankLevelDao.count(minCount, minAmount);
    }

    public void save(BankLevel m) {
        this.bankLevelDao.save(m);
    }

    public int update(BankLevel m) {
        return this.bankLevelDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.bankLevelDao.delete(ids);
    }

    public BankLevel find(Integer id) {
        return (BankLevel) this.bankLevelDao.find(id);
    }

    public List<BankLevel> list(Page p) {
        return this.bankLevelDao.list(p);
    }

    public List<BankLevel> listAll(Page p) {
        return this.bankLevelDao.listAll(p);
    }
}
