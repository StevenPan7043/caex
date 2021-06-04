<template>
  <div id="MainView-Mobile-sharem" ref="sharemMobile" v-loading.fullscreen.lock="loading">
    <div class="sharem-wrap page-sharem-Mobile">
      <div class="hied1" v-if="hied1==1">
        <transition name="el-zoom-in-center">
          <div
            class="join-wrap"
            v-if="shows[0]===true?true:false"
          >“小叮当“邀请你加入“围观顶级交易员爆仓”群聊，群聊参与人还有：半木夏、荀森森、玛莎拉帝、小野先森、金枪比特-米米家、王牌飞行员</div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="sys-wrap mb50" v-if="shows[1]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="msg-wrap">这里是币圈顶级交易员联盟，全球只有0.01%的人能加入。</div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="sys-wrap mb50" v-if="shows[2]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="msg-wrap">群里有5位联盟发起人，将对你进行严苛考验。顺利通过测试，你就能加入我们。从此有福同享，有难退群。</div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="sys-wrap mb50" v-if="shows[3]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="msg-wrap">你，准备好了吗?</div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-center">
          <div class="but" v-if="shows[4]===true?true:false">
            <el-button
              type="primary"
              round
              v-if="shows[4]===true?true:false"
              @click="openFullScreen1(1, 5)"
            >准备好了</el-button>
          </div>
        </transition>
      </div>
      <div class="hied2" v-if="hied1==2" ref="chatContent">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows1[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="banxia" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  半木夏
                  <span>(半庄)</span>
                </div>
                <div class="msg-wrap">IPFS生态中的奖励代币叫什么？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows1[1]===true?true:false">
            <img :src="t1" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio"
            :disabled="radiodisabled"
            @change="changeHandler"
            style="display: block;"
            v-if="shows1[2]===true?true:false"
          >
            <div class="boy2" v-for="(item, index) in radiolist" :key="index">
              <el-radio fill="#fff" :label="index">{{item}}</el-radio>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy3" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied3" v-if="hied1==3">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows2[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="xunsenen" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  荀森森
                  <span>(OKEx百团大赛冠军团长)</span>
                </div>
                <div class="msg-wrap">以下投资机构中，谁投资了filecoin？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows2[1]===true?true:false">
            <img :src="t2" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio1"
            :disabled="radiodisabled1"
            @change="changeHandler1"
            style="display: block;"
            v-if="shows2[2]===true?true:false"
          >
            <div v-for="(item, index) in radiolist1" :key="index">
              <div class="boy2 hied3">
                <el-radio fill="#fff" :label="index">{{item}}</el-radio>
              </div>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy3" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied4" v-if="hied1==4">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows3[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="mashaladi" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  玛莎拉帝
                  <span>(接针小能手)</span>
                </div>
                <div class="msg-wrap">热狗IPFS云算力出售，首轮几折优惠？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows3[1]===true?true:false">
            <img :src="t3" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio2"
            :disabled="radiodisabled2"
            @change="changeHandler2"
            style="display: block;"
            v-if="shows3[2]===true?true:false"
          >
            <div class="boy2" v-for="(item, index) in radiolist2" :key="index">
              <el-radio fill="#fff" :label="index">{{item}}</el-radio>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy4" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied5" v-if="hied1==5">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows4[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="xiaoye" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  小野先森
                  <span>(Hotdog实盘收益超百万)</span>
                </div>
                <div class="msg-wrap">直接用合约币做对应交易的方式，即币本位合约是以下哪种合约？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows4[1]===true?true:false">
            <img :src="t2" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio3"
            :disabled="radiodisabled3"
            @change="changeHandler3"
            style="display: block;"
            v-if="shows4[2]===true?true:false"
          >
            <div v-for="(item, index) in radiolist3" :key="index">
              <div class="boy2 hied3">
                <el-radio fill="#fff" :label="index">{{item}}</el-radio>
              </div>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy3" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied6" v-if="hied1==6">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows5[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="jinqiang" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  金枪比特-米米家
                  <span>(三年千万盈利实盘V)</span>
                </div>
                <div class="msg-wrap">以下哪个选项是合约交易需要掌握的？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows5[1]===true?true:false">
            <img :src="t3" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio4"
            :disabled="radiodisabled4"
            @change="changeHandler4"
            style="display: block;"
            v-if="shows5[2]===true?true:false"
          >
            <div class="boy2" v-for="(item, index) in radiolist4" :key="index">
              <el-radio fill="#fff" :label="index">{{item}}</el-radio>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy4" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied11" v-if="hied1==11">
        <transition name="el-zoom-in-center">
          <div class="boy" v-if="shows7[0]===true?true:false">
            <div class="sys-wrap mb50">
              <img :src="jinqiang" class="u-img" alt />
              <div class="inner-wrap">
                <div class="u-name">
                  王牌飞行员
                  <span>(实战十年)</span>
                </div>
                <div class="msg-wrap">热狗Hotdog的合约产品最高能提供多少倍杠杆？</div>
              </div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="boy1 option-img" v-if="shows7[1]===true?true:false">
            <img :src="t3" class="option-img1" alt />
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <el-radio-group
            v-model="radio7"
            :disabled="radiodisabled7"
            @change="changeHandler5"
            style="display: block;"
            v-if="shows7[2]===true?true:false"
          >
            <div class="boy2" v-for="(item, index) in radiolist5" :key="index">
              <el-radio fill="#fff" :label="index">{{item}}</el-radio>
            </div>
          </el-radio-group>
        </transition>
        <transition name="el-zoom-in-bottom">
          <div class="boy4" v-if="boytow" id="boytow1">
            <div class="sys-wrap user-wrap">
              <div class="inner-wrap">
                <div class="msg-wrap">
                  <div>你选择了</div>
                  <span>{{selectext}}</span>
                </div>
              </div>
              <img :src="sys" class="u-img" alt />
            </div>
          </div>
        </transition>
      </div>
      <div class="hied7" v-if="hied1==7">
        <transition name="el-zoom-in-center">
          <div class="sys-wrap mb50" v-if="shows6[1]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="msg-wrap">恭喜你顺利通过测试，欢迎加入”围观顶级交易员爆仓群”</div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-top">
          <div class="sys-wrap mb50" v-if="shows6[2]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="msg-wrap">正在计算你的2020交易盈利金额...</div>
            </div>
          </div>
        </transition>
        <transition name="el-zoom-in-center">
          <div class="sys-wrap mb50" v-if="shows6[3]===true?true:false">
            <img :src="sys" class="u-img" alt />
            <div class="inner-wrap">
              <div class="u-name">
                小叮当
                <span>(Hotdog创始人)</span>
              </div>
              <div class="res-wrap">
                <div class="result-wrap">
                  <div class="res-img-wrap res-img-wrap-0" @click="cloimg(8)" v-if="jg1">
                    <img :src="a" alt class="img-res" />
                  </div>
                  <div class="res-img-wrap res-img-wrap-1" @click="cloimg(9)" v-if="jg2">
                    <img :src="b" alt class="img-res" />
                  </div>
                  <div class="res-img-wrap res-img-wrap-2" @click="cloimg(10)" v-if="jg3">
                    <img :src="c" alt class="img-res" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
      <div class="hied8" v-if="hied1==8">
        <transition name="el-collapse-transition">
          <div class="res-img-wrap res-img-wrap-0" v-if="jg1">
            <img :src="a" alt class="img-res" @click="cloimg(7)" />
          </div>
        </transition>
      </div>
      <div class="hied9" v-if="hied1==9">
        <transition name="el-collapse-transition">
          <div class="res-img-wrap res-img-wrap-0" v-if="jg2">
            <img :src="b" alt class="img-res" @click="cloimg(7)" />
          </div>
        </transition>
      </div>
      <div class="hied10" v-if="hied1==10">
        <transition name="el-collapse-transition">
          <div class="res-img-wrap res-img-wrap-0" v-if="jg3">
            <img :src="c" alt class="img-res" @click="cloimg(7)" />
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  metaInfo: {
    title: '围观顶级交易员爆仓群（7）',
    meta: [
      {
        name: 'viewport',
        content:
          'width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no, viewport-fit=cover',
      },
      {
        name: 'msapplication-tap-highlight',
        content: 'no',
      },
      {
        name: 'MobileOptimized',
        content: '320',
      },
      {
        name: 'HandheldFriendly',
        content: 'true',
      },
    ],
  },
  data() {
    return {
      a: require('../assets/images/intake.png'),
      b: require('../assets/images/intake1.png'),
      c: require('../assets/images/intake2.png'),
      d: require('../assets/images/xuanyao.png'),
      sys: require('../assets/images/sys.png'),
      xunsenen: require('../assets/images/xunsenen.png'),
      t2: require('../assets/images/t2-1.png'),
      mashaladi: require('../assets/images/mashaladi.png'),
      xiaoye: require('../assets/images/xiaoye.png'),
      jinqiang: require('../assets/images/jinqiang.png'),
      t3: require('../assets/images/t3-1.png'),
      t1: require('../assets/images/t1.png'),
      banxia: require('../assets/images/banxia.png'),
      shows: [],
      shows1: [],
      shows2: [],
      shows3: [],
      shows4: [],
      shows5: [],
      shows6: [],
      shows7: [],
      shows8: [],
      code: [2, 0, 1, 1, 3, 3],
      radiolist: ['A. BTC', 'B. ETH', 'C. FIL', 'D. EOS'],
      radiolist1: [
        'A. 红杉资本',
        'B. 数字资本',
        'C. 共识实验室',
        'D. 科银资本',
      ],
      radiolist2: ['A. 9折', 'B. 8折', 'C. 8.5折', 'D. 9.5折'],
      radiolist3: ['A.  正向合约', 'B.  反向合约', 'C.  ab都不是'],
      radiolist4: [
        'A. 仓位管理',
        'B. 要有风险意识',
        'C. 学习正确的交易系统',
        'D. 以上都是',
      ],
      radiolist5: ['A. 10', 'B. 20', 'C. 50', 'D. 100'],
      loading: false,
      hied1: 1,
      radio: '',
      boytow: false,
      selectext: '',
      radiodisabled: false,
      radio1: '',
      radiodisabled1: false,
      radio2: '',
      radiodisabled2: false,
      radio3: '',
      radiodisabled3: false,
      radio4: '',
      radiodisabled4: false,
      radio5: '',
      radiodisabled5: false,
      radio6: '',
      radiodisabled6: false,
      radio7: '',
      radiodisabled7: false,
      maps: [],
      jg1: false,
      jg2: false,
      jg3: false,
    }
  },
  mounted() {
    setTimeout(() => {
      document.body.style.minWidth = '0px'
    }, 200)
    this.openFullScreen1(0, 5)
  },

  beforeDestroy() {
    document.body.style.minWidth = '1200px'
    document.html.style.minWidth = '1200px'
    this.befdisabled()
  },
  methods: {
    goAnchor(selector) {
      this.$el.querySelector('#boytow1').scrollIntoView({
        behavior: 'smooth',
        block: 'start',
      })
    },
    changeHandler(e) {
      this.radiodisabled = true
      this.maps.push(e)
      this.selectext = this.radiolist[e]
      this.boytow = true
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(2, 4)
      }, 1000)
    },
    changeHandler1(e) {
      this.radiodisabled1 = true
      this.maps.push(e)
      this.selectext = this.radiolist1[e]
      this.boytow = true
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(3, 4)
      }, 1000)
    },
    changeHandler2(e) {
      this.radiodisabled2 = true
      this.maps.push(e)
      this.selectext = this.radiolist2[e]
      this.boytow = true
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(4, 4)
      }, 1000)
    },
    changeHandler3(e) {
      this.radiodisabled3 = true
      this.maps.push(e)
      this.selectext = this.radiolist3[e]
      this.boytow = true
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(5, 4)
      }, 1000)
    },
    changeHandler4(e) {
      this.radiodisabled4 = true
      this.maps.push(e)
      this.selectext = this.radiolist4[e]
      this.boytow = true
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(6, 4)
      }, 1000)
    },
    changeHandler5(e) {
      this.radiodisabled7 = true
      this.maps.push(e)
      this.selectext = this.radiolist5[e]
      this.boytow = true
      let temp = JSON.parse(JSON.stringify(this.maps))
      let ct = []
      let ask = this.code.every((value, index, array) => {
        if (value == temp[index]) {
          return true
        } else {
          ct.push(index)
        }
        return false
      })
      if (ask) {
        this.jg1 = true
      }
      let a = JSON.parse(JSON.stringify(ct))
      if (a[0] >= 5) {
        this.jg2 = true
      } else if (a[0] <= 4) {
        this.jg3 = true
      }
      setTimeout(() => {
        this.goAnchor()
      }, 200)
      setTimeout(() => {
        this.openFullScreen1(11, 4)
      }, 1000)
    },
    getRepeatNum() {
      return this.maps.reduce(function (prev, next) {
        prev[next] = prev[next] + 1 || 1
        return prev
      }, {})
    },
    openFullScreen1(index, num) {
      this.loading = true
      this.boytow = false
      this.selectext = ''
      if (index == 6) {
        this.hied1 = 11
      } else if (index == 11) {
        this.hied1 = 7
      } else {
        this.hied1 = index + 1
      }
      setTimeout(() => {
        this.loading = false
        var that = this
        var i = 1
        var timer = setInterval(function () {
          if (i <= num) {
            if (index === 0) {
              that.shows.push(true)
            } else if (index === 1) {
              that.shows1.push(true)
            } else if (index === 2) {
              that.shows2.push(true)
            } else if (index === 3) {
              that.shows3.push(true)
            } else if (index === 4) {
              that.shows4.push(true)
            } else if (index === 5) {
              that.shows5.push(true)
            } else if (index === 6) {
              that.shows7.push(true)
            } else if (index === 11) {
              that.shows6.push(true)
            }
            i++
          } else {
            clearInterval(timer)
          }
        }, 600)
      }, 800)
    },
    cloimg(index, hie) {
      this.hied1 = index
    },
    share() {},
    befdisabled() {
      this.radiodisabled = true
      this.radiodisabled1 = true
      this.radiodisabled2 = true
      this.radiodisabled3 = true
      this.radiodisabled4 = true
    },
  },
}
</script>

