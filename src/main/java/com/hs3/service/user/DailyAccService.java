package com.hs3.service.user;

import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.user.DailyAccDao;
import com.hs3.dao.user.DailyDataDao;
import com.hs3.dao.user.DailyRuleDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Message;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.users.DailyAcc;
import com.hs3.entity.users.DailyData;
import com.hs3.entity.users.DailyRule;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.service.article.MessageService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.IdBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dailyAccService")
public class DailyAccService {
    private static final Logger log = LoggerFactory.getLogger(DailyAccService.class);
    @Autowired
    private MessageService messageService;
    @Autowired
    private DailyAccDao dailyAccDao;
    @Autowired
    private DailyRuleDao dailyRuleDao;
    @Autowired
    private DailyDataDao dailyDataDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;

    public void update(DailyAcc m, String parentAccount) {
        DailyAcc dailyAcc = (DailyAcc) this.dailyAccDao.findLock(m.getId());
        if ((dailyAcc == null) || (!dailyAcc.getParentAccount().equals(parentAccount)) || (dailyAcc.getAccount().equals(parentAccount))) {
            throw new BaseCheckException("[" + dailyAcc.getId() + "]无记录！");
        }
        checkRule(dailyAcc.getRuleId(), m);

        dailyAcc.setRate(m.getRate());
        dailyAcc.setBetAmount(m.getBetAmount());
        dailyAcc.setValidAccountCount(m.getValidAccountCount());
        dailyAcc.setLimitAmount(m.getLimitAmount());
        dailyAcc.setChangeTime(new Date());
        dailyAcc.setLossStatus(m.getLossStatus());

        this.dailyAccDao.update(dailyAcc);
    }

    public void save(DailyAcc m, String parentAccount) {
        User user = this.userDao.findByAccount(m.getAccount());
        if ((user == null) || (!user.getParentAccount().equals(parentAccount)) || (user.getAccount().equals(parentAccount))) {
            throw new BaseCheckException("[" + m.getAccount() + "]不存在！");
        }
        if (this.dailyAccDao.findByAccount(m.getAccount()) != null) {
            throw new BaseCheckException("[" + m.getAccount() + "]已存在契约日薪！");
        }
        User parentUser = this.userDao.findByAccount(parentAccount);
        if (parentUser.getDailyWagesStatus().intValue() != 0) {
            throw new BaseCheckException("未开通日工资，不允许分配日薪！");
        }
        checkRule(parentUser.getDailyRuleId(), m);

        Date now = new Date();
        m.setRuleId(parentUser.getDailyRuleId());
        m.setParentAccount(parentAccount);
        m.setRootAccount(user.getRootAccount());
        m.setParentList(user.getParentList());
        m.setCreateTime(now);
        m.setChangeTime(now);
        m.setStatus(Integer.valueOf(0));

        this.dailyAccDao.save(m);
        this.userDao.updateDailyWagesStatusOpen(m.getAccount(), parentAccount);
        this.userDao.updateDailyRule(m.getAccount(), parentUser.getDailyRuleId());
    }

    private void checkRule(Integer dailyRuleId, DailyAcc m) {
        DailyRule dailyRule = (DailyRule) this.dailyRuleDao.find(dailyRuleId);
        if ((dailyRule.getLimitAmount().compareTo(BigDecimal.ZERO) > 0) &&
                (m.getLimitAmount().compareTo(dailyRule.getLimitAmount()) > 0)) {
            throw new BaseCheckException("日薪额度不允许超过" + dailyRule.getLimitAmount());
        }
        if ((m.getValidAccountCount().intValue() > 0) && (dailyRule.getValidAccountCount().intValue() != 0)) {
            throw new BaseCheckException("该日薪规则不允许填写有效人数！");
        }
        if ((m.getValidAccountCount().intValue() < 0) || (m.getValidAccountCount().intValue() > 3000)) {
            throw new BaseCheckException("有效人数可分配范围0 ~ 3000");
        }
        if ((m.getLossStatus().intValue() == 0) && (dailyRule.getLossStatus().intValue() != 0)) {
            throw new BaseCheckException("该日薪规则不允许填写亏损要求！");
        }
        if ((m.getRate().compareTo(dailyRule.getMinRate()) < 0) || (m.getRate().compareTo(m.getRate()) > 0)) {
            throw new BaseCheckException("该日薪规则要求在" + dailyRule.getMinRate() + "~" + m.getRate() + "之间！");
        }
    }

