<template>
  <div>
    <my-header></my-header>
    <div id="MainView">
      <div class="main_container">
        <div class="title">
          <p>{{$t('user.Certification')}}</p>
        </div>
        <div class="step_container" v-if="step===1||step===2||step===3">
          <div class="step">
            <step-item
              number="1"
              :text="$t('user.BaseInfo')"
              :select="step===1||step===2||step===3"
            />
            <div class="divide"></div>
            <step-item number="2" :text="$t('user.UploadIDPhoto')" :select="step===2||step===3" />
            <div class="divide"></div>
            <step-item number="3" :text="$t('user.HasSubmitted')" :select="step===3" />
          </div>
        </div>
        <div class="step_one" v-if="step===1">
          <div class="form_container">
            <div class="form_item_container">
              <p>{{$t('user.SelectCountry')}}</p>
              <input type="text" v-model="form.country" disabled="true" />
            </div>
            <div class="form_item_container">
              <p>{{$t('user.DocumentType')}}</p>
              <el-select v-model="form.type" v-if="lang==='zh'">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select v-model="form.type" v-else-if="lang==='en'">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label_en"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select v-model="form.type" v-else-if="lang==='ko'">
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label_ko"
                  :value="item.value"
                ></el-option>
              </el-select>
              <el-select v-model="form.type" v-else>
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label_ja"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
            <div class="form_item_container">
              <p>{{$t('user.FamilyName')}}</p>
              <input type="text" v-model="form.firstName" />
            </div>
            <div class="form_item_container">
              <p>{{$t('user.Name')}}</p>
              <input type="text" v-model="form.secondName" />
            </div>
            <div class="form_item_container">
              <p>{{$t('user.IDNumber')}}</p>
              <input type="text" v-model="form.id" />
            </div>
          </div>

          <div class="tip_container">
            <p>{{$t('user.TipOne')}}</p>
            <p>{{$t('user.TipTwo')}}</p>
          </div>

          <div class="next_button" :class="firstCanNext?'enable':''" @click="firstBtnClick">
            <p>{{$t('user.Next')}}</p>
          </div>
        </div>
        <div class="step_two" v-if="step===2">
          <div class="upload_container">
            <div class="row_one">
              <div class="text_container"></div>
              <div class="img_container">
                <img :src="o_frontImgUrl" alt />
                <input type="file" class="file-upload" ref="idFrontImg" @change="frontImgChange" />
              </div>
              <img class="img_container" src="../assets/images/upload1.1.png" alt />
              <div class="text_container">
                <p>· {{$t('user.UploadTipOne')}}</p>
              </div>
            </div>
            <div class="row_one">
              <div class="text_container"></div>
              <div class="img_container">
                <img :src="o_backImgUrl" alt />
                <input type="file" class="file-upload" ref="idBackImg" @change="backImgChange" />
              </div>
              <img class="img_container" src="../assets/images/upload2.1.png" alt />
              <div class="text_container">
                <p>· {{$t('user.UploadTipTwo')}}</p>
              </div>
            </div>
            <div class="row_one">
              <div class="text_container"></div>
              <div class="img_container">
                <img :src="o_handholdImgUrl" alt />
                <input
                  type="file"
                  class="file-upload"
                  ref="idHandheldImg"
                  @change="handholdImgChange"
                />
              </div>
              <img class="img_container" src="../assets/images/upload3.1.png" alt />
              <div class="text_container">
                <p>· {{$t('user.UploadTipThree')}}</p>
              </div>
            </div>
          </div>

          <div class="next_button" :class="secondCanNext?'enable':''" @click="secondBtnClick">
            <p>{{$t('user.Submit')}}</p>
          </div>
        </div>
        <div class="step_three" v-if="step===3">
          <div class="context_container">
            <img src="../assets/images/waiting.png" alt />
            <p>{{$t('user.YourAuthentication')}}</p>
          </div>
        </div>
        <div class="success_container" v-if="step===4">
          <div class="tip">
            <img src="../assets/images/pass.png" />
            <p>{{$t('user.YourIdentity')}}</p>
          </div>
          <div class="item">
            <div class="first">{{$t('user.DocumentType')}}</div>
            <div class="second">{{idInfo.authIdentity.id_type|idTypeFilter}}</div>
          </div>
          <div class="item">
            <div class="first">{{$t('user.IDNumber')}}</div>
            <div
              class="second"
            >{{idInfo.authIdentity.id_number==null?"":idInfo.authIdentity.id_number.slice(0,2)}}*********{{idInfo.authIdentity.id_number==null?'':idInfo.authIdentity.id_number.slice(idInfo.authIdentity.id_number.length-2,idInfo.authIdentity.id_number.length)}}</div>
          </div>
          <div class="item">
            <div class="first">{{$t('user.FullName')}}</div>
            <div class="second">*{{idInfo.authIdentity.given_name}}</div>
          </div>
        </div>

        <div class="fail_container" v-if="step===5">
          <div class="tips">
            <img src="../assets/images/denied.png" alt />
            <p>{{$t('user.SorryYourIdentity')}}</p>
          </div>

          <div class="context">
            <p>{{$t('user.PleaseUpload')}}</p>
            <p>{{$t('user.PleaseEnsure')}}</p>
            <p style="color:red">{{$t('user.FailReason')}}：{{idInfo.authIdentity.reject_reason}}</p>
          </div>

          <div class="resubmit_button" @click="reSubmit">{{$t('user.Resubmit')}}</div>
        </div>

        <div class="identity">
          <el-dialog
            :title="$t('user.Certification')"
            :visible.sync="dialogFormVisible"
            :show-close="false"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
          >
            <p class="dialog_title">{{$t('user.SelectCountry')}}</p>

            <el-select v-model="form.country" filterable>
              <el-option
                v-for="item in options"
                :key="item.cname"
                :label="lang==='zh'?item.cname:item.name"
                :value="item.cname"
              ></el-option>
            </el-select>

            <div slot="footer" class="dialog-footer">
              <el-button @click="closeModal">{{$t('user.Cancel')}}</el-button>
              <el-button type="primary" @click="countryConfrim">{{$t('user.Confirm')}}</el-button>
            </div>
          </el-dialog>
        </div>

        <div class="china_container" v-if="step===6">
          <div class="contain-cmp">
            <div class="id-pic" v-if="c_first">
              <div class="upload_container">
                <ul class="tips">
                  <li>· 以下材料需用 JPG / JPEG / PNG 格式上传，并保证照片清晰、边角完整、亮度均匀，且大小不得超过5M；</li>
                  <li>· 提交本申请代表承诺你的身份信息真实可靠，不存在盗用他人信息资料的情况；</li>
                  <li>· 您的提交申请我们将在一个工作日内进行审核处理，并告知审核处理结果。</li>
                </ul>
                <div class="first">
                  <div class="cards" v-if="c_first_step == 1">
                    <div class="desc">本人身份证正面照片</div>
                    <div class="row_one">
                      <div class="img_container">
                        <img :src="frontImgUrl" alt />
                        <input
                          type="file"
                          class="file-upload"
                          ref="CidFrontImg"
                          @change="CfrontImgChange"
                        />
                      </div>
                      <img class="img_container" src="../assets/images/upload2.1.png" alt />
                    </div>
                    <div class="desc">本人身份证背面照片</div>
                    <div class="row_one">
                      <div class="img_container">
                        <img :src="backImgUrl" alt />
                        <input
                          type="file"
                          class="file-upload"
                          ref="CidBackImg"
                          @change="CbackImgChange"
                        />
                      </div>
                      <img class="img_container" src="../assets/images/upload1.1.png" alt />
                    </div>
                    <button class="btn" @click="getCardInfo">下一步</button>
                  </div>
                  <div class="hand-card" v-else>
                    <div class="desc">手持身份证</div>
                    <div class="row_one">
                      <div class="img_container">
                        <img :src="handholdImgUrl" alt />
                        <input
                          type="file"
                          class="file-upload"
                          ref="ChangBackImg"
                          @change="HbackImgChange"
                        />
                      </div>
                      <img class="img_container" src="../assets/images/upload3.1.png" alt />
                    </div>
                    <button class="btn" @click="getCardHand">下一步</button>
                  </div>
                </div>
              </div>
            </div>
            <div class="sec" v-if="c_sec">
              <div class="confirm">
                <div class="success_container">
                  <div class="tip">
                    <img src="../assets/images/pass.png" />
                    <p>{{ c_msg }}</p>
                  </div>
                  <p class="id-type">证件类型&nbsp;&nbsp;&nbsp;&nbsp;身份证</p>
                  <div class="c-item">
                    <span class="left">姓名</span>
                    <span class="diviler">:</span>
                    <span class="right">{{ c_info.name }}</span>
                  </div>
                  <div class="c-item">
                    <span class="left">性别</span>
                    <span class="diviler">:</span>
                    <span class="right">{{ c_info.sex == 'F'?'女':'男' }}</span>
                  </div>
                  <div class="c-item">
                    <span class="left">出生</span>
                    <span class="diviler">:</span>
                    <span class="right">{{ c_info.birthday }}</span>
                  </div>
                  <div class="c-item">
                    <span class="left">地址</span>
                    <span class="diviler">:</span>
                    <span class="right">{{ c_info.province }}</span>
                  </div>
                  <div class="c-item">
                    <span class="left">证件号</span>
                    <span class="diviler">:</span>
                    <span class="right">{{ c_info.id_number }}</span>
                  </div>
                  <!-- <div class="item">
                    <div class="first"></div>
                    <div class="second">{{c_info.name}}</div>
                  </div>-->
                  <!-- <div class="item">
                    <div class="first">{{c_info.sex}}</div>
                    <div
                      class="second"
                    >{{idInfo.authIdentity.id_number==null?"":idInfo.authIdentity.id_number.slice(0,2)}}*********{{idInfo.authIdentity.id_number==null?'':idInfo.authIdentity.id_number.slice(idInfo.authIdentity.id_number.length-2,idInfo.authIdentity.id_number.length)}}</div>
                  </div>-->
                  <!-- <div class="item">
                    <div class="first">姓名：</div>
                    <div class="second">张包</div>
                  </div>
                                   <div class="item">
                    <div class="first">姓名：</div>
                    <div class="second">张包</div>
                  </div>-->
                </div>
              </div>
              <div class="next-box" v-if="c_sec_step == 2">
                <button class="reload" @click="reUpload">重新上传</button>
                <button class="next" @click="toVerifyHand">下一步</button>
              </div>
            </div>
            <!-- <upload-idcard /> -->
            <!-- <verify-info /> -->
          </div>
          <!-- <div class="china_main_container">
            <div class="qrcodeContainer" v-if="faceIdInfo!=null">
              <qrcode :value="faceIdInfo.url+faceIdInfo.biz_token" :options="{size:158}"></qrcode>
            </div>
            <div class="text_container">
              <p class="info1">请使用手机浏览器扫描二维码，打开实名认证界面。</p>
              <p class="info2">拍摄身份证正反面，并通过人脸识别认证</p>
              <p
                class="info3"
                v-if="limitTime>0"
              >二维码有效期：{{parseInt(limitTime/60)}}分{{limitTime%60}}秒</p>
              <p class="info4" v-else>二维码已过期，请刷新页面重新获取</p>
            </div>
          </div>
          <div class="has_auth_btn" @click="checkChinaHasAuth">
            <p>已在手机上完成验证</p>
          </div>-->
        </div>
      </div>
    </div>
    <my-footer></my-footer>
  </div>
