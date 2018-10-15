package com.hs3.service.article;

import com.hs3.dao.article.MenuDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("menuService")
public class MenuService {
    @Autowired
    private MenuDao menuDao;

    public List<Menu> listByCond(Menu m, Page page) {
        return this.menuDao.listByCond(m, page);
    }

    public void save(Menu m) {
        this.menuDao.save(m);
    }

    public List<Menu> listWithOrder(Page p) {
        return this.menuDao.listWithOrder(p);
    }

    public Menu find(Integer id) {
        return (Menu) this.menuDao.find(id);
    }

    public int update(Menu m) {
        return this.menuDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.menuDao.delete(ids);
    }
}
