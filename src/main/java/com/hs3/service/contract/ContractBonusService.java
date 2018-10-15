package com.hs3.service.contract;

import com.hs3.dao.contract.ContractBadrecordDao;
import com.hs3.dao.contract.ContractBonusDao;
import com.hs3.dao.contract.ContractConfigDao;
import com.hs3.dao.contract.ContractRuleDao;
import com.hs3.dao.lotts.BetDao;
import com.hs3.dao.report.TeamReportDao;
import com.hs3.dao.report.UserReportDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.contract.ContractBadrecord;
import com.hs3.entity.contract.ContractBonus;
import com.hs3.entity.contract.ContractConfig;
import com.hs3.entity.contract.ContractRule;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.report.TeamReport;
import com.hs3.entity.users.User;
import com.hs3.service.finance.FinanceChangeService;
import com.hs3.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contractBonusService")
public class ContractBonusService {
    @Autowired
    private ContractBonusDao contractBonusDao;
    @Autowired
    private ContractConfigDao contractConfigDao;
    @Autowired
    private TeamReportDao teamReportDao;
    @Autowired
    private UserReportDao userReportDao;
    @Autowired
    private ContractRuleDao contractRuleDao;
    @Autowired
    private BetDao betDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ContractBadrecordDao contractBadrecordDao;
    @Autowired
    private FinanceChangeService financeChangeService;
    protected static final Integer USER_CONTRACT_STATUS_0 = Integer.valueOf(0);
    protected static final Integer USER_CONTRACT_STATUS_1 = Integer.valueOf(1);
    protected static final Integer BONUS_STATUS_0 = Integer.valueOf(0);
    protected static final Integer BONUS_STATUS_1 = Integer.valueOf(1);
    protected static final Integer BONUS_STATUS_2 = Integer.valueOf(2);
    protected static final Integer BONUS_STATUS_3 = Integer.valueOf(3);
    protected static final Integer BONUS_STATUS_4 = Integer.valueOf(4);
    protected static final Integer BONUS_STATUS_5 = Integer.valueOf(5);
    protected static final Integer BONUS_STATUS_6 = Integer.valueOf(6);

    public void save(ContractBonus m) {
        this.contractBonusDao.save(m);
    }

    public List<ContractBonus> findEntityByAccount(String account) {
        return this.contractBonusDao.findEntityByAccount(account);
    }

    public List<ContractBonus> list(Page p) {
        return this.contractBonusDao.list(p);
    }

    public List<ContractBonus> listByCondition(ContractBonus m, Page p) {
        return this.contractBonusDao.listByCondition(m, p);
    }

