<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="container" id="usercenter">
        <div class="main-panel f-cb">
          <side-nav></side-nav>
          <div class="content" id="view">

            <div class="inner-identity-verify">
              <div class="header">{{$t('user.Verification')}}</div>
              <div class="identity-verify">
                <div class="step1" id="StepOne" v-show="stepOneShow">
                  <div class="tips">
                    <i></i>
                    <span>{{$t('user.AccordingTo')}}</span>
                  </div>
                  <div class="form-wrap">
                    <div class="input-wrap">
                      <label>{{$t('user.IDType')}}</label>
                      <select>
                        <option value="">{{$t('user.IdentityCard')}}</option>
                      </select>
                    </div>
                    <div class="input-tips"></div>
                    <div class="input-wrap">
                      <label>{{$t('user.FamilyName')}}</label>
                      <input type="text" :placeholder="$t('user.PleaseEnterName')" id="familyName" v-model="stepOneForm.familyName" @blur="familyNameBlur">
                    </div>
                    <div class="input-tips red" ><span v-show="tipShow.familyNameTipShow">{{$t('user.SurnameCannotBeEmpty')}}</span></div>
                    <div class="input-wrap">
                      <label>{{$t('user.Name')}}</label>
                      <input type="text" :placeholder="$t('user.PleaseEnterName')" id="givenName" v-model="stepOneForm.givenName"
                      @blur="givenNameBlur">
                    </div>
                    <div class="input-tips red"  ><span v-show="tipShow.givenNameTipShow">{{$t('user.TheNameCannotBeEmpty')}}</span></div>
                    <div class="input-wrap">
                      <label>{{$t('user.IDNumber')}}</label>
                      <input type="text" :placeholder="$t('user.PleaseEnterYourIdNumber')" id="idNumber" v-model="stepOneForm.idNumber" @blur="idNumberBlur">
                    </div>
                    <div class="input-tips red" ><span v-show="tipShow.idNumberTipShow">{{idNumberTipText}}</span></div>
                    <button type="button" id="s1Btn" @click="s1BtnClick">{{$t('user.Next')}}</button>
                  </div>
                </div>

                <div class="step2" id="StepTwo" v-show="stepTwoShow">
                  <div class="tips">
                    <i></i>
                    <ul>
                      <li>{{$t('user.TheFollowing')}}</li>
                      <li>{{$t('user.SubmitThis')}}</li>
                      <li>{{$t('user.YourApplication')}}</li>
                    </ul>
                  </div>
                  <div class="identification">
                    <div class="row row-1 f-cb">
                      <div class="user-upload f-fl">
                        <span>{{$t('user.TheFrontOfIDCard')}}</span>
                        <img :src="frontImgUrl" alt="" id="idFrontImgPreview">
                        <input type="file" class="file-upload" ref="idFrontImg" @change="frontImgChange">
                      </div>
                      <div class="example f-fl">
                        <span>{{$t('user.Example')}}</span>
                        <img src="https://bigen.oss-cn-shanghai.aliyuncs.com/images/identification-example-2.png" alt="">
                      </div>
                    </div>
                    <div class="row row-2 f-cb">
                      <div class="user-upload f-fl">
                        <span>{{$t('user.BackOfIdCard')}}</span>
                        <img :src="backImgUrl" alt="" id="idBackImgPreview">
                        <input type="file" class="file-upload" id="idBackImg" @change="backImgChange">
                      </div>
                      <div class="example f-fl">
                        <span>{{$t('user.Example')}}</span>
                        <img src="https://bigen.oss-cn-shanghai.aliyuncs.com/images/identification-example-1.png" alt="">
                      </div>
                    </div>
                    <div class="row row-3 f-cb">
                      <div class="user-upload f-fl">
                        <span>{{$t('user.HandIdentityCard')}}</span>
                        <img :src="handholdImgUrl" alt="" id="idHandheldImgPreview">
                        <input type="file" class="file-upload" id="idHandheldImg" @change="handholdImgChange">
                      </div>
                      <div class="example f-fl">
                        <span>{{$t('user.Example')}}</span>
                        <img src="https://bigen.oss-cn-shanghai.aliyuncs.com/images/identification-example-3.png" alt="">
                      </div>
                    </div>
                    <button type="button" id="s2Btn" @click="s2BtnClick">{{$t('user.ConfirmToSubmit')}}</button>
                  </div>
                </div>

                <div class="step3" id="StepThree" v-show="stepThreeShow">
                  <div class="tips f-cb">
                    <img class="waiting f-fl" src="../assets/images/waiting.png" alt="">
                    <p class="f-fl">{{$t('user.YourAuthentication')}}</p>
                  </div>
                </div>

                <div class="step4" id="StepFour" v-show="stepFourShow">
                  <div class="tips">
                    <div class="f-cb">
                      <img class="pass f-fl" src="../assets/images/pass.png" alt="">
                      <p class="f-fl">{{$t('user.YourIdentity')}}</p>
                    </div>

                    <div class="user-list">
                      <!-- <table>
                              <tr>
                                  <td>证件类型</td>
                                  <td>身份证</td>
                              </tr>
                              <tr>
                                  <td>证件号</td>
                                  <td>42********93</td>
                              </tr>
                              <tr>
                                  <td>姓名</td>
                                  <td>*勇</td>
                              </tr>
                          </table> -->
                    </div>
                  </div>
                </div>

                <div class="step5" id="StepFive" v-show="stepFiveShow">
                  <div class="tips">
                    <div class="f-cb">
                      <img class="denied f-fl" src="../assets/images/denied.png" alt="">
                      <p class="f-fl">{{$t('user.SorryYourIdentity')}}</p>
                    </div>

                    <ul class="suggestion">
                      <li>{{$t('user.PleaseUpload')}}</li>
                      <li>{{$t('user.PleaseEnsure')}}</li>
                    </ul>

                    <button @click="reSubmit">{{$t('user.Resubmit')}}</button>
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
import SideNav from '@/components/SideNav'
import { getAuthindentity, getAliOssPolicy, authIdentity, postImg } from '@/api/identityVerify.js'
export default {
  components: {
    MyHeader,
    MyFooter,
    SideNav
  },
  data () {
    return {
      stepOneShow: false,
      stepTwoShow: false,
      stepThreeShow: false,
      stepFourShow: false,
      stepFiveShow: false,
      stepOneForm: {
        familyName: '',
        givenName: '',
        idNumber: ''
      },
      tipShow: {
        familyNameTipShow: false,
        givenNameTipShow: false,
        idNumberTipShow: false
      },
      family_name_state: '',
      given_name_state: '',
      id_number_state: '',
      idNumberTipText: '',
      frontImgUrl: 'https://bigen.oss-cn-shanghai.aliyuncs.com/images/add.png',
      backImgUrl: 'https://bigen.oss-cn-shanghai.aliyuncs.com/images/add.png',
      handholdImgUrl: 'https://bigen.oss-cn-shanghai.aliyuncs.com/images/add.png'
    }
  },
  mounted () {
    getAuthindentity().then(res => {
      if (res.state === 1) {
        if (res.data === null) {
          this.stepOneShow = true
        } else {
          let userAuthState = res.data.id_status
          if (userAuthState === 0) {
            this.stepThreeShow = true
          } else if (userAuthState === 1) {
            this.stepFourShow = true
            // 用户详细信息
          } else if (userAuthState === 2) {
            this.stepFiveShow = true
          } else {
            this.stepOneShow = true
          }
        }
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
  methods: {
    familyNameBlur () {
      if (this.stepOneForm.familyName === '') {
        this.tipShow.familyNameTipShow = true
      } else {
        this.tipShow.familyNameTipShow = false
        this.family_name_state = true
      }
    },
    givenNameBlur () {
      if (this.stepOneForm.givenName === '') {
        this.tipShow.givenNameTipShow = true
      } else {
        this.tipShow.givenNameTipShow = false
        this.given_name_state = true
      }
    },
    idNumberBlur () {
      // let regIdCard = /^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\d{4}(([1][9]\d{2})|([2]\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\d{3}[0-9xX]$/
      if (this.stepOneForm.idNumber === '') {
        this.tipShow.idNumberTipShow = true
        this.idNumberTipText = this.$t('user.IdNumberCannotBeEmpty')
      }
      // else if (!regIdCard.test(this.stepOneForm.idNumber)) {
      //   this.idNumberTipText = this.$t('user.IdNumberIsNotCorrect')
      //   this.tipShow.idNumberTipShow = true
      // }
      else {
        this.tipShow.idNumberTipShow = false
        this.id_number_state = true
      }
    },
    s1BtnClick () {
      if (this.family_name_state && this.given_name_state && this.id_number_state) {
        this.stepOneShow = false
        this.stepTwoShow = true
      } else {
        this.$message({
          type: 'error',
          message: this.$t('user.PleaseCompleteTheFormInformation'),
          duration: 3000,
          showClose: true
        })
      }
    },
    frontImgChange (el) {
      getAliOssPolicy().then(res => {
        let data = res.data
        let file = el.target.files[0]
        let imagesType = ['image/jpeg', 'image/png']
        let isexists = imagesType.includes(file.type)
        if (!isexists) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileType'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileSize'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = JSON.parse(sessionStorage.getItem("useruid"));
        const fileName='web'+timestamp+useruid+'01'

        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});
        // 封装成 formData
        let date = Date.parse(new Date())
        let formData = new FormData()
        formData.append('OSSAccessKeyId', data.accessid)
        formData.append('policy', data.policy)
        formData.append('Signature', data.signature)
        formData.append('key', data.dir + useruid + newfile.name + date)
        formData.append('success_action_status', '200')
        formData.append('file', newfile)
        // 上传
        postImg(data.host, formData).then(res => {
          let url = data.host + '/' + data.dir + useruid + newfile.name + date
          this.frontImgUrl = url
        })
      })
    },
    backImgChange (el) {
      getAliOssPolicy().then(res => {
        let data = res.data
        let file = el.target.files[0]
        let imagesType = ['image/jpeg', 'image/png']
        let isexists = imagesType.includes(file.type)
        if (!isexists) {
          this.$message({
            type: 'error',
            message:  this.$t('otc.FileType'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message:  this.$t('otc.FileSize'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = JSON.parse(sessionStorage.getItem("useruid"));
       const fileName='web'+timestamp+useruid+'02'

        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});
        // 封装成 formData
        let date = Date.parse(new Date())
        let formData = new FormData()
        formData.append('OSSAccessKeyId', data.accessid)
        formData.append('policy', data.policy)
        formData.append('Signature', data.signature)
        formData.append('key', data.dir + useruid + newfile.name + date)
        formData.append('success_action_status', '200')
        formData.append('file', newfile)
        // 上传
        postImg(data.host, formData).then(res => {
          let url = data.host + '/' + data.dir + useruid + newfile.name + date
          this.backImgUrl = url
        })
      })
    },
    handholdImgChange (el) {
      getAliOssPolicy().then(res => {
        let data = res.data
        let file = el.target.files[0]
        let imagesType = ['image/jpeg', 'image/png']
        let isexists = imagesType.includes(file.type)
        if (!isexists) {
          this.$message({
            type: 'error',
            message:  this.$t('otc.FileType'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message:  this.$t('otc.FileSize'),
            duration: 3000,
            showClose: true
          })
          file = ''
          return
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = JSON.parse(sessionStorage.getItem("useruid"));
        const fileName='web'+timestamp+useruid+'03'
        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});

        // 封装成 formData
        let date = Date.parse(new Date())
        let formData = new FormData()
        formData.append('OSSAccessKeyId', data.accessid)
        formData.append('policy', data.policy)
        formData.append('Signature', data.signature)
        formData.append('key', data.dir + useruid + newfile.name + date)
        formData.append('success_action_status', '200')
        formData.append('file', newfile)
        // 上传
        postImg(data.host, formData).then(res => {
          let url = data.host + '/' + data.dir + useruid + newfile.name + date
          this.handholdImgUrl = url
        })
      })
    },
    s2BtnClick () {
      var addr1 = this.frontImgUrl
      var addr2 = this.backImgUrl
      var addr3 = this.handholdImgUrl
      var data = {
        nationality: '中国Chinese',
        family_name: this.stepOneForm.familyName,
        given_name: this.stepOneForm.givenName,
        id_number: this.stepOneForm.idNumber,
        idType: 'idcard',
        id_front_img: addr1,
        id_back_img: addr2,
        id_handheld_img: addr3
      }

      authIdentity(data).then(res => {
        // 请求成功跳转到等待页面
        if (res.state === 1) {
          this.stepTwoShow = false
          this.stepThreeShow = true
        } else if (res.state === -1) {
          setTimeout(function () {
            history.go(0)
          }, 3000)
        }
      })
    },
    reSubmit () {
      this.stepOneShow = true
      this.stepFiveShow = false
    },
    userLogout () {
      this.loginState = false
      localStorage.setItem('loginState', this.loginState)
      this.$router.push('/login')
    }

  }
}
</script>
<style scoped>
.red {
  color: red;
  transition: 0.6s;
}
</style>
