<template>
  <div>
    <div class="billing_container">
      <my-title titleName='收款方式' subtitle="请务必使用您本人的实名账户，被激活的收款方式将在交易时向买方展示，最多激活3种"/>
      <div class="no_payment" v-show="accountInfo.length===0">您暂时没有添加任何收款方式</div>
      <div class="item-container" v-show="accountInfo.length>0" v-for="(item,index) in accountInfo" :key="item.id">
        <div class="left-container">
          <p class="item-name">{{item.type|payTypeFilter}}</p>
          <p>{{item.account}} {{item.name}} <span v-show="item.type==='BANK'">{{item.bankOrImg}}</span></p>
          <i v-if="item.type!=='BANK'" class="iconfont icon-erweima" @mouseenter="qrcodeMouseEnter(index)"
             @mouseleave="qrcodeMouseLeave(index)"></i>
        </div>
        <img :src="item.bankOrImg" alt="" v-show="qrcodeShow[index]">
        <div class="right-container">
          <p @click="deleteBtnClick(item)">删除</p>
        </div>
      </div>
      <div class="add-container">
        <p @click="addPayClick">点击添加收款方式</p>
      </div>
      <Modal
              v-model="addPayShow"
              width="472px"
              title="添加收款方式">
        <Form :model="formTop" label-position="top">
          <div style="margin-left:8px">
            <FormItem label="收款方式">
              <Select v-model="formTop.payType" style="width:424px">
                <Option v-for="item in payList" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </FormItem>
            <FormItem label="姓名">
              <Input v-model="realName" disabled/>
            </FormItem>

            <div v-show="formTop.payType==='BANK'">
              <FormItem label="开户银行">
                <Input v-model="formTop.bankOrImg"/>
              </FormItem>
              <FormItem label="开户支行(选填)">
                <Input v-model="formTop.subBank"/>
              </FormItem>
              <FormItem label="银行账户">
                <Input v-model="formTop.account"/>
              </FormItem>
            </div>

            <div v-show="formTop.payType==='ALIPAY'">
              <FormItem label="支付宝账号">
                <Input v-model="formTop.account"/>
              </FormItem>
              <FormItem label="二维码">
                <Upload
                        v-show="!formTop.bankOrImg"
                        multiple
                        :before-upload="handleUpload"
                        type="drag"
                        action=''
                >
                  <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>请上传您的支付宝收款二维码图片(JPG/PNG/JPEG)</p>
                  </div>
                </Upload>
                <img :src="formTop.bankOrImg" class="img_container" v-show="formTop.bankOrImg"
                     @click="formTop.bankOrImg=''">
              </FormItem>
            </div>

            <div v-show="formTop.payType==='WXPAY'">
              <FormItem label="微信账号">
                <Input v-model="formTop.account"/>
              </FormItem>
              <FormItem label="二维码">
                <Upload
                        v-show="!formTop.bankOrImg"
                        multiple
                        type="drag"
                        :before-upload="handleUpload"
                        action=''
                >
                  <div style="padding: 20px 0">
                    <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
                    <p>请上传您的微信收款二维码图片(JPG/PNG/JPEG)</p>
                  </div>
                </Upload>
                <img :src="formTop.bankOrImg" class="img_container" v-show="formTop.bankOrImg"
                     @click="formTop.bankOrImg=''">
              </FormItem>
            </div>

            <FormItem label="资金密码">
              <Input v-model="formTop.pwd" type="password"/>
            </FormItem>
            <div class="btn_tool">
              <span @click="closeForm">取消</span>
              <Button type="primary" @click="setPayment">完成设置</Button>
            </div>
          </div>
        </Form>
      </Modal>
      <Modal
              width="472px"
              v-model="deleteFormShow"
              title="请输入资金密码">
        <Input v-model="deleteForm.pwd" placeholder="请输入资金密码" type="password"/>
        <div class="btn_tool" style="margin-top:10px">
          <span @click="closeForm">取消</span>
          <Button type="primary" @click="deleteAccount">确定删除</Button>
        </div>
      </Modal>
    </div>
  </div>
</template>
<script>
import MyTitle from './components/title.vue'
import {
  createPayment, deleteAccount, postImg, getAliOssPolicy,
} from './api'

