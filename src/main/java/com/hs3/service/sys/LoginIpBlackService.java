package com.hs3.service.sys;

import com.hs3.dao.sys.LoginIpBlackDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.LoginIpBlack;
import com.hs3.entity.sys.SysConfig;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginIpBlackService {
    private static final String LOCALHOST = "127.0.0.1";
    private static final String IP_BLACK_STATUS = "IP_BLACK_STATUS";
    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private LoginIpBlackDao loginIpBlackDao;

    public LoginIpBlack find(String ip) {
        return this.loginIpBlackDao.find(ip);
    }

    public int update(LoginIpBlack black) {
        return this.loginIpBlackDao.update(black);
    }

    public List<LoginIpBlack> list(Page p) {
        return this.loginIpBlackDao.list(p);
    }

    public List<LoginIpBlack> listByCond(LoginIpBlack black, Page page) {
        return this.loginIpBlackDao.listByCond(black, page);
    }

    public void save(LoginIpBlack black) {
        this.loginIpBlackDao.save(black);
    }

    public int delete(List<String> ips) {
        int len = this.loginIpBlackDao.deleteByIps(ips);
        return len;
    }

    public boolean vaild(String ip) {
        if (ip.equals("127.0.0.1")) {
            return true;
        }
        SysConfig config = (SysConfig) this.sysConfigDao.find("IP_BLACK_STATUS");
        if ((config == null) || ("0".equals(config.getVal()))) {
            return true;
        }
        return !checkInIpList(ip);
    }

    private boolean checkInIpList(String ip) {
        List<String> loginIp = ListUtils.toList(ip, "\\.");

        List<String> list = this.loginIpBlackDao.listString(null);
        boolean canLogin = true;
        for (String id : list) {
            List<String> listIp = ListUtils.toList(id, "\\.");
            for (int i = 0; i < listIp.size(); i++) {
                String ip1 = (String) loginIp.get(i);
                String ip2 = (String) listIp.get(i);
                if (ip2.equals("*")) {
                    break;
                }
                if (!ip1.equals(ip2)) {
                    canLogin = false;
                    break;
                }
                canLogin = true;
            }
            if (canLogin) {
                return true;
            }
        }
        return false;
    }
}
