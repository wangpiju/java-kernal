package com.hs3.dao.report;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.report.UserState;
import com.hs3.entity.report.ZongDaiState;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("userStateDao")
public class UserStateDao
        extends BaseDao<UserState> {
    private static StringBuilder sb = new StringBuilder();
    private static StringBuilder sb2 = new StringBuilder();

    static {
        sb.append(" SELECT                                       ");
        sb.append("   x.*,                                       ");
        sb.append("   x.player + x.daili alls                    ");
        sb.append(" FROM                                         ");
        sb.append("   (SELECT                                    ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user a                               ");
        sb.append("     WHERE a.regTime = a.loginTime) notLogin, ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user a                               ");
        sb.append("     WHERE NOT EXISTS                         ");
        sb.append("       (SELECT                                ");
        sb.append("         1                                    ");
        sb.append("       FROM                                   ");
        sb.append("         t_bank_user b                        ");
        sb.append("       WHERE b.account = a.account)) notBind, ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user a                               ");
        sb.append("     WHERE a.regchargeCount = 0) notRecharge, ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user a                               ");
        sb.append("     WHERE a.isBet = '0') notBet,             ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user u                               ");
        sb.append("     WHERE u.userType = 0) AS player,         ");
        sb.append("     (SELECT                                  ");
        sb.append("       COUNT(1)                               ");
        sb.append("     FROM                                     ");
        sb.append("       t_user u                               ");
        sb.append("     WHERE u.userType = 1) AS daili) X        ");


        sb2.append(" SELECT                                      ");
        sb2.append("   x.*,                                      ");
        sb2.append("   x.player + x.daili alls,                  ");
        sb2.append("   x.player2 + x.daili2 alls2                ");
        sb2.append(" FROM                                        ");
        sb2.append("   (SELECT                                   ");
        sb2.append("     u.account,                              ");
        sb2.append("     (SELECT                                 ");
        sb2.append("       COUNT(1)                              ");
        sb2.append("     FROM                                    ");
        sb2.append("       t_user a                              ");
        sb2.append("     WHERE a.parentAccount = u.account       ");
        sb2.append("       AND a.account <> u.account            ");
        sb2.append("       AND a.userType = 0                    ");
        sb2.append("       AND NOT EXISTS                        ");
        sb2.append("       (SELECT                               ");
        sb2.append("         1                                   ");
        sb2.append("       FROM                                  ");
        sb2.append("         t_ext_code_l b                      ");
        sb2.append("       WHERE b.account = a.account)) player, ");
        sb2.append("     (SELECT                                 ");
        sb2.append("       COUNT(1)                              ");
        sb2.append("     FROM                                    ");
        sb2.append("       t_user a                              ");
        sb2.append("     WHERE a.parentAccount = u.account       ");
        sb2.append("       AND a.account <> u.account            ");
        sb2.append("       AND a.userType = 1                    ");
        sb2.append("       AND NOT EXISTS                        ");
        sb2.append("       (SELECT                               ");
        sb2.append("         1                                   ");
        sb2.append("       FROM                                  ");
        sb2.append("         t_ext_code_l b                      ");
        sb2.append("       WHERE b.account = a.account)) daili,  ");
        sb2.append("     (SELECT                                 ");
        sb2.append("       COUNT(1)                              ");
        sb2.append("     FROM                                    ");
        sb2.append("       t_ext_code_l a                        ");
        sb2.append("     WHERE EXISTS                            ");
        sb2.append("       (SELECT                               ");
        sb2.append("         1                                   ");
        sb2.append("       FROM                                  ");
        sb2.append("         t_ext_code b                        ");
        sb2.append("       WHERE b.account = u.account           ");
        sb2.append("         AND b.code = a.code                 ");
        sb2.append("         AND b.userType = 0)) player2,       ");
        sb2.append("     (SELECT                                 ");
        sb2.append("       COUNT(1)                              ");
        sb2.append("     FROM                                    ");
        sb2.append("       t_ext_code_l a                        ");
        sb2.append("     WHERE EXISTS                            ");
        sb2.append("       (SELECT                               ");
        sb2.append("         1                                   ");
        sb2.append("       FROM                                  ");
        sb2.append("         t_ext_code b                        ");
        sb2.append("       WHERE b.account = u.account           ");
        sb2.append("         AND b.code = a.code                 ");
        sb2.append("         AND b.userType = 1)) daili2         ");
        sb2.append("   FROM                                      ");
        sb2.append("     t_user u                                ");
        sb2.append("   WHERE u.rootAccount = u.account) X        ");
    }

    public UserState getUserState() {
        return this.dbSession.getObject(sb.toString(), this.cls);
    }

    public List<ZongDaiState> getZongDaiState(Page page) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT a.parentAccount as account, ");
        sb.append(" sum(case  when b.code is  null and a.userType =0 then 1 ELSE 0 END) as player,");
        sb.append(" sum(case  when b.code is  null and a.userType =1 then 1 ELSE 0 END) as daili,");
        sb.append(" sum(case  when b.code is not null and a.userType =0 then 1 ELSE 0 END) as player2,");
        sb.append(" sum(case  when b.code is not null and a.userType =1 then 1 ELSE 0 END) as daili2,");
        sb.append(" sum(case  when b.code is  null and a.userType =0 then 1 ELSE 0 END) + sum(case  when b.account is  null and a.userType =1 then 1 ELSE 0 END) as alls,");
        sb.append(" sum(case  when b.code is not null and a.userType =0 then 1 ELSE 0 END) + sum(case  when b.account is not null and a.userType =1 then 1 ELSE 0 END) as alls2 ");
        sb.append(" from t_user a LEFT JOIN  t_ext_code_l b on a.account = b.account");
        sb.append(" WHERE a.parentAccount = a.rootAccount and a.account <> a.parentAccount ");
        sb.append(" GROUP BY a.parentAccount ");
        return this.dbSession.list(sb.toString(), ZongDaiState.class, page);
    }
}
