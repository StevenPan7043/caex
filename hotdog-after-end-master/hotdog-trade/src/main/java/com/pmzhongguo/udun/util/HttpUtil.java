package com.pmzhongguo.udun.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpUtil {

    private static HttpUtil instance;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public String get(String url) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    public String post(String url, Map<String, Object> param) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(url, param, String.class);
        return result;
    }

    private HttpHeaders header() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpEntity<MultiValueMap<String, Object>> httpEntity(Map<String, Object> param) {
        //组装参数
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for (String key : param.keySet()) {
            map.add(key, param.get(key));
        }
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, header());
        return request;
    }
}
