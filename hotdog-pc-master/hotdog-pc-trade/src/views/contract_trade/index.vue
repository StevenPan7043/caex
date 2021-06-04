<template>
  <div>
    <my-header></my-header>
    <div class="bgm404" v-if="maintain"></div>
    <div v-if="!maintain">
      <div class="main_view" v-if="show">
        <div class="top">
          <div class="first">
            <symbol-container
              :symbol-list="symbolList"
              @changeSymbol="changeSymbol"
              :symbol="symbol"
              :coinPrice="coinPrice"
            />
            <assets
              :moneyMap="moneyMap"
              :assetsInfo="assetsInfo"
              @reloadAssets="getAssets"
            />
          </div>
          <div class="second">
            <k_line
              :table-data="tableData"
              :klineDetail="klineDetail"
              :klineList="klineList"
              @times="imtes"
              :symbol="symbol"
              :coinCny="coinCny"
            />
            <Trade
              :coinPrice="coinPrice"
              :symbol="symbol"
              :tableData="tableData"
              @reloadAssets="getAssets"
              @reloadDelegate="reloadDelegate"
            />
          </div>
          <div class="third">
            <Depth
              :bids="bids"
              :asks="asks"
              :depthlist="depthlist"
              :coin-price="coinPrice"
              :coin-cny="coinCny"
              :symbol="symbol"
            />
          </div>
        </div>
        <div class="bottom">
          <Order
            :qc-list="qcList"
            :zc-list="zcList"
            :wt-list="wtList"
            :symbol="symbol"
            @reloadAssets="getAssets"
            @reloadDelegate="reloadDelegate"
          />
        </div>
      </div>
      <div class="empty_main_view" v-else></div>
    </div>
    <my-footer></my-footer>
    <el-dialog
      :visible.sync="dialogVisible1"
      width="500px"
      :show-close="false"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <div slot="title" class="dialog_title">
        <p>
          <span class="my_dialog_title">邀请码（必填）</span>
          <span class="my_dialog_sub_title">
            如无邀请码请点击
            <span
              style="color: #196bdf; cursor: pointer"
              @click="inputDefaultInviteCode"
              >官方默认邀请码</span
            >
          </span>
        </p>
      </div>
      <el-input v-model="inviteCode"></el-input>
      <span slot="footer" class="dialog-footer1">
        <el-button @click="disagree">我再想想</el-button>
        <el-button type="primary" @click="inviteCodeSubmit">下一步</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogVisible"
      width="500px"
      :show-close="false"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
    >
      <div slot="title" class="dialog_title">
        <p>永续合约开启确认</p>
      </div>
      <p class="tip_text">
        风险提示：永续合约通过引入杠杆，将使您的投资收益和风险同步放大，请在自身风险可承受范围内谨慎使用。
      </p>
      <div>
        <el-checkbox v-model="form.check">
          <p class="check_text">
            我已了解并同意
            <a
              target="_blank"
              href="https://hotdogvip.zendesk.com/hc/zh-cn/articles/360050745533-%E4%B8%80-%E5%90%88%E7%BA%A6%E4%BA%A4%E6%98%93%E5%B9%B3%E5%8F%B0%E6%9C%8D%E5%8A%A1%E5%8D%8F%E8%AE%AE"
              >《合约交易平台服务协议》</a
            >，确认开启永续合约交易
          </p>
        </el-checkbox>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="disagree">暂不开启</el-button>
        <el-button type="primary" @click="agree">开启交易</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import MyHeader from '@/components/Header/index.vue'
import MyFooter from '@/components/Footer'
import SymbolContainer from './components/symbol'
import Assets from './components/assets'
import Order from './components/order'
import k_line from './components/kLine/k_line'
import Trade from './components/trade'
import Depth from './components/depth'
import { baseUrl, contractWsUr } from '../../../public/config'
import { getMember } from '../../api/login'

import { bindingInviteCode } from '@/api/contract.js'
import { getUsdtPrice } from '@/api/myAssets'

