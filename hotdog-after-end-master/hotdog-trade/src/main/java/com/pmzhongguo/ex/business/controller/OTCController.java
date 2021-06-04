package com.pmzhongguo.ex.business.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.service.MerchantManager;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.entity.OTCOwner;
import com.pmzhongguo.ex.business.service.OTCService;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmUser;

@ApiIgnore
@Controller
@RequestMapping("backstage/otc")
public class OTCController extends TopController {
	@Resource
	private OTCService otcService;


	@Autowired
	private MerchantManager merchantManager;

	@RequestMapping("/owner_list")
	public String toOwnerList(HttpServletRequest request, HttpServletResponse response) {
		return "business/otc/owner_list";
	}

	@RequestMapping("/listOwner")
	@ResponseBody
	public Map listOwner(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map params = $params(request);
		List jsonList = otcService.getAllOTCOwner(params);
		Map map = new HashMap();
		map.put("Rows", jsonList);
		map.put("Total", params.get("total"));
		return map;
	}

	@RequestMapping("/toAddOwner")
	public String toAddOwner(HttpServletRequest request, HttpServletResponse response) {
		attrCommonDateTime();
		return "business/otc/owner_edit";
	}

	@RequestMapping("/toAddImg")
	public String toAddImg(HttpServletRequest request, HttpServletResponse response) {
		$attr("image", $("image"));
		$attr("sType", $("sType"));
		return "business/otc/img_edit";
	}

	@RequestMapping(value = "/addOwner", method = RequestMethod.POST)
	@ResponseBody
	public Resp addOwner(HttpServletRequest request, OTCOwner otcOwner) {

		otcService.addOTCOwner(otcOwner);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	@RequestMapping("/toEditOwner")
	public String toEditOwner(HttpServletRequest request, HttpServletResponse response) {
		Integer id = $int("id");
		OTCOwner otcOwner = otcService.findOTCOwnerById(id);
		$attr("info", otcOwner);

		return "business/otc/owner_edit";
	}

	@RequestMapping(value = "/editOwner", method = RequestMethod.POST)
	@ResponseBody
	public Resp editOwner(HttpServletRequest request, OTCOwner otcOwner) {
		otcService.updateOTCOwner(otcOwner);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	@ApiIgnore
	@RequestMapping(value = "/addImg", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp addImg(HttpServletRequest request) throws Exception {
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			if (request.getHeader("content-type") != null
					&& "application/x-www-form-urlencoded".equals(request.getHeader("content-type"))) {
				return new ObjResp(Resp.FAIL, "error", null);
			}
			// 将请求转换成
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;

			String folder = HelpUtils.formatDate4(new Date());
			String path = request.getSession().getServletContext().getRealPath("/") + "ueditor/";
			String fileName = UUID.randomUUID().toString();

			Iterator<String> fns = mRequest.getFileNames();// 获取上传的文件列表
			if (fns.hasNext()) {
				String s = fns.next();

				MultipartFile mFile = mRequest.getFile(s);
				if (mFile.isEmpty()) {
					map.put("error", "EventAction.picture.failed");
				} else {

					File dir = new File(path);
					// 如果文件夹不存在则创建
					if (!dir.exists() && !dir.isDirectory()) {
						dir.mkdir();
					}
					String originFileName = mFile.getOriginalFilename();
					String suffix = originFileName.split("\\.")[originFileName.split("\\.").length - 1];
					fileName += (System.currentTimeMillis() + HelpUtils.randomString(10) + "." + suffix);
					File file = new File(path, fileName);

					FileUtils.copyInputStreamToFile(mFile.getInputStream(), file);// 存储文件
				}
			}
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,
					HelpUtils.getMgrConfig().getServer_url() + "/ueditor/" + fileName);
		} catch (IOException e) {
			return new ObjResp(Resp.FAIL, e.toString(), null);
		}
	}

	@RequestMapping(value = "/delOwner", method = RequestMethod.POST)
	@ResponseBody
	public Resp delOwner(HttpServletRequest request) {
		Integer id = $int("id");

		// 判断能否删除：
		Integer isCanDel = daoUtil.queryForInt("SELECT count(1) FROM otc_ads WHERE owner_id = ?", id);
		if (isCanDel > 0) {
			throw new BusinessException(-1, ErrorInfoEnum.ADS_HAS_USED_NOT_DELETE.getErrorCNMsg());
		}

		otcService.delOTCOwner(id);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	@RequestMapping("/ads_list")
	public String toAdsList(HttpServletRequest request, HttpServletResponse response) {
		return "business/otc/ads_list";
	}

	/**
	 * 广告管理列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listAds")
	@ResponseBody
	public Map<String, Object> listAds(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = $params(request);
		List<OTCAds> jsonList = otcService.getAllOTCAds(params);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("Rows", jsonList);
		map.put("Total", params.get("total"));
		return map;
	}

	@RequestMapping("/toAddAds")
	public String toAddAds(HttpServletRequest request, HttpServletResponse response) {
		return "business/otc/ads_edit";
	}

	@RequestMapping(value = "/addAds", method = RequestMethod.POST)
	@ResponseBody
	public Resp addAds(HttpServletRequest request, OTCAds otcAds) {
		otcService.addOTCAds(otcAds);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	@RequestMapping("/toEditAds")
	public String toEditAds(HttpServletRequest request, HttpServletResponse response) {
		Integer id = $int("id");
		OTCAds otcAds = otcService.findOTCAdsById(id);
		$attr("info", otcAds);

		return "business/otc/ads_edit";
	}

	@RequestMapping(value = "/editAds", method = RequestMethod.POST)
	@ResponseBody
	public Resp editAds(HttpServletRequest request, OTCAds otcAds) {
		otcService.updateOTCAds(otcAds);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 商户基本信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/owner_info")
	public String ownerInfo(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = otcService.ownerInfo();
		$attr("result", map);
		return "business/otc/owner_info";
	}

	/**
	 * 取消商家资格
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/merchant/cancelBusAuth", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp cancelBusinessAuthority(HttpServletRequest request, HttpServletResponse response) {
		FrmUser user = (FrmUser) request.getSession().getAttribute(Constants.SYS_SESSION_USER);
		if (BeanUtil.isEmpty(user)){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
		}
//		if (!user.getRole().equals("super超级管理员")){
//			return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_IS_SUPERUSER.getErrorENMsg(), null);
//		}
		MerchantDTO dto = merchantManager.findById($int("id"));
		if (dto == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_RECORD.getErrorENMsg(), null);
		}
		if (dto.getStatus().getType() != AuditStatusEnum.APPLY_PASSED.getType()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.MAST_BE_AUDIT_PASS.getErrorENMsg(), null);
		}
		logger.warn("管理员取消商家权限,操作人IP：{},操作时间：{}", user.getUser_name()+"："+HelpUtils.getIpAddr(request), DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
		return merchantManager.auditMerchant(dto.getMemberId(), dto.getDepositCurrency(), dto.getDepositVolume(),
				AuditStatusEnum.SECEDE_PASSED, StringUtils.isBlank(dto.getMemo())?"管理员删除商家权限":dto.getMemo()+"▲管理员删除商家权限", null);
	}


}
