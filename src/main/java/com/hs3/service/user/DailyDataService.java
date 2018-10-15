package com.hs3.service.user;

import com.hs3.dao.user.DailyDataDao;
import com.hs3.db.Page;
import com.hs3.entity.users.DailyData;
import com.hs3.utils.sys.WebDateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dailyDataService")
public class DailyDataService {
    @Autowired
    private DailyDataDao dailyDataDao;

    public List<DailyData> listByCond(DailyData dailyData, String parentList, Date begin, Date end, Page page) {
        if (begin != null) {
            begin = WebDateUtils.getDayBeginTime(begin);
        }
        if (end != null) {
            end = WebDateUtils.getDayEndTime(end);
        }
        return this.dailyDataDao.listByCond(dailyData, parentList, begin, end, page);
    }

    public void save(DailyData m) {
        this.dailyDataDao.save(m);
    }

    public List<DailyData> list(Page p) {
        return this.dailyDataDao.list(p);
    }

    public List<DailyData> listWithOrder(Page p) {
        return this.dailyDataDao.listWithOrder(p);
    }

    public DailyData find(Integer id) {
        return (DailyData) this.dailyDataDao.find(id);
    }

    public int update(DailyData m) {
        return this.dailyDataDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.dailyDataDao.delete(ids);
    }
}
