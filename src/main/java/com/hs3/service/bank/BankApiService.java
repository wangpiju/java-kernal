package com.hs3.service.bank;

import com.hs3.dao.bank.BankAccDao;
import com.hs3.dao.bank.BankApiDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankAcc;
import com.hs3.entity.bank.BankApi;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankApiService")
public class BankApiService {
    @Autowired
    private BankApiDao bankApiDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BankAccDao bankAccDao;

    public List<BankApi> listByCond(BankApi m, String[] classKeyArray, Page page) {
        return this.bankApiDao.listByCond(m, classKeyArray, page);
    }

    public void save(BankApi bankApi) {
        String[] specialAccountArray = checkExist(bankApi.getSpecialAccount());
        String[] proxyLineArray = checkExist(bankApi.getProxyLine());
        int bankApiId = this.bankApiDao.save(bankApi).intValue();

        updateBankAcc(Integer.valueOf(bankApiId), specialAccountArray, proxyLineArray);
    }

    public int update(Integer bankApiId, String specialAccount, String proxyLine, int status) {
        BankApi bankApi = (BankApi) this.bankApiDao.findLock(bankApiId);

        bankApi.setSpecialAccount(specialAccount);
        bankApi.setProxyLine(proxyLine);
        bankApi.setStatus(Integer.valueOf(status));

        return update(bankApi);
    }

    public int update(BankApi bankApi) {
        String[] specialAccountArray = checkExist(bankApi.getSpecialAccount());
        String[] proxyLineArray = checkExist(bankApi.getProxyLine());
        int i = this.bankApiDao.update(bankApi);

        updateBankAcc(bankApi.getId(), specialAccountArray, proxyLineArray);

        return i;
    }

    private String[] checkExist(String specialAccount) {
        if (!StrUtils.hasEmpty(new Object[]{specialAccount})) {
            String[] accounts = specialAccount.split("\\,");
            for (String account : accounts) {
                if (this.userDao.findRecordByAccount(account) == 0) {
                    throw new BaseCheckException("[" + account + "]不存在！");
                }
            }
            return accounts;
        }
        return null;
    }

    private void updateBankAcc(Integer bankApiId, String[] specialAccountArray, String[] proxyLineArray) {
        this.bankAccDao.deleteByBankApiId(bankApiId);
        if (specialAccountArray != null) {
            for (String account : specialAccountArray) {
                BankAcc m = new BankAcc();

                m.setBankApiId(bankApiId);
                m.setType(Integer.valueOf(0));
                m.setAccount(account);

                this.bankAccDao.save(m);
            }
        }
        if (proxyLineArray != null) {
            for (String account : proxyLineArray) {
                BankAcc m = new BankAcc();

                m.setBankApiId(bankApiId);
                m.setType(Integer.valueOf(1));
                m.setAccount(account);

                this.bankAccDao.save(m);
            }
        }
    }

    public int delete(List<Integer> ids) {
        return this.bankApiDao.delete(ids);
    }

    public BankApi find(Integer id) {
        return (BankApi) this.bankApiDao.find(id);
    }

    public List<BankApi> list(Page p) {
        return this.bankApiDao.list(p);
    }

    public List<BankApi> findByClassKey(String classKey) {
        return this.bankApiDao.findByClassKey(classKey);
    }

    public List<BankApi> listByAmount(BigDecimal rechargeAmount, Integer rechargeNum) {
        return this.bankApiDao.listByAmount(rechargeAmount, rechargeNum);
    }
}
