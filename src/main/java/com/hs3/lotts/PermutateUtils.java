package com.hs3.lotts;

import com.hs3.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutateUtils {
    private static void swap(Object[] str, int i, int j) {
        Object temp = str[i];
        str[i] = str[j];
        str[j] = temp;
    }

    private static void arrange(Object[] str, Set<String> result, String split, int st, int len) {
        if (st == len - 1) {
            String s = "";
            for (int i = 0; i < len; i++) {
                s = s + split + str[i];
            }
            result.add(s.substring(split.length()));
        } else {
            for (int i = st; i < len; i++) {
                swap(str, st, i);
                arrange(str, result, split, st + 1, len);
                swap(str, st, i);
            }
        }
    }

    public static Set<String> getPerms(Object[] str, String split) {
        Set<String> result = new HashSet();
        arrange(str, result, split, 0, str.length);
        return result;
    }

    public static Set<String> getPerms(Object[] str) {
        return getPerms(str, "");
    }

    private static long factorial(int n) {
        return n > 1 ? n * factorial(n - 1) : 1L;
    }

    public static long arrangement(int n, int m) {
        return n >= m ? factorial(n) / factorial(n - m) : 0L;
    }

    public static long combination(int n, int m) {
        return n >= m ? factorial(n) / factorial(n - m) / factorial(m) : 0L;
    }

    public static Set<String> getCombinSelect(List<?> dataList, int n) {
        Set<String> rel = new HashSet();
        getCombinSelect(dataList, 0, new Object[n], 0, rel);
        return rel;
    }

    private static void getCombinSelect(List<?> dataList, int dataIndex, Object[] resultList, int resultIndex,
                                        Set<String> rel) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) {
            rel.add(ListUtils.toString(resultList));
            return;
        }
        for (int i = dataIndex; i < dataList.size() + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList.get(i);
            getCombinSelect(dataList, i + 1, resultList, resultIndex + 1, rel);
        }
    }

    private static void getAllNum(Set<String> result, List<List<Integer>> nums, List<Integer> cache, String split,
                                  int st, int len, boolean canSame) {
        int lenSize = ((List) nums.get(st)).size();
        for (int i = 0; i < lenSize; i++) {
            int n = ((Integer) ((List) nums.get(st)).get(i)).intValue();
            if ((canSame) || (!cache.contains(Integer.valueOf(n)))) {
                cache.set(st, Integer.valueOf(n));
                if (st == len - 1) {
                    result.add(ListUtils.toString(cache, split));
                } else {
                    getAllNum(result, nums, cache, split, st + 1, len, canSame);
                }
                cache.set(st, null);
            }
        }
    }

    public static Set<String> getAllNum(List<List<Integer>> nums, String split, boolean canSame) {
        Set<String> result = new HashSet();
        int len = nums.size();
        List<Integer> cache = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            cache.add(null);
        }
        getAllNum(result, nums, cache, split, 0, len, canSame);
        return result;
    }
}
