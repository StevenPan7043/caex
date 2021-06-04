package com.pmzhongguo.ipfs.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ipfs.constant.CONSTANT;
import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Daily
 * @date 2020/7/13 16:23
 */
@Service
public class IpfsOutputManager {

    private static Logger logger = LoggerFactory.getLogger(IpfsOutputManager.class);

    //小数位数暂按1位算，先用一个常量，后面要写个方法计算小数的调整数
    private static final String ADJUSTMENT = "100";

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private IpfsOutputService ipfsOutputService;

    /**
     * 根据条件查询数据
     *
     * @param param
     * @return
     */
    public List<IpfsOutput> findByConditionPage(Map<String, Object> param) {
        return ipfsOutputService.findByConditionPage(param);
    }

    List<IpfsOutput> findByCondition(Map<String, Object> param) {
        return ipfsOutputService.findByCondition(param);
    }

    /**
     * 根据项目表创建一条产量记录
     *
     * @param projectId
     * @param outputDate yyyy-MM-dd
     * @return
     */
    public IpfsOutput create(Integer projectId, String outputDate) {
        String qurey = "SELECT t.`output_currency` currency, t.`r_output_floor` rfloor,  t.`r_output_upper` rupper FROM `ipfs_project` t WHERE t.id = ? AND t.`run_status` = '2';";
        Map<String, Object> map = daoUtil.queryForMap(qurey, projectId);
        return create(projectId, outputDate, map);
    }

    /**
     * 根据项目表创建所有的产量记录
     *
     * @param outputDate yyyy-MM-dd
     * @return
     */
    public List<IpfsOutput> createAll(String outputDate) {
        String qureyone = "SELECT t.id id, t.`output_currency` currency, t.`r_output_floor` rfloor,  t.`r_output_upper` rupper, t.`exchange_rate` exchange FROM `ipfs_project` t WHERE t.`id` = ?;";
        Map<String, Object> scaleMap = daoUtil.queryForMap(qureyone, 35);
        Object rfloor = scaleMap.get("rfloor");
        int floor = (new BigDecimal(rfloor.toString())).multiply(new BigDecimal(ADJUSTMENT)).intValue();
        Object rupper = scaleMap.get("rupper");
        int upper = (new BigDecimal(rupper.toString())).multiply(new BigDecimal(ADJUSTMENT)).intValue();
        Random rand = new Random();
        int bound = upper - floor + 1;
        int capacity = rand.nextInt(bound) + floor;//今日产出量
        BigDecimal out = BigDecimal.valueOf(capacity).divide(new BigDecimal(ADJUSTMENT));
        String qurey = "SELECT t.id id, t.`output_currency` currency, t.`r_output_floor` rfloor,  t.`r_output_upper` rupper, t.`exchange_rate` exchange FROM `ipfs_project` t WHERE t.`run_status` = '2';";
        List<Map<String, Object>> maps = daoUtil.queryForList(qurey);
        List<IpfsOutput> list = new ArrayList<IpfsOutput>();
        for (Map<String, Object> m : maps) {
            try {
                Object rate = m.get("exchange");
                Integer projectId = Integer.valueOf(String.valueOf(m.get("id")));
                IpfsOutput ipfsOutput = create(projectId, outputDate, m);
                ipfsOutput.setCapacity(out.multiply(new BigDecimal(rate.toString())));
                ipfsOutput.setCapacityOut(ipfsOutput.getCapacity());
                list.add(ipfsOutput);
            } catch (Exception e) {
                logger.warn(m.toString() + "\n" + e.getStackTrace());
            }
        }
        return list;
    }

    /**
     * 插入一条产量记录到数据库
     *
     * @param ipfsOutput
     * @return
     */
    public int insert(IpfsOutput ipfsOutput) {
        return ipfsOutputService.insert(ipfsOutput);
    }

