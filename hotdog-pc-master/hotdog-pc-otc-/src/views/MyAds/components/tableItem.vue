<template>
  <div>
    <div
      class="tab_body_container"
      v-for="item in adsList"
      :key="item.id"
    >
      <div class="flex-start order_number">
        <p>{{item.number}}</p>
      </div>
      <div class="flex-start">
        <p class="buy" :class="item.type==='BUY'?'green':'red'">{{item.type|typeFilter}}</p>
        <p>{{item.baseCurrency}}</p>
      </div>
      <div class="flex-start">
        <p>{{item.volume}}</p>
      </div>
      <div class="flex-start">
        <p>{{(item.minQuote)}}-{{(item.maxQuote)}} {{item.quoteCurrency}}</p>
      </div>
      <div class="flex-start">
        <p>{{item.price}}</p>
      </div>
      <div class="flex-start">
        <p >{{item.doneVolume}}</p>
      </div>
      <div class="flex-start">
        <p>{{item.createTime}}</p>
      </div>
      <div class="flex-start">
        <p :class="item.status==='WATTING'?'underway':'option_cancel'">{{item.status|statusFilter}}
          <span>( {{((item.doneVolume)/(item.volume)*100).toFixed(2)}}% )</span>
        </p>
      </div>
      <div class="flex-end">
        <p v-if="item.status==='WATTING'" class="option" @click="soldOutClick(item.orderId)">下架</p>
        <p v-else class="option_cancelgetOTCOrder">下架</p>
      </div>
    </div>
    <div class="page_container">
      <Page :total="total" @on-change="pageChange"/>
    </div>
  </div>
</template>
<script>
const tradeTypeEnum = {
  BUY: '购买',
  SELL: '卖出',
};
const statusEnum = {
  WATTING: '进行中',
  CANCELED: '已取消',
  TRADING: '交易中',
  DONE: '已完成',
  PC: '部分取消',

};
export default {
  props: {
    adsList: Array,
    total: Number,
  },
  filters: {
    typeFilter(val) {
      return tradeTypeEnum[val];
    },
    statusFilter(val) {
      return statusEnum[val];
    },
  },
  data() {
    return {
      soldOutShow: false,
    };
  },
  methods: {
    pageChange(e) {
      this.$emit('page-change', e);
    },
    soldOutClick(id) {
      const title = '您的广告正在发布中，确认下架该广告';
      this.$Modal.confirm({
        title,
        onOk: () => {
          this.$emit('cancel-order', id);
        },
      });
    },
  },
};
</script>

<style scoped>
.tab_body_container{
  width: 1200px;
  height: 70px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #e1e1e1;
}
.tab_body_container div{
  font-size: 14px;
  color: #999999;
}
.tab_body_container .flex-start{
  width: 12%;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  color: #333333
}
.tab_body_container .flex-end{
  width: 4%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.buy{
  margin-right: 5px;
}
.green{
   color: #149664 ;
}
.red{
  color: #f22e58 ;
}
.sell{
  color: #f22e58 ;
  margin-right: 5px;
}
.page_container{
  width:1200px;
  margin-top: 60px;
  display: flex;
  justify-content: center;
  height: 117px;
}
.option{
  color: #387ffa;
  cursor: pointer;
}
.option_cancel{
  color: #999;
}
.underway{
  color: #3475e9;
}

</style>
