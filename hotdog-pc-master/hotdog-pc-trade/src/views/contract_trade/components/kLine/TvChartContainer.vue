<template>
  <div class="TVChartContainer chart-main" id="tv_chart_container" />
</template>

<script>
import { widget } from './charting_library.min'

import { baseUrl } from '../../../../../public/config.js'
import request from '@/libs/axios3'

export default {
  name: 'TVChartContainer',
  data() {
    return {
      loop: '',
    }
  },
  props: {
    symbol: {
      default: 'btc/usdt',
      type: String,
    },
    klineDetail: {
      default: {},
      type: Object,
    },
    klineList: {
      default: () => [],
      type: Array,
    },
    interval: {
      default: '15',
      type: String,
    },
    height: {
      default: '420px',
      type: String,
    },
    width: {
      default: '100%',
      type: String,
    },
  },
  mounted() {
    this.TVChartInit()
  },
  data() {
    return {
      Kdata: {},
      kline: null,
    }
  },
  watch: {
    symbol() {
      clearInterval(this.loop)
      this.TVChartInit()
    },
    klineDetail() {
      this.Kdata = this.klineDetail
      this.OnsubscribeK()
    },
    klineList() {
      this.Klist = this.klineList
      this.OnsubKlist()
    },
  },
  beforeDestroy() {
    window.clearInterval(this.loop)
  },
  methods: {
    // 时间周期格式转换
    getResolutionFormat(resolution) {
      // console.log(resolution)
      let reg = /^[0-9]*$/
      if (reg.test(resolution)) {
        resolution = resolution + 'min'
      } else if (resolution.includes('D')) {
        resolution = '1day'
      } else if (resolution.includes('W')) {
        resolution = '1week'
      } else if (resolution.includes('M')) {
        resolution = '1mon'
      }
      return resolution
    },
    OnsubscribeK() {},
    OnsubKlist() {},
    TVChartInit() {
      var mythat = this
      var Datafeeds = {}
      Datafeeds.WebsockFeed = function () {}

      Datafeeds.WebsockFeed.prototype.onReady = function (callback) {
        let config = {}
        config.supported_resolutions = ['1', '5', '15', '60', '1D', '1W']
        callback(config)
      }

      Datafeeds.WebsockFeed.prototype.resolveSymbol = function (
        symbolName,
        onSymbolResolvedCallback,
        onResolveErrorCallback
      ) {
        let data = {
          symbol: symbolName,
          has_daily: true,
          ticker: symbolName,
          name: symbolName,
          description: symbolName,
          type: 'bitcoin',
          exchange: 'hotdogvip.com',
          listed_exchange: 'hotdogvip.com',
          timezone: 'Asia/Shanghai',
          has_weekly_and_monthly: true,
          has_intraday: true,
          session: '24x7',
          supported_resolutions: ['1', '5', '15', '60', '1D', '1W'],
          intraday_multipliers: ['1', '5', '15', '60', '1D', '1W'],
          has_fractional_volume: false,
          full_name: '',
          has_empty_bars: false,
          'exchange-traded': symbolName,
          minmove2: 0,
          minmov: 1,
          pricescale: 10000,
          data_status: 'streaming',
          force_session_rebuild: false,
          has_no_volume: false,
        }

        onSymbolResolvedCallback(data)
      }

      Datafeeds.WebsockFeed.prototype.getBars = function (
        symbolInfo,
        resolution,
        from,
        to,
        onHistoryCallback,
        onErrorCallback,
        firstDataRequest
      ) {
        resolution = mythat.getResolutionFormat(resolution)
        // 根据时间周期来加载
        // if (firstDataRequest) {
        //   mythat.$ajax(`account/getKlineList?token=${mythat.$userID()}&symbol=${mythat.symbol}&klineType=${resolution}`).then(res => {
        //     if (res.data.status) {
        //       var data = res.data.data
        //       var lastData = []
        //       data.forEach(ele => {
        //         if (ele.amount !== 0) {
        //           lastData.push(ele)
        //         }
        //       })
        //       let bars = lastData.map(el => {
        //         return {
        //           time: el.id * 1000,
        //           close: el.close,
        //           high: el.high,
        //           low: el.low,
        //           open: el.open,
        //           volume: el.amount
        //         }
        //       })

        //       onHistoryCallback(bars, {
        //         noData: false
        //       })
        //     }
        //   }).catch(function (reason) {
        //     onErrorCallback(reason)
        //   })
        // } else {
        //   onHistoryCallback([], {
        //     noData: true
        //   })
        // }

        // 火币合约
        let symbolsold = mythat.symbol.replace('/', '-').toUpperCase()
        let symbolst = symbolsold.replace('-USDT', '-USD')
        let curTime = Math.round(new Date().getTime() / 1000)
        let day = 10;
        switch (resolution) {
          case '1min':
            day = 1
            break
          case '5min':
            day = 5
            break
          case '15min':
            day = 10
            break
          case '60min':
            day = 20
            break
          case '1day':
            day = 200
            break
          case '1week':
            day = 1000
            break
        }
        let startDate = Math.round(curTime - day * 3600 * 24)
        // 根据时间周期来加载
        if (firstDataRequest) {
          mythat
            .$get(
              `/trade/historyKline?contract_code=${symbolst}&period=${resolution}&from=${startDate}&to=${curTime}`
            )
            .then((res) => {
              if (res.status === 200) {
                // var data = mythat.klineList
                if (res.data.data) {
                  var data = JSON.parse(res.data.data)
                  var lastData = []
                  data.data.forEach((ele) => {
                    if (ele.amount !== 0) {
                      lastData.push(ele)
                    }
                  })
                  let bars = lastData.map((el) => {
                    return {
                      time: el.id * 1000,
                      close: el.close,
                      high: el.high,
                      low: el.low,
                      open: el.open,
                      volume: el.amount,
                    }
                  })

                  onHistoryCallback(bars, {
                    noData: false,
                  })
                }
              }
            })
            .catch(function (reason) {
              onErrorCallback(reason)
            })
        } else {
          onHistoryCallback([], {
            noData: true,
          })
        }
      }

      // 订阅K线数据
      Datafeeds.WebsockFeed.prototype.subscribeBars = function (
        symbolInfo,
        resolution,
        onRealtimeCallback,
        listenerGUID,
        onResetCacheNeededCallback
      ) {
        resolution = mythat.getResolutionFormat(resolution)

        // subscribeK()
        // mythat.loop = setInterval(function () {
        //   subscribeK()
        // }, 500)

        // function subscribeK() {
        //   resolution = mythat.getResolutionFormat(resolution)
        //   mythat
        //     .$ajax(
        //       `/account/getKlineCurrent?token=${mythat.$userID()}&symbol=${mythat.symbol.toLowerCase()}&klineType=${resolution}`
        //     )
        //     .then((res) => {
        //       if (res.data.status) {
        //         let bar = {
        //           time: res.data.data.id * 1000,
        //           close: res.data.data.close,
        //           high: res.data.data.high,
        //           low: res.data.data.low,
        //           open: res.data.data.open,
        //           volume: res.data.data.amount,
        //         }
        //         // 服务器返回的数据塞进去
        //         onRealtimeCallback(bar)
        //       }
        //     })
        // }
        // /火币合约
        mythat.$emit('func', resolution)
        mythat.OnsubscribeK = () => {
          let data = mythat.klineDetail
          if (data) {
            let bar = {
              time: data.id * 1000,
              close: data.close,
              high: data.high,
              low: data.low,
              open: data.open,
              volume: data.amount,
            }
            // 服务器返回的数据塞进去
            onRealtimeCallback(bar)
          }
        }
      }

      Datafeeds.WebsockFeed.prototype.unsubscribeBars = function (
        subscriberUID
      ) {
        // 清除之前的轮询
        clearInterval(mythat.loop)
      }

      const widgetOptions = {
        fullscreen: false,
        symbol: this.symbol,
        interval: this.interval,
        width: this.width,
        height: this.height,
        container_id: 'tv_chart_container',
        timezone: 'Asia/Shanghai',
        loading_screen: {
          backgroundColor: 'rgb(255,255,255)',
        },
        autosize: true,
        datafeed: new Datafeeds.WebsockFeed(),
        locale: 'zh',
        // static文件夹的路径
        library_path: '/charting_library/',
        disabled_features: [
          'header_symbol_search',
          'header_widget_dom_node',
          'adaptive_logo',
          'constraint_dialogs_movement',
          'display_market_status',
          'header_compare',
          'header_undo_redo',
          'header_screenshot',
          'volume_force_overlay',
          'caption_buttons_text_if_possible',
          'chart_property_page_trading',
          'header_saveload',
          'timeframes_toolbar',
          'border_around_the_chart',
          'star_some_intervals_by_default',
          'datasource_copypaste',
          'right_bar_stays_on_scroll',
          'context_menus',
          'pane_context_menu',
          'scales_context_menu',
          'legend_context_menu',
          'go_to_date',
        ],
        theme: 'Light',
        overrides: {
          'mainSeriesProperties.style': 1,
          'paneProperties.background': '#ffffff', // 背景色
          'mainSeriesProperties.candleStyle.upColor': '#14ce96',
          'mainSeriesProperties.candleStyle.downColor': '#ff4d4d',
          // 烛心
          'mainSeriesProperties.candleStyle.drawWick': true,
          // 烛心颜色
          'mainSeriesProperties.candleStyle.wickUpColor': '#14ce96',
          'mainSeriesProperties.candleStyle.wickDownColor': '#ff4d4d',

          // 边框
          'mainSeriesProperties.candleStyle.drawBorder': true,
          'mainSeriesProperties.candleStyle.borderUpColor': '#14ce96',
          'mainSeriesProperties.candleStyle.borderDownColor': '#ff4d4d',
          // 网格
          'paneProperties.vertGridProperties.style': 0,
          'paneProperties.horzGridProperties.color': '#d2d2d2', // 横行线
          'paneProperties.vertGridProperties.color': '#d2d2d2', // 竖行线
          // 坐标轴和刻度标签颜色
          // 区域+字坐标颜色
          'scalesProperties.lineColor': '#8a8a8a',
          'scalesProperties.textColor': '#8a8a8a',
          // shezhizuigaok线柱距离top-border的高度
          'paneProperties.topMargin': 5,
          'paneProperties.bottomMargin': 5,
          'timeScale.barSpacing': 50,
          // 收起左上角标题
          'paneProperties.legendProperties.showLegend': false,
          'symbolWatermarkProperties.transparency': 90,
        },
        studies_overrides: {
          'volume.volume.color.0': '#ff4d4d',
          'volume.volume.color.1': '#14ce96',
        },
        toolbar_bg: '#ffffff',
      }

      const tvWidget = new widget(widgetOptions)
      tvWidget.onChartReady(() => {
        tvWidget
          .chart()
          .createStudy('Moving Average', false, false, [5], null, {
            'plot.color': '#b7248a',
            'Plot.linewidth': 1,
          })
        tvWidget
          .chart()
          .createStudy('Moving Average', false, false, [10], null, {
            'plot.color': '#84aad5',
            'Plot.linewidth': 1,
          })
        tvWidget
          .chart()
          .createStudy('Moving Average', false, false, [30], null, {
            'plot.color': '#55b263',
            'Plot.linewidth': 1,
          })
        tvWidget
          .chart()
          .createStudy('Moving Average', false, false, [60], null, {
            'plot.color': '#965fc4',
            'Plot.linewidth': 1,
          })
      })
      this.kline = Datafeeds
    },
  },
}
</script>
