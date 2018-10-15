package com.hs3.utils.sec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtils {
    private static final String DES_ALGORITHM = "DES";
    private static final BASE64Encoder BASE64_ENCODER = new BASE64Encoder();
    private static final BASE64Decoder BASE64_DECODER = new BASE64Decoder();
    private static final String CHAR_SET = "utf-8";

    public static String encryption(String plainData, String secretKey)
            throws Exception {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(1, generateKey(secretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException localInvalidKeyException) {
        }
        try {
            byte[] buf = cipher.doFinal(plainData.getBytes("utf-8"));


            return BASE64_ENCODER.encodeBuffer(buf);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new Exception("BadPaddingException", e);
        }
    }

    public static String decryption(String secretData, String secretKey)
            throws Exception {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(2, generateKey(secretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("NoSuchAlgorithmException", e);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new Exception("NoSuchPaddingException", e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("InvalidKeyException", e);
        }
        try {
            byte[] buf = BASE64_DECODER.decodeBuffer(secretData);
            return new String(cipher.doFinal(buf), "utf-8");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new Exception("BadPaddingException", e);
        }
    }

    private static SecretKey generateKey(String secretKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }
}
