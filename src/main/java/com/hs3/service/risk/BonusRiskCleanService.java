package com.hs3.service.risk;

import com.hs3.dao.risk.BonusRiskCleanDao;
import com.hs3.entity.lotts.BonusRiskClean;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-25 15:55
 **/
@Service("bonusRiskCleanService")
public class BonusRiskCleanService {

    @Resource
    private BonusRiskCleanDao bonusRiskCleanDao;

    public void save(BonusRiskClean po) {
        bonusRiskCleanDao.save(po);
    }
}
