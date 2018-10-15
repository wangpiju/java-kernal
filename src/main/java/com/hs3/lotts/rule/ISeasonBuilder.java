package com.hs3.lotts.rule;

import com.hs3.entity.lotts.Lottery;
import com.hs3.entity.lotts.LotteryCloseRule;
import com.hs3.entity.lotts.LotterySaleRule;
import com.hs3.entity.lotts.LotterySaleTime;

import java.util.Date;
import java.util.List;

public abstract interface ISeasonBuilder {
    public abstract String getTitle();

    public abstract String getRemark();

    public abstract List<LotterySaleTime> create(Lottery paramLottery, List<LotterySaleRule> paramList, List<LotteryCloseRule> paramList1, Date paramDate, int paramInt);
}
