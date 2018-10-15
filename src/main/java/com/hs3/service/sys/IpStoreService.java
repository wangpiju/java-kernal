package com.hs3.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.hs3.dao.sys.IpStoreDao;
import com.hs3.entity.sys.IpStore;
import com.hs3.service.user.UserLoginIpService;
import com.hs3.utils.AssembleHttpUtils;
import com.hs3.utils.ResponseData;
import com.hs3.utils.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class IpStoreService {

    private AssembleHttpUtils assembleHttpUtils = AssembleHttpUtils.instance();

    private ExecutorService es = ThreadFactory.ES;

    private static final Logger log = LoggerFactory.getLogger(IpStoreService.class);

    private String remoteUrl="http://ip.taobao.com";
    @Autowired
    private UserLoginIpService userLoginIpService;

    @Autowired
    private IpStoreDao ipStoreDao;

    public boolean save(IpStore ipStore) {
        return ipStoreDao.saveAuto(ipStore) > 0;
    }

    public IpStore findByIp(String ip) {
        return ipStoreDao.findByIp(ip);
    }

    public void ipTrans(String ip) {
        if (ip.equals("127.0.0.1") || ip.startsWith("0.0.0") || ip.startsWith("0:0:0") || ip.startsWith("localhost")) {
            IpStore ipStore = new IpStore();
            ipStore.setIp(ip);
            ipStore.setCountry("菲律宾");
            ipStore.setRegion("");
            ipStore.setCity("");
            ipStore.setIsp("");
            ipStore.setCountryId("");
            ipStore.setRegionId("");
            ipStore.setCityId("");
            save(ipStore);
            userLoginIpService.updateIpInfoByIp(transIpAddress(ipStore), ip, "菲律宾");
        } else {
            ResponseData responseData = assembleHttpUtils.execRequest(remoteUrl, AssembleHttpUtils.SendReq.get_taobao_ip, new Object[]{ip}, AssembleHttpUtils.SendReq.defaultHeader);
            if (responseData.isFlag()) {
                IpStore ipStore = findByIp(ip);
                if (ipStore == null) {
                    JSONObject jb = JSONObject.parseObject(responseData.getResult());
                    if (jb.getInteger("code") == 0) {
                        JSONObject ipData = JSONObject.parseObject(jb.getString("data"));
                        ipStore = new IpStore();
                        ipStore.setIp(ip);
                        ipStore.setCountry(ipData.getString("country").equals("XX") ? "" : ipData.getString("country"));
                        ipStore.setRegion(ipData.getString("region").equals("XX") ? "" : ipData.getString("region"));
                        ipStore.setCity(ipData.getString("city").equals("XX") ? "" : ipData.getString("city"));
                        ipStore.setIsp(ipData.getString("isp").equals("XX") ? "" : ipData.getString("isp"));
                        ipStore.setCountryId(ipData.getString("country_id").equals("XX") ? "" : ipData.getString("country_id"));
                        ipStore.setRegionId(ipData.getString("region_id").equals("XX") ? "" : ipData.getString("region_id"));
                        ipStore.setCityId(ipData.getString("city_id").equals("XX") ? "" : ipData.getString("city_id"));
                        save(ipStore);
                        userLoginIpService.updateIpInfoByIp(transIpAddress(ipStore), ip, "菲律宾");
                    }
                }
            }
        }
    }

    public void ipTransThread(String ip) {
        es.execute(() -> {
            try {
                ipTrans(ip);
            } catch (Exception e) {}
        });
    }

    public String transIpAddress(IpStore ipStore) {
        String address = "菲律宾";
        if (ipStore != null) {
            if (ipStore.getCountry().equals("中国")) {
                address = ipStore.getRegion() + "省" + ipStore.getCity() + "市 " + ipStore.getIsp();
            } else {
                address = ipStore.getCountry()+" "+ipStore.getRegion() + " " + ipStore.getCity() + " " + ipStore.getIsp();
            }
        }
        return address;
    }
}
