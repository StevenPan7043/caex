<template>
  <div class="result_container">
    <p>商家申请</p>
    <div class="table_header">
      <div>申请时间</div>
      <div>申请账户</div>
      <div>姓名</div>
      <div>证件号</div>
      <div>保证金</div>
      <div>状态</div>
      <div class="special_item">操作</div>
    </div>
    <div class="table_item">
      <div>{{applInfo.createTime}}</div>
      <div>{{userInfo.m_name}}</div>
      <div>{{realName}}</div>
      <div>{{idNumber}}</div>
      <div>{{applInfo.depositVolume}}{{applInfo.depositCurrency}}</div>
      <div>{{applInfo.status|statusFilter}}</div>
      <div class="special_item">
        <span v-if="applInfo.status==='APPLY_AUDITING'||applInfo.status==='SECEDE_AUDITING'">-</span>
        <span v-if="applInfo.status==='APPLY_PASSED'" @click="quitMerchantClick">退出</span>
        <span v-if="applInfo.status==='APPLY_REJECT'" @click="reApplication">申请</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    applInfo: Object,
    userInfo: Object,
    idNumber: String,
    realName: String,
  },
  filters: {
    statusFilter(val) {
      let name = '';
      switch (val) {
        case 'APPLY_AUDITING':
          name = '审核中';
          break;
        case 'APPLY_PASSED':
          name = '已通过';
          break;
        case 'APPLY_REJECT':
          name = '未通过';
          break;
        case 'SECEDE_AUDITING':
          name = '退商审核中';
          break;
        default:
          break;
      }
      return name;
    },
  },
  methods: {
    reApplication() {
      this.$emit('re-app');
    },
    quitMerchantClick() {
      this.$emit('quit-merchant');
    },
  },
};
</script>
<style scoped>
.result_container{
  width: 1200px;
  box-sizing: border-box;
  padding: 20px 30px 100px 30px
}
.result_container p{
  font-size: 18px;
  line-height: 18px;
  font-weight: 500;
  color: #1e2629;
}
.table_header{
  width: 100%;
  display: flex;
  margin-top: 34px;
  margin-bottom: 26px;
}
.table_header div{
  width: 15%;
  color: #8aa0a6;
}
.table_header .special_item{
  width: 10% !important;
  display: flex;
  justify-content: flex-end;
  color: #8aa0a6;
}
.table_item{
  width: 100%;
  display: flex;
  margin-bottom: 27px;
}
.table_item div{
  width: 15%;
  color: #1e2629;
}
.table_item .special_item{
  width: 10% !important;
  display: flex;
  justify-content: flex-end;
  color: #1e2629;
  cursor: pointer;
}
</style>
