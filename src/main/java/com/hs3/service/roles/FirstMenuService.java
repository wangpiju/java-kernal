package com.hs3.service.roles;

import com.hs3.dao.roles.FirstMenuDao;
import com.hs3.db.Page;
import com.hs3.entity.roles.FirstMenu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("firstMenuService")
public class FirstMenuService {
    @Autowired
    private FirstMenuDao firstMenuDao;

    public List<FirstMenu> list() {
        return this.firstMenuDao.list();
    }

    public void save(FirstMenu m) {
        this.firstMenuDao.save(m);
    }

    public int update(FirstMenu m) {
        return this.firstMenuDao.update(m);
    }

    public List<FirstMenu> list(Page p) {
        return this.firstMenuDao.list(p);
    }

    public boolean delete(List<Integer> ids) {
        return this.firstMenuDao.delete(ids) > 0;
    }

    public FirstMenu find(Integer id) {
        return (FirstMenu) this.firstMenuDao.find(id);
    }
}
