<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="usercenter">
        <div class="main-panel f-cb">
<!--          <side-nav></side-nav>-->
          <div class="content" id="view">
            <div class="inner-api-manager" id="InnerApiManager">
              <div class="header">{{$t('user.APIManagement')}}</div>
              <div class="api-desc">
                <div class="api-descHeader">
                  <span>{{$t('user.CreateAPI')}}</span>
                </div>
                <div class="api-dialog" id="apiDialog">
                  <div class="dialog-content">
                    <div class="inner" id="Inner">
                      <div class="input-wrap api-name">
                        <div>{{$t('user.APIName')}}</div>
<!--                        <input type="text" placeholder="如：我的API" v-model.trim="apiForm.apiName">-->
                        <input type="text" v-model.trim="apiForm.apiName">
                      </div>
                      <div class="input-wrap api-ip">
                        <div>{{$t('user.WhiteListDress')}}</div>
<!--                        <input type="text" :placeholder="$t('user.IPSeparatedByEnglish')" @blur="apiIpBlur" v-model.trim="apiForm.apiIp">-->
                        <input type="text"  @blur="apiIpBlur" v-model.trim="apiForm.apiIp">
                      </div>
                      <div class="input-wrap img-code f-cb">
                        <div>{{$t('user.PictureVerificationCode')}}</div>
<!--                        <input type="text" class="code-input f-fl"-->
<!--                               :placeholder="$t('user.PleaseVerificationCode')" v-model.trim="apiForm.imgCode">-->
                        <input type="text" class="code-input f-fl"
                               v-model.trim="apiForm.imgCode">
                        <img :src="backImgCode" alt="" class="f-fl" @click="getImageCode">
                      </div>
                      <div class="input-wrap mobile-code f-cb">
                        <div>{{$t('user.VerificationCodeS')}}</div>
<!--                        <input type="text" class="code-input f-fl"-->
<!--                               :placeholder="$t('user.PleaseEnterYourMobile')" v-model.trim="apiForm.mobileCode">-->
                        <input type="text" class="code-input f-fl"
                                v-model.trim="apiForm.mobileCode">
                        <get-code @get-kaptcha="getKaptcha" :tokencode="tokencode" :imgCode="apiForm.imgCode" :stop.sync="stop" type="forgot"></get-code>
                      </div>
                      <div class="input-wrap currency-pwd">
                        <div>{{$t('user.MoneyPassword')}}</div>
<!--                        <input type="password" :placeholder="$t('user.PleaseEnterFundPassword')" v-model.trim="apiForm.assetPwd">-->
                        <input type="password"  v-model.trim="apiForm.assetPwd">
                      </div>
<!--                      <div class="input-wrap api-privilege">-->
<!--                        <div>{{$t('user.Privilege')}}</div>-->
<!--                        <input type="checkbox" value='0' v-model="apiForm.apiPrivilege">-->
<!--                        {{$t('user.Transaction')}}-->
<!--                        <input type="checkbox" value='1' v-model="apiForm.apiPrivilege">-->
<!--                        {{$t('user.Withdraw')}}-->
<!--                        <input type="checkbox" value="2"  disabled v-show="false">-->
<!--                      </div>-->
                      <ul class="tips-lists" id="tipsLists">
                        <li>{{$t('user.GBTTips3')}}</li>
                        <li>{{$t('user.GBTTips4')}}</li>
                        <li>{{$t('user.GBTTips5')}}</li>
                      </ul>
                      <button class="submit" @click="apiDialogSubmit">{{$t('user.Create')}}</button>
                    </div>
                  </div>
                </div>
              </div>
              <div class="api-list" id="apiList">
                <div class="api-list-header f-cb">
                  <span>{{$t('user.MyAPIkey')}}</span>
                </div>
                <div class="rm-list-header">
                  <span class="date">API ID</span>
                  <span class="type">{{$t('user.APIName')}}</span>
                  <span class="category">API Key</span>
                  <span class="number">{{$t('user.WhiteList')}}</span>
                  <span class="truly">{{$t('user.Privilege')}}</span>
                  <span class="status">{{$t('user.DeleteAPI')}}</span>
                </div>
                <div class="rm-list-mains">
                  <dl v-for="(item,index) in apiTokens" :key="index">
                    <dd>
                      <div class="rl-row">
                        <span>{{item.id}}</span>
                        <span>{{item.label}}</span>
                        <span>{{item.api_key}}</span>
                        <span>{{item.trusted_ip == null?'--':item.trusted_ip}}</span>
                        <span>{{$t('user.Withdraw')}}</span>
                        <span @click="deleteApiClick(item)" style="color: #196bdf">{{$t('user.Delete')}}</span>
                      </div>
                    </dd>
                  </dl>
                </div>
