package com.hs3.service.sys;

import com.alibaba.fastjson.JSON;
import com.hs3.dao.sys.Log4jDao;
import com.hs3.dao.sys.SysClearDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysClear;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.service.third.DhThirdService;
import com.hs3.tasks.sys.SysClearJobFactory;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysClearService")
public class SysClearService {
    @Autowired
    private SysClearDao sysClearDao;
    @Autowired
    private DhThirdService dhThirdService;
    @Autowired
    private Log4jDao log4jDao;

    public Integer deleteForClear(SysClear m) {
        return this.sysClearDao.deleteForClear(m);
    }

    public int deleteForClear(String table, String column, String order, Object obj, int limit, boolean isBankUp) {
        return this.sysClearDao.deleteForClear(table, column, order, obj, limit, isBankUp);
    }

    public void save(SysClear m) {
        if (m.getCategory() == null) {
            throw new BaseCheckException("系统名称不能为空！");
        }
        if (m.getJob() == null) {
            throw new BaseCheckException("任务不能为空！");
        }
        if (m.getClearMode() == null) {
            throw new BaseCheckException("清理方式不能为空！");
        }
        if ((m.getBeforeDays() == null) && (m.getBeforeDaysDefault() == null)) {
            throw new BaseCheckException("清理天数不能为空！");
        }
        m.setTitle(SysClearJobFactory.getDesc(m.getJob()));
        if (m.getStatus() == 0) {
            addJob(m, false);
        }
        this.sysClearDao.save(m);
    }

    public List<SysClear> list(Page p) {
        return this.sysClearDao.list(p);
    }

    public List<SysClear> listWithOrder(Page p) {
        return this.sysClearDao.listWithOrder(p);
    }

    public SysClear find(Integer id) {
        return this.sysClearDao.find(id);
    }

    public int update(SysClear m) {
        deleteJob(m.getId());

        m.setTitle(SysClearJobFactory.getDesc(m.getJob()));
        if (m.getStatus() == 0) {
            addJob(m, true);
        }
        return this.sysClearDao.update(m);
    }

    public int delete(List<Integer> ids) {
        for (Integer id : ids) {
            deleteJob(id);
        }
        return this.sysClearDao.delete(ids);
    }

    private void deleteJob(Integer id) {
        SysClear sysClear = this.sysClearDao.find(id);
        if (sysClear != null) {
            deleteJob(SysClearJobFactory.getDesc(sysClear.getJob()));
        }
    }

    private void deleteJob(String jobName) {
        dhThirdService.quartzDeleteJob(jobName, "系统数据清理任务");
    }

    private void addJob(SysClear m, boolean deleteWhenExists) {
        Class<? extends Job> jobClass = SysClearJobFactory.getJobClass(m.getJob());
        if (jobClass == null) {
            throw new BaseCheckException("没有匹配的任务！");
        }
        String cronExpression = getCron(m.getExecuteTime());

        String jobName = m.getTitle();

        Map<String, Object> map = new HashMap<>();
        map.put("sys_clear", m);
        if (deleteWhenExists) {
            deleteJob(jobName);
        }
        dhThirdService.quartzAddNewJobCron(jobName, "系统数据清理任务", cronExpression, jobClass.getName(), JSON.toJSONString(map));
    }

    private String getCron(String executeTime) {
        try {
            String[] time = executeTime.split(":");
            String second = time[2];
            String minute = time[1];
            String hours = time[0];
//            if ((second < 0) || (second > 59) || (minute < 0) || (minute > 59) || (hours < 0) || (hours > 23)) {
//                throw new BaseCheckException("执行时间不满足表达式！");
//            }
            if (StringUtils.isBlank(second) || StringUtils.isBlank(minute) || StringUtils.isBlank(hours)) {
                throw new BaseCheckException("执行时间不满足表达式！");
            }
            return second + " " + minute + " " + hours + " * * ?";
        } catch (BaseCheckException e1) {
            throw e1;
        } catch (Exception e) {
            throw new BaseCheckException("执行时间错误！");
        }
    }
}