export default {
  components: {
    MyTitle,
  },
  props: {
    realName: String,
    userInfo: Object,
    accountInfo: Array,
    payList: Array,
  },
  data () {
    return {
      addPayShow: false,
      deleteFormShow: false,
      qrcodeImg: '',
      qrcodeShow: [false, false, false],
      formTop: {
        payType: '',
        name: '',
        bankOrImg: '',
        subBank: '',
        account: '',
      },
      deleteForm: {
        pwd: '',
      },
    }
  },
  filters: {
    payTypeFilter (val) {
      let name = ''
      switch (val) {
        case 'BANK':
          name = '银行卡'
          break
        case 'ALIPAY':
          name = '支付宝'
          break
        case 'WXPAY':
          name = '微信'
          break
        default:
          break
      }
      return name
    },
  },
  methods: {
    addPayClick () {
      if (!this.realName) {
        this.$Message.error({
          content: '请先完成实名认证',
          duration: 2,
          closable: true,
        })
      } else if (this.payList.length === 0) {
        this.$Message.error({
          content: '收款方式每种只能有一个账户',
          duration: 2,
          closable: true,
        })
      } else {
        this.addPayShow = true
      }
    },
    setPayment () {
      if (!this.formTop.payType
          || !this.formTop.account || !this.formTop.bankOrImg || !this.formTop.pwd) {
        this.$Message.error({
          content: '请先完成表单',
          duration: 2,
          closable: true,
        })
        return
      }
      const params = {
        memberId: this.userInfo.uid,
        type: this.formTop.payType,
        name: this.realName,
        account: this.formTop.account,
        bankOrImg: `${this.formTop.bankOrImg} ${this.formTop.subBank}`,
        securityPwd: this.formTop.pwd,
      }
      createPayment(params).then(({data, msg, state}) => {
        if (state === 1) {
          this.$Message.success({
            content: '新增成功',
            duration: 2,
            closable: true,
          })
          this.reloadAccountInfo()
        }
        this.closeForm()
      })
    },
    deleteBtnClick (item) {
      this.deleteItem = item
      this.deleteFormShow = true
    },
    deleteAccount () {
      const params = {
        id: this.deleteItem.id,
        securityPwd: this.deleteForm.pwd,
      }
      deleteAccount(params).then(({data, msg, state}) => {
        if (state === 1) {
          this.reloadAccountInfo()
          this.$Message.success({
            content: '删除成功',
            duration: 2,
            closable: true,
          })
          this.reloadAccountInfo()
        }
        this.closeForm()
      })
    },
    handleUpload (file) {
      getAliOssPolicy().then(({data}) => {
        const alidata = data

        const timestamp = Date.parse(new Date()) / 1000;
        const useruid = localStorage.getItem('useruid');
        const fileName = 'web' + timestamp + useruid + '01';

        //创建新文件对象
        var newfile = new File([file], fileName + '.jpg', {type: 'image/jpeg'})
        // 封装成 formData
        const date = Date.parse(new Date())
        const formData = new FormData()
        formData.append('OSSAccessKeyId', alidata.accessid)
        formData.append('policy', alidata.policy)
        formData.append('Signature', alidata.signature)
        formData.append('key', alidata.dir + newfile.name)
        formData.append('success_action_status', '200')
        formData.append('file', newfile)
        // 上传
        postImg(alidata.host, formData).then((res) => {
          const url = `${data.host}/${data.dir}${newfile.name}`
          this.formTop.bankOrImg = url
        })
      })
      return false
    },
    closeForm () {
      this.addPayShow = false
      this.deleteFormShow = false
      this.deleteForm = {
        pwd: '',
      }
      this.formTop = {
        payType: 'BANK',
        name: '',
        bankOrImg: '',
        subBank: '',
        account: '',
      }
    },
    reloadAccountInfo () {
      this.$emit('reload-account')
    },
    qrcodeMouseEnter (index) {
      const cash = []
      for (let i = 0; i < 3; i++) {
        if (i === index) {
          cash.push(true)
        } else {
          cash.push(false)
        }
      }
      this.qrcodeShow = cash
    },
    qrcodeMouseLeave (index) {
      this.qrcodeShow = [false, false, false]
    },
  },
}
</script>

<style scoped>
  .billing_container {
    width: 1200px;
    background-color: #ffffff;
    box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.06);
    border-radius: 6px;
    margin-top: 30px;
    padding-top: 20px;
    box-sizing: border-box;
    margin-bottom: 50px;
  }

  .add-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px;
    width: 1200px;
  }

  .add-container p {
    font-size: 14px;
    color: #3b78c3;
    cursor: pointer;
  }

  .item-container {
    width: 1135px;
    height: 59px;
    border-bottom: 1px dotted #e5e5e5;
    margin-left: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .left-container {
    display: flex;
    align-items: center
  }

  .left-container i {
    margin-right: 20px;
    margin-left: 20px;
  }

  .item-container img {
    width: 200px;
    height: 200px;
    object-fit: contain
  }

  .item-name {
    margin-right: 173px;
    width: 200px;
  }

  .right-container {
    display: flex;
    align-content: center;
    justify-content: flex-end;
  }

  .right-container p {
    font-size: 14px;
    color: #3b78c3;
    cursor: pointer;
  }

  .no_payment {
    font-size: 14px;
    line-height: 14px;
    color: #999999;
    text-align: center;
    margin-top: 50px;
  }

  .btn_tool {
    display: flex;
    align-items: center;
    justify-content: flex-end
  }

  .btn_tool span {
    margin-right: 35px;
    font-size: 14px;
    line-height: 14px;
    color: #3d414d;
    cursor: pointer;
  }

  .img_container {
    width: 432px;
    height: 118px;
  }
</style>