<style>
body,
html {
  min-width: 0px;
}
#MainView-Mobile-sharem {
  margin: auto 0;
  padding: 20px 20px;
  color: #1c242c;
}
.transition-box {
  margin-bottom: 10px;
  width: 200px;
  height: 100px;
  border-radius: 4px;
  background-color: #409eff;
  text-align: center;
  color: #fff;
  padding: 40px 20px;
}

.join-wrap {
  height: 80px;
  background: #ebeef4;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 10px auto;
  font-size: 14px;
  line-height: 20px;
  color: #707a86;
  padding: 10px 20px;
}

.sys-wrap {
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  margin-top: 30px;
}
.sys-wrap .u-img {
  width: 45px;
  margin-right: 10px;
}
.sys-wrap .inner-wrap .u-name {
  font-size: 14px;
  color: #262b30;
  margin-bottom: 10px;
}
.sys-wrap .inner-wrap .u-name span {
  font-size: 14px;
  color: #707a86;
}
.sys-wrap .inner-wrap .msg-wrap {
  min-width: 100px;
  border-radius: 10px;
  padding: 15px 15px 20px 15px;
  background: #e5f4ff;
  font-size: 16px;
  color: #262b30;
  line-height: 20px;
  margin-top: 15px;
}
.but {
  margin: 0 auto;
  margin-top: 60px;
  text-align: center;
}