    public void createBonus(Date curDate) {
        ContractConfig config = this.contractConfigDao.findEntity();
        Date startTime = null;
        if (config.getBonusCycle().intValue() == 0) {
            startTime = DateUtils.getStartDateByOne(curDate);
        } else {
            startTime = DateUtils.getStartDateBySecond(curDate);
        }
        Date endTime = DateUtils.getEndDateByContract(curDate);
        Date startDate = DateUtils.getDate(startTime);
        Date endDate = DateUtils.getDate(endTime);

        List<ContractRule> accountList = this.contractRuleDao.accountList(USER_CONTRACT_STATUS_1);

        List<TeamReport> dataList = null;
        if (accountList.size() > 0) {
            dataList = this.teamReportDao.findListByContract(startDate, endDate, accountList);
        }
        for (ContractRule user : accountList) {
            for (TeamReport a : dataList) {
                ContractBonus contractBonus = new ContractBonus();
                if (user.getAccount().equals(a.getAccount())) {
                    List<ContractRule> ruleList = this.contractRuleDao.findContractRuleByOrder(a.getAccount(), USER_CONTRACT_STATUS_1);
                    BigDecimal dividendAmount = new BigDecimal("0");
                    for (ContractRule c : ruleList) {
                        if (c.getCumulativeSales() == null) {
                            if (c.getGtdBonusesCycle() != null) {
                                if (a.getCount().compareTo(new BigDecimal("0")) == -1) {
                                    dividendAmount = dividendAmount.add(a.getCount().multiply(new BigDecimal(-1)).multiply(new BigDecimal(String.valueOf(c.getGtdBonuses()))).divide(new BigDecimal(100)));
                                    contractBonus.setStatus(BONUS_STATUS_0);
                                } else {
                                    contractBonus.setStatus(BONUS_STATUS_2);
                                }
                                contractBonus.setDividendAmount(dividendAmount);
                                contractBonus.setAccount(c.getAccount());
                                contractBonus.setParentAccount(c.getParentAccount());
                                contractBonus.setRootAccount(c.getRootAccount());
                                contractBonus.setCumulativeSales(a.getBetAmount());
                                contractBonus.setCumulativeProfit(a.getCount());
                                contractBonus.setStartDate(startDate);
                                contractBonus.setDividend(c.getGtdBonuses());
                                contractBonus.setEndDate(endDate);
                                this.contractBonusDao.save(contractBonus);
                                break;
                            }
                            if (a.getCount().compareTo(new BigDecimal("0")) == -1) {
                                dividendAmount = dividendAmount.add(a.getCount().multiply(new BigDecimal(-1)).multiply(new BigDecimal(String.valueOf(c.getGtdBonuses()))).divide(new BigDecimal(100)));
                                contractBonus.setStatus(BONUS_STATUS_0);
                            } else {
                                contractBonus.setStatus(BONUS_STATUS_2);
                            }
                            contractBonus.setDividendAmount(dividendAmount);
                            contractBonus.setAccount(c.getAccount());
                            contractBonus.setParentAccount(c.getParentAccount());
                            contractBonus.setRootAccount(c.getRootAccount());
                            contractBonus.setCumulativeSales(a.getBetAmount());
                            contractBonus.setCumulativeProfit(a.getCount());
                            contractBonus.setStartDate(startDate);
                            contractBonus.setDividend(c.getGtdBonuses());
                            contractBonus.setEndDate(endDate);
                            this.contractBonusDao.save(contractBonus);
                            break;
                        }
                        if (c.getCumulativeSales().compareTo(a.getBetAmount()) == -1) {
                            int humanNum = this.userReportDao.getHumanNum(startDate, endDate, a.getAccount());
                            if (humanNum > c.getHumenNum().intValue()) {
                                if (a.getCount().compareTo(new BigDecimal("0")) == -1) {
                                    dividendAmount = dividendAmount.add(a.getCount().multiply(new BigDecimal(-1)).multiply(new BigDecimal(String.valueOf(c.getDividend()))).divide(new BigDecimal(100)));
                                    contractBonus.setStatus(BONUS_STATUS_0);
                                } else {
                                    contractBonus.setStatus(BONUS_STATUS_2);
                                }
                                contractBonus.setDividendAmount(dividendAmount);
                                contractBonus.setAccount(c.getAccount());
                                contractBonus.setParentAccount(c.getParentAccount());
                                contractBonus.setRootAccount(c.getRootAccount());
                                contractBonus.setCumulativeSales(a.getBetAmount());
                                contractBonus.setCumulativeProfit(a.getCount());
                                contractBonus.setStartDate(startDate);
                                contractBonus.setDividend(c.getDividend());
                                contractBonus.setEndDate(endDate);
                                this.contractBonusDao.save(contractBonus);
                                break;
                            }
                        }
                    }
                }
            }
            ContractBonus nullDate = new ContractBonus();
            nullDate.setStatus(BONUS_STATUS_2);
            nullDate.setDividendAmount(new BigDecimal("0"));
            nullDate.setAccount(user.getAccount());
            nullDate.setParentAccount(user.getParentAccount());
            nullDate.setRootAccount(user.getRootAccount());
            nullDate.setCumulativeSales(new BigDecimal("0"));
            nullDate.setCumulativeProfit(new BigDecimal("0"));
            nullDate.setStartDate(startDate);
            nullDate.setDividend(Double.valueOf(0.0D));
            nullDate.setEndDate(endDate);
            this.contractBonusDao.save(nullDate);
        }
    }

    public List<ContractBonus> findContractBonus(String account) {
        return this.contractBonusDao.findContractBonus(account);
    }

    public void createToCheck(Date endDate) {
        List<ContractBonus> overdueList = this.contractBonusDao.findOverdueList(endDate, BONUS_STATUS_0);
        for (ContractBonus m : overdueList) {
            ContractBadrecord contractBadrecord = new ContractBadrecord();
            contractBadrecord.setAccount(m.getAccount());
            contractBadrecord.setParentAccount(m.getParentAccount());
            contractBadrecord.setRootAccount(m.getRootAccount());
            contractBadrecord.setCumulativeSales(m.getCumulativeSales());
            contractBadrecord.setCumulativeProfit(m.getCumulativeProfit());
            contractBadrecord.setStartDate(m.getStartDate());
            contractBadrecord.setEndDate(m.getEndDate());
            contractBadrecord.setDividend(m.getDividend());
            contractBadrecord.setDividendAmount(m.getDividendAmount());
            contractBadrecord.setStatus(BONUS_STATUS_3);
            this.contractBadrecordDao.save(contractBadrecord);

            this.contractBonusDao.updateStatus(m.getId(), BONUS_STATUS_3);
        }
    }

