package com.hs3.service.approval;

import com.hs3.dao.approval.ApprovalDao;
import com.hs3.dao.finance.FinanceChangeDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.approval.Approval;
import com.hs3.utils.StrUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("approvalService")
public class ApprovalService {
    @Autowired
    private ApprovalDao approvalDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FinanceChangeDao financeChangeDao;

    public List<Approval> list(Page p, Approval approval, String startTime, String endTime, Integer status) {
        return this.approvalDao.list(p, approval, startTime, endTime, status);
    }

    public int update(Approval m) {
        return this.approvalDao.update(m);
    }

    public int updateRemark(Approval m) {
        if (!StrUtils.hasEmpty(new Object[]{m.getRechargeId()})) {
            this.financeChangeDao.updateByFinanceId(m.getRechargeId(), m.getRemark());
        }
        return this.approvalDao.update(m);
    }

    public Approval getEntity(Integer id) {
        return (Approval) this.approvalDao.find(id);
    }

    public int saveAndUpdate(Approval m) {
        if (m.getApplyType().intValue() == 0) {
            this.userDao.updateNewPassword(m.getUserName(), m.getPassword());
        } else {
            this.userDao.updateNewSafePassword(m.getUserName(), m.getPassword());
        }
        return this.approvalDao.update(m);
    }

    public int save(Approval entity) {
        return this.approvalDao.save(entity);
    }
}
