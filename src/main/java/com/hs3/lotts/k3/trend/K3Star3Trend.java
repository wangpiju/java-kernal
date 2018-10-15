package com.hs3.lotts.k3.trend;

import com.hs3.lotts.ssc.trend.SscStar5Trend;

public class K3Star3Trend extends SscStar5Trend {
    private static final String NAME = "三星";
    private static final String[] TITLES = {"百位", "十位", "个位", "号码分布"};
    private static final Integer[] NUMS = {1, 2, 3,
            4, 5, 6};
    private static final int NUMS_LEN = 6;
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
        return 6;
    }

    public int getOpenLen() {
        return 3;
    }
}
