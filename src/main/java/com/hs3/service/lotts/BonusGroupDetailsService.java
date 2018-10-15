package com.hs3.service.lotts;

import com.hs3.dao.lotts.BonusGroupDetailsDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusGroupDetails;
import com.hs3.models.BonusGroupDetailsModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bonusGroupDetailsService")
public class BonusGroupDetailsService {
    private static Logger logger = LoggerFactory.getLogger(BonusGroupDetailsService.class);
    @Autowired
    private BonusGroupDetailsDao bonusGroupDetailsDao;
    private static Map<String, BonusGroupDetails> maps = new HashMap();

    private String getKey(String playId, String lotteryId, Integer bonusGroupId) {
        return String.format("%s_%s_%s", new Object[]{playId, lotteryId, bonusGroupId});
    }

    public List<BonusGroupDetails> list(Page p) {
        return this.bonusGroupDetailsDao.list(p);
    }

    public BonusGroupDetails find(String playId, String lotteryId, Integer bonusGroupId) {
        String key = getKey(playId, lotteryId, bonusGroupId);
        if (!maps.containsKey(key)) {
            List<BonusGroupDetails> list = this.bonusGroupDetailsDao.list(null);
            for (BonusGroupDetails b : list) {
                String k = getKey(b.getId(), b.getLotteryId(), b.getBonusGroupId());
                maps.put(k, b);
            }
        }
        BonusGroupDetails rel = (BonusGroupDetails) maps.get(key);
        if (rel == null) {
            logger.error("奖金组详情没有数据：" + key);
        }
        return rel;
    }

    public void save(BonusGroupDetails m) {
        this.bonusGroupDetailsDao.save(m);
        String k = getKey(m.getId(), m.getLotteryId(), m.getBonusGroupId());
        maps.put(k, m);
    }

    public List<BonusGroupDetails> listByGroupIdAndLotteryId(Integer groupId, String lotteryId) {
        return this.bonusGroupDetailsDao.listByGroupIdAndLotteryId(groupId, lotteryId);
    }

    public List<BonusGroupDetails> listByPlayIdAndGroupId(Integer groupId, String[] playIds) {
        return this.bonusGroupDetailsDao.listByPlayIdAndGroupId(groupId, playIds);
    }

    public List<BonusGroupDetails> listByLotteryId(String lotteryId) {
        return this.bonusGroupDetailsDao.listByLotteryId(lotteryId);
    }

    public int updateAll(BonusGroupDetailsModel model) {
        int i = this.bonusGroupDetailsDao.updateAll(model).intValue();
        if (i > 0) {
            for (BonusGroupDetails m : model.getDetails()) {
                m.setLotteryId(model.getLotteryId());
                m.setBonusGroupId(model.getBonusGroupId());

                String k = getKey(m.getId(), m.getLotteryId(), m.getBonusGroupId());
                maps.put(k, m);
            }
        }
        return i;
    }
}
