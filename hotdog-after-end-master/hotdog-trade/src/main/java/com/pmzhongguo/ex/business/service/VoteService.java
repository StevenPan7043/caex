package com.pmzhongguo.ex.business.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.mapper.VoteMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;

/**
 * 投票模块
 * 
 * @author zengweixiong
 *
 */
@Service
public class VoteService extends BaseServiceSupport {

	@Resource
	private VoteMapper voteMapper;

	/**
	 * 查询币种信息
	 * 
	 * @return
	 */
	public List<Map<Integer, String>> coins() {
		return voteMapper.coins();
	}

	/**
	 * 查询设置信息
	 * 
	 * @return
	 */
	public Map<String, Object> getSet() {
		return voteMapper.getSet();
	}

	/**
	 * 编辑投票设置信息
	 * 
	 * @param param
	 */
	public void editSet(Map<String, Object> param) {
		voteMapper.editSet(param);
	}

	/**
	 * 查询投票列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> list() {
		return voteMapper.list();
	}

	/**
	 * 投票情况 赞成 反对百分比
	 * 
	 * @return
	 */
	public Map<String, Object> info() {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		Map<String, Object> info = voteMapper.info();
		Float totalNum = Float.valueOf("" + info.get("totalNum"));
		Float likeNum = Float.valueOf("" + info.get("likeNum"));
		Float opposeNum = Float.valueOf("" + info.get("opposeNum"));
		// 换算成%
		String like = (int) Math.rint(likeNum / totalNum * 100) + "%";
		String oppose = (int) Math.rint(opposeNum / totalNum * 100) + "%";
		resultMap.put("like", like);
		resultMap.put("oppose", oppose);
		return resultMap;
	}

	/**
	 * api 列表分页
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, Object> apiList(Map<String, Object> param) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		Integer total = voteMapper.count(param);
		resultMap.put("total", total);
		List<Map<String, Object>> list = voteMapper.apiList(param);
		resultMap.put("list", list);
		return resultMap;
	}

	/**
	 * 添加投票信息
	 * 
	 * @param uid
	 *            用户id
	 * @param pollNum
	 *            投票个数
	 * @param state
	 *            状态 1赞成; 0反对
	 */
	public void add(Integer uid, int pollNum, int state) {
		Map<String, Object> map = voteMapper.getSet();
		map.put("uid", uid);
		map.put("pollNum", pollNum);
		Integer currencyNum = Integer.parseInt("" + map.get("num")) * pollNum;
		map.put("currencyNum", currencyNum);
		map.put("state", state);
		map.put("createtime", HelpUtils.formatDate8(new Date()));

		// 查询用户资产信息
		String currency = (String)   map.get("currency");
		Map<String, Object> asset = voteMapper.findMemberInfo(map);
		asset.put("currencyNum", currencyNum);
		asset.put("currency", currency);

		// 用户资产扣除币种数量
		voteMapper.updateMemberInfo(asset);
		// 添加投票记录
		voteMapper.add(map);
	}

}
