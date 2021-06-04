<template>
  <div class="pulish_container">
    <div class="title_container">
      <p>发布广告</p>
    </div>

    <div class="remind_container">
      <div class="title">
        广告提醒
      </div>
      <ul>
        <li>发布一则广告是免费的。</li>
        <li>您的钱包必须有足够的余额，才能成功发布出售广告。</li>
        <li>您必须在交易广告或交易聊天中提供您的收款详细信息，发起交易后，资金进入平台托管，交易订单价格会锁定。</li>
        <li>所有交流必须在Hotdog上进行，请注意高风险有欺诈风险的付款方式。</li>
      </ul>
    </div>

    <div class="where_container">
      <div class="title">
        我想在（必填）
      </div>
      <div class="right_container">
        <Select
          v-model="formTop.country"
          style="width:180px"
          placeholder="地区"
        >
          <Option
            v-for="item in countryList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
        <span class="specialSpan">以</span>
        <Select
          v-model="formTop.legalTender"
          style="width:180px"
          placeholder="币种"
        >
          <Option
            v-for="item in legalTenderList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
        <span class="normalSpan"></span>
        <Select
          v-model="formTop.tradeType"
          style="width:180px"
        >
          <Option
            v-for="item in tradeTypeList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
        <span class="normalSpan"></span>
        <Select
          v-model="formTop.coin"
          style="width:180px"
          @on-change ='coinChange'
        >
          <Option
            v-for="item in coinList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
      </div>
    </div>

    <div class="price_container">
      <div class="title">
        价格设置（必填）
      </div>
      <div class="right_container">
        <Select
          v-model="formTop.priceType"
          style="width:180px"
        >
          <Option
            v-for="item in priceTypeList"
            :value="item.value"
            :key="item.value"
          >
            {{ item.label }}
          </Option>
        </Select>
        <span class="normalSpan"></span>
        <Input
          v-model="formTop.price"
          style="width:230px"
          placeholder="您的报价"
          @on-blur="priceBlur"
        >
        <span
          slot="append"
          style="width:50px"
        >CNY</span>
        </Input>
        <div class="reference_price">参考价格：{{referencePrice}} CNY</div>
      </div>
    </div>

    <div class="limit_container">
      <div class="title">
        交易限额（必填）
      </div>

      <Form
        :model="formTop"
        label-position="top"
        class="right_container"
      >
        <FormItem label="交易数量">
          <Input
            v-model="formTop.num"
            placeholder="请输入你要的交易数量"
            @on-blur="checkHasEnoughMoney"
          />
        </FormItem>
        <FormItem label="最小单笔限额（一次交易的最低交易限制）">
          <Input
            v-model="formTop.lowPrice"
            placeholder="请输入交易最小限额"
            @on-keyup="formTop.lowPrice=formTop.lowPrice.replace(/^(0+)|[^\d]+/g,'')"
          >
          <span
            slot="append"
            style="width:50px"
          >CNY</span>
          </Input>
        </FormItem>
        <FormItem label="最大单笔限额（一次交易的最高交易限制）">
          <Input
            v-model="formTop.highPrice"
            placeholder="请输入交易最大限额"
            @on-keyup="formTop.highPrice=formTop.highPrice.replace(/^(0+)|[^\d]+/g,'')"
          >
          <span
            slot="append"
            style="width:50px"
          ><span
              @click="setMaxPrice"
              class="curor"
            >最大</span> | CNY</span>
          </Input>
        </FormItem>
        <FormItem label="付款期限">
          <Select
            v-model="formTop.timeLimit"
            style="width:300px"
          >
            <Option
              v-for="item in timeLimitList"
              :value="item.value"
              :key="item.value"
            >
              {{ item.label }}
            </Option>
          </Select>
        </FormItem>
        <FormItem label="失效时间">
          <Select
            v-model="formTop.deadTime"
            style="width:300px"
          >
            <Option
              v-for="item in deadTimeList"
              :value="item.value"
              :key="item.value"
            >
              {{ item.label }}
            </Option>
          </Select>
        </FormItem>
      </Form>

    </div>

    <div class="payment_container">
      <div class="title">
        交易方式
      </div>
      <div class="right_container">
        <CheckboxGroup v-model="formTop.accountGroup">
          <div
            v-for="item in accountInfo"
            :key="item.id"
          >
            <Checkbox
              :label="item.id"
              class="container"
            >
              <i
                class="iconfont icon-yinhangqia"
                v-if="item.type==='BANK'"
                style="color: #768db0;font-size: 22px;"
              ></i>
              <i
                class="iconfont icon-zhifubao"
                v-if="item.type==='ALIPAY'"
                style="color: #39a9ed;font-size: 22px;"
              ></i>
              <i
                class="iconfont icon-weixindenglu"
                v-if="item.type==='WXPAY'"
                style="color: #3baf34;font-size: 22px;"
              ></i>
              <p>{{item.type|typeFilter}}</p>
              <p>{{item.account}} {{item.name}} <span v-show="item.type==='BANK'">{{item.bankOrImg}}</span></p>
            </Checkbox>
          </div>
        </CheckboxGroup>

      </div>
    </div>

    <div class="message_container">
      <div class="title">
        交易留言
      </div>
      <div class="right_container">
        <Input
          v-model="formTop.mark"
          type="textarea"
          :autosize="{minRows: 8,maxRows: 8}"
          style='width:939px'
        />
      </div>
    </div>

    <div class="pwd_container">
      <div class="title">
        资金密码
      </div>
      <div class="right_container">
        <Input
          type="password"
          v-model="formTop.pwd"
          style="width: 300px"
        />
        <Button
          class="submit_btn"
          type="info"
          @click="submitClick"
          :loading="sbumitBtnLoading"
        >
          立即发布
        </Button>
      </div>
    </div>

  </div>
