package com.pays;

import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSA {
    public static final String SIGN_TYPE_SHA1 = "SHA1withRSA";
    public static final String SIGN_TYPE_MD5 = "MD5withRSA";
    private static final String MESSAGE_ALGORITHM = "RSA";
    private static final String CHARSET = "utf-8";

    public static RSAPrivateKey getPriKey(InputStream fis, String certPassword) {
        RSAPrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");


            char[] nPassword = null;
            if ((certPassword == null) || (certPassword.trim().equals(""))) {
                nPassword = null;
            } else {
                nPassword = certPassword.toCharArray();
            }
            ks.load(fis, nPassword);

            Enumeration enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = (String) enumas.nextElement();
            }
            privateKey = (RSAPrivateKey) ks.getKey(keyAlias, nPassword);


            return privateKey;
        } catch (Exception localException1) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception localException3) {
                }
            }
        }
        return privateKey;
    }

    public static RSAPublicKey getPubKey(InputStream inStream) {
        RSAPublicKey publicKey = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certFactory.generateCertificate(inStream);
            publicKey = (RSAPublicKey) certificate.getPublicKey();
        } catch (Exception localException) {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception localException1) {
                }
            }
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Exception localException2) {
                }
            }
        }
        return publicKey;
    }

    private static PrivateKey convertToPrivateKey(String privKeyString) {
        try {
            byte[] privKeyByte = Base64.decodeBase64(privKeyString);
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(privKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PublicKey convertToPublicKey(String pubKeyString) {
        try {
            byte[] pubKeyByte = Base64.decodeBase64(pubKeyString);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(pubKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String plainText, String publicKey) {
        try {
            PublicKey pubKey = convertToPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, pubKey);
            byte[] cipherByte = cipher.doFinal(plainText.getBytes("utf-8"));
            return Base64.encodeBase64String(cipherByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(byte[] digest, PrivateKey privKey, String signType) {
        try {
            Signature signature = Signature.getInstance(signType);
            signature.initSign(privKey);
            signature.update(digest);
            byte[] signedByte = signature.sign();
            return Base64.encodeBase64String(signedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(byte[] digest, String privateKey, String signType) {
        PrivateKey privKey = convertToPrivateKey(privateKey);
        return sign(digest, privKey, signType);
    }

    public static String sign(byte[] digest, PrivateKey privKey) {
        return sign(digest, privKey, "SHA1withRSA");
    }

    public static String sign(byte[] digest, String privateKey) {
        PrivateKey privKey = convertToPrivateKey(privateKey);
        return sign(digest, privKey, "SHA1withRSA");
    }

    public static String sign(String plainText, PrivateKey privKey, String password, String signType) {
        try {
            Signature signature = Signature.getInstance(signType);
            signature.initSign(privKey);
            String digest = plainText + password;
            signature.update(digest.getBytes("utf-8"));
            byte[] signedByte = signature.sign();
            return Base64.encodeBase64String(signedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String plainText, PrivateKey privKey, String password) {
        return sign(plainText, privKey, password, "SHA1withRSA");
    }

    public static String sign(String plainText, String privateKey, String password, String signType) {
        PrivateKey privKey = convertToPrivateKey(privateKey);
        return sign(plainText, privKey, password, signType);
    }

    public static String sign(String plainText, String privateKey, String password) {
        return sign(plainText, privateKey, password, "SHA1withRSA");
    }

    public static String decrypt(String cipherText, String privateKey) {
        try {
            PrivateKey privKey = convertToPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, privKey);
            byte[] plainByte = cipher.doFinal(Base64.decodeBase64(cipherText.getBytes("utf-8")));
            return new String(plainByte);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String plainText, String signature, PublicKey pubKey, String password, String signType) {
        try {
            Signature sig = Signature.getInstance(signType);
            sig.initVerify(pubKey);
            String digest = plainText + password;
            sig.update(digest.getBytes("utf-8"));
            return sig.verify(Base64.decodeBase64(signature));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verify(byte[] digest, byte[] signature, PublicKey pubKey, String signType) {
        try {
            Signature sig = Signature.getInstance(signType);
            sig.initVerify(pubKey);
            sig.update(digest);
            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verify(byte[] digest, byte[] signature, String publicKey, String signType) {
        PublicKey pubKey = convertToPublicKey(publicKey);
        return verify(digest, signature, pubKey, signType);
    }

    public static boolean verify(byte[] digest, byte[] signature, PublicKey pubKey) {
        return verify(digest, signature, pubKey, "SHA1withRSA");
    }

    public static boolean verify(byte[] digest, byte[] signature, String publicKey) {
        PublicKey pubKey = convertToPublicKey(publicKey);
        return verify(digest, signature, pubKey, "SHA1withRSA");
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey, String password) {
        return verify(plainText, signature, publicKey, password, "SHA1withRSA");
    }

    public static boolean verify(String plainText, String signature, String publicKey, String password, String signType) {
        PublicKey pubKey = convertToPublicKey(publicKey);
        return verify(plainText, signature, pubKey, password, signType);
    }

    public static boolean verify(String plainText, String signature, String publicKey, String password) {
        return verify(plainText, signature, publicKey, password, "SHA1withRSA");
    }

    public static String getParamsWithDecodeByPublicKey(String paramsString, String fcsPublicKey) {
        try {
            byte[] paramByteArr = encryptByPublicKey(paramsString.getBytes("utf-8"), fcsPublicKey);
            return URLEncoder.encode(encryptBASE64(paramByteArr), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByPrivateKey(String text, String key) {
        try {
            byte[] data = decryptBASE64(text);
            byte[] keyBytes = decryptBASE64(key);

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(2, privateKey);

            byte[] dataReturn = null;
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(subarray(data, i, i + 128));
                dataReturn = addAll(dataReturn, doFinal);
            }
            return new String(dataReturn, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptByPublicKey(byte[] data, String key)
            throws Exception {
        byte[] keyBytes = decryptBASE64(key);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicKey);

        byte[] dataReturn = null;
        for (int i = 0; i < data.length; i += 117) {
            byte[] doFinal = cipher.doFinal(subarray(data, i, i + 117));
            dataReturn = addAll(dataReturn, doFinal);
        }
        return dataReturn;
    }

    public static byte[] addAll(byte[] array1, byte[] array2) {
        if (array1 == null) {
            return clone(array2);
        }
        if (array2 == null) {
            return clone(array1);
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        return joinedArray;
    }

    public static byte[] clone(byte[] array) {
        if (array == null) {
            return null;
        }
        return (byte[]) array.clone();
    }

    public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }
        byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    public static String encryptBASE64(byte[] key)
            throws Exception {
        Base64 base64 = new Base64();
        byte[] b = base64.encode(key);
        String s = new String(b);
        return s;
    }

    public static byte[] decryptBASE64(String key) {
        byte[] b = key.getBytes();
        Base64 base64 = new Base64();
        return base64.decode(b);
    }
}
