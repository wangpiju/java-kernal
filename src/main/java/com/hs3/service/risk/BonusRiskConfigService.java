package com.hs3.service.risk;

import com.hs3.dao.risk.BonusRiskConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BonusRiskConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-06-20 17:12
 **/
@Service("bonusRiskConfigService")
public class BonusRiskConfigService {

    private static final Logger logger = LoggerFactory.getLogger(BonusRiskConfigService.class);

    @Autowired
    private BonusRiskConfigDao bonusRiskConfigDao;

    /**
     * 查询列表
     */
    public List<BonusRiskConfig> queryList(Page page) {
        List<BonusRiskConfig> bonusRisks = bonusRiskConfigDao.list(page);
        return bonusRisks;
    }

    public void save(BonusRiskConfig brc) {
        bonusRiskConfigDao.save(brc);
    }

    public BonusRiskConfig getByLotteryId(String lotteryId) {
        return bonusRiskConfigDao.findByLotteryId(lotteryId);
    }

    public BonusRiskConfig get(Integer id) {
        return bonusRiskConfigDao.find(id);
    }

    public void delete(Integer id) {
        bonusRiskConfigDao.delete(id);
    }

}
