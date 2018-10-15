package com.pays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DES {
    private static final String CHARSET = "UTF-8";

    public static String encrypt(byte[] textBytes, byte[] keyBytes, byte[] ivBytes) {
        try {
            SecretKey securekey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(keyBytes));
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            cipher.init(1, securekey, iv);

            byte[] retByte = cipher.doFinal(textBytes);
            return new BASE64Encoder().encode(retByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String text, String key, String iv) {
        try {
            return encrypt(text.getBytes("UTF-8"), key.getBytes("UTF-8"), iv.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByDecode(String text, String key, String iv) {
        try {
            return encrypt(text.getBytes("UTF-8"), new BASE64Decoder().decodeBuffer(key), new BASE64Decoder().decodeBuffer(iv));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] textBytes, byte[] keyBytes, byte[] ivBytes) {
        try {
            SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(keyBytes));
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, secretKey, iv);

            return cipher.doFinal(textBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String text, String key, String iv) {
        try {
            return new String(decrypt(text.getBytes("UTF-8"), key.getBytes("UTF-8"), iv.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByDecode(String text, String key, String iv) {
        try {
            return new String(decrypt(new BASE64Decoder().decodeBuffer(text), new BASE64Decoder().decodeBuffer(key), new BASE64Decoder().decodeBuffer(iv)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
