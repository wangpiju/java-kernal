package com.hs3.service.bank;

import com.hs3.dao.bank.BankSysDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankSys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankSysService")
public class BankSysService {
    @Autowired
    private BankSysDao bankSysDao;

    public void save(BankSys m) {
        this.bankSysDao.save(m);
    }

    public int update(BankSys m) {
        return this.bankSysDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.bankSysDao.delete(ids);
    }

    public BankSys find(Integer id) {
        return (BankSys) this.bankSysDao.find(id);
    }

    public List<BankSys> list(Page p) {
        return this.bankSysDao.list(p);
    }
}
