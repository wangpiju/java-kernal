package com.hs3.service.contract;

import com.hs3.dao.contract.ContractBadrecordDao;
import com.hs3.db.Page;
import com.hs3.entity.contract.ContractBadrecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contractBadrecordService")
public class ContractBadrecordService {
    @Autowired
    private ContractBadrecordDao contractBadrecordDao;

    public void save(ContractBadrecord m) {
        this.contractBadrecordDao.save(m);
    }

    public List<ContractBadrecord> findEntityByAccount(String account) {
        return this.contractBadrecordDao.findEntityByAccount(account);
    }

    public List<ContractBadrecord> list(Page p) {
        return this.contractBadrecordDao.list(p);
    }

    public List<ContractBadrecord> listByCondition(ContractBadrecord m, Page p) {
        return this.contractBadrecordDao.listByCondition(m, p);
    }

    public boolean isExistRecord(Date endDate) {
        return this.contractBadrecordDao.isExistRecord(endDate);
    }

    public boolean deleteBadrecordByDate(Date endDate) {
        return this.contractBadrecordDao.deleteBadrecordByDate(endDate);
    }
}
