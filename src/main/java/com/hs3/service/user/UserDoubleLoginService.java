package com.hs3.service.user;

import com.hs3.dao.user.UserDao;
import com.hs3.dao.user.UserDoubleLoginDao;
import com.hs3.db.Page;
import com.hs3.entity.users.User;
import com.hs3.entity.users.UserDoubleLogin;
import com.hs3.exceptions.UnLogException;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserDoubleLoginService {
    private static final Logger logger = LoggerFactory.getLogger(UserDoubleLoginService.class);
    private static Map<String, Integer> cache = new HashMap();
    private static ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
    private static boolean inited = false;
    @Autowired
    private UserDoubleLoginDao dao;
    @Autowired
    private UserDao userDao;

    public UserDoubleLogin find(Integer id) {
        return (UserDoubleLogin) this.dao.find(id);
    }

    public int save(UserDoubleLogin m) {
        User u = this.userDao.findByAccount(m.getAccount());
        if (u == null) {
            throw new UnLogException("用户不存在");
        }
        if ((m.getStatus().intValue() == 1) && (!u.getRootAccount().equals(m.getAccount()))) {
            throw new UnLogException("只有总代可以设置成【整团队】");
        }
        return this.dao.saveAuto(m);
    }

    public int update(UserDoubleLogin m) {
        User u = this.userDao.findByAccount(m.getAccount());
        if (u == null) {
            throw new UnLogException("用户不存在");
        }
        if ((m.getStatus().intValue() == 1) && (!u.getRootAccount().equals(m.getAccount()))) {
            throw new UnLogException("只有总代可以设置成【整团队】");
        }
        return this.dao.updateByIdAuto(m, m.getId());
    }

    public int delete(List<Integer> ids) {
        return this.dao.delete(ids);
    }

    public List<UserDoubleLogin> list(String account, Integer status, Page p) {
        return this.dao.list(account, status, p);
    }

    public synchronized void init() {
        if (!inited) {
            excutor.scheduleWithFixedDelay(new EchoServer(this),
                    0L,
                    2L,
                    TimeUnit.MINUTES);
            logger.info("允许用户重复登录：刷新配置的定时器【2分钟】");
        }
    }

    public boolean canDouble(String account, String root) {
        if (cache.containsKey(account)) {
            return true;
        }
        if (cache.containsKey(root)) {
            Integer n = (Integer) cache.get(root);
            return n.intValue() == 1;
        }
        return false;
    }

    public void load() {
        Map<String, Integer> map = new HashMap();
        List<UserDoubleLogin> list = this.dao.listStatus();
        for (UserDoubleLogin m : list) {
            map.put(m.getAccount(), m.getStatus());
        }
        cache = map;
    }

    private class EchoServer
            implements Runnable {
        private UserDoubleLoginService service;
        private final Logger logger = LoggerFactory.getLogger(EchoServer.class);

        public EchoServer(UserDoubleLoginService s) {
            this.service = s;
        }

        public void run() {
            try {
                this.service.load();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
