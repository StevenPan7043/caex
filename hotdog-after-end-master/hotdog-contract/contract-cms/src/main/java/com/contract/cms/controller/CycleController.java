package com.contract.cms.controller;


import com.contract.cms.common.MappingUtils;
import com.contract.entity.GdBuyRecord;
import com.contract.entity.GdDetail;
import com.contract.entity.GdTeamBonus;
import com.contract.entity.GdUserBonus;
import com.contract.service.cms.CycleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CycleController {



    @Autowired
    private CycleService gdService;

    /**
     * @Author lidongxu
     * @Description //购买跟单列表
     * @Date 10:30 2020/12/25
     * @Param [request, gdBuyRecord]
     * @return org.springframework.web.servlet.ModelAndView
     **/
    @RequestMapping(value = MappingUtils.showRecordList)
    public ModelAndView showCycleList(HttpServletRequest request, GdBuyRecord gdBuyRecord){
        List<GdBuyRecord> list = gdService.gdByRecordList(gdBuyRecord);
        PageInfo<GdBuyRecord> pageInfo = new PageInfo<>(list);
        ModelAndView view = new ModelAndView(MappingUtils.showRecordList);
        view.addObject("pageInfo", pageInfo);
        view.addObject("memberId",gdBuyRecord.getMemberId() == null ? null : gdBuyRecord.getMemberId());
        return view;
    }

    /**
     * 会员收益
     * @param gdUserBonus
     * @return
     */
    @RequestMapping(value = MappingUtils.showUserBonus)
    public ModelAndView showUserBonus(GdUserBonus gdUserBonus){
        List<GdUserBonus> list = gdService.gdUserBonusList(gdUserBonus);
        PageInfo<GdUserBonus> pageInfo = new PageInfo<>(list);
        ModelAndView mav = new ModelAndView(MappingUtils.showUserBonus);
        mav.addObject("pageInfo",pageInfo);
        mav.addObject("memberId",gdUserBonus.getMemberId() == null ? null : gdUserBonus.getMemberId());
        return mav;
    }

    /**
     * 团队收益
     * @param gdTeamBonus
     * @return
     */
    @RequestMapping(value = MappingUtils.showTeamBonus)
    public ModelAndView showTeamBonus(GdTeamBonus gdTeamBonus){
        List<GdTeamBonus> list = gdService.gdTeamBonusList(gdTeamBonus);
        PageInfo<GdTeamBonus> pageInfo = new PageInfo<>(list);
        ModelAndView mav = new ModelAndView(MappingUtils.showTeamBonus);
        mav.addObject("pageInfo",pageInfo);
        mav.addObject("memberId",gdTeamBonus.getMemberId() == null ? null : gdTeamBonus.getMemberId());
        return mav;
    }

    /**
     * 跟单资金明细
     * @param gdDetail
     * @return
     */
    @RequestMapping(value = MappingUtils.showDetailList)
    public ModelAndView showDetailList(GdDetail gdDetail){
        List<GdDetail> list = gdService.gdDetailList(gdDetail);
        PageInfo<GdDetail> pageInfo = new PageInfo<>(list);
        ModelAndView mav = new ModelAndView(MappingUtils.showDetailList);
        mav.addObject("pageInfo",pageInfo);
        mav.addObject("cid",gdDetail.getCid() == null ? null : gdDetail.getCid());
        mav.addObject("isout",gdDetail.getIsout() == null ? null : gdDetail.getIsout());
        return mav;
    }
}
