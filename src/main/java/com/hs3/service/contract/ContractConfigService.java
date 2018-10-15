package com.hs3.service.contract;

import com.hs3.dao.contract.ContractConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.contract.ContractConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contractConfigService")
public class ContractConfigService {
    @Autowired
    private ContractConfigDao contractConfigDao;

    public int save(ContractConfig m) {
        return this.contractConfigDao.save(m);
    }

    public List<ContractConfig> list(Page p) {
        return this.contractConfigDao.list(p);
    }

    public Object find(Integer id) {
        return this.contractConfigDao.find(id);
    }

    public int delete(List<Integer> ids) {
        return this.contractConfigDao.delete(ids);
    }

    public int update(ContractConfig m) {
        return this.contractConfigDao.update(m);
    }

    public ContractConfig findEntity() {
        return this.contractConfigDao.findEntity();
    }
}
