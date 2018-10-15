package com.hs3.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdBuilder {
    private static final String chars = "abcdefghkmnptuvwxz";
    private static final String nums = "2345678";

    public static String createRom(int numLen, int charLen) {
        String s = "";
        for (int i = 0; i < numLen; i++) {
            s = s + "2345678".charAt(NumUtils.getRandom(0, "2345678".length() - 1));
        }
        for (int i = 0; i < charLen; i++) {
            s = s + "abcdefghkmnptuvwxz".charAt(NumUtils.getRandom(0, "abcdefghkmnptuvwxz".length() - 1));
        }
        return s;
    }

    public static String getId(String prefix, int randomNum) {
        StringBuilder sb = new StringBuilder(prefix);
        String date = DateUtils.format(new Date(), "yyMMddHHmmss");

        sb.append(date);
        for (int i = 0; i < randomNum; i++) {
            sb.append(NumUtils.getRandom(0, 9));
        }
        return sb.toString();
    }

    public static String CreateId(String lotteryId, String sign) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(sign.toUpperCase());
        sBuffer.append(lotteryId.toUpperCase().substring(0, 2));
        sBuffer.append(new SimpleDateFormat("MMdd").format(new Date()));
        sBuffer.append(StrUtils.getGuid().substring(0, 16));
        String idString = sBuffer.toString();
        return idString;
    }
}
