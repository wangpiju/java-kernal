package com.hs3.lotts.rule;

import com.hs3.lotts.rule.impl.Num6AddSeasonBuilder;
import com.hs3.lotts.rule.impl.Num7AddSeasonBuilder;
import com.hs3.lotts.rule.impl.Num9AddSeasonBuilder;
import com.hs3.lotts.rule.impl.Y3SeasonBuilder;
import com.hs3.lotts.rule.impl.Ymd3SeasonBuilder;
import com.hs3.lotts.rule.impl.Ymd4SeasonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SeasonBuilderFactory {
    private static final List<ISeasonBuilder> LIST = new ArrayList();

    static {
        LIST.add(new Ymd3SeasonBuilder());
        LIST.add(new Ymd4SeasonBuilder());
        LIST.add(new Num6AddSeasonBuilder());
        LIST.add(new Num7AddSeasonBuilder());
        LIST.add(new Num9AddSeasonBuilder());
        LIST.add(new Y3SeasonBuilder());
    }

    public static ISeasonBuilder getInstance(String title) {
        for (ISeasonBuilder sb : LIST) {
            if (sb.getTitle().equals(title)) {
                return sb;
            }
        }
        return null;
    }

    public static List<ISeasonBuilder> getList() {
        return LIST;
    }
}
