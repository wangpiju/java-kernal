package com.pays;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HASH {
    public static String SHA256(String strText) {
        return SHA(strText, "SHA-256");
    }

    public static String SHA512(String strText) {
        return SHA(strText, "SHA-512");
    }

    public static String SHA1(String strText) {
        return SHA(strText, "SHA-1");
    }

    private static String SHA(String strText, String strType) {
        String strResult = null;
        if ((strText != null) && (strText.length() > 0)) {
            try {
                MessageDigest messageDigest =
                        MessageDigest.getInstance(strType);

                messageDigest.update(strText.getBytes());

                byte[] byteBuffer = messageDigest.digest();


                StringBuffer strHexString = new StringBuffer();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xFF & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
