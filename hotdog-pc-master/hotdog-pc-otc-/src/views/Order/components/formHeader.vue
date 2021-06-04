<template>
  <div class="tab_header_container">
    <div class="flex-start" style="width:15.5%">
      <p>订单号</p>
    </div>
    <div class="flex-start" style="width:17%">
      <p>交易类型</p>
    </div>
    <div class="flex-start" style="width:11.5%">
      <p>总价</p>
    </div>
    <div class="flex-start" style="width:11.5%">
      <p>单价</p>
    </div>
    <div class="flex-start" style="width:11.5%">
      <p>手续费</p>
    </div>
    <div class="flex-start" style="width:11.5%">
      <p>时间</p>
    </div>
    <div class="flex-start" style="width:9.5%">
      <p>状态</p>
    </div>
    <div class="flex-start" style="width:9.5%">
      <p>申诉原因</p>
    </div>
    <div class="flex-end">
      <p>交易对象</p>
    </div>
  </div>
</template>
<style scoped>
.tab_header_container{
  width: 1200px;
  height: 26px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e1e1e1;
  margin-top: 60px;
}
.tab_header_container div{
  font-size: 14px;
  color: #999999;
}
.flex-start{
  display: flex;
  justify-content: flex-start;
}
.flex-end{
  width: 11%;
  display: flex;
  justify-content: flex-end
}
</style>
