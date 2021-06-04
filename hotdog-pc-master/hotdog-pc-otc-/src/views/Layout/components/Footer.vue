<template>
  <div>
      <div class="home-footer">
    <div class="footer-content f-cb">
      <div class="footer-left f-fl">
        <img class="logoFoot" src="@/assets/images/footer-logo.png" alt="" style="width:191px;height: 32px">
        <p class="logo-desc">全球领先数字资产交易平台</p>
        <p class="iconWrap">
          <a href="">
            <img src="@/assets/images/tw01.png" alt="">
          </a>
          <a href="">
            <img src="@/assets/images/tw02.png" alt="">
          </a>
          <a href="">
            <img src="@/assets/images/tw03.png" alt="">
          </a>
           <a @mouseenter="wxImg=true" @mouseleave="wxImg=false">
            <img src="@/assets/images/tw04.png" alt="">
            <img class="KE" v-show="wxImg" src="@/assets/images/wxcode.png" />
          </a>

        </p>
      </div>
      <div class="footer-right f-fr">
        <dl>
          <dt>关于</dt>
          <dd>
            <a  target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960654-%E5%85%B3%E4%BA%8E%E6%88%91%E4%BB%AC">关于我们</a>
          </dd>
          <dd>
            <a  target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960694-%E8%81%94%E7%B3%BB%E6%88%91%E4%BB%AC">联系我们</a>
          </dd>
        </dl>
        <dl>
          <dt>服务</dt>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004079614-%E5%B8%AE%E5%8A%A9%E4%B8%AD%E5%BF%83">帮助中心</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/categories/360004132653-%E5%85%AC%E5%91%8A%E4%B8%AD%E5%BF%83">重要公告</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960474-%E8%B4%B9%E7%8E%87%E8%AF%B4%E6%98%8E">费率说明</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/requests/new">提交工单</a>
          </dd>
        </dl>
        <dl>
          <dt>支持</dt>
          <dd>
            <a target="blank" href="http://hk.mikecrm.com/2HX2n2I">上币申请</a>
          </dd>
          <dd>
            <a target="blank" href="">API文档</a>
          </dd>
          <dd>
            <a target="blank" href="https://Hotdogvip.com/#/downloadPage">APP下载</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744553-%E6%B3%95%E5%B8%81%E5%95%86%E5%AE%B6%E7%94%B3%E8%AF%B7">法币商家申请</a>
          </dd>
        </dl>
        <dl>
          <dt>条款</dt>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050743293-%E7%94%A8%E6%88%B7%E5%8D%8F%E8%AE%AE">用户协议</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049959654-%E9%9A%90%E7%A7%81%E6%9D%A1%E6%AC%BE">隐私条款</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050744173-%E5%85%8D%E8%B4%A3%E5%A3%B0%E6%98%8E">免责声明</a>
          </dd>
          <dd>
            <a target="blank" href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049960114-%E6%B3%95%E5%B8%81%E4%BA%A4%E6%98%93%E8%A7%84%E5%88%99">法币交易细则</a>
          </dd>
        </dl>
      </div>
    </div>
  </div>
    <div class="footerBot">© 2019 - 2020 Hotdogvip.com All Rights Reserved</div>
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
    position: relative;
    top: 10px;
    left: -30px;
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
