<template>
  <div class="page-download">
    <section class="s1">
      <img src="../assets/image/logo.png" alt="Hotdog" />
      <p class="h1">
        全球领先的
        <br />数字资产交易平台
      </p>
      <div class="phone white zh-cn"></div>
    </section>
    <section class="s3">
      <dl>
        <dt class="zh-cn">国际化交易平台</dt>
        <dd>丰富的币种及币种信息，提供币币、合约、法币交易等多种交易方式，专业的盘口和深度。</dd>
      </dl>
      <div class="phone white zh-cn">
        <div class="s3-icon"></div>
      </div>
    </section>
    <section class="s4">
      <dl>
        <dt>法币交易</dt>
        <dd>
          使用法定货币购买数字资产
          <br />快速进入数字资产的世界
        </dd>
      </dl>
      <div class="phone white zh-cn">
        <div class="rect"></div>
      </div>
    </section>
    <section class="s5">
      <dl>
        <dt class="zh-cn">专业的行情机制</dt>
        <dd>实时动态的行情数据、精准的k线图，专业的指标自定义工具，更清晰的掌握行情趋势。</dd>
      </dl>
      <div class="phone black zh-cn"></div>
    </section>
    <div class="footer-download show">
      <el-button class="buttonq" type="primary" @click="download" v-if="!isIos">本&shy;地&shy;下&shy;载</el-button>
      <el-button class="buttonq" type="primary" @click="download" v-if="isIos">
        <img src="../assets/image/iospg.png" />
      </el-button>
    </div>
    <div class="open-browser">
      <p>
        请点击右上角···
        <br />选择“在浏览器中打开”
      </p>
    </div>
  </div>
</template>

<script>
import {
  isMobile,
  isIOS,
  isAndroid,
  mobileModel,
  getUA,
} from "mobile-device-detect";
export default {
  data() {
    return {
      isIos: isIOS,
    };
  },
  mounted() {
    const m = document.querySelector(".open-browser");
    if (isMobile) {
      if (isIOS) {
        if (mobileModel == "iPhone" && getUA.indexOf("MicroMessenger") != -1) {
          m.classList.add("show");
        }
      } else if (isAndroid && getUA.indexOf("MicroMessenger") != -1) {
        m.classList.add("show");
      }
    }
  },
  methods: {
    download() {
      if (isMobile) {
        if (isIOS) {
          if (mobileModel == "iPhone") {
            this.$router.push("/testflight");
          }
        } else if (isAndroid) {
          window.location.href = "https://www.hotdogvip.com/downapp/hotdog.apk";
        }
      }
    },
  },
};
</script>

<style scoped>
@import "../assets/css/home.css";
</style>


