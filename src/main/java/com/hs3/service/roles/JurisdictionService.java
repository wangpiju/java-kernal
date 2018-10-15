package com.hs3.service.roles;

import com.hs3.dao.roles.FirstMenuDao;
import com.hs3.dao.roles.JurisdictionDao;
import com.hs3.dao.roles.RoleMapJurisdictionDao;
import com.hs3.dao.roles.SecondMenuDao;
import com.hs3.db.Page;
import com.hs3.entity.roles.FirstMenu;
import com.hs3.entity.roles.Jurisdiction;
import com.hs3.entity.roles.RoleMapJurisdiction;
import com.hs3.entity.roles.SecondMenu;
import com.hs3.models.roles.JurisdictionEx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jurisdictionService")
public class JurisdictionService {
    @Autowired
    private RoleMapJurisdictionDao roleMapJurisdictionDao;
    @Autowired
    private JurisdictionDao jurisdictionDao;
    @Autowired
    private FirstMenuDao firstMenuDao;
    @Autowired
    private SecondMenuDao secondMenuDao;

    public List<Jurisdiction> listByRoleId(Integer roleId, Page p) {
        if (roleId != null) {
            List<Integer> ids = this.roleMapJurisdictionDao.listJurisdictionIds(roleId, p);
            if ((ids == null) || (ids.size() == 0)) {
                return new ArrayList();
            }
            return this.jurisdictionDao.listByIds(ids);
        }
        return this.jurisdictionDao.list(p);
    }

    public List<Jurisdiction> listNotByRoleId(Integer roleId, Page p) {
        if (roleId != null) {
            List<Integer> ids = this.roleMapJurisdictionDao.listJurisdictionIds(roleId, null);
            if ((ids == null) || (ids.size() == 0)) {
                return this.jurisdictionDao.list(p);
            }
            return this.jurisdictionDao.listNoByIds(ids);
        }
        return this.jurisdictionDao.list(p);
    }

    public void saveRoleMapJurisdiction(RoleMapJurisdiction jur) {
        this.roleMapJurisdictionDao.save(jur);
    }

    public boolean delete(List<Integer> ids) {
        return this.jurisdictionDao.delete(ids) > 0;
    }

    public List<Jurisdiction> listAndOrder(Page p) {
        return this.jurisdictionDao.listAndOrder(p);
    }

    public List<JurisdictionEx> listEx(Page p) {
        return this.jurisdictionDao.listEx(p);
    }

    public void save(Jurisdiction m) {
        this.jurisdictionDao.save(m);
    }

    public int update(Jurisdiction m) {
        return this.jurisdictionDao.update(m);
    }

    public JurisdictionEx findEx(String id) {
        return this.jurisdictionDao.findEx(id);
    }

    public List<FirstMenu> listByFirstMenu() {
        return this.firstMenuDao.list();
    }

    public List<SecondMenu> listBySecondMenu(Integer firstMenuId) {
        return this.secondMenuDao.list(firstMenuId);
    }
}
