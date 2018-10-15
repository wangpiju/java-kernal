package com.hs3.service.lotts;

import com.hs3.dao.lotts.*;
import com.hs3.db.Page;
import com.hs3.entity.lotts.*;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.utils.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service("lotteryService")
public class LotteryService {
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private LotterySeasonDao lotterySeasonDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private BonusGroupDao bonusGroupDao;
    @Autowired
    private BonusGroupDetailsDao bonusGroupDetailsDao;
    @Autowired
    private LotteryCloseRuleDao lotteryCloseRuleDao;
    @Autowired
    private LotteryCrawlerConfigDao lotteryCrawlerConfigDao;
    @Autowired
    private LotteryLockDao lotteryLockDao;
    @Autowired
    private LotterySaleRuleDao lotterySaleRuleDao;
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private LotterySeasonWeightDao lotterySeasonWeightDao;
    @Autowired
    private SeasonForDateDao seasonForDateDao;
    @Autowired
    private LotteryJobService lotteryJobService;

    public List<Lottery> list(Page p) {
        return this.lotteryDao.listByOrder(p);
    }

    public int deleteRelevance(String id) {
        this.bonusGroupDetailsDao.deleteByLotteryId(id);
        this.lotteryCloseRuleDao.deleteByLotteryId(id);
        this.lotteryCrawlerConfigDao.deleteByLotteryId(id);
        this.lotteryLockDao.deleteByLotteryId(id);
        this.lotterySaleTimeDao.deleteByLotteryId(id);
        this.lotterySeasonDao.deleteByLotteryId(id);
        this.lotterySeasonWeightDao.deleteByLotteryId(id);
        this.playerDao.deleteByLotteryId(id);
        this.seasonForDateDao.deleteByLotteryId(id);
        this.lotteryDao.delete(id);
        List<LotterySaleRule> lotterySaleRules = this.lotterySaleRuleDao.list(id, null);
        if (lotterySaleRules.size() > 0) {
            for (LotterySaleRule lotterySaleRule : lotterySaleRules) {
                this.lotteryJobService.deleteJob(lotterySaleRule);
            }
        }
        this.lotterySaleRuleDao.deleteByLotteryId(id);
        return 0;
    }

    public int delete(List<String> ids) {
        return this.lotteryDao.delete(ids);
    }

    public Lottery find(String id) {
        if (StrUtils.hasEmpty(id)) {
            return null;
        }
        return this.lotteryDao.find(id);
    }

    public int update(Lottery m) {
        return this.lotteryDao.update(m);
    }

    public boolean updateStatus(String lotteryId, Integer Status) {
        return this.lotteryDao.updateStatus(lotteryId, Status);
    }

    public LotterySeason getLast(String lotteryId) {
        List<LotterySeason> list = this.lotterySeasonDao.getLast(lotteryId, 1);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<LotterySeason> getLast(String lotteryId, int num) {
        return this.lotterySeasonDao.getLast(lotteryId, num);
    }

    public List<LotterySeason> getALLLast(int num) {
        return this.lotterySeasonDao.getALLLast(num);
    }

    public void save(Lottery m) {
        lotteryDao.save(m);
        LotteryBase lott = LotteryFactory.getInstance(m.getGroupName());
        List<BonusGroup> bonusGroup = bonusGroupDao.list(null);
        for (Iterator iterator = lott.getPlayers().iterator(); iterator.hasNext(); ) {
            PlayerBase p = (PlayerBase) iterator.next();
            Player player = new Player();
            player.setId(p.getId());
            player.setTitle(p.getTitle());
            player.setLotteryId(m.getId());
            player.setSaleStatus(0);
            player.setRemark(p.getRemark());
            player.setExample(p.getExample());
            playerDao.save(player);
            BonusGroupDetails bgd;
            for (Iterator iterator1 = bonusGroup.iterator(); iterator1.hasNext(); bonusGroupDetailsDao.save(bgd)) {
                BonusGroup b = (BonusGroup) iterator1.next();
                bgd = new BonusGroupDetails();
                bgd.setBonusGroupId(b.getId());
                bgd.setLotteryId(m.getId());
                bgd.setId(p.getId());
                bgd.setBonus(p.getBonus());
                bgd.setBonusRatio(b.getBonusRatio());
                bgd.setRebateRatio(b.getRebateRatio());
            }

        }

    }

    public List<Lottery> getListByGroupName(String groupName) {
        return this.lotteryDao.getListByGroupName(groupName);
    }

    public Lottery getGroupNameByLottery(String lotteryId) {
        return this.lotteryDao.find(lotteryId);
    }

    public List<Lottery> listByStatus(int status) {
        return this.lotteryDao.listByStatusOrderId(status);
    }

    public List<Lottery> listAndOrder(Page p) {
        return this.lotteryDao.listAndOrder(p);
    }

    public List<Lottery> listAndOrderField(String fields) {
        return this.lotteryDao.listAndOrderField(fields);
    }
}
