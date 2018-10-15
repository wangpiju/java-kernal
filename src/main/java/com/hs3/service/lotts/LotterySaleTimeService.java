package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryDao;
import com.hs3.dao.lotts.LotterySaleTimeDao;
import com.hs3.dao.lotts.SeasonForDateDao;
import com.hs3.db.Page;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.entity.lotts.SeasonForDate;
import com.hs3.models.lotts.SeasonCount;
import com.hs3.models.lotts.TraceSeasonId;
import com.hs3.utils.DateUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lotterySaleTimeService")
public class LotterySaleTimeService {
    private static final Logger logger = LoggerFactory.getLogger(LotterySaleTimeService.class);
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private SeasonForDateDao seasonForDateDao;
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private CreateSeasonService createSeasonService;

    public List<LotterySaleTime> list(String lotteryId, Date startTime, Date endTime, Page p) {
        return this.lotterySaleTimeDao.list(lotteryId, startTime, endTime, p);
    }

    public LotterySaleTime find(String seasonId, String lotteryId) {
        return this.lotterySaleTimeDao.find(seasonId, lotteryId);
    }

    public void save(LotterySaleTime m) {
        this.lotterySaleTimeDao.save(m);
    }

    public void saveBatch(List<LotterySaleTime> lstLotterySaleTime) {
        for (Iterator<LotterySaleTime> iterator = lstLotterySaleTime.iterator(); iterator.hasNext(); ) {
            LotterySaleTime lotterySaleTime = (LotterySaleTime) iterator.next();
            this.lotterySaleTimeDao.save(lotterySaleTime);
        }
    }

    public void delete(Date begin, Date end, String lotteryid) {
        this.lotterySaleTimeDao.delete(begin, end, lotteryid);
    }

    public void deleteByStartDate(Date begin, String lotteryid) {
        this.lotterySaleTimeDao.deleteByStartDate(begin, lotteryid);
    }

    public void deleteByDate(Date end, String lotteryid) {
        this.lotterySaleTimeDao.deleteByDate(end, lotteryid);
    }

    public String maxDaySeason(String lotteryid) {
        return this.lotterySaleTimeDao.maxDaySeason(lotteryid);
    }

    public boolean updateOpenStatus(String seasonId, String lotteryId, Integer openStatus) {
        return this.lotterySaleTimeDao.updateOpenStatus(seasonId, lotteryId, openStatus);
    }

    public boolean updateSettleStatus(String seasonId, String lotteryId, Integer PreSettleStatus, Integer settleStatus) {
        return this.lotterySaleTimeDao.updateSettleStatus(seasonId, lotteryId, PreSettleStatus, settleStatus);
    }

    public boolean updatePlanStatus(String seasonId, String lotteryId, Integer prePlanStatus, Integer planStatus) {
        return this.lotterySaleTimeDao.updatePlanStatus(seasonId, lotteryId, prePlanStatus, planStatus);
    }

    public LotterySaleTime getCurrentByLotteryId(String lotteryId) {
        return this.lotterySaleTimeDao.getCurrentByLotteryId(lotteryId);
    }

    public SeasonCount getSeasonCountByLotteryId(String lotteryId, Date d) {
        return this.lotterySaleTimeDao.getSeasonCountByLotteryId(lotteryId, d);
    }

    public LotterySaleTime getPreviousSeasonByLotteryId(String lotteryId, Date d, Integer openCycle) {
        Date preDate = DateUtils.AddSecond(d, openCycle.intValue());
        return this.lotterySaleTimeDao.getPreviousSeasonByLotteryId(lotteryId, preDate);
    }

    public LotterySaleTime getNextByLotteryId(String lotteryId, String seasonId, Date openTime) {
        return this.lotterySaleTimeDao.getNextByLotteryId(lotteryId, seasonId, openTime);
    }

    public LotterySaleTime getPreviousByLotteryId(String lotteryId, Date d) {
        return this.lotterySaleTimeDao.getPreviousByLotteryId(lotteryId, d);
    }

