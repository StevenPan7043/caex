<template>
  <div class="top_container">
    <div class="title_container">
      <p><span><router-link to="/user">个人中心</router-link></span>>实名认证</p>
    </div>
    <div class="step_one" v-show="stepOneShow">
      <ul class="text_container">
        <li>请使用本人真实身份认证，用户需实名认证才能进行充值/提现等操作。</li>
      </ul>
      <div class="one_form_container">
        <Form :model="formTop" label-position="top" class="form_style">
          <FormItem label="证件类型">
            <Select v-model="formTop.type" >
              <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
            </Select>
          </FormItem>
          <FormItem label="姓">
            <Input v-model="formTop.familyName"/>
          </FormItem>
          <FormItem label="名">
            <Input v-model="formTop.givenName"/>
          </FormItem>
          <FormItem label="证件号码">
            <Input v-model="formTop.idNumber"/>
          </FormItem>
          <Button type="primary" long  @click="stepOneClick">下一步</Button>
        </Form>
      </div>
    </div>
    <div class="step_two" v-show="stepTwoShow">
      <ul class="text_container">
        <li>以下材料需用 JPG / JPEG / PNG 格式上传，并保证照片清晰、边角完整、亮度均匀，且大小不得超过5M；</li>
        <li>提交本申请代表承诺你的身份信息真实可靠，不存在盗用他人信息资料的情况；</li>
        <li>您的提交申请我们将在一个工作日内进行审核处理，并告知审核处理结果。</li>
      </ul>
      <div class="flex_container">
        <div class="pic_container">
          <div class="title">本人身份证正面照片</div>
          <div class="pic">
            <div class="left">
              <div class="flex-container" v-show="!frontImgUrl" >
                <img class="img" src="../../assets/images/addImg.png" alt="">
                <input type="file" ref="idFrontImg" @change="frontImgChange" class="file_upload" >
              </div>
              <div class="text" v-show="!frontImgUrl">
                * 证件需在有效期内，照片文字清晰可见支持JPG/JPEG/PNG等图片格式
              </div>
              <img :src="frontImgUrl"  alt="" v-show="frontImgUrl">
            </div>
            <div class="right">
              <img src="../../assets/images/auth1.png" alt="">
            </div>
          </div>
        </div>

        <div class="pic_container">
          <div class="title">本人身份证背面照片</div>
          <div class="pic">
            <div class="left">
              <div class="flex-container" v-show="!backImgUrl" >
                <img class="img" src="../../assets/images/addImg.png" alt="">
                <input type="file" ref="idFrontImg" @change="backImgChange" class="file_upload" >
              </div>
              <div class="text" v-show="!backImgUrl">
                * 证件需在有效期内，照片文字清晰可见支持JPG/JPEG/PNG等图片格式
              </div>
              <img :src="backImgUrl"  alt="" v-show="backImgUrl">
            </div>
            <div class="right">
              <img src="../../assets/images/auth2.png" alt="">
            </div>
          </div>
        </div>

        <div class="pic_container">
          <div class="title">手持身份证 + 手写日期 </div>
          <div class="pic">
            <div class="left">
              <div class="flex-container" v-show="!handholdImgUrl" >
                <img class="img" src="../../assets/images/addImg.png" alt="">
                <input type="file" ref="idFrontImg" @change="handholdImgChange" class="file_upload" >
              </div>
              <div class="text" v-show="!handholdImgUrl">
                * 证件需在有效期内，照片文字清晰可见支持JPG/JPEG/PNG等图片格式
              </div>
              <img :src="handholdImgUrl"  alt="" v-show="handholdImgUrl">
            </div>
            <div class="right">
              <img src="../../assets/images/auth3.png" alt="">
            </div>
          </div>
        </div>
        <Button type="primary" long class="sub_btn" @click="submitBtnClick">提交</Button>
      </div>
    </div>
    <div class="step_three" v-show="stepThreeShow">
      <div class="tips">
        <img class="waiting " src="@/assets/images/waiting.png" alt="">
        <p>您的身份认证信息已经提交成功，正在审核中 ...</p>
      </div>
    </div>
    <div class="step_four" id="StepFour" v-show="stepFourShow">
      <div class="tips">

          <img class="pass f-fl" src="@/assets/images/pass.png" alt="">
          <p class="f-fl">您的身份认证信息已经通过审核</p>


        <!-- <div class="user-list">
          <table>
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
              </table>
        </div> -->
      </div>
    </div>

    <div class="step_five" id="StepFive" v-show="stepFiveShow">
      <div class="tips">
        <div class="f-cb">
          <img class="denied f-fl" src="@/assets/images/denied.png" alt="">
          <p class="f-fl">抱歉，您的身份认证信息未能通过审核 ！</p>
        </div>
          <p v-if="authData!==null" class="rej_reason">原因:{{authData.reject_reason}}</p>


        <ul class="suggestion">
          <li>请上传清晰、完整证件照，手持证件照和手写日期清晰可见；</li>
          <li>请保证提交认证信息与填写的身份信息一致，且不存在盗用他人信息。</li>
        </ul>

        <Button type="primary" @click="reSubmit" class="reSubmit">重新提交</Button>
      </div>
    </div>
  </div>
