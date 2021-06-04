package com.pmzhongguo.ex.datalab.mapper;

import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.datalab.entity.PairFreeDetail;
import com.pmzhongguo.ex.datalab.entity.dto.PairFreeDetailDto;

import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/12/5 9:58 AM
 */
public interface PairFreeDetailsMapper extends SuperMapper<PairFreeDetail> {

    List<PairFreeDetailDto> getPairFreeDetailListByPage(Map<String,Object> reqMap);

}
