package com.hs3.service.lotts;

import com.hs3.dao.lotts.LotteryCloseRuleDao;
import com.hs3.dao.lotts.LotteryDao;
import com.hs3.dao.lotts.LotterySaleRuleDao;
import com.hs3.dao.lotts.LotterySaleTimeDao;
import com.hs3.dao.lotts.SeasonForDateDao;
import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotteryCloseRule;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.entity.lotts.SeasonForDate;
import com.hs3.exceptions.BaseException;
import com.hs3.lotts.rule.ISeasonBuilder;
import com.hs3.lotts.rule.SeasonBuilderFactory;
import com.hs3.utils.DateUtils;
import com.hs3.utils.sys.WebDateUtils;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSeasonService {
    @Autowired
    private LotterySaleRuleDao lotterySaleRuleDao;
    @Autowired
    private LotteryCloseRuleDao lotteryCloseRuleDao;
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private LotterySaleTimeDao lotterySaleTimeDao;
    @Autowired
    private SeasonForDateDao seasonForDateDao;

    private static final Logger logger = LoggerFactory.getLogger(CreateSeasonService.class);

    public void saveSeason(String lotteryId, Date begin, int days) {
        Lottery lottery = (Lottery) this.lotteryDao.find(lotteryId);
//start saveSeason, lotteryId:tjssc , beginDate:Sat Jun 16 00:00:00 CST 2018, days:3
        logger.info(String.format("--> start saveSeason, lotteryId:%s , beginDate:%s, days:%s", lotteryId, begin, days));
        this.lotterySaleTimeDao.deleteByStartDate(begin, lotteryId);

        List<LotterySaleRule> rule = this.lotterySaleRuleDao.listByStatus(lotteryId, 0);
        List<LotteryCloseRule> close = this.lotteryCloseRuleDao.listByStatus(lotteryId, 0);
        ISeasonBuilder iSeasonBuilder = SeasonBuilderFactory.getInstance(lottery.getSeasonRule());

        List<LotterySaleTime> lstLotterySaleTimes = iSeasonBuilder.create(lottery, rule, close, begin, days);
        logger.info("--> create lotterySaleTime success, lstLotterySaleTimes.size :"+ lstLotterySaleTimes.size());
        for (LotterySaleTime lotterySaleTime : lstLotterySaleTimes) {
            this.lotterySaleTimeDao.save(lotterySaleTime);
        }
        logger.info("--> saveSaleTime success, lotteryId:"+lotteryId);
    }

    public void saveSeasonAuto(Lottery lottery) {
        int daySpace = 3;
        String lotteryId = lottery.getId();
        SeasonForDate seasonForDate = this.seasonForDateDao.getByLotteryId(lotteryId);
        if ((seasonForDate != null) && (seasonForDate.getAutoCreateDay().intValue() > daySpace)) {
            daySpace = seasonForDate.getAutoCreateDay().intValue();
        }
        Date begin = DateUtils.toDateNull(DateUtils.format(new Date()), "yyyy-MM-dd");
        int hasCount = this.lotterySaleTimeDao.findMaxDayCount(lotteryId, begin);
        if (hasCount >= daySpace) {
            return;
        }
        String max = this.lotterySaleTimeDao.maxDaySeason(lotteryId);
        if (max != null) {
            begin = DateUtils.addDay(DateUtils.toDateNull(max, "yyyy-MM-dd"), 1);
        }
        List<LotterySaleRule> rules = this.lotterySaleRuleDao.listByStatus(lotteryId, Integer.valueOf(0));
        if (rules.isEmpty()) {
            throw new BaseException(lotteryId + ":查不到奖期规则, lotteryId : "+lotteryId);
        }
        List<LotteryCloseRule> closes = this.lotteryCloseRuleDao.listByStatus(lotteryId, 0);

        ISeasonBuilder sb = SeasonBuilderFactory.getInstance(lottery.getSeasonRule());
        List<LotterySaleTime> list = sb.create(lottery, rules, closes, begin, daySpace);
        for (LotterySaleTime saleTime : list) {
            this.lotterySaleTimeDao.save(saleTime);
        }
        Date d = WebDateUtils.getBeginTime(new Date());
        if (lottery.getGroupId().equals("3d")) {
            d = DateUtils.addDay(d, -30);
        } else {
            d = DateUtils.addDay(d, -3);
        }
        this.lotterySaleTimeDao.deleteByDate(d, lottery.getId());
    }
}
