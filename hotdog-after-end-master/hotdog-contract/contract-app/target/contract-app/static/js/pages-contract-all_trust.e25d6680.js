(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-contract-all_trust"],{"04df":function(t,n,e){"use strict";var i=e("3138"),a=e.n(i);a.a},"151c":function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i={data:function(){return{payword:[],num:0,show:!1}},props:{url:{type:String,default:""},payMoney:{type:null,default:0}},methods:{showPay:function(){this.show=!0},closeTap:function(){this.show=!1,this.payword=[],this.num=0},entry:function(t){if(6!=this.num){var n=this.num,e=this.payword;n<6&&(n+=1,e.push(t)),this.num=n,this.payword=e,this.$emit("onEntry",this.payword)}},delTap:function(){this.num>0&&(this.num-=1,this.payword.pop(),console.log(this.payword))},valueDefault:function(){this.num=0,this.payword=[]}}};n.default=i},3130:function(t,n,e){var i=e("88b8");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var a=e("4f06").default;a("2e710357",i,!0,{sourceMap:!1,shadowMode:!1})},3138:function(t,n,e){var i=e("96ed");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var a=e("4f06").default;a("2e92677f",i,!0,{sourceMap:!1,shadowMode:!1})},"3d2a":function(t,n,e){"use strict";e.r(n);var i=e("a087"),a=e("641f");for(var o in a)"default"!==o&&function(t){e.d(n,t,function(){return a[t]})}(o);e("f010");var s=e("2877"),r=Object(s["a"])(a["default"],i["a"],i["b"],!1,null,"10ab6d90",null);n["default"]=r.exports},"4c39":function(t,n,e){"use strict";var i=function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("v-uni-view",[e("v-uni-view",{staticClass:"tab-box"},[e("v-uni-view",{staticClass:"trust-tab"},t._l(t.tabs,function(n,i){return e("v-uni-text",{key:i,class:t.curID==i?"cur":"",on:{click:function(n){n=t.$handleEvent(n),t.tabTap(i)}}},[t._v(t._s(n))])}),1),t._e()],1),e("v-uni-view",{staticStyle:{height:"90upx"}}),e("v-uni-view",{staticClass:"h20"}),t.WebSocketData.length>0&&0==t.curID?e("v-uni-view",{staticClass:"contract-bottom-list"},t._l(t.WebSocketData,function(n,i){return e("v-uni-view",{key:i,staticClass:"contract-bottom-item"},[e("v-uni-view",{staticClass:"contract-bottom-top"},[e("v-uni-view",{staticClass:"contract-bottom-top-head"},[e("v-uni-text",{class:1==n.type?"green":"red"},[t._v(t._s(1==n.type?"开多":"开空"))]),e("v-uni-text",[t._v(t._s(n.coinname))])],1),e("v-uni-view",{staticClass:"contract-bottom-top-right"},[e("v-uni-text",{staticClass:"cancel",on:{click:function(n){n=t.$handleEvent(n),t.editTap(i)}}},[t._v("修改")]),e("v-uni-text",{staticClass:"cancel",on:{click:function(n){n=t.$handleEvent(n),t.coverTap(i)}}},[t._v("平仓")])],1)],1),e("v-uni-view",{staticClass:"contract-bottom-bottom"},[e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("持仓价(USDT)")]),e("v-uni-text",[t._v(t._s(n.buyprice))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("持仓量")]),e("v-uni-text",[t._v(t._s(n.coinnum))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("保证金(USDT)")]),e("v-uni-text",[t._v(t._s(n.realmoney))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("现价(USDT)")]),e("v-uni-text",[t._v(t._s(n.stopprice))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("手续费(USDT)")]),e("v-uni-text",[t._v(t._s(n.tax))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("浮动盈亏(USDT)")]),e("v-uni-text",[t._v(t._s(n.rates))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("杠杆倍数(倍)")]),e("v-uni-text",[t._v(t._s(n.gearing))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item",staticStyle:{flex:"1"}},[e("v-uni-text",[t._v("时间")]),e("v-uni-text",[t._v(t._s(n.timestr||"--"))])],1)],1)],1)}),1):t.WebSocketData2.length>0&&1==t.curID?e("v-uni-view",{staticClass:"contract-bottom-list"},t._l(t.WebSocketData2,function(n,i){return e("v-uni-view",{key:i,staticClass:"contract-bottom-item"},[e("v-uni-view",{staticClass:"contract-bottom-top"},[e("v-uni-view",{staticClass:"contract-bottom-top-head"},[e("v-uni-text",{class:1==n.type?"green":"red"},[t._v(t._s(1==n.type?"开多":"开空"))]),e("v-uni-text",[t._v(t._s(n.coinname))])],1),e("v-uni-view",{staticClass:"contract-bottom-top-right"},[e("v-uni-text",{staticClass:"status-flag"},[t._v(t._s(0==n.settleflag?"已结算":"未结算")+"("+t._s(n.remark)+")")])],1)],1),e("v-uni-view",{staticClass:"contract-bottom-bottom"},[e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("持仓价(USDT)")]),e("v-uni-text",[t._v(t._s(n.buyprice))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("持仓量")]),e("v-uni-text",[t._v(t._s(n.coinnum))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("保证金(USDT)")]),e("v-uni-text",[t._v(t._s(n.realmoney))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("现价(USDT)")]),e("v-uni-text",[t._v(t._s(n.stopprice))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("手续费(USDT)")]),e("v-uni-text",[t._v(t._s(n.tax))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("浮动盈亏(USDT)")]),e("v-uni-text",[t._v(t._s(n.rates))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item"},[e("v-uni-text",[t._v("杠杆倍数(倍)")]),e("v-uni-text",[t._v(t._s(n.gearing))])],1),e("v-uni-view",{staticClass:"contract-bottom-bottom-item",staticStyle:{flex:"1"}},[e("v-uni-text",[t._v("时间")]),e("v-uni-text",[t._v(t._s(n.timestr||"--"))])],1)],1)],1)}),1):e("v-uni-view",{staticClass:"state-none"},[e("v-uni-view",{staticClass:"h60"}),e("v-uni-image",{attrs:{src:"../../static/images/none.png",mode:""}}),e("v-uni-text",[t._v("暂无记录")])],1),e("pay-password",{ref:"payCover",attrs:{url:"/pages/user/editPassword?type=1"},on:{onEntry:function(n){n=t.$handleEvent(n),t.Cover(n)}}}),t.WebSocketData2.length>3&&1==t.curID?e("load-more",{attrs:{loadingType:t.loadingType,contentText:t.contentText,color:"#D9EBFA"}}):t._e()],1)},a=[];e.d(n,"a",function(){return i}),e.d(n,"b",function(){return a})},"55d6":function(t,n,e){"use strict";e.r(n);var i=e("4c39"),a=e("fb16");for(var o in a)"default"!==o&&function(t){e.d(n,t,function(){return a[t]})}(o);e("a67e");var s=e("2877"),r=Object(s["a"])(a["default"],i["a"],i["b"],!1,null,"df62f410",null);n["default"]=r.exports},"63a2":function(t,n,e){"use strict";e.r(n);var i=e("ff23"),a=e("7a2a9");for(var o in a)"default"!==o&&function(t){e.d(n,t,function(){return a[t]})}(o);e("04df");var s=e("2877"),r=Object(s["a"])(a["default"],i["a"],i["b"],!1,null,"bc7f8664",null);n["default"]=r.exports},"641f":function(t,n,e){"use strict";e.r(n);var i=e("dc46"),a=e.n(i);for(var o in i)"default"!==o&&function(t){e.d(n,t,function(){return i[t]})}(o);n["default"]=a.a},"7a2a9":function(t,n,e){"use strict";e.r(n);var i=e("151c"),a=e.n(i);for(var o in i)"default"!==o&&function(t){e.d(n,t,function(){return i[t]})}(o);n["default"]=a.a},"88b8":function(t,n,e){var i=e("b041");n=t.exports=e("2350")(!1),n.push([t.i,'\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.tab-box[data-v-df62f410]{position:fixed;left:0;top:0;width:100%;z-index:10}.trust-tab[data-v-df62f410]{height:%?90?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;background:#141f30;border-bottom:1px solid #081724;-webkit-box-sizing:border-box;box-sizing:border-box}.trust-tab uni-text[data-v-df62f410]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;text-align:center;line-height:%?90?%;color:#778aa2;font-size:%?30?%;position:relative}.trust-tab uni-text.cur[data-v-df62f410]{color:#fa0}.trust-tab uni-text.cur[data-v-df62f410]:after{content:"";width:100%;height:%?4?%;background:#fa0;position:absolute;left:0;bottom:0}.trust-tab uni-text[data-v-df62f410]:active{background:hsla(0,0%,100%,.05)}.trust-screen[data-v-df62f410]{height:%?70?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;padding:0 %?30?%;background:#141f30}.trust-screen uni-text[data-v-df62f410]{font-size:%?24?%;color:#d1d5eb;background:url('+i(e("9ccf"))+") no-repeat 100%;padding-right:%?28?%;background-size:%?16?% %?12?%;line-height:%?70?%}.trust-screen uni-text[data-v-df62f410]:active{background-color:hsla(0,0%,100%,.1)}\r\n/*  */.contract-bottom-item[data-v-df62f410]{background:#141f30;margin-bottom:%?20?%}.contract-bottom-top[data-v-df62f410]{height:%?100?%;position:relative;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between;border-bottom:1px solid #22324b;padding:0 %?30?%}.contract-bottom-top-head[data-v-df62f410]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:end;-webkit-align-items:flex-end;-ms-flex-align:end;align-items:flex-end}.contract-bottom-top-head uni-text[data-v-df62f410]:first-of-type{font-size:%?36?%}.contract-bottom-top-head uni-text[data-v-df62f410]:nth-of-type(2){font-size:%?30?%;color:#d1d5eb;margin-left:%?8?%}.contract-bottom-top>uni-text[data-v-df62f410]:first-of-type{font-size:%?24?%}.contract-bottom-top-right[data-v-df62f410]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center}.contract-bottom-top-right .cancel[data-v-df62f410]{width:%?108?%;height:%?54?%;text-align:center;line-height:%?54?%;background:#22324b;color:#fa0;margin-left:%?30?%}.contract-bottom-top uni-text.red[data-v-df62f410]{color:#d14b64}.contract-bottom-top uni-text.green[data-v-df62f410]{color:#00ad76}\r\n/*  */.cancel[data-v-df62f410]{-webkit-transition:.25s all;-o-transition:.25s all;transition:.25s all}.cancel[data-v-df62f410]:active{background:hsla(0,0%,78.4%,.4)!important;-webkit-transition:.25s all;-o-transition:.25s all;transition:.25s all}.contract-bottom-bottom[data-v-df62f410]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;min-height:%?118?%;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;padding:%?30?% %?30?% 0 %?30?%;-webkit-flex-wrap:wrap;-ms-flex-wrap:wrap;flex-wrap:wrap;overflow:hidden}.contract-bottom-bottom-item[data-v-df62f410]{-webkit-box-flex:0;-webkit-flex:0 0 33.33%;-ms-flex:0 0 33.33%;flex:0 0 33.33%;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;margin-bottom:%?30?%}.contract-bottom-bottom-item uni-text[data-v-df62f410]:first-child{font-size:%?24?%;color:#46586f;margin-bottom:%?10?%}.contract-bottom-bottom-item uni-text[data-v-df62f410]:nth-child(2){color:#d1d5eb;font-size:%?26?%}\r\n/*  */.cancel[data-v-df62f410]{-webkit-transition:.25s all;-o-transition:.25s all;transition:.25s all}.cancel[data-v-df62f410]:active{background:hsla(0,0%,78.4%,.4)!important}.contract-bottom-date[data-v-df62f410]{height:%?78?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;color:#d1d5eb;font-size:%?24?%;border-top:1px solid #22324b;background:#141f30;padding:0 %?30?%}.contract-bottom-date uni-text[data-v-df62f410]{color:#46586f;margin-right:%?10?%}",""])},"96ed":function(t,n,e){n=t.exports=e("2350")(!1),n.push([t.i,'.keyboard[data-v-bc7f8664]{background:#fafafa;overflow:hidden;width:100%;position:fixed;left:0;bottom:0;z-index:3000;-webkit-transform:translateY(100%);-ms-transform:translateY(100%);transform:translateY(100%);-webkit-transition:all ease .3s;-o-transition:all ease .3s;transition:all ease .3s;opacity:0}.keyboard.show[data-v-bc7f8664]{-webkit-transform:translateY(0);-ms-transform:translateY(0);transform:translateY(0);opacity:1}.keyboard-title[data-v-bc7f8664]{height:%?96?%;padding:0 %?20?%;border-bottom:1px solid #eee;position:relative;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center}.keyboard-title .close[data-v-bc7f8664]{width:%?36?%;height:%?36?%;padding:%?16?%;background-size:%?36?%;position:absolute;left:%?20?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}.keyboard-title uni-text[data-v-bc7f8664]{font-size:%?32?%;color:#454545}.keyboard-payword[data-v-bc7f8664]{margin:%?50?% %?30?% %?20?% %?30?%;height:%?92?%;line-height:%?90?%;text-align:center;border-radius:%?8?%;border:1px solid #d3d3d3;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex}.keyboard-item[data-v-bc7f8664]{-webkit-box-flex:1;-webkit-flex:1;-ms-flex:1;flex:1;border-right:1px solid #d3d3d3;font-size:%?36?%;line-height:%?92?%;color:#333}.keyboard-item[data-v-bc7f8664]:last-of-type{border-right:0}.forget[data-v-bc7f8664]{line-height:%?80?%;display:block;text-align:center;color:#e6b463;margin-bottom:%?30?%}.keyboard-num[data-v-bc7f8664]{background:#e2e7ed;padding-bottom:%?110?%;position:relative;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-flex-wrap:wrap;-ms-flex-wrap:wrap;flex-wrap:wrap;border-top:1px solid #eee}.keyboard-num .num[data-v-bc7f8664]{background:#fff;line-height:%?108?%;border-right:1px solid #eee;border-bottom:1px solid #eee;font-size:%?48?%;text-align:center;width:33.33%;-webkit-box-sizing:border-box;box-sizing:border-box;color:#666}.keyboard-num .del[data-v-bc7f8664]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;width:33.33%;position:absolute;right:0;bottom:0;height:%?110?%;-webkit-box-sizing:border-box;box-sizing:border-box}.keyboard-num .del .del-icon[data-v-bc7f8664]{width:%?64?%;height:%?38?%;background:url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAAAmCAMAAABDGm2rAAAAS1BMVEUAAAD///////////////////////////////////////////////////////////+ZmZn5+fns7Oyfn5+srKzMzMzGxsa/v7+pqam20vjYAAAAD3RSTlMApV5X4mH1VC0nGREK1JAztwIyAAAAvklEQVRIx73Wyw6CUAxF0YJXBERbVB7//6XegdKE0hRyEs6IydqJ8iS7hvftTtsri52BKj3bwIdLNeTzHh3m81DPqGfUM+oZ9Rq4eH7o9fg12kDkJ/n0i3/LZAKBH0VyYfEigwl4XtGvoIcm4Hplnmf1QcHxbLxTmB3P6oOC45lu6oPCbDwYwH8C/ifipxG/kPBLGb+Z8Nv5vxJ4oNjCqQ9VtIC/WNAC/nJd7XqskDqCClVNhBSKlrbW8JGvrC/rTnd+pLfbagAAAABJRU5ErkJggg==")no-repeat;background-size:100%}.keyboard-num .num[data-v-bc7f8664]:first-of-type{position:absolute;left:33.33%;bottom:0}.keyboard-num .num[data-v-bc7f8664]:active{background:#d2ddeb;-webkit-transition:.1s;-o-transition:.1s;transition:.1s}.keyboard-num .del[data-v-bc7f8664]:active{background:#fff;-webkit-transition:.1s;-o-transition:.1s;transition:.1s}.pay-password .pay-money[data-v-bc7f8664]{height:%?90?%;font-size:%?48?%;color:#ff3f3f;text-align:center;line-height:%?90?%;margin-top:%?20?%}',""])},"9ccf":function(t,n){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAMBAMAAACZySCyAAAAElBMVEVHcEx3i6N3iqJ4i6N5hp53iqIV7iYwAAAABXRSTlMAqulVFeyfWQUAAAA6SURBVAjXLclBDQAgEAPBJtWAgwpAAw544N8Kd+3ta5PBeu7iZARmNrAiiKmGI7YWmzwcKYuUaYYtH28OD/GnsfxDAAAAAElFTkSuQmCC"},a087:function(t,n,e){"use strict";var i=function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("v-uni-view",{staticClass:"load-more"},[e("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:1===t.loadingType&&t.showImage,expression:"loadingType === 1 && showImage"}],staticClass:"loading-img"},[e("v-uni-view",{staticClass:"load1"},[e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}})],1),e("v-uni-view",{staticClass:"load2"},[e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}})],1),e("v-uni-view",{staticClass:"load3"},[e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}}),e("v-uni-view",{style:{background:t.color}})],1)],1),e("v-uni-text",{staticClass:"loading-text",style:{color:t.color}},[t._v(t._s(0===t.loadingType?t.contentText.contentdown:1===t.loadingType?t.contentText.contentrefresh:t.contentText.contentnomore))])],1)},a=[];e.d(n,"a",function(){return i}),e.d(n,"b",function(){return a})},a67e:function(t,n,e){"use strict";var i=e("3130"),a=e.n(i);a.a},ace7:function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i=o(e("63a2")),a=o(e("3d2a"));function o(t){return t&&t.__esModule?t:{default:t}}var s={components:{payPassword:i.default,loadMore:a.default},data:function(){return{tabs:["持仓中","历史记录"],curID:0,pickerItem:0,pickerIndex1:0,pickerIndex2:0,pickers1:["BTC","BTC1","BTC2"],pickers1ID:[],pickers2:["全部状态","已委托","委托失败"],pickers2ID:[],WebSocketData:[],WebSocketData2:[],payPasswordStr2:"",CoverIndex:0,page:1,loadingType:0,contentText:{contentdown:"上拉显示更多",contentrefresh:"正在加载...",contentnomore:"没有更多数据了"},socketTask:"",timer:"",timer2:""}},onLoad:function(){uni.showLoading({title:"loading..",mask:!1}),setTimeout(function(){uni.hideLoading()},1500)},onHide:function(){this.socketTask.close(),clearInterval(this.timer),this.timer=null,clearTimeout(this.timer2),this.timer2=null},onUnload:function(){this.socketTask.close(),clearInterval(this.timer),this.timer=null,clearTimeout(this.timer2),this.timer2=null},onShow:function(){var t=this;this.timers1(),this.timer2=setTimeout(function(){t.socketTask.close({success:function(){clearInterval(t.timer),t.timer=null,console.log("每隔十分钟断开连接"),t.timers1()}})},3e5)},methods:{timers1:function(){var t=this;this.socketTask=uni.connectSocket({url:"ws://"+this.webSocketUrl+"/websocket/"+this.$userId(),method:"GET",complete:function(){}}),t.socketTask.onOpen(function(){console.log("WebSocket连接已打开！"),t.timer=setInterval(function(){t.socketTask.send({data:t.$userId()+"_contractall",success:function(){t.socketTask.onMessage(function(n){t.WebSocketData=JSON.parse(n.data)})}})},1500)})},tabTap:function(t){var n=this;this.curID=t,this.page=1,this.loadingType=0,uni.pageScrollTo({scrollTop:0,duration:100}),1==t&&(uni.showLoading({title:"loading...",mask:!1}),this.$ajax("/trade/queryContractOrder",{token:this.$userId(),page:1,status:2},function(t){uni.hideLoading(),t.data.status&&(n.WebSocketData2=t.data.data)}))},pickerTap:function(t){this.pickerItem=t},pickerChoose:function(t){0==this.pickerItem?this.pickerIndex1=t:1==this.pickerItem&&(this.pickerIndex2=t),this.cancel()},cancel:function(){0==this.pickerItem?this.$refs.picker.cancel():1==this.pickerItem&&this.$refs.picker2.cancel()},coverTap:function(t){var n=this;this.CoverIndex=t,uni.showModal({title:"提示",content:"是否要平仓？",success:function(t){t.confirm&&(uni.showLoading({title:"loading...",mask:!0}),n.$ajax("/trade/handleCloseOrder",{token:n.$userId(),ordercode:n.WebSocketData[n.CoverIndex].ordercode},function(t){uni.hideLoading(),n.$alert(t.data.desc,"none",1e3),t.data.status}))}})},Cover:function(t){var n=this;6==t.length&&(console.log(t),this.payPasswordStr2=t.join(""),uni.showLoading({title:"loading...",mask:!0}),this.$ajax("/trade/handleCloseOrder",{token:this.$userId(),payword:this.payPasswordStr2,ordercode:this.WebSocketData[this.CoverIndex].ordercode},function(t){uni.hideLoading(),n.$alert(t.data.desc,"none",1e3),t.data.status?n.$refs.payCover.closeTap():n.$refs.payCover.valueDefault()}))}},onReachBottom:function(){var t=this;0===this.loadingType&&(this.loadingType=1,this.$ajax("/trade/queryContractOrder",{token:this.$userId(),page:++this.page,status:2},function(n){uni.hideLoading(),n.data.status&&(t.WebSocketData2=t.WebSocketData2.concat(n.data.data)),setTimeout(function(){0!=n.data.data.length?t.loadingType=0:t.loadingType=2},300)}))}};n.default=s},b041:function(t,n){t.exports=function(t){return"string"!==typeof t?t:(/^['"].*['"]$/.test(t)&&(t=t.slice(1,-1)),/["'() \t\n]/.test(t)?'"'+t.replace(/"/g,'\\"').replace(/\n/g,"\\n")+'"':t)}},c2f2:function(t,n,e){var i=e("f88b");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var a=e("4f06").default;a("686c8f18",i,!0,{sourceMap:!1,shadowMode:!1})},dc46:function(t,n,e){"use strict";Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i={name:"load-more",props:{loadingType:{type:Number,default:0},showImage:{type:Boolean,default:!0},color:{type:String,default:"#777777"},contentText:{type:Object,default:function(){return{contentdown:"上拉显示更多",contentrefresh:"正在加载...",contentnomore:"没有更多数据了"}}}},data:function(){return{}}};n.default=i},f010:function(t,n,e){"use strict";var i=e("c2f2"),a=e.n(i);a.a},f88b:function(t,n,e){n=t.exports=e("2350")(!1),n.push([t.i,".load-more[data-v-10ab6d90]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-orient:horizontal;-webkit-box-direction:normal;-webkit-flex-direction:row;-ms-flex-direction:row;flex-direction:row;height:%?80?%;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center}.loading-img[data-v-10ab6d90]{height:24px;width:24px;margin-right:10px}.loading-text[data-v-10ab6d90]{font-size:15px;color:#777}.loading-img>uni-view[data-v-10ab6d90]{position:absolute}.load1[data-v-10ab6d90],.load2[data-v-10ab6d90],.load3[data-v-10ab6d90]{height:24px;width:24px}.load2[data-v-10ab6d90]{-webkit-transform:rotate(30deg);-ms-transform:rotate(30deg);transform:rotate(30deg)}.load3[data-v-10ab6d90]{-webkit-transform:rotate(60deg);-ms-transform:rotate(60deg);transform:rotate(60deg)}.loading-img>uni-view uni-view[data-v-10ab6d90]{width:6px;height:2px;border-top-left-radius:1px;border-bottom-left-radius:1px;background:#777;position:absolute;opacity:.2;-webkit-transform-origin:50%;-ms-transform-origin:50%;transform-origin:50%;-webkit-animation:load-data-v-10ab6d90 1.56s ease infinite}.loading-img>uni-view uni-view[data-v-10ab6d90]:first-child{-webkit-transform:rotate(90deg);-ms-transform:rotate(90deg);transform:rotate(90deg);top:2px;left:9px}.loading-img>uni-view uni-view[data-v-10ab6d90]:nth-child(2){-webkit-transform:rotate(180deg);top:11px;right:0}.loading-img>uni-view uni-view[data-v-10ab6d90]:nth-child(3){-webkit-transform:rotate(270deg);-ms-transform:rotate(270deg);transform:rotate(270deg);bottom:2px;left:9px}.loading-img>uni-view uni-view[data-v-10ab6d90]:nth-child(4){top:11px;left:0}.load1 uni-view[data-v-10ab6d90]:first-child{-webkit-animation-delay:0s;animation-delay:0s}.load2 uni-view[data-v-10ab6d90]:first-child{-webkit-animation-delay:.13s;animation-delay:.13s}.load3 uni-view[data-v-10ab6d90]:first-child{-webkit-animation-delay:.26s;animation-delay:.26s}.load1 uni-view[data-v-10ab6d90]:nth-child(2){-webkit-animation-delay:.39s;animation-delay:.39s}.load2 uni-view[data-v-10ab6d90]:nth-child(2){-webkit-animation-delay:.52s;animation-delay:.52s}.load3 uni-view[data-v-10ab6d90]:nth-child(2){-webkit-animation-delay:.65s;animation-delay:.65s}.load1 uni-view[data-v-10ab6d90]:nth-child(3){-webkit-animation-delay:.78s;animation-delay:.78s}.load2 uni-view[data-v-10ab6d90]:nth-child(3){-webkit-animation-delay:.91s;animation-delay:.91s}.load3 uni-view[data-v-10ab6d90]:nth-child(3){-webkit-animation-delay:1.04s;animation-delay:1.04s}.load1 uni-view[data-v-10ab6d90]:nth-child(4){-webkit-animation-delay:1.17s;animation-delay:1.17s}.load2 uni-view[data-v-10ab6d90]:nth-child(4){-webkit-animation-delay:1.3s;animation-delay:1.3s}.load3 uni-view[data-v-10ab6d90]:nth-child(4){-webkit-animation-delay:1.43s;animation-delay:1.43s}@-webkit-keyframes load-data-v-10ab6d90{0%{opacity:1}to{opacity:.2}}",""])},fb16:function(t,n,e){"use strict";e.r(n);var i=e("ace7"),a=e.n(i);for(var o in i)"default"!==o&&function(t){e.d(n,t,function(){return i[t]})}(o);n["default"]=a.a},ff23:function(t,n,e){"use strict";var i=function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("v-uni-view",{staticClass:"pay-password"},[t.show?e("v-uni-view",{staticClass:"popup-bg mask",on:{click:function(n){n=t.$handleEvent(n),t.closeTap(n)}}}):t._e(),e("v-uni-view",{staticClass:"keyboard",class:t.show?"show":""},[e("v-uni-view",{staticClass:"keyboard-title"},[e("v-uni-image",{staticClass:"close",attrs:{src:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoBAMAAAB+0KVeAAAAFVBMVEUAAACdnZ2dnZ2dnZ2dnZ2dnZ2dnZ03OIj5AAAABnRSTlMAzRHqtWIJ/RT+AAAAkUlEQVQoz4XTwQnDMAwAQNER+ui7ha7QDtBHF+gEJSH7j5AQPQ4LgQ1JZOVCbMuO3ydKe77i/b+Pucttie92HZOPbT2upOChzpuWqNA0KOgJCkDRAIVeiwvUAfXAQEEUDDRhoQkLTVgoWJKzz/1oOiSDn03TglSItovcl6MvXF9iEAAJEO23Ioi227s9CO2R2QFLnTvnWz5f3wAAAABJRU5ErkJggg=="},on:{click:function(n){n=t.$handleEvent(n),t.closeTap(n)}}}),e("v-uni-text",[t._v("请输入支付密码")])],1),e("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t.payMoney,expression:"payMoney"}],staticClass:"pay-money"},[t._v("¥"+t._s(t.payMoney))]),e("v-uni-view",{staticClass:"keyboard-payword"},t._l(6,function(n,i){return e("v-uni-view",{key:i,staticClass:"keyboard-item"},[t._v(t._s(t.num>i?"●":""))])}),1),e("v-uni-navigator",{staticClass:"forget",attrs:{url:t.url}},[t._v("忘记密码？")]),e("v-uni-view",{staticClass:"keyboard-num"},[t._l(10,function(n,i){return e("v-uni-view",{key:i,staticClass:"num",on:{click:function(n){n=t.$handleEvent(n),t.entry(i)}}},[t._v(t._s(i))])}),e("v-uni-view",{staticClass:"del",on:{click:function(n){n=t.$handleEvent(n),t.delTap(n)}}},[e("v-uni-view",{staticClass:"del-icon"})],1)],2)],1)],1)},a=[];e.d(n,"a",function(){return i}),e.d(n,"b",function(){return a})}}]);