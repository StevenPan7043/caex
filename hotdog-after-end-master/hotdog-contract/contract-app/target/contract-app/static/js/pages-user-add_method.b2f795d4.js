(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-user-add_method"],{3477:function(n,t,e){"use strict";e.r(t);var i=e("aaf9"),a=e.n(i);for(var s in i)"default"!==s&&function(n){e.d(t,n,function(){return i[n]})}(s);t["default"]=a.a},"757c":function(n,t,e){"use strict";e.r(t);var i=e("933e"),a=e("3477");for(var s in a)"default"!==s&&function(n){e.d(t,n,function(){return a[n]})}(s);e("a156");var o=e("2877"),c=Object(o["a"])(a["default"],i["a"],i["b"],!1,null,"12c3e83f",null);t["default"]=c.exports},"8e24":function(n,t,e){t=n.exports=e("2350")(!1),t.push([n.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\r\n/*  */.add-method-item[data-v-12c3e83f]{display:-webkit-box;display:-webkit-flex;display:-ms-flexbox;display:flex;-webkit-box-align:center;-webkit-align-items:center;-ms-flex-align:center;align-items:center;background:#fff;height:%?120?%;padding:0 %?30?%;border-bottom:1px solid #22324b;-webkit-box-pack:justify;-webkit-justify-content:space-between;-ms-flex-pack:justify;justify-content:space-between}.add-method-item uni-input[data-v-12c3e83f]{width:%?500?%}.add-method-item uni-text[data-v-12c3e83f]:nth-of-type(2){width:%?500?%}.add-method-photo[data-v-12c3e83f]{background:#fff;padding:%?30?%}.share-bottom[data-v-12c3e83f]{background:#fff;padding:%?20?% 0;position:absolute;left:0;bottom:0;width:100%;-webkit-box-sizing:border-box;box-sizing:border-box}.close-view[data-v-12c3e83f]{text-align:center;line-height:14px;height:16px;width:16px;border-radius:50%;background:#ff5053;color:#fff;position:absolute;top:-6px;right:-4px;font-size:12px}",""])},"933e":function(n,t,e){"use strict";var i=function(){var n=this,t=n.$createElement,e=n._self._c||t;return e("v-uni-view",[e("v-uni-view",{staticClass:"h20"}),e("v-uni-view",{staticClass:"add-method"},[e("v-uni-view",{staticClass:"add-method-item"},[e("v-uni-text",[n._v("姓名")]),e("v-uni-text",[n._v(n._s(n.userinfo.realname||"未实名"))])],1),0==n.ids||1==n.ids?e("v-uni-view",{staticClass:"add-method-item"},[e("v-uni-text",[n._v(n._s(n.methodText[n.ids]))]),e("v-uni-input",{attrs:{type:"text",placeholder:"请输入"+n.methodText2[n.ids],"placeholder-class":"placeholder-color"},model:{value:n.account,callback:function(t){n.account=t},expression:"account"}})],1):n._e(),2==n.ids?e("v-uni-view",{staticClass:"add-method-item"},[e("v-uni-text",[n._v("开户银行")]),e("v-uni-input",{attrs:{type:"text",placeholder:"请输入开户银行名称","placeholder-class":"placeholder-color"},model:{value:n.bankname,callback:function(t){n.bankname=t},expression:"bankname"}})],1):n._e(),2==n.ids?e("v-uni-view",{staticClass:"add-method-item"},[e("v-uni-text",[n._v("银行卡账号")]),e("v-uni-input",{attrs:{type:"text",placeholder:"请输入银行卡号","placeholder-class":"placeholder-color",maxlength:"26"},model:{value:n.card,callback:function(t){n.card=t},expression:"card"}})],1):n._e(),2!=n.ids?e("v-uni-view",{staticClass:"add-method-photo"},[e("v-uni-view",{staticClass:"uni-uploader"},[e("v-uni-view",{staticClass:"uni-uploader-head"},[e("v-uni-view",{staticClass:"uni-uploader-title"},[n._v("上传收款二维码")]),e("v-uni-view",{staticClass:"uni-uploader-info"},[n._v(n._s(n.imageList.length)+"/1")])],1),e("v-uni-view",{staticClass:"h20",staticStyle:{background:"#fff"}}),e("v-uni-view",{staticClass:"uni-uploader-body"},[e("v-uni-view",{staticClass:"uni-uploader__files"},[n._l(n.imageList,function(t,i){return[e("v-uni-view",{key:i+"_0",staticClass:"uni-uploader__file",staticStyle:{position:"relative"}},[e("v-uni-image",{staticClass:"uni-uploader__img",attrs:{src:t},on:{click:function(t){t=n.$handleEvent(t),n.previewImage(t)}}}),e("v-uni-view",{staticClass:"close-view",on:{click:function(t){t=n.$handleEvent(t),n.close(i)}}},[n._v("x")])],1)]}),n.imageList.length<1?e("v-uni-view",{staticClass:"uni-uploader__input-box"},[e("v-uni-view",{staticClass:"uni-uploader__input",on:{click:function(t){t=n.$handleEvent(t),n.chooseImg(t)}}})],1):n._e()],2)],1)],1)],1):n._e()],1),e("v-uni-view",{staticClass:"share-bottom"},[e("v-uni-view",{staticClass:"pub-btn-save",on:{click:function(t){t=n.$handleEvent(t),n.save(t)}}},[n._v("保存")])],1)],1)},a=[];e.d(t,"a",function(){return i}),e.d(t,"b",function(){return a})},a156:function(n,t,e){"use strict";var i=e("efd6"),a=e.n(i);a.a},aaf9:function(n,t,e){"use strict";var i=e("288e");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var a=i(e("f499")),s={data:function(){return{methodText:["支付宝账号","微信账号","开户银行"],methodText2:["支付宝账号","微信账号","开户银行名称"],ids:0,userinfo:"",account:"",bankname:"",card:"",imageList:[],img:"",imgs:[]}},onLoad:function(n){var t=this;n.ids&&(this.ids=n.ids),this.$ajax("/account/getMyInfos",{token:this.$userId()},function(n){n.data.status&&(t.userinfo=n.data.data,1==t.userinfo.authflag&&(t.$alert("您还未实名认证，请先实名认证","",1200),setTimeout(function(){uni.navigateBack({delta:1})},1e3)))})},methods:{save:function(){var n=this;if(""!=this.account||0!=this.ids&&1!=this.ids)if(""==this.bankname&&2==this.ids)this.$alert("请输入"+this.methodText2[this.ids],"",1200);else if(""==this.card&&2==this.ids)this.$alert("请输入银行卡号","",1200);else if(this.imageList.length<1&&(0==this.ids||1==this.ids))this.$alert("请上传收款二维码","",1200);else{this.$loading();var t="";t=0==this.ids||1==this.ids?this.account:this.card,this.$ajax("/account/addPayChannel",{token:this.$userId(),type:this.ids-0+1,name:this.userinfo.realname,account:t,payqr:this.imageList[0],bankname:this.bankname},function(t){n.$hideLoading(),n.$alert(t.data.desc,"",1200),t.data.status&&setTimeout(function(){uni.navigateBack({delta:1})},1e3)})}else this.$alert("请输入"+this.methodText[this.ids],"",1200)},close:function(n){this.imageList.splice(n,1)},chooseImg:function(){var n=this;this.imgs=[],uni.chooseImage({sourceType:["camera","album"],count:1,success:function(t){n.imgs=n.imgs.concat(t.tempFilePaths),console.log(n.imgs),n.$loading();for(var e=0;e<n.imgs.length;e++)uni.uploadFile({url:n.websiteUrl+"/utils/upload",filePath:n.imgs[e],name:"file",success:function(t){n.$hideLoading();var e=JSON.parse(t.data);n.img=e.data,n.imageList=n.imageList.concat(n.img),console.log((0,a.default)(n.imageList))}})}})},previewImage:function(){uni.previewImage({urls:this.imageList})}}};t.default=s},efd6:function(n,t,e){var i=e("8e24");"string"===typeof i&&(i=[[n.i,i,""]]),i.locals&&(n.exports=i.locals);var a=e("4f06").default;a("44b98776",i,!0,{sourceMap:!1,shadowMode:!1})}}]);