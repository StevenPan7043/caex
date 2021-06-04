<template>
  <div>
    <my-header></my-header>

    <div class="order-box">
      <div class="order-content main">
        <div class="order-title">
          合约委托订单
        </div>
        <div class="order-tab">
            <span
                    v-for="(item,index) in tabs"
                    :key="index+item"
                    :class="tabCur == index ? 'cur':''"
                    @click="tabTap(index)"
            >{{item}}</span>
        </div>

        <div class="trade-bottom">
          <div class="trade-bottom-head">
            <p class="first">委托单号</p>
            <p>币种</p>
            <p>类型</p>
            <p>委托价(USDT)</p>
            <p>委托数量(BTC)</p>
            <p>杠杆倍数(倍)</p>
            <p>时间</p>
            <p class="special">{{tabCur==0?'操作':'状态'}}</p>
          </div>
          <template v-if="WebSocketData.length != 0">
            <div class="trade-bottom-item" v-for="(item,index) in WebSocketData" :key="index">
              <p class="first">{{item.ordercode}}</p>
              <p>{{item.coin.toUpperCase().replace('_','/')}}</p>
              <p :style="item.type==1?'color:#29d190':'color:#da3f4d'">{{item.type==1?'开多':'开空'}}</p>
              <p>{{item.price}}</p>
              <p>{{item.coinnum}}</p>
              <p>{{item.gearing}}</p>
              <p v-if="tabCur == 0">{{item.createtimeStr}}</p>
              <p v-if="tabCur == 1">{{item.succestimeStr}}</p>
              <p v-if="tabCur == 2">{{item.canceltimeStr}}</p>
              <div class="cancel_button" @click.prevent="cancelDelegate(item)">
                <div v-if="tabCur==0">撤销</div>
                <p v-if="tabCur==1">委托成功</p>
                <p v-if="tabCur==2">已取消</p>
              </div>
            </div>
          </template>
          <div class="state-none" v-else>
            <img src="../static/images/none.png" alt/>
            <span>暂无订单</span>
          </div>
          <div class="loading-more" v-if="tabCur == 1 && WebSocketData2.length >= 5">
            <img
                    class="loading-animate"
                    src="../static/images/loading.png"
                    v-show="loadingType === 1"
            />
            <span
                    class="loading-text"
                    @click="loadingMore"
            >{{loadingType === 0 ? contentText.contentdown : (loadingType === 1 ? contentText.contentrefresh : contentText.contentnomore)}}</span>
          </div>
        </div>
      </div>
    </div>
    <!-- 修改止盈止损弹窗 -->
    <div class="popup-bg" v-if="coverStatus"></div>
    <div class="trade-popup" v-if="coverStatus">
      <div class="trade-popup-wrap">
        <div class="trade-popup-title">
          <span>
            修改·
            <span
                    :class="WebSocketData[stopIndex].type == 1?'green':'red'"
                    style="margin: 0 5px;"
            >{{WebSocketData[stopIndex].type == 1?'开多':'开空'}}</span>
            {{WebSocketData[stopIndex].coinname}}
          </span>
          <img src="../static/images/close.png" alt title="关闭" @click="coverClose"/>
        </div>
        <div class="trade-popup-item">
          <span>止盈价格</span>
          <div class="trade-popup-item-right">
            <input class="tc" type="tel" v-model="stopwin" placeholder="请输入止盈价格"/>
            <b>USDT</b>
          </div>
        </div>
        <div class="trade-popup-item">
          <span>止损价格</span>
          <div class="trade-popup-item-right">
            <input class="tc" type="tel" v-model="stopdonat" placeholder="请输入止损价格"/>
            <b>USDT</b>
          </div>
        </div>
        <div style="height: 10px;"></div>
        <div class="trade-popup-btn" @click="editConfirm">确定</div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>

<script>
import MyHeader from '@/components/Header'
import MyFooter from '@/components/Footer'
import {contractWsUr} from '../../public/config'

