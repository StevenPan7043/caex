<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="otcmyorder-panel container">

        <div class="inner-otcmyorder-panel" id="InnerOtcMyorder">

          <div class="otcmyorder-header">
            <span class="title">{{$t('otc.MyOrders')}}</span>
            <div class="orders-option">
              <span :class="selectTab===0?'cur':''" @click="selectTabClick(0)">{{$t('otc.TheCurrentOrder')}}</span>
              <span :class="selectTab===1?'cur':''" @click="selectTabClick(1)">{{$t('otc.CompletedOrder')}}</span>
              <span :class="selectTab===2?'cur':''" @click="selectTabClick(2)">{{$t('otc.CancelledOrder')}}</span>
            </div>
          </div>
          <div class="otcmyorder-main">

            <table class="cur" cellspacing=0 id="pendingOrder" v-show="selectTab===0">
              <thead>
                <tr>
                  <th>{{$t('otc.Order')}}</th>
                  <th>{{$t('tradecenter.Type')}}</th>
                  <th>{{$t('otc.MonovalentCNY')}}</th>
                  <th>{{$t('otc.NumCNY')}}</th>
                  <th>{{$t('otc.MoneyCNY')}}</th>
                  <th>{{$t('otc.Acceptance')}}</th>
                  <th>{{$t('otc.CreationTime')}}</th>
                  <th>{{$t('user.Status')}}</th>
                  <th>{{$t('otc.Operation')}}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item,index) in orders" :key="index">
                  <td>{{item.id}}</td>
                  <td class="fall" v-if="item.type==='sale'">{{$t('tradecenter.Sells')}}</td>
                  <td class="rise" v-else>{{$t('tradecenter.Buys')}}</td>
                  <td>{{item.price}}</td>
                  <td>{{item.volume}}</td>
                  <td>{{item.amount}}</td>
                  <td>{{item.owner_name}}</td>
                  <td>{{item.create_time}}</td>
                  <td>
                    <i></i>
                    <span v-if="item.type==='sale'">{{$t('user.Pending')}}</span>
                    <span v-else>{{item.status==='-1'?$t('user.Obligation'):$t('user.Pending')}}</span>
                  </td>
                  <td v-if="item.type==='sale'">{{$t('otc.PendingPaymentByBuyer')}}</td>
                  <td v-if="item.type==='buy'&&item.status==-1" @click="selectPayType(item)">{{$t('otc.SelectPaymentMethod')}}</td>
                  <td v-if="item.type==='buy'&&item.status==0">{{$t('otc.Paid')}}</td>
                </tr>
              </tbody>
            </table>

            <table class="cur" cellspacing=0 id="completeOrder" v-show="selectTab===1">
              <thead>
                <tr>
                  <th>{{$t('otc.Order')}}</th>
                  <th>{{$t('tradecenter.Type')}}</th>
                  <th>{{$t('otc.MonovalentCNY')}}</th>
                  <th>{{$t('otc.NumCNY')}}</th>
                  <th>{{$t('otc.MoneyCNY')}}</th>
                  <th>{{$t('otc.Acceptance')}}</th>
                  <th>{{$t('otc.CreationTime')}}</th>
                  <th>{{$t('user.Status')}}</th>
                  <th>{{$t('otc.Operation')}}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item,index) in compOrders" :key="index">
                  <td>{{item.id}}</td>
                  <td class="fall" v-if="item.type==='sale'">{{$t('tradecenter.Sells')}}</td>
                  <td class="rise" v-else>{{$t('tradecenter.Buys')}}</td>
                  <td>{{item.price}}</td>
                  <td>{{item.volume}}</td>
                  <td>{{item.amount}}</td>
                  <td>{{item.owner_name}}</td>
                  <td>{{item.create_time}}</td>
                  <td>
                    <span>{{$t('user.Completed')}}</span>
                  </td>
                  <td>-</td>
                </tr>
              </tbody>
            </table>

            <table class="cur" cellspacing=0 id="cancelOrder" v-show="selectTab===2">
              <thead>
                <tr>
                  <th>{{$t('otc.Order')}}</th>
                  <th>{{$t('tradecenter.Type')}}</th>
                  <th>{{$t('otc.MonovalentCNY')}}</th>
                  <th>{{$t('otc.NumCNY')}}</th>
                  <th>{{$t('otc.MoneyCNY')}}</th>
                  <th>{{$t('otc.Acceptance')}}</th>
                  <th>{{$t('otc.CreationTime')}}</th>
                  <th>{{$t('user.Status')}}</th>
                  <th>{{$t('otc.Operation')}}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item,index) in cancelOrders" :key="index">
                  <td>{{item.id}}</td>
                  <td class="fall" v-if="item.type==='sale'">{{$t('tradecenter.Sells')}}</td>
                  <td class="rise" v-else>{{$t('tradecenter.Buys')}}</td>
                  <td>{{item.price}}</td>
                  <td>{{item.volume}}</td>
                  <td>{{item.amount}}</td>
                  <td>{{item.owner_name}}</td>
                  <td>{{item.create_time}}</td>
                  <td>
                    <span>{{$t('user.Canceled')}}</span>
                  </td>
                  <td>-</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import { getOtcorders } from '@/api/otcmyorder'

