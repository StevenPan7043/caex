package com.contract.cms.controller;

import java.util.List;

import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.entity.SysBanner;
import com.contract.entity.SysNotice;
import com.contract.service.cms.BannerService;
import com.contract.service.cms.NewService;


@Controller
public class BannerController {

	@Autowired
	private BannerService bannerService;
	@Autowired
	private NewService newService;
	
	@RequestMapping(value = MappingUtils.showBanners)
	public ModelAndView showBanners() {
		List<SysBanner> list=bannerService.queryBanners();
		ModelAndView view=new ModelAndView(MappingUtils.showBanners);
		view.addObject("list", list);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.deleteBanner)
	@ResponseBody
	public RestResponse deleteBanner(Integer id) {
		RestResponse result=bannerService.deleteBanner(id);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showBannerEdit)
	public ModelAndView showBannerEdit(Integer id) {
		SysBanner banner=bannerService.getBanner(id);
		List<SysNotice> list=newService.queryNotices();
		ModelAndView view=new ModelAndView(MappingUtils.showBannerEdit);
		view.addObject("banner", banner);
		view.addObject("list", list);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.editBanner)
	@ResponseBody
	public RestResponse editBanner(Integer id,@RequestParam(value="file",required=false) MultipartFile upfile,Integer noticeid,Integer type) {
		RestResponse result=bannerService.editBanner(id,upfile,noticeid,type);
		return result;
	}
}
