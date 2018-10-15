package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.entity.sys.SysClear;
import com.hs3.tasks.sys.SysClearJobEnum;
import com.hs3.tasks.sys.SysClearJobFactory;
import org.springframework.stereotype.Repository;

@Repository("sysClearDao")
public class SysClearDao
        extends BaseDao<SysClear> {
    public void save(SysClear m) {
        saveAuto(m);
    }

    public Integer deleteForClear(SysClear m) {
        SysClearJobEnum sysClearJobEnum = SysClearJobFactory.getSysClearJobEnum(m.getJob());
        Object[] cond = {sysClearJobEnum.getObj(m)};
        if (m.getClearMode().intValue() == 2) {
            this.dbSession.update("INSERT INTO backup_" + sysClearJobEnum.getTable() + " SELECT * FROM " + sysClearJobEnum.getTable() + " WHERE " + sysClearJobEnum.getColumn() + " <= ?", cond);
        }
        return Integer.valueOf(this.dbSession.update("DELETE FROM " + sysClearJobEnum.getTable() + " WHERE " + sysClearJobEnum.getColumn() + " <= ?", cond));
    }

    public int deleteForClear(String table, String column, String order, Object obj, int limit, boolean isBankUp) {
        Object[] cond = {obj, Integer.valueOf(limit)};
        if (isBankUp) {
            this.dbSession.update("INSERT INTO backup_" + table + " SELECT * FROM " + table + " WHERE " + column + " <= ? ORDER BY " + order + " LIMIT ?", cond);
        }
        return this.dbSession.update("DELETE FROM " + table + " WHERE " + column + " <= ? ORDER BY " + order + " LIMIT ?", cond);
    }

    public int update(SysClear m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"category", "title", "job", "beforeDays", "beforeDaysDefault", "executeTime", "status", "clearMode", "deleteMinAmount", "deleteMaxAmount", "freezeMixAmount",
                "freezeMaxAmount", "extends1", "extends2"};
    }

    protected Object[] getValues(SysClear m) {
        return new Object[]{m.getCategory(), m.getTitle(), m.getJob(), m.getBeforeDays(), m.getBeforeDaysDefault(), m.getExecuteTime(), m.getStatus(), m.getClearMode(), m.getDeleteMinAmount(),
                m.getDeleteMaxAmount(), m.getFreezeMixAmount(), m.getFreezeMaxAmount(), m.getExtends1(), m.getExtends2()};
    }
}
