<template>
  <div>
    <div class="trade_title">{{tradeTitle}}</div>
    <div class="panel_container">
      <div class="title">
        <div class="left_align">
          <p>交易币种</p>
        </div>
        <div class="left_align">
          <p>数量</p>
        </div>
        <div class="left_align">
          <p>限额</p>
        </div>
        <div class="left_align">
          <p>单价</p>
        </div>
        <div class="left_align">
          <p>支付方式</p>
        </div>
        <div class="right_align">
          <p>操作</p>
        </div>
      </div>
      <div class="item" v-for="item in orders" :key="item.id">
        <div class="first_item">
          <img src="../../../assets/images/USDTCoin.png" v-if="item.baseCurrency==='USDT'" >
          <img src="../../../assets/images/BTCCoin.png" v-if="item.baseCurrency==='BTC'">
          <img src="../../../assets/images/ETHCoin.png" v-if="item.baseCurrency==='ETH'">
          <img src="../../../assets/images/ZCCoin.png" v-if="item.baseCurrency==='ZC'">
          <p>{{item.baseCurrency}}</p>
        </div>
        <div class="left_align text_style">
          <p>{{item.remainVolume}} {{item.baseCurrency}}</p>
        </div>
        <div class="left_align text_style">
          <p>{{(item.minQuote)}}-{{(item.maxQuote)}} {{item.quoteCurrency}}</p>
        </div>
        <div class="left_align green_style">
          <p>{{(item.price).toFixed(2)}} {{item.quoteCurrency}}</p>
        </div>
        <div class="left_align">
          <span v-for="(sbuItem,index) in item.accountArray" :key="index" class="icon_container">
            <i class="iconfont icon-yinhangqia" v-if="sbuItem==='BANK'" style="color: #768db0;font-size: 22px;"></i>
            <i class="iconfont icon-zhifubao" v-if="sbuItem==='ALIPAY'" style="color: #39a9ed;font-size: 22px;"></i>
            <i class="iconfont icon-weixindenglu" v-if="sbuItem==='WXPAY'"  style="color: #3baf34;font-size: 22px;"></i>
          </span>
        </div>
        <div class="right_align">
        <Button type="primary" :loading='tradeBtnLoading' class="btn_font_size" @click="btnClick(item)">{{item.type==='SELL'?'购买':'出售'}}{{item.baseCurrency}}</Button>
        </div>
      </div>
    </div>
    <Modal
      v-model="modalFormShow"
      @on-cancel="closeModal"
      width="1200">
      <div class="modal_container">
        <div class="left_container">
          <div class="avatar_container">
            <div class="avatar_style" :style="{backgroundColor:adInfo.color}">{{adInfo.m_nick_name|avatarFilter}}</div>
          </div>
          <div class="middle_container">
            <div class="info_container">
              <div class="left">
                <p class="name">{{adInfo.m_nick_name}}({{adInfo.done_30}} | {{adInfo.completePerecent}})</p>
                <p class="num">数量 {{adInfo.volume}} {{adInfo.baseCurrency}}</p>
              </div>
              <div class="right">
                <p>{{adInfo.price}} {{adInfo.quoteCurrency}}</p>
                <p>{{adInfo.minQuote}}-{{adInfo.maxQuote}} {{adInfo.quoteCurrency}}</p>
              </div>
            </div>
                        <div style="height:10px"></div>
            <div class="flex_container">
              <div class="pay_container" v-for="(item,index) in adInfo.accountArray" :key="index">
                <i class="iconfont icon-yinhangqia" style="color: #768db0;font-size: 22px;" v-if="item ==='BANK'"></i>
                <span class="name"  v-if="item ==='BANK'">银行卡</span>
                <i class="iconfont icon-zhifubao" style="color: #39a9ed;font-size: 22px;"  v-if="item ==='ALIPAY'"></i>
                <span class="name"  v-if="item ==='ALIPAY'">支付宝</span>
                <i class="iconfont icon-weixindenglu" style="color: #3baf34;font-size: 22px;"  v-if="item ==='WXPAY'"></i>
                <span class="name"  v-if="item ==='WXPAY'">微信</span>
              </div>
            </div>
          <p style="color:red;fontSize:10px">请在收到付款短信1小时内确认放行所出售币种，如超过1小时未放行被投诉，将会在二十四小时内禁止进行出售操作。</p>

          </div>
        </div>

        <div class="right_container">
          <div class="header">
            <Input v-model="num" :placeholder="buyNumPlaceholder" style="width: 230px;" >
              <span slot="suffix" class="input_btn">{{adInfo.baseCurrency}}|<span class="all_btn" @click="allIn">全部</span></span>
            </Input>
            <Input v-model="pwd" placeholder="请输入资金密码" style="width: 230px;" type="password" class="second_input"/>
          </div>
          <p class="total_money">交易总额 {{totalCNY}} CNY</p>
          <div class="footer">
            <p>买方付款时限为{{adInfo.paymentTime}}分钟</p>
            <Button class="cancel_btn" type="warning" ghost size="large" @click="closeModal">自动取消({{deadTime}})</Button>
            <Button type="warning" size="large" @click="buyCoin">{{adInfo.type==='BUY'?'出售':'购买'}}{{adInfo.baseCurrency}}</Button>
          </div>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script>