    public void updateTeamDailyData(String account, Date beginTime, Date endTime, int validAccountCount) {
        if (this.dailyDataDao.count(account, endTime, DateUtils.addDay(endTime, 1)) > 0) {
            log.warn("[" + account + "][" + endTime + "][" + DateUtils.addDay(endTime, 1) + "] had daily data!");
            return;
        }
        Date changeTime = DateUtils.toDateNull(DateUtils.format(DateUtils.addDay(endTime, 1), "yyyy-MM-dd") + " " + 4 + ":00:00");
        User user = this.userDao.findByAccountMaster(account);


        DailyData dailyData = this.dailyDataDao.findTeam(account, validAccountCount, beginTime, endTime);

        DailyAcc dailyAcc = this.dailyAccDao.findByAccount(account);

        updateDailyData(null, user, beginTime, endTime, changeTime, validAccountCount, dailyAcc, dailyData);
    }

    private BigDecimal updateDailyData(BigDecimal parentAmount, User user, Date beginTime, Date endTime, Date changeTime, int validAccountCount, DailyAcc dailyAcc, DailyData dailyData) {
        boolean cond1 = dailyData.getBetAmount().compareTo(dailyAcc.getBetAmount()) >= 0;
        boolean cond2 = dailyData.getValidAccountCount().intValue() >= dailyAcc.getValidAccountCount().intValue();
        boolean cond3 = (dailyAcc.getLossStatus().intValue() == 1) || (dailyData.getBetAmount().compareTo(dailyData.getWinAmount()) > 0);
        boolean condAll = (cond1) && (cond2) && (cond3);
        boolean cond4 = true;
        BigDecimal exactDailyWages = BigDecimal.ZERO;


        BigDecimal dailyWages = condAll ? countDaily(dailyAcc, dailyData) : BigDecimal.ZERO;
        if ((parentAmount != null) && (dailyWages.compareTo(BigDecimal.ZERO) > 0) && (dailyWages.compareTo(parentAmount) > 0)) {
            exactDailyWages = dailyWages;
            dailyWages = BigDecimal.ZERO;
            cond4 = false;
        }
        saveFinanceChange(dailyWages, user, null, changeTime, Integer.valueOf(28));
        saveDailyData(cond1, cond2, cond3, cond4, exactDailyWages, user, dailyData, dailyAcc, dailyWages, changeTime);


        List<DailyAcc> dailyAccChildList = this.dailyAccDao.listByParentAccount(user.getAccount());
        for (DailyAcc dailyAccChild : dailyAccChildList) {
            DailyData dailyDataChild = this.dailyDataDao.findTeam(dailyAccChild.getAccount(), validAccountCount, beginTime, endTime);
            User userChild = this.userDao.findByAccountMaster(dailyAccChild.getAccount());

            BigDecimal dailyWagesChild = updateDailyData(this.userDao.getAmount(user.getAccount()), userChild, beginTime, endTime, changeTime, validAccountCount, dailyAccChild, dailyDataChild);

            saveFinanceChange(BigDecimal.ZERO.subtract(dailyWagesChild), user, userChild, changeTime, Integer.valueOf(40));
        }
        return dailyWages;
    }

