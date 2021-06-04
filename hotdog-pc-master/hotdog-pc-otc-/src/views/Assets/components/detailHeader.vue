<template>
  <div class="item_container">
    <div class="first_child">时间</div>
    <div class="middle_child">类型</div>
    <div class="middle_child">币种</div>
    <div class="last_child">数量</div>
  </div>
</template>
<script>
export default {
};
</script>

<style scoped>
.item_container{
  display: flex;
  height: 23px;
  font-size: 14px !important;
}
.first_child{
  width: 25%;
  display: flex;
  align-items: center;
  font-size: 14px !important;
}
.middle_child{
  width: 25%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px !important;
}
.last_child{
  width: 25%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 14px !important;
}
</style>
