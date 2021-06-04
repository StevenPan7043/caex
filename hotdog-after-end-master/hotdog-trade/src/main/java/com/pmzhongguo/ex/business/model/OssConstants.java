package com.pmzhongguo.ex.business.model;

import com.pmzhongguo.ex.core.utils.PropertiesUtil;

/**
 * 阿里云服务器上传配置
 * @author jary
 * @creatTime 2020/4/24 9:50 AM
 */
public class OssConstants {

    /**
     * OSS访问地址
     */
//    public static final String ALIYUN_OSS_ENDPOINT = "oss-cn-shanghai.aliyuncs.com";
    public static final String ALIYUN_OSS_ENDPOINT = PropertiesUtil.getPropValByKey("aliyun_endpoint");
//    public static final String ALIYUN_OSS_ENDPOINT = "oss-cn-shanghai.aliyuncs.com";

    /**
     * id   : LTAI4GDVt6UhcVry8E4etdTL
     * key:  khGFtfEvChV30skzG3DmcjdJKL0fK3
     * 阿里云的访问·KEY
     */
//    public static final String ALIYUN_ACCESS_KEY = "LTAIh2PPYtuCjduK";
    public static final String ALIYUN_ACCESS_KEY = PropertiesUtil.getPropValByKey("aliyun_accessId");
//    public static final String ALIYUN_ACCESS_KEY = "LTAI4GDVt6UhcVry8E4etdTL";

    /**
     * 阿里云的访问·秘钥
     */
//    public static final String ALIYUN_ACCESS_SECRET = "5nRsAgse0Nq9ZGHtE3CwVhNqOpH6lV";
    public static final String ALIYUN_ACCESS_SECRET = PropertiesUtil.getPropValByKey("aliyun_accessKey");
//    public static final String ALIYUN_ACCESS_SECRET = "khGFtfEvChV30skzG3DmcjdJKL0fK3";

    /**
     * 临时空间
     */
    public static final String BUCKET_TEMP_NAME = PropertiesUtil.getPropValByKey("aliyun_bucket");
//    public static final String BUCKET_TEMP_NAME = "peppaexcom";  线上环境
//    public static final String BUCKET_TEMP_NAME = "ppexinfo";     //测试环境

    /**
     * 认证国外上传路径
     */
        public static final String ALIYUN_IDCARD_DIR = PropertiesUtil.getPropValByKey("aliyun_gw_dir");

//    public static final String ALIYUN_IDCARD_DIR = "idcard/";
    /**
     * 认证国内上传路径
     */
        public static final String ALIYUN_UEDITOR_DIR = PropertiesUtil.getPropValByKey("aliyun_ueditor_dir");
//    public static final String ALIYUN_UEDITOR_DIR = "ueditor/";

    /**
     * 访问前缀
     */
    public static final String ALIYUN_HOST = "http://"+BUCKET_TEMP_NAME+"."+ALIYUN_OSS_ENDPOINT+"/";
}
