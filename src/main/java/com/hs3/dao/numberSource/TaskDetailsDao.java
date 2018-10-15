package com.hs3.dao.numberSource;

import com.hs3.dao.QuartzDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.numberSource.Triggers;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("taskDetailsDao")
public class TaskDetailsDao
        extends QuartzDao<Triggers> {
    public List<Triggers> listByGroupIdAndLotteryId(Integer groupId, String lotteryId) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{this.tableName, "JOB_GROUP,JOB_NAME,TRIGGER_GROUP,TRIGGER_NAME,NEXT_FIRE_TIME,PREV_FIRE_TIME,PRIORITY,TRIGGER_STATE,START_TIME,END_TIME,TRIGGER_TYPE",
                ""});
        return this.dbSession.list(sql, new Object[]{groupId, lotteryId}, this.cls);
    }

    public List<Triggers> list(Page page, String group, String groupName) {
        String sql = "select a.JOB_GROUP,a.JOB_NAME,a.TRIGGER_GROUP,a.TRIGGER_NAME,NEXT_FIRE_TIME,PREV_FIRE_TIME,PRIORITY,TRIGGER_STATE,START_TIME,END_TIME,CRON_EXPRESSION,JOB_CLASS_NAME from qrtz_triggers a LEFT JOIN qrtz_cron_triggers  b  on a.TRIGGER_NAME=b.TRIGGER_NAME and a.TRIGGER_GROUP=b.TRIGGER_GROUP LEFT JOIN qrtz_job_details c on a.JOB_GROUP=C.JOB_GROUP AND a.JOB_NAME=c.JOB_NAME where a.JOB_GROUP like ? and a.JOB_NAME like ? order by a.JOB_GROUP,a.JOB_NAME,NEXT_FIRE_TIME";

        return this.dbSession.list(sql, new Object[]{"%" + group + "%", "%" + groupName + "%"}, Triggers.class, page);
    }
}
