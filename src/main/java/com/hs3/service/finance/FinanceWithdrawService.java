package com.hs3.service.finance;

import com.hs3.dao.finance.FinanceWithdrawDao;
import com.hs3.dao.user.ManagerDao;
import com.hs3.db.Page;
import com.hs3.entity.finance.FinanceWithdraw;
import com.hs3.exceptions.UnLogException;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("financeWithdrawService")
public class FinanceWithdrawService {
    @Autowired
    private FinanceWithdrawDao financeWithdrawDao;
    @Autowired
    private ManagerDao managerDao;

    public FinanceWithdraw findByAmount(BigDecimal amount) {
        return findByAmount(amount, null);
    }

    public FinanceWithdraw findByAmount(BigDecimal amount, Integer status) {
        return this.financeWithdrawDao.findByAmount(amount, status);
    }

    public void save(FinanceWithdraw m) {
        check(m);

        this.financeWithdrawDao.save(m);
    }

    public List<FinanceWithdraw> list(Page p) {
        return this.financeWithdrawDao.list(p);
    }

    public List<FinanceWithdraw> listWithOrder(Page p) {
        return this.financeWithdrawDao.listWithOrder(p);
    }

    public FinanceWithdraw find(Integer id) {
        return (FinanceWithdraw) this.financeWithdrawDao.find(id);
    }

    public int update(FinanceWithdraw m) {
        check(m);

        FinanceWithdraw financeWithdraw = (FinanceWithdraw) this.financeWithdrawDao.find(m.getId());
        if (StrUtils.hasEmpty(new Object[]{m.getSign()})) {
            m.setSign(financeWithdraw.getSign());
        }
        if (StrUtils.hasEmpty(new Object[]{m.getPublicKey()})) {
            m.setPublicKey(financeWithdraw.getPublicKey());
        }
        return this.financeWithdrawDao.update(m);
    }

    private void check(FinanceWithdraw m) {
        if ((!StrUtils.hasEmpty(new Object[]{m.getAutoOperator()})) &&
                (this.managerDao.findByAccount(m.getAutoOperator()) == null)) {
            throw new UnLogException("[" + m.getAutoOperator() + "]不存在！");
        }
    }

    public int delete(List<Integer> ids) {
        return this.financeWithdrawDao.delete(ids);
    }
}
