package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs3.service.lotts.TraceService;
import com.hs3.utils.DateUtils;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.SpringContext;

import java.util.Date;

import org.springframework.core.NamedThreadLocal;

public final class TraceSysClearJob
        extends SysClearJob {
    private static final ThreadLocal<Integer> countDelete = new NamedThreadLocal<>("ThreadLocal DeleteTrace");

    protected void doClear(SysClear sysClear) {
        countDelete.set(0);
        deleteByClear(sysClear);
        ThreadLog.log(sysClear.getTitle() + "," + sysClear.getClearMode() + "," + sysClear.getBeforeDays() + "," + sysClear.getBeforeDaysDefault() + " delete:" + countDelete.get());
        countDelete.remove();
    }

    public void deleteByClear(SysClear m) {
        TraceService traceService = (TraceService) SpringContext.getBean(TraceService.class);

        Date createTime = DateUtils.addDay(new Date(), -m.getBeforeDays());
        int size = 100;
        int start = 0;

        long endTime = new Date().getTime() + 7200000L;

        int count = traceService.countClearTrace(createTime);
        while (count > 0) {
            if (count > size) {
                start = count - size;
            } else {
                start = 0;
                size = count;
            }
            int i = traceService.deleteTraceData(traceService.listClearTrace(createTime, start, size), m.getClearMode() == 2);
            countDelete.set(countDelete.get() + i);

            count -= size;
            if (new Date().getTime() > endTime) {
                break;
            }
        }
    }
}
