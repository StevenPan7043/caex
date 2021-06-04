<template>
  <div class="container">
    <div class="title">
      <p>资产明细</p>
      <div class="right_container">
        <Select
          v-model="coin"
          style="width:160px"
          placeholder="全部币种"
          @on-change="coinChange"
        >
          <Option
            v-for="item in coinList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
        <div style="width:20px;"></div>
        <Select
          v-model="type"
          style="width:160px"
          placeholder="交易类型"
          @on-change="typeChange"
        >
          <Option
            v-for="item in typeList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
      </div>
    </div>
    <div>
      <detail-header class="detail-header"/>
      <div class="split-line" ></div>
      <div class="table_container">
        <div v-for="item in accountDetail" :key="item.id" >
          <detail-item
            :time="item.create_time"
            :type="item.proc_type"
            :coin="item.currency"
            :number="item.num"
            />
          <div class="split-line" ></div>
        </div>
      </div>
    </div>
    <div class="page_container">
      <Page :total="total" :page-size="pageSize" @on-change="pageChange" />
    </div>
  </div>
</template>
<script>
import DetailItem from './components/detailItem.vue';
import DetailHeader from './components/detailHeader.vue';
import { getCurrencyList } from './api';

export default {
  components: {
    DetailItem,
    DetailHeader,
  },
  props: {
    accountDetail: Array,
    total: Number,
    pageSize: Number,
  },
  data() {
    return {
      coinList: [],
      typeList: [
        { label: '全部', value: 'all' },
        { label: '买入', value: 'ADDTOTAL' },
        { label: '卖出', value: 'REDUCEFROZEN' },
        { label: '转入', value: 'INTO' },
        { label: '转出', value: 'OUT' },
      ],
      coin: 'all',
      type: 'all',
    };
  },
  created() {
    this.pageInit();
  },
  methods: {
    pageInit() {
      getCurrencyList().then(({ data }) => {
        const coinList = [{ label: '全部币种', value: 'all' }];
        data.forEach((ele) => {
          coinList.push({
            label: ele,
            value: ele,
          });
        });
        this.coinList = coinList;
      });
    },
    coinChange() {
      this.$emit('coin-change', this.coin);
    },
    typeChange() {
      this.$emit('type-change', this.type);
    },
    pageChange(page) {
      this.$emit('page-change', page);
    },
  },
};
</script>

<style scoped>
.container{
  width: 1200px;
}
.title{
  width: 1200px;
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center
}
.title p{
  font-size: 18px;
  color: #333333;
}
.right_container{
  display: flex;
  justify-content: space-between;
}
.split-line{
  width: 1200px;
  height: 1px;
  background-color: #eeeeee;
}

.detail-header{
  margin-top: 18px;
}
.page_container{
  width:1200px;
  margin-top: 60px;
  display: flex;
  justify-content: center;
  margin-bottom: 182px;
}
.table_container{
  height: 500px;
}
</style>
