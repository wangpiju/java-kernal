package com.hs3.entity.lotts;

import com.hs3.utils.NumUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.util.List;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public abstract class BetInProbability {
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public static BetInProbability getProbability(List<? extends BetInProbability> probabilityList) {
        int index = getProbabilityIndex(probabilityList);
        if (index >= 0) {
            return (BetInProbability) probabilityList.get(index);
        }
        return null;
    }

    public static int getProbabilityIndex(List<? extends BetInProbability> probabilityList) {
        int rand = NumUtils.getRandom(1, 10000);
        BigDecimal start = BigDecimal.ZERO;
        for (int i = 0; i < probabilityList.size(); i++) {
            BetInProbability betInProbability = (BetInProbability) probabilityList.get(i);
            start = start.add(betInProbability.getProbability().multiply(HUNDRED));
            if (rand <= start.intValue()) {
                return i;
            }
        }
        return -1;
    }

    protected abstract BigDecimal getProbability();
}