    public int saveToSend(Integer id, String account, String parentAccount, BigDecimal dividendAmount) {
        int flag = 0;
        User user = this.userDao.findByAccountMaster(parentAccount);
        if (dividendAmount.compareTo(user.getAmount()) == 1) {
            flag = 2;
        } else if (this.userDao.updateAmount(parentAccount, dividendAmount.multiply(new BigDecimal("-1"))).intValue() > 0) {
            FinanceChange m = new FinanceChange();
            m.setAccountChangeTypeId(Integer.valueOf(88));
            m.setBalance(user.getAmount().add(dividendAmount.multiply(new BigDecimal("-1"))));
            m.setChangeAmount(dividendAmount.multiply(new BigDecimal("-1")));
            m.setChangeTime(new Date());
            m.setChangeUser(user.getAccount());
            m.setTest(user.getTest());
            m.setRemark("");
            m.setStatus(Integer.valueOf(2));
            m.setOperator(account);
            this.financeChangeService.save(m);

            User u = this.userDao.findByAccountMaster(account);
            if (this.userDao.updateAmount(account, dividendAmount).intValue() > 0) {
                this.contractBonusDao.updateStatus(id, BONUS_STATUS_1);
                FinanceChange f = new FinanceChange();
                f.setAccountChangeTypeId(Integer.valueOf(89));
                f.setBalance(u.getAmount().add(dividendAmount));
                f.setChangeAmount(dividendAmount);
                f.setChangeTime(new Date());
                f.setChangeUser(u.getAccount());
                f.setTest(u.getTest());
                f.setRemark("");
                f.setStatus(Integer.valueOf(2));
                f.setOperator(parentAccount);
                this.financeChangeService.save(f);
                flag = 1;
            } else {
                flag = 3;
            }
        } else {
            flag = 3;
        }
        return flag;
    }

    public boolean isExistContractBonus(Date endDate) {
        return this.contractBonusDao.isExistContractBonus(endDate);
    }

    public List<ContractBonus> list(ContractBonus m, Page p) {
        return this.contractBonusDao.list(m, p);
    }

    public int saveToPayout(Integer id, String account, String parentAccount, BigDecimal dividendAmount) {
        int flag = 0;
        User user = this.userDao.findByAccountMaster(parentAccount);
        if (dividendAmount.compareTo(user.getAmount()) == 1) {
            flag = 2;
        } else if (this.userDao.updateAmount(parentAccount, dividendAmount.multiply(new BigDecimal("-1"))).intValue() > 0) {
            FinanceChange m = new FinanceChange();
            m.setAccountChangeTypeId(Integer.valueOf(88));
            m.setBalance(user.getAmount().add(dividendAmount.multiply(new BigDecimal("-1"))));
            m.setChangeAmount(dividendAmount.multiply(new BigDecimal("-1")));
            m.setChangeTime(new Date());
            m.setChangeUser(user.getAccount());
            m.setTest(user.getTest());
            m.setRemark("");
            m.setStatus(Integer.valueOf(2));
            m.setOperator(account);
            this.financeChangeService.save(m);

            User u = this.userDao.findByAccountMaster(account);
            if (this.userDao.updateAmount(account, dividendAmount).intValue() > 0) {
                this.contractBonusDao.updateStatus(id, BONUS_STATUS_4);
                FinanceChange f = new FinanceChange();
                f.setAccountChangeTypeId(Integer.valueOf(89));
                f.setBalance(u.getAmount().add(dividendAmount));
                f.setChangeAmount(dividendAmount);
                f.setChangeTime(new Date());
                f.setChangeUser(u.getAccount());
                f.setTest(u.getTest());
                f.setRemark("");
                f.setStatus(Integer.valueOf(2));
                f.setOperator(parentAccount);
                this.financeChangeService.save(f);
                flag = 1;
            } else {
                flag = 3;
            }
        } else {
            flag = 3;
        }
        return flag;
    }

    public int saveToPayoutByAdmin(Integer id, String operator, String account, BigDecimal dividendAmount) {
        User user = this.userDao.findByAccountMaster(account);
        int n = this.userDao.updateAmount(account, dividendAmount).intValue();
        if (n > 0) {
            this.contractBonusDao.updateStatus(id, BONUS_STATUS_5);

            FinanceChange f = new FinanceChange();
            f.setAccountChangeTypeId(Integer.valueOf(22));
            f.setBalance(user.getAmount().add(dividendAmount));
            f.setChangeAmount(dividendAmount);
            f.setChangeTime(new Date());
            f.setChangeUser(user.getAccount());
            f.setTest(user.getTest());
            f.setRemark("");
            f.setStatus(Integer.valueOf(2));
            f.setOperator(operator);
            this.financeChangeService.save(f);
        }
        return n;
    }

    public boolean updateToRefuse(Integer id) {
        int i = this.contractBonusDao.updateStatus(id, BONUS_STATUS_6);
        return i > 0;
    }

    public boolean deleteBonusByDate(Date endDate) {
        return this.contractBonusDao.deleteBonusByDate(endDate);
    }

    public boolean isExistBonus(String account) {
        return this.contractBonusDao.isExistBonus(account);
    }
}
