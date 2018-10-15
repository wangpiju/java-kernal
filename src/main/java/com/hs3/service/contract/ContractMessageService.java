package com.hs3.service.contract;

import com.hs3.dao.contract.ContractMessageDao;
import com.hs3.db.Page;
import com.hs3.entity.contract.ContractMessage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contractMessageService")
public class ContractMessageService {
    @Autowired
    private ContractMessageDao contractMessageDao;

    public void save(ContractMessage m) {
        this.contractMessageDao.save(m);
    }

    public int updateEntity(ContractMessage m) {
        return this.contractMessageDao.updateEntity(m);
    }

    public List<ContractMessage> list(Page p) {
        return this.contractMessageDao.list(p);
    }

    public int countByAccount(String account) {
        return this.contractMessageDao.countByAccount(account);
    }

    public List<ContractMessage> listByAccount(String account) {
        return this.contractMessageDao.listByAccount(account);
    }

    public int updateMessageStatus(Integer id) {
        return this.contractMessageDao.updateMessageStatus(id);
    }

    public int updateStatusByAccount(String account) {
        return this.contractMessageDao.updateStatusByAccount(account);
    }
}
