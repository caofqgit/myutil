package com.github.admincaofuqiang.googleauth;


import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

public class GoogleAuth {
    //随机产生一组秘钥 秘钥内容随机
    public static String createSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        return secretKey.toLowerCase();
    }
    /**
     * 根据密钥获取验证码
     * 返回字符串是因为验证码有可能以 0 开头
     * @param secretKey 密钥
     * @param time      第几个 30 秒 System.currentTimeMillis() / 1000 / 30
     */
    public static String getTOTP(String secretKey, long time) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey.toUpperCase());
        String hexKey = Hex.encodeHexString(bytes);
        String hexTime = Long.toHexString(time);
        return TOTP.generateTOTP(hexKey, hexTime, "6");
    }
    /**
     * 生成 Google Authenticator 二维码所需信息
     * Google Authenticator 约定的二维码信息格式 : otpauth://totp/{issuer}:{account}?secret={secret}&issuer={issuer}
     * 参数需要 url 编码 + 号需要替换成 %20
     * @param secret  密钥 使用 createSecretKey 方法生成
     * @param account 用户账户 如: 当前认证主体的登录名称 需确保全平台唯一
     * @param issuer  服务名称 如: 项目名称 就是这个令牌生成以后给哪个应用使用的名字可以随便 不做要求
     */
    public static String createGoogleAuthQRCodeData(String secret, String account, String issuer) {
        String qrCodeData = "otpauth://totp/%s?secret=%s&issuer=%s";
        try {
            return String.format(qrCodeData, URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20"), URLEncoder.encode(secret, "UTF-8")
                    .replace("+", "%20"), URLEncoder.encode(issuer, "UTF-8").replace("+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    /** 时间前后偏移量 */
    private static final int timeExcursion = 3;
    /**
     * 校验方法
     * @param secretKey 密钥
     * @param code      用户输入的令牌密码 与用户的密文校验是否正确
     */
    public static boolean verify(String secretKey, String code) {
        long time = System.currentTimeMillis() / 1000 / 30;
        for (int i = -timeExcursion; i <= timeExcursion; i++) {
            String totp = getTOTP(secretKey, time + i);
            if (code.equals(totp)) {
                return true;
            }
        }
        return false;
    }



}
