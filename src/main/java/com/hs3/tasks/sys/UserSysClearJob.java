package com.hs3.tasks.sys;

import com.hs3.entity.sys.SysClear;
import com.hs3.entity.users.User;
import com.hs3.service.user.UserService;
import com.hs3.utils.DateUtils;
import com.hs3.web.auth.ThreadLog;
import com.hs3.web.utils.SpringContext;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

public final class UserSysClearJob
        extends SysClearJob {
    private static final Logger log = LoggerFactory.getLogger(UserSysClearJob.class);
    private static final ThreadLocal<Integer> countDelete = new NamedThreadLocal<>("ThreadLocal DeleteUser");
    private static final ThreadLocal<Integer> countFreeze = new NamedThreadLocal<>("ThreadLocal FreezeUser");

    protected void doClear(SysClear sysClear) {
        countDelete.set(0);
        countFreeze.set(0);
        deleteByClear(sysClear);
        ThreadLog.log(sysClear.getTitle() + "," + sysClear.getClearMode() + "," + sysClear.getBeforeDays() + "," + sysClear.getBeforeDaysDefault() + " delete:" + countDelete.get() + ",freeze:" +
                countFreeze.get());
        countDelete.remove();
        countFreeze.remove();
    }

    public void deleteByClear(SysClear m) {
        UserService userService = (UserService) SpringContext.getBean(UserService.class);

        Date beforeLoginTime = DateUtils.addDay(new Date(), -m.getBeforeDays());
        int size = 100;
        int start = 0;

        long endTime = new Date().getTime() + 7200000L;

        int count = userService.countClearUser(beforeLoginTime, m.getDeleteMinAmount(), m.getDeleteMaxAmount());
        while (count > 0) {
            if (count > size) {
                start = count - size;
            } else {
                start = 0;
                size = count;
            }
            delete(userService.listClearUser(beforeLoginTime, m.getDeleteMinAmount(), m.getDeleteMaxAmount(), start, size), userService);

            count -= size;
            if (new Date().getTime() > endTime) {
                break;
            }
        }
    }

    private void delete(List<User> userList, UserService userService) {
        for (User user : userList) {
            if (userService.countChild(user.getAccount()) <= 0) {
                countDelete.set(countDelete.get() + userService.deleteUserData(user.getAccount()));
                log.info("UserSysClearJob deleteUserData:" + user.getAccount() + "," + user.getLoginTime() + "," + user.getAmount() + "," + user.getLoginStatus() + "," + user.getFreezeAccount());
            } else if (user.getLoginStatus() != 1) {
                countFreeze.set(countFreeze.get() + userService.updateFreeze(user.getAccount()));
                log.info("UserSysClearJob updateFreeze:" + user.getAccount() + "," + user.getLoginTime() + "," + user.getAmount());
            }
        }
    }
}
