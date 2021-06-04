<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="assetsmanager">
        <div class="main-panel f-cb">
<!--          <asset-side-nav></asset-side-nav>-->
          <div class="content" id="view">
            <div class="inner-address-manager" id="innerAddressManager">
              <div class="header">
                <a href="#/myAssets" style="color: #196bdf">{{$t('user.Assets')}}</a>
                <i class="iconfont icon-angleright"></i>
                <span>{{$t('user.Address1')}}</span></div>
              <div class="addressmanager-main">
                <div class="am-inner">
                  <div class="coin-type input-wrap">
                    <div>{{$t('user.Coin')}}</div>
                    <select v-model="selectCoin" @change="getAddress">
                      <option value="" style="color: red">{{$t('user.PleaseSelectYourCurrency')}}</option>
                      <option :value="item" v-for="(item,index) in currencyLstArr" :key="index"> {{item}}</option>
                    </select>
                  </div>
                  <div class="coin-address input-wrap">
                    <div>{{$t('user.Withdrawss')}}</div>
                    <input type="text" 
                    :placeholder="$t('user.PleaseEnterTheWithdrawalAddress')" v-model.trim="address">
                  </div>
                  <div class="chain_container" v-if="selectCoin==='USDT'">
                    <div class="LinkName">{{$t('user.ChainName')}}</div>
                    <div class="LinkBot">
                      <div @click="changeSelect(0)">
                        <custom-radio :cur="select==='OMNI'">OMNI</custom-radio>
                      </div>
                      <div @click="changeSelect(1)">
                        <custom-radio :cur="select==='ERC20'">ERC20</custom-radio>
                      </div>
                    </div>
                  </div>
                  <div class="coin-note input-wrap">
                    <div>{{$t('user.Remark')}}</div>
                    <input type="text" 
                    :placeholder="$t('user.PleaseEnterRemarks')" v-model.trim="remark">
                  </div>
                  <div class="coin-note input-wrap" v-if="isInETH===2">
                    <div>{{$t('user.AddressSlab')}}</div>
                    <input type="text"
                           :placeholder="$t('user.PleaseFillInTheAddressLabel')" v-model.trim="addLab">
                  </div>
                  <div class="i-code-input code-input input-wrap f-cb">
                    <div>{{$t('user.PictureVerificationCode')}}</div>
                    <input class="" type="text"
                    :placeholder="$t('user.PleaseVerificationCode')" v-model.trim="imgCode">
                    <img class="img-code code" :src="backImgCode" @click="imgCodeClick">
                  </div>
                  <div class="m-code-input code-input input-wrap f-cb">
                    <div>{{$t('user.verificationCode')}}</div>
                    <input class="f-fl" type="text" 
                    :placeholder="$t('user.PleaseEnterTheverification')" v-model.trim="mobileCode">
                    <get-code @get-kaptcha="getKaptcha" :tokencode="tokencode" :imgCode="imgCode" :stop.sync="stop" type="forgot"></get-code>
                  </div>
                  <ul class="tips-lists" id="tipsLists">
                    <li>{{$t('user.GBTTips1')}}</li>
                    <li>{{$t('user.GBTTips2')}}</li>
                  </ul>
                  <button type="button" class="btn-add" id="addBtn" @click="addBtnClick">{{$t('user.Add')}}</button>
                </div>
              </div>
              <div class="address-list">
                <div class="al-title">{{$t('user.AddressList')}}</div>
                <div class="al-list">
                  <div class="al-list-header">
                    <span class="selectoption">
                      <select class="type" v-model="selectType" @change="getCurrencyList">
                        <option value="currency">{{$t('user.Coin')}}</option>
                        <option :value="item" v-for="(item,index) in currencyLstArr" :key="index"> {{item}}</option>
                      </select>
                    </span>
                    <span class="addr">{{$t('user.WithdrawAddress')}}</span>
                    <span class="note">{{$t('user.Remark')}}</span>
                    <span class="notes">{{$t('user.AddressSlab')}}</span>
                    <span class="operate">{{$t('user.Action')}}</span>
                  </div>
                  <div class="al-list-main" id="alListMain">
                    <dl v-for="(item,index) in addressList" :key="index">
                      <dd>
                        <div class="al-row">
                          <span>{{item.currency}}</span>
                          <span>{{item.addr}}</span>
                          <span>{{item.addr_label}}</span>
                          <span>{{item.addr_label}}</span>
                          <span @click="deleteAddress(item)" style="color: #196bdf">{{$t('user.Delete')}}</span>
                        </div>
                      </dd>
                    </dl>
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
import GetCode from '@/components/GetCode'
import CustomRadio from '@/components/customRadio'
import {getWithdrawAddr} from "@/api/withdraw";
import { getAssetsLst, getKaptcha, getMember, saveWithdrawAddr, getWithdrawAddrs, delWithdrawAddr } from '@/api/addressManage'
export default {
  components: {
    MyHeader,
    MyFooter,
    AssetSideNav,
    GetCode,
    CustomRadio
  },
  data () {
    return {
      currencyLstArr: '',
      selectCoin: '',
      selectType: 'currency',
      backImgCode: '',
      tokencode: '',
      useraccount: '',
      imgCode: '',
      address: '',
      remark: '',
      addLab: '',
      isInETH: "",
      mobileCode: '',
      addressList: [],
      stop: false,
      select:'ERC20'
    }
  },
  mounted () {
    this.getOption()
    this.getKaptcha()
    this.getCurrencyList()
    this.getAddress()
    getMember().then(res => {
      if (res.state === 1) {
        this.useraccount = res.data.m_name
      }
    })

  },
  methods: {
    changeSelect(index){
      switch (index) {
        case 0:
           this.select= 'OMNI'
          break
        case 1:
          this.select= 'ERC20'
          break
      }
    },
    getOption () {
      getAssetsLst().then(res => {
        if (res.state === 1) {
          let currencyLst = res.data.currencyLst
          let currencyLstArr = []
          for (let i in currencyLst) {
            currencyLstArr.push(currencyLst[i].currency)
          }
          this.currencyLstArr = currencyLstArr
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
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    },
    getKaptcha () {
      getKaptcha().then(res => {
        this.backImgCode = res.check_code_img
        this.tokencode = res.check_code_token
      })
    },
    imgCodeClick () {
      this.getKaptcha()
    },
    getAddress() {
      getWithdrawAddr(this.selectCoin, this.select).then(response => {
        if (response.state === 1) {
          let data = response.data;
          this.isInETH = response.is_in_eth;
          this.data = data;
        }
      });
    },
    addBtnClick () {
      if(this.remark.trim().length>=20){
        this.$message({
          type: 'error',
          message: this.$t('user.EnterRemarks'),
          duration: 3000,
          showClose: true
        })
        return
      }
      if (this.isInETH === 2){
        if (this.selectCoin.trim()&&this.remark.trim()&&this.address.trim()&&this.addLab.trim()&&this.mobileCode.trim()){
          saveWithdrawAddr(this.selectCoin.trim(), this.remark.trim(), this.address.trim(),this.mobileCode.trim(),this.addLab.trim(),this.select).then(res => {
            if (res.state === 1) {
              this.$message({
                type: 'success',
                message: this.$t('header.AddedSuccessfully'),
                duration: 3000,
                showClose: true
              })
              this.selectCoin = ''
              this.imgCode = ''
              this.address = ''
              this.remark = ''
              this.mobileCode = ''
              this.addLab = ''
              this.stop = true
              this.getCurrencyList()
            } else {
              this.getKaptcha()
            }
          })
        }else{
          this.$message({
            type: 'error',
            message: this.$t('register.PleaseCompleteTheFormInformation'),
            duration: 3000,
            showClose: true
          })
        }
      }else {
        if (this.selectCoin.trim()&&this.remark.trim()&&this.address.trim()&&this.mobileCode.trim()){
          saveWithdrawAddr(this.selectCoin.trim(), this.remark.trim(), this.address.trim(),this.mobileCode.trim(),this.select).then(res => {
            if (res.state === 1) {
              this.$message({
                type: 'success',
                message: this.$t('header.AddedSuccessfully'),
                duration: 3000,
                showClose: true
              })
              this.selectCoin = ''
              this.imgCode = ''
              this.address = ''
              this.remark = ''
              this.mobileCode = ''
              this.stop = true
              this.getCurrencyList()
            } else {
              this.getKaptcha()
            }
          })
        }else{
          this.$message({
            type: 'error',
            message: this.$t('register.PleaseCompleteTheFormInformation'),
            duration: 3000,
            showClose: true
          })
        }
      }

    },
    getCurrencyList () {
        getWithdrawAddrs(this.selectType).then(res => {
          if (res.state === 1) {
            if (res.data.length > 0) {
              let lists = res.data
              this.addressList = lists
            } else {
              this.addressList = []
            }
          }
        })
    },
    deleteAddress (item) {
      let data = {
        'id': item.id
      }
      delWithdrawAddr(data).then(res => {
        if (res.state === 1) {
          this.$message({
            type: 'success',
            message: this.$t('user.DeletedSuccessfully'),
            duration: 3000,
            showClose: true
          })
          this.getCurrencyList()
        }
      })
    }

  }
}
</script>
<style scoped>
.chain_container{
  /*display: flex;*/
  width: 900px;
  height: 85px;
  /*align-items: flex-start*/
}
/*.chain_container div{*/
/*  height:40px;*/
/*  width: 500px;*/
/*  !*display: flex;*!*/
/*  !*align-items: center*!*/
/*}*/
.LinkBot {
  display: flex;
  height: 52px;
  align-items: flex-start;
}
</style>
