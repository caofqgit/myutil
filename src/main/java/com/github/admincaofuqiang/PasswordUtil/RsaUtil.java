package com.github.admincaofuqiang.PasswordUtil;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RsaUtil extends BaseContent{


    public static Map<String,String> generatorKeyPair(Integer keySize){
        Map<String, Object> init = BaseClass.init(keySize);
        String publicKey = BaseClass.getPublicKey(init);
        String privateKey = BaseClass.getPrivateKey(init);
        Map<String, String> map = new HashMap<>();
        map.put("publicKey",publicKey);
        map.put("privateKey",privateKey);
        return map;
    }
    public static boolean verifyPublicAndPrivateKey(String publicKeyStr, String privateKeyStr) {
        RSAPublicKey publicKeyToVerify = null;
        RSAPrivateKey privateKeyToVerify = null;
        try {
            publicKeyToVerify = BaseClass.loadPublicKey(publicKeyStr);
        } catch (Exception e) {
            System.err.println("加载公钥失败-公钥非法");
            return false;
        }
        try {
            privateKeyToVerify = BaseClass.loadPrivateKey(privateKeyStr);
        } catch (Exception e) {
            System.err.println("加载私钥失败-私钥非法");
            return false;
        }
        if (publicKeyToVerify == null || privateKeyToVerify == null) {
            return false;
        }
        //生成随机数字符串用于验证公钥私钥是否匹配
        Random seed = new Random();
        int randomNum = seed.nextInt(1000) + 1;
        String randomStr = String.valueOf(randomNum);

        byte[] cipher = null;
        byte[] plainText = null;
        try {
            cipher = BaseClass.encrypt(publicKeyToVerify, randomStr.getBytes());
            plainText = BaseClass.decrypt(privateKeyToVerify, cipher);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        assert plainText != null;
        String plainStr = new String(plainText);
        return plainStr.equals(randomStr);
    }
    public static String encryptByPublicKey(String content,String publicKey) throws Exception{
       return  RsaBase.encryptByPublicKey(content,publicKey);
    }
    public static String encryptByPrivateKey(String content,String privateKey) throws Exception{
        return  RsaBase.encryptByPrivateKey(content,privateKey);
    }
    public static String  decryptByPrivateKey(String data,String privateKey)throws Exception{
        return RsaBase.decryptByPrivateKey(data,privateKey);
    }
    public static String  decryptByPublicKey(String data,String publicKey)throws Exception{
        return RsaBase.decryptByPublicKey(data,publicKey);
    }
    public static String sign(String content,String privateKey)throws Exception{
       return  RsaBase.sign(content,privateKey);
    }
    public static boolean verify(String content,String sign,String publicKey)throws Exception{
        return  RsaBase.verify(content,publicKey,sign);
    }

}
