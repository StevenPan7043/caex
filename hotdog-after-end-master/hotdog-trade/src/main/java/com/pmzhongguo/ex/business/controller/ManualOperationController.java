package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.ManualOperationCurrencyEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.HttpUtil;
import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pmzhongguo.ex.core.web.TopController.$;
import static com.pmzhongguo.ex.core.web.TopController.$attr;

/**
 * 后台管理 - 钱包手动扫描归集
 *
 * @author zn
 *
 */

@ApiIgnore
@Controller
@RequestMapping("backstage/manual")
public class ManualOperationController extends TopController {
    //请求url
    private final String FUNCTION_URL = "http://192.168.0.69:8060";
    //项目方名称
    private final String ZZEX = "ZZEX";



    /**
     * 手动回写界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toRecharge", method = RequestMethod.GET)
    public String toManualRecharge(HttpServletRequest request, HttpServletResponse response) {
        List<String> currencys = new ArrayList();
        for (ManualOperationCurrencyEnum x:ManualOperationCurrencyEnum.values()) {
            if (1 == x.getRecharge()){
                currencys.add(x.getCurrency());
            }
        }
        $attr("currencys", currencys);
        return "business/manualoperation/manual_recharge";
    }

    /**
     *
     * 手动回写
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public Resp ManualRecharge(HttpServletRequest request, HttpServletResponse response){
        String currency = $("currency");
        String info = $("info");
        String url = FUNCTION_URL + "/" + currency + "/manual/recharge?info=" + info;
        if ("eth".equals(currency)){
            String result = "";
            try {
                 result = HttpUtil.get(url);
            }catch (Exception e){
                logger.warn("手动回写失败，交易hash或者高度:" + info + "  错误" + e.getMessage());
                return new Resp(Resp.FAIL,"failed");
            }
            if (!StringUtils.isNullOrEmpty(result)) {
                result = result.replaceAll("\r|\n","");
                return new Resp(Resp.SUCCESS,result);
            }
            return new Resp(Resp.FAIL, "failed");
        }else {
            // todo --------------------------

            }
        return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
    }


    /**
     * 手动归集界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toGuiji", method = RequestMethod.GET)
    public String toManualGuiji(HttpServletRequest request, HttpServletResponse response) {
        List<String> currencys = new ArrayList();
        for (ManualOperationCurrencyEnum x:ManualOperationCurrencyEnum.values()) {
            if (1 == x.getGuiji()){
                currencys.add(x.getCurrency());
            }
        }
        $attr("currencys", currencys);
        return "business/manualoperation/manual_guiji";
    }


    /**
     *
     * 手动归集
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(value = "/guiji", method = RequestMethod.POST)
    @ResponseBody
    public Resp ManualGuiji(HttpServletRequest request, HttpServletResponse response){
        String currency = $("currency");
        String info = $("info");
        String url = FUNCTION_URL + "/" + currency + "/guiji/" + info;
        if ("eth".equals(currency)){
            String result = "";
            try {
                result = HttpUtil.get(url);
            }catch (Exception e){
                logger.warn("手动归集失败，交易hash:" + info + "  错误" + e.getMessage());
                return new Resp(Resp.FAIL,"failed");
            }
        }
        return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
    }


    /**
     * 地址余额查询界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toFeeBanlance", method = RequestMethod.GET)
    public String toFeeBanlance(HttpServletRequest request, HttpServletResponse response) {
        return "business/manualoperation/fee_banlance_list";
    }

    /**
     * 查询地址余额
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/feeBanlanceList")
    @ResponseBody
    public Map listArticle(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        String currency = "eth";
        Map map = new HashMap();
        List list= new ArrayList<>();
        String banlance = "";
        try {
             banlance = HttpUtil.get(FUNCTION_URL + "/" + currency + "/getFeeBlance");
             banlance = banlance.replaceAll("\r|\n","");
        }catch (Exception e){
            logger.warn("查询地址余额失败" + e.getMessage());
            return map;
        }
        if (!StringUtils.isNullOrEmpty(banlance)) {
            Map feeBanlance = new HashMap();
            feeBanlance.put("source", ZZEX);
            feeBanlance.put("currency", "eth");
            feeBanlance.put("desc", "eth手续费地址余额");
            feeBanlance.put("banlance", banlance);
            list.add(feeBanlance);
        }
        map.put("Rows", list);
        map.put("Total", list.size());
        return map;
    }

}
