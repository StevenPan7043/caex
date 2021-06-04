package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.datalab.entity.vo.SysRequestVO;

import java.util.List;
        import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 10:33 AM
 */
public interface IFaceManager{

    ObjResp doSaveProcess(Map<String,Object> reqMapVo) throws Exception;

    ObjResp doUpdateProcess(Map<String,Object> reqMapVo) throws Exception;

    Map<String, Object> getParamMap(Map<String,Object> reqMapVo) throws Exception;

//    List<T> getParamList(SysRequestVO reqVO) throws Exception;
}