export default {
  components: {
    MyHeader,
    MyFooter
  },
  data () {
    return {
      tabs: ['委托中', '委托成功', '已取消'],
      tabCur: 0, //tab选中
      timer: null, //socket发送计时器
      WebSocketData: '', //socket数据
      // list:[],  //历史记录列表数据
      WebSocketData2: '', //历史记录列表
      deposit: 0, // 保证金
      CoverIndex: 0, //平仓的索引
      coverStatus: false,
      // 止盈
      stopwin: '',
      // 止损
      stopdonat: '',
      // 止损index
      stopIndex: 0,
      // ordercode
      ordercode: '',
      // 支付密码
      payWordStr: '',
      payWordStatus: false, //支付确认弹窗
      // 买入卖出type
      orderType: 0,
      //加载更多
      page: 1,
      loadingType: 0,
      contentText: {
        contentdown: '点击显示更多',
        contentrefresh: '正在加载...',
        contentnomore: '没有更多数据了'
      }
    }
  },
  created () {
  },
  mounted () {
    this.loadingType = 0
    this.page = 1
    //this.initSocket()
    this.tabTap(0)
    this.$ajax('/account/getMyInfos', {
      token: this.$userID()
    }).then(res => {
      if (res.data.status) {
        if (this.$route.name === 'Login') {
          this.$router.push('/')
        }
      } else {
        this.$router.push('/login')
      }
    })
  },
  beforeDestroy () {
    clearInterval(this.timer)
    this.timer = null
    this.socket.close()
  },
  methods: {
    // 初始化socket
    initSocket () {
      let socket = new WebSocket(contractWsUr + this.$userID())
      socket.onopen = () => {
        socket.send(this.$userID() + '_contractall')
        this.timer = setInterval(() => {
          socket.send(this.$userID() + '_contractall')
        }, 1500)
      }
      socket.onmessage = e => {
        // console.log(JSON.parse(e.data))
        this.WebSocketData = JSON.parse(e.data)
      }
      this.socket= socket
    },
    // tab选择
    tabTap (e) {
      this.tabCur = e
      this.loadingType = 0
      this.$ajax('/trade/queryMyEntrust', {
        token: this.$userID(),
        status: this.tabCur + 1,
        page: 1
      }).then(res => {
        if (res.data.status) {
          this.WebSocketData = res.data.data
        }
      })
    },
    // 修改止盈止损（弹窗）
    editTap (e) {
      this.stopIndex = e
      this.coverStatus = true
      //
      this.ordercode = this.WebSocketData[e].ordercode
    },
    // 修改确认操作
    editConfirm () {
      let rex = /^[0-9]+(.[0-9]{1,4})?$/
      if (this.stopwin == '' || this.stopdonat == '') {
        this.$alert('请输入价格', '', 1200)
      } else if (!rex.test(this.stopwin) || !rex.test(this.stopdonat)) {
        this.$alert('输入价格不正确', '', 1200)
      } else {
        this.$ajax('/trade/handleEditOrder', {
          token: this.$userID(),
          ordercode: this.ordercode,
          stopwin: this.stopwin,
          stopdonat: this.stopdonat
        }).then(res => {
          if (res.data.status) {
            this.$message({
              type: 'success',
              message: res.data.desc
            })
            this.coverStatus = false
          } else {
            this.$message({
              type: 'error',
              message: res.data.desc
            })
          }
        })
      }
    },
    // 止盈止损修改 弹窗关闭
    coverClose () {
      this.coverStatus = false
      this.stopwin = ''
      this.stopdonat = ''
    },
    // 平仓
    coverTap (e) {
      this.CoverIndex = e
      this.$confirm('是否要平仓?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$ajax('/trade/handleCloseOrder', {
          token: this.$userID(),
          ordercode: this.WebSocketData[this.CoverIndex].ordercode
        }).then(res => {
          if (res.data.status) {
            this.$message({
              type: 'success',
              message: res.data.desc
            })
            // 查询USDT余额
            this.$ajax('/account/getWallet', {
              token: this.$userID(),
              coin: 'USDT'
            }).then(res => {
              if (res.data.status) {
                this.usdt_num = res.data.data
              }
            })
          } else {
            this.$message({
              type: 'error',
              message: res.data.desc
            })
          }
        })
      })
    },
    // 加载更多
    loadingMore () {
      if (this.loadingType !== 0) {
        return
      }
      this.loadingType = 1

      this.$ajax('/trade/queryContractOrder', {
        token: this.$userID(),
        page: ++this.page,
        status: 2
      }).then(res => {
        if (res.data.status) {
          this.WebSocketData2 = this.WebSocketData2.concat(res.data.data)
        }
        setTimeout(() => {
          if (res.data.data.length == 0) {
            this.loadingType = 2
            return
          }
          this.loadingType = 0
        }, 300)
      })
    },
    // 取消委托订单
    cancelDelegate (item) {
      const coin = item.coin.toUpperCase().replace('_', '/')
      const type = item.type == 1 ? '开多' : '开空'
      this.$confirm(type + ' ' + coin + ' 是否取消该委托订单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$ajax('/trade/handleCancelEntrust', {
          token: this.$userID(),
          ordercode: item.ordercode
        }).then(
            res => {
              if (res.data.status) {
                this.$message({
                  type: 'success',
                  message: res.data.desc
                })
                this.$ajax('/trade/queryMyEntrust', {
                  token: this.$userID(),
                  status: this.tabCur + 1,
                  page: 1
                }).then(res => {
                  if (res.data.status) {
                    this.WebSocketData = res.data.data
                  }
                })
              } else {
                this.$message({
                  type: 'error',
                  message: res.data.desc
                })
              }
            })
      })

    },
  }
}
</script>

