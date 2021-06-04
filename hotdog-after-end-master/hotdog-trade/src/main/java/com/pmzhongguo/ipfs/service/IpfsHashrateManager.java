package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ipfs.constant.CONSTANT;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.entity.IpfsProjectWithBLOBs;
import com.qiniu.util.BeanUtil;
import jxl.read.biff.Record;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Daily
 * @date 2020/7/15 14:47
 */
@Service
public class IpfsHashrateManager {

    @Autowired
    private IpfsHashrateService ipfsHashrateService;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private IpfsProjectManager ipfsProjectManager;


    /**
     * 1、加锁
     * 5、推荐人分红
     * 6、推荐人增加资产
     */
    @Transactional
    public ObjResp purchase(int memberId, int projectId, int num) {
        String globalLockKey = "IPFSHASHRATEMANAGER_PURCHASE" + projectId;
        boolean globalIsLock = JedisUtil.getInstance().getLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS, 1000 * 5);
        int id = 0;
        if (globalIsLock) {
            try {
                String query = "SELECT t.`publish_num`, t.`bought_num`, t.project_name, t.`code`, t.`output_currency`, t.`quote_currency`, t.`price`, t.discount, t.type FROM `ipfs_project` t WHERE  t.sale_status = ? AND t.id = ?;";
                Map<String, Object> project = daoUtil.queryForMap(query, "2", projectId);
                if (project == null) {
                    return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_THIRDPARTTY_NOT_EXIST.getErrorENMsg(), null);
                }
                int publishNum = Integer.valueOf(String.valueOf(project.get("publish_num")));
                int boughtNum = Integer.valueOf(String.valueOf(project.get("bought_num")));
                boughtNum = boughtNum + num;
                if (boughtNum > publishNum) {
                    return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INSUFFICIENT_STOCK.getErrorENMsg(), null);
                }

                //扣减资产
                BigDecimal price = new BigDecimal(String.valueOf(project.get("price")));
                String quoteCurrency = String.valueOf(project.get("quote_currency"));
                BigDecimal total = price.multiply(new BigDecimal(num));

