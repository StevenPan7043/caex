<template>
  <div class="page-testFlight-guide">
    <h1>
        <img src="../assets/image/logo.png" class="log" alt="">
      iOS内测版安装教程
    </h1>
    <dl>
      <dd>
        <h2 class="list-title">
          <span>第一步</span>
        </h2>
        <p>
          <strong>下载 TestFlight</strong>
        </p>
        <!-- <a href="https://testflight.apple.com/join/5VHZeROM" target="_blank">点击下载 TestFlight</a> -->
        <el-button class="buttonq" type="primary" @click="gotoTestFlight">点击下载 TestFlight</el-button>
        <p>点击页面中的“在 App Store 中查看”去下载</p>
        <img alt="点击页面中的“在App Store中查看”去下载" src="../assets/image/as04.jpeg" />
      </dd>
      <dd>
        <h2 class="list-title">
          <span>第二步</span>
        </h2>
        <p>
          <strong>在 TestFlight 中下载 Hotdog</strong>
        </p>
        <el-button class="buttonq" type="primary" @click="gotoTestFlight">点击下载 Hotdog</el-button>
        <!-- <a href="https://testflight.apple.com/join/5VHZeROM" target="_blank">点击下载 Hotdog</a> -->
        <p>点击页面中的“开始测试”</p>
        <img alt="点击页面中的“开始测试”" src="../assets/image/as03.jpeg" />
      </dd>
      <dd>
        <h2 class="list-title">
          <span>第三步</span>
        </h2>
        <p>点击“安装”</p>
        <img alt="点击“安装”" src="../assets/image/as01.png" />
        <p class="btm">安装完成，打开App即可使用</p>
      </dd>
    </dl>
    <p>后续有版本更新，可以直接点开 TestFlight 安装更新，无需重复以上操作</p>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  methods: {
    gotoTestFlight(){
      window.location.href = 'http://fir.geroinv.com/downapple?randStr=h8ltdoka62';
    }
  },
};
</script>

<style scoped>
@import "../assets/css/testflight.css";
</style>