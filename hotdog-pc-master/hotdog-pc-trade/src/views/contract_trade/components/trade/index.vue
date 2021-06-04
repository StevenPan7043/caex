<template>
  <div class="main_container">
    <div class="title">
      <div :class="tabIndex === 0 ? 'select' : ''" @click="changeTab(0)">
        {{ $t('contract.AllOpenTrade') }}
      </div>
      <div :class="tabIndex === 1 ? 'select' : ''" @click="changeTab(1)">
        {{ $t('contract.ByOpenTrade') }}
      </div>
    </div>
    <div class="body">
      <div class="button_container">
        <div class="left">
          <div :class="type === 0 ? 'select' : ''" @click="changeType(0)">
            {{ $t('contract.LimitOrder') }}
          </div>
          <div :class="type === 1 ? 'select' : ''" @click="changeType(1)">
            {{ $t('contract.PlanToEntrust') }}
          </div>
          <el-popover
            v-show="tabIndex === 0"
            placement="top-start"
            trigger="hover"
            width="256"
          >
            <p slot="reference">
              <i class="el-icon-info"></i>
            </p>
            <p style="color: #25425a; font-size: 12px">
              {{ $t('contract.notice') }}
            </p>
          </el-popover>
        </div>
        <div v-if="tabIndex === 0" class="right">
          <span>{{ $t('contract.Leverage') }}</span>
          <!--          <div :class="level===item?'select':''" v-for="(item,index) in levelList" :key="index"-->
          <!--               @click="changeLevel(item)">-->
          <!--            {{item}}X-->
          <!--          </div>-->
          <el-select v-model="level" placeholder="请选择" size="mini">
            <el-option
              v-for="item in levelList"
              :key="item"
              :label="item + 'X'"
              :value="item"
            ></el-option>
          </el-select>
          <el-popover placement="top-start" trigger="hover" width="130">
            <p slot="reference">
              <i class="el-icon-info"></i>
            </p>
            <p style="color: #25425a; font-size: 12px">
              {{ $t('contract.notice2') }}
            </p>
          </el-popover>
        </div>
        <div v-else class="zc_notice">
          <a
            target="_blank"
            href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360049130214-%E5%85%B3%E4%BA%8E%E7%BB%93%E7%AE%97"
            >{{ $t('contract.Rule') }}>></a
          >
        </div>
      </div>
      <div class="form_container">
        <div class="left">
          <el-form label-position="left" :model="buyForm" label-width="44px">
            <el-form-item :label="$t('contract.BuyingRate')">
              <el-input
                v-model="buyForm.price"
                autocomplete="off"
                :disabled="(tabIndex === 0 && type === 0) || tabIndex === 1"
              >
                <template slot="suffix">USDT</template>
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('contract.Purchases')">
              <el-input v-model="buyForm.num" autocomplete="off">
                <template slot="suffix">{{
                  tabIndex === 0 ? symbol.split('/')[0] : 'USDT'
                }}</template>
              </el-input>
            </el-form-item>
          </el-form>
          <div class="slider_container">
            <el-slider
              v-model="buyRatio"
              :step="25"
              :format-tooltip="formatTooltip"
              @change="buySliderRangeChange"
              show-stops
            ></el-slider>
          </div>
          <div class="money_container">
            <div>
              {{ $t('contract.Margin') }}
              {{ tabIndex === 0 ? buyMargin.toFixed(4) : buyForm.num || 0.0 }}
              USDT
            </div>
            <p v-if="tabIndex === 0">
              {{ $t('contract.AllWarehouseBalance') }} {{ usdt_num.usdt }} USDT
            </p>
            <p v-else>
              {{ $t('contract.BalanceByStorehouse') }} {{ usdt_num.zcUsdt }}USDT
            </p>
          </div>
          <div class="buy_button" @click="handleSubmit(0)">
            {{ $t('contract.BuyLone') }}
          </div>
        </div>
        <div class="divider"></div>
        <div class="right">
          <el-form label-position="left" :model="buyForm" label-width="44px">
            <el-form-item :label="$t('contract.SellingRate')">
              <el-input
                v-model="sellForm.price"
                autocomplete="off"
                :disabled="(tabIndex === 0 && type === 0) || tabIndex === 1"
              >
                <template slot="suffix">USDT</template>
              </el-input>
            </el-form-item>
            <el-form-item :label="$t('contract.SellQuantity')">
              <el-input v-model="sellForm.num" autocomplete="off">
                <template slot="suffix">{{
                  tabIndex === 0 ? symbol.split('/')[0] : 'USDT'
                }}</template>
              </el-input>
            </el-form-item>
          </el-form>
          <div class="slider_container">
            <el-slider
              :format-tooltip="formatTooltip"
              v-model="sellRatio"
              :step="25"
              show-stops
              @change="sellSliderRangeChange"
            ></el-slider>
          </div>
          <div class="money_container">
            <div>
              {{ $t('contract.Margin') }}
              {{ tabIndex === 0 ? sellMargin.toFixed(4) : sellForm.num || 0.0 }}
              USDT
            </div>
            <p v-if="tabIndex === 0">
              {{ $t('contract.AllWarehouseBalance') }} {{ usdt_num.usdt }} USDT
            </p>
            <p v-else>
              {{ $t('contract.BalanceByStorehouse') }} {{ usdt_num.zcUsdt }}USDT
            </p>
          </div>
          <div class="sell_button" @click="handleSubmit(1)">
            {{ $t('contract.SellShort') }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'index',

  mounted() {
    this.getLevel()
    this.getQcMoney()
  },
  props: ['coinPrice', 'symbol', 'tableData'],
  data() {
    return {
      // 全仓--0 逐仓--1
      tabIndex: 0,
      buyForm: {
        price: '',
        num: '',
      },
      buyRatio: 0,
      sellForm: {
        price: '',
        num: '',
      },
      sellRatio: 0,
      // 限价--0 委托--1
      type: 0,
      // 杠杆
      level: 0,
      levelList: [],
      // 资产
      usdt_num: {
        usdt: 0,
        zcUsdt: 0,
      },
    }
  },
  watch: {
    coinPrice() {
      if ((this.tabIndex === 0 && this.type === 0) || this.tabIndex === 1) {
        this.buyForm.price = this.coinPrice
        this.sellForm.price = this.coinPrice
      }
    },
    level() {
      this.sellRatio = 0
      this.buyRatio = 0
      this.buySliderRangeChange(0)
      this.sellSliderRangeChange(0)
    },
    type() {
      this.sellRatio = 0
      this.buyRatio = 0
      this.buySliderRangeChange(0)
      this.sellSliderRangeChange(0)
    },
    tabIndex() {
      this.sellRatio = 0
      this.buyRatio = 0
      this.buySliderRangeChange(0)
      this.sellSliderRangeChange(0)
    },
    symbol() {
      this.tabIndex = 0
      this.type = 0
      this.getQueryCoins()
    },
  },
  computed: {
    // 买保证金
    buyMargin() {
      return (this.buyForm.price / this.level) * this.buyForm.num
    },
    // 卖保证金
    sellMargin() {
      return (this.sellForm.price / this.level) * this.sellForm.num
    },
  },
  methods: {
    buySliderRangeChange(e) {
      let buyMaxNum = 0
      if (this.buyForm.price === '' || this.buyForm.price === 0) {
        return
      }
      if (this.tabIndex === 0) {
        buyMaxNum = this.usdt_num.usdt * this.level
      } else {
        buyMaxNum = this.usdt_num.zcUsdt / 1.1
      }
      let pow = Math.pow(10, 6)
      let num
      num = (
        Math.floor(((buyMaxNum * e) / 100 / this.buyForm.price) * pow) / pow
      ).toFixed(6)
      this.buyForm.num = num <= 0 ? '' : num
    },
    sellSliderRangeChange(e) {
      let sellMaxNum = 0
      if (this.sellForm.price === '' || this.sellForm.price === 0) {
        return
      }
      if (this.tabIndex === 0) {
        sellMaxNum = this.usdt_num.usdt * this.level
      } else {
        sellMaxNum = this.usdt_num.zcUsdt / 1.1
      }
      let pow = Math.pow(10, 6)
      let num
      num = (
        Math.floor(((sellMaxNum * e) / 100 / this.sellForm.price) * pow) / pow
      ).toFixed(6)
      this.sellForm.num = num <= 0 ? '' : num
    },
    formatTooltip(val) {
      return val + '%'
    },
    changeTab(index) {
      if (this.tableData.type === -1 && index === 1) {
        return
      }
      this.tabIndex = index
      this.type = 0
      this.buyForm.price = this.coinPrice
      this.sellForm.price = this.coinPrice
    },
    changeType(type) {
      if (this.tabIndex === 0) {
        this.type = type
      }
      if (this.tabIndex === 0 && this.type === 1) {
        this.buyForm.price = ''
        this.sellForm.price = ''
      } else {
        this.buyForm.price = this.coinPrice
        this.sellForm.price = this.coinPrice
      }
    },

    getLevel() {
      // 杠杆
      this.$ajax('/trade/queryGearing', {}).then((res) => {
        if (res.data.status) {
          this.levelList = res.data.data
          this.level = this.levelList[0]
        }
      })
    },
    getQcMoney() {
      this.$ajax('/account/getMyInfos', {
        token: this.$userID(),
      }).then((res) => {
        if (res.data.status) {
          this.usdt_num = res.data.data
        } else {
          this.$router.push('/login')
        }
      })
    },
    handleSubmit(e) {
      if (this.tabIndex === 0) {
        this.deal(e)
      } else {
        this.ZCDeal(e)
      }
    },

    // 交易
    deal(e) {
      let rex = /^[0-9]+(.[0-9]{1,4})?$/
      const usdtNum = e === 0 ? this.buyForm.num : this.sellForm.num
      if (usdtNum == '' || usdtNum == 0) {
        this.$message({
          type: 'error',
          message: '请输入数量',
          duration: 1200,
          showClose: true,
        })
      } else if (!rex.test(usdtNum)) {
        this.$message({
          type: 'error',
          message: '输入数量有误',
          duration: 1200,
          showClose: true,
        })
      } else {
        this.dealSubmit(e)
      }
    },
    dealSubmit(e) {
      if (this.type === 0) {
        //普通购买
        this.$ajax('/trade/handlContractOrder', {
          token: this.$userID(),
          type: e + 1,
          coinnum: e === 0 ? this.buyForm.num : this.sellForm.num,
          usdtPrice: e === 0 ? this.buyForm.price : this.sellForm.price,
          gearing: this.level,
          symbol: this.symbol.toLowerCase().replace('/', '_'),
        }).then((res) => {
          if (res.data.desc) {
            this.handSubmitResult(res)
            this.emitReloadAssets()
          } else {
            this.$message({
              type: 'error',
              message: res.data.desc,
              duration: 1200,
              showClose: true,
            })
          }
        })
      } else {
        //委托购买
        this.$ajax('/trade/handleEntrust', {
          token: this.$userID(),
          type: e + 1,
          coinnum: e === 0 ? this.buyForm.num : this.sellForm.num,
          gearing: this.level,
          symbol: this.symbol.toLowerCase().replace('/', '_'),
          price: e === 0 ? this.buyForm.price : this.sellForm.price,
        }).then((res) => {
          if (res.data.status) {
            this.handSubmitResult(res)
            this.emitReloadAssets()
            this.emitReloadDelegate()
          } else {
            this.$message({
              type: 'error',
              message: res.data.desc,
              duration: 1200,
              showClose: true,
            })
          }
        })
      }
    },
    //逐仓交易
    ZCDeal(e) {
      let rex = /^[0-9]+(.[0-9]{1,4})?$/
      const usdtNum = e === 0 ? this.buyForm.num : this.sellForm.num
      if (usdtNum == '' || usdtNum == 0) {
        this.$message({
          type: 'error',
          message: '请输入数量',
          duration: 1200,
          showClose: true,
        })
      } else if (!rex.test(usdtNum)) {
        this.$message({
          type: 'error',
          message: '输入数量有误',
          duration: 1200,
          showClose: true,
        })
      } else {
        this.ZCSubmit(e)
      }
    },
    //逐仓交易提交
    ZCSubmit(e) {
      this.$ajax('/trade/handlZCOrder', {
        token: this.$userID(),
        type: e + 1, //1开多  2开空
        coinnum: 1, // 持仓量
        price: e === 0 ? this.buyForm.num : this.sellForm.num, //金额单价
        symbol: this.symbol.toLowerCase().replace('/', '_'), //交易对 btc_usdt eth_usdt等等
      }).then((res) => {
        this.handSubmitResult(res)
        this.emitReloadAssets()
      })
    },

    handSubmitResult(res) {
      if (res.data.status) {
        this.$message({
          type: 'success',
          message: res.data.desc,
          duration: 1000,
          showClose: true,
        })
        this.getQcMoney()
      } else {
        this.$message({
          type: 'error',
          message: res.data.desc,
          duration: 1000,
          showClose: true,
        })
      }
    },
    emitReloadAssets() {
      this.$emit('reloadAssets')
    },
    emitReloadDelegate() {
      this.$emit('reloadDelegate')
    },
  },
}
</script>

