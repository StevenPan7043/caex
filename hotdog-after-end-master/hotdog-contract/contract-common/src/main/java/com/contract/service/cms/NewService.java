package com.contract.service.cms;

import java.util.List;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.contract.common.PathUtils;
import com.contract.dao.SysNoticeMapper;
import com.contract.entity.SysNotice;
import com.contract.entity.SysNoticeExample;
import com.image.common.RestUploadFileInfo;

@Service
public class NewService {

	@Autowired
	private SysNoticeMapper sysNoticeMapper;
	
	
	public List<SysNotice> queryNotices() {
		SysNoticeExample example=new SysNoticeExample();
		example.setOrderByClause("createtime desc");
		List<SysNotice> list=sysNoticeMapper.selectByExample(example);
		return list;
	}
	
	public RestResponse deleteNew(Integer id) {
		int i=sysNoticeMapper.deleteByPrimaryKey(id);
		if(i<1) {
			return GetRest.getFail("记录不存在");
		}
		return GetRest.getSuccess("删除成功");
	}


	public SysNotice getNew(Integer id) {
		return sysNoticeMapper.selectByPrimaryKey(id);
	}

	public RestResponse editNew(SysNotice notices, MultipartFile upfile) {
		if(StringUtils.isEmpty(notices.getTitle())) {
			return GetRest.getFail("请填写标题");
		}
		if(StringUtils.isEmpty(notices.getContent())) {
			return GetRest.getFail("请填写内容");
		}
		if(upfile!=null) {
			try {
				RestUploadFileInfo r=com.image.common.Service.uploadImage(upfile.getOriginalFilename(), upfile.getInputStream(), PathUtils.notice_url);
				if(!r.isStatus()) {
					return GetRest.getFail(r.getDesc());
				}
				notices.setImgurl(r.getServiceName()+r.getFilePath()+r.getFileName());
			} catch (Exception e) {
				return GetRest.getFail("上传失败");
			}
		}
		if(notices.getId()!=null) {
			sysNoticeMapper.updateByPrimaryKeySelective(notices);
		}else {
			sysNoticeMapper.insertSelective(notices);
		}
		return GetRest.getSuccess("操作成功");
	}
}
