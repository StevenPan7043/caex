(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-user-recharge"],{"007f":function(t,n,a){"use strict";a.r(n);var e=a("7e58"),i=a("a78f");for(var c in i)"default"!==c&&function(t){a.d(n,t,function(){return i[t]})}(c);a("e6a5");var o=a("2877"),r=Object(o["a"])(i["default"],e["a"],e["b"],!1,null,"084ccac3",null);n["default"]=r.exports},"00c7":function(t,n,a){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var e={data:function(){return{info:"",sysinfo:""}},onLoad:function(){var t=this;uni.showLoading({title:"",mask:!0}),this.$ajax("/account/getWallet",{token:this.$userId(),coin:"USDT"},function(n){uni.hideLoading(),n.data.status?t.info=n.data.data:t.$token(n)})},onShow:function(){var t=this;this.$ajax("/utils/getSysInfo",{token:this.$userId()},function(n){n.data.status?t.sysinfo=n.data.data:t.$token(n)})},methods:{copy:function(t){var n=this,a=this.info.addr;uni.setClipboardData({data:a,success:function(){n.$alert("复制成功","success",1200)}})},saveCode:function(){var t=this;uni.showLoading({title:"",mask:!0}),uni.downloadFile({url:this.info.qrurl,success:function(n){if(200===n.statusCode){var a=n.tempFilePath;uni.saveImageToPhotosAlbum({filePath:a,success:function(){uni.hideLoading(),t.$alert("保存成功","success",1200)},fail:function(n){uni.hideLoading(),t.$alert("保存失败","",1200)}})}}})}}};n.default=e},"0151":function(t,n,a){n=t.exports=a("2350")(!1),n.push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.charge-code[data-v-084ccac3]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.charge-code uni-image[data-v-084ccac3]{width:%?286?%;height:%?286?%;padding:%?16?%;background:#fff;margin-bottom:%?30?%}.charge-code uni-text[data-v-084ccac3]{width:%?260?%;height:%?66?%;text-align:center;line-height:%?66?%;color:#fff;border-radius:%?8?%;background:#22324b}.charge-code uni-text[data-v-084ccac3]:active{opacity:.8;color:#fff}.wrap[data-v-084ccac3]{padding:0 %?30?%}.charge-info[data-v-084ccac3]{padding:%?42?% %?26?%;height:%?356?%;-webkit-box-sizing:border-box;box-sizing:border-box;background:#202639;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.charge-info-title[data-v-084ccac3]{color:#6c819e;font-size:%?26?%}.charge-info-text[data-v-084ccac3]{height:%?90?%;line-height:%?90?%;width:100%;border:1px solid #2e3a4c;color:#d9ebfa;font-size:%?24?%;text-align:center}.charge-info-btn[data-v-084ccac3]{width:%?260?%;height:%?66?%;text-align:center;line-height:%?66?%;border-radius:%?8?%;background:#22324b;color:#fff}.charge-info-btn[data-v-084ccac3]:active{opacity:.8;color:#fff}.charge-tips[data-v-084ccac3]{overflow:hidden}.charge-tips-title[data-v-084ccac3]{font-size:%?30?%;color:#6c819e;margin-bottom:%?16?%}.charge-tips-content[data-v-084ccac3]{color:#6c819e}.red[data-v-084ccac3]{color:#f63f53!important}.draw[data-v-084ccac3]{padding:0 %?30?%}.draw-item[data-v-084ccac3]{margin-bottom:%?30?%}.draw-item[data-v-084ccac3]:last-child{margin-bottom:0}.draw-title[data-v-084ccac3]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;margin-bottom:%?24?%;position:relative}.draw-title uni-image[data-v-084ccac3]{width:%?34?%;height:%?34?%;margin-right:%?14?%}.draw-title uni-text[data-v-084ccac3]{font-size:%?28?%;color:#d9ebfa}.draw-title-right[data-v-084ccac3]{font-size:%?24?%;color:#52627a;position:absolute;right:0;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-title-right uni-text[data-v-084ccac3]{color:#2bcbb9;font-size:%?24?%}.draw-box[data-v-084ccac3]{height:%?100?%;padding:0 %?20?%;background:#202639;position:relative;font-size:%?32?%}.draw-box uni-input[data-v-084ccac3]{height:100%;color:#d9ebfa}\r\n/* .draw-box picker{height: 100%;} */.form-picker[data-v-084ccac3]{height:100%;line-height:%?100?%}\r\n/* .draw-box picker text{color: #52627A;font-size: 26upx;position: absolute;right: 60upx;top: 50%;transform: translateY(-50%);} */.draw-box uni-text.rightText[data-v-084ccac3]{color:#52627a;font-size:%?26?%;position:absolute;right:%?60?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-box-text[data-v-084ccac3]{line-height:%?100?%;font-size:%?32?%;color:#d9ebfa}.draw-box uni-text[data-v-084ccac3]{color:#d9ebfa;position:absolute;right:%?24?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-count[data-v-084ccac3]{font-size:%?26?%;color:#d9ebfa}.draw-count uni-text[data-v-084ccac3]{margin:0 %?6?%}",""])},"6a11":function(t,n,a){var e=a("0151");"string"===typeof e&&(e=[[t.i,e,""]]),e.locals&&(t.exports=e.locals);var i=a("4f06").default;i("84d13020",e,!0,{sourceMap:!1,shadowMode:!1})},"7e58":function(t,n,a){"use strict";var e=function(){var t=this,n=t.$createElement,a=t._self._c||n;return a("v-uni-view",{staticClass:"wrap"},[a("v-uni-view",{staticClass:"h60"}),a("v-uni-view",{staticClass:"charge-code"},[a("v-uni-image",{attrs:{src:t.info.qrurl}})],1),a("v-uni-view",{staticClass:"h60"}),a("v-uni-view",{staticClass:"charge-info"},[a("v-uni-view",{staticClass:"charge-info-title"},[t._v("充币地址")]),a("v-uni-text",{staticClass:"charge-info-text",attrs:{selectable:!0}},[t._v(t._s(t.info.addr||"请稍等..."))])],1),a("v-uni-view",{staticClass:"h60"}),a("v-uni-view",{staticClass:"charge-tips"},[a("v-uni-view",{staticClass:"charge-tips-title"},[t._v("充币说明")]),a("v-uni-view",{staticClass:"charge-tips-content"},[t._v("1、请勿向上述地址充值非USDT资产，否则资产将不可找回。"),a("v-uni-view"),t._v("2、您充值至上述地址后，需要整个网络节点的确认，1次网络确认后到账，1次网络确认后可提币。"),a("v-uni-view"),t._v("3、最小充值金额："+t._s(t.sysinfo.usdt_cash_min)+" USDT，小于最小金额的充值将不会上账且无法退回。"),a("v-uni-view"),t._v("4、您的充值地址不会经常改变，可以重复充值；如有更改，我们会尽量通过网站公告或邮件通知您。"),a("v-uni-view"),t._v("5、请务必确认电脑及浏览器安全，防止信息被篡改或泄露。")],1)],1),a("v-uni-view",{staticClass:"h30"})],1)},i=[];a.d(n,"a",function(){return e}),a.d(n,"b",function(){return i})},a78f:function(t,n,a){"use strict";a.r(n);var e=a("00c7"),i=a.n(e);for(var c in e)"default"!==c&&function(t){a.d(n,t,function(){return e[t]})}(c);n["default"]=i.a},e6a5:function(t,n,a){"use strict";var e=a("6a11"),i=a.n(e);i.a}}]);