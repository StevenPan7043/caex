var manager = null;   //弹出的提示层，例如正在处理
var subDialog = null; //修改时，弹出的子窗口
var subGrid = null;   //类似字典管理的字典数据的子表格
/**
 * 绑定公共功能
 */
var $hook = {
	/**
	 * 页面表格查询功能绑定
	 * 
	 * @param btnCls
	 * @param gridId
	 * @param boxId
	 */
	search : function(btnCls) {
		var cls = btnCls || 'wanwu_search';
		if ($("." + cls).length) {
			$("." + cls).click(function() {
				var data = $util.data(this);
				var scope = data.scope;
				var param=$('#' + scope).vals();
				var opt = {"page": "1", "newPage": "1"};
				var gId = data.grid||$('#'+data.tab+" .eventStat").attr("id");
				$grid.load(gId,param,opt);
			});
		}
	},
    /**
	 *
     * 在表格中操作栏执行完相应操作后，刷新表格，具体参考【订单数据】中的撤单功能
     * @param btnCls 操作按钮的类名
     * @param grid	  表格dev id
     * @param scope  查询栏中的参数div域
     */
    opeRefresh : function(btnCls,grid,scope) {
        if ($("." + btnCls).length) {
            var param=$('#' + scope).vals();
            // 每次刷新跳到第一页
            // var opt = {"page": "1", "newPage": "1"};
			// 这样就可以固定在那个页刷新了
            var opt = {};
            $grid.load(grid,param,opt);
        }
    },
	/**
	 * 页面表单验证
	 */
	validate : function(formClass) {
		formClass = formClass || "wanwu_form";
		if ($("." + formClass).length > 0) {
			$("." + formClass).validate(
			{
				errorPlacement : function(lable, element) {
					if (lable.html() != "<b>undefined</b>") {
	                    if (element.hasClass("txta"))
	                    {
	                        element.parent().addClass("l-textarea-invalid");
	                    }
	                    else if (element.hasClass("txt") || element.hasClass("sel") || element.hasClass("number"))
	                    {
	                        element.parent().addClass("l-text-invalid");
	                    }
	                    $(element).removeAttr("title").ligerHideTip();
	                    $(element).attr("title", lable.html()).ligerTip();
					}
				},
				success : function(lable,element) {
						element.parent().removeClass("l-text-invalid");
						element.parent().removeClass("l-textarea-invalid");
						element.ligerHideTip();
				},
				submitHandler : function(vform) {
					var name = $(this.submitButton).attr("tip")
							|| "您确定要提交吗？";
					var action = $(this.submitButton).attr("action")
							|| vform.action;
					
					if (name == "noConfirm" || ($("_noConfirmKey") && $("#_noConfirmKey").val() == "noConfirm")) {
						layer.closeAll();
						params = $(vform).vals();
						$.reqUrlEx(action, params, null, false);
						return false;
					}
					layer.confirm(name, function(){
						layer.closeAll();
						params = $(vform).vals();
						$.reqUrlEx(action, params, null, false);
					}, '确认');		
					return false;
				}
			});
		}
	},
	
	/**
	 * 页面控件初始化
	 */
	widget : function() {
		if ($('.required').length) {
			$('.required').each(function () {
				if ($(this).hasClass('txt_dateATime')||$(this).hasClass('txt_date') || $(this).hasClass('txt_datetime')) {
					$(this).addClass('txt_requireDate');
				}
				if ($(this).hasClass('txt_choose')) {
					$(this).addClass('txt_requireChoose');
				}
			});
		}
		// 下拉框初始化(ID类型)
		if ($("select.ww_select").length) {
			var codes = [], params = [];
			var ss = $("select.ww_select");
			ss.each(function() {
				var data = $util.data(this);
				if (data.textTo) {
					$(this).change(
							data.textTo,
							function(e) {
								$("#" + e.data + ",[name=" + e.data + "]").val(
										$("option:checked", this).text());
							});
				}
				codes.push(data.code.replace("|", ".")); //因为RFC3986文档规定，Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符以及所有保留字符。);
			});
			$.reqUrl($.url("select:" + codes.join('~')), {
				muti : true
			}, function(rst) {
				if (rst.state == 1) {
					ss.each(function() {
						var mdata = $util.data(this);
						var data = rst.data;
						var list = data[mdata.code.replace("|", ".")]; //因为RFC3986文档规定，Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符以及所有保留字符。);
						for(var i in list) {
							var d = list[i];
							var opt = new Option(d.text, d.id);
							if ($(this).hasClass("ww_select_text")) {
								opt = new Option(d.text, d.text);
							}
							if (!$(this).hasClass("ww_select_text") && d.id == mdata.initValue)
								opt.selected = true;
							
							if ($(this).hasClass("ww_select_text") && d.text == mdata.initValue) {
								opt.selected = true;
							}
							
							opt.attrs = d;
							this.options[this.length] = opt;
						}
						
						if (mdata.callBack)
							mdata.callBack(rst);
					});
				} else {
					layer.alert("页面下拉框数据初始化出错!");
				}
			});
		}
		
		
		//导出Excel
		$("body").on("click", ".exportExcel", function () {
			var data = $(this).data("excel")||{};
			var tmp=$(this).data("load-param")||[];
			var param = {
				_export_displays : data.displays.join(","),
				_export_names : data.names.join(","),
				_export_title:data.title||''
			};
			$.each(tmp,function(k,v){
				param[v.name]=v.value;
			});
			$.applyIf(param,data.init||{});
			$util.excel(selfDefineExcelExportUrl || data.url, data.displays, data.names, param);
		});
			
		
		// 下拉树初始化
		if ($('.ww_tree').length) {
			$('.ww_tree').each(function() {
				var data = $util.data(this);
				data.selectBoxWidth = data.selectBoxWidth || 500;
				data.selectBoxHeight = data.selectBoxHeight || 240;
				data.width = data.width || 180;
				// data.slide===undefined&& (data.slide=false);
				if (data.tree && data.tree.code) {
					data.tree.url = $.url("tree:" + data.tree.code);
					delete data.tree.code;
				}
				if (data.code) {
					data.url = $.url("combo:" + data.code);
					if (data.isMultiSelect == true) {
						data.isShowCheckBox = true;
					}
					delete data.code;
				}
				$(this).ligerComboBox(data);
			});
		}
		
		if($('.txt_date').length) {
			$('.txt_date').each(function () {
				var dataDefault = {
					lang:'ch',
					timepicker:false,
					format:'Y-m-d',
					formatDate:'Y-m-d',
					maxDate: $(this).attr("data-maxDate") || false,
					minDate: $(this).attr("data-minDate") || false
				};
				$(this).datetimepicker(dataDefault);
			});
		}
		
		if($('.txt_datetime').length) {
			$('.txt_datetime').each(function () {
				var dataDefault = {
					lang:'ch',
					timepicker:true,
					format:'Y-m-d H:i',
					formatDate:'Y-m-d H:i',
					step:10,
					maxDate: $(this).attr("data-maxDate") || false,
					minDate: $(this).attr("data-minDate") || false
				};
				$(this).datetimepicker(dataDefault);
			});
		}

	}
};


