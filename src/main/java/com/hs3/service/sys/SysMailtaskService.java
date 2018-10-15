package com.hs3.service.sys;

import com.hs3.dao.sys.SysMailtaskDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysMailtask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sysMailtaskService")
public class SysMailtaskService {
    @Autowired
    private SysMailtaskDao sysMailtaskDao;

    public List<SysMailtask> updateToSend(int size) {
        List<SysMailtask> mailtaskList = new ArrayList<>();
        for (SysMailtask sysMailtask : listByStatus(0, new Page(1, size))) {
            if (updateByStatus(sysMailtask.getId(), 1, 0) == 1) {
                mailtaskList.add(sysMailtask);
            }
        }
        return mailtaskList;
    }

    public List<SysMailtask> listByStatus(int status, Page p) {
        SysMailtask cond = new SysMailtask();
        cond.setStatus(status);
        return this.sysMailtaskDao.listByCond(cond, p);
    }

    public int updateByStatus(int id, int status, int preStatus) {
        return this.sysMailtaskDao.updateByStatus(id, status, preStatus);
    }

    public void save(SysMailtask m) {
        this.sysMailtaskDao.save(m);
    }

    public List<SysMailtask> list(Page p) {
        return this.sysMailtaskDao.list(p);
    }

    public List<SysMailtask> listWithOrder(Page p) {
        return this.sysMailtaskDao.listWithOrder(p);
    }

    public SysMailtask find(Integer id) {
        return this.sysMailtaskDao.find(id);
    }

    public int update(SysMailtask m) {
        return this.sysMailtaskDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.sysMailtaskDao.delete(ids);
    }
}
