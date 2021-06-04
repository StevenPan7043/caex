package com.pmzhongguo.ex.datalab.service;

import com.pmzhongguo.ex.datalab.entity.PairFreeDetail;
import com.pmzhongguo.ex.datalab.entity.dto.PairFreeDetailDto;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/5 10:03 AM
 */
public interface PairFreeDetailService extends IService<PairFreeDetail> {

    List<PairFreeDetailDto> getPairFreeDetailList(Map<String,Object> reqMap);

}
