package com.hs3.service.bank;

import com.hs3.dao.bank.BankAccDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankAcc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankAccService")
public class BankAccService {
    @Autowired
    private BankAccDao bankAccDao;

    public void save(BankAcc m) {
        this.bankAccDao.save(m);
    }

    public List<BankAcc> listByBankApiId(Integer bankApiId) {
        return this.bankAccDao.listByBankApiId(bankApiId);
    }

    public List<BankAcc> list(Page p) {
        return this.bankAccDao.list(p);
    }

    public List<BankAcc> listWithOrder(Page p) {
        return this.bankAccDao.listWithOrder(p);
    }

    public BankAcc find(Integer id) {
        return (BankAcc) this.bankAccDao.find(id);
    }

    public int update(BankAcc m) {
        return this.bankAccDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.bankAccDao.delete(ids);
    }
}
