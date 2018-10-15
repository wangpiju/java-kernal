package com.hs3.service.user;

import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.ManagerDao;
import com.hs3.dao.user.UserTokenDao;
import com.hs3.db.Page;
import com.hs3.entity.users.Manager;
import com.hs3.utils.StrUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("managerService")
public class ManagerService {
    public static final String LOGIN_TYPE = "LOGIN_TYPE";
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private UserTokenDao userTokenDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    public Manager updateLogin(String account, String password, String code, String sessionId) {
        Manager manager = this.managerDao.findByAccount(account);
        if (manager == null) {
            return manager;
        }
        if (!password.equals(StrUtils.MD5(manager.getPassword() + code))) {
            manager = null;
        } else {
            this.managerDao.setSessionId(account, sessionId);
        }
        return manager;
    }

    public List<Manager> list(Page p) {
        return this.managerDao.list(p);
    }

    public void save(Manager m) {
        this.managerDao.save(m);
    }

    public int update(Manager m) {
        return this.managerDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.managerDao.delete(ids);
    }

    public Manager find(String id) {
        return (Manager) this.managerDao.find(id);
    }

    public boolean updatePassword(String account, String old, String password) {
        return this.managerDao.updatePassword(account, old, password) > 0;
    }

    public String getSessionId(String account) {
        return this.managerDao.getSessionId(account);
    }

    public String getAuthKey(String account) {
        return this.managerDao.getAuthKey(account);
    }

    public int updateAuthKey(String account, String key) {
        return this.managerDao.updateAuthKey(account, key);
    }
}
