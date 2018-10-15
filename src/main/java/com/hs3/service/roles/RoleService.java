package com.hs3.service.roles;

import com.hs3.dao.roles.JurisdictionDao;
import com.hs3.dao.roles.RoleDao;
import com.hs3.dao.roles.RoleMapJurisdictionDao;
import com.hs3.db.Page;
import com.hs3.entity.roles.Role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private JurisdictionDao jurisdictionDao;
    @Autowired
    private RoleMapJurisdictionDao roleMapJurisdictionDao;

    public List<String> getPaths(Integer roleId) {
        List<Integer> jurisdictionIds = this.roleMapJurisdictionDao.listJurisdictionIds(roleId, null);
        if ((jurisdictionIds == null) || (jurisdictionIds.size() == 0)) {
            return null;
        }
        return this.jurisdictionDao.listPathByIds(jurisdictionIds);
    }

    public List<Role> list() {
        return this.roleDao.list(null);
    }

    public List<Role> list(Page p) {
        return this.roleDao.list(p);
    }

    public boolean delete(List<Integer> ids) {
        return this.roleDao.delete(ids) > 0;
    }

    public Role find(Integer id) {
        return (Role) this.roleDao.find(id);
    }

    public void save(Role m) {
        this.roleDao.save(m);
    }

    public int update(Role m) {
        return this.roleDao.update(m);
    }
}
