package com.hs3.service.roles;

import com.hs3.dao.roles.RoleMapJurisdictionDao;
import com.hs3.db.Page;
import com.hs3.entity.roles.RoleMapJurisdiction;
import com.hs3.models.roles.JurisdictionEx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleMapJurService")
public class RoleMapJurService {
    @Autowired
    private RoleMapJurisdictionDao roleMapJurisdictionDao;

    public boolean deleteAndSave(Integer roleId, List<Integer> jurIds) {
        this.roleMapJurisdictionDao.delete(roleId);
        RoleMapJurisdiction rmj = new RoleMapJurisdiction();
        rmj.setRoleId(roleId);
        for (Integer id : jurIds) {
            rmj.setJurisdictionId(id);
            this.roleMapJurisdictionDao.save(rmj);
        }
        return true;
    }

    public void save(Integer roleId, List<Integer> jurList) {
        for (Integer jurId : jurList) {
            this.roleMapJurisdictionDao.save(roleId, jurId);
        }
    }

    public List<JurisdictionEx> listRJEx(Page page, Integer roleId) {
        return this.roleMapJurisdictionDao.listRJEx(page, roleId);
    }
}
