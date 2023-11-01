package com.github.admincaofuqiang.httputil;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUrlUtil {
    public static String doGet(String url) {
        URL requestUrl = null;
        if (StringUtils.isBlank(url))
            throw new RuntimeException("reuqest url is null");
        try {
            requestUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("reuqest url format is error");
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
        } catch (IOException e) {
            throw new RuntimeException("request error");
        }
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                bufferedReader = new BufferedReader(inputStreamReader);
                return bufferedReader.readLine();
            } else {
                String responseMessage = httpURLConnection.getResponseMessage();
                Map<String, Object> map = new HashMap<>();
                map.put("code", responseCode);
                map.put("message", responseMessage);
                return JSON.toJSONString(map);
            }
        } catch (IOException e) {
            throw new RuntimeException("request error");
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException ignored) {

            }


        }
    }

    public static String doPostJson(String url, String param, Map<String, String> header) {
        // 结果值
        StringBuffer rest = new StringBuffer();
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader br = null;

        try {
            URL restUrl = new URL(url);
            conn = (HttpURLConnection) restUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (ObjectUtils.isNotEmpty(header)) {
                Set<String> key = header.keySet();
                for (String s : key) {
                    conn.setRequestProperty(s, header.get(s));
                }
            }
            conn.connect();
            out = conn.getOutputStream();
            out.write(param.getBytes());
            out.flush();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while (null != (line = br.readLine())) {
                rest.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return rest.toString();
    }
}