    /**
     * 插入多条产量记录到数据库
     *
     * @return 插入成功的条数
     */
    public int insert(List<IpfsOutput> list) {
        int result = 0;
        for (IpfsOutput t : list) {
            try {
                List<IpfsOutput> queryList = findByCondition(HelpUtils.newHashMap("projectId", t.getProjectId(), "outputDate", t.getOutputDate()));
                if (CollectionUtils.isNotEmpty(queryList)) {
                    t.setCapacity(queryList.get(0).getCapacity());
                    continue;
                }
                int r = insert(t);
                if (r == 1) {
                    result++;
                }
            } catch (Exception e) {
                logger.warn(t.toString() + " \n " + e.getStackTrace());
            }
        }
        return result;
    }

    private IpfsOutput create(Integer projectId, String outputDate, Map<String, Object> map) {
        IpfsOutput ipfsOutput = new IpfsOutput();
        ipfsOutput.setOutputDate(outputDate);   //产量日期
        ipfsOutput.setProjectId(projectId);     //项目id
//        Object rfloor = map.get("rfloor");
//        int floor = (new BigDecimal(rfloor.toString())).multiply(new BigDecimal(ADJUSTMENT)).intValue();
//        Object rupper = map.get("rupper");
//        int upper = (new BigDecimal(rupper.toString())).multiply(new BigDecimal(ADJUSTMENT)).intValue();
//        Random rand = new Random();
//        int bound = upper - floor + 1;
//        int capacity = rand.nextInt(bound) + floor;
//        ipfsOutput.setCapacity(BigDecimal.valueOf(capacity).divide(new BigDecimal(ADJUSTMENT)));  //今日产出量
        ipfsOutput.setOutputCurrency(String.valueOf(map.get("currency")));  //产出币种
//        ipfsOutput.setCapacityOut(ipfsOutput.getCapacity());
        ipfsOutput.setOutputStatus(0);
        return ipfsOutput;
    }

    /**
     * 获取所有产量
     *
     * @param param
     * @return
     */
    public List<IpfsOutput> findAllByStatus(Map<String, Object> param) {
        int limit = Integer.parseInt(param.get("limit").toString());
        int pageNum = Integer.parseInt(param.get("pageNum").toString());
        List<IpfsOutput> result = new ArrayList<>();
        while (true) {
            List<IpfsOutput> outputList = ipfsOutputService.findByCondition(param);
            result.addAll(outputList);
            if (outputList.size() < limit) {
                break;
            } else {
                param.put("pageNum", limit + pageNum);
            }
        }
        return result;
    }

    /**
     * 分红的释放量
     *
     * @param bonusDate
     * @param output
     * @param typeMap   购买类型
     * @return
     */
    public BigDecimal unlockOutPut(String bonusDate, IpfsOutput output, Map<Integer, String> typeMap) {
        //已产出天数
        int intervalDays = DateUtil.getIntervalDays(bonusDate, output.getOutputDate()) + 1;
        BigDecimal day = new BigDecimal(intervalDays);
        int model = BigDecimal.ROUND_DOWN;//舍取方式
        int precision = 6;//小数位数
        BigDecimal total = new BigDecimal("0");//当日解封数量
        BigDecimal lock_day = CONSTANT.LOCK_DAY;
        if (typeMap.get(output.getProjectId()).equals("2")) {
            lock_day = CONSTANT.KJ_LOCK_DAY;
        }
        //判断剩余释放量
        if (output.getCapacityOut() == null || output.getCapacityOut().compareTo(BigDecimal.ZERO) <= 0) {
            output.setOutputStatus(1);
            return total;
        }
        if (day.compareTo(CONSTANT.FROZEN_DAY) <= 0) {
            if (day.compareTo(lock_day) < 0) {
                total = output.getCapacity().multiply(day).divide(CONSTANT.FROZEN_DAY.multiply(lock_day), precision, model);
            } else {
                total = output.getCapacity().divide(CONSTANT.FROZEN_DAY, precision, model);
            }
            output.setCapacityOut(total);
            if (day.compareTo(CONSTANT.FROZEN_DAY) == 0) {
                output.setOutputStatus(1);
            }
        }
        return total;
    }

    public int reduceCapacity(List<IpfsOutput> list) {
        return ipfsOutputService.reduceCapacity(list);
    }

    public int updateIpfsOutput(IpfsOutput ipfsOutput) {
        return ipfsOutputService.updateIpfsOutput(ipfsOutput);
    }
}
