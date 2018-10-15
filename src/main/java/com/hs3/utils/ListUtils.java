package com.hs3.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListUtils {
    public static String toString(Object[] objs) {
        return toString(objs, ",");
    }

    public static String toString(Object[] objs, String split) {
        return toString(objs, split, "");
    }

    public static String toString(Object[] objs, String split, String nullValue) {
        String rel = "";
        if ((objs != null) && (objs.length > 0)) {
            Object[] arrayOfObject = objs;
            int j = objs.length;
            for (int i = 0; i < j; i++) {
                Object o = arrayOfObject[i];
                rel = rel + split + (o == null ? nullValue : o.toString());
            }
            rel = rel.substring(split.length());
        }
        return rel;
    }

    public static String toString(List objs) {
        return toString(objs, ",");
    }

    public static String toString(List objs, String split) {
        return toString(objs, split, "");
    }

    public static String toString(List objs, String split, String nullValue) {
        String rel = "";
        if ((objs != null) && (objs.size() > 0)) {
            for (Object o : objs) {
                rel = rel + split + (o == null ? nullValue : o.toString());
            }
            rel = rel.substring(split.length());
        }
        return rel;
    }

    public static List<String> toList(String line, String split) {
        List<String> rel = new ArrayList();
        String[] ns = line.split(split);
        for (String n : ns) {
            rel.add(n);
        }
        return rel;
    }

    public static List<String> toList(String line) {
        return toList(line, ",");
    }

    public static List<Integer> toIntList(String c) {
        return toIntList(c, ",");
    }

    public static List<Integer> toIntList(String c, String split) {
        List<Integer> list = new ArrayList();
        String[] l = c.split(split);
        for (String n : l) {
            try {
                int d = Integer.parseInt(n);
                list.add(Integer.valueOf(d));
            } catch (NumberFormatException localNumberFormatException) {
            }
        }
        return list;
    }

    public static int toSum(List<Integer> list) {
        int x = 0;
        for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) {
            int n = ((Integer) localIterator.next()).intValue();
            x += n;
        }
        return x;
    }

    public static boolean hasSame(List list) {
        Set slist = new HashSet();
        slist.addAll(list);
        return slist.size() != list.size();
    }

    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> lists = new ArrayList();
        int allSize = list.size();
        int formIndex = 0;
        int endIndex = size;
        if (allSize > size) {
            while (allSize > size) {
                lists.add(new ArrayList(list.subList(formIndex, endIndex)));
                formIndex += size;
                endIndex += size;
                if (formIndex >= allSize) {
                    break;
                }
                if (endIndex > allSize) {
                    endIndex = allSize;
                }
            }
        } else {
            lists.add(new ArrayList(list));
        }
        return lists;
    }
}
