package com.hs3.utils.sec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Des {
    private static byte[] IV = {18, 52, 86, 120, -112, -85, -51, -17};

    public static String encrypt(byte[] src, String key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(IV);

            cipher.init(1, securekey, iv);


            byte[] retByte = cipher.doFinal(src);
            return new BASE64Encoder().encode(retByte);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String src, String key) {
        try {
            byte[] bytesrc = src.getBytes("UTF-8");
            return encrypt(bytesrc, key);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String src, String key) {
        byte[] rel = decryptToByte(src, key);
        if (rel != null) {
            return new String(rel);
        }
        return null;
    }

    public static byte[] decryptToByte(String src, String key) {
        try {
            byte[] bytesrc = new BASE64Decoder().decodeBuffer(src);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(IV);

            cipher.init(2, secretKey, iv);

            return cipher.doFinal(bytesrc);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt_des_cbc(byte[] src, byte[] key)
            throws Exception {
        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
        SecretKey secretKey = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(1, secretKey, iv);
        byte[] encryptedData = cipher.doFinal(src);
        return Hex.encodeHexString(encryptedData);
    }

    public static String decrypt_des_cbc(byte[] src, byte[] key)
            throws Exception {
        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
        SecretKey secretKey = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, secretKey, iv);
        byte[] decryptPlainText = cipher.doFinal(src);
        return new String(decryptPlainText);
    }
}
