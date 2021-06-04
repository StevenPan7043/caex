<template>
  <div class="tab_header_container">
    <div class="flex-start">
      <p>广告编号</p>
    </div>
    <div class="flex-start">
      <p>交易类型</p>
    </div>
    <div class="flex-start">
      <p>数量</p>
    </div>
    <div class="flex-start">
      <p>交易限额</p>
    </div>
    <div class="flex-start">
      <p>单价</p>
    </div>
    <div class="flex-start">
      <p>成交数量</p>
    </div>
    <div class="flex-start">
      <p>创建时间</p>
    </div>
    <div class="flex-start">
      <p>状态</p>
    </div>
    <div class="flex-end">
      <p>操作</p>
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
  width: 12%;
  display: flex;
  justify-content: flex-start;
}
.flex-end{
  width: 4%;
  display: flex;
  justify-content: flex-end
}
</style>