</template>
<script>
import {
  getAliOssPolicy, postImg, authIdentity, getAuthindentity,
} from './api';
import { logoutApi } from '@/api/GetCode';

export default {
  data() {
    return {
      authData: null,
      formTop: {
        type: 'id',
        familyName: '',
        givenName: '',
        idNumber: '',
      },
      typeList: [
        {
          value: 'id',
          label: '身份证',
        },
      ],
      frontImgUrl: '',
      backImgUrl: '',
      handholdImgUrl: '',
      stepOneShow: false,
      stepTwoShow: false,
      stepThreeShow: false,
      stepFourShow: false,
      stepFiveShow: false,
    };
  },
  created() {
    getAuthindentity().then((res) => {
      if (res.state === 1) {
        this.authData = res.data.authIdentity;
        if (res.data.authIdentity === null) {
          this.stepOneShow = true;
        } else {
          const userAuthState = res.data.authIdentity.id_status;
          if (userAuthState === 0) {
            this.stepThreeShow = true;
          } else if (userAuthState === 1) {
            this.stepFourShow = true;
            // 用户详细信息
          } else if (userAuthState === 2) {
            this.stepFiveShow = true;
          } else {
            this.stepOneShow = true;
          }
        }
      } else if (res.state === -1) {
        if (res.msg === 'LANG_NO_LOGIN') {
          this.logout();
        }
      }
    });
  },
  methods: {
    frontImgChange(el) {
      getAliOssPolicy().then(({ data }) => {
        const alidata = data;
        let file = el.target.files[0];
        const imagesType = ['image/jpeg', 'image/png'];
        const isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: 'error',
            message: '文件格式必须为jpg、jpeg或png',
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message: '上传文件不能超过5M',
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        const useruid = JSON.parse(localStorage.getItem('useruid'));
        // 封装成 formData
        const date = Date.parse(new Date());
        const formData = new FormData();
        formData.append('OSSAccessKeyId', alidata.accessid);
        formData.append('policy', alidata.policy);
        formData.append('Signature', alidata.signature);
        formData.append('key', alidata.dir + useruid + file.name + date);
        formData.append('success_action_status', '200');
        formData.append('file', file);
        // 上传
        postImg(alidata.host, formData).then((res) => {
          const url = `${data.host}/${data.dir}${useruid}${file.name}${date}`;
          this.frontImgUrl = url;
        });
      });
    },
    backImgChange(el) {
      getAliOssPolicy().then((res) => {
        const data = res.data;
        let file = el.target.files[0];
        const imagesType = ['image/jpeg', 'image/png'];
        const isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileType'),
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileSize'),
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        const useruid = JSON.parse(localStorage.getItem('useruid'));
        // 封装成 formData
        const date = Date.parse(new Date());
        const formData = new FormData();
        formData.append('OSSAccessKeyId', data.accessid);
        formData.append('policy', data.policy);
        formData.append('Signature', data.signature);
        formData.append('key', data.dir + useruid + file.name + date);
        formData.append('success_action_status', '200');
        formData.append('file', file);
        // 上传
        postImg(data.host, formData).then((res) => {
          const url = `${data.host}/${data.dir}${useruid}${file.name}${date}`;
          this.backImgUrl = url;
        });
      });
    },
    handholdImgChange(el) {
      getAliOssPolicy().then((res) => {
        const data = res.data;
        let file = el.target.files[0];
        const imagesType = ['image/jpeg', 'image/png'];
        const isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileType'),
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: 'error',
            message: this.$t('otc.FileSize'),
            duration: 1000,
            showClose: true,
          });
          file = '';
          return;
        }
        const useruid = JSON.parse(localStorage.getItem('useruid'));
        // 封装成 formData
        const date = Date.parse(new Date());
        const formData = new FormData();
        formData.append('OSSAccessKeyId', data.accessid);
        formData.append('policy', data.policy);
        formData.append('Signature', data.signature);
        formData.append('key', data.dir + useruid + file.name + date);
        formData.append('success_action_status', '200');
        formData.append('file', file);
        // 上传
        postImg(data.host, formData).then((res) => {
          const url = `${data.host}/${data.dir}${useruid}${file.name}${date}`;
          this.handholdImgUrl = url;
        });
      });
    },
    stepOneClick() {
      if (this.formTop.familyName.trim() === '' || this.formTop.givenName.trim() === '' || this.formTop.idNumber.trim() === '') {
        this.$Message.error({
          content: '请先完成表单',
          duration: 2,
          closable: true,
        });
        return;
      }
      this.stepOneShow = false;
      this.stepTwoShow = true;
    },
    submitBtnClick() {
      const addr1 = this.frontImgUrl;
      const addr2 = this.backImgUrl;
      const addr3 = this.handholdImgUrl;
      if (!addr1 || !addr2 || !addr3) {
        this.$Message.error({
          content: '请先完成上传照片',
          duration: 2,
          closable: true,
        });
        return;
      }

      const data = {
        nationality: '中国Chinese',
        family_name: this.formTop.familyName.trim(),
        given_name: this.formTop.givenName.trim(),
        id_number: this.formTop.idNumber.trim(),
        idType: 'idcard',
        id_front_img: addr1,
        id_back_img: addr2,
        id_handheld_img: addr3,
      };

      authIdentity(data).then((res) => {
        // 请求成功跳转到等待页面
        if (res.state === 1) {
          this.stepTwoShow = false;
          this.stepThreeShow = true;
        } else if (res.state === -1) {
          // setTimeout(() => {
          //   history.go(0);
          // }, 3000);
        }
      });
    },
    reSubmit() {
      this.stepOneShow = true;
      this.stepFiveShow = false;
    },
    logout() {
      this.$Message.error({
        content: '请先登录',
        duration: 2,
        closable: true,
      });
      const loginState = false;
      localStorage.removeItem('userInfo');
      localStorage.removeItem('useruid');
      localStorage.removeItem('nickName');
      localStorage.removeItem('mNameHidden');
      localStorage.setItem('loginState', loginState);
      this.$store.commit('CLEAR_USERINFO');
      this.$router.push('/login');
      logoutApi();
    },
  },
};
</script>

