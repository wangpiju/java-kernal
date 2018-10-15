package com.hs3.service.lotts;

import com.hs3.dao.lotts.BonusGroupDao;
import com.hs3.dao.lotts.BonusGroupDetailsDao;
import com.hs3.dao.lotts.LotteryDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.lotts.BonusGroupDetails;
import com.hs3.entity.lotts.Lottery;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bonusGroupService")
public class BonusGroupService {
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private BonusGroupDao bonusGroupDao;
    @Autowired
    private BonusGroupDetailsDao bonusGroupDetailsDao;

    public List<BonusGroup> list(Page p) {
        return this.bonusGroupDao.list(p);
    }

    public int delete(Integer id) {
        return this.bonusGroupDao.delete(id);
    }

    public BonusGroup find(Integer id) {
        return (BonusGroup) this.bonusGroupDao.find(id);
    }

    public int update(BonusGroup m) {
        return this.bonusGroupDao.update(m);
    }

    public void save(BonusGroup m) {
        /**jd-gui
         this.bonusGroupDao.save(m);

         List<Lottery> lotteryList = this.lotteryDao.list(null);
         Iterator localIterator2;
         for (Iterator localIterator1 = lotteryList.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         Lottery lottery = (Lottery)localIterator1.next();
         LotteryBase lott = LotteryFactory.getInstance(lottery.getGroupName());

         localIterator2 = lott.getPlayers().iterator(); continue;PlayerBase p = (PlayerBase)localIterator2.next();


         BonusGroupDetails bgd = new BonusGroupDetails();
         bgd.setId(p.getId());
         bgd.setLotteryId(lottery.getId());
         bgd.setBonusGroupId(m.getId());
         bgd.setBonus(p.getBonus());
         bgd.setBonusRatio(m.getBonusRatio());
         bgd.setRebateRatio(m.getRebateRatio());
         this.bonusGroupDetailsDao.save(bgd);
         }*/

        bonusGroupDao.save(m);
        List<Lottery> lotteryList = lotteryDao.list(null);
        for (Iterator iterator = lotteryList.iterator(); iterator.hasNext(); ) {
            Lottery lottery = (Lottery) iterator.next();
            LotteryBase lott = LotteryFactory.getInstance(lottery.getGroupName());
            BonusGroupDetails bgd;
            for (Iterator iterator1 = lott.getPlayers().iterator(); iterator1.hasNext(); bonusGroupDetailsDao.save(bgd)) {
                PlayerBase p = (PlayerBase) iterator1.next();
                bgd = new BonusGroupDetails();
                bgd.setId(p.getId());
                bgd.setLotteryId(lottery.getId());
                bgd.setBonusGroupId(m.getId());
                bgd.setBonus(p.getBonus());
                bgd.setBonusRatio(m.getBonusRatio());
                bgd.setRebateRatio(m.getRebateRatio());
            }

        }

    }
}
