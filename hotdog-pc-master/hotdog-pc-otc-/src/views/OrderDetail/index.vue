<template>
  <div class="detail_container">
    <div class="main_conatiner">
      <div class="info_container">
        <div v-if="order.id">
          <p class="orderID">订单：#{{order.tNumber}}</p>
          <p
            class="order_info"
          >您向 {{oppoMerchant.m_nick_name}} {{order.tType|tTypeFilter}} {{order.volume}} {{order.baseCurrency}}</p>
          <p class="contact">对方联系方式: {{this.phones.oppositePhone}}</p>
          <p class="contact">己方联系方式: {{this.phones.selfPhone}}</p>
        </div>
        <p class="unit_price">单价： {{order.price}} {{order.quoteCurrency}}</p>
        <p class="total_price">
          总价：
          <span>{{(order.price*order.volume).toFixed(2)}} {{order.quoteCurrency}}</span>
        </p>
      </div>
      <!-- 买方显示的收款方式 -->
      <buy-payment
        v-if="order.tType==='BUY'"
        :payments="payments"
        :payInfo="payInfo"
        :selfAccount="selfAccount"
        :payStep="payStep"
        @payment-change="paymentChange"
      />
      <!-- 卖方显示的收款方式 -->
      <sell-payment
        v-if="order.tType==='SELL'"
        :payInfo="payInfo"
        :oppositeAccount="oppositeAccount"
        :payStep="payStep"
      />
      <div class="attention">
        <p>转款注意事项</p>
        <p>
          1. 在转账过程中
          <span class="red">请勿备注类似BTC、USDT等信息</span>，防止造成您汇款被拦截、银行卡被冻结等问题，因此到账延迟，卖方可选择拒绝成交。
        </p>
        <p/>
        <p>
          2.
          <span class="red">请使用平台实名的银行卡、支付宝等进行转款，</span>
        </p>
        <p>3. 若交易金额大于50，000CNY，法定假日或工作日17:00以后汇款到银行卡可能造成到账不及时，请分批支付保证及时到账，若未及时到账，卖家有权拒绝成交。</p>
      </div>
      <p class="cannot_complaining">在规定的付款限时内不允许申诉</p>
      <!-- 买方info&btn -->
      <div class="status_container" v-if="order.tType==='BUY'">
        <div v-show="payStep==='NP'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title">
            {{order.status|statusFilter}}，请于
            <span class="red">
              <count-down class="count_down" :startTime="order.startTime" :endTime="order.endTime"/>
            </span>
            内向 {{oppoMerchant.m_nick_name}} 支付
            <span
              class="green"
            >{{(order.price*order.volume).toFixed(2)}} {{order.quoteCurrency}}</span>，本人支付无需备注付款
          </p>
          <div class="btn_container">
            <Button class="to_pay" @click="payClick" :loading="goPayLoading">去支付</Button>
            <Button class="to_cancel" @click="cancelClick" :loading="cancelOrderBtnLoading">取消订单</Button>
            <div class="tips" v-if="!oppoMerchant.depositVolume">
              <i class="iconfont icon-49"></i>
              对方已缴纳 {{oppoMerchant.depositVolume}} {{oppoMerchant.depositCurrency}} 保证金
            </div>
          </div>
        </div>
        <div v-show="payStep==='UNCONFIRMED'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title" style="line-height:30px">
            {{order.status|statusFilter}}，待{{order.opposite_nick_name}}确认并放币，您将收到
            <span
              class="green"
            >{{order.volume}} {{order.baseCurrency}}</span>，请本人付款
          </p>
          <div class="btn_container">
            <Button class="to_pay" disabled>对方确认放币中...</Button>
            <Button class="to_cancel" @click="cancelClick">取消订单</Button>
            <div class="tips appeal" @click="appeal" v-show="order.appealFlag">
              <i class="iconfont icon-49"></i>
              提交申诉
            </div>
          </div>
        </div>

        <div v-show="payStep==='CANCELED'&&order.oppositeStatus!=='complaining'">
          <div class="cancel_title">订单已取消</div>
          <img src="../../assets/images/error.png" style="margin-top:30px">
        </div>

        <div v-show="payStep==='COMPLAINING'||order.oppositeStatus==='complaining'">
          <div class="cancel_title">申诉中，人工客服将会介入处理，请耐心等待处理结果！</div>
          <img src="../../assets/images/warning.png" style="margin-top:30px">
          <div
            class="cancel_appeal"
            @click="cancelAppeal"
            v-show="order.oppositeStatus!=='complaining'"
          >取消申诉</div>
        </div>
        <div v-show="payStep==='DONE'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title">
            {{order.status|statusFilter}}，
            <router-link to="/assets">查看资产</router-link>或
            <a @click.prevent="showRollForm">立即划转</a>
            至币币账户交易
          </p>
          <img src="../../assets/images/success.png" style="margin-top:30px" alt>
        </div>
      </div>
      <!-- 卖方info&btn -->
      <div class="status_container" v-if="order.tType==='SELL'">
        <div v-show="payStep==='NP'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title">
            等待对方付款，{{oppoMerchant.m_nick_name}}将在
            <span class="red">
              <count-down class="count_down" :startTime="order.startTime" :endTime="order.endTime"/>
            </span>内向您支付
            <span
              class="green"
            >{{(order.price*order.volume).toFixed(2)}} {{order.quoteCurrency}}</span>，本人支付无需备注付款
          </p>
          <div class="btn_container">
            <Button class="to_pay" disabled>对方正在支付...</Button>
          </div>
          <div
            class="tips appeal"
            @click="appeal"
            style="margin-top:30px;magin-left:0"
            v-show="order.appealFlag"
          >
            <i class="iconfont icon-49"></i>
            提交申诉
          </div>
        </div>

        <div v-if="payStep==='UNCONFIRMED'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title" style="line-height:30px">
            对方已支付，已标记向您{{order.selfAccount.type|typeFilter}}
            <span
              class="green"
            >{{order.selfAccount.account}}</span>成功支付
            <span
              class="green"
            >{{(order.price*order.volume).toFixed(2)}} {{order.quoteCurrency}}</span>，本人支付无需备注付款
          </p>
          <div class="btn_container">
            <Button
              class="to_pay"
              @click="confrimReceiveClick"
              :loading="confrimReceiveLoading"
            >确认收款并放行</Button>
          </div>
          <div
            class="tips appeal"
            @click="appeal"
            style="margin-top:30px;magin-left:0"
            v-show="order.appealFlag"
          >
            <i class="iconfont icon-49"></i>
            提交申诉
          </div>
        </div>

        <div v-show="payStep==='COMPLAINING'||order.oppositeStatus==='complaining'">
          <div class="cancel_title">申诉中，人工客服将会介入处理，请耐心等待处理结果！</div>
          <img src="../../assets/images/warning.png" style="margin-top:30px">
          <div
            class="cancel_appeal"
            @click="cancelAppeal"
            v-show="order.oppositeStatus!=='complaining'"
          >取消申诉</div>
        </div>

        <div v-show="payStep==='CANCELED'&&order.oppositeStatus!=='complaining'">
          <div class="cancel_title">订单已取消</div>
          <img src="../../assets/images/error.png" style="margin-top:30px">
        </div>

        <div v-show="payStep==='DONE'&&order.oppositeStatus!=='complaining'">
          <p class="pay_info_title">{{order.status|statusFilter}}，本人支付无需付款备注</p>
          <img src="../../assets/images/success.png" style="margin-top:30px" alt>
        </div>
      </div>
      <question/>
    </div>
    <div class="chat_container">
      <my-chart
        :order="order"
        :done30="done30"
        :msg="msg"
        :oppoMerchant="oppoMerchant"
        @send-msg="sendMsg"
      ></my-chart>
    </div>
    <!-- 申诉弹框 -->
    <Modal v-model="appealShow" title="订单申诉">
      <div class="appeal_container">
        <p>提起申诉后资产将会冻结，申诉专员将介入本次交易，直至申诉结束。恶意申诉将被冻结账户。</p>
        <Form :model="appealForm" label-position="top" style="width:100%">
          <FormItem label="申诉类型">
            <Select v-model="appealForm.type">
              <Option
                v-for="item in appealTypeList"
                :value="item.value"
                :key="item.value"
              >{{ item.label }}</Option>
            </Select>
          </FormItem>
          <FormItem label="申诉理由">
            <Input v-model="appealForm.reason" type="textarea"/>
          </FormItem>
        </Form>
        <div class="btn_tool">
          <span @click="closeAppealForm">取消</span>
          <Button type="primary" @click="appealClick">确定</Button>
        </div>
      </div>
    </Modal>
    <!-- 支付弹窗 -->
    <Modal v-model="goPayModalShow" title="确认付款" @on-cancel="payCancel">
      <go-pay-modal
        :selectPayInfo="selectPayInfo"
        :totalMoney="order.price*order.volume"
        :quoteCurrency="order.quoteCurrency"
        @pay-cancel="payCancel"
        @has-pay="hasPayClick"
      />
    </Modal>
    <!-- 取消原因弹窗 -->
    <Modal v-model="cancelOrderReasonModalShow" title="取消订单" @on-cancel="cancelCancelReasonOrder">
      <RadioGroup v-model="cancelOrderReason">
        <Radio label="已付款"></Radio>
        <span style="width:10px;"></span>
        <Radio label="还未付款"></Radio>
        <span style="width:10px;"></span>
        <Radio label="协商取消"></Radio>
      </RadioGroup>
      <div class="btn_tool">
        <span @click="cancelCancelReasonOrder">我再想想</span>
        <Button type="primary" @click="cancelOrderReasonClick">确定取消</Button>
      </div>
    </Modal>
    <!-- 取消支付弹窗 -->
    <Modal v-model="cancelOrderModalShow" title="取消订单" @on-cancel="cancelCancelOrder">
      <p>
        温馨提示：OTC交易当日累计
        <span class="red">3</span> 笔取消，会限制当日买入功能。由于恶意取消订单被商家多次投诉，情节严重的将永远禁止OTC买卖功能。
      </p>
      <div class="btn_tool">
        <span @click="cancelCancelOrder">我再想想</span>
        <Button type="primary" @click="cancelOrderClick">确定取消</Button>
      </div>
    </Modal>
    <!-- 放行弹窗 -->
    <Modal v-model="confirmReceiveModalShow" title="确认已收款并放行" @on-cancel="cancelConfirmReceive">
      <p>请务必登录网银账户、支付宝、微信等第三方支付账号查验确认收到该笔款项</p>
      <!-- <Form :model="confrimReceiveForm" label-position="top">
        <FormItem label="资金密码">
          <Input v-model="confrimReceiveForm.pwd"/>
        </FormItem>
      </Form>-->
      <div class="btn_tool_spacebetween">
        <Checkbox v-model="confrimReceiveForm.check">确认已收到该款项，核实无误</Checkbox>
        <Button type="primary" @click="confrimRece">确定</Button>
      </div>
    </Modal>
    <!-- 划转弹框 -->
    <!-- <Modal
      v-model="rollShow"
      width="472"
      @on-cancel="closeRollForm"
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
    </Modal>-->
  </div>
