<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
<!--          <asset-side-nav></asset-side-nav>-->
          <div class="content" id="view">

            <div class="inner-assets-record" id="innerAssetsRecord">
              <div class="header">
                <span class="">{{$t('user.Financial')}}</span>
              </div>
              <div class="tab-record">
                <!-- <span>全部</span> -->
                <span :class="selectTab===0?'cur':''" @click="tabClick(0)">{{$t('user.DepositHistory')}}
                  </span>
                <span :class="selectTab===1?'cur':''" @click="tabClick(1)">{{$t('user.WithdrawalHistory')}}
                  </span>
                <span :class="selectTab===2?'cur':''" @click="tabClick(2)">{{$t('user.LockHistory')}}
                  </span>
                <!-- <span>其他记录</span> -->
              </div>
              <div class="record-main assetsBOX">
                <div class="rm-list">

                  <div class="rm-recharge cur" id="recharge">

                    <div class="rm-list-header" v-show="selectTab===0">
                      <span class="date">{{$t('user.Dates')}}</span>
                      <span class="type">{{$t('user.Coin')}}</span>
                      <span class="category">{{$t('user.Type')}}</span>
                      <span class="number">{{$t('user.Num')}}</span>
                      <span class="truly">{{$t('user.Depositss')}}</span>
                      <span class="status">{{$t('user.Status')}}</span>
                      <span class="state">{{$t('header.CurrencyAddress')}}</span>
                    </div>
                    <!-- 充币记录 -->
                    <div class="rm-list-main">
                      <dl v-for="(item,index) in rechargeLst" :key="index" v-show="selectTab===0" @mouseover="rechargeMouseover" @mouseleave="rechargeMouseleave">
                        <dd>
                          <div class="rl-row">
                            <span>{{item.r_create_time}}</span>
                            <span>{{item.currency}}</span>
                            <span>{{$t('user.Deposit')}}</span>
                            <span>{{item.r_amount}}</span>
                            <el-tooltip  effect="dark" :content="item.r_address" placement="top" v-if="item.r_address.length>20">
                              <span class="roy_add">{{item.r_address.slice(0,10)}}......{{item.r_address.slice(item.r_address.length-10)}}</span>
                            </el-tooltip>
                            <span v-else>{{item.r_address}}</span>
                            <el-tooltip  effect="dark" :content="$t('header.ReasonCancellation')+item.reject_reason" placement="top-end" v-if="rechargeReason&&item.r_status===2">
                              <span class="status">{{item.state}}</span>
                            </el-tooltip>
                            <span class="status" v-else>{{item.state}}</span>
                            <el-tooltip  effect="dark" :content="item.r_txid" placement="top">
                              <span class="roy_add">{{item.r_txid == null?'--':item.r_txid}}</span>
                            </el-tooltip>
                          </div>
                        </dd>
                      </dl>
                    </div>

                    <div class="rm-list-header" v-show="selectTab===1">
                      <span class="date">{{$t('user.Dates')}}</span>
                      <span class="type">{{$t('user.Coin')}}</span>
                      <span class="category">{{$t('user.Type')}}</span>
                      <span class="number">{{$t('user.Num')}}</span>
                      <span class="truly">{{$t('user.Fee')}}</span>
                      <span class="state">{{$t('user.Withdrawss')}}</span>
                      <span class="status">{{$t('user.Status')}}</span>
                      <span class="state">{{$t('header.CurrencyAddress')}}</span>
                    </div>
                    <!-- 提币记录 -->
                    <div class="rm-list-main">
                      <dl v-for="(item,index) in withdrawLst" :key="index" v-show="selectTab===1"  @mouseover="withdrawMouseover" @mouseleave="withdrawMouseleave">
                        <dd>
                          <div class="rl-row">
                            <span>{{item.w_create_time}}</span>
                            <span>{{item.currency}}</span>
                            <span>{{$t('user.Withdraw')}}</span>
                            <span>{{item.w_amount}}</span>
                            <span v-if="item.currency!=='CNY'">{{item.w_fee}}</span>
                            <span v-else>{{item.otc_volume*5/1000}}</span>
                            <el-tooltip  effect="dark" :content="item.member_coin_addr" placement="top" v-if="item.member_coin_addr.length>20">
                              <span class="roy_add">{{item.member_coin_addr.slice(0,10)}}......{{item.member_coin_addr.slice(item.member_coin_addr.length-10)}}</span>
                            </el-tooltip>
                            <span v-else>{{item.member_coin_addr}}</span>
                            <!-- <span v-if="withDrawReason&&item.w_status===2">{{$t('header.ReasonCancellation')}}{{item.reject_reason}}</span> -->
                            <el-tooltip  effect="dark" :content="$t('header.ReasonCancellation')+item.reject_reason" placement="top-end" v-if="withDrawReason&&item.w_status===2">
                              <span class="status">{{item.state}}</span>
                            </el-tooltip>
                            <span class="status" v-else>{{item.state}}</span>
                            <el-tooltip  effect="dark" :content="item.w_txid" placement="top">
                              <span class="roy_add">{{item.w_txid == null?'--':item.w_txid}}</span>
                            </el-tooltip>
                          </div>
                        </dd>
                      </dl>
                    </div>

                    <div class="rm-list-header" v-show="selectTab===2">
                      <span class="date">{{$t('user.Dates')}}</span>
                      <span class="type">{{$t('user.Coin')}}</span>
                      <span class="category">{{$t('user.Type')}}</span>
                      <span class="number">{{$t('user.Num')}}</span>

                      <span class="truly">{{$t('user.HasBeenReleased')}}</span>
                      <span class="state">{{$t('user.UNRELEASED')}}</span>
                      <span class="status">{{$t('user.Status')}}</span>
                    </div>
                    <!-- 锁仓记录 -->
                    <div class="rm-list-main">
                      <dl v-for="(item,index) in Rows" :key="index" v-show="selectTab===2"  @mouseover="withdrawRockMouseover" @mouseleave="withdrawRockMouseleave">
                        <dd>
                          <div class="rl-row">
                            <span>{{item.createTime}}</span>
                            <span>{{item.currency}}</span>
                            <span>{{$t('user.Lock')}}</span>
                            <span>{{item.warehouseAmount}}</span>
                            <span>{{item.warehouseReleaseAmount}}</span>
                            <span>{{item.warehouseAmount - item.warehouseReleaseAmount}}</span>
                            <span class="status">{{item.state}}</span>
                          </div>
                        </dd>
                      </dl>
                    </div>

                    <div class="pages">
                      <div class="inner-pages f-cb">
                        <div class="flip f-fl">
                          <span class="prev" @click="prevClick">{{$t('user.PageUp')}}</span>
                          <span class="next pageNext" @click="nextClick">{{$t('user.PageDown')}}</span>
                        </div>
                        <div class="total f-fl">
                          {{$t('user.First')}}<span class="first">{{pageRecharge}}</span>{{$t('user.PpTotal')}}<span class="sum pageNext">{{rechareSum}}</span>{{$t('user.Page')}}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
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
import AssetSideNav from '@/components/AssetSideNav'
import { getAssetsRecharge, getAssetsWithDraw , getWarehouseList} from '@/api/assetsRecord'
import {getLang} from '../libs/utils.js'
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav
  },
  data () {
    return {
      pageRecharge: 1,
      pageWithdraw: 1,
      rechargeLst: [],
      withdrawLst:[],
      Rows:[],
      rechareSum: '',
      selectTab: 0,
      rechargeReason:false,
      withDrawReason:false,
      withDrawRockReason:false,
      lang:'zh',
    }
  },

  created(){
    let lang =getLang()
    if(lang) {
      this.lang =lang
    }
  },
  mounted () {
    this.getAssetsRecharge(0)
  },
  methods: {
    getAssetsRecharge (index) {
      const uid= localStorage.getItem("useruid")

      if (index === 0) {
        getAssetsRecharge(this.pageRecharge,uid).then(res => {
          if (res.state === 1) {
            let rechargeLst=res.data.rechargeLst
            rechargeLst.forEach(ele => {
              switch (ele.r_status) {
                case -1:
                  ele.state=this.$t('user.Obligation')
                  break;
                case 0:
                  ele.state=this.$t('user.Unconfirmed')
                  break;
                case 1:
                  ele.state=this.$t('user.Confirmed')
                  break;
                case 2:
                  ele.state=this.$t('user.Canceled')
                  break;
              }
            });
            this.rechargeLst =rechargeLst
            let totalRecharge = res.data.total
            this.rechareSum = Math.ceil(totalRecharge / 20)
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
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
        })
      } else if (index === 1){
        const uid= localStorage.getItem("useruid")

        getAssetsWithDraw(this.pageRecharge,uid).then(res => {
          if (res.state === 1) {
            let withdrawLst = res.data.withdrawLst
            withdrawLst.forEach(ele=>{
              ele.member_coin_addr=ele.member_coin_addr.replace('▲', ",标签：")

            })
            withdrawLst.forEach(ele => {
              switch (ele.w_status) {
                case -1:
                  ele.state=this.$t('user.Obligation')
                  break;
                case 0:
                  ele.state=this.$t('user.Unconfirmed')
                  break;
                case 1:
                  ele.state=this.$t('user.Confirmed')
                  break;
                case 2:
                  ele.state=this.$t('user.Canceled')
                  break;
              }
            });
            this.withdrawLst = withdrawLst
            let totalRecharge = res.data.total
            this.rechareSum = Math.ceil(totalRecharge / 20)
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
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
        })
      }else if(index === 2){
        getWarehouseList(this.pageRecharge).then(res => {
          if (res.state === 1) {
            let Rows = res.data.Rows
            Rows.forEach(ele => {
              switch (ele.isRelease) {
                case 0:
                  ele.state=this.$t('user.RELEASING')
                  break;
                case 1:
                  ele.state=this.$t('user.RELEASING_END')
                  break;
                case 2:
                  ele.state=this.$t('user.UNRELEASED')
                  break;
                case 3:
                  ele.state=this.$t('user.DELETED')
                  break;
              }
            });
            this.Rows = Rows
            let totalRecharge = res.total
            this.rechareSum = Math.ceil(totalRecharge / 20)
          } else if (res.state === -1) {
            if (res.msg === 'LANG_NO_LOGIN') {
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
        })
      }
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    prevClick () {
      this.pageRecharge--
      if (this.pageRecharge < 1) {
        this.pageRecharge++
        this.$message({
          type: 'error',
          message: this.$t('tradecenter.ThisIsPageOne'),
          duration: 3000,
          showClose: true
        })
      } else {
        // 加载上一页
        this.getAssetsRecharge(this.selectTab)
      }
    },
    nextClick () {
      this.pageRecharge++
      if (this.pageRecharge > this.rechareSum) {
        this.pageRecharge--
        this.$message({
          type: 'error',
          message: this.$t('tradecenter.ThisIsTheLastPage'),
          duration: 3000,
          showClose: true
        })
      } else {
        // 加载下一页
        this.getAssetsRecharge(this.selectTab)
      }
    },
    tabClick (index) {
      this.selectTab = index
      this.pageRecharge = 1
      this.getAssetsRecharge(index)
    },
    rechargeMouseover(){
      this.rechargeReason=true
    },
    rechargeMouseleave(){
      this.rechargeReason=false
    },
    withdrawMouseover(){
      this.withDrawReason=true
    },
    withdrawMouseleave(){
      this.withDrawReason=false
    },
    withdrawRockMouseover(){
      this.withDrawRockReason=true
    },
    withdrawRockMouseleave(){
      this.withDrawRockReason=false
    }
  }
}
</script>
<style scoped>
.roy_add{
  width: 100px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
