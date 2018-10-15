package com.hs3.service.sys;

import com.hs3.dao.sys.LoginIpWhiteDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.LoginIpWhite;
import com.hs3.entity.sys.SysConfig;
import com.hs3.utils.ListUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginIpWhiteService {
    private static String LOCALHOST = "127.0.0.1";
    private static final String IP_WHITE_STATUS = "IP_WHITE_STATUS";
    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private LoginIpWhiteDao loginIpWhiteDao;

    public List<LoginIpWhite> list(Page p) {
        return this.loginIpWhiteDao.list(p);
    }

    public List<LoginIpWhite> listByCond(LoginIpWhite white, Page page) {
        return this.loginIpWhiteDao.listByCond(white, page);
    }

    public void save(String ip) {
        this.loginIpWhiteDao.save(ip);
    }

    public int delete(List<String> ips) {
        int len = this.loginIpWhiteDao.deleteByIps(ips);
        return len;
    }

    public boolean vaild(String ip) {
        if (ip.equals(LOCALHOST)) {
            return true;
        }
        SysConfig config = (SysConfig) this.sysConfigDao.find("IP_WHITE_STATUS");
        if ((config == null) || ("0".equals(config.getVal()))) {
            return true;
        }
        List<String> loginIp = ListUtils.toList(ip, "\\.");

        List<String> list = this.loginIpWhiteDao.listString(null);
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
