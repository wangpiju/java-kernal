package com.hs3.service.bank;

import com.hs3.dao.bank.BankNameDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankName;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankNameService")
public class BankNameService {
    @Autowired
    private BankNameDao bankNameDao;

    public void save(BankName m) {
        this.bankNameDao.save(m);
    }

    public int update(BankName m) {
        return this.bankNameDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.bankNameDao.delete(ids);
    }

    public BankName find(Integer id) {
        return (BankName) this.bankNameDao.find(id);
    }

    public List<BankName> list(Page p) {
        return this.bankNameDao.listWithOrder(p);
    }

    public List<BankName> findByStatus(Integer depositStatus, Integer rechargeStatus) {
        return this.bankNameDao.findByStatus(depositStatus, rechargeStatus);
    }

    public List<BankName> findByAccount(String account) {
        return this.bankNameDao.findByAccount(account);
    }

    public List<Map<String, Object>> listBankAll() {
        return this.bankNameDao.listBankAll();
    }

    public List<BankName> listByCodes(Set<String> codes) {
        return this.bankNameDao.listByCodes(codes);
    }

    public List<BankName> bankByBnTypeList(Integer bnType) {
        return this.bankNameDao.bankByBnTypeList(bnType);
    }
}
