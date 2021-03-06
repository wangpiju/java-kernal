package com.hs3.lotts.pk10.star5;

import com.hs3.lotts.NumberView;
import com.hs3.lotts.pk10.star2.Pk10Star2Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pk10Star5Player extends Pk10Star2Player {
    private BigDecimal bonus = new BigDecimal("60480").divide(new BigDecimal(2));
    private String qunName = "前五";
    private String basicBet = "01,02,03,04,05";
    private List<String> nums = new ArrayList(
            Arrays.asList(new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"}));
    private NumberView[] view = {new NumberView("冠军", this.nums), new NumberView("亚军", this.nums),
            new NumberView("三名", this.nums), new NumberView("四名", this.nums), new NumberView("五名", this.nums)};

    protected int getLen() {
        return 5;
    }

    protected void init() {
        setRemark("冠军、亚军、第三名、第四名、四五名各选一个号码，所选号码与开奖号码前5名车号相同，且顺序一致，即中奖");
        setExample("投注：01,02,03,04,05 开奖：01,02,03,04,05,*,*,*,*,* 即中奖");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public NumberView[] getNumView() {
        return this.view;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getBasicBet() {
        return basicBet;
    }

    public void setBasicBet(String basicBet) {
        this.basicBet = basicBet;
    }
}
