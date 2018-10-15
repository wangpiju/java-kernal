package com.hs3.dao.approval;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.approval.Approval;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("approvalDao")
public class ApprovalDao
        extends BaseDao<Approval> {
    public int save(Approval m) {
        return saveAuto(m);
    }

    public int update(Approval m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"status", "userName", "userIdentify", "createTime", "handleTime", "applyContent", "amount", "password", "operator", "remark", "applyType", "rechargeId", "receiveCard"};
    }

    protected Object[] getValues(Approval m) {
        return new Object[]{m.getStatus(), m.getUserName(), m.getUserIdentify(), m.getCreateTime(), m.getHandleTime(), m.getApplyContent(), m.getAmount(), m.getPassword(), m.getOperator(),
                m.getRemark(), m.getApplyType(), m.getRechargeId(), m.getReceiveCard()};
    }

    public List<Approval> list(Page p, Approval approval, String startTime, String endTime, Integer status) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE 1=1"});
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{approval.getUserName()})) {
            sql = sql + " AND userName = ?";
            cond.add(approval.getUserName().trim());
        }
        if (!StrUtils.hasEmpty(new Object[]{approval.getApplyType()})) {
            sql = sql + " AND applyType = ?";
            cond.add(approval.getApplyType());
        }
        if (!StrUtils.hasEmpty(new Object[]{approval.getApplyContent()})) {
            sql = sql + " AND applyContent like ?";
            cond.add("%" + approval.getApplyContent() + "%");
        }
        if (!StrUtils.hasEmpty(new Object[]{status})) {
            sql = sql + " AND status = ?";
            cond.add(status);
        }
        if (!StrUtils.hasEmpty(new Object[]{startTime})) {
            sql = sql + " AND createTime >= ?";
            cond.add(startTime);
        }
        if (!StrUtils.hasEmpty(new Object[]{endTime})) {
            sql = sql + " AND createTime <= ?";
            cond.add(endTime);
        }
        sql = sql + " order by createTime desc";
        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, p);
    }
}
