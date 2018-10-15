package com.hs3.service.sys;

import com.hs3.dao.sys.SysServiceDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysServiceService")
public class SysServiceService {
    @Autowired
    private SysServiceDao sysServiceDao;

    public void save(SysService m) {
        this.sysServiceDao.save(m);
    }

    public SysService findOpen(Integer userMark) {
        List<SysService> list = listOpen(userMark);
        if (list.isEmpty()) {
            list = listOpen(null);
        }
        return list.isEmpty() ? null : (SysService) list.get(0);
    }

    public List<SysService> listOpen(Integer userMark) {
        SysService m = new SysService();

        m.setStatus(0);
        m.setUserMark(userMark);

        return listByCond(m, null);
    }

    public List<SysService> listByCond(SysService m, Page p) {
        return this.sysServiceDao.listByCond(m, p);
    }

    public List<SysService> list(Page p) {
        return this.sysServiceDao.list(p);
    }

    public List<SysService> listWithOrder(Page p) {
        return this.sysServiceDao.listWithOrder(p);
    }

    public SysService find(Integer id) {
        return (SysService) this.sysServiceDao.find(id);
    }

    public int update(SysService m) {
        return this.sysServiceDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.sysServiceDao.delete(ids);
    }
}