<style scoped>
.top_container{
  width: 1200px;
  height: 1140px;
  background-color: #ffffff;
  box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  margin-top: 30px;
  margin-bottom: 30px;
}
.title_container{
  width: 1200px;
  height: 60px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  align-items: center;
}
.title_container p{
  margin-left: 30px;
  color: #999999;
  font-size: 16px;
  line-height: 16px;
}
.title_container p span{
  color: #39a9ed;
}
.text_container{
  margin-left: 40px;
  margin-top: 35px;
  font-size: 14px;
  line-height:24px;
  color: #999999;
}
.flex_container{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 58px;
}
.pic_container{
  width: 614px;
  height: 202px;
  margin-bottom: 27px;
}
.pic_container .title{
  font-size: 14px;
  line-height: 14px;
  color: #999999;
}
.pic_container .pic{
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}
.pic_container .pic .left{
  width: 265px;
  height: 176px;
  background-color: #f8f8f8;
  display: flex;
  flex-direction: column;
  justify-content:space-evenly;
  align-items: center;
}
.pic_container .pic .left .img{
  width: 56px;
  height: 56px;
}
.pic_container .pic .left img{
  width: 265px;
  height: 176px;
  display: block;
  background-color: #f8f8f8;
  cursor: pointer;
  transition: .6s;
  border-radius: 3px;
}
.pic_container .pic .left .text{
  width: 210px;
  height: 30px;
  font-size: 12px;
  line-height: 12px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  text-align: center
}

.pic_container .pic .right{
  width: 267px;
  height: 178px;
}
.pic_container .pic .right img{
  width: 267px;
  height: 178px;
}
.file_upload{
  opacity: 0;
  position: relative;
  height: 56px;
  top: -56px;
  z-index: 999;
}
.sub_btn{
  height: 48px;
  width: 612px;
  margin-top: 60px;
}
.one_form_container{
  display: flex;
  justify-content: center;
}
.form_style{
  width: 500px;
  margin-top: 86px;
}
.step_three .tips{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 164px;
}
.step_three .tips p{
  font-size: 24px;
  line-height: 24px;
  color: #333333;
  margin-left: 11px;

}
.step_four .tips{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 164px;

}
.step_four .tips p{
  font-size: 24px;
  line-height: 24px;
  color: #333333;
  margin-left: 11px;
}
.step_five .tips{
  display: flex;
  flex-direction: column;
  align-items: center;
}
.step_five .tips .f-cb{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 164px;
}
.step_five .tips .f-cb p{
  font-size: 24px;
  line-height: 24px;
  color: #333333;
  margin-left: 11px;
}
.step_five .tips .suggestion{
  font-size: 14px;
  color: #333333;
  line-height: 36px;
  margin-top: 40px;
}
.reSubmit{
  width: 260px;
  height: 50px;
  margin-top: 60px
}
.flex-container{
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  margin-top: 38px;
}
.rej_reason{
    margin-top: 40px;
    font-size: 24px;
    line-height: 24px;
    color: #333333;
    margin-left: 11px;
}
</style>
