package com.pmzhongguo.ex.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.pmzhongguo.ex.business.mapper.CustomServiceMapper;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.mapper.AppUpdateMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;

/**
 * app更新设置服务
 * 
 * @author zengweixiong
 *
 */
@Service
public class AppUpdateService extends BaseServiceSupport implements IDataProcess {

	@Resource
	private AppUpdateMapper appUpdateMapper;

	@Resource
	private CustomServiceMapper customServiceMapper;

	/**
	 * APP更新设置信息
	 *
	 *            app类型: 1 Android; 2 iOS
	 * @return
	 */
	public Map<String, Object> info(Map<String, Object> map) {
		return appUpdateMapper.findOne(map);
	}

	/**
	 * 获取所有App设置信息
	 * 
	 * @return
	 */
	public List<Map<String, Object>> list() {
		return appUpdateMapper.findAll();
	}

	/**
	 * 编辑设置信息
	 * 
	 * @param param
	 */
	public void edit(Map<String, Object> param) {
		 appUpdateMapper.update(param);
	}

	/**
	 * 加载缓存信息
	 * map ,key = 1 是Android，key = 2 是iOS
	 * @param servletContext
	 */
	public void cacheAppUpdateInfo(ServletContext servletContext) {
		List<Map<String, Object>> list = appUpdateMapper.findAll();
		Map<Integer, Object> map = new HashMap<>();
		for (Map<String, Object> e : list) {
			Object type = e.get("type");
			if (type != null) {
				map.put(Integer.parseInt(type.toString()),e);
			}
		}

		servletContext.setAttribute(HelpUtils.APP_INFO_MAP, map);
	}





	/**
	 * 查找客服二维码信息
	 *
	 */
	public List<Map<String,Object>> findMgrByPage(Map<String, Object> param) {
		return customServiceMapper.findMgrByPage(param);
	}

	/**
	 * 通过id查找
	 */
	public Map findQrCodeInfoById(Integer id){
		return customServiceMapper.findQrCodeInfoById(id);
	}

	/**
	 * 添加客服二维码信息
	 */
	public void addQrCode(Map params){
		customServiceMapper.insertQrCode(params);
	}

	/**
	 * 更新客服二维码信息
	 */
	public void updateQrCode(Map params){
		customServiceMapper.updateQrCode(params);
	}

	/**
	 * 删除客服二维码信息
	 */
	public void delQrCode(Integer id){
		customServiceMapper.deleteById(id);
	}

	public List<Map> findAllQrCode(){
		return customServiceMapper.findAllIsShow();
	}


	/**
	 * 加载缓存信息
	 * map ,key = 1 是Android，key = 2 是iOS
	 * @param servletContext
	 */
	public void cacheQrCode(ServletContext servletContext) {
		List<Map> list = customServiceMapper.findAllIsShow();
		servletContext.setAttribute(HelpUtils.CUSTOM_SERVICE_QR_CODE_LST,list);
	}


	@Override
	public void dataSync(ServletContext servletContext) {
		cacheAppUpdateInfo(servletContext);
		cacheQrCode(servletContext);
	}
}