<!--                <table>-->
<!--                  <tbody>-->
<!--                    <tr v-for="(item,index) in apiTokens" :key="index">-->
<!--                      <td>{{item.id}}</td>-->
<!--                      <td>{{item.label}}</td>-->
<!--                      <td class="look">{{item.api_key}}</td>-->
<!--                      <td>{{item.trusted_ip}}</td>-->
<!--                      <td>-->
<!--                        <span v-if="item.api_privilege.indexOf('Accounts')!==-1">-->
<!--                          {{$t('user.Transaction')}}-->
<!--                        </span>-->
<!--                        <span v-if="item.api_privilege.indexOf('Withdraw')!==-1">-->
<!--                          {{$t('user.Withdraw')}}-->
<!--                        </span>-->
<!--                      </td>-->
<!--                      <td class="operate">-->
<!--                        <span class="edit" @click="editApiClick(item)">-->
<!--                          {{$t('user.Edit')}}-->
<!--                        </span>-->
<!--                        <span class="delete" @click="deleteApiClick(item)">-->
<!--                          {{$t('user.Delete')}}-->
<!--                        </span>-->
<!--                      </td>-->
<!--                    </tr>-->

<!--                  </tbody>-->
<!--                </table>-->
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="api-delete-dialog" id="apiDelDialog"  v-show="apiDelDialogShow">
        <div class="dialog-content">
          <div class="inner" id="Inner">
            <div class="title f-cb">
              <span></span>
              <img class="close_icon f-fr fa fa-times"  @click="closeApiDelDialog" src="../assets/icons/close.png" alt="">
            </div>
            <div class="input-wrap currency-pwd">
              <label>{{$t('user.MoneyPassword')}}</label>
              <input type="password" :placeholder="$t('user.PleaseEnterFundPassword')" v-model="deletePwd">
            </div>
            <button class="submit" @click="deleteSubmitClick">{{$t('user.Confirm')}}</button>
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
import SideNav from '@/components/SideNav'
import GetCode from '@/components/GetCode'
import { getKaptcha, apiToken, getApiTokens, putApiToken, deleteApiToken } from '@/api/apiManager'
export default {
  components: {
    MyHeader,
    MyFooter,
    SideNav,
    GetCode
  },
  data () {
    return {
      dialogTitle: '',
      // apiDialogShow: false,
      apiDelDialogShow: false,
      backImgCode: '',
      tokencode: '',
      getCodeBtnDisabled: false,
      ipNewArr: [],
      apiForm: {
        apiName: '',
        apiIp: '',
        imgCode: '',
        mobileCode: '',
        assetPwd: '',
        apiPrivilege: ['0','1', '2'],
        id: ''
      },
      apiTokens: '',
      addOrEdit: 0,
      deleteItem: '',
      deletePwd: '',
      stop: false
    }
  },
  mounted () {
    this.getImageCode()

    this.getApi()
  },
  methods: {
    getApi () {
      getApiTokens().then(res => {
        let apiTokens = res.apiTokens
        apiTokens.forEach(ele => {
          if (ele.api_privilege.indexOf('Order') !== -1) {
            ele.myAction = true
          } else {
            ele.myAction = false
          }
        })
        this.apiTokens = apiTokens
      })
    },
    createAPIClick () {
      this.addOrEdit = 0
      this.dialogTitle = this.$t('user.CreateAPI')
      // this.apiDialogShow = true
    },
    closeApiDelDialog () {
      this.apiDelDialogShow = false
    },
    closeApiDialog () {
      // this.apiDialogShow = false
      this.apiForm = {
        apiName: '',
        apiIp: '',
        imgCode: '',
        mobileCode: '',
        assetPwd: '',
        apiPrivilege: ['0','1', '2'],
        id: ''
      }
    },
    getImageCode () {
      this.getKaptcha()
    },
    getKaptcha () {
      getKaptcha().then(res => {
        this.backImgCode = res.check_code_img
        this.tokencode = res.check_code_token
      })
    },

    apiIpBlur () {
      let regex =
            '^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.' +
            '(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.' +
            '(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.' +
            '(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$'
      let regIP = new RegExp(regex)
      let ipNewArr = []
      let ipArr = this.apiForm.apiIp.split(',')
      for (let i = 0; i < ipArr.length; i++) {
        if (regIP.test(ipArr[i])) {
          ipNewArr.push(ipArr[i])
        }
      }
      this.ipNewArr = ipNewArr
    },
    apiDialogSubmit () {
      if (this.apiForm.apiName==''){
        this.$message({
          type: 'error',
          message: this.$t('user.Apiname'),
          duration: 3000,
          showClose: true
        })
        return false;
      }
      // else if (this.apiForm.apiIp == ''){
      //   this.$message({
      //     type: 'error',
      //     message: this.$t('user.Apiwhitelist'),
      //     duration: 3000,
      //     showClose: true
      //   })
      //   return false;
      // }
      else if (this.apiForm.imgCode == ''){
        this.$message({
          type: 'error',
          message: this.$t('register.ImageVerificationEmpty'),
          duration: 3000,
          showClose: true
        })
        return false;
      }else if (this.apiForm.mobileCode == ''){
        this.$message({
          type: 'error',
          message: this.$t('register.VerificationEmpty'),
          duration: 3000,
          showClose: true
        })
        return false;
      }else if (this.apiForm.assetPwd == ''){
        this.$message({
          type: 'error',
          message: this.$t('user.PleaseEnterPss'),
          duration: 3000,
          showClose: true
        })
        return false;
      }
      let privilegeArr = []
      let privilegeStr = ''
      if (this.apiForm.apiPrivilege.indexOf('0') !== -1) {
        privilegeArr.push('Accounts')
      }
      if (this.apiForm.apiPrivilege.indexOf('1') !== -1) {
        privilegeArr.push('Withdraw')
      }
      if (this.apiForm.apiPrivilege.indexOf('2') !== -1) {
        privilegeArr.push('Order')
      }
      privilegeStr = privilegeArr.toString()
      let data = {
        label: this.apiForm.apiName,
        trusted_ip: this.apiForm.apiIp,
        api_privilege: privilegeStr,
        sms_code: this.apiForm.mobileCode,
        security_pwd: this.apiForm.assetPwd,
        google_auth_code: 0
      }
      if (this.addOrEdit === 0) {
        apiToken(data).then(res => {
          if (res.state === 1) {
            let message='API Secret为:' + res.api_secret + '丢失将无法找回！'
            this.$alert(message, '创建成功！', {
              confirmButtonText: '确定',
            });
            this.closeApiDialog()
            this.getApi()
            this.stop = true
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
      } else {
        data.id = this.apiForm.id
        putApiToken(data).then(res => {
          if (res.state === 1) {
            this.$message({
              type: 'success',
              message: this.$t('user.ModifySuccessfully'),
              duration: 3000,
              showClose: true
            })
            this.closeApiDialog()
            this.getApi()
          } else if (res.state === -1) {
            this.getKaptcha()
          }
        })
      }
    },
    editApiClick (item) {
      this.addOrEdit = 1
      this.dialogTitle = this.$t('user.EditAPI')
      // this.apiDialogShow = true
      this.getImageCode()
      this.apiForm.apiName = item.label
      this.apiForm.id = item.id
      this.apiForm.apiIp = item.trusted_ip
      let power = item.api_privilege
      let powerArr = ['2']
      if (power.indexOf('Accounts') !== -1) {
        powerArr.push('0')
      }
      if (power.indexOf('Withdraw') !== -1) {
        powerArr.push('1')
      }
      this.apiForm.apiPrivilege = powerArr
    },
    deleteApiClick (item) {
      this.deleteItem = item
      this.apiDelDialogShow = true
    },
    deleteSubmitClick () {
      if(this.deletePwd===''){
          this.$message({
            type: 'error',
            message: this.$t('otc.PleaseEnterFundPassword'),
            duration: 3000,
            showClose: true
          })
          return;
      }
      let data = {
        id: this.deleteItem.id,
        security_pwd: this.deletePwd,
        google_auth_code: 0
      }
      deleteApiToken(data).then(res => {
        if (res.state === 1) {
          this.$message({
            type: 'success',
            message: this.$t('user.DeletedSuccessfully'),
            duration: 3000,
            showClose: true
          })
          this.closeApiDelDialog()
          this.getApi()
        }
      })
    }
  }
}
</script>
<style scoped>
.close_icon{
  width:30px;
  height: 30px;
}

</style>
<style>
.el-message-box{
  background-color: #ffffff;
  border-color: #30374f;
  box-shadow: 2px 2px 10px #000;
}
.el-message-box__message p{
  color: #5975d3;
}
.el-message-box__title span {
  color: #5975d3;
}
.el-message-box__btns button {
  background-color: #5f81ec;
  border-color: #5f81ec;
}
</style>