import {
  getAuthindentity, getAccountId, addOTCOrder, getMoneyByCoinType,
} from '../api';
import { customToFixed } from '@/libs/utils';
import { logoutApi } from '@/api/GetCode';

export default {
  props: ['tradeTitle', 'orders'],
  data() {
    return {
      modalFormShow: false,
      adInfo: {},
      buyNumPlaceholder: '',
      num: '',
      pwd: '',
      deadTime: 45,
      timeDown: '',
      tradeBtnLoading: false,
    };
  },
  beforeDestroy() {
    window.clearInterval(this.timeDown);
  },
  filters: {
    avatarFilter(val) {
      if (val) {
        return val.substr(0, 1);
      }
      return '';
    },
    tradeTypeFilter(val) {
      if (val === 'SELL') {
        return '购买';
      }
      return '出售';
    },
  },
  computed: {
    totalCNY() {
      const cashTotal = this.num * this.adInfo.price;
      return customToFixed(cashTotal, 'CNY');
    },
  },
  methods: {
    btnClick(item) {
      this.isLogin = JSON.parse(localStorage.getItem('loginState'));
      // 判断是否登录
      if (this.isLogin) {
        // 判断是否实名
        getAuthindentity().then((res) => {
          if (res.state === 1) {
            if (!res.data.authIdentity) {
              this.$Message.error({
                content: '请先完成实名认证',
                duration: 2,
                closable: true,
              });
              return;
            }
            if (res.data.authIdentity.id_status === 1) {
              // 判断是否绑定收款账号
              getAccountId().then(({ data }) => {
                const accountInfo = [];
                data.rows.forEach((ele) => {
                  if (ele.isDelete === 'NO') {
                    accountInfo.push(ele);
                  }
                });
                if (accountInfo.length < 1) {
                  this.$Message.error({
                    content: '请先添加收款方式',
                    duration: 2,
                    closable: true,
                  });
                } else {
                  if (!this.hasCorrespondPayment(item, accountInfo)) {
                    this.$Message.error({
                      content: '收款方式不匹配',
                      duration: 2,
                      closable: true,
                    });
                    return;
                  }
                  this.adInfo = item;
                  this.buyNumPlaceholder = item.type === 'BUY' ? '请输入出售数量' : '请输入购买数量';
                  this.modalFormShow = true;
                  this.countDown();
                }
              });
            } else {
              this.$Message.error({
                content: '请先完成实名认证',
                duration: 2,
                closable: true,
              });
            }
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
              this.logout();
            }
          }
        });
      } else {
        this.logout();
      }
    },
    countDown() {
      this.timeDown = setInterval(() => {
        this.deadTime = this.deadTime - 1;
        if (this.deadTime === 0) {
          this.modalFormShow = false;
          window.clearInterval(this.timeDown);
          this.deadTime = 45;
          this.num = '';
          this.pwd = '';
        }
      }, 1000);
    },
    allIn() {
      if (this.tradeType === 'SELL') {
        const num = this.adInfo.maxQuote / this.adInfo.price;
        this.num = customToFixed(num, 'USDT');
      } else {
        const num = this.adInfo.maxQuote / this.adInfo.price;

        const currency = this.adInfo.baseCurrency;
        const params = {
          memberId: this.userId,
          currency,
        };
        getMoneyByCoinType(params).then(({ data, msg, state }) => {
          const hasMoney = data.totalBalance - data.frozenBalance;
          const finalNum = Math.min(hasMoney, num);
          this.num = customToFixed(finalNum, 'USDT');
        });
      }
    },
    hasCorrespondPayment(item, ownAccount) {
      const ownAccountArray = [];
      ownAccount.forEach((ele) => {
        ownAccountArray.push(ele.type);
      });
      const sameAccount = item.accountArray.filter(v => ownAccountArray.indexOf(v) !== -1);
      if (sameAccount.length > 0) {
        return true;
      }
      return false;
    },
    buyCoin() {
      if (this.totalCNY < this.adInfo.minQuote) {
        this.$Message.error({
          content: '低于最小交易限额',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (this.totalCNY > this.adInfo.maxQuote) {
        this.$Message.error({
          content: '超过最大交易限额',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.tradeBtnLoading = true;
      const params = {
        oppositeId: this.adInfo.orderId,
        volume: this.num,
        securityPwd: this.pwd,
        price: this.adInfo.price,
      };
      addOTCOrder(params).then(({ data, msg, state }) => {
        this.tradeBtnLoading = false;
        if (state === 1) {
          this.$Message.success({
            content: '交易成功',
            duration: 2,
            closable: true,
          });
          localStorage.setItem('order-id', data.id);
          this.$router.push({ name: 'orderDetail', params: { mark: this.adInfo.remark, id: data.id } });
        }
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      });
    },
    closeModal() {
      this.modalFormShow = false;
      this.num = '';
      this.pwd = '';
      this.adInfo = '';
      window.clearInterval(this.timeDown);
      this.deadTime = 45;
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
  },
};
</script>

<style scoped>
.trade_title{
  font-size: 16px;
  font-weight: 500;
  line-height: 16px;
  color: #1e2629;
  margin-bottom: 20px;
}
.panel_container{
  border: 1px solid #dde5e5;
  width: 1200px;
}
.panel_container .title{
  height: 52px;
  width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  line-height: 14px;
  border-bottom: 1px solid #dde5e5;
  box-sizing: border-box;
  padding: 0 30px
}
.title .left_align{
  width: 18%;
}
.title .right_align{
  width: 10%;
  display: flex;
  justify-content: flex-end
}
.panel_container .item{
  height: 82px;
  width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  line-height: 14px;
  border-bottom: 1px solid #dde5e5;
  box-sizing: border-box;
  padding: 0 30px
}
.item .first_item{
  width: 18%;
  display: flex;
  align-items: center
}
.item .left_align{
  width: 18%;
}
.item .right_align{
  width: 10%;
  display: flex;
  justify-content: flex-end;
}
.coin_icon{
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #000;
}
.item .first_item p{
  font-size: 14px;
  line-height: 14px;
  color: #5793ff;
  margin-left: 11px;
}
.text_style{
  color: #1e2629;
}
.green_style{
  font-size: 16px;
  font-weight: 500;
  line-height: 16px;
  color: #0ca495;
}
.icon_container{
  margin-right: 16px;
}
.modal_container{
  display: flex;
  justify-content: space-between;
  height: 160px;
  box-sizing: border-box;
  padding-left: 13px;
  padding-right: 33px;
  padding-top: 6px;
  padding-bottom: 19px;
}
.modal_container .left_container{
  display: flex;
}
.avatar_container{
  display: flex;
  width: 56px;

}
.avatar_style{
  width: 36px;
  height: 36px;
  border-radius: 50%;
  text-align: center;
  line-height: 36px;
  color: #fff
}
.middle_container{
  display: flex;
  flex-direction: column;
  justify-content: space-between
}
.info_container{
  display: flex;
  flex-direction: row;
  width: 500px;
  margin-top: 11px;
}
.info_container .left{
  width: 268px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 43px;
  font-size: 14px;
  line-height: 14px;
}
.info_container .left .name{
  color: #638bd4;
  letter-spacing: 1px
}
.info_container .left .num{
  color: #333333;
  letter-spacing: 1px
}
.info_container .right{
  width: 268p x;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 43px;
  font-size: 14px;
  line-height: 14px;
}
.pay_container{
  display: flex;
  height: 22px;
  margin-bottom: 5px;
  align-items: center
}
.pay_container .name{
  margin-left: 10px;
  margin-right: 30px;
  color: #333333;
}
.right_container{
  display: flex;
  flex-direction: column;
  margin-top: 11px;
}
.right_container .header{
  display: flex;
  justify-content: flex-end;
}
.right_container .footer{
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.right_container .footer p{
  font-size: 14px;
  line-height: 13px;
  color: #999999;
  margin-right: 10px;
}
.right_container .footer .cancel_btn{
  margin-right: 10px;
}
.second_input{
  margin-left: 36px;
}
.total_money{
  margin: 17px 0 15px 0 ;
  font-size: 12px;
  line-height: 12px;
  color: #666666;
  text-align: end
}
.all_btn{
  cursor: pointer;
}
.input_btn{
  font-size: 14px;
  line-height: 14px;
  color: #3b78c3;
}
.flex_container{
  display: flex;

}
</style>
<style>
.btn_font_size span{
  font-size: 14px ;
}
</style>
