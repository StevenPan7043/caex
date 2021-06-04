package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.service.IpfsHashrateManager;
import com.pmzhongguo.ipfs.service.IpfsUserBonusManager;
import com.pmzhongguo.ipfs.service.IpfsUserBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("backstage/ipfs")
public class UserBonusBackgroundController extends TopController {

    @Autowired
    private IpfsUserBonusManager ipfsUserBonusManager;
    @Autowired
    private IpfsUserBonusService ipfsUserBonusService;

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "user_bonus_list", method = RequestMethod.GET)
    public String toListUserBonus(HttpServletRequest request,
                                  HttpServletResponse response) {
        return "ipfs/user_bonus_list";
    }


    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "userBonus", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map listUserBonus(HttpServletRequest request, HttpServletResponse response) {
        Map params = $params(request);
        List<IpfsUserBonus> list = ipfsUserBonusManager.findByConditionPage(params);
        BigDecimal bigDecimal = ipfsUserBonusService.sumBonus(params);
        Map map = new HashMap();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        map.put("Allbonus", bigDecimal.toPlainString());
        return map;
    }

}
