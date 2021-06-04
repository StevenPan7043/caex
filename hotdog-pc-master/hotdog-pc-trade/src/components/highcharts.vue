<template>
  <div>
    <div id="containers" class="conta-chart" style="min-width:480px;height:480px"></div>
  </div>
</template>

<script>
// 导入chart组件
var myvue = {}
import Highcharts from '../highcharts.js'

export default {
  props: [
    'bg',
    'depth',
    'currentVolumePrecision',
    'currentPricePrecision'
  ],
  data () {
    return {
      backgroundColorsd: '',
      depthBidsArr: [],
      depthAsksArr: [],
      sellAmountArr: [],
      buyAmountArr: [],
      SellhighchartsArr: [],
      BuyhighchartsArr: [],
      option: {},
      data: [{
        name: '买',
        data: [[0, 0], [0, 0]]
      },
        {
          name: '卖',
          data: [[0, 0], [0, 0]]
        }],
      other: {
        chart: {
          type: 'area',
        },
        title: {
          text: ''
        },
        subtitle: {
          text: ''
        },
        credits: {
          text: '',
          href: ''
        },
        xAxis: {
          title: {
            text: ''
          },
          labels: {
            formatter: function () {
              return this.value
            }
          },
        },
        yAxis: {
          title: {
            text: ''
          },
          labels: {
            formatter: function () {
              return this.value
            }
          }
        },
        tooltip: {
          padding: 30,
          shared: !0,
          snap: 20,
          crosshairs: {
            dashStyle: 'shortdot',
            color: 'green'
          },
          formatter: function () {
            return '委 托 价 ：' + this.points[0].x + '<br/>' + '累 计 ：' + Math.round(this.points[0].y)
          }
        },
        plotOptions: {
          area: {
            pointStart: 0,
            marker: {
              enabled: !1,
              symbol: 'circle',
              radius: 2,
              states: {
                hover: {
                  enabled: !0
                }
              }
            }
          }
        },
        series: [{
          name: ' ',
          data: []
        },
          {
            name: ' ',
            data: []
          }]
      }
    }
  },
  beforeCreate: function () {
    myvue = this
  },
  mounted: function () {
    this.loadChart()
    this.initH()
  },
  watch: {
    bg (v) {
      this.a(v)
    },
    depth (depth) {
      this.loadDepthPanel(depth)
    }
  },
  methods: {
    initH(){
      myvue.other.series = myvue.data
      myvue.option = myvue.other
      Highcharts.zdy_chart = new Highcharts.Chart('containers', myvue.option)
    },
    loadDepthPanel (depth) {
      //bid数组
      let depthBidsArr = depth.bids.slice(0, 100)
      this.depthBidsArr = depthBidsArr
      //ask数组
      let depthAsksArr = depth.asks.slice(0, 100)
      depthAsksArr.forEach((ele, index) => {
        ele.type = this.$t('header.TradeCenterSell') + (index + 1)
      })
      this.depthAsksArr = depthAsksArr.reverse()
      this.depthBuyPanel()
      this.depthSellPanel()
    },
    //BUY样式动态生成
    depthBuyPanel () {
      let buyAmount = 0
      let buyAmountArr = []
      let buyVolumeArr = []
      let buyVolumeDivideArr = []
      let buyPrec = []
      let BuyhighchartsArr = []
      let depthBidsArrLength = this.depthBidsArr.length
      for (let i = 0; i < depthBidsArrLength; i++) {
        buyVolumeArr.push(this.depthBidsArr[i][1])
        buyPrec.push(this.depthBidsArr[i][0])
      }
      let maxVolume = Math.max.apply(null, buyVolumeArr)
      for (let j = 0; j < buyVolumeArr.length; j++) {
        buyAmount += buyVolumeArr[j]
        buyAmountArr.push(buyAmount.toFixed(this.currentVolumePrecision))
      }

      for (let i = buyPrec.length - 1; i >= 0; i--) {
        BuyhighchartsArr.push([buyPrec[i], Number(buyAmountArr[i])])
      }
      var a = {
        name: '买',
        data: BuyhighchartsArr
      }
      Highcharts.zdy_chart.series[0].update(a)
      // var sd = {
      //     tooltip:{
      //         backgroundColor:'#00b477'
      //     }
      // }
      // Highcharts.zdy_chart.update(sd)
    },
    //SELL样式动态生成
    depthSellPanel () {
      let sellAmount = 0
      let sellAmountArr = []
      let sellVolumeArr = []
      let sellVolumeDivideArr = []
      let sellPrec = []
      let SellhighchartsArr = []
      let depthAsksArrLength = this.depthAsksArr.length

      for (let i = depthAsksArrLength - 1; i >= 0; i--) {
        sellVolumeArr.push(this.depthAsksArr[i][1])
        sellPrec.push(this.depthAsksArr[i][0])
      }
      let maxVolume = Math.max.apply(null, sellVolumeArr)
      for (let j = 0; j < sellVolumeArr.length; j++) {
        sellAmount += sellVolumeArr[j]
        sellAmountArr.push(sellAmount.toFixed(this.currentVolumePrecision))
      }
      for (let i = sellPrec.length - 1; i >= 0; i--) {
        SellhighchartsArr.push([sellPrec[i], Number(sellAmountArr[i])])
      }
      this.sellAmountArr = sellAmountArr.reverse()
      var a = {
        name: '卖',
        data: SellhighchartsArr
      }
      Highcharts.zdy_chart.series[1].update(a)
      // var sd = {
      //     tooltip:{
      //         backgroundColor:'#e93c55'
      //     }
      // }
      // Highcharts.zdy_chart.update(sd)
    },

    a (v) {
      if (v == 'a') {
        this.loadChart('#ffffff')
      } else {
        this.loadChart('#1f2431')
      }
    },
    loadChart (v) {
      if (v == undefined) {
        v = window.localStorage.getItem('back') !== 'a' ? '#ffffff' : '#1f2431'
      }
      var chartTheme = {
        colors: ['#00b477', '#e93c55'],
        chart: {
          backgroundColor: v,
        },
        xAxis: {
          gridLineWidth: 0,
          lineColor: '#ffffff',
          tickColor: '#ffffff',
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          title: {
            style: {
              color: '#8a8a8a'
            }
          }
        },
        yAxis: {
          gridLineWidth: 0,
          lineColor: '#ffffff',
          lineWidth: 1,
          tickWidth: 1,
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          title: {
            style: {
              color: '#8a8a8a'
            }
          }
        },
        plotOptions: {
          series: {
            fillOpacity: .2
          }
        }
      }
      Highcharts.setOptions(chartTheme)
      Highcharts.zdy_chart = new Highcharts.Chart('containers', myvue.option)

    }
  },
}
</script>

