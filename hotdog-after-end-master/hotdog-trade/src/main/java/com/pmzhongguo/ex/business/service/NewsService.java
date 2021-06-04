package com.pmzhongguo.ex.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HttpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>File：NewsService.java</p>
 * <p>Description: ${description}</p>
 * <p>Copyright: Copyright (c) 2019/3/8 10:45</p>
 * <p>Company: zzex</p>
 *
 * @author yukai
 * @Version: 1.0
 */
@Service
public class NewsService extends BaseServiceSupport
{

    /**
     * 快讯key前缀
     */
    private static final String NEWS_KEY_PREFIX = "news:prefix";

    // 快讯缓存30s
    private static final int NEWS_KEY_EXIPRE = 30;

    /**
     * 获取快讯数据
     *
     * @return
     */
    public Map<String, Object> getNewsList(Integer pageSize, Integer page, String flag)
    {
        String key = new StringBuffer(NEWS_KEY_PREFIX).append("|").append(pageSize).append(page).toString();
        Object data = JedisUtil.getInstance().get(key, false);
        if (data != null) return (Map<String, Object>) data;
        // 通过REST API 访问拉取快讯数据
        String url = "http://api.coindog.com/live/list";
        Map<String, String> params = Maps.newHashMap();
        params.put("limit", String.valueOf(pageSize));
        params.put("id", String.valueOf((page - 1) * pageSize));
        params.put("flag", flag);
        String result = null;
        try
        {
            result = HttpUtils.get(url, params, "UTF-8");
        }catch (Exception e)
        {
            logger.error("jinse.com 访问异常!" + e.getLocalizedMessage());
            return Maps.newHashMap();
        }
        if (StringUtils.isBlank(result)) throw new BusinessException(Resp.FAIL, "error", "connection timed out");
        result = decodeUnicode(result);

        JSONObject list = JSON.parseObject(result).getJSONArray("list").getJSONObject(0);
        String date = (String) list.get("date");
        // 列表数据
        JSONArray lives = list.getJSONArray("lives");
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("date", date);
        ret.put("lives", lives);
        //将数据进行缓存
        JedisUtil.getInstance().set(key, ret, false);
        JedisUtil.getInstance().expire(key, NEWS_KEY_EXIPRE);
        return ret;
    }

    /**
     * 将Unicode转义为中文
     *
     * @param uniCode
     * @return
     */
    private static String decodeUnicode(String uniCode)
    {
        String[] asciis = uniCode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return uniCode;
        }
        return nativeValue;
    }
}
