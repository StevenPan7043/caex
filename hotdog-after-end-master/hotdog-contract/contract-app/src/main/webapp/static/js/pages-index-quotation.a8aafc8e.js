(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-index-quotation"],{4363:function(n,t,i){"use strict";var e=function(){var n=this,t=n.$createElement,i=n._self._c||t;return i("v-uni-view",[i("v-uni-view",{staticClass:"seat"}),i("v-uni-view",{staticClass:"quotation-head"},n._l(n.navs,function(t,e){return i("v-uni-view",{key:e},[i("v-uni-text",[n._v(n._s(t))])],1)}),1),n.WebSocketData.length>0?i("v-uni-view",{staticClass:"quotation-box"},n._l(n.WebSocketData,function(t,e){return i("v-uni-view",{key:e,staticClass:"quotation-list",on:{click:function(t){t=n.$handleEvent(t),n.goCharts(e)}}},[i("v-uni-view",{staticClass:"quotation-item"},[i("v-uni-text",[n._v(n._s(t.coin)),i("v-uni-text",[n._v("/"+n._s(t.unit))])],1)],1),i("v-uni-view",{staticClass:"quotation-item"},[i("v-uni-text",[n._v(n._s(t.usdtPrice))]),i("v-uni-text",[n._v("￥"+n._s(t.cny))])],1),i("v-uni-view",{staticClass:"quotation-item"},[i("v-uni-text",{class:0==t.isout?"down":"up"},[n._v(n._s(0==t.isout?"-":"+")+n._s(n._f("tofixed4")(100*t.scale))+"%")])],1)],1)}),1):i("v-uni-view",{staticClass:"state-none"},[i("v-uni-view",{staticClass:"h100"}),i("v-uni-image",{attrs:{src:"../../static/images/none.png",mode:""}}),i("v-uni-text",[n._v("数据加载中...")])],1)],1)},a=[];i.d(t,"a",function(){return e}),i.d(t,"b",function(){return a})},"50df":function(n,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var e={data:function(){return{curID:0,navs:["名称","最新价","24H涨跌幅"],num:0,WebSocketData:[],socketTask:"",timer:"",timer2:""}},filters:{tofixed4:function(n){return n.toFixed(2)}},onLoad:function(){},onHide:function(){this.socketTask.close(),clearInterval(this.timer),this.timer=null,clearTimeout(this.timer2),this.timer2=null},onShow:function(){var n=this;uni.showLoading({title:"loading..",mask:!0}),setTimeout(function(){uni.hideLoading()},1e3);this.$ajax("/account/getMyInfos",{token:this.$userId()},function(t){t.data.status?(n.tokenStatus=!0,setTimeout(function(){n.timers1()},500)):(clearInterval(n.timer),n.timer=null,n.$token(t))}),this.timer2=setTimeout(function(){n.socketTask.close({success:function(){clearInterval(n.timer),n.timer=null,console.log("每隔十分钟断开连接"),n.timers1()}})},3e5)},methods:{timers1:function(){var n=this;this.socketTask=uni.connectSocket({url:"ws://"+this.webSocketUrl+"/websocket/"+this.$userId(),method:"GET",complete:function(){}}),n.socketTask.onMessage(function(t){n.WebSocketData=JSON.parse(t.data)}),n.socketTask.onOpen(function(){console.log("WebSocket连接已打开！"),n.timer=setInterval(function(){n.socketTask.send({data:n.$userId()+"_home",success:function(){n.socketTask.onMessage(function(t){n.WebSocketData=JSON.parse(t.data)})}})},1500)})},goCharts:function(n){uni.navigateTo({url:"../contract/web_charts?&coin="+this.WebSocketData[n].coin})}}};t.default=e},"683f":function(n,t,i){"use strict";i.r(t);var e=i("4363"),a=i("ef11");for(var o in a)"default"!==o&&function(n){i.d(t,n,function(){return a[n]})}(o);i("def5");var s=i("2877"),u=Object(s["a"])(a["default"],e["a"],e["b"],!1,null,"5a3861d6",null);t["default"]=u.exports},9158:function(n,t,i){t=n.exports=i("2350")(!1),t.push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.seat[data-v-5a3861d6]{height:%?92?%;width:100%}.quotation-head[data-v-5a3861d6]{height:%?90?%;background:#141f30;border-top:1px solid #081724;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;padding:0 %?30?%;-webkit-box-sizing:border-box;box-sizing:border-box;width:100%;position:fixed;left:0;top:0;z-index:100}.quotation-head uni-view[data-v-5a3861d6]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;text-align:left;line-height:%?90?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex}\r\n/* .quotation-head view:first-child{text-align: left;} */.quotation-head uni-view[data-v-5a3861d6]:nth-child(2){padding-left:%?10?%}.quotation-head uni-view[data-v-5a3861d6]:last-child{text-align:right;-webkit-box-flex:0;-webkit-flex:0 0 25%;-ms-flex:0 0 25%;flex:0 0 25%}\r\n/*排序图标base64*/.quotation-head uni-text[data-v-5a3861d6]{color:#778aa2;font-size:%?26?%}\r\n/* .quotation-head text{color: #778AA2;font-size: 26upx;background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYBAMAAAASWSDLAAAAIVBMVEVHcEx2iqJ3iaR+jqh3iqJ3i6N3iqJ1jKF3iqJ3iqJ3iqJilGztAAAACnRSTlMAQS0R0mrsILahIk0fOQAAAGNJREFUGNNjYGBgMHFgYGgxYICAWYsYGFYJQNisq5A4Ukgc9iwvBIdlmRaCM2uhFYKTFYDEEWVA4jDQhMMYgMSRWo7giGc1IDgiywwQHK+FSAasCkDirEQ22hnKYQEyWgqADABvmC8618hGNAAAAABJRU5ErkJggg==) no-repeat right;background-size: 24upx;padding-right: 28upx;} */.quotation-head uni-text.up[data-v-5a3861d6]{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAaCAMAAACaYWzBAAAARVBMVEVHcEx2i6L/qgD/qgB3iqN1iqP/qgD/qgD/qgB1iqL/qgB3i6J3iqN4iqP/tgD/qwD/qgD/qwB3iqN3i6P/qgD/qgB3iqICye/xAAAAFXRSTlMAMx5C5hJaLRE/0YtqUQfrrHe8pZlhkIK9AAAAdElEQVQoz9WSSwrAIBBDp9WqtT8tzdz/qP0uVHKBZhPIgwkDEXnl3GPLLLUc0N+u2lW53wD/grHMQwQHCRxkIDNggCMwsCGuFKTYS+Dl8mcwWDtQsCsHk+rCTl35RDtUd15eFtSgXsP6rcRqux9jHuua/4hOcQIMUCQ9qj4AAAAASUVORK5CYII=);color:#fa0}.quotation-head uni-text.down[data-v-5a3861d6]{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAaCAMAAACaYWzBAAAAP1BMVEVHcEx3iqJ4i6N3iqKfjnj/qgD/qgB3iaT/qgCKkJ13i6L/qgB3iqJ3i6P/qgD/rwD/qgD/rQD/qwD/qgB3iqJLrsY6AAAAE3RSTlMA4VuEBcjnLj8R9PyiuagZLSJ/kqZcHQAAAHhJREFUKM/N0t0KgDAIBeD9ubmtWqXv/6xRRLBx6jpvBD/Qc6Ex3xWmOZx9odzDpJrOXoW7udMbpIdUMGSrFsKsJSHwqi4jKOoNBOcDhuv+/4EZA9faELQoFBCQxA2tIqkMj4vsOJUsL3Hj8CbWXgOK6/hYd8In6AHHrQr+rP/mxQAAAABJRU5ErkJggg==);color:#fa0}.quotation-list[data-v-5a3861d6]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;padding:0 %?30?%}.quotation-item[data-v-5a3861d6]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;height:%?144?%;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;border-bottom:1px solid #22324b}.quotation-item>uni-text[data-v-5a3861d6]:first-child{font-size:%?32?%;color:#d1d5eb}.quotation-item>uni-text:first-child uni-text[data-v-5a3861d6]{font-size:%?22?%;color:#778aa2}.quotation-item>uni-text[data-v-5a3861d6]:nth-child(2){color:#778aa2;font-size:%?22?%;margin-top:%?6?%}.quotation-item[data-v-5a3861d6]:nth-child(2){padding-left:%?10?%}.quotation-item[data-v-5a3861d6]:last-child{text-align:right;-webkit-box-flex:0;-webkit-flex:0 0 25%;-ms-flex:0 0 25%;flex:0 0 25%}.quotation-item:last-child uni-text[data-v-5a3861d6]{width:%?170?%;height:%?66?%;border-radius:%?4?%;text-align:center;background:#ccc;line-height:%?66?%}.quotation-item:last-child uni-text.up[data-v-5a3861d6]{background:#02ad8f}.quotation-item:last-child uni-text.down[data-v-5a3861d6]{background:#d14b64}",""])},c708:function(n,t,i){var e=i("9158");"string"===typeof e&&(e=[[n.i,e,""]]),e.locals&&(n.exports=e.locals);var a=i("4f06").default;a("75400734",e,!0,{sourceMap:!1,shadowMode:!1})},def5:function(n,t,i){"use strict";var e=i("c708"),a=i.n(e);a.a},ef11:function(n,t,i){"use strict";i.r(t);var e=i("50df"),a=i.n(e);for(var o in e)"default"!==o&&function(n){i.d(t,n,function(){return e[n]})}(o);t["default"]=a.a}}]);