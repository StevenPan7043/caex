<template>
  <div class="tab-container">
    <div class="left-container">
      <p :class="selectTab===0?'cur':''" @click="tabClick(0)">进行中</p>
      <p :class="selectTab===2?'cur':''" @click="tabClick(2)">申诉中</p>
      <p :class="selectTab===1?'cur':''" @click="tabClick(1)">已完成</p>
      <p :class="selectTab===3?'cur':''" @click="tabClick(3)">已取消</p>
      <p :class="selectTab===4?'cur':''" @click="tabClick(4)">全部</p>
    </div>
    <div class="right-container">
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
</template>
<script>
export default {
  props: {
    coinList: Array,
    typeList: Array,
  },
  data() {
    return {
      selectTab: 0,
      coin: 'all',
      type: 'all',
    };
  },
  methods: {
    tabClick(index) {
      this.selectTab = index;
      let name = '';
      switch (index) {
        case 0:
          name = 'WATTING';
          break;
        case 1:
          name = 'DONE';
          break;
        case 2:
          name = 'Complaining';
          break;
        case 3:
          name = 'CANCELED';
          break;
        case 4:
          name = '';
          break;
        default:
          break;
      }
      this.$emit('tab-click', name);
    },
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
.tab-container{
  width: 1200px;
  height: 32px;
  display: flex;
  justify-content: space-between;
}
.left-container{
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width: 353px;
}
.left-container p {
  font-size: 16px;
  color: #333333;
  line-height: 32px;
  cursor: pointer;
}
.cur{
  color: #3b78c3 !important;
  border-bottom: 2px solid #3b78c3
}
.ivu-select {
  margin:0 5px 0 5px;
}

</style>
