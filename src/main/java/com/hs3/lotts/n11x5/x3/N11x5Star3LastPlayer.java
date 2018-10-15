package com.hs3.lotts.n11x5.x3;

import com.hs3.lotts.NumberView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class N11x5Star3LastPlayer extends N11x5Star3FrontPlayer {
    private String qunName = "后三";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"}));
    private NumberView[] view = {new NumberView("第3位", this.nums), new NumberView("第4位", this.nums),
            new NumberView("第5位", this.nums)};

    protected void init() {
        setRemark("从01-11共11个号码中选择3个不重复的号码组成一注，所选号码与当期顺序摇出的5个号码中 的后3个号码相同，且顺序一致，即为中奖");
        setExample("投注：01,02,03 开奖：*,*,01,02,03");
    }

    protected int index() {
        return 2;
    }

    protected int numLen() {
        return 3;
    }

    public String getQunName() {
        return this.qunName;
    }

    public NumberView[] getNumView() {
        return this.view;
    }
}
