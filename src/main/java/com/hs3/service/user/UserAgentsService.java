package com.hs3.service.user;

import com.hs3.dao.lotts.LotteryDao;
import com.hs3.dao.report.AgentReportDao;
import com.hs3.dao.user.UserAgentsNatureDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.userAgents.*;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceChange;
import com.hs3.entity.report.AgentLotteryReport;
import com.hs3.entity.report.AgentReport;
import com.hs3.entity.userAgents.*;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserAgentsNature;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.service.finance.FinanceChangeService;
import com.hs3.service.newReport.CpsReportService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.StrUtils;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userAgentsService")
public class UserAgentsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserAgentsNatureDao userAgentsNatureDao;

    @Autowired
    private UserAgentsDailyDao userAgentsDailyDao;

    @Autowired
    private UserAgentsDailyMgDao userAgentsDailyMgDao;

    @Autowired
    private UserAgentsDailyLotteryDao userAgentsDailyLotteryDao;

    @Autowired
    private UserAgentsDailyLotteryMgDao userAgentsDailyLotteryMgDao;

    @Autowired
    private UserAgentsDividendDao userAgentsDividendDao;

    @Autowired
    private UserAgentsDividendMgDao userAgentsDividendMgDao;

    @Autowired
    private UserAgentsDividendLotteryDao userAgentsDividendLotteryDao;

    @Autowired
    private UserAgentsDividendLotteryMgDao userAgentsDividendLotteryMgDao;

    @Autowired
    private AgentReportDao agentReportDao;

    @Autowired
    private CpsReportService cpsReportService;

    @Autowired
    private FinanceChangeService financeChangeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LotteryDao lotteryDao;

    //*************************************************************代理商性质配置Start*************************************************************

    /**
     * 查询
     *
     * @param account           会员账户
     * @param parentAccount     上级账户
     * @param isA               是否A类代理 0：否、1：是
     * @param isDaily           是否日工资 0：否、1：是
     * @param isDividend        是否周期分红 0：否、1：是
     * @param isDailyLottery    是否日工资彩种加奖 0：否、1：是
     * @param isDividendLottery 是否周期分红彩种加奖 0：否、1：是
     * @param p                 分页对象
     * @return
     */
    public List<UserAgentsNature> userAgentsNatureList(String account, String parentAccount, Integer isA, Integer isDaily, Integer isDividend, Integer isDailyLottery, Integer isDividendLottery, Page p) {
        return userAgentsNatureDao.userAgentsNatureList(account, parentAccount, isA, isDaily, isDividend, isDailyLottery, isDividendLottery, p);
    }

    public UserAgentsNature findByID(Integer id){
        return userAgentsNatureDao.findByID(id);
    }

    public UserAgentsNature findByAccount(String account){
        return userAgentsNatureDao.findByAccount(account);
    }

    public void save(UserAgentsNature m) {

        Integer id = m.getId();

        Integer isA = m.getIsA();
        Integer isDaily = m.getIsDaily();
        Integer isDailyLottery = m.getIsDailyLottery();

        if((StrUtils.hasEmpty(new Object[]{isA}) || isA == 0) && (!StrUtils.hasEmpty(new Object[]{isDaily}) && isDaily == 1)){
            throw new BaseCheckException("必须A类代理才可以配置日工资！");
        }

        if((StrUtils.hasEmpty(new Object[]{isA}) || isA == 0) && (!StrUtils.hasEmpty(new Object[]{isDailyLottery}) && isDailyLottery == 1)){
            throw new BaseCheckException("必须A类代理才可以配置日工资彩种加奖！");
        }

        if(!StrUtils.hasEmpty(new Object[]{id})){
            userAgentsNatureDao.update(m);
        }else{
            userAgentsNatureDao.save(m);
        }

    }

    //*************************************************************代理商性质配置End*************************************************************


    //*************************************************************日工资Start*************************************************************

    /**
     * 查询
     *
     * @param programName           方案名称
     * @param status                状态 0：关闭、1：开启
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDaily> userAgentsDailyList(String programName, Integer status, Integer sorting, Page p) {
        return userAgentsDailyDao.userAgentsDailyList(programName, status, sorting, p);
    }

    public UserAgentsDaily findUserAgentsDaily(Integer id){
        return userAgentsDailyDao.find(id);
    }

    public void saveUserAgentsDaily(UserAgentsDaily m) {
        Integer id = m.getId();

        String programName = m.getProgramName();
        if(StrUtils.hasEmpty(new Object[]{programName})) {
            throw new BaseCheckException("方案名称不能为空！");
        }

        BigDecimal salesVolume = m.getSalesVolume();
        if(StrUtils.hasEmpty(new Object[]{salesVolume})) {
            throw new BaseCheckException("团队销量不能为空！");
        }
        if(salesVolume.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("团队销量金额设置异常！");
        }

        BigDecimal proportion = m.getProportion();
        if(StrUtils.hasEmpty(new Object[]{proportion})) {
            throw new BaseCheckException("日工资比例不能为空！");
        }
        if(proportion.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("日工资比例设置异常！");
        }

        Integer activeNumber = m.getActiveNumber();
        if(StrUtils.hasEmpty(new Object[]{activeNumber})) {
            throw new BaseCheckException("活跃人数不能为空！");
        }

        Integer cycle = m.getCycle();
        if(StrUtils.hasEmpty(new Object[]{cycle})) {
            throw new BaseCheckException("计算周期不能为空！");
        }

        Integer status = m.getStatus();
        if(StrUtils.hasEmpty(new Object[]{status})) {
            throw new BaseCheckException("状态不能为空！");
        }
        if(status!=0 && status!=1){
            throw new BaseCheckException("状态异常！");
        }


        if(!StrUtils.hasEmpty(new Object[]{id})){
            userAgentsDailyDao.update(m);
        }else{
            userAgentsDailyDao.save(m);
        }
    }


    /**
     * 查询
     *
     * @param account               用户名称
     * @param parentAccount         上级
     * @param programName           方案名称
     * @param begin                 计算日期-开始
     * @param end                   计算日期-结束
     * @param startTime             修改时间-开始
     * @param endTime               修改时间-结束
     * @param status                状态 0：待处理、1：派发、2：不予派发
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDailyMg> userAgentsDailyMgList(String account, String parentAccount, String programName, String begin, String end, Date startTime, Date endTime, Integer status, Page p) {
        return userAgentsDailyMgDao.userAgentsDailyMgList(account, parentAccount, programName, begin, end, startTime, endTime, status, p);
    }

    public UserAgentsDailyMg findUserAgentsDailyMg(Integer id){
        return userAgentsDailyMgDao.find(id);
    }

    public void updateUserAgentsDailyMg(UserAgentsDailyMg m) {
        Integer id = m.getId();
        if(!StrUtils.hasEmpty(new Object[]{id})){
            Integer status = m.getStatus();
            if(status == 1) {
                String account = m.getAccount();
                BigDecimal distributionAmount = m.getDistributionAmount();
                User user = userDao.findByAccount(account);
                Integer test = user.getTest();
                Integer programId = m.getProgramId();
                UserAgentsDaily userAgentsDaily = userAgentsDailyDao.find(programId);
                String remarks = userAgentsDaily.getHint() + userAgentsDaily.getProportion() + "%";

                FinanceChange financeChange = new FinanceChange();
                financeChange.setChangeUser(account);
                financeChange.setChangeAmount(distributionAmount);
                financeChange.setTest(test);
                financeChange.setRemark(remarks);
                financeChange.setAccountChangeTypeId(28);

                this.financeChangeService.saveToApproval(financeChange, null, null);
            }
            userAgentsDailyMgDao.update(m);
        }else{
            throw new BaseCheckException("数据ID异常！");
        }
    }

    /**
     *
     * 日工资派发计算
     * @param   reportDate      派发日期
     * @param   cycleFlag       是否需要判断计算周期
     * @throws  Exception
     */
    public void addUserAgentsDailyWhenNotExists(String reportDate, boolean cycleFlag) {

        if(userAgentsDailyMgDao.userAgentsDailyMgList(null, null, null, reportDate, reportDate, null, null, null, null).isEmpty()) {

            //userAgentsDailyMgDao.delete(reportDate);

            String[] userAgentsNatureAccounts = userAgentsNatureDao.userAgentsNatureAccounts(1, 1, null, null, null);

            if (userAgentsNatureAccounts.length > 0) {
                List<UserAgentsDaily> userAgentsDailyList = userAgentsDailyList(null, 1, 1, null);
                if (!userAgentsDailyList.isEmpty()) {
                    List<AgentReport> agentReportList = agentReportDao.findListByAccountsAndDate(userAgentsNatureAccounts, reportDate, reportDate, null);

                    BigDecimal betAmountNoOwn = BigDecimal.ZERO;//投注金额（不含本人）
                    Integer betPerCountNoOwn = 0;//投注人数（不含本人）
                    BigDecimal proportion = BigDecimal.ZERO;  //日工资比例
                    Integer cycle = 0;
                    boolean dailyFlag = false;
                    for (AgentReport agentReport : agentReportList) {

                        betAmountNoOwn = agentReport.getBetAmountNoOwn();
                        betPerCountNoOwn = agentReport.getBetPerCountNoOwn();
                        dailyFlag = false;

                        //String reportDate;  //日期
                        String account = agentReport.getAccount(); //账户
                        String parentAccount = agentReport.getParentAccount();   //上级
                        //BigDecimal distributionAmount;  //派发金额
                        BigDecimal actualSalesVolume = betAmountNoOwn; //团队销量（元）— 不含代理本人
                        Integer actualActiveNumber = betPerCountNoOwn;   //活跃人数
                        Integer programId = 0;  //方案ID
                        String programName = ""; //方案名称
                        BigDecimal salesVolume = BigDecimal.ZERO; //方案团队销量（元）— 不含代理本人
                        //BigDecimal proportion;  //方案日工资比例
                        Integer activeNumber = 0;   //方案活跃人数
                        //Integer cycle;  //方案计算周期
                        Integer status = 0; //状态 0：待处理、1：派发、2：不予派发

                        for (UserAgentsDaily userAgentsDaily : userAgentsDailyList) {
                            if (betAmountNoOwn.compareTo(userAgentsDaily.getSalesVolume()) >= 0 && betPerCountNoOwn >= userAgentsDaily.getActiveNumber()) {
                                proportion = userAgentsDaily.getProportion();
                                programId = userAgentsDaily.getId();  //方案ID
                                programName = userAgentsDaily.getProgramName(); //方案名称
                                salesVolume = userAgentsDaily.getSalesVolume(); //方案团队销量（元）— 不含代理本人
                                activeNumber = userAgentsDaily.getActiveNumber();   //方案活跃人数
                                status = 0; //状态 0：待处理、1：派发、2：不予派发

                                //此处判断计算周期【暂时不处理】
                                if (cycleFlag) {
                                    cycle = userAgentsDaily.getCycle();

                                } else {
                                    cycle = userAgentsDaily.getCycle();//在周期打开前暂时这样设定
                                    dailyFlag = true;
                                }

                                break;
                            }
                        }

                        if (dailyFlag) {

                            BigDecimal distributionAmount = betAmountNoOwn.multiply(proportion.divide(new BigDecimal("100")));
                            distributionAmount = distributionAmount.setScale(0, BigDecimal.ROUND_HALF_UP);

                            UserAgentsDailyMg userAgentsDailyMg = new UserAgentsDailyMg();
                            userAgentsDailyMg.setReportDate(reportDate);
                            userAgentsDailyMg.setAccount(account);
                            userAgentsDailyMg.setParentAccount(parentAccount);
                            userAgentsDailyMg.setDistributionAmount(distributionAmount);
                            userAgentsDailyMg.setActualSalesVolume(actualSalesVolume);
                            userAgentsDailyMg.setActualActiveNumber(actualActiveNumber);
                            userAgentsDailyMg.setProgramId(programId);
                            userAgentsDailyMg.setProgramName(programName);
                            userAgentsDailyMg.setSalesVolume(salesVolume);
                            userAgentsDailyMg.setProportion(proportion);
                            userAgentsDailyMg.setActiveNumber(activeNumber);
                            userAgentsDailyMg.setCycle(cycle);
                            userAgentsDailyMg.setStatus(status);

                            userAgentsDailyMgDao.save(userAgentsDailyMg);

                        }

                    }
                }
            } else {
                logger.info("日期为[" + reportDate + "]的日工资无账户需要生成数据！");
            }

        }
    }



    //*************************************************************日工资End*************************************************************


    //*************************************************************日工资彩种加奖Start*************************************************************


    /**
     * 查询
     *
     * @param programName           方案名称
     * @param status                状态 0：关闭、1：开启
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDailyLottery> userAgentsDailyLotteryList(String programName, Integer status, Integer sorting, Page p) {
        return userAgentsDailyLotteryDao.userAgentsDailyLotteryList(programName, status, sorting, p);
    }

    public UserAgentsDailyLottery findUserAgentsDailyLottery(Integer id){
        return userAgentsDailyLotteryDao.find(id);
    }

    public void saveUserAgentsDailyLottery(UserAgentsDailyLottery m) {
        Integer id = m.getId();

        String programName = m.getProgramName();
        if(StrUtils.hasEmpty(new Object[]{programName})) {
            throw new BaseCheckException("方案名称不能为空！");
        }

        BigDecimal salesVolume = m.getSalesVolume();
        if(StrUtils.hasEmpty(new Object[]{salesVolume})) {
            throw new BaseCheckException("团队销量不能为空！");
        }
        if(salesVolume.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("团队销量金额设置异常！");
        }

        BigDecimal proportion = m.getProportion();
        if(StrUtils.hasEmpty(new Object[]{proportion})) {
            throw new BaseCheckException("日工资比例不能为空！");
        }
        if(proportion.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("日工资比例设置异常！");
        }

        Integer activeNumber = m.getActiveNumber();
        if(StrUtils.hasEmpty(new Object[]{activeNumber})) {
            throw new BaseCheckException("活跃人数不能为空！");
        }

        Integer cycle = m.getCycle();
        if(StrUtils.hasEmpty(new Object[]{cycle})) {
            throw new BaseCheckException("计算周期不能为空！");
        }

        Integer status = m.getStatus();
        if(StrUtils.hasEmpty(new Object[]{status})) {
            throw new BaseCheckException("状态不能为空！");
        }
        if(status!=0 && status!=1){
            throw new BaseCheckException("状态异常！");
        }

        String lotteryId = m.getLotteryId();
        String lotteryName = lotteryDao.find(lotteryId).getTitle();
        m.setLotteryName(lotteryName);

        if(!StrUtils.hasEmpty(new Object[]{id})){
            userAgentsDailyLotteryDao.update(m);
        }else{
            userAgentsDailyLotteryDao.save(m);
        }
    }


    /**
     * 查询
     *
     * @param account               用户名称
     * @param parentAccount         上级
     * @param programName           方案名称
     * @param begin                 计算日期-开始
     * @param end                   计算日期-结束
     * @param startTime             修改时间-开始
     * @param endTime               修改时间-结束
     * @param status                状态 0：待处理、1：派发、2：不予派发
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDailyLotteryMg> userAgentsDailyLotteryMgList(String account, String parentAccount, String programName, String begin, String end, Date startTime, Date endTime, Integer status, Page p) {
        return userAgentsDailyLotteryMgDao.userAgentsDailyLotteryMgList(account, parentAccount, programName, begin, end, startTime, endTime, status, p);
    }

    public UserAgentsDailyLotteryMg findUserAgentsDailyLotteryMg(Integer id){
        return userAgentsDailyLotteryMgDao.find(id);
    }

    public void updateUserAgentsDailyLotteryMg(UserAgentsDailyLotteryMg m) {
        Integer id = m.getId();
        if(!StrUtils.hasEmpty(new Object[]{id})){
            Integer status = m.getStatus();
            if(status == 1) {
                String account = m.getAccount();
                BigDecimal distributionAmount = m.getDistributionAmount();
                User user = userDao.findByAccount(account);
                Integer test = user.getTest();
                Integer programId = m.getProgramId();
                UserAgentsDailyLottery userAgentsDailyLottery = userAgentsDailyLotteryDao.find(programId);
                String remarks = userAgentsDailyLottery.getHint() + userAgentsDailyLottery.getProportion() + "%";

                FinanceChange financeChange = new FinanceChange();
                financeChange.setChangeUser(account);
                financeChange.setChangeAmount(distributionAmount);
                financeChange.setTest(test);
                financeChange.setRemark(remarks);
                financeChange.setAccountChangeTypeId(28);

                this.financeChangeService.saveToApproval(financeChange, null, null);
            }
            userAgentsDailyLotteryMgDao.update(m);
        }else{
            throw new BaseCheckException("数据ID异常！");
        }
    }

    /**
     *
     * 日工资彩种加奖派发计算
     * @param   reportDate      派发日期
     * @param   cycleFlag       是否需要判断计算周期
     * @throws  Exception
     */
    public void addUserAgentsDailyLotteryWhenNotExists(String reportDate, boolean cycleFlag) throws ParseException {

        if(userAgentsDailyLotteryMgDao.userAgentsDailyLotteryMgList(null, null, null, reportDate, reportDate, null, null, null, null).isEmpty()) {

            //userAgentsDailyLotteryMgDao.delete(reportDate);

            String[] userAgentsNatureAccounts = userAgentsNatureDao.userAgentsNatureAccounts(1, null, null, 1, null);
            //System.out.println("=================userAgentsNatureAccounts.length==============>>>>" + userAgentsNatureAccounts.length);

            if (userAgentsNatureAccounts.length > 0) {
                List<UserAgentsDailyLottery> userAgentsDailyLotteryList = userAgentsDailyLotteryList(null, 1, 1, null);
                //System.out.println("=================userAgentsDailyLotteryList.size()==============>>>>" + userAgentsDailyLotteryList.size());
                //System.out.println("=================!userAgentsDailyLotteryList.isEmpty()==============>>>>" + (!userAgentsDailyLotteryList.isEmpty()));
                if (!userAgentsDailyLotteryList.isEmpty()) {

                    Date historyBeginDate = null;
                    Date historyEndDate = null;
                    String startDateStr = reportDate + " 00:00:00";
                    String startEndStr = reportDate + " 23:59:59";
                    historyBeginDate = DateUtils.toDate(startDateStr);
                    historyEndDate = DateUtils.toDate(startEndStr);
                    //System.out.println("=================startDateStr==============>>>>" + startDateStr);
                    //System.out.println("=================startEndStr==============>>>>" + startEndStr);

                    List<AgentLotteryReport> agentLotteryReportListZ = cpsReportService.agentLotteryReportList(null, null, historyBeginDate, historyEndDate, null);
                    //System.out.println("=================agentLotteryReportListZ.size()==============>>>>" + agentLotteryReportListZ.size());
                    List<AgentLotteryReport> agentLotteryReportList = new ArrayList<AgentLotteryReport>();
                    for (AgentLotteryReport agentLotteryReport : agentLotteryReportListZ) {
                        for (String account : userAgentsNatureAccounts) {
                            if (agentLotteryReport.getAccount().equals(account)) {
                                agentLotteryReportList.add(agentLotteryReport);
                            }
                        }
                    }
                    //System.out.println("=================agentLotteryReportList.size()==============>>>>" + agentLotteryReportList.size());

                    BigDecimal betAmountNoOwn = BigDecimal.ZERO;//投注金额（不含本人）
                    Integer betPerCountNoOwn = 0;//投注人数（不含本人）
                    BigDecimal proportion = BigDecimal.ZERO;  //日工资比例
                    Integer cycle = 0;
                    boolean dailyFlag = false;
                    for (AgentLotteryReport agentLotteryReport : agentLotteryReportList) {
                        //System.out.println("**********************************************分割线**********************************************");

                        betAmountNoOwn = agentLotteryReport.getBetAmountNoOwn();
                        betPerCountNoOwn = agentLotteryReport.getBetPerCountNoOwn();
                        dailyFlag = false;

                        //String reportDate;  //日期
                        String account = agentLotteryReport.getAccount(); //账户
                        //System.out.println("=================account==============>>>>" + account);
                        //String parentAccount = AgentLotteryReport.getParentAccount();   //上级
                        //BigDecimal distributionAmount;  //派发金额
                        BigDecimal actualSalesVolume = betAmountNoOwn; //团队销量（元）— 不含代理本人
                        Integer actualActiveNumber = betPerCountNoOwn;   //活跃人数
                        Integer programId = 0;  //方案ID
                        String programName = ""; //方案名称
                        BigDecimal salesVolume = BigDecimal.ZERO; //方案团队销量（元）— 不含代理本人
                        //BigDecimal proportion;  //方案日工资比例
                        Integer activeNumber = 0;   //方案活跃人数
                        //Integer cycle;  //方案计算周期
                        Integer status = 0; //状态 0：待处理、1：派发、2：不予派发

                        String lotteryId = agentLotteryReport.getLotteryId(); //彩种ID
                        String lotteryName = agentLotteryReport.getLotteryName(); //彩种名称
                        //System.out.println("=================lotteryId==============>>>>" + lotteryId);
                        //System.out.println("=================lotteryName==============>>>>" + lotteryName);
                        //System.out.println("=================betAmountNoOwn==============>>>>" + betAmountNoOwn);

                        for (UserAgentsDailyLottery userAgentsDailyLottery : userAgentsDailyLotteryList) {
                            //System.out.println("--------------------------------------------分割线--------------------------------------------");
                            //System.out.println("------------------------userAgentsDailyLottery.getLotteryId()------------------------>>>>" + userAgentsDailyLottery.getLotteryId());
                            //System.out.println("------------------------userAgentsDailyLottery.getSalesVolume()------------------------>>>>" + userAgentsDailyLottery.getSalesVolume());
                            //System.out.println("------------------------userAgentsDailyLottery.getActiveNumber()------------------------>>>>" + userAgentsDailyLottery.getActiveNumber());
                            if (userAgentsDailyLottery.getLotteryId().equals(lotteryId) && betAmountNoOwn.compareTo(userAgentsDailyLottery.getSalesVolume()) >= 0 && betPerCountNoOwn >= userAgentsDailyLottery.getActiveNumber()) {
                                //System.out.println("--------------------------------------------符合条件--------------------------------------------");
                                proportion = userAgentsDailyLottery.getProportion();
                                //lotteryId = userAgentsDailyLottery.getLotteryId(); //彩种ID
                                //lotteryName = userAgentsDailyLottery.getLotteryName(); //彩种名称
                                programId = userAgentsDailyLottery.getId();  //方案ID
                                programName = userAgentsDailyLottery.getProgramName(); //方案名称
                                salesVolume = userAgentsDailyLottery.getSalesVolume(); //方案团队销量（元）— 不含代理本人
                                activeNumber = userAgentsDailyLottery.getActiveNumber();   //方案活跃人数
                                status = 0; //状态 0：待处理、1：派发、2：不予派发

                                //此处判断计算周期【暂时不处理】
                                if (cycleFlag) {
                                    cycle = userAgentsDailyLottery.getCycle();

                                } else {
                                    cycle = userAgentsDailyLottery.getCycle();

                                    dailyFlag = true;
                                }

                                break;
                            }
                        }

                        if (dailyFlag) {
                            //System.out.println("--------------------------------------------添加对象--------------------------------------------");
                            BigDecimal distributionAmount = betAmountNoOwn.multiply(proportion.divide(new BigDecimal("100")));
                            distributionAmount = distributionAmount.setScale(0, BigDecimal.ROUND_HALF_UP);

                            UserAgentsDailyLotteryMg userAgentsDailyLotteryMg = new UserAgentsDailyLotteryMg();
                            userAgentsDailyLotteryMg.setReportDate(reportDate);
                            userAgentsDailyLotteryMg.setAccount(account);
                            //userAgentsDailyLotteryMg.setParentAccount(parentAccount);
                            userAgentsDailyLotteryMg.setDistributionAmount(distributionAmount);
                            userAgentsDailyLotteryMg.setActualSalesVolume(actualSalesVolume);
                            userAgentsDailyLotteryMg.setActualActiveNumber(actualActiveNumber);
                            userAgentsDailyLotteryMg.setProgramId(programId);
                            userAgentsDailyLotteryMg.setProgramName(programName);
                            userAgentsDailyLotteryMg.setSalesVolume(salesVolume);
                            userAgentsDailyLotteryMg.setProportion(proportion);
                            userAgentsDailyLotteryMg.setActiveNumber(activeNumber);
                            userAgentsDailyLotteryMg.setCycle(cycle);
                            userAgentsDailyLotteryMg.setStatus(status);

                            userAgentsDailyLotteryMg.setLotteryId(lotteryId);
                            userAgentsDailyLotteryMg.setLotteryName(lotteryName);

                            userAgentsDailyLotteryMgDao.save(userAgentsDailyLotteryMg);

                        }

                    }
                }
            } else {
                logger.info("日期为[" + reportDate + "]的日工资彩种加奖无账户需要生成数据！");
            }

        }
    }


    //*************************************************************日工资彩种加奖End*************************************************************


    //*************************************************************周期分红Start*************************************************************

    /**
     * 查询
     *
     * @param programName           方案名称
     * @param status                状态 0：关闭、1：开启
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDividend> userAgentsDividendList(String programName, Integer cycle, Integer status, Integer sorting, Page p) {
        return userAgentsDividendDao.userAgentsDividendList(programName, cycle, status, sorting, p);
    }

    public UserAgentsDividend findUserAgentsDividend(Integer id){
        return userAgentsDividendDao.find(id);
    }

    public void saveUserAgentsDividend(UserAgentsDividend m) {
        Integer id = m.getId();

        String programName = m.getProgramName();
        if(StrUtils.hasEmpty(new Object[]{programName})) {
            throw new BaseCheckException("方案名称不能为空！");
        }

        BigDecimal negativeProfit = m.getNegativeProfit();
        if(StrUtils.hasEmpty(new Object[]{negativeProfit})) {
            throw new BaseCheckException("负盈利值不能为空！");
        }
        if(negativeProfit.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("负盈利值金额设置异常！");
        }

        BigDecimal proportion = m.getProportion();
        if(StrUtils.hasEmpty(new Object[]{proportion})) {
            throw new BaseCheckException("周期分红比例不能为空！");
        }
        if(proportion.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("周期分红比例设置异常！");
        }

        Integer activeNumber = m.getActiveNumber();
        if(StrUtils.hasEmpty(new Object[]{activeNumber})) {
            throw new BaseCheckException("活跃人数不能为空！");
        }

        Integer cycle = m.getCycle();
        if(StrUtils.hasEmpty(new Object[]{cycle})) {
            throw new BaseCheckException("计算周期不能为空！");
        }
        if(cycle!=1 && cycle!=2){
            throw new BaseCheckException("计算周期数据异常！");
        }

        Integer status = m.getStatus();
        if(StrUtils.hasEmpty(new Object[]{status})) {
            throw new BaseCheckException("状态不能为空！");
        }
        if(status!=0 && status!=1){
            throw new BaseCheckException("状态异常！");
        }

        if(!StrUtils.hasEmpty(new Object[]{id})){
            userAgentsDividendDao.update(m);
        }else{
            userAgentsDividendDao.save(m);
        }
    }


    /**
     * 查询
     *
     * @param account               用户名称
     * @param parentAccount         上级
     * @param programName           方案名称
     * @param begin                 计算日期-开始
     * @param end                   计算日期-结束
     * @param startTime             修改时间-开始
     * @param endTime               修改时间-结束
     * @param status                状态 0：待处理、1：派发、2：不予派发
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDividendMg> userAgentsDividendMgList(String account, String parentAccount, String programName, String begin, String end, Date startTime, Date endTime, Integer status, Page p) {
        return userAgentsDividendMgDao.userAgentsDividendMgList(account, parentAccount, programName, begin, end, startTime, endTime, status, p);
    }

    public UserAgentsDividendMg findUserAgentsDividendMg(Integer id){
        return userAgentsDividendMgDao.find(id);
    }

    public void updateUserAgentsDividendMg(UserAgentsDividendMg m) {
        Integer id = m.getId();
        if(!StrUtils.hasEmpty(new Object[]{id})){
            Integer status = m.getStatus();
            if(status == 1) {
                String account = m.getAccount();
                BigDecimal distributionAmount = m.getDistributionAmount();
                User user = userDao.findByAccount(account);
                Integer test = user.getTest();
                Integer programId = m.getProgramId();
                UserAgentsDividend userAgentsDividend = userAgentsDividendDao.find(programId);
                String remarks = userAgentsDividend.getHint() + userAgentsDividend.getProportion() + "%";

                FinanceChange financeChange = new FinanceChange();
                financeChange.setChangeUser(account);
                financeChange.setChangeAmount(distributionAmount);
                financeChange.setTest(test);
                financeChange.setRemark(remarks);
                financeChange.setAccountChangeTypeId(30);

                this.financeChangeService.saveToApproval(financeChange, null, null);
            }

            userAgentsDividendMgDao.update(m);
        }else{
            throw new BaseCheckException("数据ID异常！");
        }
    }

    /**
     *
     * 周期分红派发计算
     * @param   calculationDate      派发日期 格式：yyyy-MM-dd HH:mm:ss
     * @throws  Exception
     */
    public void addUserAgentsDividendWhenNotExists(Date calculationDate) throws ParseException {

        if(StrUtils.hasEmpty(new Object[]{calculationDate})){
            throw new BaseCheckException("分红派发计算日期不能为空！");
        }

        //根据派发日期计算分红日期参数

        Integer cycleFlag = 0;

        String reportDate = DateUtils.formatDate(calculationDate);
        String[] dateElement = reportDate.split("-");
        String dayStr = dateElement[2];
        Integer dayInt = Integer.valueOf(dayStr);

        String reportDateBegin = "";
        String reportDateEnd = "";

        if(dayInt == 16){//半月分红标记
            cycleFlag = 1;
            reportDateBegin = dateElement[0] + "-" + dateElement[1] + "-01";
            reportDateEnd = dateElement[0] + "-" + dateElement[1] + "-15";
        }else if(dayInt == 1){//月分红标记
            cycleFlag = 2;
            //取时间参数的上个月的时间段
            Date lastMonthDate = DateUtils.getLastMonthByDate(calculationDate);
            Date firstDayDateOfMonth = DateUtils.getFirstDayDateOfMonth(lastMonthDate);
            Date lastDayOfMonth = DateUtils.getLastDayOfMonth(lastMonthDate);

            reportDateBegin = DateUtils.formatDate(firstDayDateOfMonth);
            reportDateEnd = DateUtils.formatDate(lastDayOfMonth);
        }

        if(cycleFlag == 1 || cycleFlag == 2) {

            if(userAgentsDividendMgDao.userAgentsDividendMgList(null, null, null, reportDate, reportDate, null, null, null, null).isEmpty()) {

                //userAgentsDividendMgDao.delete(reportDate);

                String[] userAgentsNatureAccounts = userAgentsNatureDao.userAgentsNatureAccounts(null, null, 1, null, null);

                if (userAgentsNatureAccounts.length > 0) {
                    List<UserAgentsDividend> userAgentsDividendList = userAgentsDividendList(null, cycleFlag, 1, 1, null);
                    if (!userAgentsDividendList.isEmpty()) {

                        List<AgentReport> agentReportListAll = cpsReportService.agentsReport(null, null, reportDateBegin, reportDateEnd, null, null);
                        List<AgentReport> agentReportList = new ArrayList<AgentReport>();
                        for (AgentReport agentReport : agentReportListAll) {
                            for (String account : userAgentsNatureAccounts) {
                                if (agentReport.getAccount().equals(account)) {
                                    agentReportList.add(agentReport);
                                    break;
                                }
                            }
                        }

                        BigDecimal profitNoOwn = BigDecimal.ZERO;//负盈利（不含本人）
                        Integer betPerCountNoOwn = 0;//投注人数（不含本人）
                        BigDecimal proportion = BigDecimal.ZERO;  //周期分红比例
                        Integer cycle = 0;
                        boolean CalculationFlag = false;
                        for (AgentReport agentReport : agentReportList) {

                            profitNoOwn = agentReport.getProfitNoOwn();
                            betPerCountNoOwn = agentReport.getBetPerCountNoOwn();
                            CalculationFlag = false;

                            String account = agentReport.getAccount(); //账户
                            String parentAccount = agentReport.getParentAccount();   //上级
                            BigDecimal actualNegativeProfit = profitNoOwn; //负盈利（元）— 不含代理本人
                            Integer actualActiveNumber = betPerCountNoOwn;   //活跃人数
                            Integer programId = 0;  //方案ID
                            String programName = ""; //方案名称
                            BigDecimal negativeProfit = BigDecimal.ZERO; //方案负盈利（元）— 不含代理本人
                            Integer activeNumber = 0;   //方案活跃人数
                            Integer status = 0; //状态 0：待处理、1：派发、2：不予派发

                            for (UserAgentsDividend userAgentsDividend : userAgentsDividendList) {
                                if (actualNegativeProfit.multiply(new BigDecimal("-1")).compareTo(userAgentsDividend.getNegativeProfit()) >= 0 && betPerCountNoOwn >= userAgentsDividend.getActiveNumber()) {
                                    proportion = userAgentsDividend.getProportion();
                                    programId = userAgentsDividend.getId();  //方案ID
                                    programName = userAgentsDividend.getProgramName(); //方案名称
                                    negativeProfit = userAgentsDividend.getNegativeProfit(); //方案团队销量（元）— 不含代理本人
                                    activeNumber = userAgentsDividend.getActiveNumber();   //方案活跃人数
                                    cycle = userAgentsDividend.getCycle();
                                    status = 0; //状态 0：待处理、1：派发、2：不予派发
                                    CalculationFlag = true;
                                    break;
                                }
                            }

                            if (CalculationFlag) {

                                BigDecimal distributionAmount = actualNegativeProfit.multiply(new BigDecimal("-1")).multiply(proportion.divide(new BigDecimal("100")));
                                distributionAmount = distributionAmount.setScale(0, BigDecimal.ROUND_HALF_UP);

                                UserAgentsDividendMg userAgentsDividendMg = new UserAgentsDividendMg();
                                userAgentsDividendMg.setReportDate(reportDate);
                                userAgentsDividendMg.setAccount(account);
                                userAgentsDividendMg.setParentAccount(parentAccount);
                                userAgentsDividendMg.setDistributionAmount(distributionAmount);
                                userAgentsDividendMg.setActualNegativeProfit(actualNegativeProfit);
                                userAgentsDividendMg.setActualActiveNumber(actualActiveNumber);
                                userAgentsDividendMg.setProgramId(programId);
                                userAgentsDividendMg.setProgramName(programName);
                                userAgentsDividendMg.setNegativeProfit(negativeProfit);
                                userAgentsDividendMg.setProportion(proportion);
                                userAgentsDividendMg.setActiveNumber(activeNumber);
                                userAgentsDividendMg.setCycle(cycle);
                                userAgentsDividendMg.setStatus(status);

                                userAgentsDividendMgDao.save(userAgentsDividendMg);

                            }

                        }
                    }
                } else {
                    logger.info("日期为[" + reportDate + "]的周期分红无账户需要生成数据！");
                }

            }
        }
    }


    //*************************************************************周期分红End*************************************************************


    //*************************************************************周期分红彩种加奖Start*************************************************************

    /**
     * 查询
     *
     * @param programName           方案名称
     * @param status                状态 0：关闭、1：开启
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDividendLottery> userAgentsDividendLotteryList(String programName, Integer cycle, Integer status, Integer sorting, Page p) {
        return userAgentsDividendLotteryDao.userAgentsDividendLotteryList(programName, cycle, status, sorting, p);
    }

    public UserAgentsDividendLottery findUserAgentsDividendLottery(Integer id){
        return userAgentsDividendLotteryDao.find(id);
    }

    public void saveUserAgentsDividendLottery(UserAgentsDividendLottery m) {
        Integer id = m.getId();

        String programName = m.getProgramName();
        if(StrUtils.hasEmpty(new Object[]{programName})) {
            throw new BaseCheckException("方案名称不能为空！");
        }

        BigDecimal negativeProfit = m.getNegativeProfit();
        if(StrUtils.hasEmpty(new Object[]{negativeProfit})) {
            throw new BaseCheckException("负盈利值不能为空！");
        }
        if(negativeProfit.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("负盈利值金额设置异常！");
        }

        BigDecimal proportion = m.getProportion();
        if(StrUtils.hasEmpty(new Object[]{proportion})) {
            throw new BaseCheckException("周期分红比例不能为空！");
        }
        if(proportion.compareTo(BigDecimal.ZERO) <= 0){
            throw new BaseCheckException("周期分红比例设置异常！");
        }

        Integer activeNumber = m.getActiveNumber();
        if(StrUtils.hasEmpty(new Object[]{activeNumber})) {
            throw new BaseCheckException("活跃人数不能为空！");
        }

        Integer cycle = m.getCycle();
        if(StrUtils.hasEmpty(new Object[]{cycle})) {
            throw new BaseCheckException("计算周期不能为空！");
        }
        if(cycle!=1 && cycle!=2){
            throw new BaseCheckException("计算周期数据异常！");
        }

        Integer status = m.getStatus();
        if(StrUtils.hasEmpty(new Object[]{status})) {
            throw new BaseCheckException("状态不能为空！");
        }
        if(status!=0 && status!=1){
            throw new BaseCheckException("状态异常！");
        }

        String lotteryId = m.getLotteryId();
        String lotteryName = lotteryDao.find(lotteryId).getTitle();
        m.setLotteryName(lotteryName);

        if(!StrUtils.hasEmpty(new Object[]{id})){
            userAgentsDividendLotteryDao.update(m);
        }else{
            userAgentsDividendLotteryDao.save(m);
        }
    }


    /**
     * 查询
     *
     * @param account               用户名称
     * @param parentAccount         上级
     * @param programName           方案名称
     * @param begin                 计算日期-开始
     * @param end                   计算日期-结束
     * @param startTime             修改时间-开始
     * @param endTime               修改时间-结束
     * @param status                状态 0：待处理、1：派发、2：不予派发
     * @param p                     分页对象
     * @return
     */
    public List<UserAgentsDividendLotteryMg> userAgentsDividendLotteryMgList(String account, String parentAccount, String programName, String begin, String end, Date startTime, Date endTime, Integer status, Page p) {
        return userAgentsDividendLotteryMgDao.userAgentsDividendLotteryMgList(account, parentAccount, programName, begin, end, startTime, endTime, status, p);
    }

    public UserAgentsDividendLotteryMg findUserAgentsDividendLotteryMg(Integer id){
        return userAgentsDividendLotteryMgDao.find(id);
    }

    public void updateUserAgentsDividendLotteryMg(UserAgentsDividendLotteryMg m) {
        Integer id = m.getId();
        if(!StrUtils.hasEmpty(new Object[]{id})){
            Integer status = m.getStatus();
            if(status == 1) {
                String account = m.getAccount();
                BigDecimal distributionAmount = m.getDistributionAmount();
                User user = userDao.findByAccount(account);
                Integer test = user.getTest();
                Integer programId = m.getProgramId();
                UserAgentsDividendLottery userAgentsDividendLottery = userAgentsDividendLotteryDao.find(programId);
                String remarks = userAgentsDividendLottery.getHint() + userAgentsDividendLottery.getProportion() + "%";

                FinanceChange financeChange = new FinanceChange();
                financeChange.setChangeUser(account);
                financeChange.setChangeAmount(distributionAmount);
                financeChange.setTest(test);
                financeChange.setRemark(remarks);
                financeChange.setAccountChangeTypeId(30);

                this.financeChangeService.saveToApproval(financeChange, null, null);
            }
            userAgentsDividendLotteryMgDao.update(m);
        }else{
            throw new BaseCheckException("数据ID异常！");
        }
    }

    /**
     *
     * 周期分红彩种加奖派发计算
     * @param   calculationDate      派发日期 格式：yyyy-MM-dd HH:mm:ss
     * @throws  Exception
     */
    public void addUserAgentsDividendLotteryWhenNotExists(Date calculationDate) throws ParseException {

        if(StrUtils.hasEmpty(new Object[]{calculationDate})){
            throw new BaseCheckException("分红彩种加奖派发计算日期不能为空！");
        }

        //根据派发日期计算分红日期参数

        Integer cycleFlag = 0;

        String reportDate = DateUtils.formatDate(calculationDate);
        String[] dateElement = reportDate.split("-");
        String dayStr = dateElement[2];
        Integer dayInt = Integer.valueOf(dayStr);

        String reportDateBegin = "";
        String reportDateEnd = "";

        if(dayInt == 16){//半月分红标记
            cycleFlag = 1;
            reportDateBegin = dateElement[0] + "-" + dateElement[1] + "-01" + " 00:00:00";
            reportDateEnd = dateElement[0] + "-" + dateElement[1] + "-15" + " 23:59:59";
        }else if(dayInt == 1){//月分红标记
            cycleFlag = 2;
            //取时间参数的上个月的时间段
            Date lastMonthDate = DateUtils.getLastMonthByDate(calculationDate);
            Date firstDayDateOfMonth = DateUtils.getFirstDayDateOfMonth(lastMonthDate);
            Date lastDayOfMonth = DateUtils.getLastDayOfMonth(lastMonthDate);

            reportDateBegin = DateUtils.formatDate(firstDayDateOfMonth) + " 00:00:00";
            reportDateEnd = DateUtils.formatDate(lastDayOfMonth) + " 00:00:00";
        }

        if(cycleFlag == 1 || cycleFlag == 2) {

            if(userAgentsDividendLotteryMgDao.userAgentsDividendLotteryMgList(null, null, null, reportDate, reportDate, null, null, null, null).isEmpty()) {

                Date reportBegin = DateUtils.toDate(reportDateBegin, "yyyy-MM-dd HH:mm:ss");
                Date reportEnd = DateUtils.toDate(reportDateEnd, "yyyy-MM-dd HH:mm:ss");

                //userAgentsDividendLotteryMgDao.delete(reportDate);

                String[] userAgentsNatureAccounts = userAgentsNatureDao.userAgentsNatureAccounts(null, null, null, null, 1);

                if (userAgentsNatureAccounts.length > 0) {
                    List<UserAgentsDividendLottery> userAgentsDividendLotteryList = userAgentsDividendLotteryList(null, cycleFlag, 1, 1, null);
                    if (!userAgentsDividendLotteryList.isEmpty()) {

                        List<AgentLotteryReport> agentLotteryReportListZ = cpsReportService.agentLotteryReportList(null, null, reportBegin, reportEnd, null);
                        List<AgentLotteryReport> agentLotteryReportList = new ArrayList<AgentLotteryReport>();
                        for (AgentLotteryReport agentLotteryReport : agentLotteryReportListZ) {
                            for (String account : userAgentsNatureAccounts) {
                                if (agentLotteryReport.getAccount().equals(account)) {
                                    agentLotteryReportList.add(agentLotteryReport);
                                }
                            }
                        }

                        BigDecimal profitNoOwn = BigDecimal.ZERO;//负盈利（不含本人）
                        Integer betPerCountNoOwn = 0;//投注人数（不含本人）
                        BigDecimal proportion = BigDecimal.ZERO;  //周期分红比例
                        Integer cycle = 0;
                        boolean CalculationFlag = false;
                        for (AgentLotteryReport agentLotteryReport : agentLotteryReportList) {

                            profitNoOwn = agentLotteryReport.getProfitNoOwn();
                            betPerCountNoOwn = agentLotteryReport.getBetPerCountNoOwn();
                            CalculationFlag = false;

                            String account = agentLotteryReport.getAccount(); //账户
                            //String parentAccount = agentLotteryReport.getParentAccount();   //上级
                            BigDecimal actualNegativeProfit = profitNoOwn; //负盈利（元）— 不含代理本人
                            Integer actualActiveNumber = betPerCountNoOwn;   //活跃人数
                            Integer programId = 0;  //方案ID
                            String programName = ""; //方案名称
                            BigDecimal negativeProfit = BigDecimal.ZERO; //方案负盈利（元）— 不含代理本人
                            Integer activeNumber = 0;   //方案活跃人数
                            Integer status = 0; //状态 0：待处理、1：派发、2：不予派发

                            String lotteryId = agentLotteryReport.getLotteryId(); //彩种ID
                            String lotteryName = agentLotteryReport.getLotteryName(); //彩种名称

                            for (UserAgentsDividendLottery userAgentsDividendLottery : userAgentsDividendLotteryList) {
                                if (userAgentsDividendLottery.getLotteryId().equals(lotteryId) && actualNegativeProfit.multiply(new BigDecimal("-1")).compareTo(userAgentsDividendLottery.getNegativeProfit()) >= 0 && betPerCountNoOwn >= userAgentsDividendLottery.getActiveNumber()) {
                                    proportion = userAgentsDividendLottery.getProportion();
                                    programId = userAgentsDividendLottery.getId();  //方案ID
                                    programName = userAgentsDividendLottery.getProgramName(); //方案名称
                                    negativeProfit = userAgentsDividendLottery.getNegativeProfit(); //方案团队销量（元）— 不含代理本人
                                    activeNumber = userAgentsDividendLottery.getActiveNumber();   //方案活跃人数
                                    status = 0; //状态 0：待处理、1：派发、2：不予派发
                                    cycle = userAgentsDividendLottery.getCycle();
                                    CalculationFlag = true;
                                    break;
                                }
                            }

                            if (CalculationFlag) {

                                BigDecimal distributionAmount = actualNegativeProfit.multiply(new BigDecimal("-1")).multiply(proportion.divide(new BigDecimal("100")));
                                distributionAmount = distributionAmount.setScale(0, BigDecimal.ROUND_HALF_UP);

                                UserAgentsDividendLotteryMg userAgentsDividendLotteryMg = new UserAgentsDividendLotteryMg();
                                userAgentsDividendLotteryMg.setReportDate(reportDate);
                                userAgentsDividendLotteryMg.setAccount(account);
                                //userAgentsDividendLotteryMg.setParentAccount(parentAccount);
                                userAgentsDividendLotteryMg.setDistributionAmount(distributionAmount);
                                userAgentsDividendLotteryMg.setActualNegativeProfit(actualNegativeProfit);
                                userAgentsDividendLotteryMg.setActualActiveNumber(actualActiveNumber);
                                userAgentsDividendLotteryMg.setProgramId(programId);
                                userAgentsDividendLotteryMg.setProgramName(programName);
                                userAgentsDividendLotteryMg.setNegativeProfit(negativeProfit);
                                userAgentsDividendLotteryMg.setProportion(proportion);
                                userAgentsDividendLotteryMg.setActiveNumber(activeNumber);
                                userAgentsDividendLotteryMg.setCycle(cycle);
                                userAgentsDividendLotteryMg.setStatus(status);
                                userAgentsDividendLotteryMg.setLotteryId(lotteryId);
                                userAgentsDividendLotteryMg.setLotteryName(lotteryName);

                                userAgentsDividendLotteryMgDao.save(userAgentsDividendLotteryMg);

                            }

                        }
                    }
                } else {
                    logger.info("日期为[" + reportDate + "]的周期分红彩种加奖无账户需要生成数据！");
                }

            }
        }
    }

    //*************************************************************周期分红彩种加奖End*************************************************************








}
