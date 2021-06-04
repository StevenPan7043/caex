package com.contract.app.controller;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.service.api.GdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GdController {

    @Autowired
    private GdService gdService;

    /**
     * 跟单交易
     *
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/gd/buyGd")
    @ResponseBody
    public RestResponse buyGd(String token, Integer id) {
        if (id == null) {
            return GetRest.getFail("请选择跟单金额");
        }
        RestResponse result = gdService.buyGd(token, id);
        return result;
    }
}
