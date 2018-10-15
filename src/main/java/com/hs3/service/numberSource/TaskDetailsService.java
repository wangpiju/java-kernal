package com.hs3.service.numberSource;

import com.hs3.dao.numberSource.TaskDetailsDao;
import com.hs3.db.Page;
import com.hs3.entity.numberSource.Triggers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskDetailsService")
public class TaskDetailsService {
    @Autowired
    private TaskDetailsDao taskDetailsDao;

    public List<Triggers> list(Page p, String group, String groupName) {
        return this.taskDetailsDao.list(p, group, groupName);
    }

    public Triggers find(Integer id) {
        return (Triggers) this.taskDetailsDao.find(id);
    }

    public List<Triggers> listByGroupIdAndLotteryId(Integer groupId, String lotteryId) {
        return this.taskDetailsDao.listByGroupIdAndLotteryId(groupId, lotteryId);
    }
}
