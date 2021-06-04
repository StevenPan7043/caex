package com.pmzhongguo.ex.framework.entity;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class FrmConfig implements Serializable
{
    private static final long serialVersionUID = -3751054023254677358L;
    private String comp_name;// 公司名称，标题栏、报表等处，均使用该字段内容
    private String comp_en_name; //公司英文名，在邮件中展示
    private String def_passwd;// 新增用户、初始化密码时默认的密码
    private Integer is_use_big_log;// 是否启用大日志模式，大日志模式时，SELECT语句也输出
    private String cms_def_author;// 文章默认作者
    private String mail_reciver;// 邮箱接收人，多个以逗号分隔
    private Integer is_use_validate;// 是否启用验证码
    private String reg_reward_currencys; //注册送币类型
    private String invite_reward_currencys; //推荐送币类型
    private String reg_reward_volume; //注册送币数量
    private String invite_reward_volume; //推荐送币数量
    private String redis_ip;//
    private String redis_port;//
    private String redis_pwd;//
    private Integer is_stop_ex; //是否停止交易
    private Integer volume_multiply; //成交量乘以多少，默认1
    private String mail_name;  //邮件NAME
    private String mail_nick_name; //邮件nickName
    private String mail_password; //邮件PASSWORD
    private Integer withdraw_need_identity; //提现是否需实名认证
    private String ex_secreat; //数据交换私钥
    private String api_access_limit_rule; //APi访问次数限制规则


    private String server_url; //服务器URL
    private String root_path; //程序所在位置
    private Integer fraud_seconds; //刷数据频率，单位秒
    private String cron_job_ip;    //指定执行计划任务的服务器IP地址
    private String eoslist;    // 3代币充值地址 存储格式： aac,zzexqbao1|eos,zzexqbao2
    private String otc_deposit_currency;
    private BigDecimal otc_deposit_volume;

    /**
     * 返佣比例
     */
    private BigDecimal return_commi_rate;

    /**
     * 返佣有效时间，单位；月
     */
    private Integer return_commi_time;

    public BigDecimal getReturn_commi_rate()
    {
        return return_commi_rate;
    }

    public void setReturn_commi_rate(BigDecimal return_commi_rate)
    {
        this.return_commi_rate = return_commi_rate;
    }

    public Integer getReturn_commi_time()
    {
        return return_commi_time;
    }

    public void setReturn_commi_time(Integer return_commi_time)
    {
        this.return_commi_time = return_commi_time;
    }

    public String getEoslist()
    {
        return eoslist;
    }

    public void setEoslist(String eoslist)
    {
        this.eoslist = eoslist;
    }

    public String getCron_job_ip()
    {
        return cron_job_ip;
    }

    public void setCron_job_ip(String cron_job_ip)
    {
        this.cron_job_ip = cron_job_ip;
    }

    public String getApi_access_limit_rule()
    {
        return api_access_limit_rule;
    }

    public void setApi_access_limit_rule(String api_access_limit_rule)
    {
        this.api_access_limit_rule = api_access_limit_rule;
    }

    public String getEx_secreat()
    {
        return ex_secreat;
    }

    public void setEx_secreat(String ex_secreat)
    {
        this.ex_secreat = ex_secreat;
    }

    public String getMail_name()
    {
        return mail_name;
    }

    public void setMail_name(String mail_name)
    {
        this.mail_name = mail_name;
    }

    public String getMail_nick_name()
    {
        return mail_nick_name;
    }

    public void setMail_nick_name(String mail_nick_name)
    {
        this.mail_nick_name = mail_nick_name;
    }

    public String getMail_password()
    {
        return mail_password;
    }

    public void setMail_password(String mail_password)
    {
        this.mail_password = mail_password;
    }

    public Integer getWithdraw_need_identity()
    {
        return withdraw_need_identity;
    }

    public void setWithdraw_need_identity(Integer withdraw_need_identity)
    {
        this.withdraw_need_identity = withdraw_need_identity;
    }

    public String getComp_en_name()
    {
        return comp_en_name;
    }

    public void setComp_en_name(String comp_en_name)
    {
        this.comp_en_name = comp_en_name;
    }

    public Integer getFraud_seconds()
    {
        return fraud_seconds;
    }

    public void setFraud_seconds(Integer fraud_seconds)
    {
        this.fraud_seconds = fraud_seconds;
    }

    public String getServer_url()
    {
        return server_url;
    }

    public void setServer_url(String server_url)
    {
        this.server_url = server_url;
    }

    public String getRoot_path()
    {
        return root_path;
    }

    public void setRoot_path(String root_path)
    {
        this.root_path = root_path;
    }

    public Integer getVolume_multiply()
    {
        return volume_multiply;
    }

    public void setVolume_multiply(Integer volume_multiply)
    {
        this.volume_multiply = volume_multiply;
    }

    public Integer getIs_stop_ex()
    {
        return is_stop_ex;
    }

    public void setIs_stop_ex(Integer is_stop_ex)
    {
        this.is_stop_ex = is_stop_ex;
    }

    public String getReg_reward_currencys()
    {
        return reg_reward_currencys;
    }

    public void setReg_reward_currencys(String reg_reward_currencys)
    {
        this.reg_reward_currencys = reg_reward_currencys;
    }

    public String getInvite_reward_currencys()
    {
        return invite_reward_currencys;
    }

    public void setInvite_reward_currencys(String invite_reward_currencys)
    {
        this.invite_reward_currencys = invite_reward_currencys;
    }

    public String getReg_reward_volume()
    {
        return reg_reward_volume;
    }

    public void setReg_reward_volume(String reg_reward_volume)
    {
        this.reg_reward_volume = reg_reward_volume;
    }

    public String getInvite_reward_volume()
    {
        return invite_reward_volume;
    }

    public void setInvite_reward_volume(String invite_reward_volume)
    {
        this.invite_reward_volume = invite_reward_volume;
    }

    public String getRedis_ip()
    {
        return redis_ip;
    }

    public void setRedis_ip(String redis_ip)
    {
        this.redis_ip = redis_ip;
    }

    public String getRedis_port()
    {
        return redis_port;
    }

    public void setRedis_port(String redis_port)
    {
        this.redis_port = redis_port;
    }

    public String getRedis_pwd()
    {
        return redis_pwd;
    }

    public void setRedis_pwd(String redis_pwd)
    {
        this.redis_pwd = redis_pwd;
    }

    public String getComp_name()
    {
        return comp_name;
    }

    public void setComp_name(String comp_name)
    {
        this.comp_name = comp_name;
    }

    public String getDef_passwd()
    {
        return def_passwd;
    }

    public void setDef_passwd(String def_passwd)
    {
        this.def_passwd = def_passwd;
    }

    public Integer getIs_use_big_log()
    {
        return is_use_big_log;
    }

    public void setIs_use_big_log(Integer is_use_big_log)
    {
        this.is_use_big_log = is_use_big_log;
    }

    public String getCms_def_author()
    {
        return cms_def_author;
    }

    public void setCms_def_author(String cms_def_author)
    {
        this.cms_def_author = cms_def_author;
    }

    public String getMail_reciver()
    {
        return mail_reciver;
    }

    public void setMail_reciver(String mail_reciver)
    {
        this.mail_reciver = mail_reciver;
    }

    public Integer getIs_use_validate()
    {
        return is_use_validate;
    }

    public void setIs_use_validate(Integer is_use_validate)
    {
        this.is_use_validate = is_use_validate;
    }

    public String getOtc_deposit_currency()
    {
        return otc_deposit_currency;
    }

    public void setOtc_deposit_currency(String otc_deposit_currency)
    {
        this.otc_deposit_currency = otc_deposit_currency;
    }

    public BigDecimal getOtc_deposit_volume()
    {
        return otc_deposit_volume;
    }

    public void setOtc_deposit_volume(BigDecimal otc_deposit_volume)
    {
        this.otc_deposit_volume = otc_deposit_volume;
    }

    @Override
    public String toString()
    {
        String result = "";
        try
        {
            result = JsonUtil.beanToJson(this);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
