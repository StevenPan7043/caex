(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-index-index"],{"0fa9":function(n,t,e){"use strict";var i=function(){var n=this,t=n.$createElement,e=n._self._c||t;return e("v-uni-view",[e("v-uni-view",{staticClass:"seat"}),e("v-uni-view",{staticClass:"top-head",class:n.topStatus?"showBorder":""},[e("v-uni-view",{staticClass:"top-head-left"},[e("v-uni-image",{attrs:{src:"/static/images/logo_top.png",mode:""}}),e("v-uni-text",[n._v("G币")])],1),n.loginStatus?e("v-uni-view",{staticClass:"top-head-right",on:{click:function(t){t=n.$handleEvent(t),n.goUserCenter(t)}}},[e("v-uni-image",{attrs:{src:"../../static/images/user_top.png",mode:""}}),e("v-uni-text",[n._v(n._s(n.topPhone))])],1):e("v-uni-view",{staticClass:"top-head-right",on:{click:function(t){t=n.$handleEvent(t),n.goLogin(t)}}},[e("v-uni-text",[n._v("点击登录")])],1)],1),e("v-uni-view",{staticClass:"banner-head"},[n.banner.length>0?e("v-uni-swiper",{staticClass:"swiper",attrs:{"indicator-dots":n.indicatorDots,autoplay:!0,interval:n.interval,circular:!0,duration:n.duration,"indicator-color":n.color,"indicator-active-color":n.colorActive}},n._l(n.banner,function(n,t){return e("v-uni-swiper-item",{key:t},[e("v-uni-image",{attrs:{src:n.imgurl?n.imgurl:"/static/images/banner1.png"}})],1)}),1):e("v-uni-swiper",{staticClass:"swiper"},[e("v-uni-swiper-item",[e("v-uni-image",{attrs:{src:"/static/images/banner1.png"}})],1)],1)],1),e("v-uni-view",{staticClass:"hot-news"},[e("v-uni-view",{staticClass:"hot-news-title"},[e("v-uni-image",{attrs:{src:"/static/images/info_icon.png",mode:""}})],1),e("v-uni-view",{staticClass:"hot-news-box"},[n.newsList.length>0?e("v-uni-swiper",{attrs:{autoplay:n.autoplay2,interval:n.interval2,duration:n.duration2,vertical:n.vertical2,circular:n.circular2}},n._l(n.newsList,function(t,i){return e("v-uni-swiper-item",{key:i},[e("v-uni-text",{staticClass:"text_overflow",on:{click:function(e){e=n.$handleEvent(e),n.gotoHotnews(t.id)}}},[n._v(n._s(t.title?t.title:"数据加载中..."))])],1)}),1):e("v-uni-swiper",[e("v-uni-swiper-item",[e("v-uni-text",{staticClass:"text_overflow"},[n._v("数据正在拼命加载中...")])],1)],1)],1),e("v-uni-view",{staticClass:"news-more",on:{click:function(t){t=n.$handleEvent(t),n.moreNotice(t)}}},[e("v-uni-image",{attrs:{src:"/static/images/right_more.png",mode:""}})],1)],1),e("v-uni-view",{staticClass:"wave"},n._l(n.WebSocketData,function(t,i){return i<3?e("v-uni-view",{key:i,staticClass:"wave-item",on:{click:function(t){t=n.$handleEvent(t),n.goCharts(i)}}},[e("v-uni-text",[n._v(n._s(t.name))]),e("v-uni-text",{class:0==t.isout?"down":"up"},[n._v(n._s(t.usdtPrice))]),e("v-uni-text",{class:0==t.isout?"down":"up"},[n._v(n._s(0==t.isout?"-":"+")+n._s(n._f("tofixed4")(100*t.scale))+"%")]),e("v-uni-text",[n._v("≈"+n._s(t.cny)+"CNY")])],1):n._e()}),1),e("v-uni-view",{staticClass:"h20"}),e("v-uni-view",{staticClass:"ad"},[e("v-uni-image",{attrs:{src:"../../static/images/ad_1.png",mode:""},on:{click:function(t){t=n.$handleEvent(t),n.goPage(0)}}}),e("v-uni-image",{attrs:{src:"../../static/images/ad_2.png",mode:""},on:{click:function(t){t=n.$handleEvent(t),n.goPage(1)}}})],1),e("v-uni-view",{staticClass:"h20"}),e("v-uni-view",{staticClass:"quotation"},[e("v-uni-view",{staticClass:"quotation-title"},[n._v("行情涨幅榜")]),e("v-uni-view",{staticClass:"quotation-head"},[e("v-uni-text",[n._v("名称")]),e("v-uni-text",[n._v("最新价")]),e("v-uni-text",[n._v("涨跌幅")])],1),n.WebSocketData.length>0?e("v-uni-view",{staticClass:"quotation-list"},n._l(n.WebSocketData,function(t,i){return e("v-uni-view",{key:i,staticClass:"quotation-item",on:{click:function(t){t=n.$handleEvent(t),n.goCharts(i)}}},[e("v-uni-text",[n._v(n._s(t.coin)),e("v-uni-text",[n._v("/"+n._s(t.unit))])],1),e("v-uni-text",[n._v(n._s(t.usdtPrice))]),e("v-uni-text",{class:0==t.isout?"down":"up"},[n._v(n._s(0==t.isout?"-":"+")+n._s(n._f("tofixed4")(100*t.scale))+"%")])],1)}),1):e("v-uni-view",{staticClass:"state-none"},[e("v-uni-view",{staticClass:"h60"}),e("v-uni-image",{attrs:{src:"../../static/images/none.png",mode:""}}),e("v-uni-text",[n._v("暂无数据")])],1)],1),e("v-uni-view",{staticClass:"h40"})],1)},a=[];e.d(t,"a",function(){return i}),e.d(t,"b",function(){return a})},6875:function(n,t,e){"use strict";e.r(t);var i=e("0fa9"),a=e("a24e");for(var o in a)"default"!==o&&function(n){e.d(t,n,function(){return a[n]})}(o);e("daaa");var s=e("2877"),l=Object(s["a"])(a["default"],i["a"],i["b"],!1,null,"2c7e7160",null);t["default"]=l.exports},a24e:function(n,t,e){"use strict";e.r(t);var i=e("a3d0"),a=e.n(i);for(var o in i)"default"!==o&&function(n){e.d(t,n,function(){return i[n]})}(o);t["default"]=a.a},a3d0:function(n,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var i={data:function(){return{topStatus:!1,topPhone:"",banner:[],indicatorDots:!0,interval:5e3,duration:500,color:"rgba(255,255,255,.7)",colorActive:"#fff",newsList:[],autoplay2:!0,interval2:2500,duration2:500,vertical2:!0,circular2:!0,banner2:[],timer:"",timer2:"",WebSocketData:[],tokenStatus:!1,socketTask:"",loginStatus:!0}},filters:{tofixed4:function(n){return n.toFixed(2)}},onLoad:function(){},onHide:function(){console.log("onHide"),this.socketTask.close(),this.socketTask.onClose(function(){console.log("关闭当前页websocket")}),clearInterval(this.timer),this.timer=null,clearTimeout(this.timer2),this.timer2=null},onUnload:function(){console.log("onUnload"),this.socketTask.close(),clearInterval(this.timer),this.timer=null,clearTimeout(this.timer2),this.timer2=null},onShow:function(){var n=this;123!==this.$userId()&&this.$userId()||(this.loginStatus=!1),this.$loading(),setTimeout(function(){n.$hideLoading()},1200);this.$ajax("/account/getMyInfos",{token:this.$userId()},function(t){if(t.data.status){n.userinfo=t.data.data;var e=t.data.data.phone;n.topPhone=e.substring(0,3)+"*****"+e.substring(e.length-3,e.length),n.tokenStatus=!0,setTimeout(function(){n.timers1()},500)}else clearInterval(n.timer),n.timer=null,setTimeout(function(){n.timers1()},500)}),this.timer2=setInterval(function(){n.socketTask.close({success:function(){clearInterval(n.timer),n.timer=null,console.log("每隔十分钟断开连接"),n.timers1()}})},3e5),this.$ajax("/home/queryBanner",{position:1},function(t){t.data.status&&(n.banner=t.data.data)}),this.$ajax("/home/queryBanner",{position:2},function(t){t.data.status&&(n.banner2=t.data.data)}),this.$ajax("/home/queryNotice",{page:1},function(t){t.data.status&&(n.newsList=t.data.data)})},methods:{goPage:function(n){0===n?uni.switchTab({url:"../contract/contract"}):1===n&&uni.navigateTo({url:"../user/share"})},timers1:function(){var n=this;this.socketTask=uni.connectSocket({url:"ws://"+this.webSocketUrl+"/websocket/"+this.$userId(),method:"GET",complete:function(){}}),n.socketTask.onMessage(function(t){n.WebSocketData=JSON.parse(t.data)}),n.socketTask.onOpen(function(){console.log("WebSocket连接已打开！"),n.timer=setInterval(function(){n.socketTask.send({data:n.$userId()+"_home",success:function(){n.socketTask.onMessage(function(t){n.WebSocketData=JSON.parse(t.data)})}})},1500)})},navigateToHY:function(){uni.switchTab({url:"/pages/contract/contract"})},gotoHotnews:function(n){uni.navigateTo({url:"notice_detail?id="+n})},moreNotice:function(){uni.navigateTo({url:"notice"})},goCharts:function(n){uni.navigateTo({url:"../contract/web_charts?&coin="+this.WebSocketData[n].coin})},goUserCenter:function(){uni.navigateTo({url:"../user/userCenter"})},goLogin:function(){uni.navigateTo({url:"../login/login"})}},onPageScroll:function(n){n.scrollTop>100?this.topStatus=!0:this.topStatus=!1}};t.default=i},d12c:function(n,t,e){t=n.exports=e("2350")(!1),t.push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.seat[data-v-2c7e7160]{height:%?88?%;\r\n}.top-head[data-v-2c7e7160]{width:100%;height:%?88?%;background:#fff;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;position:fixed;left:0;top:0;z-index:100;\r\n}.top-head-left[data-v-2c7e7160]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;padding-left:%?30?%}.top-head-left uni-image[data-v-2c7e7160]{width:%?48?%;height:%?48?%;margin-right:%?10?%}.top-head-left uni-text[data-v-2c7e7160]{font-size:%?34?%}.top-head-right[data-v-2c7e7160]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;padding-right:%?30?%}.top-head-right uni-image[data-v-2c7e7160]{width:%?32?%;height:%?32?%;margin-right:%?16?%}.top-head-right uni-text[data-v-2c7e7160]{font-size:%?30?%}.showBorder[data-v-2c7e7160]{border-bottom:1px solid #efefef}.banner-head[data-v-2c7e7160]{padding:%?20?%}.banner-head .swiper[data-v-2c7e7160]{height:%?340?%}.banner-head .swiper uni-image[data-v-2c7e7160]{width:100%;height:%?340?%;border-radius:%?0?%}.hot-news[data-v-2c7e7160]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;padding:0 %?20?% %?10?% %?20?%;height:%?74?%;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;position:relative;background:#fff;margin:0 %?20?%;border-bottom:1px solid #ededed}.hot-news-title[data-v-2c7e7160]{-webkit-box-flex:0;-webkit-flex:0 0 %?36?%;-ms-flex:0 0 %?36?%;flex:0 0 %?36?%}.hot-news-title uni-image[data-v-2c7e7160]{width:%?36?%;height:%?36?%;display:block}.hot-news-box[data-v-2c7e7160]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;height:100%;line-height:%?74?%;margin-left:%?20?%}.hot-news-box uni-swiper[data-v-2c7e7160]{height:100%}.hot-news-box uni-swiper uni-text[data-v-2c7e7160]{display:block;-o-text-overflow:ellipsis;text-overflow:ellipsis;overflow:hidden;white-space:nowrap;font-size:%?28?%;margin-right:%?90?%}.news-more[data-v-2c7e7160]{color:#26c4b3;height:100%;font-size:%?26?%;position:absolute;right:0;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%);padding-left:%?24?%;padding-right:%?20?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}\r\n/* .news-more:after{content: '';width: 1px;height: 32upx;background: #8A9FB0;position: absolute;left: 0;top: 50%;transform: translateY(-50%);} */.news-more uni-image[data-v-2c7e7160]{width:%?36?%;height:%?36?%;display:block}.wave[data-v-2c7e7160]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;padding:%?20?%}.wave-item[data-v-2c7e7160]{background:#fff;width:%?230?%;height:%?222?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;margin-right:%?10?%}.wave-item[data-v-2c7e7160]:last-child{margin-right:0}.wave-item uni-text[data-v-2c7e7160]:first-child{font-size:%?26?%}.wave-item uni-text[data-v-2c7e7160]:nth-child(2){font-size:%?32?%;margin:%?16?% 0}.wave-item uni-text[data-v-2c7e7160]:nth-child(3){font-size:%?24?%;margin-bottom:%?10?%}.wave-item uni-text[data-v-2c7e7160]:nth-child(4){font-size:%?24?%;color:#778aa2}.up[data-v-2c7e7160]{color:#1cc696}.down[data-v-2c7e7160]{color:#ee534f}.ad[data-v-2c7e7160]{padding:%?20?% %?30?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-flex-wrap:wrap;-ms-flex-wrap:wrap;flex-wrap:wrap;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;margin-bottom:%?-20?%}.ad uni-image[data-v-2c7e7160]{width:%?336?%;height:%?168?%;margin-bottom:%?20?%}.quotation[data-v-2c7e7160]{padding:0 %?20?%}.quotation-title[data-v-2c7e7160]{font-size:%?32?%;height:%?88?%;line-height:%?88?%;background:#fff;border-bottom:1px solid #ededed;padding-left:%?24?%}.quotation-head[data-v-2c7e7160]{background:#fff;height:%?70?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;padding:0 %?30?%}.quotation-head uni-text[data-v-2c7e7160]{font-size:%?26?%;color:#888;-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1}.quotation-head uni-text[data-v-2c7e7160]:first-child{-webkit-box-flex:0;-webkit-flex:0 0 29%;-ms-flex:0 0 29%;flex:0 0 29%}.quotation-head uni-text[data-v-2c7e7160]:nth-child(2){text-align:left}.quotation-head uni-text[data-v-2c7e7160]:nth-child(3){text-align:right}.quotation-item[data-v-2c7e7160]{height:%?120?%;background:#fff;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;padding:0 %?30?%;border-bottom:1px solid #ededed}.quotation-item>uni-text[data-v-2c7e7160]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;font-size:%?30?%}.quotation-item>uni-text[data-v-2c7e7160]:first-child{-webkit-box-flex:0;-webkit-flex:0 0 29%;-ms-flex:0 0 29%;flex:0 0 29%}.quotation-item>uni-text uni-text[data-v-2c7e7160]{color:#999;font-size:%?22?%}.quotation-item>uni-text[data-v-2c7e7160]:last-child{-webkit-box-flex:0;-webkit-flex:none;-ms-flex:none;flex:none;width:%?170?%;height:%?66?%;border-radius:%?4?%;text-align:center;background:#ccc;line-height:%?66?%;color:#fff}.quotation-item uni-text.up[data-v-2c7e7160]{background:#1cc696}.quotation-item uni-text.down[data-v-2c7e7160]{background:#ee534f}.quotation-item[data-v-2c7e7160]:last-of-type{border-bottom:0}",""])},daaa:function(n,t,e){"use strict";var i=e("eb12"),a=e.n(i);a.a},eb12:function(n,t,e){var i=e("d12c");"string"===typeof i&&(i=[[n.i,i,""]]),i.locals&&(n.exports=i.locals);var a=e("4f06").default;a("0ec1505a",i,!0,{sourceMap:!1,shadowMode:!1})}}]);