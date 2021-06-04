<template>
  <div class="supfoot">
    <div class="placeholder">
      <div class="supfootleft">
        <span>© 2019 support.zzexvip.com</span>
      </div>
      <div class="supfootright">
        <span>
          <a href="mailto:info@zzex.pro">info@zzex.pro</a>
        </span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  }
};
</script>
<style lang="scss" scoped>
.supfoot {
  position: relative;
  background-color: #f4f4f4;
}
.supfoot .placeholder {
  height: 60px;
  max-width: 1200px;
  min-width: 1200px;
  margin: 0 auto;
  color: #999999;
  font-size: 14px;
}
.supfoot .placeholder .supfootleft {
  width: 20%;
  text-align: left;
  float: left;
  line-height: 60px;
}
.supfoot .placeholder .supfootright {
  width: 20%;
  text-align: right;
  float: right;
  line-height: 60px;
}
.supfoot .placeholder .supfootright span a {
  color: #999999;
}
</style>
