package com.hs3.service.bank;

import com.hs3.dao.bank.BankGroupDao;
import com.hs3.dao.bank.BankSysDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankGroup;
import com.hs3.entity.bank.BankSys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankGroupService")
public class BankGroupService {
    @Autowired
    private BankGroupDao bankGroupDao;
    @Autowired
    private BankSysDao bankSysDao;

    public void save(BankGroup m) {
        this.bankGroupDao.save(m);
    }

    public int update(BankGroup m) {
        return this.bankGroupDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.bankGroupDao.delete(ids);
    }

    public BankGroup find(Integer id) {
        return (BankGroup) this.bankGroupDao.find(id);
    }

    public BankGroup findByName(String bankName) {
        return this.bankGroupDao.findByName(bankName);
    }

    public List<BankGroup> list(Page p) {
        return this.bankGroupDao.list(p);
    }

    public List<BankSys> listBanks(Integer id) {
        List<Integer> ids = this.bankGroupDao.listBankIds(id);
        return this.bankSysDao.listByIds(ids);
    }

    public List<BankSys> listBanksNot(Integer id, Page page) {
        List<Integer> ids = this.bankGroupDao.listBankIds(id);
        return this.bankSysDao.listByIdsNot(ids, page);
    }

    public void deleteBankList(Integer id, List<Integer> bankIds) {
        this.bankGroupDao.deleteBankList(id, bankIds);
    }

    public void addBankList(Integer id, List<Integer> bankIds) {
        this.bankGroupDao.addBankList(id, bankIds);
    }
}
