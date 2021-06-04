<template>
  <div class="main-container">
    <div class="header-container">
      <div id="first-header">
        <p>商家(30日成单 | 30日完成率)</p>
      </div>
      <div >
        <p>数量</p>
      </div>
      <div >
        <p>限额</p>
      </div>
      <div >
        <p>单价</p>
      </div>
      <div >
        <p>支付方式</p>
      </div>
      <div id="rigth-align">
        <p >操作</p>
      </div>
    </div>
    <div class="table-container">
    <div v-for="item in tradeList" :key="item.id">
      <div class="item-container">
        <div class="first-item">
          <div class="avatar_style" :style="{backgroundColor:item.color}">
            {{item.m_nick_name|avatarFilter}}
          </div>
          <!-- <Avatar :style="{background: item.memberId|colorFilter}"></Avatar> -->
          <p  @click="toUserInfo(item)">{{item.m_nick_name}} ({{item.done_30}} | {{item.completePerecent}}) </p>
        </div>
        <div class="middle-item font1">
          <p>{{item.remainVolume}} {{item.baseCurrency}}</p>
        </div>
        <div class="middle-item font1">
          <p>{{(item.minQuote)}}-{{(item.maxQuote)}} {{item.quoteCurrency}}</p>
        </div>
        <div class="middle-item font2">
          <p class="bold_price">{{item.baseCurrency=='ZC'?item.price.toFixed(3):item.price.toFixed(2)}} {{item.quoteCurrency}}</p>
        </div>
        <div class="middle-item" >
          <span v-for="(sbuItem,index) in item.accountArray" :key="index" >
            <i class="iconfont icon-yinhangqia" v-if="sbuItem==='BANK'" style="color: #768db0;font-size: 22px;"></i>
            <i class="iconfont icon-zhifubao" v-if="sbuItem==='ALIPAY'" style="color: #39a9ed;font-size: 22px;"></i>
            <i class="iconfont icon-weixindenglu" v-if="sbuItem==='WXPAY'"  style="color: #3baf34;font-size: 22px;"></i>
          </span>
        </div>
        <div class="last-item">
          <div>
            <p @click="btnClick(item)">{{tradeType|tradeTypeFilter}}{{item.baseCurrency}}</p>
          </div>
        </div>
      </div>
      <div class="split-container">
        <div class="split-line"></div>
      </div>
    </div>
    </div>

    <div class="pagination-container">
      <Page :total="total" :pageSize="pageSize" @on-change="pageChange"/>
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
                <p class="num">数量 {{adInfo.remainVolume}} {{adInfo.baseCurrency}}</p>
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
            <Input v-model="num" :placeholder="buyNumPlaceholder" style="width: 230px;" @on-blur="numBlur">
              <span slot="suffix" class="input_btn">{{adInfo.baseCurrency}}|<span class="all_btn" @click="allIn">全部</span></span>
            </Input>
            <Input v-model="pwd" placeholder="请输入资金密码" style="width: 230px;" type="password" class="second_input"/>
          </div>
          <p class="total_money">交易总额 {{totalCNY}} CNY</p>
          <div class="footer">
            <p>买方付款时限为{{adInfo.paymentTime}}分钟</p>
            <Button class="cancel_btn" type="warning" ghost size="large" @click="closeModal">自动取消({{deadTime}})</Button>
            <Button type="warning" size="large" :loading='tradeBtnLoading' @click="buyCoin">{{adInfo.type==='BUY'?'出售':'购买'}}{{adInfo.baseCurrency}}</Button>
          </div>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script>
import {
  addOTCOrder, getAuthindentity, getAccountId, getMoneyByCoinType, getMember, getMoneyInfo, sendMsgApi,
} from './api';
import { logoutApi } from '@/api/GetCode';
import { customToFixed } from '@/libs/utils';