export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      orders: [],
      compOrders:[] ,
      cancelOrders: [],
      selectTab: 0
    }
  },
  created () {
    this.getCurrentOtcOrder()
  },
  methods: {
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    //获取当前交易订单
    getCurrentOtcOrder () {
      //1买 2 卖
      //当前卖出订单
      this.orders=[]
      let p1= getOtcorders('ZC', 'CNY', '1', '20', 2, 0)
      let p2 = getOtcorders('ZC', 'CNY', '1', '20', 1, 0)
      Promise.all([p1,p2]).then(res=>{
        let sellOrders=[]
        let buyOrders=[]
        if (res[0].state === 1) {
          sellOrders= res[0].orders
          sellOrders.forEach(ele => {
            ele.type = 'sale'
          })
        } else if (res[0].state === -1) {
          if (res[0].msg === 'LANG_NO_LOGIN') {
            this.$message({
              type: 'error',
              message: this.$t('header.PlaseLogin'),
              duration: 3000,
              showClose: true
            })
            setTimeout(() => {
              this.userLogout()
            }, 2000)
          }
        } 
        buyOrders=res[1].orders
        buyOrders.forEach(ele=>{
          ele.type = 'buy'
        })
        this.orders = [...sellOrders,...buyOrders]
      })
    },

    //获取已完成交易订单
    getCompleteOtcOrder () {
      let p1 = getOtcorders('ZC', 'CNY', '1', '20', 2, 1)
      let p2 =  getOtcorders('ZC', 'CNY', '1', '20', 1, 1)

      Promise.all([p1,p2]).then(res=>{
        let sellOrders=res[0].orders
        let buyOrders=res[1].orders
        sellOrders.forEach(ele => {
          ele.type = 'sale'
        })
        buyOrders.forEach(ele => {
          ele.type = 'buy'
        })
        this.compOrders =[...sellOrders,...buyOrders]
      })
    },

    getCancelOtcOrder () {
      let p1 = getOtcorders('ZC', 'CNY', '1', '20', 2, 2)
      let p2 = getOtcorders('ZC', 'CNY', '1', '20', 1, 2)
      Promise.all([p1,p2]).then(res=>{
        let sellOrders = res[0].orders
        let buyOrders=res[1].orders
        sellOrders.forEach(ele => {
          ele.type = 'sale'
        })
        buyOrders.forEach(ele => {
          ele.type = 'buy'
        })
        this.cancelOrders = [...sellOrders,...buyOrders]
      })
    },
    selectTabClick (index) {
      this.selectTab = index
      switch (index) {
        case 0:
          this.getCurrentOtcOrder()
          break
        case 1:
          this.getCompleteOtcOrder()
          break
        case 2:
          this.getCancelOtcOrder()
          break
        default:
          break
      }
    },
    selectPayType (item) {
      this.$router.push({ name: 'Otctrade', params: { id: item.id, ad_type: 1 } })
    }
  }
}
</script>
