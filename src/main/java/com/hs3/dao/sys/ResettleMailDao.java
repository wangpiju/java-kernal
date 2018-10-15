package com.hs3.dao.sys;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.SQLUtils;
import com.hs3.entity.sys.ResettleMail;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("resettleMailDao")
public class ResettleMailDao
        extends BaseDao<ResettleMail> {
    public int update(ResettleMail m) {
        String sql = new SQLUtils(this.tableName).field("title,type,host,sendaddress,address,user,password,status,lotteryId,remark").where("id=?").getUpdate();
        return this.dbSession.update(sql, new Object[]{
                m.getTitle(),
                m.getType(),
                m.getHost(),
                m.getSendAddress(),
                m.getAddress(),
                m.getUser(),
                m.getPassword(),
                m.getStatus(),
                m.getLotteryId(),
                m.getRemark(),
                m.getId()});
    }

    public void save(ResettleMail m) {
        String sql = new SQLUtils(this.tableName).field("id,title,type,host,sendaddress,address,user,password,status,lotteryId,remark").getInsert();
        this.dbSession.update(sql, new Object[]{
                m.getId(),
                m.getTitle(),
                m.getType(),
                m.getHost(),
                m.getSendAddress(),
                m.getAddress(),
                m.getUser(),
                m.getPassword(),
                m.getStatus(),
                m.getLotteryId(),
                m.getRemark()});
    }

    public List<ResettleMail> listByStatusAndType(Integer type, Integer status) {
        String sql = new SQLUtils(this.tableName).where("type=? AND status=?").getSelect();
        Object[] args = {type, status};
        return this.dbSession.list(sql, args, this.cls);
    }

    public List<ResettleMail> list(Integer type, Integer status, String lotteryId) {
        String sql = new SQLUtils(this.tableName).where("type=? AND status=? AND lotteryId=? ORDER BY id").getSelect();
        Object[] args = {type, status, lotteryId};
        return this.dbSession.list(sql, args, this.cls);
    }
}