<style scoped>
.main_container {
  width: 100%;
  height: 100%;
  margin-top: 6px;
}

.title {
  width: 100%;
  height: 45px;
  background-color: #f2f6fa;
  padding: 0 20px;
  box-sizing: border-box;
  display: flex;
}

.title div {
  height: 45px;
  width: 80px;
  line-height: 45px;
  border-bottom: 3px solid transparent;
  border-top: 3px solid transparent;
  box-sizing: border-box;
  text-align: center;
  font-size: 14px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #25425a;
  cursor: pointer;
}

.title div.select {
  border-bottom: 3px solid #196bdf;
  color: #196bdf;
}

.body {
  width: 100%;
  height: 320px;
  background-color: #fff;
}

.button_container {
  padding: 20px 20px 0 20px;
  box-sizing: border-box;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button_container .left {
  display: flex;
  height: 28px;
  align-items: center;
}

.button_container .left div {
  width: 114px;
  height: 28px;
  border: solid 1px #cfdde6;
  font-size: 14px;
  color: #91a4b7;
  text-align: center;
  line-height: 28px;
  margin-right: 8px;
  cursor: pointer;
}

.button_container .left div.select {
  border: solid 1px #196bdf;
  color: #196bdf;
}

.button_container .right {
  display: flex;
  height: 28px;
  align-items: center;
}

.button_container .right span {
  font-size: 12px;
  color: #91a4b7;
  margin-right: 8px;
}

.button_container .right div {
  padding: 0 2%;
  height: 28px;
  /*border: solid 1px #cfdde6;*/
  font-size: 14px;
  color: #91a4b7;
  text-align: center;
  line-height: 28px;
  margin-right: 8px;
  cursor: pointer;
}

.button_container .right div.select {
  border: solid 1px #196bdf;
  color: #196bdf;
}

.form_container {
  display: flex;
}

.form_container .left {
  width: 50%;
  padding: 20px;
  box-sizing: border-box;
}

.form_container .divider {
  width: 0px;
  height: 240px;
  border-left: dotted 1px #d7e0ec;
}

.form_container .right {
  padding: 20px;
  box-sizing: border-box;
  width: 50%;
}

.zc_notice {
  font-size: 12px;

  color: #445666;
}

.slider_container {
  padding: 0 5px;
}

.money_container {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.left .money_container div {
  font-size: 12px;
  color: #5bbe8e;
}

.money_container p {
  font-size: 12px;
  color: #25425a;
}

.buy_button {
  width: 100%;
  height: 38px;
  background-color: #5bbe8e;
  font-size: 14px;
  color: #ffffff;
  line-height: 38px;
  text-align: center;
  cursor: pointer;
  margin-top: 20px;
}

.sell_button {
  width: 100%;
  height: 38px;
  background-color: #ee6560;
  font-size: 14px;
  color: #ffffff;
  line-height: 38px;
  text-align: center;
  cursor: pointer;
  margin-top: 20px;
}

.right .money_container div {
  font-size: 12px;
  color: #ee6560;
}
</style>
<style>
.form_container .left .el-input__inner {
  height: 36px;
}

.form_container .left .el-input__suffix {
  font-size: 12px;
  color: #91a4b7 !important;
  margin-right: 10px;
}

.form_container .left .el-form-item__label {
  width: 44px !important;
  min-width: 44px !important;
  font-size: 12px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #91a4b7;
}

.form_container .left .el-slider__button {
  border: solid 4px #29d190 !important;
}

.form_container .left .el-slider__bar {
  background-color: #29d190 !important;
}

.form_container .left .el-slider__runway {
  height: 4px;
}

.form_container .left .el-slider__stop {
  width: 10px;
  height: 10px;
  background-color: #d7e0e9 !important;
  top: -3px;
}

.form_container .left .el-slider__button {
  width: 12px;
  height: 12px;
  border-width: 4px;
}
</style>
<style>
.form_container .right .el-input__inner {
  height: 36px;
}

.form_container .right .el-input__suffix {
  font-size: 12px;
  color: #91a4b7;
  margin-right: 10px;
}

.form_container .right .el-form-item__label {
  width: 44px !important;
  min-width: 44px !important;
  font-size: 12px;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
  color: #91a4b7 !important;
}

.form_container .right .el-slider__button {
  border: solid 4px #da3f4d !important;
}

.form_container .right .el-slider__bar {
  background-color: #da3f4d !important;
}

.form_container .right .el-slider__runway {
  height: 4px;
}

.form_container .right .el-slider__stop {
  width: 10px;
  height: 10px;
  background-color: #d7e0e9 !important;
  top: -3px;
}

.form_container .right .el-slider__button {
  width: 12px;
  height: 12px;
  border-width: 4px;
}
</style>

<style>
.el-input {
  height: 28px;
}
</style>
