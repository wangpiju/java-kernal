package com.hs3.service.user;

import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.report.TeamReportDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.dao.user.PrivateRatioDao;
import com.hs3.dao.user.PrivateRatioRuleDao;
import com.hs3.dao.user.PrivateRatioRuleDetailsDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.report.TeamReport;
import com.hs3.entity.sys.SysConfig;
import com.hs3.entity.users.PrivateRatio;
import com.hs3.entity.users.PrivateRatioRule;
import com.hs3.entity.users.PrivateRatioRuleDetails;
import com.hs3.entity.users.User;
import com.hs3.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateRatioService {
    private static final Logger logger = LoggerFactory.getLogger(PrivateRatioService.class);
    private static final String SYS_PRIVATEREBATE_KEY = "PRIVATEREBATE_KEY";
    @Autowired
    private PrivateRatioDao privateRatioDao;
    @Autowired
    private PrivateRatioRuleDao privateRatioRuleDao;
    @Autowired
    private PrivateRatioRuleDetailsDao privateRatioRuleDetailsDao;
    @Autowired
    private TeamReportDao teamReportDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;
    @Autowired
    private SysConfigDao sysConfigDao;

    public List<PrivateRatio> list(String account, BigDecimal ratio, Date begin, Date end, Page p) {
        return this.privateRatioDao.list(account, ratio, begin, end, p);
    }

    public void addRatioToUser(Date createDate) {
        Date date = DateUtils.getDate(createDate);
        String d = DateUtils.formatDate(date);
        SysConfig sys = (SysConfig) this.sysConfigDao.findLock("PRIVATEREBATE_KEY");
        if (d.equals(sys.getVal())) {
            return;
        }
        sys.setVal(d);
        this.sysConfigDao.update(sys);


        Date changeTime = DateUtils.addHour(date, 28);


        List<User> users = this.userDao.listByPrivateRatioNot(0);
        for (User u : users) {
            PrivateRatioRule rule = (PrivateRatioRule) this.privateRatioRuleDao.find(u.getPrivateRebate());
            if (rule == null) {
                logger.info(u.getAccount() + ":的私返规则[" + u.getPrivateRebate() + "] 未找到");
            }
            String info = u.getAccount() + ":的私返规则[" + rule.getName() + "] ";
            if (rule.getStatus().intValue() != 0) {
                logger.info(info + "已禁用");
            } else {
                TeamReport report = this.teamReportDao.findByAccount(u.getAccount(), date);
                if (report == null) {
                    logger.info(info + "没有报表数据.");
                } else {
                    PrivateRatioRuleDetails details = this.privateRatioRuleDetailsDao.listByPidAndAmount(rule.getId(), report.getBetAmount());
                    if (details == null) {
                        logger.info(info + "没有达到最低标准.");
                    } else {
                        logger.info(info + "销量:" + report.getBetAmount() + "  达到:" + details.getAmount() + " : " + details.getRatio());
                        if (details.getRatio().compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal bonus = report.getBetAmount().multiply(details.getRatio()).divide(new BigDecimal("100"));

                            this.userDao.updateAmount(u.getAccount(), bonus);

                            FinanceChange m = new FinanceChange();
                            m.setAccountChangeTypeId(Integer.valueOf(41));
                            m.setTest(u.getTest());
                            m.setBalance(u.getAmount().add(bonus));
                            m.setChangeAmount(bonus);
                            m.setChangeTime(changeTime);
                            m.setChangeUser(u.getAccount());
                            m.setRemark("投注私返");
                            m.setStatus(Integer.valueOf(2));
                            m.setFinanceId("privateRebate");
                            this.financeChangeDao.save(m);


                            PrivateRatio pr = new PrivateRatio();
                            pr.setAccount(u.getAccount());
                            pr.setParentAccount(u.getParentAccount());
                            //jd-gui
                            //pr.setRemark(u.getUserMark());
                            pr.setRemark((new StringBuilder()).append(u.getUserMark()).toString());
                            pr.setCreateDate(changeTime);
                            pr.setAmount(bonus);
                            pr.setBetAmount(report.getBetAmount());
                            pr.setRatio(details.getRatio());
                            this.privateRatioDao.saveAuto(pr);
                        }
                    }
                }
            }
        }
    }
}