    public List<TraceSeasonId> listTraceSeasonId(String lotteryId, Integer count) {
        return this.lotterySaleTimeDao.listTraceSeasonId(lotteryId, count);
    }

    public List<String> listSeasonId(String lotteryId, Integer count) {
        return this.lotterySaleTimeDao.listSeasonId(lotteryId, count);
    }

    public List<LotterySaleTime> listDuringSeason(String lotteryId, String seasonBegin, String seasonEnd) {
        return this.lotterySaleTimeDao.listDuringSeason(lotteryId, seasonBegin, seasonEnd);
    }

    public LotterySaleTime getByLotteryIdAndSeasonId(String lotteryId, String seasonId) {
        return this.lotterySaleTimeDao.getByLotteryIdAndSeasonId(lotteryId, seasonId);
    }

    public void saveCanada(LotterySeason lSeason) {
        List<LotterySaleTime> lst = this.lotterySaleTimeDao.listByOpenTime(lSeason.getLotteryId(), lSeason.getSeasonId());
        logger.info("加拿大第一期" + ((LotterySaleTime) lst.get(0)).getSeasonId() + "  " + ((LotterySaleTime) lst.get(0)).getOpenTime());
        logger.info("加拿大最后期" + ((LotterySaleTime) lst.get(lst.size() - 1)).getSeasonId() + "  " + ((LotterySaleTime) lst.get(lst.size() - 1)).getOpenTime());
        Date secondDate = null;
        if (lst.size() > 0) {
            int len = (int) DateUtils.getSecondBetween(((LotterySaleTime) lst.get(0)).getOpenTime(), lSeason.getOpenTime());
            for (LotterySaleTime lt : lst) {
                Date tempOpenTimeDate = DateUtils.AddSecond(lt.getOpenTime(), len);
                this.lotterySaleTimeDao.updateTime(DateUtils.AddSecond(lt.getBeginTime(), len), DateUtils.AddSecond(lt.getEndTime(), len),
                        tempOpenTimeDate, DateUtils.AddSecond(lt.getOpenAfterTime(), len), lSeason.getLotteryId(), lt.getSeasonId());
                logger.info("加拿大当前期" + lt.getSeasonId() + "  " + lt.getOpenTime());
                if (DateUtils.format(tempOpenTimeDate, "HH:mm:ss").equals("14:58:30")) {
                    secondDate = tempOpenTimeDate;
                }
                if (DateUtils.format(tempOpenTimeDate, "HH:mm:ss").equals("15:02:00")) {
                    SeasonForDate sfd = this.seasonForDateDao.getByLotteryId(lSeason.getLotteryId());
                    sfd.setSeasonDate(tempOpenTimeDate);
                    sfd.setFirstSeason(lt.getSeasonId());
                    this.seasonForDateDao.update(sfd);
                    logger.info("加拿大更新基准期号" + lt.getSeasonId() + "  " + DateUtils.format(tempOpenTimeDate));
                }
            }
        }
        this.lotterySaleTimeDao.deleteByStartDateTime(secondDate, lSeason.getLotteryId());
        logger.info("加拿大删除多余的奖期" + lSeason.getSeasonId() + " " + DateUtils.format(secondDate));

        this.createSeasonService.saveSeason(lSeason.getLotteryId(), DateUtils.getDate(secondDate), 1);
        logger.info("加拿大生成下一天的奖期");

        this.lotteryDao.updateStatus(lSeason.getLotteryId(), Integer.valueOf(0));
        logger.info("加拿大启动销售");
    }

    public String minDaySeason(String lotteryId, Date date) {
        return this.lotterySaleTimeDao.minDaySeason(lotteryId, date);
    }

    public LotterySaleTime getBySeasonId(String lotteryId) {
        return this.lotterySaleTimeDao.getBySeasonId(lotteryId);
    }

    public List<LotterySaleTime> listException(Date begin) {
        return this.lotterySaleTimeDao.listException(begin, new Date());
    }
}
