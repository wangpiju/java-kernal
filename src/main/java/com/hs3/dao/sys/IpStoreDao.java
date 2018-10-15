package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.entity.sys.IpStore;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-12 15:43
 **/
@Repository
public class IpStoreDao extends BaseDao<IpStore> {

    public IpStore findByIp(String ip) {
        String sql = String.format("select ip, country, region, city, isp from %s where ip = ?", this.tableName);
        return dbSession.getObject(sql, new Object[]{ip}, IpStore.class);
    }

    protected String[] getColumns() {
        return new String[]{"ip", "country", "region", "city", "isp", "countryId", "regionId", "cityId"};
    }

    protected Object[] getValues(IpStore m) {
        return new Object[]{m.getIp(), m.getCountry(), m.getRegion(), m.getCity(), m.getIsp(), m.getCountryId(), m.getRegionId(), m.getCityId()};
    }
}
