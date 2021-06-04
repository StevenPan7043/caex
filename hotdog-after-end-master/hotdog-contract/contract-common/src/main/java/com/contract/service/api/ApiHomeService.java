package com.contract.service.api;

import java.util.List;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.dao.SysBannerMapper;
import com.contract.dao.SysNoticeMapper;
import com.contract.entity.SysBanner;
import com.contract.entity.SysBannerExample;
import com.contract.entity.SysNotice;
import com.contract.entity.SysNoticeExample;
import com.github.pagehelper.PageHelper;

@Service
public class ApiHomeService {

	@Autowired
	private SysBannerMapper sysBannerMapper;
	@Autowired
	private SysNoticeMapper sysNoticeMapper;
	/**
	 * 获取banner
	 * @param position
	 * @return
	 */
	public RestResponse queryBanner(Integer position, Integer type) {
		if(type==null) {
			type=1;
		}
		SysBannerExample example=new SysBannerExample();
		example.createCriteria().andPositionEqualTo(position).andTypeEqualTo(type);
		List<SysBanner> banners=sysBannerMapper.selectByExample(example);
		return GetRest.getSuccess("成功",banners);
	}
	/**
	 * 查询公告
	 * @param notice
	 * @return
	 */
	public RestResponse queryNotice(SysNotice notice,Integer type) {
		if(type==null) {
			type=1;
			PageHelper.startPage(notice.getPage(),notice.getRows());
		}
		SysNoticeExample example=new SysNoticeExample();
		example.createCriteria().andTypeEqualTo(type);
		example.setOrderByClause("id desc");
		List<SysNotice> list=sysNoticeMapper.selectByExample(example);
		return GetRest.getSuccess("成功",list);
	}
	public RestResponse getNotice(Integer detailid) {
		SysNotice notice=sysNoticeMapper.selectByPrimaryKey(detailid);
		return GetRest.getSuccess("成功",notice);
	}

	
}
