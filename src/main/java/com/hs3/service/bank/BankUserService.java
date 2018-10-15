package com.hs3.service.bank;

import com.hs3.dao.bank.BankNameDao;
import com.hs3.dao.bank.BankUserDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.bank.BankUser;
import com.hs3.entity.users.User;
import com.hs3.utils.StrUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bankUserService")
public class BankUserService {
    @Autowired
    private BankUserDao bankUserDao;
    @Autowired
    private BankNameDao bankNameDao;
    @Autowired
    private UserDao userDao;

    public void save(BankUser m) {
        this.bankUserDao.save(m);
    }

    public int saveByUser(BankUser m, Integer id, String oldCard, String niceName, String account, String safeWord) {
        if (this.bankNameDao.find(m.getBankNameId()) == null) {
            return 3;
        }
        List<BankUser> list = this.bankUserDao.listBy_Account(m.getAccount(), Integer.valueOf(0));
        if (list.size() >= 5) {
            return 1;
        }
        BankUser bu = this.bankUserDao.findByCard(m.getCard());
        if (bu != null) {
            if ((bu.getAccount().equals(m.getAccount())) && (bu.getStatus().intValue() != 0)) {
                m.setStatus(Integer.valueOf(0));
                m.setCreateTime(new Date());
                m.setId(bu.getId());
                this.bankUserDao.update(m);
                return 0;
            }
            return 4;
        }
        if (list.size() == 0) {
            User user = this.userDao.findByAccount(m.getAccount());
            if (!StrUtils.MD5(safeWord).equals(user.getSafePassword())) {
                return 2;
            }
            this.bankUserDao.save(m);
            return 0;
        }
        if (this.bankUserDao.findRecordByCard(id, oldCard, niceName, account, safeWord) > 0) {
            if (!niceName.equals(m.getNiceName())) {
                return 5;
            }
            this.bankUserDao.save(m);
            return 0;
        }
        return 2;
    }

    public int delete(List<Integer> ids) {
        return this.bankUserDao.delete(ids);
    }

    public int deleteByUser(int id, String account) {
        return this.bankUserDao.deleteByUser(Integer.valueOf(id), account);
    }

    public BankUser find(Integer id) {
        return (BankUser) this.bankUserDao.find(id);
    }

    public int findRecordByOld(int id, String oldNiceName, String oldCard, String safeWord, String account) {
        return this.bankUserDao.findRecordByCard(Integer.valueOf(id), oldCard, oldNiceName, account, safeWord);
    }

    public List<BankUser> list(Page p) {
        return this.bankUserDao.list(p);
    }

    public void updateStatus(Integer id, Integer status) {
        this.bankUserDao.updateStatus(id, status);
    }

    public List<BankUser> listByAccount(String account) {
        List<BankUser> list = this.bankUserDao.listBy_Account(account, Integer.valueOf(0));
        for (Iterator<BankUser> iterator = list.iterator(); iterator.hasNext(); ) {
            BankUser bankUser = (BankUser) iterator.next();
            String name = bankUser.getNiceName();
            switch (name.length()) {
                case 2:
                    name = "*" + name.substring(name.length() - 1);
                    break;
                case 3:
                    name = "**" + name.substring(name.length() - 1);
                    break;
                case 4:
                    name = "***" + name.substring(name.length() - 1);
                    break;
                case 5:
                    name = "****" + name.substring(name.length() - 1);
                    break;
                default:
                    name = "**" + name.substring(name.length() - 1);
            }
            bankUser.setNiceName(name);
        }
        return list;
    }

    public List<BankUser> listByAccount(String account, Integer status) {
        return this.bankUserDao.listByAccount(account, status);
    }

    public List<BankUser> listByAccount(int userMark, String account, String niceName, String bankCard, Date begin, Date end, Page p) {
        return this.bankUserDao.listByAccount(userMark, account, niceName, bankCard, begin, end, p);
    }

    public List<Map<String, Object>> listCityByProId(int id) {
        return this.bankUserDao.listCityByProId(id);
    }

    public List<Map<String, Object>> listProvince() {
        return this.bankUserDao.listProvince();
    }

    public List<BankUser> findByAccount(String account) {
        return this.bankUserDao.findByAccount(account);
    }
}
