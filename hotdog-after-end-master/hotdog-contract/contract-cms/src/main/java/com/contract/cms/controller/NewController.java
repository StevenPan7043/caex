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
import com.contract.entity.SysNotice;
import com.contract.service.cms.NewService;


@Controller
public class NewController {

	@Autowired
	private NewService newService;
	/**
	 * 查询资讯列表
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showNotices)
	public ModelAndView showNotices() {
		List<SysNotice> list=newService.queryNotices();
		ModelAndView view=new ModelAndView(MappingUtils.showNotices);
		view.addObject("list", list);
		return view;
	}
	
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping(value = MappingUtils.deleteNew)
	@ResponseBody
	public RestResponse deleteNew(Integer id) {
		RestResponse result=newService.deleteNew(id);
		return result;
	}
	
	/**
	 * 显示编辑页面
	 * @param id
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showNewEdit)
	public ModelAndView showNewEdit(Integer id) {
		SysNotice notices=newService.getNew(id);
		ModelAndView view=new ModelAndView(MappingUtils.showNewEdit);
		view.addObject("notices", notices);
		return view;
	}
	
	/**
	 * 新增编辑新闻
	 * @return
	 */
	@RequestMapping(value = MappingUtils.editNew)
	@ResponseBody
	public RestResponse editNew(SysNotice notices,@RequestParam(value="file",required=false) MultipartFile upfile) {
		RestResponse result=newService.editNew(notices,upfile);
		return result;
	}
	
}
