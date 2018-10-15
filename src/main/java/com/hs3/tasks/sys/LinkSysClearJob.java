package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs3.service.user.ExtCodeService;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.SpringContext;

public final class LinkSysClearJob
        extends SysClearJob {
    protected void doClear(SysClear sysClear) {
        Integer count = SpringContext.getBean(ExtCodeService.class).deleteByClear(sysClear);
        ThreadLog.log(sysClear.getTitle() + "," + sysClear.getClearMode() + "," + sysClear.getBeforeDays() + "," + sysClear.getBeforeDaysDefault() + " delete records:" + count);
    }
}
