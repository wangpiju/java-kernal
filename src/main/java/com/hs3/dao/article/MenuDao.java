package com.hs3.dao.article;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.article.Menu;
import com.hs3.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("menuDao")
public class MenuDao
        extends BaseDao<Menu> {
    public List<Menu> listByCond(Menu m, Page page) {
        String sql = String.format("SELECT %s FROM %s %s", new Object[]{"*", this.tableName, "WHERE 1 = 1"});
        List<Object> cond = new ArrayList();
        if (!StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            sql = sql + " AND title like CONCAT('%', ?, '%')";
            cond.add(m.getTitle());
        }
        if (m.getStatus() != null) {
            sql = sql + " AND status = ?";
            cond.add(m.getStatus());
        }
        if (m.getPosition() != null) {
            sql = sql + " AND position = ?";
            cond.add(m.getPosition());
        }
        sql = sql + " ORDER BY orderId desc";

        return this.dbSession.list(sql, cond.toArray(new Object[cond.size()]), this.cls, page);
    }

    public void save(Menu m) {
        saveAuto(m);
    }

    public int update(Menu m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"id", "title", "position", "status", "orderId", "url"};
    }

    protected Object[] getValues(Menu m) {
        return new Object[]{m.getId(), m.getTitle(), m.getPosition(), m.getStatus(), m.getOrderId(), m.getUrl()};
    }
}