.option-img {
  display: block;
  margin: 0 auto;
  text-align: center;
}
.option-img .option-img1 {
  width: 120px;
}
.boy2 {
  display: flex;
  align-items: center;
  padding: 20px 35px;
  margin-top: 20px;
  border-radius: 50px;
  background: #fff;
}
.boy3,
.boy4 {
  transition: all 0.4s;
}
.boy3 .sys-wrap,
.boy4 .sys-wrap {
  justify-content: flex-end;
}
.boy3 .sys-wrap .u-img,
.boy4 .sys-wrap .u-img {
  width: 45px;
  margin-left: 10px;
  margin-right: 0;
}
.boy3 .sys-wrap .inner-wrap .msg-wrap {
  min-width: 220px;
}

.option-img .option-img2 {
  width: 70px;
}

.boy2.hied3 {
  margin-top: 0px;
  margin-bottom: 20px;
}
.hied3 .sys-wrap,
.hied4 .sys-wrap {
  margin-bottom: 20px;
}

.res-wrap {
  width: 113px;
  height: 200px;
  border-radius: 10px;
  overflow: hidden;
}
.result-wrap .res-img-wrap {
  width: 100%;
  position: relative;
  align-items: center;
  justify-content: center;
}
.result-wrap.scale-to-1 {
  position: fixed;
  transform: scale(1);
  transform-origin: 0% 0%;
}
.result-wrap .res-img-wrap .img-res {
  width: 100%;
}
.result-wrap .res-img-wrap .share {
  position: absolute;
  width: 35px;
  bottom: 10px;
  left: 63px;
}
.result-wrap .res-img-wrap .study {
  position: absolute;
  width: 35px;
  bottom: 10px;
  right: 63px;
}
.hied8 .res-img-wrap,
.hied9 .res-img-wrap,
.hied10 .res-img-wrap {
  z-index: 10;
  position: relative;
  left: 0;
  right: 0;
  bottom: 0;
  top: 0;
  background: #f7f8fa;
}

.hied8 .res-img-wrap .share,
.hied9 .res-img-wrap .share,
.hied10 .res-img-wrap .share {
  position: absolute;
  width: 110px;
  height: 40px;
  left: 30px;
  bottom: 25px;
}
.hied8 .res-img-wrap .study,
.hied9 .res-img-wrap .study,
.hied10 .res-img-wrap .study {
  position: absolute;
  width: 110px;
  height: 40px;
  right: 30px;
  bottom: 25px;
}
.hied8 .res-img-wrap .img-res,
.hied9 .res-img-wrap .img-res,
.hied10 .res-img-wrap .img-res {
  width: 100%;
}
</style>
