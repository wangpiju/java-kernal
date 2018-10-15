package com.hs3.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.StringCharacterIterator;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class StrUtils {
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final String passKey = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String specialChars = "<>";
    private static final String REGX_EMAIL = "^[\\w-]+@[\\w-]{2,}\\.\\w+$";
    private static final String REGX_CHINA = "^[一-龥]+$";
    private static final String REGX_ENGLISH = "^[a-zA-Z]+$";
    private static final String REGX_NUM = "^\\d+$";
    private static final String REGX_NAME = "^[\\·A-Za-z一-龥-]+$";
    private static final String REGX_ADDRESS = "^[\\d\\.A-Za-z一-龥-]+$";
    private static final String REGX_ACCOUNT = "^[a-zA-Z]\\w*$";

    public static boolean inLength(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        if ((text.length() < min) || (text.length() > max)) {
            return false;
        }
        return true;
    }

    public static boolean isRegx(String text, String regx) {
        return Pattern.matches(regx, text);
    }

    public static boolean isLengthAndRegx(String text, String regx, int min, int max) {
        if (inLength(text, min, max)) {
            return isRegx(text, regx);
        }
        return false;
    }

    public static boolean isEmail(String text, int min, int max) {
        return isLengthAndRegx(text, "^[\\w-]+@[\\w-]{2,}\\.\\w+$", min, max);
    }

    public static boolean isChinese(String text, int min, int max) {
        return isLengthAndRegx(text, "^[一-龥]+$", min, max);
    }

    public static boolean isEnglish(String text, int min, int max) {
        return isLengthAndRegx(text, "^[a-zA-Z]+$", min, max);
    }

    public static boolean isNumber(String text, int min, int max) {
        return isLengthAndRegx(text, "^\\d+$", min, max);
    }

    public static boolean isName(String text, int min, int max) {
        return isLengthAndRegx(text, "^[\\·A-Za-z一-龥-]+$", min, max);
    }

    public static boolean isAddress(String text, int min, int max) {
        return isLengthAndRegx(text, "^[\\d\\.A-Za-z一-龥-]+$", min, max);
    }

    public static boolean isAccount(String text, int min, int max) {
        return isLengthAndRegx(text, "^[a-zA-Z]\\w*$", min, max);
    }

    public static String getGuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    public static String toUpperCaseFirst(String s) {
        String first = s.substring(0, 1).toUpperCase();
        String rest = s.substring(1).toLowerCase();
        return first + rest;
    }

    public static boolean hasEmpty(Object... args) {
        Object[] arrayOfObject = args;
        int j = args.length;
        for (int i = 0; i < j; i++) {
            Object arg = arrayOfObject[i];
            if ((arg == null) || (arg.toString().trim().equals(""))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNotEmpty(Object... args) {
        Object[] arrayOfObject = args;
        int j = args.length;
        for (int i = 0; i < j; i++) {
            Object arg = arrayOfObject[i];
            if ((arg != null) && (!arg.toString().trim().equals(""))) {
                return true;
            }
        }
        return false;
    }

    public static String MD5(String source) {
        if (source == null) {
            return null;
        }
        return MD5(source.getBytes());
    }

    public static String MD5(byte[] source) {
        return DigestUtils.md5Hex(source);
    }

    public static String MD5(String source, String charset) {
        try {
            return DigestUtils.md5Hex(source.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object jsonModel) {
        return gson.toJson(jsonModel);
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static String Encode(String key) {
        StringBuilder sb = new StringBuilder();
        String k = key;
        int count = k.length();
        while (count > 0) {
            int n = count >= 3 ? count - 3 : 0;
            sb.append(k.substring(n));

            k = k.substring(0, n);
            count = k.length();
        }
        return sb.toString();
    }

    public static String Decode(String key) {
        StringBuilder sb = new StringBuilder();
        String k = key;
        int count = k.length();
        while (count > 0) {
            int n = count > 3 ? 3 : count;
            sb.insert(0, k.substring(0, n));

            k = k.substring(n);
            count = k.length();
        }
        return sb.toString();
    }

    public static String parseLength(Object str, int length, String fix) {
        int n = str.toString().length();
        String rel = str.toString();
        if (n < length) {
            for (int i = 0; i < length - n; i++) {
                rel = fix + rel;
            }
        }
        return rel;
    }

    public static String parseLength(Object str, int length) {
        return parseLength(str, length, "0");
    }

    public static String getSpecialChars() {
        return "<>";
    }

    public static boolean checkHadSpecialChar(String... args) {
        String[] arrayOfString = args;
        int j = args.length;
        for (int i = 0; i < j; i++) {
            String str = arrayOfString[i];
            if (checkHadSpecialChar(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkHadSpecialChar(String text) {
        if (hasEmpty(new Object[]{text})) {
            return false;
        }
        for (char sc : "<>".toCharArray()) {
            if (text.indexOf(sc) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static String htmlEncode(String aText) {
        if (hasEmpty(new Object[]{aText})) {
            return aText;
        }
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character = iterator.current();
        while (character != 65535) {
            if (character == '<') {
                result.append("&lt;");
            } else if (character == '>') {
                result.append("&gt;");
            } else if (character == '&') {
                result.append("&amp;");
            } else if (character == '"') {
                result.append("&quot;");
            } else {
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = NumUtils.getRandom(0, 61);
            if(i == 0)
                sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz789abcdefp".charAt(number));
            else
                sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".charAt(number));
        }
        return sb.toString();
    }

    public static String formBase64(String str) {
        try {
            return new String(Base64.decodeBase64(str), "utf-8");
        } catch (Exception localException) {
        }
        return null;
    }

    public static String toBase64(String str) {
        try {
            return Base64.encodeBase64String(str.getBytes("utf-8"));
        } catch (Exception localException) {
        }
        return null;
    }
}
