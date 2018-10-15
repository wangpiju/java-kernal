package com.hs3.lotts.n3.trend;

import com.hs3.lotts.ssc.trend.SscStar5Trend;

public class N3Star3Trend extends SscStar5Trend {
    private static final String NAME = "三星";
    private static final String[] TITLES = {"百位", "十位", "个位", "号码分布"};
    private static final Integer[] NUMS = {Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2),
            Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7),
            Integer.valueOf(8), Integer.valueOf(9)};
    private static final int NUMS_LEN = 10;
    private static final int OPEN_LEN = 3;

    public String getName() {
        return "三星";
    }

    public String[] getTitles() {
        return TITLES;
    }

    public Integer[] getNums() {
        return NUMS;
    }

    public int getNumLen() {
        return 10;
    }

    public int getOpenLen() {
        return 3;
    }
}
