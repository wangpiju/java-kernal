package com.hs3.service.webs;

import com.hs3.dao.webs.ImgDao;
import com.hs3.db.Page;
import com.hs3.entity.webs.Img;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImgService {
    @Autowired
    private ImgDao imgDao;

    public List<Img> list(Page p) {
        return this.imgDao.list(p);
    }

    public List<Img> listByShow(int status) {
        return this.imgDao.listByShow(status);
    }

    public int delete(List<Integer> ids) {
        return this.imgDao.delete(ids);
    }

    public void save(Img m) {
        this.imgDao.save(m);
    }

    public int update(Img m) {
        return this.imgDao.update(m);
    }

    public Img find(Integer id) {
        return (Img) this.imgDao.find(id);
    }
}