export default {
  props: {
    tradeList: {
      type: Array,
    },
    total: {
      type: Number,
    },
    pageSize: {
      type: Number,
      default: 10,
    },
    tradeType: String,
  },
  data() {
    return {
      user: '芒',
      color: '#63cdba',
      modalFormShow: false,
      adInfo: {},
      deadTime: 45,
      timeDown: '',
      num: '',
      pwd: '',
      userId: '',
      // 是否登录
      isLogin: false,
      buyNumPlaceholder: '',
      tradeBtnLoading: false,
    };
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
  created() {
    this.userId = localStorage.getItem('useruid');
  },
  beforeDestroy() {
    window.clearInterval(this.timeDown);
  },
  computed: {
    completePercent() {
      if (this.adInfo.trade_30 === 0) {
        return '0%';
      }
      return `${this.adInfo.done_30 / this.adInfo.trade_30 * 100}%`;
    },
    totalCNY() {
      const cashTotal = this.num * this.adInfo.price;
      return customToFixed(cashTotal, 'CNY');
    },
  },

  methods: {
    pageChange(e) {
      this.$emit('page-change', e);
    },
    allIn() {
      if (this.tradeType === 'SELL') {
        const num = this.adInfo.remainVolume;
        this.num = customToFixed(num, this.adInfo.baseCurrency);
      } else {
        const num = this.adInfo.remainVolume;

        const currency = this.adInfo.baseCurrency;
        const params = {
          memberId: this.userId,
          currency,
        };
        getMoneyByCoinType(params).then(({ data, msg, state }) => {
          if (state === 1) {
            if (data) {
              const hasMoney = data.totalBalance - data.frozenBalance;
              const finalNum = Math.min(hasMoney, num);
              this.num = customToFixed(finalNum, this.adInfo.baseCurrency);
            } else {
              this.num = 0;
            }
          } else {
            this.logout();
          }
        });
      }
    },
    async buyCoin() {
      if (this.pwd.trim() === '') {
        this.$Message.error({
          content: '请输入资金密码',
          duration: 3,
          closable: true,
        });
        return;
      }
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
      if (this.num <= 0) {
        this.$Message.error({
          content: '错误的交易数量',
          duration: 2,
          closable: true,
        });
        return;
      }
      if (this.tradeType === 'BUY') {
        const hasMoney = await this.checkHasCoin();
        if (!hasMoney) {
          this.$Message.error({
            content: '资金不足',
            duration: 2,
            closable: true,
          });
          return;
        }
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
          const tradeId = data.id;
          this.sendMsg(tradeId);
          localStorage.setItem('order-id', data.id);
          this.$router.push({ name: 'orderDetail', params: { mark: this.adInfo.remark, id: data.id } });
        }
        if (msg === 'BUY_HAS_ONE_TRADING') {
          const id = data.tNumber;
          this.$Notice.error({
            title: '存在未交易完成订单',
            desc: `您已存在一笔买单正在进行交易，处理完成后方可再次购买！未交易完成订单ID：${id}`,
            duration: 0,
          });
          // this.$Message.error({
          //   content: `进行中订单ID：${id}`,
          //   duration: 5,
          //   closable: true,
          // });
        }
        if (msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      });
    },

    sendMsg(tradeId) {
      const sendMsg = {
        oppositeId: this.userId,
        charContent: this.adInfo.remark,
        tradeId,
        memberId: this.adInfo.memberId,
      };
      sendMsgApi(sendMsg).then((res) => {

      });
    },

    async checkHasCoin() {
      const userInfo = await getMember();
      const memberId = userInfo.data.uid;


      const moneyInfo = await getMoneyInfo(memberId);

      let hasMoney = false;
      const inputNUm = parseFloat(this.num);
      const tradeCoin = this.adInfo.baseCurrency;
      if (moneyInfo.data.rows !== null) {
        moneyInfo.data.rows.forEach((ele) => {
          if (ele.currency === tradeCoin) {
            if ((ele.totalBalance - ele.frozenBalance) >= inputNUm) {
              hasMoney = true;
            }
          }
        });
      }

      return hasMoney;
    },
    closeModal() {
      this.modalFormShow = false;
      this.num = '';
      this.pwd = '';
      this.adInfo = '';
      window.clearInterval(this.timeDown);
      this.deadTime = 45;
    },
    async btnClick(item) {
      const regPhone = new RegExp('^[1][3,4,5,7,8][0-9]{9}$');
      const res = await getMember();
      if (res.state === 1) {
        this.isLogin = true;

        if (!regPhone.test(res.data.m_name) && res.data.phone === '') {
          this.$Message.error({
            content: '请先添加联系方式',
            duration: 3,
            closable: true,
          });
          this.$router.push('/resetContact');
        } else {
          await this.checkCanTrade(item);
        }
      } else {
        this.isLogin = false;
        this.logout();
      }
      // const regPhone = new RegExp('^[1][3,4,5,6,7,8,9][0-9]{9}$');
      // getMember().then(({ data, msg, state }) => {
      //   if (state === 1) {
      //     if (regPhone.test(data.m_name)) {
      //       this.isLogin = JSON.parse(localStorage.getItem('loginState'));
      //       // 判断是否登录
      //       if (this.isLogin) {
      //         // 判断是否实名
      //         getAuthindentity().then((res) => {
      //           if (res.state === 1) {
      //             if (!res.data) {
      //               this.$Message.error({
      //                 content: '请先完成实名认证',
      //                 duration: 2,
      //                 closable: true,
      //               });
      //               return;
      //             }
      //             if (res.data.id_status === 1) {
      //               // 判断是否绑定收款账号
      //               getAccountId().then(({ data }) => {
      //                 const accountInfo = [];
      //                 data.rows.forEach((ele) => {
      //                   if (ele.isDelete === 'NO') {
      //                     accountInfo.push(ele);
      //                   }
      //                 });
      //                 if (accountInfo.length < 1) {
      //                   this.$Message.error({
      //                     content: '请先添加收款方式',
      //                     duration: 2,
      //                     closable: true,
      //                   });
      //                 } else {
      //                   if (!this.hasCorrespondPayment(item, accountInfo)) {
      //                     this.$Message.error({
      //                       content: '收款方式不匹配',
      //                       duration: 2,
      //                       closable: true,
      //                     });
      //                     return;
      //                   }
      //                   this.adInfo = item;
      //                   this.buyNumPlaceholder = item.type === 'BUY' ? '请输入出售数量' : '请输入购买数量';
      //                   this.modalFormShow = true;
      //                   this.countDown();
      //                 }
      //               });
      //             } else {
      //               this.$Message.error({
      //                 content: '请先完成实名认证',
      //                 duration: 2,
      //                 closable: true,
      //               });
      //             }
      //           } else if (res.state === -1) {
      //             if (res.msg === 'LANG_NO_LOGIN') {
      //               this.logout();
      //             }
      //           }
      //         });
      //       } else {
      //         this.logout();
      //       }
      //     } else {
      //       this.$Message.error({
      //         content: '抱歉，暂不支持邮箱用户申请商家',
      //         duration: 2,
      //         closable: true,
      //       });
      //     }
      //   } else if (msg === 'LANG_NO_LOGIN') {
      //     this.logout();
      //   }
      // });
    },
    checkCanTrade(item) {
      // 判断是否登录
      if (this.isLogin) {
        // 判断是否实名
        getAuthindentity().then((res) => {
          if (res.state === 1) {
            if (res.data.authIdentity===null) {
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
    numBlur() {
      if (parseFloat(this.num) > parseFloat(this.adInfo.remainVolume)) {
        let num = customToFixed(this.adInfo.remainVolume, this.adInfo.baseCurrency);
        if (isNaN(num)) {
          num = 0;
        }
        this.num = num;
      } else {
        let num = customToFixed(this.num, this.adInfo.baseCurrency);
        if (isNaN(num)) {
          num = 0;
        }
        this.num = num;
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
    toUserInfo(item) {
      localStorage.setItem('check-merchant-id', item.memberId);
      localStorage.setItem('check-merchant-nickName', item.m_nick_name);
      this.$router.push('/userInfo');
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
.main-container{
  width: 1200px;
  display: flex;
  flex-direction: column;
}
.header-container{
  width: 1200px;
  height: 27px;
  display: flex;
  justify-content: space-between;
  border-bottom: 1px solid #d9d9d9
}
.header-container div{
  width: 16.25%;
  height: 27px;
}
.header-container div p {
  font-size: 14px;
  color: #999999;
}
#first-header{
  width:25%;
}
#rigth-align{
  display: flex;
  justify-content: flex-end;
  width: 10%
}

.table-container{
  height: 820px;
}

.item-container{
  width: 1200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  height: 57px;
  align-items: flex-start
}
.first-item{
  width: 25% ;
  display: flex;
  align-items: center;
  font-size: 14px;
  letter-spacing: 1px;
  color: #638bd4;
}

.first-item p{
  margin-left: 20px;
  cursor: pointer;
}
.middle-item{
  width: 16.25%;
  display: flex;
  justify-content: flex-start;
  height: 36px;
  align-items: center;
}
.last-item{
  width: 10%;
  display: flex;
  justify-content: flex-end;
  height: 36px;
  align-items: center;
}
.last-item div{
  width: 80px;
  height: 32px;
  background-color: #3b78c3;
  border-radius: 4px;
  cursor: pointer;
}
.last-item div p{
  text-align: center;
  line-height: 32px;
  font-size: 14px;
  letter-spacing: 1px;
  color: #ffffff;
}
.font1{
  font-size: 14px;
  letter-spacing: 1px;
  color: #333333;
}
.font2{
  font-size: 16px;
  color: #0ca495;
}
.middle-item i{
  margin-right: 16px;
}
.split-container{
  display: flex;
  justify-content: flex-end;
  width: 1200px;
}
.split-line{
  width: 1142px;
  height: 1px;
  background-color: #e8e8e8;
}
.pagination-container{
  margin:67px 0 95px 0;
  width: 1200px;
  display: flex;
  justify-content: center;
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
.bold_price{
  font-weight: bolder
}
</style>
<style>
.v-transfer-dom .ivu-modal-wrap .ivu-modal{
  width:100%;
}
.ivu-modal-footer{
  display:none
}
.right_container .ivu-input-suffix{
  width:90px;
  line-height:29px;
}
</style>
