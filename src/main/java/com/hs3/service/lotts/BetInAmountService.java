package com.hs3.service.lotts;

import com.hs3.dao.lotts.BetInAmountDao;
import com.hs3.dao.lotts.BetInPriceDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.BetInAmount;
import com.hs3.entity.lotts.BetInPrice;
import com.hs3.exceptions.BaseCheckException;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("betInAmountService")
public class BetInAmountService {
    @Autowired
    private BetInAmountDao betInAmountDao;
    @Autowired
    private BetInPriceDao betInPriceDao;

    public void saveBatch(BetInAmount m, BigDecimal[] probabilitys) {
        BigDecimal count = BigDecimal.ZERO;
        List<BetInPrice> betInPriceList = this.betInPriceDao.list(null);
        for (int i = 0; i < betInPriceList.size(); i++) {
            BetInPrice betInPrice = (BetInPrice) betInPriceList.get(i);
            BigDecimal val = probabilitys[i];
            if (val == null) {
                throw new BaseCheckException("概率(" + betInPrice.getStart() + "-" + betInPrice.getEnd() + ")没有配置！");
            }
            count = count.add(val);

            m.setPriceId(betInPrice.getId());
            m.setProbability(val);
            this.betInAmountDao.save(m);
        }
        if (count.compareTo(new BigDecimal("100")) != 0) {
            throw new BaseCheckException("概率相加不得100！");
        }
    }

    public int update(BetInAmount bim, BigDecimal[] probabilitys) {
        int updateCount = 0;
        BetInAmount m = (BetInAmount) this.betInAmountDao.find(bim.getId());
        BigDecimal count = BigDecimal.ZERO;
        List<BetInAmount> list = this.betInAmountDao.listByRuleId(m.getRuleId(), m.getAmount());
        for (int i = 0; i < list.size(); i++) {
            BigDecimal val = probabilitys[i];
            if (val == null) {
                BetInPrice betInPrice = (BetInPrice) this.betInPriceDao.find(m.getPriceId());
                throw new BaseCheckException("概率(" + betInPrice.getStart() + "-" + betInPrice.getEnd() + ")没有配置！");
            }
            count = count.add(val);
            BetInAmount betInAmount = (BetInAmount) list.get(i);
            betInAmount.setRuleId(bim.getRuleId());
            betInAmount.setName(bim.getName());
            betInAmount.setAmount(bim.getAmount());
            betInAmount.setProbability(probabilitys[i]);
            updateCount += this.betInAmountDao.update(betInAmount);
        }
        if (count.compareTo(new BigDecimal("100")) != 0) {
            throw new BaseCheckException("概率相加不得100！");
        }
        return updateCount;
    }

    public int delete(List<Integer> ids) {
        /**jd-gui
         * int count = 0;
         Iterator localIterator2;
         for (Iterator localIterator1 = ids.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         Integer id = (Integer)localIterator1.next();
         BetInAmount m = (BetInAmount)this.betInAmountDao.find(id);
         List<BetInAmount> list = this.betInAmountDao.listByRuleId(m.getRuleId(), m.getAmount());
         localIterator2 = list.iterator(); continue;BetInAmount betInAmount = (BetInAmount)localIterator2.next();
         count += this.betInAmountDao.delete(betInAmount.getId());
         }
         return count;*/

        int count = 0;
        for (Iterator iterator = ids.iterator(); iterator.hasNext(); ) {
            Integer id = (Integer) iterator.next();
            BetInAmount m = (BetInAmount) betInAmountDao.find(id);
            List<BetInAmount> list = betInAmountDao.listByRuleId(m.getRuleId(), m.getAmount());
            for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); ) {
                BetInAmount betInAmount = (BetInAmount) iterator1.next();
                count += betInAmountDao.delete(betInAmount.getId());
            }

        }

        return count;

    }

    public List<BetInAmount> listByCond(BetInAmount m, Page p) {
        return this.betInAmountDao.listByCond(m, p);
    }

    public void save(BetInAmount m) {
        this.betInAmountDao.save(m);
    }

    public List<BetInAmount> list(Page p) {
        return this.betInAmountDao.list(p);
    }

    public List<BetInAmount> listWithOrder(Page p) {
        return this.betInAmountDao.listWithOrder(p);
    }

    public BetInAmount find(Integer id) {
        return (BetInAmount) this.betInAmountDao.find(id);
    }

    public List<BetInAmount> listByRuleId(Integer ruleId, BigDecimal amount) {
        return this.betInAmountDao.listByRuleId(ruleId, amount);
    }

    public List<BetInAmount> listAmount(Integer ruleId) {
        return this.betInAmountDao.listAmount(ruleId);
    }
}
