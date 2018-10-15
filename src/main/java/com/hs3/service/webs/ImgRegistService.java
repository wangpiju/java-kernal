package com.hs3.service.webs;

import com.hs3.dao.webs.ImgRegistDao;
import com.hs3.db.Page;
import com.hs3.entity.webs.ImgRegist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImgRegistService {
    @Autowired
    private ImgRegistDao imgRegistDao;

    public List<ImgRegist> list(Page p) {
        return this.imgRegistDao.list(p);
    }

    public List<ImgRegist> listByStats(int status) {
        return this.imgRegistDao.listByStats(status);
    }

    public int delete(List<Integer> ids) {
        return this.imgRegistDao.delete(ids);
    }

    public void save(ImgRegist m) {
        this.imgRegistDao.save(m);
    }

    public int update(ImgRegist m) {
        return this.imgRegistDao.update(m);
    }

    public ImgRegist find(Integer id) {
        return (ImgRegist) this.imgRegistDao.find(id);
    }
}
