package com.hs3.service.user;

import com.hs3.dao.user.UserTokenDao;
import com.hs3.entity.users.UserToken;
import com.hs3.utils.NumUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userTokenService")
public class UserTokenService {
    @Autowired
    private UserTokenDao userTokenDao;

    public void save(UserToken userToken) {
        this.userTokenDao.save(userToken);
    }

    public void createToken(String account) {
        String[] array = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] array1 = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<UserToken> list = new ArrayList();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array1.length; j++) {
                String value = String.valueOf(NumUtils.getRandom(0, 9)) + String.valueOf(NumUtils.getRandom(0, 9));
                UserToken userToken = new UserToken();
                userToken.setTokenKey(array[i] + array1[j]);
                userToken.setAccount(account);
                userToken.setTokenValue(value);
                list.add(userToken);
            }
        }
        int[] b = this.userTokenDao.executeBatch(list);
        System.out.println(b.length);
    }

    public int deleteByAccount(String account) {
        return this.userTokenDao.deleteByAccount(account);
    }

    public List<UserToken> getUserToken(String account) {
        return this.userTokenDao.getUserToken(account);
    }

    public String getTokenValue(String account, String[] tokenKey) {
        return this.userTokenDao.getTokenValue(account, tokenKey);
    }

    public List<UserToken> listByAccount(String account) {
        return this.userTokenDao.listByAccount(account);
    }
}
