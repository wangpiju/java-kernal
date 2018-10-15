package com.hs3.service.roles;

import com.hs3.dao.roles.SecondMenuDao;
import com.hs3.db.Page;
import com.hs3.entity.roles.SecondMenu;
import com.hs3.models.roles.SecondMenuEx;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("secondMenuService")
public class SecondMenuService {
    @Autowired
    private SecondMenuDao secondMenuDao;

    public List<SecondMenu> list(Integer firstMenuId) {
        return this.secondMenuDao.list(firstMenuId);
    }

    public void save(SecondMenu m) {
        this.secondMenuDao.save(m);
    }

    public int update(SecondMenu m) {
        return this.secondMenuDao.update(m);
    }

    public List<SecondMenu> list(Page p) {
        return this.secondMenuDao.list(p);
    }

    public List<SecondMenuEx> listEx(Page p) {
        return this.secondMenuDao.listEx(p);
    }

    public boolean delete(List<Integer> ids) {
        return this.secondMenuDao.delete(ids) > 0;
    }

    public SecondMenu find(Integer id) {
        return (SecondMenu) this.secondMenuDao.find(id);
    }

    public SecondMenuEx findEx(Integer id) {
        return this.secondMenuDao.findEx(id);
    }
}
