package com.hs3.dao.webs;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.webs.Img;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("imgDao")
public class ImgDao
        extends BaseDao<Img> {
    public void save(Img m) {
        String sql = new SQLUtils(this.tableName).field("title,img,link,status,orderId").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getTitle(),
                m.getImg(),
                m.getLink(),
                m.getStatus(),
                m.getOrderId()});
    }

    public int update(Img m) {
        SQLUtils su = new SQLUtils(this.tableName).field("title,link,status,orderId").where("id=?");
        List<Object> args = new ArrayList();
        args.add(m.getTitle());
        args.add(m.getLink());
        args.add(m.getStatus());
        args.add(m.getOrderId());
        if (m.getImg() != null) {
            su.field("img");
            args.add(m.getImg());
        }
        args.add(m.getId());
        String sql = su.getUpdate();
        return this.dbSession.update(sql, args.toArray());
    }

    public List<Img> listByShow(int status) {
        String sql = new SQLUtils(this.tableName).where("status=0 OR status=?").orderBy("orderId DESC").getSelect();
        return this.dbSession.list(sql, new Object[]{Integer.valueOf(status)}, this.cls);
    }
}