/**
 * ligerUI grid列的常用render
 */
var $LigerColRender={
	money:function(display,name,showTotal){
		var col= {
			display:display,name:name,align:'right',type:'int',width:127,
			render:function(rec, num, value){
				value=value||0;
				var vl=(value==0||value=='0')?"":Math.floor(value*100)/100;
				return "<span class='money'>"+$util.fmtNum(vl)+"</spn>";
			}
		};
		if(showTotal){
			col.totalSummary={
				type:'sum',render:function(obj){
					var vl=(obj.sum==0||obj.sum=='0')?"":Math.floor(obj.sum*100)/100;
					return "<span class='money'>"+$util.fmtNum(vl)+"</span>";
				}	
			};
		}
		return col;
	},
	num:function(display,name,opt,vwidth){
		if(vwidth == undefined){
			vwidth = 120;
		}
		var rst={display:display,width:vwidth,name:name,editor:{type:'float',minValue:0,maxValue:10000000},align:'right'};
		if(opt&&opt.onChanged)rst.editor.onChanged=opt.onChanged;
		return rst;
	},
	text:function(display,name,opt,vwidth){
		if(vwidth == undefined){
			vwidth = 120;
		}
		var rst={display:display,width:vwidth,name:name,editor:{type:'text'},align:'right'};
		if(opt&&opt.onChanged)rst.editor.onChanged=opt.onChanged;
		return rst;
	}
}; 

/*------页面初始化---------*/
$(function(){
	$hook.widget();
	$hook.validate();
	$hook.search();
	
	// -------------------------ESC 键捕获 开始------------------------------------------
	$(document).keyup(function(event){
		switch(event.keyCode) {
			case 27:
				if (null != subDialog) {
					subDialog.close();
				}
				if (typeof(DVSubDialog)!="undefined" && null != DVSubDialog) {
					//图形调度中，调度车辆的Div不能够close，只能够hide 
					DVSubDialog.hide();
				}
				if (typeof(layer)!="undefined" && null != layer) {
					layer.closeAll();
				}
		}
	});
	// -------------------------ESC 键捕获 结束------------------------------------------
});






