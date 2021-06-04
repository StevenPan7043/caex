package com.pmzhongguo.ex.core.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>File：HttpUtils.java</p>
 * <p>Description: ${description}</p>
 * <p>Copyright: Copyright (c) 2019/3/8 10:45</p>
 * <p>Company: zzex</p>
 *
 * @author yukai
 * @Version: 1.0
 */
public class HttpUtils
{

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static final RequestConfig defultConfig = RequestConfig.custom().setConnectionRequestTimeout(60000).setSocketTimeout(60000).setConnectTimeout(60000).build();

    //
    private HttpUtils()
    {
        super();
    }

    /**
     * 获取HttpClient
     *
     * @return HttpClient HttpClient
     */
    public static HttpClient getHttpClient()
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }

    /**
     * 执行Http的Get请求
     *
     * @param uri url地址
     * @return String Http请求返回的内容
     */
    public static String get(RequestConfig config, String uri)
    {
        return get(config, uri, null);
    }

    /**
     * 发送get请求
     *
     * @param uri 请求地址
     * @param map 请求参数
     * @return String 返回的内容
     */
    public static String get(String uri, Map<String, String> map)
    {
        return get(uri, map, null);
    }

    /**
     * 发送get请求
     *
     * @param config
     * @param uri    请求地址
     * @param map    请求参数
     * @return String 返回的内容
     */
    public static String get(RequestConfig config, String uri, Map<String, String> map)
    {
        return get(config, uri, map, null);
    }

    /**
     * GET提交，指定编码
     *
     * @param uri         请求地址
     * @param charsetName 编码
     * @return String 响应的内容
     */
    public static String get(String uri, Map<String, String> map, String charsetName)
    {
        return get(uri, map, charsetName, null);
    }

    /**
     * GET提交，指定编码
     *
     * @param config
     * @param uri         请求地址
     * @param charsetName 编码
     * @return String 响应的内容
     */
    public static String get(RequestConfig config, String uri, Map<String, String> map, String charsetName)
    {
        return get(config, uri, map, charsetName, null);
    }

    /**
     * 发送get请求
     *
     * @param uri         请求地址
     * @param map         请求参数
     * @param charsetName 请求编码
     * @param header      header请求头参数
     * @return String 返回的内容
     */
    public static String get(String uri, Map<String, String> map, String charsetName, Map<String, String> header)
    {
        return get(null, uri, map, charsetName, header);
    }

    /**
     * 发送get请求
     *
     * @param config
     * @param uri         请求地址
     * @param map         请求参数
     * @param charsetName 请求编码
     * @param header      header请求头参数
     * @return String 返回的内容
     */
    public static String get(RequestConfig config, String uri, Map<String, String> map, String charsetName, Map<String, String> header)
    {
        HttpGet httpGet = new HttpGet(uri);
        String parameter = getStringFromMap(map, charsetName);
        try
        {
            if (null != config) httpGet.setConfig(config);
            else httpGet.setConfig(defultConfig);
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + parameter));
        } catch (URISyntaxException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return executeRequest(getHttpClient(), httpGet, charsetName, header);
    }

    /**
     * Get方式提交,URL中包含查询参数, 格式：http://www.g.cn
     *
     * @param url 提交地址，例：http://www.g.cn
     * @return String 响应消息，该地址的Html源码
     */
    public static String get(String url)
    {
        return get(url, null, null, null);
    }

    /**
     * Get方式提交,URL中包含查询参数, 格式：http://www.g.cn
     *
     * @param url 提交地址，例：http://www.g.cn
     * @return String 响应消息，该地址的Html源码
     */
    public static String get(String url, RequestConfig config)
    {
        return get(config, url, null, null, null);
    }

    /**
     * 提交Http请求
     *
     * @param httpClient  HttpClient
     * @param request     HttpRequestBase
     * @param charsetName 编码名称
     * @param header      header请求头参数
     * @return String Http请求返回的内容
     */
    private static String executeRequest(HttpClient httpClient, HttpRequestBase request, String charsetName, Map<String, String> header)
    {
        if (null == httpClient || null == request)
        {
            throw new NullPointerException("httpClient或HttpRequestBase为空！");
        }
        String responseText = null;
        if (null != header)
        {
            for (Iterator<Entry<String, String>> it = header.entrySet().iterator(); it.hasNext(); )
            {
                Entry<String, String> entry = it.next();
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try
        {
            HttpResponse response = httpClient.execute(request);
            // 获取状态
            int statuscode = response.getStatusLine().getStatusCode();
            // 重定向处理
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
                    || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT))
            {
                Header redirectLocation = response.getFirstHeader("Location");
                String newUri = redirectLocation.getValue();
                if (StringUtils.isNotBlank(newUri))
                {
                    request.setURI(new URI(newUri));
                    response = httpClient.execute(request);
                }
            }
            if (StringUtils.isNotBlank(charsetName))
            {
                responseText = EntityUtils.toString(response.getEntity(), charsetName);
            } else
            {
                responseText = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        } finally
        {
            releaseConnection(request);// 释放连接
        }
        return responseText;
    }

    /**
     * 发送post请求
     *
     * @param httpClient HttpClient
     * @param uri        请求地址
     * @return String 返回内容
     */
    public static String post(HttpClient httpClient, String uri)
    {
        return post(httpClient, uri, null);
    }

    /**
     * 发送post请求
     *
     * @param httpClient HttpClient
     * @param uri        请求地址
     * @return String 返回内容
     */
    public static String post(HttpClient httpClient, RequestConfig config, String uri)
    {
        return post(httpClient, config, uri, null);
    }

    /**
     * Post方式提交,URL中包含提交参数, 格式：http://www.g.cn
     *
     * @param url    提交地址
     * @param params 提交参数集, 键/值对
     * @return String 响应消息
     */
    public static String post(String url, Map<String, String> params)
    {
        return post(getHttpClient(), url, params, null, null);
    }

    /**
     * Post方式提交,URL中包含提交参数, 格式：http://www.g.cn
     *
     * @param url    提交地址
     * @param config
     * @param params 提交参数集, 键/值对
     * @return String 响应消息
     */
    public static String post(String url, RequestConfig config, Map<String, String> params)
    {
        return post(getHttpClient(), config, url, params, null, null);
    }

    /**
     * POST提交
     *
     * @param uri 提交地址
     * @param map 提交的参数MAP
     * @return String 响应的内容
     */
    public static String post(HttpClient httpClient, String uri, Map<String, String> map)
    {
        return post(httpClient, uri, map, null);
    }

    /**
     * POST提交
     *
     * @param httpClient
     * @param config
     * @param uri
     * @param map
     * @return
     */
    public static String post(HttpClient httpClient, RequestConfig config, String uri, Map<String, String> map)
    {
        return post(httpClient, config, uri, map, null);
    }

    /**
     * POST提交
     *
     * @param httpClient
     * @param uri
     * @param map
     * @param charsetName
     * @return
     */
    public static String post(HttpClient httpClient, String uri, Map<String, String> map, String charsetName)
    {
        return post(httpClient, uri, map, charsetName, null);
    }

    /**
     * POST提交
     *
     * @param httpClient
     * @param config
     * @param uri
     * @param map
     * @param charsetName
     * @return
     */
    public static String post(HttpClient httpClient, RequestConfig config, String uri, Map<String, String> map, String charsetName)
    {
        return post(httpClient, config, uri, map, charsetName, null);
    }

    /**
     * POST 提交JSON数据
     *
     * @param httpClient
     * @param url
     * @param json
     * @return
     */
    public static String postWithJSON(HttpClient httpClient, String url, String json)
    {
        return postWithJSON(httpClient, null, url, json);
    }

    /**
     * POST 提交JSON数据
     *
     * @param httpClient
     * @param config
     * @param url
     * @param json
     * @return
     */
    public static String postWithJSON(HttpClient httpClient, RequestConfig config, String url, String json)
    {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        if (null != config) httpPost.setConfig(config);
        else httpPost.setConfig(defultConfig);
        httpPost.setEntity(entity);
        return executeRequest(httpClient, httpPost, null, null);
    }

    /**
     * @param httpClient  HttpClient
     * @param uri         请求地址
     * @param map         请求参数
     * @param charsetName 请求编码
     * @param header      访问来路
     * @return String 请求返回的内容
     */
    public static String post(HttpClient httpClient, String uri, Map<String, String> map, String charsetName, Map<String, String> header)
    {
        return post(httpClient, null, uri, map, charsetName, header);
    }

    /**
     * 发送Http的post请求
     *
     * @param httpClient  HttpClient
     * @param config      请求设置
     * @param uri         请求地址
     * @param map         请求参数
     * @param charsetName 请求编码
     * @param header      访问来路
     * @return String 请求返回的内容
     */
    public static String post(HttpClient httpClient, RequestConfig config, String uri, Map<String, String> map, String charsetName, Map<String, String> header)
    {
        HttpPost httpPost = new HttpPost(uri);
        HttpEntity httpEntity = getEntityFromMap(map, charsetName);
        if (null != httpEntity)
        {
            if (null != config) httpPost.setConfig(config);
            else httpPost.setConfig(defultConfig);
            httpPost.setEntity(httpEntity);
        }
        return executeRequest(httpClient, httpPost, charsetName, header);
    }

    /**
     * 根据Map请求参数获取HTTPEntity
     *
     * @param map         Map<String, String>格式的请求参数
     * @param charsetName 编码名称
     * @return HttpEntity HttpEntity
     */
    private static HttpEntity getEntityFromMap(Map<String, String> map, String charsetName)
    {
        List<NameValuePair> list = getListFromMap(map);
        return getEntityFromList(list, charsetName);
    }

    /**
     * 根据Map请求参数获取get请求格式的字符串
     *
     * @param map         Map<String,String>请求参数
     * @param charsetName 请求编码名称
     * @return String get请求格式的字符串
     */
    private static String getStringFromMap(Map<String, String> map, String charsetName)
    {
        String httpParameter = null;
        List<NameValuePair> list = getListFromMap(map);
        HttpEntity entity = getEntityFromList(list, charsetName);
        try
        {
            httpParameter = EntityUtils.toString(entity);
        } catch (ParseException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (IOException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return httpParameter;
    }

    /**
     * 将Map<String,String>请求参数转化为List<NameValuePair>
     *
     * @param map Map<String, String>请求参数
     * @return List<NameValuePair> List<NameValuePair>
     */
    private static List<NameValuePair> getListFromMap(Map<String, String> map)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (!MapUtils.isEmpty(map))
        {
            for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); )
            {
                Entry<String, String> entry = it.next();
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    /**
     * 将List<NameValuePair>转化为HttpEneity请求参数
     *
     * @param list        List<NameValuePair>
     * @param charsetName 请求编码
     * @return HttpEntity HttpEntity
     */
    private static HttpEntity getEntityFromList(List<NameValuePair> list, String charsetName)
    {
        UrlEncodedFormEntity entity = null;
        try
        {
            if (StringUtils.isBlank(charsetName))
            {
                charsetName = "UTF-8";
            }
            entity = new UrlEncodedFormEntity(list, charsetName);
        } catch (UnsupportedEncodingException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return entity;
    }

    /**
     * 释放请求连接
     *
     * @param request HttpRequestBase
     */
    private static void releaseConnection(HttpRequestBase request)
    {
        if (request != null)
        {
            request.releaseConnection();
        }
    }

    /**
     * 释放HttpClient对象
     *
     * @param httpClient CloseableHttpClient
     */
    public static void releaseHttpClient(CloseableHttpClient httpClient)
    {
        if (null != httpClient)
        {
            try
            {
                httpClient.close();
            } catch (IOException e)
            {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public static String postWithJSON(String path, String postContent)
    {
        URL url = null;
        try
        {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK)
            {
                // 开始获取数据
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            }

        } catch (Exception e)
        {
            log.error("<================ 发送短信请求参数：{},\t 异常原因：{}", postContent, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
