<template>
  <div class="page-download-transit">
    <dl>
      <img src="../assets/image/logo.png" alt="Hotdog" />
      <dt>Hotdog Global App</dt>
      <dd>
        <el-button class="buttonq" type="primary" @click="download">点击安装</el-button>
        <span>点击安装后，请返回桌面查看</span>
      </dd>
    </dl>
    <p>
      安装完成后，请到:
      <br />
      <em>「设置」⇀「通用」⇀「描述文件与设备管理」</em>
      <br />找到HotdogApp的描述文件，设置为信任。
    </p>
    <div class="line">
      弹出“
      <em>未受信的企业级开发者</em>
      ”怎么办？
    </div>
    <img src="../assets/image/image_4.png" class="img margin" />
    <div class="number">1</div>
    <img src="../assets/image/image_5.png" class="img margin" />
    <div class="number">2</div>
    <div class="line">
      找到带有Hotdog标识的描述文件并点击“
      <em>信任</em>
      ”
    </div>
    <img src="../assets/image/image_3.png" class="img" />
    <div class="line">
      点击“
      <em>信任</em>
      ”，设置完成后就可以使用Hotdog了
    </div>
    <img src="../assets/image/image_1.png" class="img" />
    <div class="open-browser"></div>
  </div>
</template>

<script>
export default {
  methods: {
    download() {
      var u = navigator.userAgent;
      var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
      if (isIOS) {
        location.href =
          "itms-services://?action=download-manifest&url=https://www.hotdogvip.com/downapp/hotdog.plist";
      }
    },
  },
};
</script>

<style scoped>
@import "../assets/css/download.css";
</style>