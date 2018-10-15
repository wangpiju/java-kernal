package com.pays.qtong;

import com.pays.RSA;

import java.io.PrintStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

public class KeyTools {
    public static void main(String[] args)
            throws Exception {
        RSAPrivateKey priKey = RSA.getPriKey(KeyTools.class.getResourceAsStream("1005598.pfx"), "11111111");
        RSAPublicKey pubKey = RSA.getPubKey(KeyTools.class.getResourceAsStream("1006168.cer"));

        String pri = Base64.encodeBase64String(priKey.getEncoded());
        String pub = Base64.encodeBase64String(pubKey.getEncoded());
        System.out.println("私钥：" + pri);
        System.out.println("公钥：" + pub);
    }
}
