package com.hs3.lotts;

import com.hs3.entity.lotts.Bet;
import com.hs3.entity.lotts.LotterySaleTime;
import com.hs3.entity.lotts.LotterySeason;
import com.hs3.utils.ListUtils;
import com.hs3.utils.StrUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LotteryUtils {
    private static final List<String> LOTTERYS = Arrays.asList(new String[]{"cqssc", "tjssc", "bjssc", "xjssc",
            "tw5fc", "pk10", "3d", "pl3", "gd11x5", "jx11x5", "sd11x5", "ahk3"});

    public static String hashCode(Bet bet) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(bet.getAccount());
        sBuffer.append(bet.getContent());
        sBuffer.append(bet.getId());
        sBuffer.append("B^41ma@8b.sfb8UE.*");
        sBuffer.append(bet.getLotteryId());
        sBuffer.append(bet.getPlayerId());
        sBuffer.append(bet.getSeasonId());
        sBuffer.append("3g!1B?8b(sfY8UE.*");
        sBuffer.append(bet.getAmount().setScale(4));
        sBuffer.append(bet.getBetCount());
        sBuffer.append(bet.getBonusType());
        sBuffer.append("Bg&1ma@)b.sfY8UE.*");
        sBuffer.append(bet.getPrice());
        sBuffer.append(bet.getTraceId());
        sBuffer.append(bet.getUnit().setScale(4));
        sBuffer.append("3g!1Ba8b(sfY8UE.*");
        return StrUtils.MD5(sBuffer.toString());
    }

    public static LotterySeason numToSeason(LotterySaleTime sale, List<Integer> nums) {
        LotterySeason season = new LotterySeason();
        int listSize = nums.size();
        season.setLotteryId(sale.getLotteryId());
        season.setAddTime(new Date());
        season.setSeasonId(sale.getSeasonId());
        season.setOpenTime(sale.getOpenTime());
        if (listSize > 0) {
            season.setN1((Integer) nums.get(0));
        }
        if (listSize > 1) {
            season.setN2((Integer) nums.get(1));
        }
        if (listSize > 2) {
            season.setN3((Integer) nums.get(2));
        }
        if (listSize > 3) {
            season.setN4((Integer) nums.get(3));
        }
        if (listSize > 4) {
            season.setN5((Integer) nums.get(4));
        }
        if (listSize > 5) {
            season.setN6((Integer) nums.get(5));
        }
        if (listSize > 6) {
            season.setN7((Integer) nums.get(6));
        }
        if (listSize > 7) {
            season.setN8((Integer) nums.get(7));
        }
        if (listSize > 8) {
            season.setN9((Integer) nums.get(8));
        }
        if (listSize > 9) {
            season.setN10((Integer) nums.get(9));
        }
        return season;
    }

    public static int getCount(List<List<String>> lines) {
        return getCount(lines, 0, lines.size(), new HashSet());
    }

    public static String getTitle(String n) {
        if (n.equals("龙")) {
            return "-LONG";
        }
        if (n.equals("虎")) {
            return "-HU";
        }
        if (n.equals("和")) {
            return "-HE";
        }
        if (n.equals("大")) {
            return "-B";
        }
        if (n.equals("小")) {
            return "-S";
        }
        if (n.equals("单")) {
            return "-O";
        }
        if (n.equals("双")) {
            return "-E";
        }
        if (n.equals("大单")) {
            return "-dd";
        }
        if (n.equals("小单")) {
            return "-xd";
        }
        if (n.equals("大双")) {
            return "-ds";
        }
        if (n.equals("小双")) {
            return "-xs";
        }
        return n;
    }

    public static boolean getSelf(String lotteryId) {
        return !LOTTERYS.contains(lotteryId);
    }

    public static String getShortSeasonId(String seasonId) {
        int index = seasonId.indexOf("-");
        if (index > 0) {
            return seasonId.substring(index + 1);
        }
        return seasonId.substring(seasonId.length() - 3);
    }

    public static String getMaxBonus(String bonus, BigDecimal playerBonus, BigDecimal userRatio, BigDecimal groupRatio,
                                     BigDecimal playerRatio) {
        List<String> bs = ListUtils.toList(bonus, " - ");
        List<String> res = new ArrayList();
        for (String b : bs) {
            BigDecimal s = playerBonus.add(userRatio).subtract(groupRatio.subtract(playerRatio));
            s = s.multiply(new BigDecimal(b)).divide(new BigDecimal("100"), 2, 1);
            res.add(s.toString());
        }
        return ListUtils.toString(res, " - ");
    }

    public static String getMinBonus(String bonus, BigDecimal playerBonus) {
        List<String> bs = ListUtils.toList(bonus, " - ");
        List<String> res = new ArrayList();
        for (String b : bs) {
            BigDecimal s = new BigDecimal(b).multiply(playerBonus).divide(new BigDecimal("100"), 2, 1);
            res.add(s.toString());
        }
        return ListUtils.toString(res, " - ");
    }

    private static int getCount(List<List<String>> lines, int index, int allSize, HashSet<String> cache) {
        List<String> line = (List) lines.get(index);
        int size = line.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            String n = (String) line.get(i);
            if (!cache.contains(n)) {
                if (index + 1 < allSize) {
                    cache.add(n);
                    count += getCount(lines, index + 1, allSize, cache);
                    cache.remove(n);
                } else {
                    count++;
                }
            }
        }
        return count;
    }

    public static List<Integer> toIntListByLength(String str, int len) {
        List<Integer> line = new ArrayList();
        if (!str.equals("-")) {
            for (int i = 0; i < str.length(); i += len) {
                int n = Integer.parseInt(str.substring(i, i + len));
                line.add(Integer.valueOf(n));
            }
        }
        return line;
    }

    public static List<String> toListByLength(String str, Integer len) {
        List<String> line = new ArrayList();
        if (!str.equals("-")) {
            for (int i = 0; i < str.length(); i += len.intValue()) {
                line.add(str.substring(i, i + len.intValue()));
            }
        }
        return line;
    }

    public static int getSscIndex(String title) {
        if ("万".equals(title)) {
            return 0;
        }
        if ("千".equals(title)) {
            return 1;
        }
        if ("百".equals(title)) {
            return 2;
        }
        if ("十".equals(title)) {
            return 3;
        }
        if ("个".equals(title)) {
            return 4;
        }
        return -1;
    }

    public static List<Integer> getSscIndexs(String title) {
        List<Integer> result = new ArrayList();
        for (int i = 0; i < title.length(); i++) {
            String n = title.substring(i, i + 1);
            int index = getSscIndex(n);
            if ((index >= 0) && (!result.contains(Integer.valueOf(index)))) {
                result.add(Integer.valueOf(index));
            }
        }
        return result;
    }

    public static int mulAll(List<Integer> nums, int len) {
        int allCount = 0;
        if (nums.size() < len) {
            return allCount;
        }
        List<Integer> indexs = new ArrayList();
        for (int n = 0; n < nums.size(); n++) {
            indexs.add(Integer.valueOf(n));
        }
        Set<String> list = PermutateUtils.getCombinSelect(indexs, len);
        for (String n : list) {
            List<Integer> ns = ListUtils.toIntList(n);
            int count = 1;
            for (Integer nnn : ns) {
                count *= ((Integer) nums.get(nnn.intValue())).intValue();
                if (count == 0) {
                    break;
                }
            }
            allCount += count;
        }
        return allCount;
    }

    public static int getCombin(int num, int len) {
        if (num < len) {
            return 0;
        }
        int nums = 1;
        int lens = 1;
        for (int i = 0; i < len; i++) {
            nums *= (num - i);
            lens *= (len - i);
        }
        return nums / lens;
    }

    public static int getArrangement(int num, int len) {
        if (num < len) {
            return 0;
        }
        int nums = 1;
        for (int i = 0; i < len; i++) {
            nums *= (num - i);
        }
        return nums;
    }

    public static Map<Integer, Integer> getNumInfo(int... nums) {
        Map<Integer, Integer> rel = new HashMap();
        int[] arrayOfInt = nums;
        int j = nums.length;
        for (int i = 0; i < j; i++) {
            int n = arrayOfInt[i];
            int m = 1;
            if (rel.containsKey(Integer.valueOf(n))) {
                m = ((Integer) rel.get(Integer.valueOf(n))).intValue();
                m++;
            }
            rel.put(Integer.valueOf(n), Integer.valueOf(m));
        }
        return rel;
    }

    public static Map<Integer, Integer> getNumInfo(List<Integer> nums) {
        Map<Integer, Integer> rel = new HashMap();
        for (Iterator localIterator = nums.iterator(); localIterator.hasNext(); ) {
            int n = ((Integer) localIterator.next()).intValue();
            int m = 1;
            if (rel.containsKey(Integer.valueOf(n))) {
                m = ((Integer) rel.get(Integer.valueOf(n))).intValue();
                m++;
            }
            rel.put(Integer.valueOf(n), Integer.valueOf(m));
        }
        return rel;
    }

    public static boolean listHasNext(List<Integer> list, int size, int min, int max) {
        for (int i = size - 1; i >= 0; i--) {
            int nNum = ((Integer) list.get(i)).intValue();
            if (nNum == max) {
                if (i == 0) {
                    break;
                }
                list.set(i, Integer.valueOf(min));
            } else {
                list.set(i, Integer.valueOf(nNum + 1));
                return true;
            }
        }
        return false;
    }

    public static String toString(String split, String otherNum, List<Integer> list, int... index) {
        String re = "";
        for (int i = 0; i < list.size(); i++) {
            boolean has = false;
            for (int j = 0; j < index.length; j++) {
                if (index[j] == i) {
                    has = true;
                    break;
                }
            }
            if (has) {
                re = re + split + list.get(i);
            } else {
                re = re + split + otherNum;
            }
        }
        return re.substring(split.length());
    }
}