(function ($) {
	/**
	 * 扩充方法
	 */
	$.fn.extend({
		/**
		 * 取ID范围内所有值 $('#id').vals(空或true) -->{xx:yy} $('#id').vals(flase)
		 * -->xx=yy 赋值 $('#id').vals({xx:yy})
		 */
		vals : function(param, map) {
			if (typeof (param) == 'boolean' || param === undefined) {
				var c = {};// 暂存checkbox,选中的值用逗号隔开
				this.each(function() {
					if ($(this).has(":input").length) {
						var sub = $(":input", this).vals();
						$.extend(c, sub);
					} else if (/checkbox/i.test(this.type)) {
						var key = this.name || this.id;
						var val = this.checked ? (this.value || 'on') : '';
						if (c[key]) {
							c[key] = c[key] + "," + val;
						} else {
							c[key] = val;
						}
					} else if (/radio/i.test(this.type)) {
						if (this.checked)
							c[this.name || this.id] = $.trim($(this).val());
					} else if (/select/i.test(this.tagName)) {
						c[this.name || this.id] = $.trim($(this).val()) + "";
					} else {
						c[this.name || this.id] = $.trim($(this).val());
					}
				});
				return param !== false ? c : $.param(c);
			} else if (typeof (param) == 'object') {
				this.each(function() {
					if (/div|span|table|form|ul|li/i.test(this.tagName)) {
						$(":input,label,b", this).vals(param, map);
					} else {
						var nm = this.name || this.id||$(this).attr("data-vl");
						var vl = map && map[nm] ? param[map[nm]] : param[nm];
						if (vl !== undefined && vl !== null) {
							if (/label|b/i.test(this.tagName)) {
								$(this).text(vl);
							}else  {
								$(this).val(vl);
							}
						}
					}
				});
			}
		}	
	});
	
	$.extend({
		/**
		 * url地址转换 /xx/yy.do -> /base/xx/yy.do tree:dept ->
		 * /base/treeUrl?_code=dept grid:dept ->
		 * /base/gridUrl?_code=dept combo:dept ->
		 * /base/comboUrl?_code=dept
		 */
		url : function(url) {
			var rst = '';
			if (url.indexOf('/') == -1) {
				var ar = url.split(":");
				var chg = "";
				switch(ar[0]) {
					case "select": 
						chg = "/common/select";
						break;
					case "tree":
						chg = "/common/tree";
						break;
					default:
						layer.alert("url函数传入参数错误!");
						break;
				}
				rst += chg;
				ar[1] && (rst += (rst.indexOf('?') == -1 ? '?' : '&')
								+ "_code=" + ar[1]);
			} else {
				rst += url;
			}
			return rst;
		},
		/**
		 * 统一的后台ajax请求
		 * 
		 * @param url
		 * @param data
		 * @param callback
		 * @returns
		 */
		reqUrl : function(url, data, callback) {
			return $.post(url, data, callback, "json");
		},
		/**
		 * 统一的后台ajax请求增强版,增加提示
		 * 
		 * @param url
		 * @param data
		 * @param callback
		 * @param type
		 *            load载入数据 submit提交数据
		 */
		reqUrlEx : function(url, data, callback, msg) {
			if(typeof callback=='string') {msg=callback,callback=null;};
			if (msg === false) {
				$.submitShowWaiting();
				$.reqUrl(url, data, function(rst) {
					$.submitShowResult(rst);
					if (callback)
						callback(rst);
				});
			} else {
				$util.sure(msg || "您确定提交吗？", function() {
					$.submitShowWaiting();
					$.reqUrl(url, data, function(rst) {
						$.submitShowResult(rst);
						if (callback)
							callback(rst);
					});
				});
			}
		},
		/**
		 * 统一的后台ajax请求（同步）
		 * 
		 * @param url
		 * @param data
		 * @param callback
		 * @returns
		 */
		reqUrlSync : function(url, data, callback) {
            return $.ajax({  
                async:false,//使用同步的Ajax请求  
                type: "POST",  
                dataType: 'json',
                url: url,  
                data: data,  
                success: function(rst){  
                	callback(rst);
                }  
            });
		},
		// 表单提交提示，submitShowWaiting在先，再用submitShowResult
		submitShowWaiting : function() {
			manager = layer.load('正在处理，请稍候...', 0);
		},
		submitShowResult : function(rst) {
			if (rst && rst.state == 1) {
				layer.close(manager);
				manager = layer.msg(rst.msg, 0, 1);
	 			hideTipObj(rst.data); 
			} else {
				layer.close(manager);
				layer.alert(rst.msg);
			}
			function hideTipObj(obj) {
				setTimeout(function() {
					try {
						layer.close(manager);
						if ("RefreshSubGrid" == obj){
							closeParentSubDialogAndRefreshSubGrid();
						} else if ("RefreshDispatchVehicle" == obj) {
							closeParentSubDialogAndRefreshDispatchVehicle();
						} else if ("ReloadParentPage" == obj) {
							closeParentSubDialogAndReloadParentPage();
						} else if ("ReLogin" == obj) {
							window.top.location.href="/noau_logout";						
						} else if ("FactoryReLogin" == obj) {
							window.top.location.href="/noau_facotryLogout";						
						} else if ("CloseSelf" == obj) {
							window.close();
						} else if ("ReloadSelfPage" == obj) {
							window.location.href = window.location.href;
						} else {
							closeParentSubDialogAndRefreshGrid();
						}
					} catch (e) {
						layer.alert(e);
					}
					
				}, 1500);
			}
		},
		/**
		 * 默认值赋值
		 * 
		 * @param o
		 *            目标对象
		 * @param c
		 *            默认值
		 * @returns
		 */
		applyIf : function(o, c) {
			if (o && c) {
				for ( var p in c) {
					if (typeof o[p] == "undefined") {
						o[p] = c[p];
					}
				}
			}
			return o;
		}
	});
})(jQuery);

var $render = {
	yesOrNo : function(rec, num, value) {
		return value ? '是' : '否';
	},
	noOrYes : function(rec, num, value) {
		return value ? '否' : '是';
	},
	money:function(rec, num, value){
		value=value||0;
		var vl=(value==0||value=='0')?"":Math.floor(value*100)/100;
		return "<span class='money'>"+vl+"</spn>";
	},
	/**
	 * render:$render.link("<a href='del.do?id={0}&name={1}'>删除</a>","id","name")
	 * 
	 * @param url
	 *            转换的地址
	 * @param keys
	 *            参数名
	 * @returns {Function}
	 */
	link : function(url, keys) {
		if (arguments.length > 2 && keys.constructor != Array) {
			keys = $.makeArray(arguments).slice(1);
		}
		if (keys.constructor == String) {
			keys = [ keys ];
		}
		return function(r, n, v) {
			var params = $.map(keys, function(vl, inx) {
				return r[vl];
			});
			return $util.format(url, params);
		};
	}
};

