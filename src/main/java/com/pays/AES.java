package com.pays;

import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES {
    private static int length = 16;
    private static Random random = new Random();
    private static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_ []{}<>~`+=,.;:/?|";

    public static String generateRandomString() {
        StringBuffer randomString = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(chars.length());
            randomString.append(chars.charAt(number));
        }
        return randomString.toString();
    }

    public static String encrypt(String plainText, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("utf-8"));

            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = plainText.getBytes("utf-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - plaintextLength % blockSize;
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            cipher.init(1, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String cipherText, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("utf-8"));

            cipher.init(2, keyspec, ivspec);

            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(cipherText));
            return new String(decrypted, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
