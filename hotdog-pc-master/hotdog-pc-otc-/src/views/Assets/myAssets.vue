<template>
  <div class="assets_container">
    <div class="assets_title">
      我的资产
    </div>
    <div class="summary-container">
      <p class="title">总资产折合：</p>
      <p class="content">{{totalBTC}} BTC ≈ {{totalZC}} CNY</p>
      <div></div>
      <p class="title">保证金：</p>
      <p class="content">{{cashDeposit}}</p>
    </div>
    <div class="assets-detail">
      <assets-detail
        v-for="(item,index) in lastList"
        :key="index"
        :coinItem="item"
        @roll-click="rollClick"
      />
    </div>
    <!-- 弹框 -->
    <Modal
      v-model="rollShow"
      width="472"
      @on-cancel="closeForm"
    >
      <p
        slot="header"
        class="modal_header"
      >
        <span class="first">资产互转</span>
        <span class="second">提币请划转至币币交易账户</span>
      </p>
      <div>
        <Form
          :model="formTop"
          label-position="top"
        >
          <FormItem label="币种">
            <Input
              v-model="formTop.coin"
              disabled
            />
          </FormItem>
          <div class="flex_container">
            <span>从</span>
            <span>(余额为：{{fromMoney}} {{formTop.coin}} )</span>
          </div>
          <FormItem>
            <Select
              v-model="formTop.from"
              @on-change="fromChange"
            >
              <Option
                v-for="item in accountType"
                :value="item.value"
                :key="item.value"
              >{{ item.label }}</Option>
            </Select>
          </FormItem>
          <div class="flex_container">
            <span>转至</span>
            <span>(余额为： {{toMoney}} {{formTop.coin}} )</span>
          </div>
          <FormItem>
            <Select
              v-model="formTop.to"
              @on-change="toChange"
            >
              <Option
                v-for="item in accountType"
                :value="item.value"
                :key="item.value"
              >{{ item.label }}</Option>
            </Select>
          </FormItem>
          <div class="flex_container">
            <span>数量</span>
            <span>(可划转数量： {{fromMoney}} {{formTop.coin}} )</span>
          </div>
          <FormItem>
            <Input v-model="formTop.num">
            <span
              slot="suffix"
              class="all_btn"
              @click="allMoneyClick"
            >全部</span>
            </Input>
          </FormItem>
        </Form>
        <div class="btn_tool">
          <span @click="closeForm">取消</span>
          <Button
            type="primary"
            @click="submitClick"
          >立即划转</Button>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script>
import AssetsDetail from './components/assetsDetail.vue';
import { accountAddChange, accountSubtChange } from './api';
import { customToFixed } from '@/libs/utils';

export default {
  components: {
    AssetsDetail,
  },
  props: {
    lastList: Array,
    coinList: Array,
    userInfo: Object,
    cashDeposit: String,
    totalBTC: String,
    totalZC: String,
  },
  data() {
    return {
      currencyInfo: {},
      rollShow: false,
      fromMoney: 0,
      toMoney: 0,
      formTop: {
        coin: '',
        from: '',
        to: '',
        num: '',
      },
      accountType: [
        {
          label: '法币账户',
          value: 'fabi',
        },
        {
          label: '币币账户',
          value: 'bibi',
        },
      ],
    };
  },
  methods: {
    rollClick(coinItem) {
      this.rollShow = true;
      this.formTop.coin = coinItem.fabi.currency;
      this.currencyInfo = coinItem;
      this.formTop.from = 'fabi';
      this.formTop.to = 'bibi';
      this.fromMoney = this.currencyInfo.fabi.enable;
      this.toMoney = this.currencyInfo.bibi.enable;
    },
    fromChange(item) {
      if (item === 'fabi') {
        this.fromMoney = this.currencyInfo.fabi.enable
        this.toMoney = this.currencyInfo.bibi.enable
        this.formTop.to = this.accountType[1].value
      } else {
        this.fromMoney = this.currencyInfo.bibi.enable
        this.toMoney = this.currencyInfo.fabi.enable
        this.formTop.to = this.accountType[0].value
      }
    },
    toChange(item) {
      if (item === 'fabi') {
        this.fromMoney = this.currencyInfo.bibi.enable
        this.toMoney = this.currencyInfo.fabi.enable
        this.formTop.from = this.accountType[1].value
        // this.formTop.from = 'bibi';
      } else {
        this.fromMoney = this.currencyInfo.fabi.enable
        this.toMoney = this.currencyInfo.bibi.enable
        this.formTop.from = this.accountType[0].value
        // this.formTop.from = 'fabi';
      }
    },
    allMoneyClick() {
      this.formTop.num = this.fromMoney;
    },
    closeForm() {
      this.formTop = {
        coin: '',
        from: '',
        to: '',
        num: '',
      };
      this.rollShow = false;
    },
    submitClick() {
      if (parseFloat(this.fromMoney) < parseFloat(this.formTop.num)) {
        this.$Message.error({
          content: '资金不足',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (
        this.formTop.from === ''
        || this.formTop.to === ''
        || this.formTop.num === ''
      ) {
        this.$Message.error({
          content: '请完成表单',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (this.formTop.from === this.formTop.to) {
        this.$Message.error({
          content: '请选择正确的地址',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (this.formTop.from === 'bibi') {
        const params = {
          memberId: this.userInfo.uid,
          currency: this.formTop.coin,
          num: this.formTop.num,
        };
        accountAddChange(params).then(({ data, msg, state }) => {
          if (state === 1) {
            this.rollShow = false;
            this.$Message.success({
              content: '划转成功',
              duration: 2,
              closable: true,
            });
            this.closeForm();
            this.$emit('reload-page');
          } else {
            this.closeForm();
            this.$emit('reload-page');
          }
        });
      } else {
        const params = {
          memberId: this.userInfo.uid,
          currency: this.formTop.coin,
          num: this.formTop.num,
        };
        accountSubtChange(params).then(({ data, msg, state }) => {
          if (state === 1) {
            this.rollShow = false;
            this.$Message.success({
              content: '划转成功',
              duration: 2,
              closable: true,
            });
            this.closeForm();
            this.$emit('reload-page');
          } else {
            this.closeForm();
            this.$emit('reload-page');
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.assets_container {
  width: 1200px;
  display: flex;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.1);
  flex-direction: column;
  margin-top: 30px;
}
.assets_title {
  font-size: 30px;
  color: #333333;
  height: 79px;
  line-height: 79px;
  border-bottom: 1px solid #e1e1e1;
  padding-left: 30px;
  box-sizing: border-box;
}
.assets-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.all_btn {
  text-align: center;
  line-height: 32px;
  color: #3b78c3;
  cursor: pointer;
}
.btn_tool {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.btn_tool span {
  margin-right: 35px;
  font-size: 14px;
  line-height: 14px;
  color: #3d414d;
  cursor: pointer;
}
.summary-container {
  width: 1200px;
  height: 18px;
  margin: 70px 0 29px 0;
  padding-left: 35px;
  box-sizing: border-box;
  display: flex;
  line-height: 18px;
}
.summary-container div {
  width: 1px;
  height: 15px;
  background-color: #999999;
  margin: 0 34px 0 34px;
}
.title {
  font-size: 18px;
  color: #999999;
}
.content {
  margin-left: 18px;
  font-size: 18px;
  color: #333333;
}
.ivu-select-selected-value{
  color: red
}
</style>
