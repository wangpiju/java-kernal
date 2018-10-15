package com.pays;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HmacSign {
    public static String signToBase64(String value, String key) {
        return signToBase64(value, key, "UTF-8");
    }

    public static String signToBase64(String value, String key, String charset) {
        try {
            return new String(Base64.encode(sign(value.getBytes(charset), key.getBytes(charset))));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("hmacsign fail!", e);
        }
    }

    public static String signToHex(String value, String key) {
        return signToHex(value, key, "UTF-8");
    }

    public static String signToHex(String value, String key, String charset) {
        try {
            return Hex.toHex(sign(value.getBytes(charset), key.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("hmacsign fail!", e);
        }
    }

    public static byte[] sign(byte[] data, byte[] key) {
        return sign(data, key, "MD5");
    }

    public static byte[] sign(byte[] data, byte[] key, String alg) {
        byte[] k_ipad = new byte[64];
        byte[] k_opad = new byte[64];
        Arrays.fill(k_ipad, key.length, 64, (byte) 54);
        Arrays.fill(k_opad, key.length, 64, (byte) 92);
        for (int i = 0; i < key.length; i++) {
            k_ipad[i] = ((byte) (key[i] ^ 0x36));
            k_opad[i] = ((byte) (key[i] ^ 0x5C));
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("hmacsign fail!", e);
        }
        md.update(k_ipad);
        md.update(data);
        byte[] dg = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return dg;
    }
}
