package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInTimeDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInTime;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInTimeService")
public class BetInTimeService {
    @Autowired
    private BetInTimeDao betInTimeDao;

    public void save(BetInTime m, Map<String, String> map) {
        BigDecimal count = BigDecimal.ZERO;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            if (key.startsWith("rule")) {
                if (!StrUtils.hasEmpty(new Object[]{val})) {
                    BigDecimal prob = new BigDecimal(val);
                    count = count.add(prob);

                    m.setRuleId(Integer.valueOf(Integer.parseInt(key.replace("rule", ""))));
                    m.setProbability(prob);
                    this.betInTimeDao.save(m);
                }
            }
        }
        if (count.compareTo(new BigDecimal("100")) != 0) {
            throw new BaseCheckException("概率相加不得100！");
        }
    }

    public int update(BetInTime m, Map<String, String> map) {
        BetInTime betInTime = (BetInTime) this.betInTimeDao.find(m.getId());
        int i = this.betInTimeDao.delete(betInTime.getStartTime(), betInTime.getEndTime());
        save(m, map);
        return i;
    }

    public List<BetInTime> list(String startTime, String endTime) {
        BetInTime m = new BetInTime();
        m.setStartTime(startTime);
        m.setEndTime(endTime);

        return this.betInTimeDao.listByCond(m, null);
    }

    public void save(BetInTime m) {
        this.betInTimeDao.save(m);
    }

    public List<BetInTime> listByCond(BetInTime m, Page p) {
        return this.betInTimeDao.listByCond(m, p);
    }

    public List<BetInTime> list(Page p) {
        return this.betInTimeDao.list(p);
    }

    public List<BetInTime> listWithOrder(Page p) {
        return this.betInTimeDao.listWithOrder(p);
    }

    public BetInTime find(Integer id) {
        return (BetInTime) this.betInTimeDao.find(id);
    }

    public int update(BetInTime m) {
        return this.betInTimeDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.betInTimeDao.delete(ids);
    }
}
