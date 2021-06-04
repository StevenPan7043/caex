<template>
  <dl class="f-cb">
    <dd>
      <span class="dspName">{{ticker.dspName}}</span>
    </dd>
    <dd>
      <div class="flex_column">
          <span v-if="trade.length===0" class="desc"
                :class="headerRiseFallPercent>0?'rise':'fall'"
          >{{lastprice.toFixed(currentPricePrecision)}}</span>
        <span class="desc"
              :class="headerRiseFallPercent>0?'rise':'fall'"
              v-else
        >{{trade[0].price.toFixed(currentPricePrecision)}}</span>
        <span class="cny-bar uppercase">{{'≈ ' + equalMoney.toFixed(2) + ' cny'}}</span>
      </div>
    </dd>
    <dd>
      <dl class="dldd">
        <dt>
          <span class="rate-pre">{{$t('home.TopGainers')}}</span>
        </dt>
        <dd>
          <span
                  class="rate"
                  :class="headerRiseFallPercent>0?'rise':'fall'"
          >{{headerRiseFallPercent|isNaNFilter}}%</span>
        </dd>
      </dl>
    </dd>
    <dd>
      <dl class="dldd">
        <dt>{{$t('tradecenter.High')}}</dt>
        <dd>
          <span class="high">{{ticker.high.toFixed(currentPricePrecision)}}</span>
        </dd>
      </dl>
    </dd>
    <dd>
      <dl class="dldd">
        <dt>{{$t('tradecenter.Low')}}</dt>
        <dd>
          <span class="low">{{ticker.low.toFixed(currentPricePrecision)}}</span>
        </dd>
      </dl>
    </dd>
    <dd>
      <dl class="dldd">
        <dt>{{$t('tradecenter.Quantity')}}</dt>
        <dd>
          <span class="amount uppercase">{{ticker.volume.toFixed(0)+' '+ticker.baseCurrencyName}}</span>
        </dd>
      </dl>
    </dd>
    <dd class="ticker-logo">
      <!-- <el-dropdown @command="themecolor"> -->
      <el-dropdown>
            <span class="el-dropdown-link">
<!--              <img src="@/assets/images/ico.ico">-->
              <i class="iconfont icon-yanseqiehuanico"></i>
            </span>
        <el-dropdown-menu slot="dropdown" style="margin-top:-5px;color:#b4c6ee;margin-right: -20px;">
          <el-dropdown-item id="ColorWhite"><img @click="themeColorWhite()" src="@/assets/images/ico-bai.png" alt="">
            <p>{{$t('tradecenter.white')}}</p></el-dropdown-item>
          <el-dropdown-item id="ColorBlue"><img @click="themeColorBlue()" src="@/assets/images/ico-hei.png" alt="">
            <p>{{$t('tradecenter.Blue')}}</p></el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <!--      <el-dropdown-menu slot="dropdown">-->
      <!--              <el-dropdown-item command="a">白色</el-dropdown-item>-->
      <!--              <el-dropdown-item command="c">黑色</el-dropdown-item>-->
      <!--      </el-dropdown-menu>-->
      <!--      </el-dropdown>-->
    </dd>
  </dl>
  <!--  <img src="@/assets/images/ico.svg" v-show="command=='a'" />-->
  <!--  <img src="@/assets/images/sunlightwhite.png" v-show="command=='c'" />-->
</template>
<script>
export default {
  props: [

    'BtcPrice', 'EthPrice',
    'ticker',
    'currentPricePrecision',
    'convertCNY',
    'currentVolumePrecision',
    'lastprice',
    'trade',
    'UsdtPrice',
    'allTicker',
    'quoteCurrencyName'
  ],
  data () {
    return {
      // background: "c"
    }
  },
  filters: {
    isNaNFilter (val) {
      if (isNaN(val)) {
        return 0
      } else {
        return val
      }
    }
  },
  mounted () {
    this.WhiteOrBlue()
  },
  computed: {
    headerRiseFallPercent () {
      for (let i = 0; i < this.allTicker.length; i++) {
        if (this.ticker.dspName === this.allTicker[i].dspName) {
          return this.allTicker[i].tickerRiseFallPercent
        }
      }
      return 0
    },
    equalMoney () {
      let price = 0
      if (this.trade.length > 0) {
        price = this.trade[0].price
      }
      if (this.quoteCurrencyName === 'USDT') {
        return this.UsdtPrice * price
      } else if (this.quoteCurrencyName === 'ETH') {
        return this.EthPrice * price
      } else {
        return this.BtcPrice * price
      }
    }
  }
  ,
  methods: {
    // themecolor(command) {
    //   this.command = command == "a" ? "c" : "a";
    //   document.getElementById("app").className = "theme" + this.command;
    //   this.$store.commit("setThemeColor", this.command);
    //   this.$emit("background", this.command);
    // }
    themeColorWhite () {
      localStorage.setItem('back', 'a')
      document.getElementById('app').className = 'themea'
      document.getElementById('ColorWhite').className = 'el-dropdown-menu__item cur'
      document.getElementById('ColorBlue').className = 'el-dropdown-menu__item'
      this.$store.commit('setThemeColor', 'a')
      this.$emit('background', 'a')

    }
    ,
    themeColorBlue () {
      localStorage.setItem('back', 'c')
      document.getElementById('app').className = 'themec'
      document.getElementById('ColorBlue').className = 'el-dropdown-menu__item cur'
      document.getElementById('ColorWhite').className = 'el-dropdown-menu__item'
      this.$store.commit('setThemeColor', 'c')
      this.$emit('background', 'c')
    }
    ,
    WhiteOrBlue () {
      var colorBG = localStorage.getItem('background')
      if (colorBG == 'a') {
        document.getElementById('ColorWhite').className = 'el-dropdown-menu__item cur'
        document.getElementById('ColorBlue').className = 'el-dropdown-menu__item'
      } else {
        document.getElementById('ColorBlue').className = 'el-dropdown-menu__item cur'
        document.getElementById('ColorWhite').className = 'el-dropdown-menu__item'
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .flex_column {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 45px;

    .desc {
      line-height: 18px;
    }

    .cny-bar {
      line-height: 18px;

    }
  }
</style>