</template>
<script>
import { Loading } from "element-ui";
import StepItem from "@/components/StepItem";
import MyHeader from "@/components/Header";
import MyFooter from "@/components/Footer";
import CountryCN from "../assets/json/country_cn";
import { getLang } from "@/libs/utils.js";
import {
  getAuthindentity,
  getAliOssPolicy,
  authIdentity,
  postImg,
  getFaceIdUrl,
  identityAuto,
  identityHand,
  checkAuto
} from "@/api/identityVerify.js";
import uploadIdcard from "@/components/identityVerify/UploadIDcard.vue";
import verifyInfo from "../components/identityVerify/VerifyInfo";
export default {
  components: {
    MyHeader,
    MyFooter,
    StepItem,
    uploadIdcard,
    verifyInfo,
    checkAuto,
    identityHand
  },

  created() {
    this.is_alreay_check();
  },
  mounted() {
    let lang = getLang();
    this.lang = lang;
    this.options = CountryCN.RECORDS;
    this.getAuthState();
  },
  filters: {
    idTypeFilter(id_type) {
      var idTypeName = "";
      switch (id_type) {
        case "PASSPORT":
          idTypeName = "护照";
          break;
        case "DRIVING_LICENSE":
          idTypeName = "驾照";
          break;
        case "OTHER(":
          idTypeName = "其他合法证件";
          break;
        default:
          idTypeName = "身份证";
          break;
      }
      return idTypeName;
    }
  },
  computed: {
    firstCanNext: function() {
      return (
        this.form.firstName &&
        this.form.secondName &&
        this.form.id &&
        this.form.type
      );
    },
    secondCanNext: function() {
      return (
        this.imagesHasUpload1 && this.imagesHasUpload2 && this.imagesHasUpload3
      );
    }
  },
  data() {
    return {
      c_first: true,
      c_first_step: 1,
      c_sec: false,
      c_sec_step: 2,
      c_info: "",
      c_error: false,
      c_msg: "请核对您的身份认证信息",
      //.............................
      lang: "zh",
      step: 0, // 1-->表单，2-->上传图片，3-->等待认证,4-->认证成功，5-->认证失败,6-->中国认证
      idInfo: "", // 身份验证信息
      faceIdInfo: null,
      // 剩余时间
      limitTime: 0,
      // setinterval句柄
      timeOut: null,
      typeOptions: [
        {
          value: "PASSPORT",
          label: "护照",
          label_en: "passport",
          label_ja: "パスポート",
          label_ko: "여권"
        },
        {
          value: "DRIVING_LICENSE",
          label: "驾驶证",
          label_en: "driving license",
          label_ja: "運転免許証",
          label_ko: "운전면허증"
        },
        {
          value: "OTHER",
          label: "其他合法证件",
          label_en: "other",
          label_ja: "その他の合法証明書",
          label_ko: "기타 합법적 증명서"
        }
      ],
      options: [],
      dialogFormVisible: false,
      form: {
        country: "中国",
        type: "",
        firstName: "",
        secondName: "",
        id: ""
      },
      imagesHasUpload1: false,
      imagesHasUpload2: false,
      imagesHasUpload3: false,
      frontImgUrl: require("../assets/images/c_front.png"),
      backImgUrl: require("../assets/images/c_back.png"),
      handholdImgUrl: require("../assets/images/c_person.png"),
      o_frontImgUrl:
        require("../assets/images/add.png"),
      o_backImgUrl:
        require("../assets/images/add.png"),
      o_handholdImgUrl:
        require("../assets/images/add.png"),
    };
  },
  beforeDestroy() {
    clearInterval(this.timeOut);
  },
  methods: {
    getAuthState() {
      getAuthindentity().then(res => {
        if (res.state === 1) {
          if (res.data.authIdentity === null) {
            this.step = 1;
            this.dialogFormVisible = true;
          } else {
            let userAuthState = res.data.authIdentity.id_status;

            switch (userAuthState) {
              case 0:
                this.step = 3;
                break;
              case 1:
                this.step = 4;
                this.idInfo = res.data;
                break;
              case 2:
                this.step = 5;
                this.idInfo = res.data;
                break;
              case 3:
                this.step = 1;
                this.dialogFormVisible = true;
                break;

              default:
                this.step = 1;
                this.dialogFormVisible = true;
                break;
            }
          }
        } else if (res.state === -1) {
          if (res.msg === "LANG_NO_LOGIN") {
            this.$message({
              type: "error",
              message: this.$t("header.PlaseLogin"),
              duration: 3000,
              showClose: true
            });
            this.userLogout();
          }
        }
      });
    },
    is_alreay_check() {
      checkAuto().then(res => {
        if (res.state == 1 && res.data != null) {
          this.c_info = res.data;
          this.c_first = false;
          this.c_sec = true;
        }
      });
    },
    reUpload() {
      this.c_first = true;
      this.c_sec = false;
      this.frontImgUrl = require("../assets/images/c_front.png");
      this.backImgUrl = require("../assets/images/c_back.png");
    },
    toVerifyHand() {
      this.c_first = true;
      this.c_first_step = 0;
      this.c_sec = false;
    },
    getFileURL(file) {
      let getUrl = null;
      if (window.createObjectURL !== undefined) {
        // basic
        getUrl = window.createObjectURL(file);
      } else if (window.URL !== undefined) {
        // mozilla(firefox)
        getUrl = window.URL.createObjectURL(file);
      } else if (window.webkitURL !== undefined) {
        // webkit or chrome
        getUrl = window.webkitURL.createObjectURL(file);
      }
      return getUrl;
    },
    // validateFile(el, imgSrc) {
    //   console.loginState(el, imgSrc);
    //   let file = el.target.files[0];
    //   let getUrl = this.getFileURL(file);
    //   imgSrc = getUrl;
    //   let imagesType = ["image/jpeg", "image/png"];
    //   let isexists = imagesType.includes(file.type);
    //   if (!isexists) {
    //     this.$message({
    //       type: "error",
    //       message: this.$t("otc.FileType"),
    //       duration: 3000,
    //       showClose: true
    //     });
    //     file = "";
    //     return;
    //   }
    //   if (file.size > 5 * 1024 * 1024) {
    //     this.$message({
    //       type: "error",
    //       message: this.$t("otc.FileSize"),
    //       duration: 3000,
    //       showClose: true
    //     });
    //     file = "";
    //     return;
    //   }
    // },
    getCardHand() {
      let formData = new FormData();
      formData.append("mugShot", this.$refs.ChangBackImg.files[0]);
      if (!this.$refs.ChangBackImg.files[0]) {
        this.$message({
          type: "error",
          message: "请上传个人图像",
          duration: 3000,
          showClose: true
        });
        return;
      }
      this._loading();
      identityHand(formData).then(res => {
        this._loading().close();
        if (res.state == 1) {
          this.c_sec_step = 0;
          this.c_sec = false;
          this.c_msg = "您的身份认证信息已经通过审核";
          this.getAuthState();
        } else {
          this.c_sec = false;
        }
      });
    },
    HbackImgChange(el) {
      let file = el.target.files[0];
      let getUrl = this.getFileURL(file);
      this.handholdImgUrl = getUrl;
      let imagesType = ["image/jpeg", "image/png"];
      let isexists = imagesType.includes(file.type);
      if (!isexists) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileType"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileSize"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
    },
    CfrontImgChange(el) {
      let file = el.target.files[0];
      let getUrl = this.getFileURL(file);
      this.frontImgUrl = getUrl;
      let imagesType = ["image/jpeg", "image/png"];
      let isexists = imagesType.includes(file.type);
      if (!isexists) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileType"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileSize"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
    },
    CbackImgChange(el) {
      let file = el.target.files[0];
      let getUrl = this.getFileURL(file);
      this.backImgUrl = getUrl;
      let imagesType = ["image/jpeg", "image/png"];
      let isexists = imagesType.includes(file.type);
      if (!isexists) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileType"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        this.$message({
          type: "error",
          message: this.$t("otc.FileSize"),
          duration: 3000,
          showClose: true
        });
        file = "";
        return;
      }
    },
    _loading() {
      return Loading.service({
        lock: true,
        text: "正在识别身份信息...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },
    getCardInfo() {
      let formData = new FormData();
      if (!this.$refs.CidFrontImg.files[0]) {
        this.$message({
          type: "error",
          message: "请上传身份证正面照",
          duration: 3000,
          showClose: true
        });
        return;
      }
      if (!this.$refs.CidBackImg.files[0]) {
        this.$message({
          type: "error",
          message: "请上传身份证背面照",
          duration: 3000,
          showClose: true
        });
        return;
      }
      formData.append("iDcardFront", this.$refs.CidFrontImg.files[0]);
      formData.append("iDcardBack", this.$refs.CidBackImg.files[0]);
      this._loading();
      identityAuto(formData).then(res => {
        this._loading().close();
        if (res.state == 1) {
          this.c_info = res.data;
          this.c_first = false;
          this.c_sec = true;
        }
      });
    },
    userLogout() {
      this.loginState = false;
      sessionStorage.setItem("loginState", this.loginState);
      this.$router.push("/login");
    },
    closeModal() {
      this.dialogFormVisible = false;
      this.$router.go(-1);
    },
    countryConfrim() {
      if (this.form.country === "中国") {
        this.step = 6;
        //this.getFaceIdUrl();
      }
      this.dialogFormVisible = false;
    },
    async getFaceIdUrl() {
      var res = await getFaceIdUrl();
      this.faceIdInfo = res.data;
      // 计算剩余时间
      let createTime = this.faceIdInfo.biz_token.split(",")[0];
      let expirationTime = Number(createTime) + this.faceIdInfo.expire_time;
      let nowTime = Date.parse(new Date()) / 1000;
      let result = expirationTime - nowTime;
      this.limitTime = result;
      // 开启倒计时
      this.timeOut = setInterval(() => {
        this.limitTime--;
        if (this.limitTime === 0) {
          clearInterval(this.timeOut);
        }
      }, 1000);
    },
    firstBtnClick() {
      if (this.firstCanNext) {
        this.step = 2;
      } else {
        this.$message({
          type: "error",
          message: this.$t("user.PleaseCompleteTheFormInformation"),
          duration: 3000,
          showClose: true
        });
      }
    },
    frontImgChange(el) {
      debugger
      getAliOssPolicy().then(res => {
        debugger
        let data = res.data;
        let file = el.target.files[0];
        let imagesType = ["image/jpeg", "image/png"];
        let isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileType"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileSize"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = localStorage.getItem("useruid")
        const fileName='web'+timestamp+useruid+'01'

        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});

        // 封装成 formData
        let date = Date.parse(new Date());
        let formData = new FormData();
        formData.append("OSSAccessKeyId", data.accessid);
        formData.append("policy", data.policy);
        formData.append("Signature", data.signature);
        formData.append("key", data.dir  + newfile.name );
        formData.append("success_action_status", "200");
        formData.append("file", newfile);
        // 上传
        postImg(data.host, formData).then(res => {
          debugger
          let url = data.host + "/" + data.dir  + newfile.name ;
          this.o_frontImgUrl = url;
          this.imagesHasUpload1 = true;
        });
      });
    },
    backImgChange(el) {
      getAliOssPolicy().then(res => {
        let data = res.data;
        let file = el.target.files[0];
        console.log(file)
        let imagesType = ["image/jpeg", "image/png"];
        let isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileType"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileSize"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = localStorage.getItem("useruid")
       const fileName='web'+timestamp+useruid+'02'

        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});

        // 封装成 formData
        let date = Date.parse(new Date());
        let formData = new FormData();
        formData.append("OSSAccessKeyId", data.accessid);
        formData.append("policy", data.policy);
        formData.append("Signature", data.signature);
        formData.append("key", data.dir  + newfile.name );
        formData.append("success_action_status", "200");
        formData.append("file", newfile);
        // 上传
        postImg(data.host, formData).then(res => {
          let url = data.host + "/" + data.dir  + newfile.name ;
          this.o_backImgUrl = url;
          this.imagesHasUpload2 = true;
        });
      });
    },
    handholdImgChange(el) {
      getAliOssPolicy().then(res => {
        let data = res.data;
        let file = el.target.files[0];
        let imagesType = ["image/jpeg", "image/png"];
        let isexists = imagesType.includes(file.type);
        if (!isexists) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileType"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        if (file.size > 5 * 1024 * 1024) {
          this.$message({
            type: "error",
            message: this.$t("otc.FileSize"),
            duration: 3000,
            showClose: true
          });
          file = "";
          return;
        }
        const timestamp = Date.parse(new Date())/1000;
        let useruid = localStorage.getItem("useruid")
        const fileName='web'+timestamp+useruid+'03'

        //创建新文件对象
        var newfile = new File([file], fileName+".jpg",{type:"image/jpeg"});
        // 封装成 formData
        let date = Date.parse(new Date());
        let formData = new FormData();
        formData.append("OSSAccessKeyId", data.accessid);
        formData.append("policy", data.policy);
        formData.append("Signature", data.signature);
        formData.append("key", data.dir + newfile.name );
        formData.append("success_action_status", "200");
        formData.append("file", newfile);
        // 上传
        postImg(data.host, formData).then(res => {
          let url = data.host + "/" + data.dir + newfile.name ;
          this.o_handholdImgUrl = url;
          this.imagesHasUpload3 = true;
        });
      });
    },
    secondBtnClick() {
      if (!this.secondCanNext) {
        return;
      }
      var addr1 = this.o_frontImgUrl;
      var addr2 = this.o_backImgUrl;
      var addr3 = this.o_handholdImgUrl;
      var data = {
        nationality: this.form.country,
        family_name: this.form.firstName,
        given_name: this.form.secondName,
        id_number: this.form.id,
        id_type: this.form.type,
        id_front_img: addr1,
        id_back_img: addr2,
        id_handheld_img: addr3
      };

      authIdentity(data).then(res => {
        // 请求成功跳转到等待页面
        if (res.state === 1) {
          this.step = 3;
        } else if (res.state === -1) {
          this.$$router.go(-1);
        }
      });
    },
    reSubmit() {
      this.dialogFormVisible = true;
      this.step = 1;
    },
    checkChinaHasAuth() {
      getAuthindentity().then(res => {
        if (res.state === 1) {
          if (res.data.authIdentity === null) {
            this.$notify({
              message: "请扫描二维码完成认证",
              type: "warning"
            });
          } else {
            let userAuthState = res.data.authIdentity.id_status;

            switch (userAuthState) {
              case 0:
                this.step = 3;
                break;
              case 1:
                this.step = 4;
                this.idInfo = res.data;
                break;
              case 2:
                this.step = 5;
                break;
              case 3:
                this.$notify({
                  message: "请扫描二维码完成认证",
                  type: "warning"
                });
                break;
              default:
                this.$notify({
                  message: "请扫描二维码完成认证",
                  type: "warning"
                });
                break;
            }
          }
        } else if (res.state === -1) {
          if (res.msg === "LANG_NO_LOGIN") {
            this.$message({
              type: "error",
              message: this.$t("header.PlaseLogin"),
              duration: 3000,
              showClose: true
            });
            this.userLogout();
          }
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
#MainView {
  display: flex;
  justify-content: center;
  .main_container {
    width: 1200px;
    height: 1012px;
    background-color: #fff;
    margin: 20px 0 100px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    .title {
      height: 50px;
      width: 1200px;
      box-shadow: 1px 2px 6px 0px rgba(0, 0, 0, 0.1);
      display: flex;
      align-items: center;
      padding-left: 30px;
      box-sizing: border-box;
      p {
        width: 64px;
        height: 16px;
        font-family: PingFang-SC-Regular;
        font-size: 16px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #25425a;
      }
    }
    .china_container {
      height: 800px;
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      .contain-cmp {
        margin-top: 60px;
        .id-pic {
          .upload_container {
            width: 902px;
            .tips {
              transform: translateX(-115px);
              font-size: 14px;
              color: #999999;
              li {
                margin-bottom: 10px;
              }
            }
            .cards {
              margin-top: 100px;
              margin-left: 150px;
              // border: 1px solid black;
              .desc {
                margin-bottom: 10px;
                font-size: 14px;
                color: #999;
              }
              .row_one {
                display: flex;
                // flex-direction: row;
                // justify-content: space-between;
                margin-bottom: 20px;
                margin-bottom: 50px;
              }
              .img_container {
                margin-right: 60px;
                width: 200px;
                height: 126px;
                position: relative;
                text-align: center;
                img {
                  width: 110px;
                  height: 90px;
                }

                .file-upload {
                  position: absolute;
                  right: 0;
                  top: 0;
                  opacity: 0;
                  width: 200px;
                  height: 126px;
                  cursor: pointer;
                }
              }
              .btn {
                margin-top: 50px;
                width: 440px;
                height: 48px;
                background-color: #718ca3;
                color: #fff;
                margin-left: 12px;
              }
            }
            .hand-card {
              margin-top: 100px;
              margin-left: 150px;
              // border: 1px solid black;
              .desc {
                margin-bottom: 10px;
                font-size: 14px;
                color: #999;
              }
              .row_one {
                display: flex;
                // flex-direction: row;
                // justify-content: space-between;
                margin-bottom: 20px;
                margin-bottom: 50px;
              }
              .img_container {
                margin-right: 60px;
                width: 200px;
                height: 126px;
                position: relative;
                img {
                  width: 200px;
                  height: 126px;
                }

                .file-upload {
                  position: absolute;
                  right: 0;
                  top: 0;
                  opacity: 0;
                  width: 200px;
                  height: 126px;
                  cursor: pointer;
                }
              }
              .btn {
                margin-top: 350px;
                width: 440px;
                height: 48px;
                background-color: #718ca3;
                color: #fff;
                margin-left: 12px;
              }
            }
          }
        }
        .sec {
          transform: translate(200px, -80px);

          .id-type {
            color: #25425a;
            font-size: 14px;
            padding-left: 80px;
            margin-top: 50px;
            margin-bottom: 45px;
          }
          .c-item {
            font-size: 14px;
            display: flex;
            color: #6c859a;
            // width: 90px;
            // justify-content: space-between;
            margin-bottom: 30px;
            .diviler {
              margin: 0 20px;
            }
          }
          .next-box {
            .reload {
              width: 328px;
              height: 48px;
              background-color: #718ca3;
              color: #fff;
              border-radius: 5px;
              transform: translateX(-340px);
            }
            .next {
              transform: translateX(-40px);
              margin-top: 180px;
              width: 328px;
              height: 48px;
              border-radius: 5px;
              background-color: #718ca3;
              color: #fff;
            }
          }
        }
        .fail_container {
          display: flex;
          flex-direction: column;
          margin-top: 168px;
          align-items: center;
          .tips {
            display: flex;
            flex-direction: row;
            align-items: center;
            img {
              width: 22px;
              margin-right: 10px;
            }
            p {
              font-size: 18px;
              font-weight: normal;
              font-stretch: normal;
              letter-spacing: 0px;
              color: #25425a;
            }
          }
          .context {
            margin-top: 58px;
            margin-left: 58px;
            p {
              line-height: 24px;
              font-size: 12px;
              font-weight: normal;
              letter-spacing: 0px;
              color: #25425a;
            }
          }
          .resubmit_button {
            margin-top: 100px;
            width: 260px;
            height: 42px;
            background-color: #196bdf;
            border-radius: 2px;
            text-align: center;
            line-height: 42px;
            font-size: 16px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #ffffff;
            cursor: pointer;
          }
        }
        .confirm {
          .success_container {
            margin-top: 166px;
            .tip {
              display: flex;
              flex-direction: row;
              align-items: center;
              img {
                width: 26px;
                margin-right: 10px;
              }
              p {
                font-size: 18px;
                font-weight: normal;
                font-stretch: normal;
                letter-spacing: 0px;
                color: #25425a;
              }
            }
            .item {
              margin-top: 35px;
              display: flex;
              flex-direction: row;
              .first {
                width: 148px;
                padding-left: 42px;
                box-sizing: border-box;
                color: #6c859a;
              }
              .second {
                width: 140px;
                font-weight: normal;
                font-stretch: normal;
                letter-spacing: 0px;
                color: #25425a;
              }
            }
          }
        }
      }
      .china_main_container {
        margin-top: 200px;
        display: flex;
        flex-direction: row;
        justify-content: center;
        .qrcodeContainer {
          margin-right: 22px;
        }
        .text_container {
          display: flex;
          flex-direction: column;
          .info1 {
            margin-top: 5px;
            margin-bottom: 20px;
            font-size: 20px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #25425a;
          }
          .info2 {
            font-size: 16px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #6c859a;
            margin-bottom: 50px;
          }
          .info3 {
            font-size: 16px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #25425a;
          }
          .info4 {
            font-size: 16px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: red;
          }
        }
      }
      .has_auth_btn {
        cursor: pointer;
        margin-top: 323px;
        width: 460px;
        height: 42px;
        background-color: #196bdf;
        border-radius: 1px;
        p {
          line-height: 42px;
          text-align: center;
          font-size: 16px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
        }
      }
    }
    .step_container {
      height: 144px;
      width: 1200px;
      display: flex;
      align-items: center;
      justify-content: center;
      .step {
        width: 845px;
        height: 24px;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        .divide {
          width: 249px;
          height: 1px;
          border-bottom: dashed 1px #768d9a;
        }
      }
    }
    .step_one {
      .form_container {
        display: flex;
        flex-direction: column;
        align-items: center;
        .form_item_container {
          margin-top: 20px;
          display: flex;
          flex-direction: column;
          height: 65px;
          width: 462px;
          justify-content: space-between;
          p {
            font-size: 12px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #6c859a;
          }
          select {
            width: 460px;
            height: 42px;
            border: solid 1px #d1d9df;
            border-radius: 2px;
            padding-left: 20px;
            color: #25425a;
            font-size: 14px;
          }
          input {
            width: 460;
            height: 42px;
            border: solid 1px #d1d9df;
            border-radius: 2px;
            padding-left: 20px;
            color: #25425a;
            font-size: 14px;
            ::-webkit-input-placeholder {
              /* WebKit browsers */
              font-size: 14px;
              color: #25425a;
            }
            ::-moz-placeholder {
              /* Mozilla Firefox 19+ */
              font-size: 14px;
              color: #25425a;
            }
            ::-ms-input-placeholder {
              /* Internet Explorer 10+ */
              font-size: 14px;
              color: #25425a;
            }
          }
        }
      }
      .tip_container {
        margin-top: 40px;
        width: 462px;
        p {
          font-size: 12px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #6c859a;
          line-height: 24px;
        }
      }
      .next_button {
        margin-top: 40px;
        width: 462px;
        height: 42px;
        background-color: #718ca3;
        border-radius: 2px;
        cursor: pointer;
        p {
          line-height: 42px;
          text-align: center;
          font-size: 16px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
          user-select: none;
          /*火狐*/
          -moz-user-select: none;
          /*谷歌、Safari*/
          -webkit-user-select: none;
          /*IE10*/
          -ms-user-select: none;
          /*欧朋*/
          -o-user-select: none;
          /*早期浏览器*/
          -khtml-user-select: none;
        }
      }
    }
    .step_two {
      width: 902px;
      height: 660px;
      display: flex;
      align-items: center;
      flex-direction: column;
      .upload_container {
        width: 902px;

        .row_one {
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          margin-bottom: 20px;
        }
        .img_container {
          width: 200px;
          height: 126px;
          position: relative;
          text-align: center;
          img {
            width: 110px;
            height: 90px;
          }

          .file-upload {
            position: absolute;
            right: 0;
            top: 0;
            opacity: 0;
            width: 200px;
            height: 126px;
            cursor: pointer;
          }
        }
        .text_container {
          margin-top: 12px;
          width: 220px;
          p {
            font-size: 12px;
            font-weight: normal;
            letter-spacing: 0px;
            color: #6c859a;
            line-height: 24px;
          }
        }
      }
      .next_button {
        margin-top: 100px;
        width: 462px;
        height: 42px;
        background-color: #718ca3;
        border-radius: 2px;
        cursor: pointer;
        p {
          line-height: 42px;
          text-align: center;
          font-size: 16px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #ffffff;
          user-select: none;
          /*火狐*/
          -moz-user-select: none;
          /*谷歌、Safari*/
          -webkit-user-select: none;
          /*IE10*/
          -ms-user-select: none;
          /*欧朋*/
          -o-user-select: none;
          /*早期浏览器*/
          -khtml-user-select: none;
        }
      }
    }
    .step_three {
      .context_container {
        display: flex;
        align-items: center;
        img {
          height: 18px;
          margin-right: 10px;
        }
        p {
          font-size: 18px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #25425a;
        }
      }
    }
    .success_container {
      margin-top: 166px;
      .tip {
        display: flex;
        flex-direction: row;
        align-items: center;
        img {
          width: 26px;
          margin-right: 10px;
        }
        p {
          font-size: 18px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #25425a;
        }
      }
      .item {
        margin-top: 35px;
        display: flex;
        flex-direction: row;
        .first {
          width: 148px;
          padding-left: 42px;
          box-sizing: border-box;
          color: #6c859a;
        }
        .second {
          width: 140px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #25425a;
        }
      }
    }
    .fail_container {
      display: flex;
      flex-direction: column;
      margin-top: 168px;
      align-items: center;
      .tips {
        display: flex;
        flex-direction: row;
        align-items: center;
        img {
          width: 22px;
          margin-right: 10px;
        }
        p {
          font-size: 18px;
          font-weight: normal;
          font-stretch: normal;
          letter-spacing: 0px;
          color: #25425a;
        }
      }
      .context {
        margin-top: 58px;
        margin-left: 58px;
        p {
          line-height: 24px;
          font-size: 12px;
          font-weight: normal;
          letter-spacing: 0px;
          color: #25425a;
        }
      }
      .resubmit_button {
        margin-top: 100px;
        width: 260px;
        height: 42px;
        background-color: #196bdf;
        border-radius: 2px;
        text-align: center;
        line-height: 42px;
        font-size: 16px;
        font-weight: normal;
        font-stretch: normal;
        letter-spacing: 0px;
        color: #ffffff;
        cursor: pointer;
      }
    }
    .dialog_title {
      font-family: PingFang-SC-Regular;
      font-size: 12px;
      font-weight: normal;
      font-stretch: normal;
      letter-spacing: 0px;
      color: #6c859a;
      margin-bottom: 12px;
    }
  }
}
.enable {
  background-color: #196bdf !important;
}
</style>
<style>
.identity .el-select .el-input__inner {
  width: 402px;
}
.identity .el-dialog {
  width: 450px;
}
</style>
