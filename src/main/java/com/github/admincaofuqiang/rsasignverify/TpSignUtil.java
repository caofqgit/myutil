package com.github.admincaofuqiang.rsasignverify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.admincaofuqiang.passwordutil.RsaBase;
import com.github.admincaofuqiang.passwordutil.RsaUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TpSignUtil {
    private static final Logger logger = Logger.getLogger(TpSignUtil.class.getName());

    private final static String SIGN = "sign";
    private final static String TIMESTAMP = "timestamp";

    public static boolean verifySign(Map<String, Object> params, String key, Boolean expireCheck) {
        Map<String, String> stringMap = toStringMap(params);
        String sign = stringMap.get(SIGN);
        logger.info("验签签名为:" + sign);
        String timestamp = stringMap.get(TIMESTAMP);
        logger.info("验签时间戳为:" + timestamp);
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(timestamp)) {
            throw new RuntimeException("签名或者时间戳不能为空");
        }
        if (expireCheck) {
            long currentTimestamp = System.currentTimeMillis() / 1000;
            long userTimestamp = Long.valueOf(timestamp) / 1000;
            boolean error = Math.abs(currentTimestamp - userTimestamp) > 1;
            if (error) {
                throw new RuntimeException("签名过期");
            }
        }
        String content = mapToString(stringMap);
        logger.info("验签内容:" + content);
        try {
            return RsaBase.verify(content, key, sign);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
    }


    public static String sign(Map<String, Object> requestParams, String key) {
        Map<String, String> params = new HashMap<>();
        if (params == null) {
            throw new RuntimeException("加签内容不能为空");
        } else {
            params = toStringMap(requestParams);
            List<String> keys = (List) params.keySet().stream().collect(Collectors.toList());
            if (keys.size() == 0) {
                throw new RuntimeException("请传递请求参数即待加签全部内容");
            } else {
                String timestamp = params.get(TIMESTAMP);
                if (StringUtils.isBlank(timestamp))
                    throw new RuntimeException("时间戳不能为空");
                keys.remove(SIGN);
                keys.remove(TIMESTAMP);
                Collections.sort(keys);
                StringBuilder sb = new StringBuilder();
                Iterator var4 = keys.iterator();
                String sign;
                while (var4.hasNext()) {
                    sign = String.valueOf(var4.next());
                    sb.append("&").append(sign).append("=").append(params.get(sign));
                }
                sb.append("&").append(TIMESTAMP).append("=").append(timestamp);
                String content = sb.toString().substring(1);
                logger.info("^_^待加签参数为：" + content);
                try {
                    String result = RsaBase.sign(content, key);
                    logger.info("^_^加签结果为:" + result);
                    return result;
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    throw new RuntimeException("加签失败");
                }
            }
        }
    }

    private static Map<String, String> toStringMap(Map<String, Object> map) {
        HashMap<String, String> aMap = new HashMap<>();
        if (MapUtils.isEmpty(map)) {
            return aMap;
        }
        for (String s : map.keySet()) {
            if (map.get(s) == null) {
                aMap.put(s, "");
            } else if (map.get(s) instanceof String) {
                aMap.put(s, map.get(s).toString());
            } else {
                aMap.put(s, JSONObject.toJSONString(map.get(s)));
            }
        }
        return aMap;
    }

    public static String mapToString(Map<String, String> params) {
        String content = "";
        if (params == null) {
            throw new RuntimeException("加签内容不能为空");
        } else {
            List<String> keys = (List) params.keySet().stream().collect(Collectors.toList());
            if (keys.size() == 0) {
                throw new RuntimeException("请传递请求参数即待加签全部内容");
            } else {
                String timestamp = params.get("timestamp");
                if (StringUtils.isBlank(timestamp))
                    throw new RuntimeException("时间戳不能为空");
                keys.remove("sign");
                keys.remove("timestamp");
                Collections.sort(keys);
                StringBuilder sb = new StringBuilder();
                Iterator var4 = keys.iterator();
                String sign;
                while (var4.hasNext()) {
                    sign = String.valueOf(var4.next());
                    sb.append("&").append(sign).append("=").append(String.valueOf(params.get(sign)));
                }
                sb.append("&").append("timestamp").append("=").append(timestamp);
                content = sb.toString().substring(1);
            }
            return content;
        }
    }
}
