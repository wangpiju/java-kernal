package com.hs3.dao.lotts;

import com.hs3.dao.BaseDao;
import com.hs3.db.DbSession;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInPrice;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("betInPriceDao")
public class BetInPriceDao
        extends BaseDao<BetInPrice> {
    private static final String SQL_updateSurplusNum = "UPDATE t_bet_in_price SET surplusNum = surplusNum + ? WHERE id = ?";

    public int updateSurplusNum(Integer id, int num) {
        return this.dbSession.update("UPDATE t_bet_in_price SET surplusNum = surplusNum + ? WHERE id = ?", new Object[]{Integer.valueOf(num), id});
    }

    public List<BetInPrice> list(Page page) {
        String sql = "SELECT * FROM t_bet_in_price ORDER BY end DESC";
        return this.dbSession.list(sql, this.cls, page);
    }

    public void save(BetInPrice m) {
        saveAuto(m);
    }

    public int update(BetInPrice m) {
        return updateByIdAuto(m, m.getId());
    }

    protected String[] getColumns() {
        return new String[]{"start", "end", "surplusNum", "warnNum", "totalAmount", "addNum"};
    }

    protected Object[] getValues(BetInPrice m) {
        return new Object[]{m.getStart(), m.getEnd(), m.getSurplusNum(), m.getWarnNum(), m.getTotalAmount(), m.getAddNum()};
    }
}
