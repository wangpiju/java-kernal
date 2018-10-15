package com.hs3.service.user;

import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.user.DaliyWagesDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.DaliyWages;
import com.hs3.entity.users.User;
import com.hs3.utils.DateUtils;
import com.hs3.utils.IdBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("daliyWagesService")
public class DaliyWagesService {
    private static final Logger log = LoggerFactory.getLogger(DaliyWagesService.class);
    @Autowired
    private DaliyWagesDao daliyWagesDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;

    public void save(DaliyWages m) {
        this.daliyWagesDao.save(m);
    }

    public List<DaliyWages> list(Page p) {
        return this.daliyWagesDao.list(p);
    }

    public List<DaliyWages> listWithOrder(Page p) {
        return this.daliyWagesDao.listWithOrder(p);
    }

    public DaliyWages find(Integer id) {
        return (DaliyWages) this.daliyWagesDao.find(id);
    }

    public int update(DaliyWages m) {
        return this.daliyWagesDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.daliyWagesDao.delete(ids);
    }

    public void updateRootDaliyWages(String account, Date beginTime, Date endTime, int validAccountCount) {
        User user = this.userDao.findByAccountMaster(account);


        FinanceChange m = new FinanceChange();
        m.setChangeUser(account);
        List<FinanceChange> financeList = this.financeChangeDao.listByCond(m, endTime, DateUtils.addDay(endTime, 1), true, new String[]{"28"}, null);
        if (!financeList.isEmpty()) {
            return;
        }
        List<DaliyWages> list = this.daliyWagesDao.list(null);

        DaliyWages dwSelf = this.daliyWagesDao.findTeam(account, validAccountCount, beginTime, endTime);

        DaliyWages daliyWagesSelf = findDaliyWagesLevel(list, dwSelf.getBetAmount(), dwSelf.getValidAccountCount().intValue());

        updateDaliyWages(user, beginTime, endTime, validAccountCount, list, dwSelf, daliyWagesSelf);
    }

    private void updateDaliyWages(User user, Date beginTime, Date endTime, int validAccountCount, List<DaliyWages> list, DaliyWages dwSelf, DaliyWages daliyWagesSelf) {
        if (dwSelf.getBetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        BigDecimal lowerDaliyWages = BigDecimal.ZERO;

        List<User> lowerUserList = this.userDao.listDailyWagesLower(user.getAccount());
        for (User lowerUser : lowerUserList) {
            DaliyWages dwLower = this.daliyWagesDao.findTeam(lowerUser.getAccount(), validAccountCount, beginTime, endTime);

            DaliyWages daliyWagesLower = findDaliyWagesLevel(list, dwLower.getBetAmount(), dwLower.getValidAccountCount().intValue());

            lowerDaliyWages = lowerDaliyWages.add(dwLower.getBetAmount().multiply(daliyWagesLower.getRate()).divide(new BigDecimal("100")));
            if (dwLower.getBetAmount().compareTo(BigDecimal.ZERO) > 0) {
                lowerUser = this.userDao.findByAccountMaster(lowerUser.getAccount());
                updateDaliyWages(lowerUser, beginTime, endTime, validAccountCount, list, dwLower, daliyWagesLower);
            }
        }
        BigDecimal daliyWages = dwSelf.getBetAmount().multiply(daliyWagesSelf.getRate()).divide(new BigDecimal("100")).subtract(lowerDaliyWages).setScale(4, 1);

        log.info("updateRootDaliyWages " + user.getAccount() + "{" + dwSelf + daliyWagesSelf + daliyWages + "+" + lowerDaliyWages + "}");
        if (daliyWages.compareTo(BigDecimal.ZERO) > 0) {
            Date changeTime = DateUtils.toDateNull(DateUtils.format(DateUtils.addDay(endTime, 1), "yyyy-MM-dd") + " " + 4 + ":00:00");

            FinanceChange financeChange = new FinanceChange();
            financeChange.setAccountChangeTypeId(Integer.valueOf(28));
            financeChange.setTest(user.getTest());
            financeChange.setBalance(user.getAmount().add(daliyWages));
            financeChange.setChangeAmount(daliyWages);
            financeChange.setChangeTime(changeTime);
            financeChange.setChangeUser(user.getAccount());
            financeChange.setFinanceId(IdBuilder.getId("D", 3));
            financeChange.setStatus(Integer.valueOf(2));
            financeChange.setRemark(null);
            this.financeChangeDao.save(financeChange);


            this.userDao.updateAmount(user.getAccount(), daliyWages);
        }
    }

    private DaliyWages findDaliyWagesLevel(List<DaliyWages> list, BigDecimal betAmount, int validAccount) {
        for (DaliyWages daliyWages : list) {
            if ((betAmount.compareTo(daliyWages.getBetAmount()) >= 0) && (validAccount >= daliyWages.getValidAccountCount().intValue())) {
                return daliyWages;
            }
        }
        return null;
    }
}
