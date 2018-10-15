package com.hs3.lotts.pk10.trend;

import com.hs3.lotts.ssc.trend.SscStar5Trend;

public class Pk10Star5Trend
        extends SscStar5Trend {
    private static final String NAME = "前五名";
    private static final String[] TITLES = {"冠军", "亚军", "三名", "四名", "五名", "号码分布"};
    private static final Integer[] NUMS = {Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10)};
    private static final int NUMS_LEN = 10;

    public String getName() {
        return "前五名";
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
}