<style>
  /*  */
  .trade-popup {
    width: 472px;
    background: #fff;
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    z-index: 1001;
    border-radius: 6px;
  }

  .trade-popup-wrap {
    padding: 40px 48px;
  }

  .trade-popup-title {
    text-align: center;
    margin-bottom: 24px;
  }

  .trade-popup-title span {
    font-size: 24px;
    font-weight: bold;
  }

  .trade-popup-title img {
    position: absolute;
    right: 0;
    top: 0;
    padding: 15px;
    width: 28px;
    cursor: pointer;
  }

  .trade-popup-item {
    height: 48px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    font-size: 16px;
    margin-bottom: 16px;
  }

  .trade-popup-item span {
  }

  .trade-popup-item input {
    height: 100%;
    line-height: 48px;
    border: 0;
    outline: none;
    text-align: right;
  }

  .trade-popup-item input.tc {
    text-align: left;
  }

  .trade-popup-item-right {
    display: flex;
    align-items: center;
    height: 100%;
  }

  .trade-popup-item-right p {
  }

  .trade-popup-item-right b {
    margin-left: 10px;
  }

  .trade-popup-btn {
    height: 48px;
    background: rgba(255, 170, 0, 1);
    border-radius: 4px;
    text-align: center;
    line-height: 48px;
    font-size: 16px;
    cursor: pointer;
  }

  .trade-popup-btn:active {
    opacity: 1;
  }

  .trade-popup-btn:hover {
    opacity: 0.85;
  }

  /*  */
  .loading-more {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 50px;
  }

  .loading-text {
    color: #d1d5eb;
    cursor: pointer;
  }

  .loading-animate {
    animation: 1.2s linear loading infinite;
    width: 20px;
    height: 20px;
    margin-right: 4px;
  }

  @keyframes loading {
    from {
    }
    to {
      transform: rotate(360deg);
    }
  }

  /*  */

  .trade-bottom-tab {
    display: flex;
    width: 400px;
    height: 45px;
    line-height: 45px;
  }

  .trade-bottom-tab span {
    flex: 1;
    text-align: center;
    font-size: 16px;
    color: #778aa2;
    cursor: pointer;
  }

  .trade-bottom-tab span.cur {
    color: #ffaa00;
    position: relative;
  }

  .trade-bottom-tab span.cur:after {
    content: "";
    background: #ffaa00;
    position: absolute;
    left: 0;
    bottom: 0;
    height: 2px;
    width: 100%;
  }

  .trade-bottom-head {
    display: flex;
    height: 64px;
    align-items: center;
    background: #f5f8fa;
    padding: 0 30px;
  }

  .trade-bottom-head p {
    flex: 1;
    text-align: center;
    color: #91a4b7;
  }


  .trade-bottom-item {
    display: flex;
    height: 74px;
    align-items: center;
    border-bottom: 1px solid #ededed;
    padding: 0 30px;
    background-color: #fff;
  }

  .trade-bottom-item:hover {
    background-color: #f5f9fc;
  }

  .trade-bottom-item p {
    flex: 1;
    text-align: center;
    color: #333333;
    font-size: 12px;
  }

  .trade-bottom-item p span {
    display: block;
  }

  .trade-btn {
    flex-direction: column;
    display: flex;
    align-items: center;
  }

  .trade-bottom-item p a {
    width: 70px;
    height: 28px;
    text-align: center;
    line-height: 28px;
    background: #3387ff;
    font-size: 12px;
    color: white;
    transition: 0.25s all;
    border-radius: 5px;
  }

  .trade-bottom-item p a:nth-child(2) {
    margin-top: 6px;
  }

  .trade-bottom-item p a:hover {
    color: #fff;
    opacity: 0.8;
  }

  .red {
    color: #d14b64 !important;
  }

  .green {
    color: #02ad8f !important;
  }

  /*  */
  .order-box {
    padding: 16px 0;
    background: #f8f9fb;
    display: flex;
    width: 100%;
    justify-content: center;
  }

  .order-content {
    min-height: 700px;
    width: 1200px;
  }

  .order-head {
  }

  .order-title {
    font-size: 18px;
    color: #1c242c;
    display: flex;
    align-items: center;
    padding: 0px 0 0 30px;
    margin-bottom: 10px;
    background-color: #fff;
    height: 60px;
  }

  .order-title img {
    margin-right: 6px;
  }

  .order-tab {
    display: flex;
    height: 50px;
    background-color: #fff;

  }

  .order-tab span {
    width: 200px;
    text-align: center;
    line-height: 60px;
    font-size: 16px;
    color: #666666;
    cursor: pointer;
  }

  .order-tab span.cur {
    color: #3387ff;
    position: relative;
  }

  .order-tab span.cur:after {
    content: "";
    width: 100%;
    height: 2px;
    background: #3387ff;
    position: absolute;
    left: 0;
    bottom: 0;
  }

  .state-none {
    display: -ms-flexbox;
    padding-top: 20px;
    display: flex;
    -ms-flex-direction: column;
    flex-direction: column;
    -ms-flex-align: center;
    align-items: center;
  }

  .cancel_button {
    cursor: pointer;
    flex: 1;
    display: flex;
    justify-content: flex-end;
  }

  .cancel_button p {
    text-align: end;
  }


  .cancel_button div {
    width: 38px;
    height: 20px;
    border-radius: 2px;
    border: solid 1px #196bdf;
    color: #196bdf;
    line-height: 20px;
    text-align: center;
  }

  .cancel_button div:hover {
    border: solid 1px #196bdf;
    background-color: #196bdf;
    color: #fff;
    line-height: 20px;
    text-align: center;
  }

  .special {
    text-align: end !important;
  }

  .first {
    text-align: start !important;
  }
</style>
