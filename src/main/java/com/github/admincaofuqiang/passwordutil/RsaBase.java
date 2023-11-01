package com.github.admincaofuqiang.passwordutil;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class RsaBase extends BaseClass {
    public static String sign(String data, String privateKey) throws Exception {
        byte[] bytes = BaseClass.decryptBase64(privateKey);
        PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey key = factory.generatePrivate(pkcs);
        Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
        signature.initSign(key);
        signature.update(data.getBytes());
        return BaseClass.encryptBase64(signature.sign());
    }

    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        byte[] bytes = BaseClass.decryptBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey key = factory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
        signature.initVerify(key);
        signature.update(data.getBytes());
        return signature.verify(BaseClass.decryptBase64(sign));
    }

    public static String encryptByPublicKey(String data, String key) throws Exception {
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        byte[] bytes = decryptBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicKey = factory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] resultBytes = {};
        byte[] cache = {};
        while (data.getBytes().length - offSet > 0) {
            if (data.getBytes().length - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(data.getBytes(), offSet, data.getBytes().length - offSet);
                offSet = data.getBytes().length;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return encryptBase64(resultBytes);

    }
    public static String decryptByPublicKey(String data, String key) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int MAX_DECRYPT_BLOCK = 256;
        int offSet = 0;
        byte[] bytes = decryptBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PublicKey publicKey = factory.generatePublic(keySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        for (int i = 0; decryptBase64(data).length - offSet > 0; offSet = i * MAX_DECRYPT_BLOCK) {
            byte[] cache;
            if (data.getBytes().length - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(decryptBase64(data), offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offSet, decryptBase64(data).length - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] result = out.toByteArray();
        out.close();
        return new String(result);
    }

    public static String encryptByPrivateKey(String data, String key) throws Exception {
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        byte[] bytes = decryptBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] resultBytes = {};
        byte[] cache = {};
        while (data.getBytes().length - offSet > 0) {
            if (data.getBytes().length - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(data.getBytes(), offSet, data.getBytes().length - offSet);
                offSet = data.getBytes().length;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return encryptBase64(resultBytes);

    }

    public static String decryptByPrivateKey(String data, String key) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int MAX_DECRYPT_BLOCK = 256;
        int offSet = 0;
        byte[] bytes = decryptBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        PrivateKey privateKey = factory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        for (int i = 0; decryptBase64(data).length - offSet > 0; offSet = i * MAX_DECRYPT_BLOCK) {
            byte[] cache;
            if (data.getBytes().length - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(decryptBase64(data), offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offSet, decryptBase64(data).length - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] result = out.toByteArray();
        out.close();
        return new String(result);

    }




}
