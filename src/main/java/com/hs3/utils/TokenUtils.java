package com.hs3.utils;

import com.hs3.entity.users.UserToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TokenUtils {
    private static final String[] yArray = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private static final String[] xArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static String[] getRandomKey(int length) {
        String[] returnArray = new String[length];
        Set<String> set = new HashSet();
        do {
            int oldSize = set.size();
            String k = yArray[NumUtils.getRandom(0, 9)] + xArray[NumUtils.getRandom(0, 9)];
            set.add(k);
            if (set.size() != oldSize) {
                returnArray[oldSize] = k;
            }
        } while (


                set.size() < length);
        return returnArray;
    }

    public static List<Map<String, String>> toTable(List<UserToken> m) {
        List<Map<String, String>> res = new ArrayList();
        for (String y : yArray) {
            Map<String, String> line = new HashMap();
            line.put("k", y);

            res.add(line);
            for (String x : xArray) {
                String k = y + x;
                for (UserToken token : m) {
                    if (token.getTokenKey().equals(k)) {
                        line.put(x, token.getTokenValue());
                        break;
                    }
                }
            }
        }
        return res;
    }
}
