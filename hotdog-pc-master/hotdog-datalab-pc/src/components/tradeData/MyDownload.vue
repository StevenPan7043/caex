<template>
  <div class="my-download">
    <span class="trade-title" :style="{marginRight:mr+'px'}">{{ desc }}</span>
    <el-dropdown @command="handleClick">
      <span class="el-dropdown-link">
        {{ tType ? tType : list[0]}}
        <i class="el-icon-caret-bottom"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item  v-for="item in list" :key="item" :command="item">{{ item }}</el-dropdown-item>
        <!-- <el-dropdown-item command="sell">sell</el-dropdown-item> -->
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  props: ["desc", "mr",'list'],
  data() {
    return {
      tType:''
    }
  },
  methods: {
    handleClick(val) {
      this.tType = val;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "~/css/tradeData/myDownload.scss";
</style>