                //会员购买达到一定数量后有折扣
//                Map<String, Object> buyHashrate = daoUtil.queryForMap("SELECT SUM(t.`total`) total FROM `ipfs_hashrate` t WHERE t.`member_id` = ?; ", memberId);
//                if(buyHashrate != null && buyHashrate.get("total") != null){
//                    BigDecimal bBuyHashrate = new BigDecimal(String.valueOf(buyHashrate.get("total")));
//                    BigDecimal buyDiscount = BigDecimal.ONE;
//                    if(bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_SEVEN_HASHRATE) >= 0){
//                        buyDiscount = CONSTANT.BUY_DISCOUNT_SEVEN;
//                    }else if(bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_EIGHT_HASHRATE) >= 0){
//                        buyDiscount = CONSTANT.BUY_DISCOUNT_EIGHT;
//                    }else if(bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_NINE_HASHRATE) >= 0){
//                        buyDiscount = CONSTANT.BUY_DISCOUNT_NINE;
//                    }
//                    total = total.multiply(buyDiscount);
//                }
                //会员本次购买达到一定数量后有折扣
                if (project.get("type").equals("1")) {
                    BigDecimal bBuyHashrate = new BigDecimal(num);
                    BigDecimal buyDiscount = BigDecimal.ONE;
                    if (bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_SEVEN_HASHRATE) >= 0) {
                        buyDiscount = CONSTANT.BUY_DISCOUNT_SEVEN;
                    } else if (bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_EIGHT_HASHRATE) >= 0) {
                        buyDiscount = CONSTANT.BUY_DISCOUNT_EIGHT;
                    } else if (bBuyHashrate.compareTo(CONSTANT.BUY_DISCOUNT_NINE_HASHRATE) >= 0) {
                        buyDiscount = CONSTANT.BUY_DISCOUNT_NINE;
                    }
                    total = total.multiply(buyDiscount);
                }
                //活动折扣
                BigDecimal actionDiscount = new BigDecimal(String.valueOf(project.get("discount")));
                total = total.multiply(actionDiscount);
                memberService.accountProc(total.negate(), quoteCurrency, memberId, 3, OptSourceEnum.IPFS);
                //添加算力
                IpfsHashrate ipfsHashrate = new IpfsHashrate();
                ipfsHashrate.setProjectId(projectId);
                ipfsHashrate.setProjectCode(String.valueOf(project.get("code")));
                ipfsHashrate.setMemberId(memberId);
                ipfsHashrate.setNum(num);
                ipfsHashrate.setOutputCurrency(String.valueOf(project.get("output_currency")));
                ipfsHashrate.setQuoteCurrency(String.valueOf(project.get("quote_currency")));
                ipfsHashrate.setProjectName(String.valueOf(project.get("project_name")));
                ipfsHashrate.setPrice(price);
                ipfsHashrate.setTotal(total);
                ipfsHashrate.setType(String.valueOf(project.get("type")));
                id = insert(ipfsHashrate);
                //修改项目表数量
                IpfsProjectWithBLOBs ipfsProjectWithBLOBs = new IpfsProjectWithBLOBs();
                ipfsProjectWithBLOBs.setId(projectId);
                ipfsProjectWithBLOBs.setBoughtNum(boughtNum);
                //已购数等于发行数量表示已经售馨
                if (boughtNum == publishNum) {
                    ipfsProjectWithBLOBs.setSaleStatus("3");
                }
                ipfsProjectManager.updateByPrimaryKeySelective(ipfsProjectWithBLOBs);
            } finally {
                JedisUtil.getInstance().releaseLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, id);
    }

    public int insert(IpfsHashrate record) {
        return ipfsHashrateService.insert(record);
    }

    public List<IpfsHashrate> findByConditionPage(Map<String, Object> param) {
        return ipfsHashrateService.findByConditionPage(param);
    }

    ;

    /**
     * 查询用户在某个项目累计的算力和收益
     *
     * @param memberId
     * @param projectId
     * @param saleStatus
     * @return
     */
    public List<Map<String, Object>> getUserBonus(int memberId, int projectId, String saleStatus) {
        Date date = HelpUtils.dateAddInt(new Date(), -1);
        String bonusDate = HelpUtils.dateToSimpleStr(date);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tt4.id, tt4.`project_name`, tt4.`equity_cycle`, tt4.`sale_status`, \n" +
                "tt1.hashrate, tt2.bonus, tt3.totalBonus FROM\n" +
                "(SELECT t1.`project_id`, SUM(t1.`num`) hashrate FROM `ipfs_hashrate` t1 \n" +
                "WHERE t1.`member_id` = ? GROUP BY t1.`project_id`) tt1 \n" +
                "LEFT JOIN \n" +
                " `ipfs_project` tt4 \n" +
                "ON tt4.`id` = tt1.project_id \n" +
                "LEFT JOIN \n" +
                "(SELECT t2.`project_id`, SUM(t2.`bonus_num`) bonus FROM `ipfs_user_bonus` t2 \n" +
                "WHERE t2.`member_id` = ? AND t2.`bonus_date` = ? GROUP BY t2.`project_id`) tt2 \n" +
                "ON tt1.project_id = tt2.project_id \n" +
                "LEFT JOIN \n" +
                "(SELECT t3.`project_id`, SUM(t3.`bonus_num`) totalBonus FROM `ipfs_user_bonus` t3 \n" +
                "WHERE t3.`member_id` = ? GROUP BY t3.`project_id`) tt3 \n" +
                "ON tt1.project_id = tt3.project_id  ");
        if (projectId != 0 || !saleStatus.equals("0")) {
            sb.append(" where ");
            if (projectId != 0) {
                sb.append(" tt4.id = " + projectId);
            }
            if (!saleStatus.equals("0")) {
                if (projectId != 0) {
                    sb.append(" and tt4.sale_status= " + saleStatus);
                } else {
                    sb.append(" tt4.sale_status= " + saleStatus);
                }
            }
        }
        List<Map<String, Object>> list = daoUtil.queryForList(sb.toString(), memberId, memberId, bonusDate, memberId);
        return list;
    }

    /**
     * 是否限购
     *
     * @param projectid
     * @param memberid
     * @param num
     * @return true 限制   false 不限制
     */
    public boolean isLimitBuy(int projectid, int memberid, int num) {
        String query = "SELECT SUM(h.`num`) total, p.`user_buy_limit` buyLimit FROM `ipfs_hashrate` h\n" +
                "LEFT JOIN `ipfs_project` p ON h.`project_id` = p.`id`\n" +
                "WHERE h.`project_id` = ? AND h.member_id = ?";
        Map<String, Object> map = daoUtil.queryForMap(query, projectid, memberid);
        if (map.get("total") == null) {
            return false;
        }
        int total = Integer.parseInt(String.valueOf(map.get("total")));
        int limit = Integer.parseInt(String.valueOf(map.get("buyLimit")));
        return limit < total + num;
    }

    /**
     * 根据主键查询用户算力记录
     *
     * @param id
     * @return
     */
    public IpfsHashrate selectByPrimaryKey(Integer id) {
        return ipfsHashrateService.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(IpfsHashrate record) {
        return ipfsHashrateService.updateByPrimaryKeySelective(record);
    }

    public Resp PLHashrateEdit(Map param) {
        try {
            String[] idSplit = null;
            String[] statusSplit = null;
            IpfsHashrate record = new IpfsHashrate();
            if (BeanUtil.isEmpty(param)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorENMsg(), null);
            }
            Object ids = param.get("ids");
            Object statuss = param.get("statuss");
            String statusParam = String.valueOf(param.get("status"));
            String idParam = String.valueOf(param.get("id"));
            if (BeanUtil.isEmpty(statusParam) && BeanUtil.isEmpty(idParam)) {
                ids = idParam;
                statuss = statusParam;
            }
            idSplit = String.valueOf(ids).split(",");
            statusSplit = String.valueOf(statuss).split(",");
            for (int i = 0; i < idSplit.length; i++) {
                if ("1".equals(statusSplit[i])) {
                    statusSplit[i] = "2";
                } else {
                    statusSplit[i] = "1";
                }
                Integer id = Integer.parseInt(idSplit[i]);
                String status = statusSplit[i];
                record.setId(id);
                record.setStatus(status);
                ipfsHashrateService.updateByPrimaryKeySelective(record);
            }


        } catch (Exception e) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    public List<IpfsHashrate> findHashrate(Map<String, Object> param) {
        return ipfsHashrateService.findHashrate(param);
    }
}