import request from '@/libs/axios'
import pako from 'pako'
var that = this
export default {
  metaInfo() {
    return {
      title: this.title,
    }
  },
  name: 'trade_index',
  components: {
    MyHeader,
    MyFooter,
    SymbolContainer,
    Assets,
    Order,
    k_line,
    Trade,
    Depth,
  },
  data() {
    return {
      maintain: false,
      show: false,
      qcList: [],
      zcList: [],
      depthlist: null,
      timer: null,
      timerDelegate: null,
      asks: null,
      bids: null,
      moneyMap: {},
      coinPrice: 0,
      coinCny: 0,
      tableData: {
        dto: {},
      },
      symbolList: [],
      symbol: 'BTC/USDT',
      oldSymbol: 'BTC/USDT',
      oldSymbol1: 'BTC/USDT',
      socket: null,
      houbisocket: null,
      houbisocketHotdog: null,
      dialogVisible1: false,
      dialogVisible: false,
      dialogVisible2: false,
      klineDetail: null,
      klineList: null,
      form: {
        check: false,
      },
      title: 'Hotdog | 全球领先数字资产交易平台',
      inviteCode: '',
      member: '',
      assetsInfo: {},
      // 委托订单
      wtList: [],
      // 委托订单页数
      wtPage: 1,
      resolution: '15min',
      UsdtPrice: 0,
    }
  },
  beforeCreate() {
    // this.$router.replace('maintain')
  },
  mounted() {
    // this.$router.replace('maintain')
    this.getUsdt()
    const { token, contactToken } = this.$router.history.current.query
    if (token && contactToken) {
      localStorage.setItem('loginState', true.toString())
      localStorage.setItem('contactToken', contactToken)
      localStorage.setItem('token', token)
      this.$router.push('/trade')
    }
    this.initSocket()
    this.initHoubiSocket()
    // this.initHotdogSocket()//深度
    this.checkHasContract()
    this.getAssets()
    this.reloadDelegate()
    this.timerDelegate = setInterval(() => {
      this.reloadDelegate()
    }, 1500)
  },
  beforeDestroy() {
    clearInterval(this.timer)
    clearInterval(this.timerDelegate)
    this.timer = null
    this.timerDelegate = null
    this.socket.close()
    this.houbisocket.close()
    // this.houbisocketHotdog.close()
  },
  methods: {
    imtes(resolution) {
      let that = this
      if (resolution != this.resolution) {
        this.onSocketUnConnect(
          this.houbisocket,
          this.oldSymbol,
          this.symbol,
          resolution
        )
      }
      this.resolution = resolution
    },
    reloadDelegate() {
      this.wtList = []
      this.wtPage = 1
      this.$ajax('/trade/queryMyEntrust', {
        token: this.$userID(),
        status: 1,
        page: this.wtPage,
      }).then((res) => {
        if (res.data.status) {
          this.wtList = [...this.wtList, ...res.data.data]
        }
      })
    },
    async getUsdt() {
      const res = await getUsdtPrice()
      ;[this.UsdtPrice] = res.USDT_CNY
      const ETH_USDT = res.ETH_USDT[0]
      const BTC_USDT = res.BTC_USDT[0]
      this.BTCPrice = this.UsdtPrice * BTC_USDT
      this.ETHPrice = this.UsdtPrice * ETH_USDT
    },
    getAssets() {
      this.$ajax('/account/getMyInfos', {
        token: this.$userID(),
      }).then((res) => {
        if (res.data.status) {
          if (this.$route.name === 'Login') {
            this.$router.push('/')
          }
          this.assetsInfo = res.data.data
        }
      })
    },
    async inviteCodeSubmit() {
      if (!this.inviteCode) {
        this.$message({
          type: 'error',
          message: '请输入邀请码',
          duration: 3000,
          showClose: true,
        })
        return
      }
      let userRes = await getMember()
      if (userRes.state === 1) {
        let userInfo = userRes.data
        const inviteCode = userInfo.invite_code
        if (inviteCode === this.inviteCode) {
          this.$message({
            type: 'error',
            message: '不能使用自己的邀请码',
            duration: 1200,
            showClose: true,
          })
        } else {
          const res = await bindingInviteCode(this.inviteCode)
          if (res.state === 1) {
            this.dialogVisible1 = false
            if (this.member.isValid !== 0) {
              this.dialogVisible = true
            } else {
              this.show = true
            }
          }
        }
      }
    },
    inputDefaultInviteCode() {
      this.inviteCode = 'dbUJ25W3'
    },
    disagree() {
      this.$router.go(-1)
    },
    agree() {
      if (!this.form.check) {
        this.$message({
          type: 'error',
          message: '请先同意并勾选协议',
          duration: 3000,
          showClose: true,
        })
        return
      }
      request({
        url: baseUrl + 'contract/editCustomer',
        method: 'get',
        params: {
          isValid: 0,
        },
      }).then((res) => {
        if (res.state === 1) {
          this.dialogVisible = false
          // this.show=true

          location.reload()
          localStorage.setItem('contactToken', res.data.contactToken)
        }
      })
    },
    // 是否开启合约
    checkHasContract() {
      request({
        url: baseUrl + '/m/member',
        method: 'get',
      }).then((res) => {
        if (res.msg === 'LANG_NO_LOGIN') {
          this.$message({
            type: 'error',
            message: this.$t('header.PlaseLogin'),
            duration: 3000,
            showClose: true,
          })
          this.userLogout()
        }

        this.member = res.data
        // if (!res.data.introduce_m_id) {
        //   this.dialogVisible1 = true
        // } else
        if (res.data.isValid !== 0) {
          this.dialogVisible = true
        } else {
          this.show = true
        }
      })
    },

    userLogout() {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    changeSymbol(item) {
      if (item === this.symbol) {
        return
      }
      this.onSocketUnConnect(this.houbisocket, this.oldSymbol, item, '15min')
      // this.onSocketHotdogUnConnect(
      //   this.houbisocketHotdog,
      //   this.oldSymbol1,
      //   item,
      //   '15min'
      // )
      this.symbol = item
    },

    //1-----------------1
    initHotdogSocket() {
      var that = this
      let houbisocketHotdog = new WebSocket('wss://api.huobiasia.vip/ws') //wss://api.huobiasia.vip/ws wss://futures.huobi.me/swap-ws wss://futures.huobi.me/ws https://api.hotdogvip.com
      houbisocketHotdog.onopen = () => {
        this.onSocketHotdogConnect(
          houbisocketHotdog,
          this.oldSymbol,
          this.symbol
        )
      }
      houbisocketHotdog.onmessage = (event) => {
        let blob = event.data
        let reader = new FileReader()
        reader.onload = function (e) {
          let ploydata = new Uint8Array(e.target.result)
          let msg = pako.inflate(ploydata, { to: 'string' })
          var res = JSON.parse(msg)
          if (res !== undefined && res.tick !== undefined) {
            that.klineDetail = res.tick
            that.title =
              res.tick.close +
              ' ' +
              that.symbol +
              that.$t('contract.Sustainable') +
              ' Hotdog | 全球领先数字资产交易平台'
          }
          // if (res !== undefined && res.data != null) {
          //   console.log(res, '请求 K线数据')
          //   that.klineList = res.data
          // }
          if (res.ping) {
            houbisocketHotdog.send(JSON.stringify({ pong: res.ping }))
          }
        }
        reader.readAsArrayBuffer(blob, 'utf-8')
      }
      this.houbisocketHotdog = houbisocketHotdog
    },
    //1-----------------1
    onSocketHotdogConnect(houbisocketHotdog, item, symbolok) {
      let symbol = symbolok
      if (symbol != this.symbol) {
        this.oldSymbol1 = symbol
      }
      let symbols = symbol.split('/').toString().replace(/,/g, '').toLowerCase()
      let symbolsold = symbol.replace('/', '-')
      let symbolst = symbolsold.replace('-USDT', '-USD')
      let curTime = new Date().getTime()
      let startDate = curTime - 3 * 3600 * 24 * 1000

      let from = Math.round(startDate / 1000)
      let to = Math.round(curTime / 1000)
      // let action1 = {
      //   id: startDate + '.1.0',
      //   req: 'market.' + symbolst + '.kline.' + this.resolution,
      //   from: 1597221558,
      //   to: 1598517558,
      // }

      // houbisocketHotdog.send(JSON.stringify(action1))

      let action = {
        sub: 'market.' + symbolst + '.kline.' + this.resolution,
        id: 'id4',
      }
      // {
      //   sub: 'market.' + symbols + '.kline.' + this.resolution,
      //   id: 'id10',
      // }
      houbisocketHotdog.send(JSON.stringify(action))
    },
    //1-----------------1
    onSocketHotdogUnConnect(houbisocketHotdog, item, symbolok, time) {
      let symbol = item
      let symbols = symbol.split('/').toString().replace(/,/g, '').toLowerCase()
      let symbolsold = symbol.replace('/', '-')
      let symbolst = symbolsold.replace('-USDT', '-USD')
      let action = {
        unsub: 'market.' + symbolst + '.kline.' + this.resolution,
        id: 'id4',
      }
      houbisocketHotdog.send(JSON.stringify(action))
      //重新订阅
      this.resolution = time
      this.onSocketHotdogConnect(houbisocketHotdog, item, symbolok)
    },

    //2----------------2
    //深度
    initHoubiSocket() {
      var that = this
      let houbisocket = new WebSocket('wss://api.huobiasia.vip/ws')
      houbisocket.onopen = () => {
        this.onSocketHoubiConnect(houbisocket, this.oldSymbol, this.symbol)
      }
      houbisocket.onmessage = (event) => {
        let blob = event.data
        let reader = new FileReader()
        reader.onload = function (e) {
          let ploydata = new Uint8Array(e.target.result)
          let msg = pako.inflate(ploydata, { to: 'string' })
          var res = JSON.parse(msg)
          // if (res != null) {
          //   if (res.id == '1599641322365') {
          //     that.klineList = res.data
          //   }
          //   if (res.ch != null && res.ch.indexOf('market.overviewv2') !== -1) {
          //     console.log(res)
          //   }
          // }
          if (
            res !== undefined &&
            res.tick !== undefined &&
            res.ch !== undefined
          ) {
            if (res.ch.indexOf('.detail') !== -1) {
              that.coinPrice = res.tick.close
              that.coinCny = (
                res.tick.close * (that.UsdtPrice !== 0 ? that.UsdtPrice : 6.855)
              ).toFixed(2)
              // that.tableData = res.tick
              that.title =
                res.tick.close +
                ' ' +
                that.symbol +
                that.$t('contract.Sustainable') +
                ' Hotdog | 全球领先数字资产交易平台'
            }
            if (res.ch.indexOf('.depth.step0') !== -1) {
              that.asks = res.tick.asks
              that.bids = res.tick.bids
            }
            if (res.ch.indexOf('.kline') !== -1) {
              that.klineDetail = res.tick
            }
          }
          //深度数据
          if (res.ping) {
            houbisocket.send(JSON.stringify({ pong: res.ping }))
          }
        }
        reader.readAsArrayBuffer(blob, 'utf-8')
      }
      this.houbisocket = houbisocket
    },
    //2----------------2
    onSocketHoubiConnect(houbisocket, item, symbolok, resolution) {
      let symbol = symbolok
      if (symbol != this.symbol) {
        this.oldSymbol = symbol
      }
      let times = resolution != undefined ? resolution : this.resolution
      let symbols = symbol.split('/').toString().replace(/,/g, '').toLowerCase()
      let symbolsold = symbol.replace('/', '-')
      let symbolst = symbolsold.replace('-USDT', '-USD')
      let symbolst_depth = symbol.replace('/', '').toLowerCase()
      let curTime = new Date().getTime()
      let startDate, from, to
      if (times == '15min') {
        startDate = curTime - 5 * 3600 * 24 * 1000
      } else if (times == '5min') {
        startDate = curTime - 0.5 * 3600 * 24 * 1000
      } else if (times == '1min') {
        startDate = curTime - 0.2 * 3600 * 24 * 1000
      }
      from = Math.round(startDate / 1000)
      to = Math.round(curTime / 1000)

      // {"sub":"market.btcusdt.detail","symbol":"btcusdt"}	50
      // 16:33:52.284
      // {"sub":"market.btcusdt.depth.step0","symbol":"btcusdt","pick":["bids.29","asks.29"],"step":"step0"}	99
      // 16:33:52.328
      // {"req":"market.btcusdt.trade.detail","symbol":"btcusdt"}	56
      // 16:33:52.357
      // {"sub":"market.btcusdt.trade.detail","symbol":"btcusdt"}
      // {"req":"market.btcusdt.kline.15min","id":"kdfxiovdido.1599640433920.1.0","from":1599370433,"to":1599640433}
      // {"id":"kdfxiovdido.btcusdt_15","sub":"market.btcusdt.kline.15min"}
      // {"id":"kdfxiovdido.btcusdt_15","sub":"market.btcusdt.kline.15min"}
      // {"sub":"market.btcusdt.trade.detail","symbol":"btcusdt"}
      // {"sub":"market.btcusdt.trade.detail","symbol":"btcusdt"}
      // {"unsub":"market.btcusdt.trade.detail"}
      // {"unsub":"market.btcusdt.trade.detail"}

      let actions1 = {
        id: 'mff1s18chsc.' + symbolst_depth + '_' + times,
        sub: 'market.' + symbolst_depth + '.kline.' + times,
      }
      houbisocket.send(JSON.stringify(actions1))
      // let actions3 = {
      //   req: 'market.' + symbolst_depth + '.kline.' + times,
      //   id: '1599641322365',
      //   from: from,
      //   to: to,
      // }
      // houbisocket.send(JSON.stringify(actions3))
      let actions4 = {
        sub: 'market.' + symbolst_depth + '.detail',
        symbol: symbolst_depth,
      }
      houbisocket.send(JSON.stringify(actions4))
      let actions5 = {
        sub: 'market.' + symbolst_depth + '.depth.step0',
        zip: 1,
      }
      houbisocket.send(JSON.stringify(actions5))
      // let actions7 = {
      //   req: 'market.symbols',
      // }
      // houbisocket.send(JSON.stringify(actions7))
      // let actions8 = {
      //   sub: 'market.overviewv2',
      // }
      // houbisocket.send(JSON.stringify(actions8))
    },
    //2----------------2
    onSocketUnConnect(houbisocket, item, symbolok, resolution) {
      console.log(item, 'item')
      console.log(symbolok, 'symbolok')
      let symbolst_depth = item.replace('/', '').toLowerCase()
      let symbolsold = item.replace('/', '-')
      let symbolst = symbolsold.replace('-USDT', '-USD')
      let actions2 = {
        unsub: 'market.' + symbolst_depth + '.detail',
      }
      houbisocket.send(JSON.stringify(actions2))
      let actions1 = {
        unsub: 'market.' + symbolst_depth + '.depth.step0',
      }
      houbisocket.send(JSON.stringify(actions1))
      let actions6 = {
        id: 'mff1s18chsc.' + symbolst_depth + '_' + this.resolution,
        unsub: 'market.' + symbolst_depth + '.kline.' + this.resolution,
      }
      houbisocket.send(JSON.stringify(actions6))
      //重新订阅
      this.onSocketHoubiConnect(houbisocket, item, symbolok, resolution)
    },
    // 初始化socket

    //3---------------3
    initSocket() {
      let socket = new WebSocket(contractWsUr + this.$userID())
      socket.onopen = () => {
        this.onSocketConnect(socket)
      }
      socket.onmessage = (e) => {
        const res = JSON.parse(e.data)
        if (res instanceof Array) {
          this.symbolList = res
        } else if (res.moneyMap) {
          // this.qcList = res.list
          // this.zcList = res.zclist
          this.moneyMap = res.moneyMap
          // this.asks = res.asks
          // this.bids = res.bids
          // this.coinPrice = res.coinPrice
          // this.coinCny = res.coinCny
        } else if (
          res.qcPosition ||
          (res.zcPosition && res instanceof Object && res.length == 2)
        ) {
          this.qcList = res.qcPosition
          this.zcList = res.zcPosition
        } else if (res.dto) {
          this.tableData = res.dto
        }
      }
      this.socket = socket
    },

    //3---------------3
    onSocketConnect(socket) {
      socket.send(this.$userID() + '_depth_' + this.symbol)
      socket.send(this.$userID() + '_home')
      socket.send(this.$userID() + '_kline_' + this.symbol)
      socket.send(this.$userID() + '_position_')
      this.timer = setInterval(() => {
        socket.send(this.$userID() + '_home')
        socket.send(this.$userID() + '_depth_' + this.symbol)
        socket.send(this.$userID() + '_kline_' + this.symbol)
        socket.send(this.$userID() + '_position_')
      }, 400)
    },
  },
}
</script>

<style scoped>
.bgm404 {
  width: 100%;
  height: 750px;
  background-image: url('../../assets/images/404.png');
  background-size: 100%;
}
.main_view {
  width: 100%;
  min-width: 1200px;
  background-color: #f2f6f9;
}

.empty_main_view {
  width: 100%;
  min-width: 1200px;
  height: 1200px;
  background-color: #f2f6f9;
}

.top {
  margin: 6px 0;
  display: flex;
}

.first {
  width: 330px;
  margin-right: 6px;
}

.second {
  width: calc(100% - 660px);
  min-width: 480px;
}

.third {
  width: 330px;
  margin-left: 6px;
}

.bottom {
  width: 100%;
  min-width: 1200px;
  height: 460px;
}

.dialog_title p {
  font-size: 18px;
  color: #25425a;
  line-height: 28px;
}

.dialog_title div {
  font-size: 12px;
  color: #ea4872;
  line-height: 22px;
}

.dialog-footer1 {
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: center;
}

.check_text {
  font-size: 14px;
  color: #25425a;
}

.check_text a {
  color: #196bdf;
}

.tip_text {
  font-size: 14px;
  color: #25425a;
  margin-bottom: 60px;
}

.my_dialog_title {
  font-size: 18px;
  color: #25425a;
}

.my_dialog_sub_title {
  font-size: 12px;
  color: #25425a;
}
</style>
