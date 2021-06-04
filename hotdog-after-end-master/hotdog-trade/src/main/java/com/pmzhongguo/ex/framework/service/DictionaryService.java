package com.pmzhongguo.ex.framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.framework.mapper.FrmUserMapper;


@Service
public class DictionaryService {

	@Autowired
	private FrmUserMapper userMapper;

	public List getDictionaryDataList(String dicId) {
		return userMapper.getDictionaryDataList(dicId);
	}
}
