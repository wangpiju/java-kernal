package com.hs3.utils.auth.google;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TOTP {
    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey =
                    new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();


        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = bArray[(i + 1)];
        }
        return ret;
    }

    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public static String generateTOTP(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    public static String generateTOTP256(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    public static String generateTOTP512(String key, String time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }

    public static String generateTOTP(String key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;
        while (time.length() < 16) {
            time = "0" + time;
        }
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);


        int offset = hash[(hash.length - 1)] & 0xF;

        int binary =
                (hash[offset] & 0x7F) << 24 |
                        (hash[(offset + 1)] & 0xFF) << 16 |
                        (hash[(offset + 2)] & 0xFF) << 8 |
                        hash[(offset + 3)] & 0xFF;

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }
}
