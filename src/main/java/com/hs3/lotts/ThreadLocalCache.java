package com.hs3.lotts;

import com.hs3.dao.lotts.BonusGroupDao;
import com.hs3.dao.lotts.BonusGroupDetailsDao;
import com.hs3.dao.user.UserDao;
import com.hs3.entity.lotts.AmountChange;
import com.hs3.entity.lotts.BonusGroup;
import com.hs3.entity.lotts.BonusGroupDetails;
import com.hs3.entity.users.User;
import com.hs3.web.utils.SpringContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadLocalCache {
    private static UserDao userDao = (UserDao) SpringContext.getBean(UserDao.class);
    private static BonusGroupDao bonusGroupDao = (BonusGroupDao) SpringContext.getBean(BonusGroupDao.class);
    private static BonusGroupDetailsDao bonusGroupDetailsDao = (BonusGroupDetailsDao) SpringContext
            .getBean(BonusGroupDetailsDao.class);
    private static ThreadLocal<Map<String, List<AmountChange>>> CHANGES_CACHE = new ThreadLocal<>();
    private static ThreadLocal<Map<String, User>> USERS_CACHE = new ThreadLocal<>();
    private static ThreadLocal<Map<Integer, BonusGroup>> GROUPS_CACHE = new ThreadLocal<>();
    private static ThreadLocal<Map<String, BonusGroupDetails>> GROUPS_DETAILS_CACHE = new ThreadLocal<>();

    public static void clear() {
        CHANGES_CACHE.remove();
        USERS_CACHE.remove();
        GROUPS_CACHE.remove();
        GROUPS_DETAILS_CACHE.remove();
    }

    public static List<AmountChange> getAmountChanges(String account) {
        Map<String, List<AmountChange>> changes = CHANGES_CACHE.get();
        if (changes == null) {
            changes = new HashMap<>();
            CHANGES_CACHE.set(changes);
        }
        List<AmountChange> list = null;
        if (changes.containsKey(account)) {
            list = changes.get(account);
        } else {
            list = new ArrayList<>();
            changes.put(account, list);
        }
        return list;
    }

    public static Map<String, List<AmountChange>> getAmountChanges() {
        Map<String, List<AmountChange>> changes = CHANGES_CACHE.get();
        CHANGES_CACHE.remove();
        return changes;
    }

    public static User getUser(String account) {
        Map<String, User> users = USERS_CACHE.get();
        if (users == null) {
            users = new HashMap<>();
            USERS_CACHE.set(users);
        }
        User u = null;
        if (users.containsKey(account)) {
            u = users.get(account);
        } else {
            u = userDao.findByAccount(account);
            if (u != null) {
                users.put(account, u);
            }
        }
        return u;
    }

    public static BonusGroup getGroup(Integer id) {
        Map<Integer, BonusGroup> cache = GROUPS_CACHE.get();
        if (cache == null) {
            cache = new HashMap<>();
            GROUPS_CACHE.set(cache);
        }
        BonusGroup v = null;
        if (cache.containsKey(id)) {
            v = cache.get(id);
        } else {
            v = bonusGroupDao.find(id);
            if (v != null) {
                cache.put(id, v);
            }
        }
        return v;
    }

    public static BonusGroupDetails getGroupDetails(String playId, String lotteryId, Integer groupId) {
        String key = playId + lotteryId + groupId;
        Map<String, BonusGroupDetails> cache = GROUPS_DETAILS_CACHE.get();
        if (cache == null) {
            cache = new HashMap<>();
            GROUPS_DETAILS_CACHE.set(cache);
        }
        BonusGroupDetails v = null;
        if (cache.containsKey(key)) {
            v = cache.get(key);
        } else {
            v = bonusGroupDetailsDao.find(playId, lotteryId, groupId);
            if (v != null) {
                cache.put(key, v);
            }
        }
        return v;
    }

    public static boolean getHasRatio(User user) {
        boolean parentRatio = true;
        if ((user.getRegchargeCount() <= 0) && (user.getRechargeLowerTarCountTotal() <= 0)) {
            return false;
        }
        return parentRatio;
    }
}
