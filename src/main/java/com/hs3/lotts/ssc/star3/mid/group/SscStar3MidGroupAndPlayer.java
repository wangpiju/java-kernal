package com.hs3.lotts.ssc.star3.mid.group;

import com.hs3.lotts.ssc.star3.front.group.SscStar3FrontGroupAndPlayer;

public class SscStar3MidGroupAndPlayer
        extends SscStar3FrontGroupAndPlayer {
    protected void init() {
        setRemark("至少选择一个和值，竞猜开奖号码中三位数字之和(不含豹子号)");
        setExample("投注：和值6 开奖：(1) *015*（不限顺序）中组六 (2)*033*（不限顺序），中组三");
    }

    public String getQunName() {
        return "中三";
    }

    protected int index() {
        return 1;
    }
}
