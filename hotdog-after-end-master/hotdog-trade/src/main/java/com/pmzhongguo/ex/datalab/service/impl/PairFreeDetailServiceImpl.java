package com.pmzhongguo.ex.datalab.service.impl;

import com.pmzhongguo.ex.datalab.entity.PairFreeDetail;
import com.pmzhongguo.ex.datalab.entity.dto.PairFreeDetailDto;
import com.pmzhongguo.ex.datalab.mapper.PairFreeDetailsMapper;
import com.pmzhongguo.ex.datalab.service.PairFreeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/5 10:03 AM
 */
@Service
@Transactional
public class PairFreeDetailServiceImpl implements PairFreeDetailService {

    @Autowired private PairFreeDetailsMapper pairFreeDetailsMapper;

    @Override
    public Integer save(PairFreeDetail pojo)  {
        return pairFreeDetailsMapper.insert(pojo);
    }

    @Override
    public Integer update(PairFreeDetail pojo)  {
        return pairFreeDetailsMapper.updateById(pojo);
    }

    @Override
    public List<PairFreeDetailDto> getPairFreeDetailList(Map<String, Object> reqMap) {
        return pairFreeDetailsMapper.getPairFreeDetailListByPage(reqMap);
    }
}
