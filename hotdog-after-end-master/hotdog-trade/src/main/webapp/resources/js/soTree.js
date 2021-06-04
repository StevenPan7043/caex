;(function ($) {
	$.fn.extend({
		'soTree':function (o) {
			var o= $.extend({
				type : 'json',//json,stepJson,html
				url : null,//当url不为空，则tree以json格式调入数据，否则tree为html格式
				rootId:'0',//根节点id，只在stepJson方式中开启
				rootText:'根节点',//根节点文本，只在stepJson方式中开启
				rootHide:false,//根节点是否隐藏，只在stepJson方式中开启
				rootExpand:true,//根节点是否默认展开，只在stepJson方式中开启
				slide : false,//是否开启收缩动画
				expand : true,//默认展开
				checkbox : false,//默认不带选择框
				checked : false,//默认不处于选择状态
				hideId:[],//设置隐藏节点，以id为标识，数组的形式分开，如[10,22],表示当前id为10与22的节点被隐藏
				onSuccess : function () {},//数据加载成功事件
				onStepSuccess : function () {},//分步时数据加载成功事件
				onChecked : function () {},//被选中事件
				onCancelChecked : function () {},//取消选中事件
				onCheck : function () {},//选中事件
				onExpand : function () {},//节点展开事件
				onCollapse : function () {},//节点收缩事件
				onSelect : function () {},//被点选事件
				onClick : function () {}//点击事件（所有节点）
			}, o || {});

			var $this=$(this),url = o.url,param=o.param;

			//加载tree表现及事件函数
			function afterLoadData(obj,parChecked,topObj) {
				//obj = obj.tagName;
				//alert();
				$('li:has(ul)',obj).addClass('li_par');//有ul子节点添加 li_par 类名标识
				$('li.li_par>.em_op',obj).addClass('em_par');//有ul子节点的li对应em添加class，添加加减号操作图标
				$('li.li_par>.span_t',obj).addClass('span_par');//有ul子节点的li对应span添加class，添加文件夹图标
				if (o.type != 'stepJson') {
					$('li:last',obj).addClass('li_last');
				}//末尾li添加 li_last 类名标识
				$('ul',obj).each(function () {
					//if (o.type != 'stepJson') {
						$(this).find('li:last').addClass('li_last');//每个ul末尾li添加 li_last 类名标识
					//}
					if ($('>li:last',this).hasClass('li_par')) {//查找到有子节点ul的末尾li，添加class，采用不同图标，去除虚线背景
						$('>li:last',this).addClass('li_parLast');//采用不同图标
						$('>li:last>ul',this).addClass('ul_chiLast');//去除虚线背景
					}
				});

				$('.em_par',obj).click(function () {//点击十字图标，切换操作图标、文件夹图标、子ul状态
					if (!$(this).hasClass('em_in')) {
						$(this).addClass('em_in').siblings('.span_par').addClass('span_in');
						if (o.slide) {$(this).siblings('ul').slideUp('fast');}else {$(this).siblings('ul').hide();}
					}else {
						$(this).removeClass('em_in').siblings('.span_par').removeClass('span_in').siblings('ul');
						if (o.slide) {$(this).siblings('ul').slideDown('fast');}else {$(this).siblings('ul').show();}
					}
				});

				if (!o.expand) {//如果设置菜单为整体收缩
					$('.em_par',obj).addClass('em_in');
					$('.span_par',obj).addClass('span_in');
					$('ul',obj).hide();
				}
				if (o.checkbox) {
					var $b_chk = $('.b_chk',obj);
					$b_chk.css('display','inline');
					if (parChecked) {$b_chk.addClass('b_checked');}//如果父节点已勾选
					$b_chk.click(function () {//选择框事件
						if ($(this).hasClass('b_checked')) {
							$(this).removeClass('b_incomplete').removeClass('b_checked').parent('li').find('ul .b_chk').removeClass('b_checked');
						}else {
							$(this).addClass('b_checked').parent('li').find('ul .b_chk').removeClass('b_incomplete').addClass('b_checked');
						}
						//巡检选择状态 start
						var parU = $(this).parents('ul');
						parU.each(function () {
							var $that = $(this);
							var cLen = $that.find('.b_chk').length;
							var dLen = $that.find('.b_checked').length;
							if (cLen == dLen) {//比较 b_chk与b_checked长度以获得状态
								$that.siblings('.b_chk').removeClass('b_incomplete').addClass('b_checked');
							}else if (dLen>0&&cLen != dLen) {
								$that.siblings('.b_chk').removeClass('b_checked').addClass('b_incomplete');
							}else if (dLen==0){
								$that.siblings('.b_chk').removeClass('b_checked').removeClass('b_incomplete');
							}
						});
						//巡检选择状态 over
					});
				}

				var topObj = topObj?topObj:obj;//是否设定了顶级操作ul
				$(obj).click(function (e) {
					var oo = (e.target || e.srcElement);
					//console.log(oo.nodeName.toLowerCase());
					//console.log(oo.className);
					var cellInfo  = funReturnInfo(oo,topObj);
					//console.log(cellInfo);
				});
			};

//				$('.span_t',obj).click(function () {
//					$('.span_t',topObj).removeClass('span_t_selected');
//					$(this).addClass('span_t_selected');
//				});

			//点击事件返回数据函数
			function funReturnInfo(obj,topObj) {
				var obj = $(obj);
				var pLi = obj.parent('li');
				var cellInfo = {
					id : pLi.attr('id'),
					code:pLi.attr('data-code'),
					text : $('>.span_t',pLi).text(),
					pid : (pLi.attr('pid'))||(pLi.parent().parent().attr('id')),
					pText : $('>.span_t',(pLi.parent().parent())).text(),
					isLeaf : !$('>.span_t',pLi).hasClass('span_par'),
					isExpand : null,
					isChecked : null,
					childInfo : null
				};

				$('.span_t',topObj).removeClass('span_t_selected');
				$('>.span_t',pLi).addClass('span_t_selected');

				if (!cellInfo.isLeaf) {//非叶子节点，获得子对象信息并存于数组中
					var childInfo = [];
					var $thisUl = $('>ul',pLi);
					var $thisULi = $('>li',$thisUl);
					cellInfo.isExpand = !($('>em.em_par',pLi).hasClass('em_in'));
					$thisULi.each(function () {
						var cId = $(this).attr('id'),cPid = cellInfo.id,cText = $('>.span_t',this).text();
						childInfo.push(cId+'^'+cPid+'^'+cText);
					});
					cellInfo.childInfo = childInfo;
				}
				if (pLi.find('>.b_chk')) {
					cellInfo.isChecked = $('>.b_chk',pLi).hasClass('b_checked');
					if (obj.hasClass('b_chk')) {//当前节点为b
						if (cellInfo.isChecked) {
							o.onChecked(cellInfo);
						} else {
							o.onCancelChecked(cellInfo);
						}
					}
					o.onCheck(cellInfo);
				}
				if (obj.hasClass('span_t')) {//当前节点为span
					if (obj.hasClass('span_t_selected')) {
						o.onSelect(cellInfo);
					}
				}
				if (obj.hasClass('em_par')) {//当前节点为em
					if (obj.hasClass('em_in')) {
						o.onCollapse(cellInfo);
					}else {
						o.onExpand(cellInfo);
					}
				}
				o.onClick(cellInfo);
				//console.log(cellInfo);
				//return cellInfo;
			};

			function checkAexpend (obj) {
				if (o.hideId.length>0) {
					$.each(o.hideId,function () {$('#'+this).hide();});
				}
				$('li',obj).each(function () {
					if ($(this).attr('ischecked')=='true') {
						$('.b_chk',this).addClass('b_checked');
					}else if ($(this).attr('ischecked')=='false'){
						$('.b_chk',this).removeClass('b_checked');
					}
					if ($(this).attr('isexpand')=='true') {
						$('>.em_par',this).removeClass('em_in');
						$('>.span_par',this).removeClass('span_in');
						$('>ul',this).show();
					}else if ($(this).attr('isexpand')=='false') {
						$('>.em_par',this).addClass('em_in');
						$('>.span_par',this).addClass('span_in');
						$('>ul',this).hide();
					}
					if ($(this).attr('hide')=='true') {
						$(this).hide();
					}
				});
			}

			//加载tree数据
			if (o.type== 'json') {//json格式数据
				$.post(url,param, function(data) {
					var data = data;
					var len  = data.length;
					var ul_tree = $('<ul class="ul_tree"></ul>');
					var checked = o.checked;
					o.onSuccess(data);
					for (var i = 0; i < len; i++) {
						var dataCell = {
							id:data[i].id,
							pid:data[i].pid,
							text:data[i].text,
							ischecked:data[i].ischecked,
							isHide:data[i].hide,
							isexpand:data[i].isexpand,
							code:data[i].code
						};
						var ulLi = $(ul_tree).find('li'),lkey =1;
						var ischeckedS = '',isexpandS = '',isHideS = '';
						//console.log(dataCell.ischecked=='false');
						if (dataCell.ischecked===true) {ischeckedS = ' ischecked="true"';}else if(dataCell.ischecked===false){ischeckedS = ' ischecked="false"';}
						if (dataCell.isHide===true) {isHideS = ' style="display:none;"';}
						if (dataCell.isexpand===true) {isexpandS = ' isexpand="true"';}else if(dataCell.isexpand===false){isexpandS = ' isexpand="false"';}
						for (var j = 0; j < ulLi.length; j++) {
							if ( dataCell.pid == ulLi.eq(j).attr('id')) {//是否有父节点pid，如果有，添加到对应父节点下
								if (ulLi.eq(j).find('>ul').length) {//如果已经有了ul
									ulLi.eq(j).find('>ul').append('<li id="'+dataCell.id+'" data-code="'+dataCell.code+'" pid="'+dataCell.pid+'"'+ischeckedS+isexpandS+isHideS+'><em class="em_op"></em><b class="b_chk"></b><span class="span_t">'+dataCell.text+'</span></li>');
								}else {
									ulLi.eq(j).append('<ul><li id="'+dataCell.id+'" data-code="'+dataCell.code+'" pid="'+dataCell.pid+'"'+ischeckedS+isexpandS+isHideS+'><em class="em_op"></em><b class="b_chk"></b><span class="span_t">'+dataCell.text+'</span></li></ul>');
								}
								lkey = 0;//添加到已有节点下则lkey = 0
							}
						}
						if (lkey) {//如果没有找到父节点pid，添加到根节点下
							ul_tree.append('<li id="'+dataCell.id+'" data-code="'+dataCell.code+'" pid="'+dataCell.pid+'"'+ischeckedS+isexpandS+'><em class="em_op"></em><b class="b_chk"></b><span class="span_t">'+dataCell.text+'</span></li>');
						}
					}//循环判断生成树

					/*<ul class="ul_tree">
						<li><em class="em_op"></em><span class="span_t">节点1</span></li>
					</ul> json生成树的基元结构*/

					afterLoadData(ul_tree,checked);
					$this.append(ul_tree);
					checkAexpend($this);
				},"json");
			}else if (o.type == 'stepJson') {//分布json格式数据载入
				o.expand = false;
				var checked = o.checked;
				var ul_tree,rootBox;
				if (o.rootHide) {//根节点隐藏，则tree父盒子不同
					ul_tree = $('<ul class="ul_tree"></ul>');
					rootBox = $(ul_tree);
				}else {
					ul_tree = $('<ul class="ul_tree"></ul>');
					var rootHtml = '<li id='+o.rootId+' class="li_par"><em class="em_op em_par"></em><b class="b_chk"></b><span class="span_t span_par">'+o.rootText+'</span><ul></ul></li>';
					ul_tree.append(rootHtml);
					rootBox = $('ul',ul_tree);
				}

				var rootUrl = url+'?id='+o.rootId;
				$.getJSON(rootUrl, function(data) {//初次加载
					var data = data;
					var len  = data.length;
					o.onSuccess(data);
					for (var i = 0; i < len; i++) {
						var dataCell = {
							id:data[i].id,
							pid:data[i].pid,
							text:data[i].text,
							nodetype:data[i].nodetype,
							ischecked:data[i].ischecked,
							isHide:data[i].hide
						};
						var nodeClass = dataCell.nodetype?'class="li_par"':'';
						var ischeckedS = '',isHideS = '';
						if (dataCell.ischecked===true) {ischeckedS = ' ischecked="true"';}else if(dataCell.ischecked===false){ischeckedS = ' ischecked="false"';}
						if (dataCell.isHide===true) {isHideS = ' style="display:none;"';}
						rootBox.append('<li id="'+dataCell.id+'" pid="'+dataCell.pid+'" '+nodeClass+ischeckedS+isHideS+'><em class="em_op"></em><b class="b_chk"></b><span class="span_t">'+dataCell.text+'</span></li>');
					}
					afterLoadData(ul_tree,checked);
					$this.append(ul_tree);
					checkAexpend(ul_tree);
					if ((!o.rootHide)&&o.rootExpand) {//如果节点收缩并且根节点不隐藏
						rootBox.show();
						$('.em_op:first',ul_tree).removeClass('em_in');
					}
					
					$(ul_tree).on('click','.em_par',function () {//点击可扩展节点，进行异步请求数据
						var loadLi = $(this).parent();
						if (loadLi.find('ul').length==0) {//如果未加载过，进行加载
							var checked = o.checked,topUl = ul_tree;
							if ($('.b_chk:first',loadLi).hasClass('b_checked')) {checked = true;}//判断当前可扩展节点是否被勾选，如果是，扩展后的子对象也被勾选
							var stepUrl = o.url +'?id='+loadLi.attr('id');//对应当前节点json链接
							$.getJSON(stepUrl, function(stepData) {//请求json数据
								var stepData = stepData;
								var stepLen = stepData.length;
								var stepTree = $('<ul></ul>');
								o.onStepSuccess(stepData);
								for (i = 0; i < stepLen; i++) {
									var dataCell = {
										id:stepData[i].id,
										pid:stepData[i].pid,
										text:stepData[i].text,
										nodetype:stepData[i].nodetype,
										ischecked:stepData[i].ischecked,
										isHide:stepData[i].hide
									};
									var nodeClass = dataCell.nodetype?' class="li_par"':'';
									var ischeckedS = '',isHideS = '';
									if (dataCell.ischecked===true) {ischeckedS = ' ischecked="true"';}else if(dataCell.ischecked===false){ischeckedS = ' ischecked="false"';}
									if (dataCell.isHide===true) {isHideS = ' style="display:none;"';}
									stepTree.append('<li id="'+dataCell.id+'" pid="'+dataCell.pid+'"'+nodeClass+ischeckedS+isHideS+'><em class="em_op"></em><b class="b_chk"></b><span class="span_t">'+dataCell.text+'</span></li>');
								}//生成ul树
								afterLoadData(stepTree,checked,topUl);//加载新添加节点树所有事件
								$(stepTree).hide();//先隐藏，以便slide动画执行
								loadLi.append(stepTree);
								checkAexpend(stepTree);
								if (o.slide) {$(stepTree).slideDown('fast');}else {$(stepTree).show();}
							});
						}
					});
				});
			}else if (o.type == 'html') {//页面已有html dom结构
				/*<ul>
					<li><span>节点1</span></li>
				</ul> html树的基元结构*/
				if ($('span',$this).length>0) {
					$('ul',$this).addClass('ul_tree');
					$('span',$this).addClass('span_t').before('<em class="em_op"></em><b class="b_chk"></b>');
					afterLoadData($('.ul_tree',$this),o.checked);
					checkAexpend($this);
				}else {
					alert('html树结构不正确，参考结构：<ul><li><span>节点1</span></li></ul>');
				}
			}
		}
	});
})(jQuery);