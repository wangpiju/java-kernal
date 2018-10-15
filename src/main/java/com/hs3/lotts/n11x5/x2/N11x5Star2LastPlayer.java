package com.hs3.lotts.n11x5.x2;

import com.hs3.lotts.NumberView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class N11x5Star2LastPlayer extends N11x5Star2FrontPlayer {
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("第4位", this.nums), new NumberView("第5位", this.nums)};

    protected void init() {
        setRemark("从01-11共11个号码中选择2个不重复的号码组成一注，所选号码与当期顺序摇出的5个号码中 的后2个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02 开奖：*,*,*,01,02");
    }

    public String getQunName() {
        return "后二";
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    protected int index() {
        return 3;
    }

    protected int numLen() {
        return 2;
    }
}
