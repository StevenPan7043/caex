(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-user-recharge"],{"007f":function(t,n,e){"use strict";e.r(n);var i=e("e856"),a=e("a78f");for(var o in a)"default"!==o&&function(t){e.d(n,t,function(){return a[t]})}(o);e("1ca4");var r=e("2877"),c=Object(r["a"])(a["default"],i["a"],i["b"],!1,null,"0d9c318f",null);n["default"]=c.exports},"00c7":function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i={data:function(){return{info:"",sysinfo:""}},onLoad:function(){var t=this;uni.showLoading({title:"",mask:!0}),this.$ajax("/account/getWallet",{token:this.$userId(),coin:"USDT"},function(n){uni.hideLoading(),n.data.status?t.info=n.data.data:t.$token(n)})},onShow:function(){var t=this;this.$ajax("/utils/getSysInfo",{token:this.$userId()},function(n){n.data.status?t.sysinfo=n.data.data:t.$token(n)})},methods:{copy:function(t){var n=this,e=this.info.addr;uni.setClipboardData({data:e,success:function(){n.$alert("复制成功","success",1200)}})},saveCode:function(){var t=this;uni.showLoading({title:"",mask:!0}),uni.downloadFile({url:this.info.qrurl,success:function(n){if(200===n.statusCode){var e=n.tempFilePath;uni.saveImageToPhotosAlbum({filePath:e,success:function(){uni.hideLoading(),t.$alert("保存成功","success",1200)},fail:function(n){uni.hideLoading(),t.$alert("保存失败","",1200)}})}}})}}};n.default=i},"1ca4":function(t,n,e){"use strict";var i=e("216b"),a=e.n(i);a.a},"216b":function(t,n,e){var i=e("a0e5");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var a=e("4f06").default;a("276f6996",i,!0,{sourceMap:!1,shadowMode:!1})},a0e5:function(t,n,e){n=t.exports=e("2350")(!1),n.push([t.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.charge-code[data-v-0d9c318f]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.charge-code uni-image[data-v-0d9c318f]{width:%?286?%;height:%?286?%;padding:%?16?%;background:#fff;margin-bottom:%?30?%}.charge-code uni-text[data-v-0d9c318f]{width:%?260?%;height:%?66?%;text-align:center;line-height:%?66?%;color:#fff;border-radius:%?8?%;background:#22324b}.charge-code uni-text[data-v-0d9c318f]:active{opacity:.8;color:#fff}.wrap[data-v-0d9c318f]{padding:0 %?30?%}.charge-info[data-v-0d9c318f]{padding:%?42?% %?26?%;height:%?356?%;-webkit-box-sizing:border-box;box-sizing:border-box;background:#202639;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.charge-info-title[data-v-0d9c318f]{color:#6c819e;font-size:%?26?%}.charge-info-text[data-v-0d9c318f]{height:%?90?%;line-height:%?90?%;width:100%;border:1px solid #2e3a4c;color:#d9ebfa;font-size:%?24?%;text-align:center}.charge-info-btn[data-v-0d9c318f]{width:%?260?%;height:%?66?%;text-align:center;line-height:%?66?%;border-radius:%?8?%;background:#22324b;color:#fff}.charge-info-btn[data-v-0d9c318f]:active{opacity:.8;color:#fff}.charge-tips[data-v-0d9c318f]{overflow:hidden}.charge-tips-title[data-v-0d9c318f]{font-size:%?30?%;color:#6c819e;margin-bottom:%?16?%}.charge-tips-content[data-v-0d9c318f]{color:#6c819e}.red[data-v-0d9c318f]{color:#f63f53!important}.draw[data-v-0d9c318f]{padding:0 %?30?%}.draw-item[data-v-0d9c318f]{margin-bottom:%?30?%}.draw-item[data-v-0d9c318f]:last-child{margin-bottom:0}.draw-title[data-v-0d9c318f]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;margin-bottom:%?24?%;position:relative}.draw-title uni-image[data-v-0d9c318f]{width:%?34?%;height:%?34?%;margin-right:%?14?%}.draw-title uni-text[data-v-0d9c318f]{font-size:%?28?%;color:#d9ebfa}.draw-title-right[data-v-0d9c318f]{font-size:%?24?%;color:#52627a;position:absolute;right:0;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-title-right uni-text[data-v-0d9c318f]{color:#2bcbb9;font-size:%?24?%}.draw-box[data-v-0d9c318f]{height:%?100?%;padding:0 %?20?%;background:#202639;position:relative;font-size:%?32?%}.draw-box uni-input[data-v-0d9c318f]{height:100%;color:#d9ebfa}\r\n/* .draw-box picker{height: 100%;} */.form-picker[data-v-0d9c318f]{height:100%;line-height:%?100?%}\r\n/* .draw-box picker text{color: #52627A;font-size: 26upx;position: absolute;right: 60upx;top: 50%;transform: translateY(-50%);} */.draw-box uni-text.rightText[data-v-0d9c318f]{color:#52627a;font-size:%?26?%;position:absolute;right:%?60?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-box-text[data-v-0d9c318f]{line-height:%?100?%;font-size:%?32?%;color:#d9ebfa}.draw-box uni-text[data-v-0d9c318f]{color:#d9ebfa;position:absolute;right:%?24?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.draw-count[data-v-0d9c318f]{font-size:%?26?%;color:#d9ebfa}.draw-count uni-text[data-v-0d9c318f]{margin:0 %?6?%}",""])},a78f:function(t,n,e){"use strict";e.r(n);var i=e("00c7"),a=e.n(i);for(var o in i)"default"!==o&&function(t){e.d(n,t,function(){return i[t]})}(o);n["default"]=a.a},e856:function(t,n,e){"use strict";var i=function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("v-uni-view",{staticClass:"wrap"},[e("v-uni-view",{staticClass:"h60"}),e("v-uni-view",{staticClass:"charge-code"},[e("v-uni-image",{attrs:{src:t.info.qrurl}})],1),e("v-uni-view",{staticClass:"h60"}),e("v-uni-view",{staticClass:"charge-info"},[e("v-uni-view",{staticClass:"charge-info-title"},[t._v("充币地址(长按复制)")]),e("v-uni-text",{staticClass:"charge-info-text",attrs:{selectable:!0}},[t._v(t._s(t.info.addr||"请稍等..."))])],1),e("v-uni-view",{staticClass:"h60"}),e("v-uni-view",{staticClass:"charge-tips"},[e("v-uni-view",{staticClass:"charge-tips-title"},[t._v("充币说明")]),e("v-uni-view",{staticClass:"charge-tips-content"},[t._v("1、请勿向上述地址充值非USDT资产，否则资产将不可找回。"),e("v-uni-view"),t._v("2、您充值至上述地址后，需要整个网络节点的确认，1次网络确认后到账，1次网络确认后可提币。"),e("v-uni-view"),t._v("3、最小充值金额："+t._s(t.sysinfo.usdt_cash_min)+" USDT，小于最小金额的充值将不会上账且无法退回。"),e("v-uni-view"),t._v("4、您的充值地址不会经常改变，可以重复充值；如有更改，我们会尽量通过网站公告或邮件通知您。"),e("v-uni-view"),t._v("5、请务必确认电脑及浏览器安全，防止信息被篡改或泄露。")],1)],1),e("v-uni-view",{staticClass:"h30"})],1)},a=[];e.d(n,"a",function(){return i}),e.d(n,"b",function(){return a})}}]);