</template>
<script>
import {
  publishOTCOrder,
  getMember,
  getOTCList,
  getAccountId,
  getMoneyByCoinType,
  getAllTicker,
} from './api';
import { logoutApi } from '@/api/GetCode';

import { getUsdtPrice } from '../Assets/api/index';

export default {
  data() {
    return {
      countryList: [{ label: 'CN中国', value: 'CN' }],
      legalTenderList: [{ label: 'CNY', value: 'CNY' }],
      tradeTypeList: [
        { label: '购买', value: 'BUY' },
        { label: '出售', value: 'SELL' },
      ],
      coinList: [],

      priceTypeList: [],
      timeLimitList: [
        { label: '15分钟', value: '15' },
        { label: '30分钟', value: '30' },
        { label: '45分钟', value: '45' },
      ],
      deadTimeList: [
        { label: '3天', value: '3' },
        { label: '5天', value: '5' },
        { label: '10天', value: '10' },
        { label: '15天', value: '15' },
      ],

      formTop: {
        memberId: '',
        country: 'CN',
        legalTender: 'CNY',
        tradeType: 'BUY',
        coin: 'USDT',
        priceType: 'fixed',
        price: '',
        num: '',
        lowPrice: '',
        highPrice: '',
        timeLimit: '30',
        deadTime: '5',
        accountGroup: [],
        mark: `购买提示：1、请使用本人实名制账户付款；
                  2、务必在规定付款时间内支付，并点击“已付款” 否则订单失效。
                  3、付款完成后如果30分钟内未收到商家放币，请联系电话：188******** 微信同手机号。`,
        pwd: '',
      },
      sbumitBtnLoading: false,
      accountInfo: [],
      // 此账号拥有的钱币
      hasMoney: 0,
      // 参考价格
      referencePrice: 0,
    };
  },
  filters: {
    typeFilter(val) {
      switch (val) {
        case 'BANK':
          return '银行卡';
        case 'ALIPAY':
          return '支付宝';
        case 'WXPAY':
          return '微信';
        default:
          return '';
      }
    },
  },
  created() {
    this.getAccountInfo();
    getMember().then(({ msg, data }) => {
      if (msg === 'LANG_NO_LOGIN') {
        this.logout();
      } else {
        this.formTop.memberId = data.uid;
      }
    });
    getOTCList().then(({ data }) => {
      const OTCList = [];
      data.forEach((ele) => {
        OTCList.push({ label: ele, value: ele });
      });
      this.coinList = OTCList;
    });
  },
  mounted() {
    getUsdtPrice().then((res) => {
      [this.referencePrice] = res.USDT_CNY;
    });
    this.priceTypeList = [{ label: '固定价格', value: 'fixed' }, { label: '浮动价格', value: 'float' }];

    // const key = 'usdtzc_ticker';
    // // const key = 'usdtzc_ticker';
    // getAllTicker().then(({ data }) => {
    //   const value = data[key];
    //   this.referencePrice = value && value.close;
    //   if (this.referencePrice !== 0) {
    //     this.priceTypeList = [{ label: '固定价格', value: 'fixed' }, { label: '浮动价格', value: 'float' }];
    //   } else {
    //     this.priceTypeList = [{ label: '固定价格', value: 'fixed' }];
    //   }
    // });
  },
  methods: {
    coinChange() {
      if (this.formTop.coin.toLowerCase() === 'zc') {
        this.referencePrice = 1;
      } else {
        const key = `${this.formTop.coin.toLowerCase()}zc_ticker`;
        // const key = 'usdtzc_ticker';
        this.referencePrice = 0;
        getAllTicker().then(({ data }) => {
          const value = data[key];
          this.referencePrice = value.close;
        });
      }
    },
    setMaxPrice() {
      if (this.formTop.price && this.formTop.num) {
        this.formTop.highPrice = parseInt(this.formTop.price * this.formTop.num, 0);
      }
    },
    priceBlur() {
      if (this.formTop.coin === 'ZC') {
        this.clearNoNum(3);
      } else {
        this.clearNoNum(2);
      }
      if (this.formTop.price && this.formTop.num) {
        this.formTop.highPrice = parseInt(this.formTop.price * this.formTop.num, 0);
      }
    },

    clearNoNum(num) {
      this.formTop.price = this.formTop.price.replace(/[^\d.]/g, ''); // 清除“数字”和“.”以外的字符
      this.formTop.price = this.formTop.price.replace(/\.{2,}/g, '.'); // 只保留第一个. 清除多余的
      this.formTop.price = this.formTop.price.replace('.', '$#$').replace(/\./g, '').replace('$#$', '.');
      if (num === 3) {
        this.formTop.price = this.formTop.price.replace(/^(\-)*(\d+)\.(\d\d\d).*$/, '$1$2.$3');// 只能输入3个小数
      } else {
        this.formTop.price = this.formTop.price.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');// 只能输入两个小数
      }
      if (this.formTop.price.indexOf('.') < 0 && this.formTop.price !== '') { // 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
        this.formTop.price = parseFloat(this.formTop.price);
      }
    },
    submitClick() {
      const tradeTotal = this.formTop.num * this.formTop.price;
      if (tradeTotal < parseFloat(this.formTop.highPrice)) {
        this.$Message.error({
          content: '最大交易额不能超过交易总额',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (tradeTotal < parseFloat(this.formTop.lowPrice)) {
        this.$Message.error({
          content: '最小交易额不能超过交易总额',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (
        this.formTop.accountGroup.length === 0
        || !this.formTop.num
        || !this.formTop.lowPrice
        || !this.formTop.highPrice
        || !this.formTop.pwd
      ) {
        this.$Message.error({
          content: '请完成表单',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (parseFloat(this.formTop.lowPrice) < 100) {
        this.$Message.error({
          content: '最小交易限额不能低于100CNY',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.sbumitBtnLoading = true;
      const acountId = this.formTop.accountGroup.join(',');
      const params = {
        baseCurrency: this.formTop.coin,
        quoteCurrency: this.formTop.legalTender,
        type: this.formTop.tradeType,
        price: this.formTop.price,
        volume: this.formTop.num,
        minQuote: this.formTop.lowPrice,
        maxQuote: this.formTop.highPrice,
        paymentTime: this.formTop.timeLimit,
        effectiveTime: this.formTop.deadTime,
        remark: this.formTop.mark,
        acountId,
        securityPwd: this.formTop.pwd,
        priceChangeType: this.formTop.priceType === 'fixed' ? 'UNCHANGE' : 'FLOAT',
      };
      publishOTCOrder(params).then(({ state }) => {
        this.sbumitBtnLoading = false;
        if (state === 1) {
          this.$Message.success({
            content: '发布成功',
            duration: 2,
            closable: true,
          });
          this.formTop = {
            memberId: '',
            country: 'CN',
            legalTender: 'CNY',
            tradeType: 'BUY',
            coin: 'USDT',
            priceType: 'fixed',
            price: '',
            num: '',
            lowPrice: '',
            highPrice: '',
            timeLimit: '30',
            deadTime: '5',
            mark: `购买提示：1、请使用本人实名制账户付款；
                  2、务必在规定付款时间内支付，并点击“已付款” 否则订单失效。
                  3、付款完成后如果30分钟内未收到商家放币，请联系电话：188******** 微信同手机号。`,
            pwd: '',
          };
          this.$router.push('/myAds');
        }
      });
    },
    logout() {
      this.$Message.error({
        content: '请先登录',
        duration: 2,
        closable: true,
      });
      const loginState = false;
      localStorage.removeItem('userInfo');
      localStorage.removeItem('useruid');
      localStorage.removeItem('nickName');
      localStorage.removeItem('mNameHidden');
      localStorage.setItem('loginState', loginState);
      this.$store.commit('CLEAR_USERINFO');
      this.$router.push('/login');
      logoutApi();
    },


    // 获取收款方式
    getAccountInfo() {
      getAccountId().then(({ data }) => {
        const accountInfo = [];
        data.rows.forEach((ele) => {
          if (ele.isDelete === 'NO') {
            accountInfo.push(ele);
          }
        });
        this.accountInfo = accountInfo;
      });
    },
    // 检查是否有足够的资金
    checkHasEnoughMoney() {
      if (this.formTop.tradeType === 'BUY') {
        if (this.formTop.price && this.formTop.num) {
          this.formTop.highPrice = parseInt(this.formTop.price * this.formTop.num, 0);
        }
        return;
      }
      const params = {
        memberId: this.formTop.memberId,
        currency: this.formTop.coin,
      };
      getMoneyByCoinType(params).then(({ data }) => {
        if (data === null) {
          this.$Message.error({
            content: '资金不足',
            duration: 2,
            closable: true,
          });
          this.formTop.num = null;
        }
        this.hasMoney = parseFloat(data.totalBalance) - parseFloat(data.frozenBalance);
        if (this.hasMoney < this.formTop.num) {
          this.$Message.error({
            content: '资金不足',
            duration: 2,
            closable: true,
          });
          this.formTop.num = null;
        } else if (this.formTop.price && this.formTop.num) {
          this.formTop.highPrice = parseInt((this.formTop.price * this.formTop.num).toFixed(0), 0);
        }
      });
    },
  },
};
</script>

<style scoped>
.pulish_container {
  width: 1200px;
  /* height: 1504px; */
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 30px 0 65px 0;
}
.title_container {
  width: 1140px;
  height: 60px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #333333;
  line-height: 18px;
}

.remind_container {
  width: 1140px;
  height: 165px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
}
.remind_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  height: 86px;
  width: 200px;
}
.remind_container li {
  font-size: 14px;
  color: #666666;
  line-height: 24px;
}

.where_container {
  width: 1140px;
  height: 115px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
}
.where_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  width: 200px;
}
.where_container .right_container {
  display: flex;
  align-items: center;
}
.normalSpan {
  width: 40px;
}
.specialSpan {
  font-size: 14px;
  line-height: 14px;
  color: #3d414d;
  margin: 0 14px 0 14px;
}

.price_container {
  width: 1140px;
  height: 115px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
}

.price_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  width: 200px;
}
.price_container .right_container {
  display: flex;
  align-items: center;
}

.limit_container {
  width: 1140px;
  height: 460px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
}
.limit_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  margin-top: 50px;
  width: 200px;
}
.limit_container .right_container {
  margin-top: 40px;
}

.payment_container {
  width: 1140px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
}
.payment_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  width: 200px;
  margin-top: 40px;
}
.payment_container .right_container {
  margin-top: 40px;
}

.pwd_container {
  width: 1140px;
  height: 260px;
  display: flex;
}
.pwd_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  width: 200px;
  margin-top: 40px;
}
.pwd_container .right_container {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
}

.message_container {
  width: 1140px;
  display: flex;
  height: 280px;
  border-bottom: 1px solid #e1e1e1;
}
.message_container .title {
  font-size: 16px;
  line-height: 16px;
  color: #333333;
  width: 200px;
  margin-top: 40px;
}
.message_container .right_container {
  margin-top: 40px;
  display: flex;
  flex-direction: column;
}
.textarea {
  width: 939px;
  height: 214px;
}
.submit_btn {
  margin-top: 50px;
  width: 480px;
  height: 48px;
  background-color: #39a9ed;
  font-size: 16px;
  color: #ffffff;
  margin-bottom: 77px;
}
.container {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}
.container i {
  margin-left: 11px;
  margin-right: 11px;
}
.container p {
  font-size: 14px;
  color: #333333;
  margin: 0 25px 0 25px;
}
.curor {
  cursor: pointer;
  color: #39a9ed;
}
.reference_price{
  margin-left: 20px;
}
</style>
<style>
.ivu-form-item-label {
  font-size: 14px;
  color: #3d414d;
  margin-bottom: 6px;
}
</style>
