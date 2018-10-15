package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotterySaleTimeDao;
import com.hs3.dao.lotts.LotterySeasonDao;
import com.hs3.dao.sys.SysConfigDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.entity.sys.SysConfig;
import com.hs3.models.Jsoner;
import com.hs3.models.lotts.SimpleSeason;
import com.hs3.models.lotts.SimpleSeasonModel;
import com.hs3.utils.HttpUtils;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotterySeasonService")
public class LotterySeasonService {
    public static final String NUMBERSOURCE_URL = "NUMBERSOURCE_URL";
    @Autowired
    private LotterySeasonDao lotterySeasonDao;
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private BetService betService;
    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private SettlementService settlementService;

    public void save(LotterySeason lotterySeason) {
        this.lotterySeasonDao.save(lotterySeason);
    }

    public void update(LotterySeason lotterySeason) {
        this.lotterySeasonDao.update(lotterySeason);
    }

    public List<LotterySeason> list(String lotteryId, Page p) {
        return this.lotterySeasonDao.list(lotteryId, p);
    }

    public LotterySeason saveOpenNum(LotterySeason lotterySeason) {
        LotterySeason ls = this.lotterySeasonDao.getBylotteryIdAndSeason(lotterySeason.getLotteryId(), lotterySeason.getSeasonId());
        if (ls == null) {
            this.lotterySeasonDao.save(lotterySeason);
            this.lotterySaleTimeDao.updateOpenStatus(lotterySeason.getSeasonId(), lotterySeason.getLotteryId(), Integer.valueOf(1));
            return lotterySeason;
        }
        this.lotterySeasonDao.update(lotterySeason);
        return lotterySeason;
    }

    /**
     * @author jason.wang
     * 這邊邏輯很怪.......已改掉
     * @param lotterySeason
     * @return
     */
    public boolean repeatOpen(LotterySeason lotterySeason) {
        List<LotterySeason> lss = this.lotterySeasonDao.getLast(lotterySeason.getLotteryId(), 2);
        List<Integer> openList = new ArrayList<>();
        openList.add(lotterySeason.getN1());
        openList.add(lotterySeason.getN2());
        openList.add(lotterySeason.getN3());
        openList.add(lotterySeason.getN4());
        openList.add(lotterySeason.getN5());
        String newNums = ListUtils.toString(openList);
        List<Integer> list = new ArrayList<>();
//        for (LotterySeason ls : lss) {
//            if (!ls.getSeasonId().equals(lotterySeason.getSeasonId())) {
//                list.clear();
//                list.add(ls.getN1());
//                list.add(ls.getN2());
//                list.add(ls.getN3());
//                list.add(ls.getN4());
//                list.add(ls.getN5());
//                String oldNums = ListUtils.toString(list);
//                if (oldNums.equals(newNums)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    public Jsoner saveBatch(String lotteryId, SimpleSeasonModel ssm, Integer enforce) {
        if ((StrUtils.hasEmpty(new Object[]{enforce})) || (enforce.intValue() == 0)) {
            for (SimpleSeason ss : ssm.getSimpleSeasons()) {
                LotterySeason ls = this.lotterySeasonDao.getBylotteryIdAndSeason(lotteryId, ss.getSeason());
                if (!StrUtils.hasEmpty(new Object[]{ls})) {
                    return Jsoner.error("期号范围内已有存在的开奖号码，补录请在强制执行前打勾");
                }
            }
        }
        for (SimpleSeason ss : ssm.getSimpleSeasons()) {
            this.lotterySeasonDao.delete(lotteryId, ss.getSeason());
            String openNum = ss.getNums().trim();
            if (openNum.length() >= 40) {
                openNum = this.settlementService.jinuoZssc(ss.getNums().trim());
            }
            if (!this.betService.saveOpenNum(lotteryId, ss.getSeason(), openNum)) {
                return Jsoner.error("输入的内容有误！");
            }
            this.lotterySaleTimeDao.updateOpenStatus(ss.getSeason(), lotteryId, Integer.valueOf(4));
        }
        return Jsoner.success();
    }

    public String getOpened(String lotteryId, String startSeason, String endSeason) {
        SysConfig sc = (SysConfig) this.sysConfigDao.find("NUMBERSOURCE_URL");
        try {
            return HttpUtils.getString(sc.getVal() + "?lotteryId=" + lotteryId + "&startSeason=" + startSeason + "&endSeason=" + endSeason);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
