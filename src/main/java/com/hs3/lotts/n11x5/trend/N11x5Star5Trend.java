package com.hs3.lotts.n11x5.trend;

import com.hs3.lotts.ssc.trend.SscStar5Trend;

public class N11x5Star5Trend extends SscStar5Trend {
    private static final String NAME = "五星";
    private static final String[] TITLES = {"第一位", "第二位", "第三位", "第四位", "第五位", "号码分布"};
    private static final Integer[] NUMS = {Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3),
            Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8),
            Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11)};
    private static final int NUMS_LEN = 11;

    public String getName() {
        return "五星";
    }

    public String[] getTitles() {
        return TITLES;
    }

    public Integer[] getNums() {
        return NUMS;
    }

    public int getNumLen() {
        return 11;
    }
}
