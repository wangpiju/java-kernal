package com.hs3.service.lotts;

import com.hs3.dao.lotts.BonusGroupDao;
import com.hs3.dao.lotts.BonusGroupDetailsDao;
import com.hs3.dao.lotts.LotteryDao;
import com.hs3.dao.lotts.PlayerDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.lotts.BonusGroupDetails;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.Player;
import com.hs3.lotts.LotteryBase;
import com.hs3.lotts.LotteryFactory;
import com.hs3.lotts.PlayerBase;
import com.hs3.models.lotts.PlayerBonus;
import com.hs3.models.lotts.PlayerModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("playerService")
public class PlayerService {
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private BonusGroupDao bonusGroupDao;
    @Autowired
    private BonusGroupDetailsDao bonusGroupDetailsDao;

    public List<Player> list(Page p) {
        return this.playerDao.listWithOrder(p);
    }

    public int delete(Integer id) {
        return this.playerDao.delete(id);
    }

    public int delete(List<Integer> ids) {
        return this.playerDao.delete(ids);
    }

    public Player find(Integer id) {
        return (Player) this.playerDao.find(id);
    }

    public int update(Player m) {
        return this.playerDao.update(m);
    }

    public List<Player> listByLotteryId(String id) {
        return this.playerDao.listByLotteryId(id);
    }

    public List<PlayerBonus> listFullByLotteryIdAndGroupId(String lotteryId, Integer groupId) {
        return this.playerDao.listFullByLotteryIdAndGroupId(lotteryId, groupId);
    }

    public void saveNew() {
        List<Lottery> lotteryList = this.lotteryDao.list(null);

        List<BonusGroup> bonusGroup = this.bonusGroupDao.list(null);
    
    
    /*Iterator localIterator2;
    for (Iterator localIterator1 = lotteryList.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      Lottery lottery = (Lottery)localIterator1.next();
      LotteryBase lott = LotteryFactory.getInstance(lottery.getGroupName());
      
      localIterator2 = lott.getPlayers().iterator(); continue;PlayerBase p = (PlayerBase)localIterator2.next();
      if (this.playerDao.findByIdAndLotteryIdCount(p.getId(), lottery.getId()).intValue() == 0)
      {
        Player player = new Player();
        player.setId(p.getId());
        player.setTitle(p.getTitle());
        player.setLotteryId(lottery.getId());
        player.setSaleStatus(Integer.valueOf(0));
        player.setRemark(p.getRemark());
        player.setExample(p.getExample());
        this.playerDao.save(player);
        for (BonusGroup b : bonusGroup)
        {
          BonusGroupDetails bgd = new BonusGroupDetails();
          bgd.setBonusGroupId(b.getId());
          bgd.setLotteryId(lottery.getId());
          bgd.setId(p.getId());
          bgd.setBonus(p.getBonus());
          bgd.setBonusRatio(b.getBonusRatio());
          bgd.setRebateRatio(b.getRebateRatio());
          this.bonusGroupDetailsDao.save(bgd);
        }
      }
    }*/

        for (Iterator iterator = lotteryList.iterator(); iterator.hasNext(); ) {
            Lottery lottery = (Lottery) iterator.next();
            LotteryBase lott = LotteryFactory.getInstance(lottery.getGroupName());
            for (Iterator iterator1 = lott.getPlayers().iterator(); iterator1.hasNext(); ) {
                PlayerBase p = (PlayerBase) iterator1.next();
                if (playerDao.findByIdAndLotteryIdCount(p.getId(), lottery.getId()).intValue() == 0) {
                    Player player = new Player();
                    player.setId(p.getId());
                    player.setTitle(p.getTitle());
                    player.setLotteryId(lottery.getId());
                    player.setSaleStatus(Integer.valueOf(0));
                    player.setRemark(p.getRemark());
                    player.setExample(p.getExample());
                    playerDao.save(player);
                    BonusGroupDetails bgd;
                    for (Iterator iterator2 = bonusGroup.iterator(); iterator2.hasNext(); bonusGroupDetailsDao.save(bgd)) {
                        BonusGroup b = (BonusGroup) iterator2.next();
                        bgd = new BonusGroupDetails();
                        bgd.setBonusGroupId(b.getId());
                        bgd.setLotteryId(lottery.getId());
                        bgd.setId(p.getId());
                        bgd.setBonus(p.getBonus());
                        bgd.setBonusRatio(b.getBonusRatio());
                        bgd.setRebateRatio(b.getRebateRatio());
                    }

                }
            }

        }


    }

    public List<Player> listByLotteryIdAndStatus(String lotteryId, int status) {
        return this.playerDao.listByLotteryIdAndStatus(lotteryId, status);
    }

    public Integer updateAll(PlayerModel model, String lotteryId) {
        return this.playerDao.updateAll(model, lotteryId);
    }

    public Integer getStatus(String playId, String lotteryId) {
        return this.playerDao.getStatus(playId, lotteryId);
    }
}
