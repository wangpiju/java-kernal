package com.hs3.service.user;

import com.hs3.dao.user.UserQuotaDao;
import com.hs3.entity.users.UserQuota;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userQuotaService")
public class UserQuotaService {
    @Autowired
    private UserQuotaDao userQuotaDao;

    public void save(UserQuota m) {
        this.userQuotaDao.save(m);
    }

    public List<UserQuota> listByAccount(String account) {
        return this.userQuotaDao.listByAccount(account);
    }

    public List<Map<String, Object>> loadQuota(String account) {
        return this.userQuotaDao.listQuotaByAccount(account);
    }
}
