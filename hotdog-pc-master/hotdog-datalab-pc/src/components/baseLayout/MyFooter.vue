<template>
  <div>
      <div class="home-footer">
    <div class="footer-content f-cb">
      <div class="footer-left f-fl">
        <img class="logoFoot" src="../../assets/images/logo.png" alt="" style="width: 200px;height: 32px">
        <p class="logo-desc">全球领先数字资产交易平台</p>
            <div class="link-icon">
            <a href="#">
              <i class="iconfont icon-footer-twitter"></i>
            </a>
            <a href="#">
              <i class="iconfont icon-footer-telegram"></i>
            </a>
            <a href="#">
              <i class="iconfont icon-footer-weibo"></i>
            </a>
            <a href="#" class="wechat" >
              <i class="iconfont icon-footer-wechat"></i>
            </a>
            <a href="#">
              <i class="iconfont icon-facebookfacebook52"></i>
            </a>
          </div>
      </div>
      <div class="footer-right f-fr">
        <dl>
          <dt>关于</dt>
          <dd>
            <a  target="blank">关于我们</a>
          </dd>
          <dd>
            <a  target="blank">联系我们</a>
          </dd>
        </dl>
        <dl>
          <dt>服务</dt>
          <dd>
            <a target="blank" >帮助中心</a>
          </dd>
          <dd>
            <a target="blank" >重要公告</a>
          </dd>
          <dd>
            <a target="blank">费率说明</a>
          </dd>
          <dd>
            <a target="blank" >提交工单</a>
          </dd>
        </dl>
        <dl>
          <dt>支持</dt>
          <dd>
            <a target="blank" >上币申请</a>
          </dd>
          <dd>
            <a target="blank" href="">API文档</a>
          </dd>
          <dd>
            <a target="blank" href="">APP下载</a>
          </dd>
          <dd>
            <a target="blank" >法币商家申请</a>
          </dd>
        </dl>
        <dl>
          <dt>条款</dt>
          <dd>
            <a target="blank" >用户协议</a>
          </dd>
          <dd>
            <a target="blank" >隐私条款</a>
          </dd>
          <dd>
            <a target="blank" >免责声明</a>
          </dd>
          <dd>
            <a target="blank" >法币商家申请</a>
          </dd>
        </dl>
      </div>
    </div>
  </div>
    <div class="footerBot">© 2019 - 2020 hotdogvip.com All Rights Reserved</div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      wxImg: false,
    };
  },
};
</script>
<style scoped>
  .f-fl{
    float: left;
    font-size: 12px;
  }
  dd a:hover{
    color: #196bdf;
  }
  .f-fr{
    float: right;
    font-size: 12px;
  }
  .home-footer {
    height: 240px;
    background-color: #08141e;
  }

  .home-footer .footer-content {
    width: 1200px;
    min-width: 1200px;
    margin-left: auto;
    margin-right: auto;
    padding-top: 56px;
  }

  .home-footer .footer-content .footer-left img.logoFoot {
    width: 160px;
    height: 26px;
    display: block;
  }

  .home-footer .footer-content .footer-left .logo-desc {
    margin-top: 9px;
    margin-bottom: 20px;
    font-size: 12px;
    color: #a5b9d5;
  }

  .home-footer .footer-content .footer-left .copy {
    margin-bottom: 21px;
    font-size: 12px;
    color: #a5b9d5;
  }

  .home-footer .footer-content .footer-right dl {
    float: left;
    margin-left: 68px;
  }

  .home-footer .footer-content .footer-right dl dt {
    font-size: 12px;
    color: #d5dff1;

  }

  .home-footer .footer-content .footer-right dl dd {
    margin-top: 12px;
    /*text-align: center;*/
  }

  .home-footer .footer-content .footer-right dl dd a{
    color: #a5b9d5;
    font-size: 12px;
  }

  .home-footer .footer-content .footer-right dl dd.icon-wrap {
    font-size: 20px;
  }

  .home-footer .footer-content .footer-right dl dd img {
    position: relative;
    top: -150px;
    right: 100px;
    float: right;
  }
  .iconWrap{
    display: flex;
    flex-direction: row;
  }
  .iconWrap a{
    margin-right: 20px;
  }
  .iconWrap img{
    width: 16px;
    height: 16px;
    display: block;
  }
  .iconWrap img.KE{
    width: 80px;
    height: 80px;
    display: block;
    margin-left: 28px;
    margin-top: -42px;
  }
  .footerBot{
    width: 100%;
    text-align: center;
    background-color: #08141e;
    height: 50px;
    line-height: 50px;
    border-top:1px solid #1e2a3a;
  }
</style>
