package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs.comm.service.lotts.BonusRiskService;
import com.hs3.web.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BonusRiskClearJob extends SysClearJob {

    private BonusRiskService bonusRiskService = SpringContext.getBean(BonusRiskService.class);
    private static final Logger log = LoggerFactory.getLogger(BonusRiskClearJob.class);

    @Override
    protected void doClear(SysClear sysClear) {
        try {
            bonusRiskService.clearBonusPool();
            bonusRiskService.initBonusPool();
        } catch (Exception e) {
            log.info("--> bonus risk clear error, ", e);
        }
    }
}
