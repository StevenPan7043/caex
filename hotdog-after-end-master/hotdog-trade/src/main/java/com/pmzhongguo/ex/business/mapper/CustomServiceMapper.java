package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * APP更新设置信息
 * 
 * @author zengweixiong
 *
 */
public interface CustomServiceMapper extends SuperMapper<Object> {

	Map findQrCodeInfoById(Integer id);

	int insertQrCode(Map params);

	int updateQrCode(Map params);

	List<Map> findAllIsShow();

	public int deleteById(Integer id);

}
