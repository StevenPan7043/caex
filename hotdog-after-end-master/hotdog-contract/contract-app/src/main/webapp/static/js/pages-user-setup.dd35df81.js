(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-user-setup"],{"2d44":function(n,t,e){"use strict";e.r(t);var i=e("b5d7"),a=e("542d");for(var s in a)"default"!==s&&function(n){e.d(t,n,function(){return a[n]})}(s);e("4436");var o=e("2877"),u=Object(o["a"])(a["default"],i["a"],i["b"],!1,null,"3f318222",null);t["default"]=u.exports},4436:function(n,t,e){"use strict";var i=e("6563"),a=e.n(i);a.a},"542d":function(n,t,e){"use strict";e.r(t);var i=e("b348"),a=e.n(i);for(var s in i)"default"!==s&&function(n){e.d(t,n,function(){return i[n]})}(s);t["default"]=a.a},"559d":function(n,t,e){var i=e("b041");t=n.exports=e("2350")(!1),t.push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.setup-item[data-v-3f318222]{position:relative;height:%?120?%;background:#141f30;border-bottom:1px solid #22324b;padding:0 %?30?%;display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between}.right_icon5[data-v-3f318222]{background:url("+i(e("dfda"))+") no-repeat 50%;width:%?36?%;height:%?36?%;background-size:%?36?%;position:absolute;right:%?30?%;top:50%;-webkit-transform:translateY(-50%);-ms-transform:translateY(-50%);transform:translateY(-50%)}",""])},6563:function(n,t,e){var i=e("559d");"string"===typeof i&&(i=[[n.i,i,""]]),i.locals&&(n.exports=i.locals);var a=e("4f06").default;a("564a0cd6",i,!0,{sourceMap:!1,shadowMode:!1})},b041:function(n,t){n.exports=function(n){return"string"!==typeof n?n:(/^['"].*['"]$/.test(n)&&(n=n.slice(1,-1)),/["'() \t\n]/.test(n)?'"'+n.replace(/"/g,'\\"').replace(/\n/g,"\\n")+'"':n)}},b348:function(n,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var i={data:function(){return{ver:"1.0.0"}},onLoad:function(){},methods:{logout:function(){var n=this;uni.showModal({title:"提示",content:"是否退出登录？",success:function(t){t.confirm&&(uni.showLoading({title:"loading...",mask:!1}),n.$ajax("/customer/exit",{token:n.$userId()},function(t){n.$alert(t.data.desc,"",1200),uni.removeStorageSync("userId"),t.data.status&&uni.redirectTo({url:"../login/login"})}))}})},checkNew:function(){var n=this;uni.showLoading({title:"",mask:!1}),uni.request({url:this.websiteUrl+"/utils/getAppVersion",success:function(t){uni.hideLoading(),console.log(JSON.stringify(t));var e=t.data.appdownload_url;console.log(e),t.data.app_version!=n.ver?uni.showModal({title:"提示",content:t.data.desc_val?t.data.desc_val:"有新的版本需要更新",success:function(n){n.confirm&&(plus.runtime.openURL(e),console.log("确定"))}}):uni.showToast({title:"已经是最新版本",icon:"none",duration:1500})}})}}};t.default=i},b5d7:function(n,t,e){"use strict";var i=function(){var n=this,t=n.$createElement,e=n._self._c||t;return e("v-uni-view",[e("v-uni-view",{staticClass:"h20"}),e("v-uni-view",{staticClass:"setup"},[e("v-uni-navigator",{staticClass:"setup-item",attrs:{url:"about"}},[e("v-uni-text",[n._v("关于我们")]),e("v-uni-text",{staticClass:"right_icon5"})],1),e("v-uni-view",{staticClass:"h20"})],1),e("v-uni-view",{staticClass:"h60"}),e("v-uni-view",{staticClass:"pub-btn-save",on:{click:function(t){t=n.$handleEvent(t),n.logout(t)}}},[n._v("退出登录")])],1)},a=[];e.d(t,"a",function(){return i}),e.d(t,"b",function(){return a})},dfda:function(n,t){n.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkBAMAAAATLoWrAAAAJFBMVEVHcEx3iqJ2iqJ3iqJ3iqJ3iqN3iqR4i6N3iqJ3i6N4iqN3iqK01Dw8AAAAC3RSTlMA3RU875goWLTLd9AvXoQAAACNSURBVCjPY2CgMmATNEAXYt29C12IeffuAHQxk907FdCEmGbvdkZXprp79wJ0sdW7t2LY4L17ErpY5u4tGI7r3r0dXYiLOMexzd4tjK5MY/dGwkJs0rvF0IRKdu9QIOQIpu7d7mjaEndvTEAVYffe3UQwcDR2bynAcJIwhpPQfcyOGS6smKHHjJkAqAQAqpYtyPmMC3sAAAAASUVORK5CYII="}}]);