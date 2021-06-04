<template>
  <div class="withdraw-wrap">
    <div class="withdraw-contain">
      <router-title desc="提现" />
      <div class="form-box">
        <shadow-box>
          <div class="content">{{ this.$route.query.coinName}}提现账户</div>
          <div class="form-data">
            <div class="user">
              <div class="tip">交易所账号</div>
              <input type="text" v-model="formData.mName" disabled />
            </div>
            <div class="num">
              <div class="tip">
                <span>提币数量</span>
                <span>
                  可用:&nbsp;
                  <i class="total-num">{{ this.$route.query.restCoin}}</i>
                  &nbsp;{{ this.$route.query.coinName}};&nbsp;限额{{ this.$route.query.limit}}起提!
                </span>
              </div>
              <div class="input-box" :class="[ borderActive ? 'active' : '']">
                <input
                  type="text"
                  v-model="formData.currencyNo"
                  @focus="showActive"
                  @blur="hideActive"
                />
                <span class="coin-icon">{{ this.$route.query.coinName}}</span>
              </div>
            </div>
            <div class="verify-code">
              <div class="tip">验证码</div>
              <div class="sub-code">
                <input type="text" v-model="formData.sendCode" />
                <button
                  class="btn"
                  :class="[ sendCodeStatus ? 'active' : '' ]"
                  @click="_sendCode"
                >{{ codeBtnMsg }}</button>
              </div>
            </div>
            <div class="pwd">
              <div class="tip">资金密码</div>
              <input type="password" v-model="formData.fundPassword" />
            </div>
            <div class="sub">
              <button class="sub-btn" @click="subWithdraw">提现</button>
            </div>
          </div>
        </shadow-box>
      </div>
      <div class="table-box">
        <shadow-box>
          <div class="table-title">提币记录</div>
        </shadow-box>
        <div class="table-content">
          <my-table :initTable="initTable" :tableData="tableData" />
        </div>
        <div class="pagination">
          <pagination
            v-if="totalPages != 0"
            :total="totalPages"
            :pageSizes="pageSizes"
            @getCurPage="CurPage"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import routerTitle from "@/components/withdraw/RouterTitle.vue";
import shadowBox from "@/components/home/ShadowBox.vue";
import myTable from "@/components/home/MyTable.vue";
import pagination from "@/components/home/Pagination.vue";
import ERROR from "../../data/errorMsg";

import {
  feeCurrencyWithdrawal,
  getMembers,
  getMail
} from "@/api/withDraw/index.js";
import { mapActions, mapState } from "vuex";
import { vuexStatus } from "@/mixin/vuexStatus";
import { sameMethod } from "@/mixin/sameMethod";
import { Message } from "element-ui";
export default {
  mixins: [vuexStatus, sameMethod],
  created() {
    this.renderMembers();
    this.isSended();
    this.changeWithdraw({
      page: this.pages,
      source: "DATALAB",
      pagesize: this.pageSizes,
      currency: this.coin
    });
  },
  methods: {
    async renderMembers() {
      this.formData.mName = await this._getMembers();
    },
    ...mapActions("withDrawRec", ["changeWithdraw"]),
    showActive() {
      this.borderActive = true;
    },
    hideActive() {
      this.borderActive = false;
    },
    CurPage(val) {
      this.pages = val;
      this.changeWithdraw({
        page: this.pages,
        source: "DATALAB",
        pagesize: this.pageSizes,
        currency: this.coin
      });
    },
    subWithdraw() {
      this._feeCurrencyWithdrawal();
    },
    _feeCurrencyWithdrawal() {
      this.formData.currency = this.coin;
      this.formData.symbol = this.dealCoinPair(this._coinName);
      feeCurrencyWithdrawal(this.formData).then(res => {
        if (res.data.state == 1) {
          this.changeWithdraw({
            page: this.pages,
            source: "DATALAB",
            pagesize: this.pageSizes,
            currency: this.coin
          });
          Message({
            message: "操作成功",
            type: "success",
            duration: 3000,
            showClose: true,
            onClose: () => {
              this.clearForm();
            }
          });
        } else {
          let msg = res.data.msg;
          console.log(msg);
          
          console.log(ERROR[msg]);
          
          if (ERROR[msg]) {
            Message({
              message: ERROR[msg],
              type: "error",
              duration: 3000,
              showClose: true,
              onClose: () => {
                this.clearForm();
              }
            });
          } else {
            Message({
              message: "操作失败",
              type: "error",
              duration: 3000,
              showClose: true,
              onClose: () => {
                this.clearForm();
              }
            });
          }
        }
      });
    },
    async _sendCode() {
      if (this.sendCodeStatus) return;
      let name = await this._getMembers();
      let mailCode = await this._getMail(name);
      this.sendCodeStatus = mailCode;
    },
    _getMembers() {
      return getMembers().then(res => {
        return res.data.data.m_name;
      });
    },
    _getMail(name) {
      return getMail(
        "0ceabeae6dd349e1bff9c7351f7775d6",
        8256,
        name,
        "forgot?type=app"
      ).then(res => {
        return res.data.state == 1;
      });
    },
    isSended() {
      let isSend = localStorage.getItem("send");
      if (isSend) {
        this.sendCodeStatus = true;
        this.waitingSend();
      }
    },
    waitingSend() {
      clearInterval(this.timer);
      localStorage.setItem("send", true);
      this.timer = setInterval(() => {
        this.reSendTime--;
        this.codeBtnMsg = `${this.reSendTime}秒后重新发送`;
        if (this.reSendTime == 0) {
          this.sendCodeStatus = false;
          this.reSendTime = 60;
          this.codeBtnMsg = "发送验证码";
          localStorage.setItem("send", "");
          clearInterval(this.timer);
        }
      }, 1000);
    },
    clearForm() {
      this.formData.currencyNo = "";
      this.formData.sendCode = "";
      this.formData.fundPassword = "";
    }
  },
  computed: {
    ...mapState("withDrawRec", ["totalPages", "withDrawData"]),
    coin() {
      // return this._coinName.split("/")[0];
      return this.$route.query.coinName;
    },
    tableData() {
      if (this.withDrawData) {
        this.filterStatus(this.withDrawData);
        return this.withDrawData;
      }
      return "";
    }
  },
  watch: {
    sendCodeStatus: function() {
      if (this.sendCodeStatus) {
        this.isSendCode = true;
        this.waitingSend();
      }
    },
    _coinName: function() {
      this.changeWithdraw({
        page: this.pages,
        source: "DATALAB",
        pagesize: this.pageSizes,
        currency: this.coin
      });
    }
  },
  data() {
    return {
      borderActive: false,
      pageSizes: 20,
      pages: 1,
      reSendTime: 60,
      sendCodeStatus: false,
      codeBtnMsg: "发送验证码",
      m_name: "",
      formData: {
        mName: "",
        currencyNo: "",
        sendCode: "",
        fundPassword: ""
      },
      initTable: [
        {
          name: "时间",
          prop: "r_create_time",
          pos: "left"
        },
        {
          name: "币种",
          prop: "currency",
          pos: "left"
        },
        {
          name: "类型",
          prop: "tType",
          pos: "center"
        },
        {
          name: "数量",
          prop: "r_amount",
          pos: "left"
        },
        {
          name: "提现地址",
          prop: "r_address",
          pos: "left"
        },
        {
          name: "状态",
          prop: "r_status",
          pos: "right"
        }
      ]
    };
  },
  components: {
    routerTitle,
    shadowBox,
    myTable,
    pagination
  }
};
</script>

<style lang="scss" scoped>
@import "~/css/withDraw/index.scss";
</style>