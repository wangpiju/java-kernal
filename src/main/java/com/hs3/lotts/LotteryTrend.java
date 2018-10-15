package com.hs3.lotts;

import com.hs3.models.lotts.SeasonOpen;
import com.hs3.models.lotts.SeasonTrend;

import java.util.List;
import java.util.Map;

public abstract class LotteryTrend {
    public abstract String getName();

    public abstract String[] getTitles();

    public abstract Integer[] getNums();

    public abstract int getNumLen();

    public abstract int getOpenLen();

    public abstract void setData(List<SeasonOpen> paramList, List<Map<String, Object>> paramList1);

    public abstract List<SeasonTrend> getTrends();

    public abstract List<SeasonTrend> getAllTrends();
}
