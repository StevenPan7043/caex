package com.contract.service.cms;

import java.util.List;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contract.common.PathUtils;
import com.contract.dao.SysBannerMapper;
import com.contract.dao.SysNoticeMapper;
import com.contract.entity.SysBanner;
import com.contract.entity.SysBannerExample;
import com.contract.entity.SysNotice;
import com.image.common.RestUploadFileInfo;

@Service
public class BannerService {

	@Autowired
	private SysBannerMapper sysBannerMapper;
	@Autowired
	private SysNoticeMapper sysNoticeMapper;
	public List<SysBanner> queryBanners() {
		SysBannerExample example=new SysBannerExample();
		example.setOrderByClause("id desc");
		List<SysBanner> list=sysBannerMapper.selectByExample(example);
		for(SysBanner l:list) {
			SysNotice notices=sysNoticeMapper.selectByPrimaryKey(l.getNoticeid());
			if(notices!=null) {
				l.setTitle(notices.getTitle());
			}
		}
		return list;
	}
	
	public RestResponse deleteBanner(Integer id) {
		sysBannerMapper.deleteByPrimaryKey(id);
		return GetRest.getSuccess("成功");
	}
	public SysBanner getBanner(Integer id) {
		return sysBannerMapper.selectByPrimaryKey(id);
	}
	public RestResponse editBanner(Integer id, MultipartFile upfile,Integer newsid,Integer type) {
		SysBanner banner=new SysBanner();
		banner.setId(id);
		banner.setType(type);
		banner.setNoticeid(newsid);
		if(upfile!=null) {
			try {
				RestUploadFileInfo r=com.image.common.Service.uploadImage(upfile.getOriginalFilename(), upfile.getInputStream(), PathUtils.banner_url);
				if(!r.isStatus()) {
					return GetRest.getFail(r.getDesc());
				}
				banner.setImgurl(r.getServiceName()+r.getFilePath()+r.getFileName());
			} catch (Exception e) {
				return GetRest.getFail("上传失败");
			}
		}
		if(id!=null) {
			sysBannerMapper.updateByPrimaryKeySelective(banner);
		}else {
			if(upfile==null) {
				return GetRest.getFail("请选择文件");
			}
			sysBannerMapper.insertSelective(banner);
		}
		return GetRest.getSuccess("成功");
	}

	
}
