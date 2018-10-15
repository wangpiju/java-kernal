package com.hs3.lotts.open;

import com.hs3.lotts.open.impl.D3NumberBuilder;
import com.hs3.lotts.open.impl.K3NumberBuilder;
import com.hs3.lotts.open.impl.N11x5NumberBuilder;
import com.hs3.lotts.open.impl.Pk10NumberBuilder;
import com.hs3.lotts.open.impl.SscNumber5Builder;

import java.util.ArrayList;
import java.util.List;

public class NumberBuilderFactory {
    private static final List<INumberBuilder> LIST = new ArrayList<>();

    static {
        LIST.add(new N11x5NumberBuilder());
        LIST.add(new SscNumber5Builder());
        LIST.add(new D3NumberBuilder());
        LIST.add(new Pk10NumberBuilder());
        LIST.add(new K3NumberBuilder());
    }

    public static INumberBuilder getInstance(String title) {
        for (INumberBuilder sb : LIST) {
            if (sb.getTitle().equals(title)) {
                return sb;
            }
        }
        return null;
    }

    public static List<INumberBuilder> getList() {
        return LIST;
    }
}
