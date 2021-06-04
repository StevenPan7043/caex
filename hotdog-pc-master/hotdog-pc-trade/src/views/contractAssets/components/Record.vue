<template>
  <div class="main_container">
    <div class="title">
      <span>{{$t('contract.TransferRecord')}}</span>
    </div>
    <div class="label">
      <span class="first">{{$t('contract.time')}}</span>
      <span>{{$t('contract.Coin')}}</span>
      <span>{{$t('contract.Direction')}}</span>
      <span>{{$t('contract.Type')}}</span>
      <span>{{$t('contract.Amount2')}}</span>
      <span>{{$t('contract.TransferAccounts')}}</span>
      <span class="special">{{$t('contract.IntoAccount')}}</span>
    </div>
    <div class="record-container">
      <div class="item" v-for="item in list" :key="item.id">
        <span class="first">{{item.timestr}}</span>
        <span>USDT</span>
        <span>{{item.isout===0?$t('contract.RollOut'):$t('contract.Into')}}</span>
        <span>{{item.typeid|type(te)}}</span>
        <span>{{item.cost}}</span>
        <span>{{item.remark.toUpperCase()|from(te)}}</span>
        <span class="special">{{item.remark.toUpperCase()|to(te)}}</span>
      </div>
    </div>
    <div class="page">
      <span class="up" @click="prePage">{{$t('contract.Pre')}}</span>
      <span class="down" @click="nextPage">{{$t('contract.Next')}}</span>
      <span> {{page}} {{$t('contract.Page')}}</span>
      <span>/</span>
      <span>{{$t('contract.Total')}} {{Math.ceil(total/pageSize)}} {{$t('contract.Page')}}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    total: Number,
    list: Array,
    page: Number,
    pageSize: Number
  },
  filters: {
    type (typeid,te) {
      let res = ''
      switch (typeid) {
        case 1:
          res = te('tradecenter.Deposit')
          break
        case 2:
          res =  te('user.Withdraw')
          break
        case 3:
          res = te('contract.SystemOperation')
          break
        case 4:
          res = te('contract.Dismiss')
          break
        case 5:
          res = te('contract.AllOpenTrade')
          break
        case 6:
          res = te('contract.Positions2')
          break
        case 7:
          res = te('contract.Transfer')
          break
        case 8:
          res = te('contract.TransactionFee')
          break
        case 9:
          res = te('contract.EntrustTrading')
          break
        case 10:
          res = te('contract.CancelDelegate')
          break
        case 11:
          res = te('contract.ClosingCharge')
          break
        case 12:
          res = te('contract.Transfer2')
          break
        case 13:
          res = te('contract.ReferralBonuses')
          break
        case 14:
          res = te('contract.ByOpenTrade')
          break
        default:
          res = te('contract.Transfer2')
          break
      }
      return res
    },

    from (mark,te) {
      let res = ''
      switch (mark) {
        case 'ADDBALANCE':
          res = te('contract.CoinCurrencyAccount')
          break
        case 'OUTBALANCE':
          res = te('contract.AllWarehouseAccount')
          break
        case 'ADDZCBALANCE':
          res = te('contract.CoinCurrencyAccount')
          break
        case 'OUTZCBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        case 'OTCADDBALANCE':
          res = te('contract.FiatAccount')
          break
        case 'OTCOUTBALANCE':
          res = te('contract.AllWarehouseAccount')
          break
        case 'OTCADDZCBALANCE':
          res =  te('contract.FiatAccount')
          break
        case 'OTCOUTZCBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        case 'HANDLETRANSBALANCE':
          res = te('contract.AllWarehouseAccount')
          break
        case 'HANDLETRANSZCBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        default:
          res = '--'
          break
      }
      return res
    },

    to (mark,te) {
      let res = ''
      switch (mark) {
        case 'ADDBALANCE':
          res =  te('contract.AllWarehouseAccount')
          break
        case 'OUTBALANCE':
          res =  te('contract.CoinCurrencyAccount')
          break
        case 'ADDZCBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        case 'OUTZCBALANCE':
          res =  te('contract.CoinCurrencyAccount')
          break
        case 'OTCADDBALANCE':
          res = te('contract.AllWarehouseAccount')
          break
        case 'OTCOUTBALANCE':
          res = te('contract.FiatAccount')
          break
        case 'OTCADDZCBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        case 'OTCOUTZCBALANCE':
          res = te('contract.FiatAccount')
          break
        case 'HANDLETRANSBALANCE':
          res = te('contract.EachWarehouseAccount')
          break
        case 'HANDLETRANSZCBALANCE':
          res =  te('contract.ContractAccount')
          break
        default:
          res = '--'
          break
      }
      return res
    }
  },
  methods: {
    te(arg) {
      const hasKey = this.$te(arg)
      if (hasKey) {
        const result = this.$t(arg)
        return result
      }
      return arg
    },

    prePage () {
      if (this.page !== 1) {
        this.$emit('pre-page')
      }
    },
    nextPage () {
      if (this.page < Math.ceil(this.total / this.pageSize)) {
        this.$emit('next-page')
      }
    }
  }
}
</script>

<style scoped>
  .main_container {
    width: 1200px;
    margin-top: 10px;
    background-color: #fff;
    margin-bottom: 60px;
  }

  .title {
    height: 50px;
    width: 1140px;
    border-bottom: 1px solid #e1e8ed;
    padding: 0 30px;
  }

  .title span {
    font-size: 16px;
    color: #1c242c;
    line-height: 50px;
  }

  .label {
    height: 50px;
    width: 1140px;
    padding: 0 30px;
  }

  .label span {
    font-size: 12px;
    color: #91a4b7;
    width: 13.3%;
    display: inline-block;
    line-height: 50px;
  }

  .item {
    height: 40px;
    width: 1140px;
    padding: 0 30px;
  }

  .item:hover {
    background-color: #f2f6f9;
  }

  .item span {
    font-size: 12px;
    color: #1c242c;
    width: 13.3%;
    display: inline-block;
    line-height: 40px;
  }

  .special {
    text-align: right;
  }

  .item .first {
    width: 20%;
  }

  .label .first {
    width: 20%;
  }

  .page {
    text-align: right;
    padding: 0 30px;
    margin: 30px 0;
  }

  .page span {
    font-size: 12px;
    color: #1c242c;
    margin-left: 10px;
  }

  .page .up:hover {
    color: #196bdf;
    cursor: pointer;
  }

  .page .down:hover {
    color: #196bdf;
    cursor: pointer;
  }

  .record-container {
    height: 400px;
  }
</style>