</template>
<script>
import Cookies from 'js-cookie';
import CountDown from 'vue2-countdown';
import Question from './components/question.vue';
import MyChart from './components/chart.vue';
import GoPayModal from './components/goPayModal.vue';
import BuyPayment from './components/Buy/payment.vue';
import SellPayment from './components/Sell/payment.vue';
import {
  getOrderDetailApi,
  getOppositeAccountInfo,
  getTradeInfo,
  cancelComplain,
  payTrade,
  cancelTrade,
  tradeConfirmed,
  tradeComplain,
  getJsSession,
  getRecordPage,
  getMember,
  sendMsgApi,
} from './api';
import { logoutApi } from '@/api/GetCode';
import { chooseBackgroundColor } from '@/libs/utils';


export default {
  components: {
    Question,
    MyChart,
    GoPayModal,
    BuyPayment,
    SellPayment,
    CountDown,
  },
  data() {
    return {
      // 双方联系方式
      phones: {},
      // 订单Id
      orderId: '',
      order: {},
      payments: '',
      payStep: '',
      // 支付方式
      payInfo: [],
      // 买方选择的支付方式
      selectPayInfo: {},
      // 对方用户30天成单
      done30: 0,
      // 对方的信息
      oppoMerchant: null,
      // 订单详情
      selfTrade: {},
      // 己方支付方式
      selfAccount: [],
      // 对方支付方式
      oppositeAccount: [],
      // 申诉弹框是否显示
      appealShow: false,
      // 申诉表单
      appealForm: {
        type: '',
        reason: '',
      },
      // 划转弹窗
      rollShow: false,
      // 划转表单
      formTop: {
        coin: '',
        from: '',
        to: '',
        num: '',
      },
      userNickName: '',
      // 申诉类型
      appealTypeList: [
        {
          label: '对方未付款',
          value: 'NP',
        },
        {
          label: '对方未放行',
          value: 'UNCONFIRMED',
        },
        {
          label: '恶意取消订单',
          value: 'CANCELED',
        },
        {
          label: '其他',
          value: 'OTHER',
        },
      ],
      msg: [],
      goPayLoading: false,
      goPayModalShow: false,
      cancelOrderModalShow: false,
      cancelOrderBtnLoading: false,
      confirmReceiveModalShow: false,
      confrimReceiveLoading: false,
      cancelOrderReasonModalShow: false,
      // 取消原因
      cancelOrderReason: '',
      confrimReceiveForm: {
        pwd: '',
        check: false,
      },
      websock: null,
      loop: null,
      id: null,
    };
  },
  filters: {
    tTypeFilter(val) {
      switch (val) {
        case 'SELL':
          return '出售';
        case 'BUY':
          return '购买';
        default:
          return '';
      }
    },
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
  },
  mounted() {
    this.orderId = localStorage.getItem('order-id');
    if (this.orderId === null) {
      this.$Notice.error({
        title: '未能获取订单id',
        desc: '未能获取订单id，请再试一次',
      });
      this.$router.push('/order');
      return;
    }
    this.msg = JSON.parse(localStorage.getItem(`chart-msg-${this.orderId}`));
    this.getOrderDetail();
    this.getOppositeAccountInfo();

    this.loop = setInterval(() => {
      this.getOrderDetail();
      this.getOppositeAccountInfo();
      this.getRecordMsg();
    }, 10000);
    this.userNickName = localStorage.getItem('nickName');
  },

  beforeDestroy() {
    // this.$store.commit('CLOSE_SOCKET');
    clearInterval(this.loop);
    console.log(this.loop);
  },
  methods: {
    // 获取历史聊天记录
    getRecordMsg() {
      const params = {
        oppositeId: this.oppoMerchant.memberId,
        tradeId: this.orderId,
      };
      getRecordPage(params).then(({ data, msg, state }) => {
        if (!this.msg) {
          this.msg = [];
        }
        const cashHisMsg = [];
        const historyData = data.records.reverse();
        const oppoNickName = data.opposite_nick_name;
        historyData.forEach((ele) => {
          if (ele.taker === 'SELF') {
            const sendMsg = {
              type: 'send',
              text: ele.charContent,
              name: this.userNickName,
              time: ele.createTime,
              // time: ele.createTime.split(' ')[1],
            };
            cashHisMsg.push(sendMsg);
          } else {
            const recMsg = {
              type: 'receive',
              text: ele.charContent,
              name: oppoNickName,
              time: ele.createTime,
              // time: ele.createTime.split(' ')[1],
            };
            cashHisMsg.push(recMsg);
          }
        });
        if (cashHisMsg === this.msg) {
          return;
        }
        this.msg = cashHisMsg;
      });
    },
    // 获取订单详情
    async getOrderDetail() {
      await getOrderDetailApi(this.orderId).then(({ data, state, msg }) => {
        if (state === 1) {
          this.phones = data.phones;
          const oppoMerchant = data.oppositeInfo;
          oppoMerchant.color = chooseBackgroundColor(oppoMerchant.memberId);
          this.oppoMerchant = oppoMerchant;
          // 获取聊天记录
          this.getRecordMsg();

          this.order = data.selfTrade;
          this.order.startTime = new Date().getTime();
          this.order.endTime = new Date(this.order.createTime).getTime()
            + this.order.paymentTime * 60 * 1000;
          this.payStep = this.order.status;
          if (this.order.oppositeAccount) {
            this.payments = this.order.oppositeAccount.id;
          }
          if (this.order.selfAccount != null) {
            this.selfAccount = [];
            this.selfAccount.push(this.order.selfAccount);
          }
          if (this.order.oppositeAccount != null) {
            this.oppositeAccount = [];
            this.oppositeAccount.push(this.order.oppositeAccount);
          }
        } else {
          this.logout();
        }
      });

      const oppositeMemberId = this.oppoMerchant.memberId;
      await getTradeInfo(oppositeMemberId).then(({ data }) => {
        this.done30 = data.done_30;
      });
    },
    getOppositeAccountInfo() {
      getOppositeAccountInfo(this.orderId).then(({ data }) => {
        this.payInfo = data.sellAccount;
      });
    },
    paymentChange(pay) {
      this.payments = pay;
    },
    payClick() {
      if (this.payments === '') {
        this.$Message.error({
          content: '请先选择支付方式',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.payInfo.forEach((ele) => {
        if (ele.id === this.payments) {
          this.selectPayInfo = ele;
        }
      });
      this.goPayLoading = true;
      this.goPayModalShow = true;
    },
    payCancel() {
      this.goPayLoading = false;
      this.goPayModalShow = false;
    },
    hasPayClick() {
      const params = {
        id: this.order.id,
        acountId: this.payments,
      };

      payTrade(params).then(({ state }) => {
        this.payCancel();
        if (state === 1) {
          this.$Message.success({
            content: '买单成功',
            duration: 2,
            closable: true,
          });
          // 获取最新交易状态
          this.getOrderDetail();
        }
      });
    },
    cancelOrderReasonClick() {
      if (!this.cancelOrderReason) {
        this.$Message.error({
          content: '请先选择取消原因',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.cancelOrderModalShow = true;
    },
    cancelOrderClick() {
      const { id } = this.order;
      cancelTrade(id).then(({ data, state, msg }) => {
        this.cancelCancelOrder();
        if (state === 1) {
          this.$Message.success({
            content: '取消成功',
            duration: 2,
            closable: true,
          });
          // 获取最新交易状态
          this.getOrderDetail();
        }
      });
    },
    cancelCancelOrder() {
      this.cancelOrderReasonModalShow = false;
      this.cancelOrderModalShow = false;
      this.cancelOrderBtnLoading = false;
    },
    cancelClick() {
      if (this.payStep === 'NP') {
        this.cancelOrderModalShow = true;
      } else {
        this.cancelOrderReasonModalShow = true;
      }

      this.cancelOrderBtnLoading = true;
    },
    cancelCancelReasonOrder() {
      this.cancelOrderReason = '';
      this.cancelOrderReasonModalShow = false;
      this.cancelOrderBtnLoading = false;
    },
    confrimReceiveClick() {
      this.confirmReceiveModalShow = true;
      this.confrimReceiveLoading = true;
    },
    cancelConfirmReceive() {
      this.confirmReceiveModalShow = false;
      this.confrimReceiveLoading = false;
    },
    confrimRece() {
      if (!this.confrimReceiveForm.check) {
        this.$Message.error({
          content: '请先勾选确认已收款',
          duration: 2,
          closable: true,
        });
        return;
      }
      const { id } = this.order;
      tradeConfirmed(id, this.cancelOrderReason).then(
        ({ data, state, msg }) => {
          if (state === 1) {
            this.$Message.success({
              content: '确认放行成功',
              duration: 2,
              closable: true,
            });
            this.confirmReceiveModalShow = false;
            // 获取最新交易状态
            this.getOrderDetail();
          } else if (msg === 'TRADE_COMPLAINING') {
            this.$Message.error({
              content: '对方已申诉',
              duration: 2,
              closable: true,
            });
          }
        },
      );
    },
    appeal() {
      this.appealShow = true;
    },
    closeAppealForm() {
      this.appealShow = false;
      this.appealForm = {
        reason: '',
        type: '',
      };
    },
    appealClick() {
      if (this.appealForm.type === '') {
        this.$Message.error({
          content: '请选择申诉类型',
          duration: 3,
          closable: true,
        });
        return;
      }
      if (this.appealForm.reason.trim() === '') {
        this.$Message.error({
          content: '请填写申诉原因',
          duration: 3,
          closable: true,
        });
        return;
      }

      const params = {
        id: this.orderId,
        memo: this.appealForm.reason,
        complainType: this.appealForm.type,
      };
      tradeComplain(params).then(({ state }) => {
        this.closeAppealForm();
        if (state === 1) {
          this.$Message.success({
            content: '申诉成功',
            duration: 2,
            closable: true,
          });
          this.getOrderDetail();
        }
      });
    },
    cancelAppeal() {
      this.$Modal.confirm({
        title: '是否取消申诉',
        onOk: () => {
          cancelComplain(this.orderId).then(({ state }) => {
            if (state === 1) {
              this.$Message.success({
                content: '取消申诉成功',
                duration: 2,
                closable: true,
              });
              this.getOrderDetail();
            }
          });
        },
      });
    },
    sendMsg(msg) {
      if (msg.trim() === '') {
        return;
      }
      const sendMsg = {
        oppositeId: this.oppoMerchant.memberId,
        charContent: msg,
        tradeId: parseInt(this.orderId, 0),
        memberId: this.order.memberId,
      };
      // this.$store.state.websock.send(JSON.stringify(sendMsg));
      sendMsgApi(sendMsg).then((res) => {});

      if (!this.msg) {
        this.msg = [];
      }
      const thisTime = this.formateTimeToString();
      const sendMsg2 = {
        type: 'send',
        text: msg,
        name: this.userNickName,
        time: thisTime,
      };
      this.msg.push(sendMsg2);
    },

    formateTimeToString() {
      const date = new Date();
      const seperator1 = '-';
      const seperator2 = ':';
      const seperator3 = ' '
      const year = date.getFullYear();
      let month = date.getMonth() + 1;
      let strDate = date.getDate();
      let minutees = date.getMinutes();
      let seconds = date.getSeconds() + 1;
      if (month >= 1 && month <= 9) {
        month = `0${month}`;
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = `0${strDate}`;
      }
      if (minutees >= 0 && minutees <= 9) {
        minutees = `0${minutees}`;
      }
      if (seconds >= 0 && seconds <= 9) {
        seconds = `0${seconds}`;
      }
      const currentdate = year
        + seperator1
        + month
        + seperator1
        + strDate
        + seperator3
        + date.getHours()
        + seperator2
        + minutees
        + seperator2
        + seconds;
      return currentdate;
    },
    sendMsg2(msg) {
      const sendMsg = {
        oppositeId: this.oppoMerchant.memberId,
        charContent: msg,
        tradeId: parseInt(this.orderId, 0),
        memberId: this.order.memberId,
      };
      sendMsgApi(sendMsg).then((res) => {});
    },
    sendMsg3(msg) {
      const sendMsg = {
        memberId: this.oppoMerchant.memberId,
        charContent: msg,
        tradeId: parseInt(this.orderId, 0),
        oppositeId: this.order.memberId,
      };
      sendMsgApi(sendMsg).then((res) => {});
    },
    async wsLink() {
      await getJsSession().then(({ data }) => {
        this.jsSession = data;
      });
      await this.initWebSocket();
    },
    initWebSocket() {

    },
    websocketonopen() {
      // 连接建立之后执行send方法发送数据
      // const actions = { test: '12345' };
      // this.websocketsend(JSON.stringify(actions));
      console.log('连接成功');
      if (this.$route.params.mark) {
        const ordermMark = this.$route.params.mark;
        if (this.order.tType === 'SELL') {
          // this.sendMsg2(ordermMark);
        } else {
          this.sendMsg3(ordermMark);
          this.$route.params = null;
        }
      }
    },
    websocketonerror() {
      // 连接建立失败重新登录
      if (window.location.hash.substr(0, 13) === '#/orderDetail') {
        getMember().then(({ msg }) => {
          if (msg === 'LANG_NO_LOGIN') {
            this.logout();
          } else {
            this.initWebSocket();
          }
        });
      }
    },
    websocketonmessage(e) {
      // 数据接收
      const recData = JSON.parse(e.data);
      console.log(recData);

      const recMsg = {
        type: recData.oppositeId === this.order.memberId ? 'send' : 'receive',
        text: recData.charContent,
        name: recData.oppo_nick_name,
        time: recData.createTime.split(' ')[1],
      };

      if (!this.msg) {
        this.msg = [];
      }
      if (recData.nick_name === recData.oppo_nick_name) {
        return;
      }

      if (parseFloat(recData.tradeId) === parseFloat(this.orderId)) {
        this.msg.push(recMsg);
      }
    },
    websocketsend(Data) {
      // 数据发送
      this.websock.send(Data);
    },
    websocketclose(e) {
      // 关闭
      console.log('断开连接', e);
      if (window.location.hash.substr(0, 13) === '#/orderDetail') {
        getMember().then(({ msg }) => {
          if (msg === 'LANG_NO_LOGIN') {
            this.logout();
          } else {
            this.initWebSocket();
          }
        });
      }
    },
    // 立即划转
    showRollForm() {
      // this.formTop.coin = this.order.baseCurrency;
      // this.rollShow = true;
      this.$router.push('/assets');
    },
    closeRollForm() {},

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
.detail_container {
  width: 1200px;
  display: flex;
  margin: 105px 0 160px 0;
}
.detail_container .main_conatiner {
  width: 867px;
}

.info_container {
  display: flex;
  flex-direction: column;
  width: 750px;
  border-bottom: 1px solid #e1e1e1;
}
.info_container .orderID {
  font-size: 14px;
  line-height: 14px;
  color: #666666;
}
.info_container .order_info {
  font-size: 36px;
  line-height: 36px;
  color: #333333;
  margin: 30px 0 32px 0;
}
.info_container .unit_price {
  font-size: 17px;
  line-height: 17px;
  color: #333333;
  margin-bottom: 10px;
}
.info_container .total_price {
  font-size: 17px;
  line-height: 17px;
  color: #333333;
  margin: 0 0 10px 0;
}
.info_container .total_price span {
  color: #0ca495;
}
.payment_container {
  width: 750px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding-bottom: 30px;
}
.payment_container .title {
  font-size: 14px;
  line-height: 14px;
  color: #999999;
  margin-top: 30px;
}

.payment_item {
  display: flex;
  align-items: center;
}

.pay_name {
  margin-left: 15px;
  color: #999999;
  font-size: 14px;
  line-height: 14px;
}
.pay_info {
  margin-left: 28px;
  font-size: 14px;
  line-height: 14px;
  color: #39404f;
}
.status_container {
  width: 750px;
  height: 316px;
  display: flex;
  flex-direction: column;
  margin-top: 30px;
}
.status_container p {
  font-size: 20px;
  line-height: 20px;
  color: #333333;
}
.red {
  color: #dc2b2b !important;
}
.green {
  color: #0ca495 !important;
}
.btn_container {
  display: flex;
  margin-top: 40px;
  align-items: center;
}
.to_pay {
  width: 200px;
  height: 50px;
  background-color: #3b78c3;
  font-size: 16px;
  color: #ffffff;
}
.to_cancel {
  width: 200px;
  height: 50px;
  border: solid 1px #3b78c3;
  color: #3b78c3;
  font-size: 16px;
  margin-left: 10px;
}
.tips {
  font-size: 14px;
  line-height: 14px;
  color: #0ca495;
  margin-left: 40px;
}
.cancel_container {
  margin-top: 45px;
  width: 245px;
  height: 36px;
  background-color: #c8c8c8;
  font-size: 14px;
  line-height: 36px;
  text-align: center;
  color: #ffffff;
}
.cancel_title {
  font-size: 20px;
  line-height: 20px;
  font-weight: 500;
  color: #333333;
  margin-top: 30px;
  margin-bottom: 30px;
}
.cancel_img {
  width: 69px;
  height: 69px;
  border-radius: 100%;
  background-color: #c8c8c8;
}
.pay_item {
  margin-top: 30px;
}
.payment_item i {
  margin-left: 15px;
  margin-right: 17px;
}
.pay_info_title {
  font-weight: 500;
}
.appeal {
  cursor: pointer;
}
.appeal_container {
  display: flex;
  flex-direction: column;
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
.cancel_appeal {
  margin-top: 42px;
  color: #4172bf;
  font-size: 16px;
  line-height: 16px;
  cursor: pointer;
}
.btn_tool_spacebetween {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.count_down {
  display: inline;
}
.contact {
  margin: 10px 0;
  font-size: 17px;
  color: #333333;
}
.attention {
  margin: 10px 0;
  line-height: 22px;
  font-size: 12px;
}
.cannot_complaining {
  font-size: 20px;
  line-height: 20px;
  color: #333333;
  font-weight: 500;
}
</style>
<style>
.count_down p {    font-size: 20px;
    line-height: 20px;
    color: #333333;
  display: inline;
}
.count_down p i:last-child {
  display: none;
}
</style>
