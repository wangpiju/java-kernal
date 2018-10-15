package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetBackupDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.BetBackup;
import com.hs3.entity.users.User;
import com.hs3.models.CommonModel;
import com.hs3.models.lotts.AdminBetDetail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betBackupService")
public class BetBackupService {
    @Autowired
    private BetBackupDao backupDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BetDao betDao;

    public String findContentNext(String id, Integer i) {
        return this.backupDao.findContentNext(id, i);
    }

    public List<BetBackup> adminList(Page p, CommonModel m) {
        return this.backupDao.adminList(p, m);
    }

    public AdminBetDetail showBetDetail(String id, String account) {
        AdminBetDetail adminBetDetail = new AdminBetDetail();
        User user = this.userDao.findByAccount(account);
        adminBetDetail.setRebateRatio(user.getRebateRatio());
        Bet bet = this.betDao.findShort(id);
        adminBetDetail.setSeasonId(bet.getSeasonId());
        adminBetDetail.setCreateTime(bet.getCreateTime());
        adminBetDetail.setAmount(bet.getAmount());
        adminBetDetail.setContent(bet.getContent());
        adminBetDetail.setLotteryName(bet.getLotteryName());
        adminBetDetail.setBetCount(bet.getBetCount());
        adminBetDetail.setPrice(bet.getPrice());
        adminBetDetail.setUnit(bet.getUnit());
        adminBetDetail.setOpenNum(bet.getOpenNum());
        adminBetDetail.setBonusType(bet.getBonusType());
        adminBetDetail.setStatus(bet.getStatus());
        adminBetDetail.setWin(bet.getWin());
        adminBetDetail.setId(bet.getId());
        adminBetDetail.setAccount(bet.getAccount());
        adminBetDetail.setPlayName(bet.getPlayName());
        return adminBetDetail;
    }
}
