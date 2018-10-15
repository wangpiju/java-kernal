package com.hs3.lotts.open;

import com.hs3.entity.lotts.LotterySeason;

import java.math.BigDecimal;
import java.util.Random;

public abstract interface INumberBuilder {
    public abstract String getTitle();

    public abstract String getRemark();

    public abstract LotterySeason create(String paramString1, String paramString2);

    public abstract LotterySeason create(String paramString1, String paramString2, Random paramRandom);

    public abstract LotterySeason create(String paramString1, String paramString2, BigDecimal paramBigDecimal1,
                                         BigDecimal paramBigDecimal2);

    public abstract LotterySeason create(String paramString1, String paramString2, BigDecimal paramBigDecimal1,
                                         BigDecimal paramBigDecimal2, Random paramRandom);
}
