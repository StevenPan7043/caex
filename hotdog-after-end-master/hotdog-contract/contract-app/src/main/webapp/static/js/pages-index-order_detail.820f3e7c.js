(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-index-order_detail"],{"12b8":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var a={data:function(){return{array:["中国","美国","巴西"],index:0,appealStatus:!1,qrcodeStatus:!1}},onLoad:function(){},methods:{bindPickerChange:function(t){console.log("picker发送选择改变，携带值为",t.target.value),this.index=t.target.value}}};e.default=a},"4dcb":function(t,e,i){"use strict";var a=i("cc85"),n=i.n(a);n.a},"53d1":function(t,e,i){e=t.exports=i("2350")(!1),e.push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.qrcode-popup[data-v-72b1d762]{width:%?570?%;height:%?638?%;background:#141f30;position:fixed;left:50%;top:44%;-webkit-transform:translate(-50%,-50%);-ms-transform:translate(-50%,-50%);transform:translate(-50%,-50%);z-index:1005;-webkit-box-sizing:border-box;box-sizing:border-box;padding:%?60?% 0;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.qrcode-popup .qrcode-img[data-v-72b1d762]{width:%?400?%;height:%?400?%;padding:%?12?%;background:#fff;margin-bottom:%?30?%}.qrcode-popup uni-text[data-v-72b1d762]{width:%?422?%;background:#22324b;text-align:center;line-height:%?74?%;line-height:%?74?%}.qrcode-popup .qrcode-close[data-v-72b1d762]{width:%?64?%;height:%?124?%;position:absolute;left:50%;bottom:%?-124?%;-webkit-transform:translateX(-50%);-ms-transform:translateX(-50%);transform:translateX(-50%)}\r\n/*  */.h20[data-v-72b1d762],.h40[data-v-72b1d762]{background:none}.appeal-popup[data-v-72b1d762]{width:%?610?%;background:#141f30;position:fixed;left:50%;top:50%;-webkit-transform:translate(-50%,-50%);-ms-transform:translate(-50%,-50%);transform:translate(-50%,-50%);z-index:1001}.appeal-title[data-v-72b1d762]{text-align:center;font-size:%?32?%;color:#d1d5eb;border-bottom:1px solid #22324b;height:%?100?%;line-height:%?100?%}.appeal-tips[data-v-72b1d762]{font-size:%?24?%;color:#778aa2;padding:0 %?30?%}.appeal-cause[data-v-72b1d762]{padding:0 %?30?%;margin-bottom:%?40?%}.appeal-sub-title[data-v-72b1d762]{color:#778aa2;margin-bottom:%?10?%}.appeal-box[data-v-72b1d762]{border:1px solid #778aa2;position:relative}.appeal-box uni-picker[data-v-72b1d762]{line-height:%?90?%}.appeal-picker-text[data-v-72b1d762]{height:%?90?%;padding:0 %?80?% 0 %?20?%}.appeal-box uni-textarea[data-v-72b1d762]{height:%?200?%;width:100%;-webkit-box-sizing:border-box;box-sizing:border-box;padding:%?20?%}.appeal-box .more[data-v-72b1d762]{width:%?40?%;height:%?40?%;position:absolute;right:%?18?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.appeal-btn[data-v-72b1d762]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;height:%?100?%;line-height:%?100?%;border-top:1px solid #22324b}.appeal-btn uni-text[data-v-72b1d762]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;text-align:center;border-right:1px solid #22324b;color:#778aa2}.appeal-btn uni-text[data-v-72b1d762]:last-child{border-right:0}.appeal-btn uni-text.cur[data-v-72b1d762]{color:#fa0}.appeal-btn uni-text[data-v-72b1d762]:active{background:hsla(0,0%,100%,.05)}\r\n/*  */.order-head[data-v-72b1d762]{height:%?228?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;position:relative;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;padding-left:%?30?%;background:#141f30}.order-head>uni-text[data-v-72b1d762]:first-of-type{font-size:%?48?%;color:#fa0;margin-bottom:%?16?%}.order-head uni-text.time[data-v-72b1d762]{color:#fa0}.order-head uni-image[data-v-72b1d762]{width:%?128?%;height:%?128?%;position:absolute;right:%?60?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.order-info[data-v-72b1d762]{min-height:%?216?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;padding:0 %?30?%;background:#141f30;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;line-height:1.6}.order-info-price[data-v-72b1d762]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.order-info-price uni-text[data-v-72b1d762]{font-size:%?40?%;color:#fa0}.order-info-price uni-image[data-v-72b1d762]{width:%?32?%;height:%?32?%;padding:%?10?%}.order-info-item[data-v-72b1d762]{color:#778aa2}.order-info-item uni-text[data-v-72b1d762]{color:#d1d5eb}.copy[data-v-72b1d762]:active{opacity:.8}.order-paytype[data-v-72b1d762]{background:#141f30}.order-paytype-item[data-v-72b1d762]{height:%?100?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;padding:0 %?30?%;border-bottom:1px solid #22324b}.order-paytype-left[data-v-72b1d762]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.order-paytype-left uni-text[data-v-72b1d762]{color:#778aa2}.order-paytype-left uni-image[data-v-72b1d762]{width:%?48?%;height:%?48?%;margin-right:%?20?%}.order-paytype-right[data-v-72b1d762]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.order-paytype-right uni-text[data-v-72b1d762]{color:#d1d5eb}.order-paytype-right uni-image[data-v-72b1d762]{width:%?32?%;height:%?32?%;padding:%?10?% 0 %?10?% %?10?%}.qrcode uni-image[data-v-72b1d762]{width:%?48?%;height:%?48?%}.order-paytype-item.title .order-paytype-left uni-text[data-v-72b1d762]{color:#d1d5eb}.order-paytype-item.title uni-image[data-v-72b1d762]{width:%?36?%;height:%?36?%}.order-paytype-item uni-image[data-v-72b1d762]:active{opacity:.5}.order-tips[data-v-72b1d762]{padding:%?26?% %?30?%}.order-tips uni-text[data-v-72b1d762]{font-size:%?24?%;color:#fa0}.order-btn[data-v-72b1d762]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;height:%?90?%;line-height:%?90?%;text-align:center;padding:0 %?30?% %?20?% %?30?%}.order-btn uni-text[data-v-72b1d762]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;margin-right:%?14?%;color:#d1d5eb;background:#223349;border-radius:%?4?%}.order-btn uni-text[data-v-72b1d762]:last-of-type{margin-right:0}.order-btn uni-text.orange[data-v-72b1d762]{background:#fa0;color:#081724}.order-btn uni-text[data-v-72b1d762]:active{opacity:.8}",""])},"5bc3":function(t,e,i){"use strict";var a=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("v-uni-view",[i("v-uni-view",{staticClass:"order-head"},[i("v-uni-text",[t._v("待付款")]),i("v-uni-text",[t._v("请于"),i("v-uni-text",{staticClass:"time"},[t._v("14分15秒")]),t._v("内向买家付款")],1),i("v-uni-image",{attrs:{src:"../../static/images/order_icon.png",mode:""}})],1),i("v-uni-view",{staticClass:"h20"}),i("v-uni-view",{staticClass:"order-info"},[i("v-uni-view",{staticClass:"order-info-price"},[i("v-uni-text",[t._v("￥2,000.00")]),i("v-uni-image",{staticClass:"copy",attrs:{src:"/static/images/text_info.png",mode:""}})],1),i("v-uni-view",{staticClass:"order-info-item"},[t._v("单价"),i("v-uni-text",[t._v("￥6.97")])],1),i("v-uni-view",{staticClass:"order-info-item"},[t._v("数量"),i("v-uni-text",[t._v("30000.000 USDT")])],1)],1),i("v-uni-view",{staticClass:"h20"}),i("v-uni-view",{staticClass:"order-paytype"},[i("v-uni-view",{staticClass:"order-paytype-item title"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-image",{attrs:{src:"/static/images/pay_icon33.png",mode:""}}),i("v-uni-text",[t._v("银行卡")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-image",{attrs:{src:"/static/images/right_more.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("收款人")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("牛奶奶")]),i("v-uni-image",{attrs:{src:"/static/images/text_info.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("收款二维码")])],1),i("v-uni-view",{staticClass:"order-paytype-right qrcode"},[i("v-uni-image",{attrs:{src:"../../static/images/Qrcode.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("银行账户")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("622246511231315128")]),i("v-uni-image",{attrs:{src:"/static/images/text_info.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("请用本人银行账号向以上账号自行转账")])],1)],1)],1),i("v-uni-view",{staticClass:"h20"}),i("v-uni-view",{staticClass:"order-paytype"},[i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("卖家昵称")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("大头王")]),i("v-uni-image",{attrs:{src:"/static/images/text_info.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("订单号")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("5341312235")]),i("v-uni-image",{attrs:{src:"/static/images/text_info.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"order-paytype-item"},[i("v-uni-view",{staticClass:"order-paytype-left"},[i("v-uni-text",[t._v("下单时间")])],1),i("v-uni-view",{staticClass:"order-paytype-right"},[i("v-uni-text",[t._v("2019-06-15 14:12:01")])],1)],1)],1),i("v-uni-view",{staticClass:"h20"}),i("v-uni-view",{staticClass:"order-tips"},[i("v-uni-text",[t._v("*如果您已向卖方转账付款，请务必点击右下角“我已付款”按钮，否则可能造成资金损失！")])],1),i("v-uni-view",{staticClass:"order-btn"},[i("v-uni-text",[t._v("联系客服")]),i("v-uni-text",[t._v("取消订单")]),i("v-uni-text",{staticClass:"orange"},[t._v("我已付款")])],1),t.appealStatus?i("v-uni-view",{staticClass:"popup-bg"}):t._e(),t.appealStatus?i("v-uni-view",{staticClass:"appeal-popup"},[i("v-uni-view",{staticClass:"appeal-title"},[t._v("申诉")]),i("v-uni-view",{staticClass:"h20"}),i("v-uni-view",{staticClass:"appeal-tips"},[t._v("提交申诉后资产将会被冻结，申诉专员将介入本次交易，直至申诉结束。恶意申诉这将被冻结账户。")]),i("v-uni-view",{staticClass:"h40"}),i("v-uni-view",{staticClass:"appeal-cause"},[i("v-uni-view",{staticClass:"appeal-sub-title"},[t._v("申诉类型")]),i("v-uni-view",{staticClass:"appeal-box"},[i("v-uni-picker",{attrs:{value:t.index,range:t.array},on:{change:function(e){e=t.$handleEvent(e),t.bindPickerChange(e)}}},[i("v-uni-view",{staticClass:"appeal-picker-text"},[t._v(t._s(t.array[t.index]))])],1),i("v-uni-image",{staticClass:"more",attrs:{src:"/static/images/more_picker.png",mode:""}})],1)],1),i("v-uni-view",{staticClass:"appeal-cause"},[i("v-uni-view",{staticClass:"appeal-sub-title"},[t._v("申诉内容")]),i("v-uni-view",{staticClass:"appeal-box"},[i("v-uni-textarea",{attrs:{value:"",placeholder:"请输入申诉内容"}})],1)],1),i("v-uni-view",{staticClass:"appeal-btn"},[i("v-uni-text",[t._v("取消")]),i("v-uni-text",{staticClass:"cur"},[t._v("确认")])],1)],1):t._e(),t.qrcodeStatus?i("v-uni-view",{staticClass:"popup-bg"}):t._e(),t.qrcodeStatus?i("v-uni-view",{staticClass:"qrcode-popup"},[i("v-uni-image",{staticClass:"qrcode-img",attrs:{src:"../../static/images/code.png",mode:""}}),i("v-uni-text",[t._v("保存二维码")]),i("v-uni-image",{staticClass:"qrcode-close",attrs:{src:"../../static/images/qrcode_close.png",mode:""}})],1):t._e()],1)},n=[];i.d(e,"a",function(){return a}),i.d(e,"b",function(){return n})},a5dc:function(t,e,i){"use strict";i.r(e);var a=i("5bc3"),n=i("d1f1");for(var r in n)"default"!==r&&function(t){i.d(e,t,function(){return n[t]})}(r);i("4dcb");var s=i("2877"),o=Object(s["a"])(n["default"],a["a"],a["b"],!1,null,"72b1d762",null);e["default"]=o.exports},cc85:function(t,e,i){var a=i("53d1");"string"===typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);var n=i("4f06").default;n("0009ac51",a,!0,{sourceMap:!1,shadowMode:!1})},d1f1:function(t,e,i){"use strict";i.r(e);var a=i("12b8"),n=i.n(a);for(var r in a)"default"!==r&&function(t){i.d(e,t,function(){return a[t]})}(r);e["default"]=n.a}}]);