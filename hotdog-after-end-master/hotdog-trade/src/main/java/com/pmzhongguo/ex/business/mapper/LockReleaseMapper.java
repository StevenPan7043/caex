package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.dto.CurrencyLockReleaseDto;
import com.pmzhongguo.ex.business.entity.LockRelease;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @description: 锁仓释放
 * @date: 2019-06-03 10:24
 * @author: 十一
 */
public interface LockReleaseMapper extends SuperMapper<LockRelease> {


    List<CurrencyLockReleaseDto> findByIsReleaseAndReleaseTimeGtNow(Map<String, Object> params);

    /**
     * 找到某个用户某个币种锁仓待释放的总和
     * @param memberId
     * @return
     */
    List<CurrencyLockReleaseDto> findWaitReleaseByMemberId(@Param("member_id") Integer memberId);

    /**
     * 找到某个用户某个币种锁仓待释放记录
     * @param params
     * @return
     */
    List<CurrencyLockReleaseDto> findWaitReleaseByMemberIdAndCurrency(Map<String, Object> params);

    /**
     * 通过id和状态来更新
     * @param lockRelease
     * @return
     */
    int updateByIdAndIsRelease(LockRelease lockRelease);
}
