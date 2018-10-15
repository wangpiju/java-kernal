package com.hs3.utils.auth.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

public class GoogleAuthenticatorUtils {
    private static final int timeExcursion = 1;

    public static String createSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        return secretKey.toLowerCase();
    }

    public static boolean verify(String secretKey, String code) {
        long time = System.currentTimeMillis() / 1000L / 30L;
        for (int i = -1; i <= 1; i++) {
            String totp = getTOTP(secretKey, time + i);
            if (code.equals(totp)) {
                return true;
            }
        }
        return false;
    }

    public static String getTOTP(String secretKey, long time) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey.toUpperCase());
        String hexKey = Hex.encodeHexString(bytes);
        String hexTime = Long.toHexString(time);
        return TOTP.generateTOTP(hexKey, hexTime, "6");
    }

    public static String createGoogleAuthQRCodeData(String secret, String account, String issuer) {
        String qrCodeData = "otpauth://totp/%s?secret=%s&issuer=%s";
        try {
            return String.format(qrCodeData, new Object[]{URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20"), URLEncoder.encode(secret, "UTF-8")
                    .replace("+", "%20"), URLEncoder.encode(issuer, "UTF-8").replace("+", "%20")});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
