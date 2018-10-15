package com.hs3.service.user;

import com.hs3.dao.user.UserNoticeDao;
import com.hs3.entity.users.UserNotice;
import com.hs3.utils.DateUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNoticeService {
    @Autowired
    private UserNoticeDao userNoticeDao;

    public List<UserNotice> deleteAndList(String account, int size) {
        Date begin = DateUtils.addMinute(new Date(), -30);
        List<UserNotice> list = this.userNoticeDao.listByAccount(account, begin, size);
        if (!list.isEmpty()) {
            Date endTime = ((UserNotice) list.get(list.size() - 1)).getCreateTime();
            this.userNoticeDao.deleteOld(account, endTime);
        }
        return list;
    }
}
