<template>
  <div class="container">
    <div class="title">
      <p>我的广告</p>
      <div class="right_container">
         <div>
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
    </div>
  </div>
</template>
<script>
export default {
  props: {
    coinList: Array,
  },
  data() {
    return {
      typeList: [
        {
          label: '全部',
          value: '',
        },
        {
          label: '购买',
          value: 'BUY',
        },
        {
          label: '出售',
          value: 'SELL',
        },
      ],
      type: '',
      coin: '',
    };
  },
  methods: {
    coinChange() {
      this.$emit('coin-change', this.coin);
    },
    typeChange() {
      this.$emit('type-change', this.type);
    },
  },
};
</script>
<style scoped>
.title{
  width: 1200px;
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center
}
.title p{
  font-size: 24px;
  line-height: 24px;
  color: #1e2629;
}
.right_container{
  display: flex;
  justify-content: space-between;
}
</style>
<style scoped>
.ivu-select {
  margin:0 5px 0 5px;
}
</style>
