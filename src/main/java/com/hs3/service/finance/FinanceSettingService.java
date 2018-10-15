package com.hs3.service.finance;

import com.hs3.dao.finance.FinanceSettingDao;
import com.hs3.dao.user.ManagerDao;
import com.hs3.entity.finance.FinanceSetting;
import com.hs3.exceptions.UnLogException;
import com.hs3.utils.StrUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("financeSettingService")
public class FinanceSettingService {
    @Autowired
    private FinanceSettingDao financeSettingDao;
    @Autowired
    private ManagerDao managerDao;

    public List<FinanceSetting> list() {
        return this.financeSettingDao.list(null);
    }

    public FinanceSetting find(Integer id) {
        return (FinanceSetting) this.financeSettingDao.find(id);
    }

    public int update(FinanceSetting m) {
        if ((!StrUtils.hasEmpty(new Object[]{m.getDepositAutoOperator()})) &&
                (this.managerDao.findByAccount(m.getDepositAutoOperator()) == null)) {
            throw new UnLogException("[" + m.getDepositAutoOperator() + "]不存在！");
        }
        return this.financeSettingDao.update(m);
    }
}
