(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-user-about"],{2351:function(n,t,e){t=n.exports=e("2350")(!1),t.push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.about-head[data-v-689ff578]{padding:%?60?% 0;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;margin:0 %?40?%;border-bottom:1px solid #fff;-webkit-box-pack:center;-webkit-justify-content:center;-ms-flex-pack:center;justify-content:center;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;-ms-flex-direction:column;flex-direction:column}.about-head uni-image[data-v-689ff578]{width:%?200?%;height:%?200?%;margin-bottom:%?20?%}.about-head uni-text[data-v-689ff578]{font-size:%?40?%}.about-content[data-v-689ff578]{padding:%?50?% %?40?% %?40?% %?40?%}",""])},"31a3":function(n,t,e){var i=e("2351");"string"===typeof i&&(i=[[n.i,i,""]]),i.locals&&(n.exports=i.locals);var a=e("4f06").default;a("2e0a21e3",i,!0,{sourceMap:!1,shadowMode:!1})},"6d1f":function(n,t,e){"use strict";var i=function(){var n=this,t=n.$createElement,e=n._self._c||t;return e("v-uni-view",[e("v-uni-view",{staticClass:"about-head"},[e("v-uni-image",{attrs:{src:"../../static/images/logo.png",mode:""}}),e("v-uni-text",[n._v("G币")])],1),e("v-uni-view",{staticClass:"about-content"},[e("v-uni-text",[n._v(n._s(n.info.about_us))])],1)],1)},a=[];e.d(t,"a",function(){return i}),e.d(t,"b",function(){return a})},"799f":function(n,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var i={data:function(){return{info:""}},onLoad:function(){var n=this;this.$loading(),this.$ajax("/utils/getSysInfo",{token:this.$userId()},function(t){n.$hideLoading(),t.data.status&&(n.info=t.data.data)})}};t.default=i},b2f0:function(n,t,e){"use strict";e.r(t);var i=e("799f"),a=e.n(i);for(var o in i)"default"!==o&&function(n){e.d(t,n,function(){return i[n]})}(o);t["default"]=a.a},bbc1:function(n,t,e){"use strict";var i=e("31a3"),a=e.n(i);a.a},f691:function(n,t,e){"use strict";e.r(t);var i=e("6d1f"),a=e("b2f0");for(var o in a)"default"!==o&&function(n){e.d(t,n,function(){return a[n]})}(o);e("bbc1");var u=e("2877"),f=Object(u["a"])(a["default"],i["a"],i["b"],!1,null,"689ff578",null);t["default"]=f.exports}}]);