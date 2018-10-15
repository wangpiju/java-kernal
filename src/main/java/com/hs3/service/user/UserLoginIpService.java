package com.hs3.service.user;

import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.user.UserLoginIpDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.IpStore;
import com.hs3.entity.users.UserLoginIp;
import com.hs3.service.sys.IpStoreService;
import com.hs3.utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hs3.utils.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginIpService {
    @Autowired
    private UserLoginIpDao userLoginIpDao;
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private IpStoreService ipStoreService;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginIpService.class);

    private ScheduledExecutorService es = ThreadFactory.ES_INTERVAL;

    public List<UserLoginIp> listByIp(String ip, String account, String ipInfo, Page p) {
        return this.userLoginIpDao.listByIp(ip, account, ipInfo, p);
    }

    public int countByLogin(String account) {
        return this.userLoginIpDao.countByLogin(account);
    }

    public boolean getSafeIp(String account, String ip) {
        if (this.bankUserDao.listByAccount(account, Integer.valueOf(0)).size() > 0) {
            String lastIp = this.userLoginIpDao.getLastIp(account);
            if (lastIp != null) {
                String[] a = lastIp.split("\\.");
                String oldIp = a[0] + "." + a[1];


                String[] curIp = ip.split("\\.");
                String strIp = curIp[0] + "." + curIp[1];
                if (!strIp.equals(oldIp)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean countByFirstLogin(String account) {
        boolean isFirstLogin = false;
        if (this.userLoginIpDao.countByFirstLogin(account, DateUtils.addDay(new Date(), -3)) > 1) {
            isFirstLogin = false;
        } else {
            isFirstLogin = true;
        }
        return isFirstLogin;
    }

    public void updateLoginIpInfoThread(String account, String ip) {
        es.schedule(()->{
            try {
                IpStore ipStore = ipStoreService.findByIp(ip);
                if (ipStore != null) {
                    String ipInfo = ipStoreService.transIpAddress(ipStore);
                    userLoginIpDao.updateIpInfo(ipInfo, account, ip);
                } else {
                    logger.warn(String.format("--> get ipStore failed, account : %s, ip: %s", account, ip));
                }
            } catch (Exception e) {
                logger.error(String.format("--> get ipStore error, account : %s, ip: %s", account, ip), e);
            }
        },10, TimeUnit.SECONDS);

    }

    public int updateIpInfoByIp(String ipInfo,String ip, String oldIpInfo) {
        return userLoginIpDao.updateIpInfoByIp(ipInfo, ip, oldIpInfo);
    }
}