    private void saveDailyData(boolean cond1, boolean cond2, boolean cond3, boolean cond4, BigDecimal exactDailyWages, User user, DailyData dailyData, DailyAcc dailyAcc, BigDecimal dailyWages, Date changeTime) {
        String remark = "";
        if (!cond4) {
            remark = remark + "上级不足以支付日工资[" + exactDailyWages + "]";

            Message message = new Message();
            message.setTitle("系统通知");
            message.setSendContent(remark);
            message.setRever(user.getAccount());
            message.setRevStatus(Integer.valueOf(0));
            message.setSender("");
            message.setSendType(Integer.valueOf(0));
            message.setSendStatus(Integer.valueOf(0));
            message.setSendTime(changeTime);

            this.messageService.saveSingle(message);
        }
        if (!cond1) {
            remark = remark + "投注额[" + dailyData.getBetAmount() + "]达不到标准[" + dailyAcc.getBetAmount() + "]";
        }
        if (!cond2) {
            remark = remark + "有效会员[" + dailyData.getValidAccountCount() + "]达不到标准[" + dailyAcc.getValidAccountCount() + "]";
        }
        if (!cond3) {
            remark = remark + "亏损要求中奖金额[" + dailyData.getWinAmount() + "]投注额[" + dailyData.getBetAmount() + "]";
        }
        DailyData dd = new DailyData();
        dd.setAccount(user.getAccount());
        dd.setParentAccount(user.getParentAccount());
        dd.setRootAccount(user.getRootAccount());
        dd.setParentList(user.getParentList());
        dd.setTest(user.getTest());
        dd.setUserMark(user.getUserMark());
        dd.setBetAmount(dailyData.getBetAmount());
        dd.setWinAmount(dailyData.getWinAmount());
        dd.setLossStatus(Integer.valueOf(dailyData.getWinAmount().compareTo(dailyData.getBetAmount()) > 0 ? 0 : 1));
        dd.setValidAccountCount(dailyData.getValidAccountCount());
        dd.setDailyAmount(dailyWages);
        dd.setChangeTime(changeTime);
        dd.setCreateTime(changeTime);
        dd.setRuleLimitAmount(dailyAcc.getLimitAmount());
        dd.setRuleLossStatus(dailyAcc.getLossStatus());
        dd.setRuleRate(dailyAcc.getRate());
        dd.setRuleValidAccountCount(dailyAcc.getValidAccountCount());
        dd.setRemark(remark);
        this.dailyDataDao.save(dd);
    }

    private void saveFinanceChange(BigDecimal dailyWages, User user, User userChild, Date changeTime, Integer accountChangeTypeId) {
        if (dailyWages.compareTo(BigDecimal.ZERO) != 0) {
            FinanceChange financeChange = new FinanceChange();
            financeChange.setAccountChangeTypeId(accountChangeTypeId);
            financeChange.setTest(user.getTest());
            financeChange.setBalance(user.getAmount().add(dailyWages));
            financeChange.setChangeAmount(dailyWages);
            financeChange.setChangeTime(changeTime);
            financeChange.setChangeUser(user.getAccount());
            financeChange.setFinanceId(IdBuilder.getId("D", 3));
            financeChange.setStatus(Integer.valueOf(2));
            financeChange.setRemark(userChild != null ? "FOR " + userChild.getAccount() : null);
            this.financeChangeDao.save(financeChange);


            this.userDao.updateAmount(user.getAccount(), dailyWages);
        }
    }

    private BigDecimal countDaily(DailyAcc dailyAcc, DailyData dailyData) {
        BigDecimal dailyWages = dailyData.getBetAmount().multiply(dailyAcc.getRate()).divide(new BigDecimal("100"));
        if ((dailyAcc.getLimitAmount().compareTo(BigDecimal.ZERO) > 0) && (dailyWages.compareTo(dailyAcc.getLimitAmount()) > 0)) {
            dailyWages = dailyAcc.getLimitAmount();
        }
        return dailyWages.setScale(4, 1);
    }

    public List<DailyAcc> listTeam() {
        return this.dailyAccDao.listTeam();
    }

    public List<DailyAcc> listByCond(DailyAcc m, Page page) {
        return this.dailyAccDao.listByCond(m, page);
    }

    public DailyAcc findByAccount(String account) {
        return this.dailyAccDao.findByAccount(account);
    }

    public void save(DailyAcc m) {
        this.dailyAccDao.save(m);
    }

    public List<DailyAcc> list(Page p) {
        return this.dailyAccDao.list(p);
    }

    public List<DailyAcc> listWithOrder(Page p) {
        return this.dailyAccDao.listWithOrder(p);
    }

    public DailyAcc find(Integer id) {
        return (DailyAcc) this.dailyAccDao.find(id);
    }

    public int update(DailyAcc m) {
        return this.dailyAccDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.dailyAccDao.delete(ids);
    }
}
