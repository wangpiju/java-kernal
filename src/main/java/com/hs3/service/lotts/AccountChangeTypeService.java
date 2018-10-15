package com.hs3.service.lotts;

import com.hs3.dao.lotts.AccountChangeTypeDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.AccountChangeType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountChangeTypeService")
public class AccountChangeTypeService {
    @Autowired
    AccountChangeTypeDao accountChangeTypeDao;

    public List<AccountChangeType> list(Page p) {
        return this.accountChangeTypeDao.list(p);
    }

    public int update(AccountChangeType m) {
        return this.accountChangeTypeDao.update(m);
    }

    public void save(AccountChangeType m) {
        this.accountChangeTypeDao.save(m);
    }

    public Object find(Integer id) {
        return this.accountChangeTypeDao.find(id);
    }

    public List<AccountChangeType> listByType(Integer type) {
        return this.accountChangeTypeDao.listByType(type);
    }
}
