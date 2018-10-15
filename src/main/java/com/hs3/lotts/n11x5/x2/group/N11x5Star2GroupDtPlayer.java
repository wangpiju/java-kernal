package com.hs3.lotts.n11x5.x2.group;

import com.hs3.lotts.n11x5.nx.N11x5DtPlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class N11x5Star2GroupDtPlayer extends N11x5DtPlayer {
    private BigDecimal bonus = new BigDecimal("110").divide(new BigDecimal(2));
    private String qunName = "前二";
    private String groupName = "组选";

    protected void init() {
        setRemark("从01-11共11个号码中至少选择2个以上号码进行投注，每注需至少包括1个胆码及1个拖码。只要当期的前2位开奖号码中有2个包含所选号码（每注包含2个号码），即为中奖");
        setExample("投注：01,02 开奖：01,02,*,*,*（不限顺序）");
    }

    public BigDecimal getBonus() {
        return this.bonus;
    }

    public String getQunName() {
        return this.qunName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    protected int getSelectNum() {
        return 2;
    }

    protected int getWinNum() {
        return 2;
    }

    protected List<Integer> getIndexs() {
        return Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1)});
    }

    public BigDecimal getWin(String bets, List<Integer> openNums) {
        List<Integer> opens = new ArrayList();
        opens.add((Integer) openNums.get(0));
        opens.add((Integer) openNums.get(1));

        return super.getWin(bets, opens);
    }
}