/**
 * 表格工具类
 */
var $grid = {
	// 载入表格数据
	load : function(gridId, params, opt) {
		opt = opt || {};
		if (gridId) {
			var gm = $("#" + gridId).ligerGetGridManager();
			if(gm){
				if (params)
					opt.parms = this.changeParam(params);
				gm.setOptions(opt);
				gm.loadData();
			}
		}
	},
	// 获取行数据
	getData : function(gridId) {
		var gm = $("#" + gridId).ligerGetGridManager();
		return gm.getData();
	},
	changeParam : function(params) {
		if ($.isFunction(params)) {
			params = arguments.callee(params());
		} else if (!$.isArray(params)) {
			var tmp = [];
			$.each(params, function(k, v) {
				tmp.push({
					name : k,
					value : v
				});
			});
			params = tmp;
		}
		
		return params;
	}
};


/**
 * 工具类
 */
var $util = {
	data : function(el, attrName) {
		attrName = attrName || 'data';
		var data = "{}";
		var m = /({.*})/.exec($(el).attr(attrName));
		if (m)
			data = m[1];
		if (data.indexOf('{') < 0)
			data = "{" + data + "}";
		data = eval("(" + data + ")");
		return data;
	},
	format : function(tpl, params) {
		if (arguments.length > 2 && params.constructor != Array) {
			params = $.makeArray(arguments).slice(1);
		}
		if (params.constructor == String || params.constructor == Number) {
			params = [ params ];
		}
		function _replace(m, word) {
			var rst;
			if (Boolean(word.match(/^[0-9]+$/)) && params.constructor == Array) {
				rst = params[word * 1];
			} else {
				rst = params[word];
			}
			return rst === undefined ? "" : rst;
		}
		return tpl.replace(/#?\{([A-Za-z_0-9]+)\}/g, _replace);
	},
	excel : function(url, displays, names, param) {
		param = param || {};
		$.applyIf(param, {
			_start : 0,
			_pagin : 1,
			_limit : 65530,
			_export : true,
			_export_displays : displays.join(","),
			_export_names : names.join(",")
		});
		var frame = $("#export_frame");
		if (frame.length == 0) {
			frame = $("<iframe id='export_frame' style='display:none' name='export_frame'></iframe>");
			$('body').append(frame);
		}
		var form = $("#export_form");
		if (form.length == 0) {
			form = $("<form method='post' id='export_form' target='export_frame' class='hide'></form>");
			$('body').append(form);
			if ($.browser.msie)
				document.frames["export_frame"].name = "export_frame";
		}
		form.attr("action", url);
		$.each(param, function(k, v) {
			form.append($util.format(
					"<input type='hidden' name='#{name}' value='#{value}'>", {
						name : k,
						value : v
					}));
		});
		form.submit().html("");
	},
	sure:function(name,fn){
		layer.confirm(name, function(){
        	fn();
       	}, '确认');
	},
    /**
     * 10位时间戳转日期：2018-12-28 11：38：40
     * @param timestamp
     */
    timestampToString: function(timestamp) {
        console.log("ts: ",timestamp);
        console.log("ts.length: ",timestamp.length);
        var date = null;
    	if(timestamp.length == 10) {
            date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
		}else {
            date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
		}

        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        if(D < 10) {
            D = "0"+D;
        }
        var h = date.getHours();
        if(h < 10) {
            h = "0" + h + ':';
        }
        var m = date.getMinutes()
        if(m < 10) {
            m = "0" + m + ':';
        }
        var s = date.getSeconds();
        if(s < 10) {
            s = "0" + s;
        }
        return Y+M+D+h+m+s;
    }

};




var selectDialog;

/**
	initWhere 表示列表的时候，带条件，但是点击查询后，就不带此条件。
	          实际上，initWhere随便传一个字符即可，具体实现，由具体的逻辑到myBatis中去判断。
	          公共组件只会告诉你，现在有没有这个initWhere，即<if test="initWhere != null and initWhere != ''">去判断即可
*/
function openSelectDialog(title, code, param, width, height, needCbx,isNeedPage, initWhere, pageSize) {
	if(needCbx) {
		selectDialog = $.ligerDialog.open({ title: title, name:'winselector',width: width, height: height, url: '/jsp/common/SelectCommon.jsp?radomcode='+new Date().getTime()+'&code='+code+'&needCbx='+needCbx+'&isNeedPage='+isNeedPage+'&param='+param+'&initWhere='+initWhere+'&pageSize='+pageSize,buttons: [
	    	{ text: '确定', onclick: f_selectCommonOK },
	        { text: '取消', onclick: f_selectCommonCancel }
	    ]});
    } else {
        selectDialog = $.ligerDialog.open({ title: title, name:'winselector',width: width, height: height, url: '/jsp/common/SelectCommon.jsp?radomcode='+new Date().getTime()+'&code='+code+'&needCbx='+needCbx+'&isNeedPage='+isNeedPage+'&param='+param+'&initWhere='+initWhere+'&pageSize='+pageSize});
    }
    return false;
}
var selectDialog1;
function openSelectDialog1(title, code, param, width, height, needCbx,isNeedPage, initWhere, pageSize) {
	if(needCbx) {
		selectDialog1 = $.ligerDialog.open({ title: title, name:'winselector',width: width, height: height, url: '/jsp/common/SelectCommon1.jsp?radomcode='+new Date().getTime()+'&code='+code+'&needCbx='+needCbx+'&isNeedPage='+isNeedPage+'&param='+param+'&initWhere='+initWhere+'&pageSize='+pageSize,buttons: [
	    	{ text: '确定', onclick: f_selectCommonOK1 },
	        { text: '取消', onclick: f_selectCommonCancel }
	    ]});
    } else {
        selectDialog1 = $.ligerDialog.open({ title: title, name:'winselector',width: width, height: height, url: '/jsp/common/SelectCommon1.jsp?radomcode='+new Date().getTime()+'&code='+code+'&needCbx='+needCbx+'&isNeedPage='+isNeedPage+'&param='+param+'&initWhere='+initWhere+'&pageSize='+pageSize});
    }
    return false;
}


// 省市联动：
// pId:  省份ID
// selectId： 下拉框的控件ID
// initVal: 表示初始默认选中的值
function funcGetCitysByProvinceId(pId, selectId, initVal, callBack) {
	var params = {'pId' : pId, 'initVal' : initVal};
	//先清空城市下拉框
   	$("#" + selectId + " option").remove(); 
   	$("#" + selectId).append("<option value=''>请选择...</option>");
   	
	$.reqUrl($.url('/noau_listCitysByProvinceId'), params, function(rst) {
			if (rst.state) {
				var citysList = rst.data.citysList;
				if (citysList && citysList.length) {
					for ( var i = 0; i < citysList.length; i++) {
						var d = citysList[i];
						$("#" + selectId).append("<option " + (initVal == d.id ? "selected" : "") + " value='" + d.id + "'>" + d.text + "</option>");
					}
				}
				
				if (callBack) {
					callBack();
				}
			}
		}
	);
}


function funcGetCitysByProvinceName(pName, selectId, initVal, callBack) {
	var params = {'pName' : pName, 'initVal' : initVal};
	//先清空城市下拉框
   	$("#" + selectId + " option").remove(); 
   	$("#" + selectId).append("<option value=''>请选择...</option>");
   	
	$.reqUrl($.url('/noau_listCitysByProvinceName'), params, function(rst) {
			if (rst.state) {
				var citysList = rst.data.citysList;
				if (citysList && citysList.length) {
					for ( var i = 0; i < citysList.length; i++) {
						var d = citysList[i];
						$("#" + selectId).append("<option " + (initVal == d.text ? "selected" : "") + " value='" + d.text + "'>" + d.text + "</option>");
					}
				}
				
				if (callBack) {
					callBack();
				}
			}
		}
	);
}

function funcGetDistinctsByCityId(pId, selectId, initVal, callBack) {
	var params = {'pId' : pId, 'initVal' : initVal};
	//先清空城市下拉框
   	$("#" + selectId + " option").remove(); 
   	$("#" + selectId).append("<option value=''>请选择...</option>");
   	
	$.reqUrl($.url('/noau_listDistinctsByCityId'), params, function(rst) {
			if (rst.state) {
				var citysList = rst.data.citysList;
				if (citysList && citysList.length) {
					for ( var i = 0; i < citysList.length; i++) {
						var d = citysList[i];
						$("#" + selectId).append("<option " + (initVal == d.id ? "selected" : "") + " value='" + d.id + "'>" + d.text + "</option>");
					}
				}
				
				if (callBack) {
					callBack();
				}
			}
		}
	);
}

function funcGetDistinctsByCityName(pName, selectId, initVal, callBack) {
	var params = {'pName' : pName, 'initVal' : initVal};
	//先清空城市下拉框
   	$("#" + selectId + " option").remove(); 
   	$("#" + selectId).append("<option value=''>请选择...</option>");
   	
	$.reqUrl($.url('/noau_listDistinctsByCityName'), params, function(rst) {
			if (rst.state) {
				var citysList = rst.data.citysList;
				if (citysList && citysList.length) {
					for ( var i = 0; i < citysList.length; i++) {
						var d = citysList[i];
						$("#" + selectId).append("<option " + (initVal == d.text ? "selected" : "") + " value='" + d.text + "-" + d.baidu_lng + "-" + d.baidu_lat + "-" + d.id + "'>" + d.text + "</option>");
					}
				}
				
				if (callBack) {
					callBack();
				}
			}
		}
	);
}

// 站点父栏目联动：
// siteId:  站点ID
// selectId： 下拉框的控件ID
// initVal: 表示初始默认选中的值
// excludeId: 要排除的Id，因为父栏目不能引用他自己和他自己的子栏目
function funcGetParentColumnBySiteId(siteId, selectId, initVal, excludeId) {
	var params = {'siteId' : siteId, 'initVal' : initVal, 'excludeId' : excludeId};
	//先清空栏目下拉框
   	$("#" + selectId + " option").remove(); 
   	
	$.reqUrl($.url('/noau_listParentColumnBySiteId'), params, function(rst) {
			if (rst.state) {
				var columnsList = rst.data.columnsList;
				$("#" + selectId).append("<option value='0'>***一级栏目***</option>");
				if (columnsList && columnsList.length) {
					for ( var i = 0; i < columnsList.length; i++) {
						var d = columnsList[i];
						$("#" + selectId).append("<option " + (initVal == d.id ? "selected" : "") + " value='" + d.id + "'>" + d.TEXT + "</option>");
					}
				}
			}
		}
	);
}


// 站点文章栏目联动：
// siteId:  站点ID
// selectId： 下拉框的控件ID
// initVal: 表示初始默认选中的值
function funcGetArticleColumnBySiteId(siteId, selectId, initVal) {
	var params = {'siteId' : siteId, 'initVal' : initVal};
	//先清空栏目下拉框
   	$("#" + selectId + " option").remove(); 
   	
	$.reqUrl($.url('/noau_listArticleColumnBySiteId'), params, function(rst) {
			if (rst.state) {
				var columnsList = rst.data.columnsList;
				$("#" + selectId).append("<option value=''>请选择...</option>");
				if (columnsList && columnsList.length) {
					for ( var i = 0; i < columnsList.length; i++) {
						var d = columnsList[i];
						$("#" + selectId).append("<option " + (initVal == d.id ? "selected" : "") + " value='" + d.id + "'>" + d.TEXT + "</option>");
					}
				}
			}
		}
	);
}


// 根据加注站联动其仓库，参数分别为：
// sId:  加注站ID
// selectId： 下拉框的控件ID
// initVal: 表示初始默认选中的值
function funcGetStoragesByStationId(sId, selectId, initVal) {
	var params = {'sId' : sId, 'initVal' : initVal};
	//先清空仓库下拉框
   	$("#" + selectId + " option").remove(); 
   	$("#" + selectId).append("<option value=''>请选择...</option>");
	$.reqUrl($.url('/noau_listStoragesByStationId'), params, function(rst) {
			if (rst.state) {
				var storagesList = rst.data.storagesList;
				if (storagesList && storagesList.length) {
					for ( var i = 0; i < storagesList.length; i++) {
						var d = storagesList[i];
						$("#" + selectId).append("<option " + (initVal == d.id ? "selected" : "") + " value='" + d.id + "'>" + d.text + "</option>");
					}
				}
			}
		}
	);
}



//刷新页面表格
function RefreshGrid(){
	if(parent.gridBox){
		parent.$grid.load("gridBox");
	}
	if(parent.gridBox1){
		parent.$grid.load("gridBox1");
	}
	if(parent.gridBox2){
		parent.$grid.load("gridBox2");
	}
	if(parent.gridBox3){
		parent.$grid.load("gridBox3");
	}
}
//刷新页面“儿子”表格
function RefreshSubGrid(){
	parent.subGrid.ligerGetGridManager().loadData(true);
}

//关闭子窗口
function closeParentSubDialog(){
	if(parent.subDialog){
		parent.subDialog.hide();
	}
}
//表单提交CallBack函数
function closeParentSubDialogAndRefreshGrid(){
	closeParentSubDialog();
	RefreshGrid();
}
//重新加载子页面
function closeParentSubDialogAndReloadParentPage() {
	closeParentSubDialog();
	parent.location.reload();
}
//表单提交CallBack函数(处理类似字典管理的子表格的问题)
function closeParentSubDialogAndRefreshSubGrid(){
	closeParentSubDialog();
	RefreshSubGrid();
}
//表单提交CallBack函数(处理调度车辆排队的问题)
function closeParentSubDialogAndRefreshDispatchVehicle(){
	closeParentSubDialog();
	parent.RefreshDispatchPageInfo();
	RefreshGrid();
}

//打开子窗口：增加
function openAddWindow(title,url,width,height){
	subDialog = $.ligerDialog.open({title: title, url: url ,width : width,height: height,isResize: false,isHidden : false});
}

//打开子窗口：编辑
function editRow(title,url,width,height){
	subDialog = $.ligerDialog.open({title: title, url: url,width : width,height: height,isResize: false,isHidden : false});
}
//打开子窗口：阅读
function readRow(title,url,width,height){
	subDialog = $.ligerDialog.open({title: title, url: url,width : width,height: height,isResize: false,isHidden : false});
}
//打开子表窗口
function openSubTablePage(rowindex,title,url,width,height){
	url += "&id="+g.rows[rowindex].id;
	subDialog = $.ligerDialog.open({title: title, url: url,width : width,height: height,isResize: false,isHidden : false});
}
function commonOpenDialog(pTitle, pUrl, pWidth, pHeight) {
	subDialog = $.ligerDialog.open({title:pTitle, url:pUrl ,width : pWidth,height: pHeight,isResize: false,isHidden : false});
}
function f_selectCommonCancel(item, dialog) {
	dialog.close();
}

/**
	gridSelector，列表的div的ID，记得带#号
	url，格式是：/toEditDic?id={id} id是主键的名称
	isMulti 表示是否多选
*/
function f_common_del(gridSelector, url, isMulti, opName, callBack) {
	var gm = gridSelector.ligerGetGridManager(),row=isMulti?gm.getSelecteds():gm.getSelected();
	var vopName = '' ;
	if("" == opName || opName == undefined){
		vopName = '删除';
	}else{
		vopName = opName;
	}
	if(row==null||row.length==0){
		layer.alert("请选择您要操作的记录!");
	}else{
		var url=$util.format(url,row);
		$.reqUrlEx(url, null, function(rst) {
			if (rst && rst.state) {
				gm.loadData(true);
				
				if (callBack) {
					callBack();
				}
			}
		}, '您确定要将选中的记录' + vopName + '吗？');
	}
}

/**
	gridSelector，列表的div的ID，记得带#号
	url，格式是：/toEditDic?id={id} id是主键的名称
	isMulti 表示是否多选
	width,height 表示弹出的界面的宽度和高度
*/
function f_common_edit(gridSelector, url, isMulti, width, height, title) {
	var gm = gridSelector.ligerGetGridManager(),row=isMulti?gm.getSelecteds():gm.getSelected();
	if(row==null||row.length==0){
		layer.alert("请选择您要操作的记录!");
	}else{
		var url=$util.format(url,row);
		editRow((title || "修改"), url, width, height);
	}
}


//为文本域连续赋值
function checktag(o, tagID){
  	var tagid = function(id){return document.getElementById(id);}
  	var tags = [];//存放标签,避免重复加入
  	var tagidSPLITCHAR = ' ';//设定分隔符,根据程序需求可改
  	var d = tagid(tagID);
  	if (d.value)
    	tags = d.value.split(tagidSPLITCHAR);
  	var v = o.innerHTML;//如果tag有别的值或者别的非innerHTML里体现的内容
    var s = tagidSPLITCHAR+tags.join(tagidSPLITCHAR)+tagidSPLITCHAR
    if (!new RegExp(tagidSPLITCHAR+v+tagidSPLITCHAR,'g').test(s)){
      s+=v;
    }
    if (v == "清空") {
    	s = "";
    }
    s = s.replace(new RegExp("(^"+tagidSPLITCHAR+"*|"+tagidSPLITCHAR+"*tagid)","g"),'');
    d.value = s;
    tags = s.split(tagidSPLITCHAR);
}



function f_common_req(gridSelector, url, isMulti, callback, msg) {
	var gm = gridSelector.ligerGetGridManager(),row=isMulti?gm.getSelecteds():gm.getSelected();
	if(row==null||row.length==0){
		layer.alert("请选择您要操作的记录!");
	}else{
		var url=$util.format(url,row);
		$.reqUrlEx(url, null, callback || function(rst) {
			if (rst && rst.state)gm.loadData(true);
		}, msg);
	}
}

/* 质朴长存法  by lifesinger  补零，调用方法 pad(100, 4);   输出：0100  */
function pad(num, n) {
    var len = num.toString().length;
    while(len < n) {
        num = "0" + num;
        len++;
    }
    return num;
}

function myIsNum(val){  
    var reg = new RegExp("^[0-9]*$");  
    if(!reg.test(val)){  
    	return false;
    }
    return true;
}

function myIsNumWithDot(val) { 
	var re = /^\d+(?=\.{0,1}\d+$|$)/ 
    if (val != "") { 
        if (!re.test(val)) { 
        	return false;
        }
    }
	return true;
}

/**
	gridSelector，列表的div的ID，记得带#号
	url，格式是：/toEditDic?id={id} id是主键的名称
	isMulti 表示是否多选
*/
function f_common_export(url) {
	var frame = $("#vexport_frame");
	if (frame.length == 0) {
		frame = $("<iframe id='vexport_frame' class='hide' name='vexport_frame'></iframe>");
		$('body').append(frame);
	}
	var form = $("#export_form");
	if (form.length == 0) {
		form = $("<form method='post' id='vexport_form' target='vexport_frame' class='hide'></form>");
		$('body').append(form);
		if ($.browser.msie)
			document.frames["vexport_frame"].name = "vexport_frame";
	}
	form.attr("action", url);
	form.submit();
	
}

//将日期类型转换成字符串型格式yyyy-MM-dd hh:mm
function ChangeTimeToString(DateIn)
{
    var Year=0;
    var Month=0;
    var Day=0;
    var Hour = 0;
    var Minute = 0;
    var CurrentDate="";

    //初始化时间
    Year      = ( DateIn.getYear() < 1900 ) ? ( 1900 + DateIn.getYear() ) : DateIn.getYear();
    Month     = DateIn.getMonth()+1;
    Day       = DateIn.getDate();
    Hour      = DateIn.getHours();
    Minute    = DateIn.getMinutes();

    CurrentDate = Year + "-";
    if (Month >= 10 )
    {
        CurrentDate = CurrentDate + Month + "-";
    }
    else
    {
        CurrentDate = CurrentDate + "0" + Month + "-";
    }
    if (Day >= 10 )
    {
        CurrentDate = CurrentDate + Day ;
    }
    else
    {
        CurrentDate = CurrentDate + "0" + Day ;
    }
    
     if(Hour >=10)
    {
        CurrentDate = CurrentDate + " " + Hour ;
    }
    else
    {
        CurrentDate = CurrentDate + " 0" + Hour ;
    }
    if(Minute >=10)
    {
        CurrentDate = CurrentDate + ":" + Minute ;
    }
    else
    {
        CurrentDate = CurrentDate + ":0" + Minute ;
    }      
    return CurrentDate;
}


/**
	列表中敲击回车，响应查询时间的公共代码
*/
$(function () {
	$(".enterAsSearch").keyup(function (event) {
		if (event.keyCode == "13") {
			$('#btnOK').trigger('click');
         	return false;
     	}
	});
	
	$(".enterAsSearch1").keyup(function (event) {
		if (event.keyCode == "13") {
			$('#btnOK1').trigger('click');
         	return false;
     	}
	});
});

function isNull(data) {
	if (data == "" || data == "null" || data == null || data == undefined || data == "undefined") { 
		return true;
	}
	return false;
}

function isNullSetReplace(data, replaceStr) {
	if (data == "" || data == "null" || data == null || data == undefined || data == "undefined") { 
		return replaceStr;
	}
	return data;
}

//字符串转换为时间戳
function getDateTimeStamp (dateStr) {
    return Date.parse(dateStr.replace(/-/gi,"/"));
}
function getDateDiff (dateStr) {
    var publishTime = getDateTimeStamp(dateStr)/1000,
        d_seconds,
        d_minutes,
        d_hours,
        d_days,
        timeNow = parseInt(new Date().getTime()/1000),
        d,

        date = new Date(publishTime*1000),
        Y = date.getFullYear(),
        M = date.getMonth() + 1,
        D = date.getDate(),
        H = date.getHours(),
        m = date.getMinutes(),
        s = date.getSeconds();
        //小于10的在前面补0
        if (M < 10) {
            M = '0' + M;
        }
        if (D < 10) {
            D = '0' + D;
        }
        if (H < 10) {
            H = '0' + H;
        }
        if (m < 10) {
            m = '0' + m;
        }
        if (s < 10) {
            s = '0' + s;
        }

    d = timeNow - publishTime;
    d_days = parseInt(d/86400);
    d_hours = parseInt(d/3600);
    d_minutes = parseInt(d/60);
    d_seconds = parseInt(d);

    if(d_days > 0 && d_days < 7){
        return d_days + '天前';
    }else if(d_days <= 0 && d_hours > 0){
        return d_hours + '小时前';
    }else if(d_hours <= 0 && d_minutes > 0){
        return d_minutes + '分钟前';
    }else if (d_seconds < 60) {
        if (d_seconds <= 0) {
            return '1秒前';
        }else {
            return d_seconds + '秒前';
        }
    }else if (d_days >= 7){
        return '7天前';
    }
}




function DisableElements(container,blnHidenButton)
{
  if (!container)
  return;
  var aEle;
  if (navigator.appName =="Microsoft Internet Explorer") //IE
  {
    for (var i=0;i<container.all.length;i++)
    {
      aEle = container.all[i];
      tagName = aEle.tagName.toUpperCase();
      if ((tagName=="SELECT"))
      {
        aEle.disabled = true;
        if(tagName=="BUTTON" && blnHidenButton)
        {
          //aEle.style.display = "none";//对button不做处理
        }
      }
      else if (tagName=="INPUT")
      {
        if (aEle.type.toUpperCase()!="HIDDEN")
        {
          if (aEle.type.toUpperCase()=="TEXT")
          {
            ReadonlyText(aEle);
          }
          else if (aEle.type.toUpperCase()=="BUTTON")
          {
            //do nothing;
          }
          else
          {
            aEle.disabled = true;
          }
        }
        if((aEle.type.toUpperCase()=="BUTTON"||aEle.type.toUpperCase()=="SUBMIT") && blnHidenButton)
        {
          //aEle.style.display = "none";//对button不处理
        }
      }
      else if (tagName=="TEXTAREA")
      {
        ReadonlyText(aEle);
      }
    }
  }
  else//非IE浏览器
  {
    var aEle = container.getElementsByTagName("select");
    for (var i=0;i< aEle.length;i++)
    {
      aEle[i].disabled = true;
    }
    aEle = container.getElementsByTagName("button");
    for (var i=0;i< aEle.length;i++)
    {
      aEle[i].disabled = true;
    }
    aEle = container.getElementsByTagName("textarea");
    for (var i=0;i< aEle.length;i++)
    {
      ReadonlyText(aEle[i]);
    }
    aEle = container.getElementsByTagName("input");
    for (var i=0;i< aEle.length;i++)
    {
      if (aEle[i].type.toUpperCase()!="HIDDEN")
      {
        if (aEle[i].type.toUpperCase()=="TEXT")
        {
          ReadonlyText(aEle[i]);
        }
        else
        {
          aEle[i].disabled = true;
        }
      }
      if((aEle[i].type.toUpperCase()=="BUTTON"||aEle[i].type.toUpperCase()=="SUBMIT")&&blnHidenButton)
      {
        aEle[i].style.display = "none";
      }
    }
  }
}
function ReadonlyText(objText) 
{
  if (objText){
    //objText.style.backgroundColor = "menu";
    objText.style.background = "#E6E6E6";
    //objText.style.color = "black";
    objText.readOnly=true;
  }
}