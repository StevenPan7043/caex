<template>
  <div>
    <div class="question_style">常见问题</div>
    <Collapse simple>
      <Panel name="1">
        给卖家付款安全吗？
        <p slot="content">当你进入交易时，卖家的数字资产会被锁定在平台的钱包中，因此不必担心给卖家付款。如果卖家在您付款5分钟后还未放行数字资产，点击“申诉’按钮，我们的客服将会介入帮助您。</p>
      </Panel>
      <Panel name="2">
        我什么时候能收到币？
        <p slot="content">若您已完成付款，请务必点击“已付款’按钮，卖家确认收款后，您就会收到数字资产。</p>
      </Panel>
      <Panel name="3">
        转款注意事项
        <p slot="content">1. 在转账过程中 <span class="red">请勿备注类似BTC、USDT等信息</span>，防止造成您汇款被拦截、银行卡被冻结等问题，因此到账延迟，卖方可选择拒绝成交。<br/>
          2. <span class="red">请使用平台实名的银行卡、支付宝等进行转款，</span><br/>
          3. 若交易金额大于50，000CNY，法定假日或工作日17:00以后汇款到银行卡可能造成到账不及时，请分批支付保证及时到账，若未及时到
          账，卖家有权拒绝成交。</p>
      </Panel>
    </Collapse>
  </div>
</template>
<style scoped>
.question_style {
  margin-bottom: 30px;
  font-size: 12px;
  line-height: 12px;
  color: #000000;
  font-weight: 500;
}
.red{
  color: red
}
</style>

<style >
.ivu-collapse {
  border: none !important;
}
.ivu-collapse-item .ivu-collapse-item-active {
  border: none !important;
}
.ivu-collapse-simple
  > .ivu-collapse-item.ivu-collapse-item-active
  > .ivu-collapse-header {
  border: none !important;
}
.ivu-collapse > .ivu-collapse-item > .ivu-collapse-header {
  border: none !important;
}
.ivu-collapse > .ivu-collapse-item {
  border: none !important;
}
.ivu-collapse-content-box p{
  font-size: 14px;
}
.ivu-collapse-header {
  font-size: 14px;
}
</style>
