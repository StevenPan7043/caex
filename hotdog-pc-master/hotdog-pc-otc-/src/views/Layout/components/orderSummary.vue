<template>
  <div class="order_container">
    <div class="header_container">
      <p>进行中的订单</p>
    </div>
    <div class="item_container">
        <div style="width:310px; cursor:pointer" >
          <div class="item" v-for="item in rows" :key="item.id" @click="toOrderDetail(item)" >
            <div class="start"  :style="{backgroundColor:item.color}">{{item.opposite_nick_name[0]}}</div>
            <div class="mid">
              <p class="first">{{item.tType==='BUY'?'购买':'出售'}}{{item.baseCurrency}}</p>
              <p class="second">{{item.status|statusFilter}}</p>
            </div>
            <div class="end">
              <p class="first">总价：{{(item.volume*item.price).toFixed(2)}} {{item.quoteCurrency}}</p>
              <p class="second flex_container" >剩余：
                <count-down class="count_down" :startTime=item.startTime :endTime=item.endTime ></count-down>
              </p>
            </div>
          </div>
        </div>
    </div>
    <div class="footer_container">
      <p @click="toOrder">全部订单</p>
    </div>
  </div>
</template>
<script>
import CountDown from 'vue2-countdown';

export default {
  props: {
    rows: Array,
  },
  components: {
    CountDown,
  },
  data() {
    return {
      distance: -10,
    };
  },
  filters: {
    statusFilter(val) {
      switch (val) {
        case 'NP':
          return '待支付';
        case 'UNCONFIRMED':
          return '已支付';
        case 'DONE':
          return '已完成';
        case 'CANCELED':
          return '已取消';
        default:
          return '';
      }
    },
    formatDuring(mss) {
      const hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = ((mss % (1000 * 60)) / 1000).toFixed(0);
      return `${hours} : ${minutes} : ${seconds}`;
    },
  },
  methods: {
    toOrder() {
      this.$router.push('/order');
    },

    toOrderDetail(item) {
      localStorage.setItem('order-id', item.id);
      this.$router.push({ name: 'orderDetail', params: { id: item.id } });
      window.location.reload();
    },
  },
};
</script>

<style scoped>
.order_container{
  width: 350px;
  height: 520px;
  display: flex;
  flex-direction: column;
}
.order_container .header_container{
  display: flex;
  justify-content: space-between;
  padding: 0 30px;
  box-sizing: border-box;
  align-items: center;
  height: 60px;
  border-bottom:1px solid #c8c8c8;
}
.order_container .header_container p{
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  font-weight: 500
}
.order_container .header_container div{
  cursor: pointer;
  font-size: 14px;
  line-height: 14px;
  color: #3b78c3;
}
.order_container .item_container{
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: auto
}

.order_container .footer_container{
  height: 60px;
  display: flex;
  justify-content: flex-end;
  margin-right: 30px;
  align-items: center;
  border-top:1px solid #c8c8c8;
}
.order_container .footer_container p{
  font-size: 16px;
  color: #3b78c3;
  cursor: pointer;
}
.item{
  width: 310px;
  height: 80px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #dcdcdc
}
.start{
  width: 46px;
  height: 46px;
  border-radius: 50%;
  font-size: 16px;
  line-height: 46px;
  text-align: center;
  color: #ffffff
}
.mid{
  display: flex;
  flex-direction: column;
  margin-left: 25px;
}
.mid .first{
  color: #149664;
  font-size: 14px;
  line-height: 14px;
  margin-bottom: 9px;
}
.mid .second{
  font-size: 14px;
  line-height: 14px;
  color: #333333;
}
.end{
  display: flex;
  flex-direction: column;
  margin-left: 17px;
}
.end .first{
  color: #999999;
  font-size: 14px;
  line-height: 14px;
  margin-bottom: 9px;
}
.end .second{
  font-size: 14px;
  line-height: 14px;
  color: #333333;
}
.flex_container{
  display: flex
}
</style>
