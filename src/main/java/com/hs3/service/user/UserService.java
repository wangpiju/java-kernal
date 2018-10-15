package com.hs3.service.user;

import com.hs3.dao.approval.ApprovalDao;
import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.extcode.ExtCodeDao;
import com.hs3.dao.lotts.BonusGroupDao;
import com.hs3.dao.user.DailyAccDao;
import com.hs3.dao.user.DailyRuleDao;
import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserQuotaDao;
import com.hs3.dao.user.UserSafeDao;
import com.hs3.db.Page;
import com.hs3.entity.approval.Approval;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.sys.IpStore;
import com.hs3.entity.users.DailyAcc;
import com.hs3.entity.users.DailyRule;
import com.hs3.entity.users.ExtCode;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserQuota;
import com.hs3.entity.users.UserSafe;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.models.Jsoner;
import com.hs3.models.user.RootUser;
import com.hs3.models.user.RootUserAddModel;
import com.hs3.models.user.UserModel;
import com.hs3.service.sys.IpStoreService;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ListUtils;
import com.hs3.utils.PagerUtils;
import com.hs3.utils.RedisUtils;
import com.hs3.utils.StrUtils;
import com.hs3.utils.ip.IPSeeker;
import com.hs3.utils.sec.Des;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final int freeCount = 5;
    private static final String DES_KEY = "sJc@v9#b";
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserQuotaDao userQuotaDao;
    @Autowired
    private ExtCodeDao extDao;
    @Autowired
    private BonusGroupDao bonusGroupDao;
    @Autowired
    private UserSafeDao userSafeDao;
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private DailyAccDao dailyAccDao;
    @Autowired
    private DailyRuleDao dailyRuleDao;
    @Autowired
    private ApprovalDao approvalDao;
    @Autowired
    private IpStoreService ipStoreService;
    @Autowired
    private UserLoginIpService userLoginIpService;

    public List<User> listByParent(String account) {
        return this.userDao.listByParent(account);
    }

    public int setBet(String account) {
        return this.userDao.setBet(account);
    }

    public boolean updateRechargeInfo(User m) {
        return this.userDao.updateRechargeInfo(m) > 0;
    }

    public List<RootUser> listAccountWithTeam(String account, Integer bonusGroup, BigDecimal beginRebateRatio, BigDecimal endRebateRatio, BigDecimal beginAmount, BigDecimal endAmount, Integer regTime, Integer userType, Integer order, Integer test, Integer status, Integer userMark, Page p) {
        return this.userDao.listAccountWithTeam(account, bonusGroup, beginRebateRatio, endRebateRatio, beginAmount, endAmount, regTime, userType, order, test, status, userMark, p);
    }

    public List<Map<String, Object>> listAccountWithTeamByUser(String account, BigDecimal beginAmount, BigDecimal endAmount, Integer userType, Page p, String parent, String rootAccount) {
        return this.userDao.listAccountWithTeamByUser(account, beginAmount, endAmount, userType, p, parent, rootAccount);
    }

    public List<RootUser> listWithTeam(String account, Page p) {
        return this.userDao.listWithTeam(account, p);
    }

    public void save(User user) {
        this.userDao.save(user);
    }

    public int saveByRegist(String account, String passWord, BigDecimal rebateRatio, String ip, Integer userType, User parent) {
        BonusGroup bg = this.bonusGroupDao.findById(parent.getBonusGroupId().intValue());
        if (bg == null) {
            return 1;
        }
        if (this.userDao.findRecordByAccount(account) > 0) {
            return 3;
        }
        BigDecimal userMinRatio = bg.getUserMinRatio();

        BigDecimal noneMinRatio = bg.getNoneMinRatio();

        BigDecimal max = parent.getRebateRatio().subtract(userMinRatio);
        if (rebateRatio.compareTo(max) > 0) {
            return 2;
        }
        if ((userType.intValue() == 0) && (rebateRatio.compareTo(bg.getPlayerMaxRatio()) > 0)) {
            return 2;
        }
        if (rebateRatio.compareTo(noneMinRatio) > 0) {
            if (this.userQuotaDao.findRecordByAccount(parent.getAccount(), rebateRatio) <= 0) {
                return 4;
            }
            if (userType.intValue() == 1) {
                BigDecimal zero = new BigDecimal("0");
                for (BigDecimal i = rebateRatio.subtract(userMinRatio); i.compareTo(noneMinRatio) == 1; i = i.subtract(userMinRatio)) {
                    UserQuota m = new UserQuota();
                    m.setAccount(account);
                    m.setRebateRatio(i);
                    m.setNum(Integer.valueOf(0));
                    this.userQuotaDao.save(m);
                    if (userMinRatio.compareTo(zero) == 0) {
                        userMinRatio = new BigDecimal("0.1");
                    }
                }
            }
        }
        User user = new User();
        user.setAccount(account);
        user.setPassword(passWord);


        user.setRebateRatio(rebateRatio);


        user.setBonusGroupId(parent.getBonusGroupId());
        user.setUserType(userType);
        user.setTest(parent.getTest());
        Date date = new Date();
        user.setRegTime(date);
        user.setLoginTime(date);
        user.setParentAccount(parent.getAccount());
        user.setRootAccount(parent.getRootAccount());
        user.setParentList(parent.getParentList() + account + ",");
        user.setDepositStatus(Integer.valueOf(0));
        user.setRegIp(ip);
        user.setRegIpInfo(IPSeeker.getInstance().getAddress(ip));
        this.userDao.save(user);

        this.userDao.saveOnLine(account);
        this.userQuotaDao.updateNum(parent.getAccount(), rebateRatio, 1);

        return 0;
    }

    public int saveByCode(String account, String password, String extCode, String ip, String ipAddress, String userAgent) {
        ExtCode extcode = this.extDao.findByExtCode(extCode);
        if ((extcode != null) && ("0".equals(extcode.getStatus()))) {
            long validtime = extcode.getValidtime().intValue();
            if (validtime != 0L) {
                validtime *= 86400000L;
                long dtime = extcode.getCreatetime().getTime();
                long now = new Date().getTime();
                if (dtime + validtime < now) {
                    return 1;
                }
            }
            User user = null;
            if (("1".equals(extcode.getCanregists())) &&
                    (this.extDao.findRegistersByExtCode(extCode, ip) > 0)) {
                return 2;
            }
            if (this.userDao.findRecordByAccount(account) > 0) {
                return 4;
            }
            User parent = this.userDao.findByAccount(extcode.getAccount());
            BonusGroup bonusGroup = this.bonusGroupDao.findById(extcode.getBonusgroupid().intValue());

            double max = parent.getRebateRatio().subtract(bonusGroup.getUserMinRatio()).doubleValue();
            if ((extcode.getRebateratio().doubleValue() < 0.0D) || (extcode.getRebateratio().doubleValue() > max)) {
                return 5;
            }
            user = new User();
            user.setAccount(account);
            user.setPassword(password);


            user.setRebateRatio(extcode.getRebateratio());


            user.setBonusGroupId(bonusGroup.getId());
            user.setUserType(Integer.valueOf(Integer.parseInt(extcode.getUsertype())));
            user.setTest(parent.getTest());
            Date date = new Date();
            user.setRegTime(date);
            user.setLoginTime(date);
            user.setParentAccount(parent.getAccount());
            user.setRootAccount(parent.getRootAccount());
            user.setParentList(parent.getParentList() + account + ",");
            user.setDepositStatus(Integer.valueOf(0));
            user.setRegIp(ip);
            user.setRegIpInfo(ipAddress);
            this.userDao.save(user);

            this.userDao.saveUserIp(account, ip, new Date(), ipAddress, userAgent, parent.getAccount(), null, null, null);

            this.userDao.saveOnLine(account);
            this.extDao.savelink(extCode, account, new Date(), ipAddress);
            this.extDao.updateLastRegistByCode(extCode, new Date());
            return 0;
        }
        return 3;
    }

    public void addRootUser(RootUserAddModel m) {
        User u = m.getUser();
        u.setParentAccount(u.getAccount());
        u.setRootAccount(u.getAccount());
        u.setParentList(u.getAccount() + ",");
        u.setUserType(Integer.valueOf(1));
        u.setPassword("aa123456");
        this.userDao.save(u);

        this.userDao.saveOnLine(u.getAccount());
        this.userDao.addDomain(u.getAccount(), m.getDomain());


        List<UserQuota> list = m.getQuota();
        if (list != null) {
            for (UserQuota quota : list) {
                quota.setAccount(u.getAccount());
                this.userQuotaDao.save(quota);
            }
        }
    }

    public Jsoner saveIplogin(String account, String password, boolean validCode, String ip, String agent, String sessionId, String sid, String token, String cardName) {
        if (StrUtils.hasEmpty(new Object[]{account, password})) {
            return Jsoner.getInstance(0, "账号或密码不能为空");
        }
        if (!Pattern.matches("^[a-zA-Z][\\w]{5,20}", account)) {
            return Jsoner.getInstance(3, "请输入有效的用户名");
        }
        User u = this.userDao.findByAccount(account);
        if (u == null) {
            return Jsoner.getInstance(0, "账号或密码错误");
        }
        if (u.getLoginStatus().intValue() != 0) {
            if (u.getLoginErrorCount().intValue() >= 5) {
                return Jsoner.getInstance(3, "登录密码错误5次，账号已被冻结。请联系客服进行解冻");
            }
            return Jsoner.getInstance(3, "该账户存在风险，请及时沟通上级进行调整");
        }
        if ((u.getLoginErrorCount().intValue() >= 3) && (!validCode)) {
            return Jsoner.getInstance(2, "验证码错误");
        }
        if (!password.equals(StrUtils.MD5(u.getPassword() + token))) {
            int errorCount = u.getLoginErrorCount().intValue() + 1;
            int status = 0;
            if (errorCount >= 5) {
                status = 1;
                logger.info(account + "：登录错误达到：" + errorCount + "次,已冻结");
            }
            this.userDao.updateLoginStatusAndErrorCount(account, null, errorCount, status, "5times_failed_login");
            return Jsoner.getInstance(0, "账号或密码错误");
        }
        if ((cardName != null) && (this.bankUserDao.findCardByAccountAndName(account, cardName) <= 0)) {
            return Jsoner.error("姓名不正确");
        }
        Date dd = new Date();

//        String address = IPSeeker.getInstance().getAddress(ip);
        String address = "未知";
        IpStore ipStore = ipStoreService.findByIp(ip);
        if (ipStore != null) {
            address = ipStoreService.transIpAddress(ipStore);
        }
        String mac = null;
        String hard = null;
        String cpu = null;
        if (!StrUtils.hasEmpty(new Object[]{sid})) {
            try {
                byte[] src = Hex.decodeHex(sid.toCharArray());
                String c = Des.decrypt_des_cbc(src, "sJc@v9#b".getBytes());
                List<String> sidList = ListUtils.toList(c);
                if (sidList.size() == 3) {
                    mac = (String) sidList.get(0);
                    hard = (String) sidList.get(1);
                    cpu = (String) sidList.get(2);
                }
            } catch (Exception e) {
                mac = sid;
            }
        }
        this.userDao.saveUserIp(u.getAccount(), ip, dd, address, agent, u.getParentAccount(), mac, hard, cpu);
        this.userDao.updateLoginStatusAndErrorCount(account, dd, 0, 0, "");
        this.userDao.setSessionId(u.getAccount(), sessionId);
        if (ipStore == null) {
            ipStoreService.ipTransThread(ip);
        }
        return Jsoner.success(u);
    }

    public User findByAccount(String account) {
        return this.userDao.findByAccount(account);
    }

    public User findByAccountMaster(String account) {
        return this.userDao.findByAccountMaster(account);
    }

    public User findIsDown(String parentAccount, String downAccount) {
        return this.userDao.findByAccountAndParent(parentAccount, downAccount);
    }

    public String findTeamAmount(String account) {
        return this.userDao.findTeamAmount(account);
    }

    public String findMessage(String account) {
        return this.userDao.findMessage(account);
    }

    public Map<String, Object> findUserInformation(String account) {
        return this.userDao.findUserInformation(account);
    }

    public Map<String, Object> findSafeInformation(String account) {
        Map<String, Object> map = new HashMap();
        map.put("safeInformation", this.userDao.findSafeInformation(account));
        map.put("lastFiveLogin", this.userDao.getLastFiveLogin(account));
        return map;
    }

    public int delete(List<String> accounts) {
        return this.userDao.deleteByAccount(accounts);
    }

    public int setStatus(String account, String manager, Integer statusType, String remark, Integer loginStatus, Integer betStatus, Integer betInStatus, Integer depositStatus) {
        if (StrUtils.hasEmpty(new Object[]{remark})) {
            return -1;
        }
        return this.userDao.setStatus(account, manager, statusType, remark, loginStatus, betStatus, betInStatus, depositStatus);
    }

    public int setBankGroup(String account, String bankGroup) {
        return this.userDao.setBankGroup(account, bankGroup);
    }

    public int updateDailyRule(String account, Integer oldDailyRuleId, Integer dailyRuleId) {
        if (oldDailyRuleId != null) {
            this.userDao.deleteDailyRuleByRootAccount(account);
            this.dailyAccDao.deleteByRootAccount(account);
        }
        int i = this.userDao.updateDailyRule(account, dailyRuleId);
        if (dailyRuleId != null) {
            this.userDao.updateDailyWagesStatusOpen(account, account);

            DailyRule dailyRule = (DailyRule) this.dailyRuleDao.find(dailyRuleId);
            User user = this.userDao.findByAccount(account);
            if (user.getParentList().split(",").length == dailyRule.getLevel().intValue()) {
                this.dailyAccDao.save(dailyRule.getId(), dailyRule.getRate(), dailyRule.getBetAmount(), account, account, account, user.getParentList());
            }
        }
        return i;
    }

    public int updatePassword(String account, String password) {
        return this.userDao.updatePassword(account, password);
    }

    public int updatePasswordByUser(String account, String oldPass, String newPass, String niceName, String qq, String email, String phone, String message) {
        /**jd-gui
         int j = this.userDao.findRecordBySafeWord(account, newPass);
         if (j > 0) {
         return -1;
         }
         int i = this.userDao.updatePasswordByUser(account, oldPass, newPass);
         if (i > 0)
         {
         if (StrUtils.hasEmpty(new Object[] { niceName })) {
         if (StrUtils.hasEmpty(new Object[] { qq })) {
         if (StrUtils.hasEmpty(new Object[] { email })) {
         if (StrUtils.hasEmpty(new Object[] { phone })) {
         if (StrUtils.hasEmpty(new Object[] { message })) {
         break label129;
         }
         }
         }
         }
         }
         this.userDao.updateInformation(account, niceName, qq, email, phone, message);
         label129:
         return i;
         }
         return 0;*/

        int j = userDao.findRecordBySafeWord(account, newPass);
        if (j > 0)
            return -1;
        int i = userDao.updatePasswordByUser(account, oldPass, newPass);
        if (i > 0) {
            if (!StrUtils.hasEmpty(new Object[]{
                    niceName
            }) || !StrUtils.hasEmpty(new Object[]{
                    qq
            }) || !StrUtils.hasEmpty(new Object[]{
                    email
            }) || !StrUtils.hasEmpty(new Object[]{
                    phone
            }) || !StrUtils.hasEmpty(new Object[]{
                    message
            }))
                userDao.updateInformation(account, niceName, qq, email, phone, message);
            return i;
        } else {
            return 0;
        }

    }

    public int updatePassWordBySafe(String account, String safePass, String newPass) {
        if (this.userDao.findByAccountAndSafeWord(account, safePass) == null) {
            return 1;
        }
        this.userDao.updatePassword(account, newPass);
        return 0;
    }

    public int updateSafeWordByUser(String account, String oldPass, String newPass) {
        /**jd-gui
         * int j = this.userDao.findRecordByPassWord(account, newPass);
         if (j > 0) {
         return -1;
         }
         int i;
         int i;
         if (StrUtils.hasEmpty(new Object[] { this.userDao.findByAccount(account).getSafePassword() })) {
         i = this.userDao.updateSafePassword(account, newPass);
         } else {
         i = this.userDao.updateSafePasswordByUser(account, oldPass, newPass);
         }
         return i;*/

        int j = userDao.findRecordByPassWord(account, newPass);
        if (j > 0)
            return -1;
        int i;
        if (StrUtils.hasEmpty(new Object[]{
                userDao.findByAccount(account).getSafePassword()
        }))
            i = userDao.updateSafePassword(account, newPass);
        else
            i = userDao.updateSafePasswordByUser(account, oldPass, newPass);
        return i;

    }

    public int updateSafeWordByQuestions(String account, String qsType1, String answer1, String qsType2, String answer2, String safePassWord) {
        int j = this.userDao.findRecordByPassWord(account, safePassWord);
        if (j > 0) {
            return -1;
        }
        if (this.userSafeDao.findByQuestionAndAnswer(account, qsType1, answer1, qsType2, answer2) <= 0) {
            return 1;
        }
        this.userDao.updateSafePassword(account, safePassWord);
        return 0;
    }

    public int deleteUserSafe(String account) {
        return this.userSafeDao.deleteAccount(account);
    }

    public void updateIpInfo(User u) {
        this.userDao.updateIpInfo(u);
    }

    public int updateBankStatus(String account) {
        return this.userDao.updateBankStatus(account);
    }

    public int updateBankStatus(String account, Integer status) {
        return this.userDao.updateBankStatus(account, status);
    }

    public int updateInformation(String account, String name, String qq, String email, String phone, String message) {
        return this.userDao.updateInformation(account, name, qq, email, phone, message);
    }

    public int updateSafePassword(String account, String password) {
        return this.userDao.updateSafePassword(account, password);
    }

    public List<UserQuota> listQuota(String account) {
        return this.userQuotaDao.listByAccount(account);
    }

    public void updateQuota(UserQuota m) {
        this.userQuotaDao.update(m);
    }

    public void updateUserSessionId(String sessionId) {
        this.userDao.updateUserSessionId(sessionId);
    }

    public int updateQuotaByUser(String parent, List<UserQuota> list) {
        for (Iterator<UserQuota> iterator = list.iterator(); iterator.hasNext(); ) {
            UserQuota userQuota = (UserQuota) iterator.next();

            int j = this.userDao.findRecord(userQuota.getAccount(), parent);
            if (j <= 0) {
                return 2;
            }
            int i = this.userQuotaDao.findNum(parent, Double.valueOf(userQuota.getRebateRatio().doubleValue()));
            if (i >= userQuota.getNum().intValue()) {
                int k = this.userQuotaDao.updateAndAddNum(userQuota.getAccount(), parent, Double.valueOf(userQuota.getRebateRatio().doubleValue()), userQuota.getNum().intValue());
                if (k <= 0) {
                    return 1;
                }
            } else {
                return 1;
            }
        }
        return 0;
    }

    public int updateByAdjustQuota(User parent, String account, BigDecimal rebateRatio) {
        User u = this.userDao.findByAccountAndParent(parent.getAccount(), account);
        if (u == null) {
            return 2;
        }
        if (rebateRatio.compareTo(u.getRebateRatio()) <= 0) {
            return 4;
        }
        BonusGroup bg = this.bonusGroupDao.findById(parent.getBonusGroupId().intValue());
        if (bg == null) {
            return 1;
        }
        BigDecimal noneMinRatio = bg.getNoneMinRatio();
        BigDecimal userMinRatio = bg.getUserMinRatio();
        BigDecimal max = parent.getRebateRatio().subtract(userMinRatio);
        if ((rebateRatio.compareTo(noneMinRatio) == -1) || (rebateRatio.compareTo(noneMinRatio) == 0)) {
            this.userDao.updateUserRebateRatio(account, parent.getAccount(), rebateRatio);


            return 0;
        }
        if (rebateRatio.compareTo(max) == 1) {
            return 6;
        }
        if ((u.getUserType().intValue() == 0) && (rebateRatio.compareTo(bg.getPlayerMaxRatio()) == 1)) {
            return 5;
        }
        if (this.userQuotaDao.updateNum(parent.getAccount(), rebateRatio, 1) == 0) {
            return 3;
        }
        this.userDao.updateUserRebateRatio(account, parent.getAccount(), rebateRatio);
        this.userDao.updateParentRebateRatioNum(parent.getAccount(), u.getRebateRatio().doubleValue());
        if (u.getUserType().intValue() == 1) {
            BigDecimal zero = new BigDecimal("0");
            for (BigDecimal i = rebateRatio.subtract(userMinRatio); i.compareTo(noneMinRatio) == 1; i = i.subtract(userMinRatio)) {
                UserQuota m = new UserQuota();
                m.setAccount(account);
                m.setRebateRatio(i);
                m.setNum(Integer.valueOf(0));
                if (this.userQuotaDao.findRecord(account, i) <= 0) {
                    this.userQuotaDao.save(m);
                }
                if (userMinRatio.compareTo(zero) == 0) {
                    userMinRatio = new BigDecimal("0.1");
                }
            }
        }
        return 0;
    }

    public int saveQsAndSafeWord(String account, String qsType1, String answer1, String qsType2, String answer2, String safePassWord) {
        int i = this.userSafeDao.findByAccount(account);
        if (i > 0) {
            return 1;
        }
        UserSafe us = new UserSafe();
        us.setAccount(account);
        us.setQstype1(qsType1);
        us.setAnswer1(answer1);
        us.setQstype2(qsType2);
        us.setAnswer2(answer2);
        us.setCreatetime(new Date());
        this.userSafeDao.save(us);
        if (!StrUtils.hasEmpty(new Object[]{safePassWord})) {
            this.userDao.updateSafePassword(account, safePassWord);
        }
        return 0;
    }

    public List<Map<String, Object>> listUserExtCode(String account, Page p) {
        return this.extDao.listUserExtCode(account, p);
    }

    public boolean setLogout(String account) {
        return this.userDao.setSessionId(account, "");
    }

    public int updateUserClear() {
        return this.userDao.updateUserClear();
    }

    public String getSessionId(String account) {
        return this.userDao.getSessionId(account);
    }

    public BigDecimal getAmount(String account) {
        return this.userDao.getAmount(account);
    }

    public List<User> getAccountList(Integer test) {
        return this.userDao.getAccountList(test);
    }

    public BigDecimal findMaxRebateRatioByParent(String account) {
        return this.userDao.findMaxRebateRatioByParent(account);
    }

    public Jsoner updateQuotaByAdmin(String account, BigDecimal rebateRatio) {
        User u = this.userDao.findByAccount(account);
        if (u == null) {
            return Jsoner.error("用户不存在");
        }
        if (rebateRatio == null) {
            return Jsoner.error("请选择返点");
        }
        BonusGroup bonusGroup = (BonusGroup) this.bonusGroupDao.find(u.getBonusGroupId());
        if (bonusGroup == null) {
            return Jsoner.error("奖金组错误：" + u.getBonusGroupId());
        }
        BigDecimal userMinRatio = bonusGroup.getUserMinRatio();


        BigDecimal maxRebateRatio = null;


        BigDecimal minRebateRatio = this.userDao.findMaxRebateRatioByParent(u.getAccount());
        if (u.getAccount().equals(u.getParentAccount())) {
            maxRebateRatio = bonusGroup.getRebateRatio();
            if (minRebateRatio == null) {
                minRebateRatio = new BigDecimal("0");
            } else {
                minRebateRatio = minRebateRatio.add(userMinRatio);
            }
        } else {
            User parent = this.userDao.findByAccount(u.getParentAccount());
            if (u.getUserType().intValue() == 0) {
                if (parent.getRebateRatio().compareTo(bonusGroup.getPlayerMaxRatio()) > 0) {
                    maxRebateRatio = bonusGroup.getPlayerMaxRatio();
                } else {
                    maxRebateRatio = parent.getRebateRatio().subtract(userMinRatio);
                }
                minRebateRatio = new BigDecimal("0");
            } else {
                maxRebateRatio = parent.getRebateRatio().subtract(userMinRatio);
                if (minRebateRatio == null) {
                    minRebateRatio = new BigDecimal("0");
                } else {
                    minRebateRatio = minRebateRatio.add(userMinRatio);
                }
            }
        }
        if ((rebateRatio.compareTo(minRebateRatio) < 0) || (rebateRatio.compareTo(maxRebateRatio) > 0)) {
            return Jsoner.error("可调整返点范围：" + minRebateRatio + "~" + maxRebateRatio);
        }
        if (this.userDao.updateUserRebateRatio(u.getAccount(), u.getParentAccount(), rebateRatio) == 0) {
            return Jsoner.error("调整返点时，账户信息错误：" + u.getAccount() + "," + u.getParentAccount());
        }
        if (u.getUserType().intValue() == 1) {
            BigDecimal oldUserRatio = u.getRebateRatio();
            if (rebateRatio.compareTo(oldUserRatio) > 0) {
                BigDecimal noneRatio = bonusGroup.getNoneMinRatio();

                BigDecimal stepRatio = userMinRatio;
                if (userMinRatio.compareTo(new BigDecimal("0")) == 0) {
                    stepRatio = new BigDecimal("0.1");
                }
                for (BigDecimal i = rebateRatio.subtract(userMinRatio); i.compareTo(noneRatio) > 0; i = i.subtract(stepRatio)) {
                    UserQuota m = new UserQuota();
                    m.setAccount(account);
                    m.setRebateRatio(i);
                    m.setNum(Integer.valueOf(0));
                    if (this.userQuotaDao.findRecord(account, i) <= 0) {
                        this.userQuotaDao.save(m);
                    }
                }
            } else if (rebateRatio.compareTo(oldUserRatio) < 0) {
                BigDecimal deleteRatio = rebateRatio.subtract(userMinRatio);
                this.userQuotaDao.deleteMaxRebateRatio(account, deleteRatio);
            }
            setLogout(account);
        }
        return Jsoner.success();
    }

    public int setMark(String account, Integer userMark) {
        return this.userDao.setMark(account, userMark);
    }

    public Integer findRecordByAccount(String account) {
        return Integer.valueOf(this.userDao.findRecordByAccount(account));
    }

    public void updateOnlineTime(String account, Date date) {
        String ds = RedisUtils.hget("ON_LINE_TIME", account);
        Date d = DateUtils.toDateNull(ds);
        if (d != null) {
            d = DateUtils.addMinute(d, 2);
        }
        if ((d == null) || (d.compareTo(date) <= 0)) {
            this.userDao.updateOnlineTime(account, date);

            RedisUtils.hset("ON_LINE_TIME", account, DateUtils.format(date));
        }
    }

    public int updateAccountType(String account) {
        User user = this.userDao.findByAccount(account);

        BonusGroup bonusGroup = this.bonusGroupDao.findById(user.getBonusGroupId().intValue());

        BigDecimal noneRatio = bonusGroup.getNoneMinRatio();

        BigDecimal stepRatio = bonusGroup.getUserMinRatio();
        if (bonusGroup.getUserMinRatio().compareTo(new BigDecimal("0")) == 0) {
            stepRatio = new BigDecimal("0.1");
        }
        for (BigDecimal i = user.getRebateRatio().subtract(bonusGroup.getUserMinRatio()); i.compareTo(noneRatio) > 0; i = i.subtract(stepRatio)) {
            UserQuota m = new UserQuota();
            m.setAccount(account);
            m.setRebateRatio(i);
            m.setNum(Integer.valueOf(0));
            this.userQuotaDao.save(m);
        }
        Integer userType = Integer.valueOf(1);
        this.userDao.updateUserType(account, userType);
        setLogout(account);
        return 1;
    }

    public List<User> listByCond(String account, Integer start, Integer limit) {
        return this.userDao.listByCond(account, start, limit);
    }

    public List<User> listBySelf(String account, String parentAccount, Integer start, Integer limit) {
        return this.userDao.listBySelf(account, parentAccount, start, limit);
    }

    public List<User> listByParent(String account, Page p) {
        return this.userDao.listByParent(account, p);
    }

    public int countChild(String parentAccount) {
        return this.userDao.countChild(parentAccount);
    }

    public int deleteUserData(String account) {
        return this.userDao.deleteUserData(account);
    }

    public int updateFreeze(String account) {
        return this.userDao.updateFreeze(account);
    }

    public List<User> listClearUser(Date beforeLoginTime, BigDecimal minAmount, BigDecimal maxAmount, int start, int size) {
        return this.userDao.listClearUser(beforeLoginTime, minAmount, maxAmount, start, size);
    }

    public int countClearUser(Date beforeLoginTime, BigDecimal minAmount, BigDecimal maxAmount) {
        return this.userDao.countClearUser(beforeLoginTime, minAmount, maxAmount);
    }

    public List<UserModel> getUserList(String flagAccount, String flagNextAccount, String account, Page p, Integer userType, BigDecimal beginAmount, BigDecimal endAmount, Integer isOnLine) {
        List<UserModel> listShow = new ArrayList();
        List<User> listHide = new ArrayList();


        List<User> userList = this.userDao.getUserListByAccount(account);
        label397:
        for (User a : userList) {
            UserModel result = new UserModel();
            result.setAccount(a.getAccount());
            result.setRegTime(a.getRegTime());
            result.setIsOnLine(a.getIsOnLine());
            result.setRebateRatio(a.getRebateRatio());
            result.setAmount(a.getAmount());
            result.setLoginTime(a.getLoginTime());
            result.setUserType(a.getUserType());
            result.setParentAccount(a.getParentAccount());
            result.setNiceName(a.getNiceName());
            result.setTeamAmount(a.getAmount());
            result.setTeamCount(Integer.valueOf(1));
            result.setParentList(a.getParentList());
            result.setHomeRemark(a.getHomeRemark());
            result.setContractStatus(a.getContractStatus());
            result.setDailyWagesStatus(a.getDailyWagesStatus());
            DailyAcc dailyAcc = this.dailyAccDao.findByAccount(a.getAccount());
            result.setDailyRate(dailyAcc == null ? null : dailyAcc.getRate());
            result.setDailyRuleId(a.getDailyRuleId());
            boolean isShow = false;
            if (!StrUtils.hasEmpty(new Object[]{flagAccount})) {
                if (StrUtils.hasEmpty(new Object[]{flagNextAccount})) {
                    if (a.getAccount().equals(account)) {
                        isShow = getListShow(userType, beginAmount, endAmount, listShow, a, result, isShow, isOnLine);
                        break label397;
                    }
                    listHide.add(a);
                    break label397;
                }
            }
            if ((a.getParentAccount().equals(account)) && (!a.getRootAccount().equals(a.getAccount()))) {
                isShow = getListShow(userType, beginAmount, endAmount, listShow, a, result, isShow, isOnLine);
            } else {
                listHide.add(a);
            }
            if (isShow) {
                for (User u : listHide) {
                    String k = u.getParentList();
                    if (k.startsWith(a.getParentList())) {
                        result.setTeamCount(Integer.valueOf(result.getTeamCount().intValue() + 1));
                        result.setTeamAmount(u.getAmount().add(result.getTeamAmount()));
                    }
                }
            } else {
                for (UserModel u : listShow) {
                    String k = u.getParentList();
                    if (a.getParentList().startsWith(k)) {
                        u.setTeamCount(Integer.valueOf(u.getTeamCount().intValue() + 1));
                        u.setTeamAmount(a.getAmount().add(u.getTeamAmount()));
                        break;
                    }
                }
            }
        }
        if (listShow.size() > 0) {
            PagerUtils<UserModel> pager = PagerUtils.create(listShow, p.getPageSize());
            p.setRowCount(listShow.size());
            listShow = pager.getPagedList(p.getNowPage());
        }
        return listShow;
    }

    private boolean getListShow(Integer userType, BigDecimal beginAmount, BigDecimal endAmount, List<UserModel> listShow, User a, UserModel result, boolean isShow, Integer isOnLine) {
        if (isOnLine.intValue() == 0) {
            isShow = getUserByCondition(userType, beginAmount, endAmount, listShow, a, result, isShow);
        } else if (isOnLine == a.getIsOnLine()) {
            isShow = getUserByCondition(userType, beginAmount, endAmount, listShow, a, result, isShow);
        }
        return isShow;
    }

    private boolean getUserByCondition(Integer userType, BigDecimal beginAmount, BigDecimal endAmount, List<UserModel> listShow, User a, UserModel result, boolean isShow) {

        /**jd-GUI
         if (userType.intValue() == 2)
         {
         if (StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if (!StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         if (beginAmount.compareTo(a.getAmount()) != -1) {
         break label414;
         }
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if (StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (!StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         if (endAmount.compareTo(a.getAmount()) != 1) {
         break label414;
         }
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if ((beginAmount.compareTo(a.getAmount()) == -1) && (endAmount.compareTo(a.getAmount()) == 1))
         {
         listShow.add(result);
         isShow = true;
         }
         }
         else if (userType == a.getUserType())
         {
         if (StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if (!StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         if (beginAmount.compareTo(a.getAmount()) != -1) {
         break label414;
         }
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if (StrUtils.hasEmpty(new Object[] { beginAmount })) {
         if (!StrUtils.hasEmpty(new Object[] { endAmount }))
         {
         if (endAmount.compareTo(a.getAmount()) != 1) {
         break label414;
         }
         listShow.add(result);
         isShow = true;
         break label414;
         }
         }
         if ((beginAmount.compareTo(a.getAmount()) == -1) && (endAmount.compareTo(a.getAmount()) == 1))
         {
         listShow.add(result);
         isShow = true;
         }
         }
         label414:
         return isShow;
         */

        if (userType.intValue() == 2) {
            if (StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                listShow.add(result);
                isShow = true;
            } else if (!StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                if (beginAmount.compareTo(a.getAmount()) == -1) {
                    listShow.add(result);
                    isShow = true;
                }
            } else if (StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && !StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                if (endAmount.compareTo(a.getAmount()) == 1) {
                    listShow.add(result);
                    isShow = true;
                }
            } else if (beginAmount.compareTo(a.getAmount()) == -1 && endAmount.compareTo(a.getAmount()) == 1) {
                listShow.add(result);
                isShow = true;
            }
        } else if (userType == a.getUserType())
            if (StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                listShow.add(result);
                isShow = true;
            } else if (!StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                if (beginAmount.compareTo(a.getAmount()) == -1) {
                    listShow.add(result);
                    isShow = true;
                }
            } else if (StrUtils.hasEmpty(new Object[]{
                    beginAmount
            }) && !StrUtils.hasEmpty(new Object[]{
                    endAmount
            })) {
                if (endAmount.compareTo(a.getAmount()) == 1) {
                    listShow.add(result);
                    isShow = true;
                }
            } else if (beginAmount.compareTo(a.getAmount()) == -1 && endAmount.compareTo(a.getAmount()) == 1) {
                listShow.add(result);
                isShow = true;
            }
        return isShow;
    }

    public int updateHremark(String account, String homeRemark) {
        return this.userDao.updateHremark(account, homeRemark);
    }

    public int updateAremark(String account, String adminRemark) {
        return this.userDao.updateAremark(account, adminRemark);
    }

    public int updateDailyWagesStatus(String account, Integer dailyWagesStatus) {
        if ((dailyWagesStatus == null) || ((dailyWagesStatus.intValue() != 1) && (dailyWagesStatus.intValue() != 0))) {
            throw new BaseCheckException("日工资状态不正确！");
        }
        User user = this.userDao.findByAccount(account);
        if ((dailyWagesStatus.intValue() == 0) && (!user.getAccount().equals(user.getParentAccount()))) {
            throw new BaseCheckException("[" + user.getAccount() + "]非总代用户不能开通日工资！");
        }
        int count = 0;
        if (dailyWagesStatus.intValue() == 0) {
            count = this.userDao.updateDailyWagesStatusOpen(account, account);
        } else if (dailyWagesStatus.intValue() == 1) {
            count = this.userDao.updateDailyWagesStatusClose(user.getParentList());


            this.dailyAccDao.deleteByParentList(user.getParentList());
            this.userDao.deleteDailyRuleByParentList(user.getParentList());
        }
        return count;
    }

    public int updateDailyWagesStatus(String account, String parentAccount, Integer dailyWagesStatus) {
        if ((dailyWagesStatus == null) || ((dailyWagesStatus.intValue() != 1) && (dailyWagesStatus.intValue() != 0))) {
            throw new BaseCheckException("日工资状态不正确！");
        }
        if (account.equals(parentAccount)) {
            throw new BaseCheckException("[" + parentAccount + "]不能给[" + account + "]开通工资！");
        }
        User parentUser = this.userDao.findByAccount(parentAccount);
        if (parentUser.getDailyWagesStatus().intValue() != 0) {
            throw new BaseCheckException("[" + parentAccount + "]没有开通日工资，不能给下级开通工资！");
        }
        int count = 0;
        if (dailyWagesStatus.intValue() == 0) {
            count = this.userDao.updateDailyWagesStatusOpen(account, parentAccount);
            if (parentUser.getDailyRuleId() != null) {
                this.userDao.updateDailyRule(account, parentUser.getDailyRuleId());

                DailyRule dailyRule = (DailyRule) this.dailyRuleDao.find(parentUser.getDailyRuleId());
                User user = this.userDao.findByAccount(account);
                if (user.getParentList().split(",").length == dailyRule.getLevel().intValue()) {
                    this.dailyAccDao.save(dailyRule.getId(), dailyRule.getRate(), dailyRule.getBetAmount(), account, parentAccount, user.getRootAccount(), user.getParentList());
                }
            }
        }
        return count;
    }

    public List<User> listDailyWagesRoot() {
        return this.userDao.listDailyWagesRoot();
    }

    public List<User> listDailyWagesLower(String account) {
        return this.userDao.listDailyWagesLower(account);
    }

    public Jsoner updatePrivateRebateByAdmin(String account, Integer privateRebate) {
        User u = this.userDao.findByAccount(account);
        if (u == null) {
            return Jsoner.error("用户不存在");
        }
        this.userDao.updatePrivateRebate(account, privateRebate);
        return Jsoner.success();
    }

    public int updatePrivateRebateByApproval(Approval m) {
        this.approvalDao.update(m);
        Integer privateRatio = Integer.valueOf(Integer.parseInt(m.getPassword()));
        return this.userDao.updatePrivateRebate(m.getUserName(), privateRatio);
    }
}
