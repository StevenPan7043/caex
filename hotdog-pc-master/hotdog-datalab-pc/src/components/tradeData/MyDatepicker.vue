<template>
  <div class="my-date">
    <span class="time">时间</span>
    <!-- <el-date-picker v-model="startTime" type="datetime" placeholder="选择日期" size="small" value-format="yyyy-MM-dd-HH-mm-ss"></el-date-picker> -->
    <el-date-picker v-model="startTime" type="datetime" size="small" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
    <span class="deliver">--</span>
    <!-- <el-date-picker
      v-model="endTime"
      type="date"
      placeholder="选择日期"
      size="small"
      value-format="yyyy-MM-dd"
    ></el-date-picker> -->
    <el-date-picker v-model="endTime" type="datetime" size="small" placeholder="选择时间" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
  </div>
</template>
<script>
export default {
  data() {
    return {
      startTime: "",
      endTime: ""
    };
  }
};
</script>
<style lang="scss" scoped>
@import "~/css/tradeData/myDatepicker.scss";
</style>