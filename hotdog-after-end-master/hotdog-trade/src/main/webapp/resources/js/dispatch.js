var windowHeight = $(window).height();
var windowWidth  = $(window).width();
var mainContentTDHeight = windowHeight - $(".table_dispatch_station").height() - 34;
var invoiceListTDWidth = windowWidth - 670;
//调度车辆的Div，此Div在摁下ESC键时，不要close
var DVSubDialog = null;
//当前正在生产的工程简称和车号
var curProduceProjectShortNameAndVehicleNos = "";
//提醒有工地即将开盘的时间间隔参数
var dispatchInfoTipCount = 10;
//工程生产方量的HTML
var projectSCFLHtml = "";
if (haszhsXSSCFLPermission) {
	projectSCFLHtml = '			<div class="projectFinishQuantityCls">'
						+ '				<b><em class="em_data fn_finishQuantity">0</em></b>'
						+ '			</div>';
}
//订单生产方量的HTML
var orderSCFLHtml = "";
if (haszhsXSSCFLPermission) {
	orderSCFLHtml = '		<span class="oneFinishQuantityCls"><em class="em_data one_fn_finishQuantity">{product_num}</em></span>';
}
//不显示生产方量时，订单和工程距离左边的偏移量
var dedutSCFLWidth = 65;
//不显示生产方量时，最多显示12辆出站车辆，显示生产方量时，最多显示6辆出站车辆
var maxDisplayOutVNum = 12;
if (haszhsXSSCFLPermission) {
	dedutSCFLWidth = 0;
	maxDisplayOutVNum = 6
}


var template = {
	vehicleTDInner : '<span class="vehicle" data-v_id="{id}" data-v_no="{v_no}" data-load_volume="{load_volume}" data-v_memo="{v_memo}" title="{v_memo}"><b class="v_no">{v_no}</b></span>',
	readyInvoiceTDInner : '<span id="ready_invoice_{id}" data-id="{id}" data-v_no="{v_no}" data-mgr_station_id="{mgr_station_id}" data-project_short_name="{project_short_name}" class="ready_invoice" title="双击取消该任务，拖动可以换站&#13;&#13;{titleInfo}"><b class="b_project_shortname">{project_abbreviation_name}</b><b class="b_v_no">{v_no}</b></span>',
	projectListInner : '<dl id="one_project_{id}" class="one_project" title="双击收起/展开、拖动排序">'
						+ '<dt class="projectInfo" id="projectInfo_{id}">'
						+ '		<div class="projectNameCls" title="{p_name}|{p_no}[{c_name}]">{p_short_name}[{c_short_name}]</div>'
						+ '			<div class="projectPlanQuantityCls">'
						+ '				<b><em class="em_data fn_planQuantity">0</em></b>'
						+ '			</div>'
						
						+ projectSCFLHtml
						
						+ '			<div class="projectBillNumCls" style="left:' + (360 - dedutSCFLWidth) + 'px;">'
						+ '				<b><em class="em_data fn_billNum">0</em></b>'
						+ '			</div>'
						+ '			<div class="projectCarNumCls" style="left:' + (425 - dedutSCFLWidth) + 'px;">'
						+ '				<b><em class="em_data fn_carNum">0</em></b>'
						+ '			</div>'
						+ '			<div class="projectZBCls" style="left:' + (465 - dedutSCFLWidth) + 'px;">'
						+ '				&nbsp;'
						+ '			</div>'
						+ '			<div class="projectSCCls" style="left:' + (505 - dedutSCFLWidth) + 'px;">'
						+ '				&nbsp;'
						+ '			</div>'
						+ '			<div class="projectCZCls" style="left:' + (545 - dedutSCFLWidth) + 'px; width:' + (75 + dedutSCFLWidth) + 'px;">'
						+ '				&nbsp;'
						+ '			</div>'
						+ '		</dt>'
						+ '		<dd class="orderList">'
						+ '		</dd>'
						+ '		<dd class="one_project_over_span"><span id="span_over_span_{id}" class="span_over_span span_over_spanMin" title="收起/展开">收起</span></dd>'
						+ '</dl>',
	projectListAddtional : '<dl id="one_project_null" style="position:relative;float:left;width:98%;margin:5px 0 3px 0;padding:0 0 0 0; display:inline;">'
						+ '		<dt></dt>'
						+ '		<dd><br><br><br><br><br></dd>'
						+ '</dl>',  //单纯是为了使最下面有一点间隙。
	orderListInner : '<div id="order_{id}" title="[双击调度][右键更多功能]" class="div_order" data-order_id="{id}" data-fix_station="{fix_station}" data-s_project_id="{s_project_id}" data-s_customer_id="{s_customer_id}" data-l_product_id="{l_product_id}" data-p_unload_way_id="{p_unload_way_id}" data-work_part="{work_part}" data-total_car="{total_car}" data-p_name="{s_project_name}" data-product_name="{product_name}" data-dispatch_classify="{dispatch_classify}">'
						+ '<p class="order_info">'
						+ '		<span class="order_basic_info" id="order_basic_info_{id}" title="{o_title}">'
						+ '			<span class="order_basic_info_work_part" id="order_basic_info_work_part_{id}">{work_part}</span>'
						+ '			<span class="order_basic_info_product_name" id="order_basic_info_product_name_{id}">{product_name}</span>'
						+ '			<!--span style="display:inline;" id="order_basic_info_recipe_state_{id}">主 砂</span>&nbsp;--><span class="order_basic_info_plan_time" id="order_basic_info_plan_time_{id}"">{plan_time}</span>&nbsp;|&nbsp;<span class="order_basic_info_unload_way_name" id="order_basic_info_unload_way_name_{id}"">{p_unload_way_name}</span>'
						+ '		</span>'
						+ '		<span class="onePlanQuantityCls"><em class="em_data one_fn_planQuantity">{plan_number}</em></span>'
						+ orderSCFLHtml
						+ '		<span class="oneBillNumCls" style="left:' + (360 - dedutSCFLWidth) + 'px;"><em class="em_data one_fn_billNum">{bill_num}</em></span>'
						+ '		<span class="oneCarNumCls" style="left:' + (425 - dedutSCFLWidth) + 'px;"><em class="em_data one_fn_carNum">{car_num}</em></span>'
						+ '</p>'
						+ '<div class="taskReady" style="left:' + (465 - dedutSCFLWidth) + 'px;">'
						+ '</div>'
						+ '<div class="taskWorking" style="left:' + (505 - dedutSCFLWidth) + 'px;">'
						+ '</div>'
						+ '<div class="taskOut" style="left:' + (545 - dedutSCFLWidth) + 'px; width:' + (75 + dedutSCFLWidth) + 'px;">'
						+ '</div>'
					+ '</div>',
	invoiceVehicle : '<span id="invoice_{id}" class="vehicle" data-order_id="{p_order_id}" data-mgr_station_id="{mgr_station_id}" data-bill_num="{bill_num}" data-v_id="{v_vehicle_id}" data-v_no="{v_no}" data-invoice_id="{id}" data-i_status="{i_status}" data-order_info="{titleInfo}" title="{titleInfo}"><b class="v_no">{v_no}</b></span>'	
};

$.fn.extend({
	"liveHoverClass" : function(classN) {
		var _self = $(this);
		_self.live('mouseover', function() {
			$(this).addClass(classN);
		}).live('mouseout', function() {
			$(this).removeClass(classN);
		});
	},
	"bindQTip" : function() {
		$(this).qtip({
			content: {
				attr: "title",
				title: "提示信息"
			}, 	
			style: {
				width: 200,
				classes: 'ui-tooltip-red ui-tooltip-shadow ui-tooltip-rounded'
			}
		});
	},
	"tagShowInfo" : function(tag, tagCN, thisCN) {
		var _self = $(this);
		var tag = $(tag);
		_self.hover(function() {
			$(tag).addClass('showTruckAll');
			$(this).addClass('span_showAll');
		}, function() {
			tag.removeClass('showTruckAll');
			$(this).removeClass('span_showAll');
		});
	}
});

var $DispatchUtil = {
	overOrSpanAll : function() {
		if ($('#span_over_span_All').hasClass("span_over_spanMin")) {
			$('#span_over_span_All').removeClass("span_over_spanMin");
			$('#span_over_span_All').addClass("span_over_spanMax");
		} else {
			$('#span_over_span_All').removeClass("span_over_spanMax");
			$('#span_over_span_All').addClass("span_over_spanMin");
		}
		$('.one_project').each(function() {
			if ($('#span_over_span_All').hasClass("span_over_spanMin")) {
				$('#one_project_' + this.id.replace("one_project_","") * 1).find('.orderList').removeClass("hideClass");
				
				$('#span_over_span_' + this.id.replace("one_project_","") * 1).removeClass("span_over_spanMax");
				$('#span_over_span_' + this.id.replace("one_project_","") * 1).addClass("span_over_spanMin");
				
			} else {
				$('#one_project_' + this.id.replace("one_project_","") * 1).find('.orderList').addClass("hideClass");
				
				$('#span_over_span_' + this.id.replace("one_project_","") * 1).removeClass("span_over_spanMin");
				$('#span_over_span_' + this.id.replace("one_project_","") * 1).addClass("span_over_spanMax");
			}
		});
		return false;
	},
	overOrSpan : function(projectId) {
		$('#one_project_' + projectId).find('.orderList').toggleClass("hideClass");
		if ($('#span_over_span_' + projectId).hasClass("span_over_spanMin")) {
			$('#span_over_span_' + projectId).removeClass("span_over_spanMin");
			$('#span_over_span_' + projectId).addClass("span_over_spanMax");
		} else {
			$('#span_over_span_' + projectId).removeClass("span_over_spanMax");
			$('#span_over_span_' + projectId).addClass("span_over_spanMin");
		}
		return false;
	},
	getSelectedVehicle : function(suitableVNo) {
		var selected = $('#enableVehicleTD  .selectVehicle');
		// 如果没有已经选择的车辆，并且黑白名单没有指定车辆，就选最后的车
		if (!selected.length && suitableVNo == "") {
			selected = $('#enableVehicleTD .vehicle:last').addClass('selectVehicle');
		} else if (!selected.length && suitableVNo != "") {
			var childVehicle = $('#enableVehicleTD').find(".vehicle");
            var total = childVehicle.length;
			$('#enableVehicleTD .vehicle').each(function(i) {
				//需要倒叙处理
				var thisVehicle = $('#enableVehicleTD .vehicle').eq((total-1)-i)
				if (suitableVNo.indexOf(",W") >= 0) { //白名单
					if (suitableVNo.indexOf(",W" + thisVehicle.attr("data-v_no") + ",") >= 0) {
						selected = thisVehicle.addClass('selectVehicle');
						return false;
					}
				} else if(suitableVNo.indexOf(",B") >= 0) { // 黑名单
					if (suitableVNo.indexOf(",B" + thisVehicle.attr("data-v_no") + ",") < 0) {
						selected = thisVehicle.addClass('selectVehicle');
						return false;
					}
				}
			});
		}
		$('#voice_vehicle').val($(selected).find('.v_no').text());
		return selected;
	}, 
	doVehicleBack : function() {
		// 将车辆回站
		funcVehicleBack($('#voice_vehicle').val(), 1);
			
	},
	refreshVehicle : function() {
		// 刷新排队车辆
		$.reqUrl("/listVehicleSequence", null, 
			function(rst) {
				if (rst.state) {
					var selected = $('#enableVehicleTD .selectVehicle');
					$('#enableVehicleTD').empty();
					var vehicleSequenceList = rst.data.VehicleSequenceList;
					if (vehicleSequenceList && vehicleSequenceList.length) {
						var vNo = selected.length ? selected.attr("data-v_no"): '';
						for ( var i = 0; i < vehicleSequenceList.length; i++) {
							var v = vehicleSequenceList[i];
							if (v.s_status == 1) {
								$('#enableVehicleTD').append($util.format(template.vehicleTDInner,v));
							}
						}
						if (vNo)
							$('#enableVehicleTD .vehicle[data-v_no='+ vNo + ']').addClass("selectVehicle");
					}
					
					$('#disableVehicleTD').empty();
					if (vehicleSequenceList && vehicleSequenceList.length) {
						for ( var i = 0; i < vehicleSequenceList.length; i++) {
							var v = vehicleSequenceList[i];
							if (v.s_status == 99) {
								$('#disableVehicleTD').append($util.format(template.vehicleTDInner,v));
							}
						}
						$('.vehicle').liveHoverClass('vehicleMouseOver');
					}
				}
		});
	},
	refreshReadyInvoice : function() {
		// 刷新准备任务(发货单)
		$.reqUrl("/listReadyInvoice", {
				i_status_little_than : 2, needPagin : 0
			}, 
			function(rst) {
				if (rst.state) {
					//先清空各个站点的准备任务，再填充
					$('.readyInvoiceTD').each(function() {
						$(this).empty();
					});
					
					var readyInvoiceData = rst.data;
					//准备任务
					var readyInvoiceList = readyInvoiceData.readyInvoice.list;
					if (readyInvoiceList && readyInvoiceList.length) {
						for (var i = 0; i < readyInvoiceList.length; i++) {
							var invoice = readyInvoiceList[i];
							
							//排除正在生产的任务
							if (curProduceProjectShortNameAndVehicleNos.indexOf("," + invoice.project_short_name + invoice.v_no + ",") != -1) {
								continue;
							}
							
							$('#readyInvoiceTD' + invoice.mgr_station_id).append(
								$util.format(template.readyInvoiceTDInner, invoice)
							);
						}
						
						$('.ready_invoice').liveHoverClass('ready_invoice_mouseover');
					}
				}
			}
		);
	},
	refreshWorkingInvoice : function() {
		// 刷新搅拌工控机的链接状态、正在生产任务
		curProduceProjectShortNameAndVehicleNos = ",";
		
		for ( var i = 0; i < mgrStations.length; i++) {
			$.reqUrl("/listWorkingInvoice", {mgrStationId: mgrStations[i].id}, 
				function(rst) {
					if (rst.state) {
						var producingSmallJob = rst.data;
						if (producingSmallJob) {
						
							$("#produceProgressBar" + producingSmallJob.stationId).addClass("hide");
							$("#stationName" + producingSmallJob.stationId).removeClass("stationUnLinked");
							$("#produceStatusName" + producingSmallJob.stationId).removeClass("stationUnLinked");
							$("#produceProject" + producingSmallJob.stationId).html(producingSmallJob.bigJobName + "<span class='vehicle workingInvoiceInStation'><b>" + (null != producingSmallJob.vehicleNo ? producingSmallJob.vehicleNo : "") + "</b></span>");
							
							if (producingSmallJob.status == 9) {
								$("#stationName" + producingSmallJob.stationId).addClass("stationUnLinked");
								$("#produceStatusName" + producingSmallJob.stationId).addClass("stationUnLinked");
							} else if (producingSmallJob.status == 1) {
								$("#produceProgressBar" + producingSmallJob.stationId).removeClass("hide");
								$("#produceProgressBarTotalNum" + producingSmallJob.stationId).attr("class","produceTotal produceTotal_" + producingSmallJob.totalNum);
								$("#produceProgressBarNum" + producingSmallJob.stationId).attr("class","producedProgress producedProgress_" + producingSmallJob.num);
								
								//正在生产的工程名称和车号
								curProduceProjectShortNameAndVehicleNos += (producingSmallJob.bigJobName + producingSmallJob.vehicleNo + ",");
							}
							
							//排除正在打料的车辆
							$('.ready_invoice').each(function() {
								var vNo = $(this).attr("data-v_no");
								var projectShortName = $(this).attr("data-project_short_name");
								if (curProduceProjectShortNameAndVehicleNos.indexOf("," + projectShortName + vNo + ",") != -1) {
									$(this).remove();
								}
							});
						}
					}
				}
			);
		}
	},
	refreshOrders : function() {
		$.reqUrl("/listDispatchProjectAndOrders",
						function(rst) {
							if (rst.state) {
								var projects = rst.data.projectList;
								// 处理工程信息
								if (projects && projects.length) {
									// 首先把最底部的把页面撑高的dl去掉
									$("#one_project_null").remove();
									for ( var i = 0; i < projects.length; i++) {
										var project = projects[i];
										var pBox = $('#one_project_' + project.id);
										if (pBox.length) {
											//工程已经存在可能要修改一些东西
											
										} else {
											//工程不存在
											$('#projectList').append($util.format(template.projectListInner, project));
										}
									}
									
									// 删除已经结束的订单对应的工程
									if ($('#projectList').find('.one_project').length != projects.length) {
										$('#projectList').find('.one_project').each(
											function() {
												var projectId = this.id.replace("one_project_","") * 1;
												var exist = false;
												for ( var i = 0; i < projects.length; i++) {
													if (projects[i].id == projectId) {
														exist = true;
													}
												}
												if (!exist)
													$(this).remove();
											}
										);
									}
									// 最后把最底部的把页面撑高的dl加上
									$('#projectList').append($util.format(template.projectListAddtional, projects));
								} else {
									$('#projectList').empty();
								}
								// 处理订单信息
								var orders = rst.data.orderList;
								var dispatchTipInfo = "";
								if (orders && orders.length) {
									for ( var i = 0; i < orders.length; i++) {
										var order = orders[i];
										var orderBox = $('#order_'+ order.id);

										if (orderBox.length) {
											// 是否暂停
											orderBox.removeClass("pauseClass");											
											// 更新浇筑信息、配方信息等
											$("#order_basic_info_work_part_" + order.id).html(order.work_part);
											$("#order_basic_info_product_name_" + order.id).html(order.product_name);
											$("#order_basic_info_unload_way_name_" + order.id).html(order.p_unload_way_name);
											var titleChange = $('<div/>').html(order.o_title).text();
											$("#order_basic_info_" + order.id).attr("title", titleChange);
											$("#order_basic_info_plan_time_" + order.id).html(order.plan_time);
											// 更新计划、已打、已签等信息
											orderBox.find('.one_fn_planQuantity').text(order.plan_number);
											orderBox.find('.one_fn_finishQuantity').text(order.product_num);
											orderBox.find('.one_fn_billNum').text(order.bill_num);
											orderBox.find('.one_fn_carNum').text(order.car_num);											
											//data-xxxxxx数据处理
											orderBox.attr("data-fix_station", order.fix_station);
											orderBox.attr("data-s_project_id", order.s_project_id);
											orderBox.attr("data-s_customer_id", order.s_customer_id);
											orderBox.attr("data-l_product_id", order.l_product_id);
											orderBox.attr("data-p_unload_way_id", order.p_unload_way_id);
											orderBox.attr("data-work_part", order.work_part);
											orderBox.attr("data-total_car", order.total_car);
											orderBox.attr("data-p_name", order.s_project_name);
											orderBox.attr("data-product_name", order.product_name);
											orderBox.attr("data-dispatch_classify", order.dispatch_classify);
										} else {
											$("#one_project_"+order.s_project_id).find('.orderList').append($util.format(template.orderListInner, order));
											//----------------------右键订单 开始----------------------------------------
											$("#order_"+order.id).contextMenu('orderMenu', 
											{
												bindings: 
												{
													'orderMenuModifyOrder': function(t) {
														var orderId = $('#' + t.id).attr("data-order_id");
														funcModifyOrder(orderId);
											        },
											        'orderMenuSetVehicle': function(t) {
														var orderId = $('#' + t.id).attr("data-order_id");
														funcSetVehicle(orderId);
											        },
											        'orderMenuViewOrderInvoice': function(t) {
														var orderId = $('#' + t.id).attr("data-order_id");
														funcViewOrderInvoice(orderId);
											        },
											        'orderMenuViewAllInvoice': function(t) {
														funcViewOrderInvoice("");
											        },
											        'orderMenuFinishOrder': function(t) {
														var order_id = $('#' + t.id).attr("data-order_id");
														var p_name = $('#' + t.id).attr("data-p_name");
														var product_name = $('#' + t.id).attr("data-product_name");
														var work_part = $('#' + t.id).attr("data-work_part");
														
														myFuncFinishOrder(order_id, p_name, product_name, work_part);
											        }
												}
											});
											//----------------------右键订单 结束----------------------------------------
											
											
										}
										if (order.o_status == 8) {
											$('#order_'+ order.id).addClass("pauseClass");
										}
										
										//已经达到或者即将达到开盘时间										
										if ((order.isNeedKp.indexOf("-") >= 0 || order.isNeedKp <= "00:20:") && order.bill_num == 0 ) {
											dispatchTipInfo += "[" + order.s_project_name + "-" + order.work_part + "]";
										}
										
									}
									// 删除已经结束的订单
									if ($('#projectList').find('.div_order').length != orders.length) {
										$('#projectList').find('.div_order').each(
											function() {
												var orderId = this.id.replace("order_","") * 1;
												var exist = false;
												for ( var i = 0; i < orders.length; i++) {
													if (orders[i].id == orderId) {
														exist = true;
														break;
													}
												}
												if (!exist)$(this).remove();
											}
										);
									}
									
									dispatchInfoTipCount += 1;
									if (dispatchTipInfo != "" && dispatchInfoTipCount >= 10) {
										parent.showDispatchInfo(dispatchTipInfo);
										dispatchInfoTipCount = 1;
									}
									
								} else {
									$('#projectList').find('.orderList').empty();
								}
								// 刷新生产单
								var invoices = rst.data.invoiceList;
								$('#projectList').find('.taskReady').empty();
								$('#projectList').find('.taskWorking').empty();
								$('#projectList').find('.taskOut').empty();
								if (invoices && invoices.length) {
									for ( var i = 0; i < invoices.length; i++) {
										var curAddInvoice = null;
										if (invoices[i].i_status == 0 || invoices[i].i_status == 1) {
											$('#order_' + invoices[i].p_order_id).find('.taskReady').append($util.format(template.invoiceVehicle, invoices[i]));
											curAddInvoice = $('#invoice_' + invoices[i].id);
											curAddInvoice.addClass("readyInvoiceInTask");
										}
										if (invoices[i].i_status == 2) {
											$('#order_' + invoices[i].p_order_id).find('.taskWorking').append($util.format(template.invoiceVehicle, invoices[i]));
											curAddInvoice = $('#invoice_' + invoices[i].id);
											curAddInvoice.addClass("workingInvoiceInTask");
										}
										if (invoices[i].i_status == 3) {
											//因为显示太多，会将调度界面撑大，出站车辆最多显示6车
											var hasDisplayOutVehicleNum = 0;
											$('#order_' + invoices[i].p_order_id).find('.taskOut').find('.vehicle').each(function() {
												hasDisplayOutVehicleNum ++;
											});
											if (hasDisplayOutVehicleNum >= maxDisplayOutVNum) {
												continue;
											}
											
											$('#order_' + invoices[i].p_order_id).find('.taskOut').append($util.format(template.invoiceVehicle, invoices[i]));
											curAddInvoice = $('#invoice_' + invoices[i].id);
											curAddInvoice.addClass("outInvoiceInTask");
										}
										
										//----------------------右键发货单 开始----------------------------------------
										if (null != curAddInvoice) {
											curAddInvoice.contextMenu('invoiceMenu', 
											{
												bindings: 
												{
													'invoiceMenuVehicleBack': function(t) {
														var invoiceForMenu = $('#' + t.id);
														
														if (invoiceForMenu.attr("data-i_status") == 0) {
															layer.alert("该生产任务尚未启动生产，如需车辆回站，请直接取消该生产任务！");
															return false;
														}
													
														funcVehicleBack(invoiceForMenu.attr("data-v_no"), 1);
											        },
											        'invoiceMenuVehicleChangeOrder': function(t) {
														var invoiceForChangeOrder = $('#' + t.id);
														funcVehicleChangeOrder(invoiceForChangeOrder.attr("data-invoice_id"), invoiceForChangeOrder.attr("data-i_status"));
											        },
											        'invoiceMenuVehicleChangeVehicle': function(t) {
											        	var invoiceForChangeVehicle = $('#' + t.id);
											        	funcVehicleChangeVehicle(invoiceForChangeVehicle.attr("data-v_id"), invoiceForChangeVehicle.attr("data-v_no"), invoiceForChangeVehicle.attr("data-invoice_id"));
											        },
											        'invoiceMenuVehicleAddConcrete': function(t) {
											        	var invoiceForAddConcrete = $('#' + t.id);
											        	showDVDispatch("order_" + invoiceForAddConcrete.attr("data-order_id"), invoiceForAddConcrete);
											        }
												}
											});
										}
										//----------------------右键发货单 结束----------------------------------------
										
									}
								}
		
								//刷新统计数据
								updateProjectAndTotalNums();
								$DispatchUtil.adjustHeight();
							}
						});
	},
	adjustHeight : function() {
		//$('#projectList').height(windowHeight - 135);
		$('#projectList').height(windowHeight - $('#enableVehicleTD').height() - 85);
		$('#invoiceListTD').height(mainContentTDHeight);
		//$('#gridBox').height(mainContentTDHeight);
 		//$(".l-scroll").height(mainContentTDHeight - 90);
 		
		$('.div_order').each(function() {
			//获得最高高度
			var OMaxHeight = 0; 
			var OOldHeight = $(this).height();
			
			if ($(this).find('.order_info').height() == 0) {
				OMaxHeight = OOldHeight;
			}
			if ($(this).find('.order_info').height() > OMaxHeight) {
				OMaxHeight = $(this).find('.order_info').height() + 6;
			}
			if ($(this).find('.taskReady').height() > OMaxHeight) {
				OMaxHeight = $(this).find('.taskReady').height() + 6;
			}
			if ($(this).find('.taskWorking').height() > OMaxHeight) {
				OMaxHeight = $(this).find('.taskWorking').height() + 6;
			}
			if ($(this).find('.taskOut').height() > OMaxHeight) {
				OMaxHeight = $(this).find('.taskOut').height() + 6;
			}
			
			//设置div的高度
			$(this).height(OMaxHeight);
			$(this).find('.onePlanQuantityCls').height(OMaxHeight);
			$(this).find('.oneFinishQuantityCls').height(OMaxHeight);
			$(this).find('.oneBillNumCls').height(OMaxHeight);
			$(this).find('.oneCarNumCls').height(OMaxHeight);
		});
	}
};

$(function() {
	// -------------------------车辆选择 开始--------------------------------------------
	$(document).on('mousedown', '#enableVehicleTD .vehicle', function() {
		$(this).siblings('.vehicle').not(this).removeClass("selectVehicle");
		$(this).toggleClass('selectVehicle');
		$('#voice_vehicle').val($(this).find('.v_no').text());
	});

	$(document).on('mousedown', '#projectList .vehicle', function() {
		$('#projectList .vehicle').not(this).removeClass("selectVehicle");
		$(this).toggleClass('selectVehicle');
		$('#voice_vehicle').val($(this).find('.v_no').text());
	});
	
	// -------------------------车辆选择 结束--------------------------------------------
	
	
	// ----------------------订单显示方式（全部、已开盘、未开盘）选择 开始---------------------	
	$("input[name=OrderDisplayRadio]").click(function(){
		$(".OrderDisplayLabelCls").removeClass("selText");
		$("#" + this.id + "Label").addClass("selText");
	});
	// ----------------------订单显示方式（全部、已开盘、未开盘）选择 结束---------------------	
 
	
	// -------------------------站点选择 开始--------------------------------------------
	$(document).on('click','.stationForVoice',function(){
		$('#voice_station').val($(this).find('.stationIdCls').text());
	});
	
	$(document).on('click','.stationForDV',function(){
		$(".dVStationText").removeClass("dVStationSelText");
		$(this).find('.dVStationText').addClass("dVStationSelText");
	});
	// -------------------------站点选择 结束--------------------------------------------
	

	// -------------------------车辆左移、车辆右移，车辆排序 开始 ---------------------------------------------
	$('#vehicleToStop, #vehicleToRun').click(function(){
		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}
		if (this.id == "vehicleToStop") { 
			$util.sure("您确定要设置全部车辆到非运营状态吗！未回站车辆会被强制回站！", function() {
				$.reqUrl("/setAllUnUse", {vno:null}, function(rst) {
					layer.msg('系统处理成功！', 2, 1);
					if(rst.state) {
						$DispatchUtil.refreshVehicle();	
					}
				});
			});
		} else {
        	$util.sure("您确定要设置全部车辆到运营状态吗！未回站车辆会被强制回站！", function() {
				$.reqUrl("/setAllUse", {vno:null}, function(rst) {
					layer.msg('系统处理成功！', 2, 1);
					if(rst.state) {
						$DispatchUtil.refreshVehicle();	
					}
				});
			});
		}	
	});
	
	$('#vehicleOrder').click(function(){
		openAddWindow("车辆重新排队", "/toReOrderVehicle", 420, 250);	
	});
	// -------------------------车辆左移、车辆右移，车辆排序  结束 ---------------------------------------------

	// -------------------------车辆拖动 开始--------------------------------------------
	$("#disableVehicleTD, #enableVehicleTD").dragsort({
		dragSelector : ".vehicle",
		dragBetween : true,
		dragEnd : function() {
			var isUse = $(this).parents('#enableVehicleTD').length ? true : false;
			var preVNo = $(this).next('.vehicle').find('.v_no');
			preVNo = preVNo.length ? preVNo.text() : 'VNO_1';
			var params = {
				vNo : $(this).attr("data-v_no"),
				preVNo : preVNo,
				isUse : isUse
			};
			
			$.reqUrl($.url('/changeVehicleSequenceAndIsUse'),
					params, function(rst) {
						if (rst.state) {
							$DispatchUtil.refreshVehicle();	
							$.submitShowResult(rst);
							return true;
						}
					});
		},
		placeHolderTemplate : "<span class='vehicle dragingVehicle_ready'></span>"
	});
	
	//双击时，将运营车辆设为非运营，将非运营设置为运营
	$(document).on('dblclick', '#enableVehicleTD .vehicle, #disableVehicleTD .vehicle', function() {
			if (!hasDDCZPermission) {
				layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
				return;
			}
	
			//isUse true : 双击的是左边(运营车辆)的车辆
			$.submitShowWaiting();
			var isUse = $(this).parents('#enableVehicleTD').length ? true : false;
			var preVNo = "VNO_1";
			if (!isUse) {
				//双击右边的，才需要排队。
				preVNo = $('#enableVehicleTD .vehicle:first').data() ? $('#enableVehicleTD .vehicle:first').attr("data-v_no") : 'VNO_1';
			}
			
			var params = {
				vNo : $(this).attr("data-v_no"),
				preVNo : preVNo,
				isUse : !isUse
			};
			$.reqUrl($.url('/changeVehicleSequenceAndIsUse'),
					params, function(rst) {
						if (rst.state) {
							$DispatchUtil.refreshVehicle();	
							$.submitShowResult(rst);
							return true;
						}
					});
	});
	// -------------------------车辆拖动 结束--------------------------------------------
	
	// -------------------------工程拖动 开始--------------------------------------------
	$(".projectListBox").dragsort({
		dragSelector : ".one_project",
		dragSelectorExclude : 'dd',
		dragBetween : true,
		dragEnd: function () {
		
			if (!hasDDCZPermission) {
				layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
				return;
			}
		
			var nextProjectID = 'PROJECTID_LAST';;
			if ($(this).next('.one_project').length) {
				nextProjectID = $(this).next('.one_project').attr("id");
				nextProjectID = nextProjectID.replace("one_project_", "");
			}
			var projectID = $(this).attr("id").replace("one_project_", "");
			
			var params = {
				projectID : projectID,
				nextProjectID : nextProjectID
			};
			
			$.reqUrl($.url('/changeProjectDispatchOrder'),
					params, function(rst) {
						if (rst.state) {
							return true;
						}
					});
			//$ddu.autotaskH();
		},
		placeHolderTemplate : "<dl class='one_project dragingProject'></dl>"
	});
	// -------------------------工程拖动 结束--------------------------------------------
	
	// -------------------------准备任务 开始--------------------------------------------
	$('.readyInvoiceTD').dragsort({
		dragSelector : ".ready_invoice",
		dragBetween : true,
		dragEnd : function() {
		
			if (!hasDDCZPermission) {
				layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
				return;
			}
		
			var $this = $(this);
			var oldStationId = $this.attr("data-mgr_station_id");
			var newStationId = $this.parents(".readyInvoiceTD").attr("id").replace("readyInvoiceTD", "");
			if (oldStationId != newStationId) {
				$.reqUrl("/changeInvoiceStation", {
						id : $this.attr("data-id"),
						oldStationId : oldStationId,
						newStationId : newStationId
					}, function(rst) {
						//重新加载准备任务
						$DispatchUtil.refreshReadyInvoice();
						//刷新列表
						$grid.load("gridBox");
						
						if (rst.state) {
							layer.msg('更换生产线成功！', 2, 1);
						} else {
							layer.alert(rst.msg);
						}
					}
				);
			}
		},
		placeHolderTemplate : "<span class='ready_invoice dragingReady_invoice'></span>"
	});
	
	//准备任务取消
	$(document).on('dblclick', '.ready_invoice', function() {

		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}

		var $this = $(this);
		$.reqUrlEx("/cancelInvoice", {
			id : $(this).attr("data-id"), mgrStationId : $(this).attr("data-mgr_station_id") 
			}, function(rst) {
				if (rst.state) {
					RefreshDispatchPageInfo();
					//刷新列表
					$grid.load("gridBox");
				}
			}, "确定要取消<b>&nbsp;&nbsp;" + $(this).attr("data-v_no") + "&nbsp;&nbsp;</b>号车生产吗？<br/>如果机楼操作员已经启动生产，请联系机楼操作员从工控系统取消！");
	});
	// -------------------------准备任务 结束--------------------------------------------
	
	// -------------------------订单双击、单击 开始--------------------------------------------
	$(document).on('dblclick', '.div_order', function() {
		//双击调度
		showDVDispatch(this.id);
	});
	
	$(document).on('mousedown', '.div_order:not(.pauseClass)', function() {
		$('.div_order').removeClass('div_order_Selected');
		$(this).addClass('div_order_Selected');
	});
	// -------------------------订单双击、单击 结束--------------------------------------------


	// -------------------------设置开盘订单 开始 ---------------------------------------------
	$('#setDisplay').click(function(){
	
			if (!hasDDCZPermission) {
				layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
				return;
			}
	
			$.reqUrl("/listDispatchOrNotDispatchOrder", null, function(rst) {
				if (rst.state) {
					var orders = rst.data;
					if (orders && orders.length) {
						var allOrder = "";
						for ( var i = 0; i < orders.length; i++) {
							var info = orders[i];
							allOrder += '<tr style="border-bottom:1px solid #c0ced9;">' 
								+ '<td width=210><input class="setdisplay_all_orders" type="checkbox" ' + (info.is_dispatch == '1' ? 'checked' : '') + ' style="vertical-align:middle;" id="' + info.id + '" value="'+ info.id+ '"><label style="vertical-align:middle;cursor:hand;cursor:pointer;font-size:13px;width:200px;" for="' + info.id + '">' + info.s_project_name + '</label></td>' 
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.product_name + '</label></td>'
								+ '<td><select class="sel dispatch_classify"  style="font-size:14px;font-weight:bold;color:blue;" name="dispatch_classify" id="dispatch_classify_' + info.id + '">' 
									+ '<option value=0 ' + (info.dispatch_classify == '0' ? 'selected' : '') + '>不指定</option>'
									+ '<option value=1 ' + (info.dispatch_classify == '1' ? 'selected' : '') + '>1级</option>'
									+ '<option value=2 ' + (info.dispatch_classify == '2' ? 'selected' : '') + '>2级</option>'
									+ '<option value=3 ' + (info.dispatch_classify == '3' ? 'selected' : '') + '>3级</option>'
									+ '<option value=4 ' + (info.dispatch_classify == '4' ? 'selected' : '') + '>4级</option>'
									+ '<option value=5 ' + (info.dispatch_classify == '5' ? 'selected' : '') + '>5级</option>'
								+ '</select></td>'
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.work_part + '</label></td>'
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.p_unload_way_name + '</label></td>'
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.plan_number + '</label></td>'
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.bill_num + '</label></td>'
								+ '<td><label style="vertical-align:middle;height:22px;line-height:22px;cursor:hand;cursor:pointer;font-size:13px;" for="' + info.id + '">' + info.plan_time
								+ "</label></td></tr>"; 	
						}
						
						$('#SOIDAllOrderListTable').html(allOrder); 
					}
				}
			});
	
			DVSubDialog = $.ligerDialog.open({ title:'设置当前显示订单', target: $("#SetOrderIsDispatch"), width:980, height:550 });
			return false;
	});
	$("#setDisplayCheckBoxAll").live('click', function () {
		if($("#setDisplayCheckBoxAll").attr("checked")=="checked") {
			$(".setdisplay_all_orders").attr("checked", true);
		} else {
			$(".setdisplay_all_orders").attr("checked", false);
		}
	});
	// -------------------------设置开盘订单 结束 ---------------------------------------------
	
	// -------------------------展开收起工程 开始-------------------------------------------------
	$(document).on('dblclick', '.projectInfo', function() {
		$DispatchUtil.overOrSpan(this.id.replace("projectInfo_","") * 1);
	});
	$(document).on('click', '.one_project_over_span .span_over_span', function() {
		$DispatchUtil.overOrSpan(this.id.replace("span_over_span_","") * 1);
	});
	$('#span_over_span_All').live('click',function() {$DispatchUtil.overOrSpanAll();});
	$('#span_over_span_invoiceListTD').live('click',function() {$DispatchUtil.overOrSpanInvoiceListTD();});
	// -------------------------展开收起工程 结束-------------------------------------------------
	
	//---------------------将车辆回站 开始----------------------------------------------
	$("#vehicleBack").click(function(){
		if ($('#voice_vehicle').val() == "") {
			$util.sure("您未指定车辆，是否将所有车辆回站？", function() {
				$.reqUrl("/setAllUseExceptFyy", {vno:null}, function(rst) {
					layer.msg('系统处理成功！', 2, 1);
					if(rst.state) {
						$DispatchUtil.refreshVehicle();	
					}
				});
			});
		} else {
			$DispatchUtil.doVehicleBack();
		}
	});
	//---------------------将车辆回站 结束----------------------------------------------
	
	//---------------------车辆日月报表 开始--------------------------------------------
	$("#vehicleReport").click(function(){
		commonOpenDialog("生产报表", "", 800, 500);
	});
	//---------------------车辆日月报表 结束--------------------------------------------
	
	//---------------------过滤订单 开始-----------------------------------------------
	$(".dengjiClick").click(function(){
		var radioVal = this.id.replace("dengjiClick", "");
		$(".dengjiClick").removeClass("current");
		$(this).addClass("current");
		
		$('.div_order').each(function() {
			//先隐藏自己和工程
			$(this).addClass("hide");
			$("#one_project_" + $(this).attr("data-s_project_id")).addClass("hide");
			if (radioVal == $(this).attr("data-dispatch_classify") || radioVal == "0"){
				$(this).removeClass("hide");
			} 		
		});
		
		//把工程放开（设置为不隐藏）
		$('.div_order').each(function() {
			if (!$(this).hasClass("hide")){
				$("#one_project_" + $(this).attr("data-s_project_id")).removeClass("hide");
			} 		
		});
	});
	//---------------------过滤订单 结束-----------------------------------------------
	
	//----------------------方量计算 开始------------------------------------------
	$("#dVProduceCNum").bind("keyup",function(){
		calDVBillNum();
	});
	$("#dVProduceMNum").bind("keyup",function(){
		calDVBillNum();
	});
	$("#dVXfNum").bind("keyup",function(){
		calDVBillNum();
	});
	//是否显示“车内剩料时清空本车对应的上车发货单数据”
	$('#dVInNum').bind("keyup",function(){
		if ($("#dVInNum").val() > 0) {
			$("#dVRemainP").removeClass("hide");
		} else {
			$("#dVRemainP").addClass("hide");
		}
		calDVBillNum();
	});
	//方量等只能输入数字和小数点
	$(".number").keypress(function(event) {
		var keyCode = event.which;
        if (!$.browser.msie && (event.keyCode == 0x8))  //火狐下 不能使用退格键  
        {
			return;
        }
		if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))
			return true;
		else
			return false;
	}).focus(function() {
		this.style.imeMode='disabled';
	});
	//----------------------方量计算 结束------------------------------------------
	
	//----------------------车辆更换联动司机 结束----------------------------------------
	$('#cVNewVehicle').live("change", (function() {
		//联动司机
		funcGetDriversByVId(this.value, "cVNewDriverId");
	}));
	//----------------------车辆更换联动司机 结束----------------------------------------
	
	
	//----------------------一些初始化工作 开始------------------------------------
	RefreshDispatchPageInfo();
	
	$("#dVQserName").click(function(){
		openSelectDialog("质检员", "user", "84", 650, 500, false);
	});
	
	$('.div_order').liveHoverClass('div_order_mouseover');
	$('.one_project').liveHoverClass('one_project_mouseover');
	
	
	$(window).resize(function() {
		windowHeight = $(window).height();
		windowWidth  = $(window).width();
		invoiceListTDWidth = windowWidth - 670;
		mainContentTDHeight = windowHeight - $(".table_dispatch_station").height() - 34;
		$DispatchUtil.adjustHeight();
	});
	
	
	//自动刷新车辆、准备任务、正在生产任务、订单等
	setInterval($DispatchUtil.refreshVehicle, 20000);
	setInterval($DispatchUtil.refreshReadyInvoice, 20000);
	setInterval($DispatchUtil.refreshWorkingInvoice, 20000);
	setInterval($DispatchUtil.refreshOrders, 20000);
	
	//----------------------一些初始化工作 结束------------------------------------
});

//提供给其他程序刷新车辆排队的，比如车辆重新排队
function RefreshDispatchPageInfo() {
	//加载车辆信息，并默认选中最先回站的一个车
	$DispatchUtil.refreshVehicle();
	//加载订单信息
	$DispatchUtil.refreshOrders();
	//加载准备任务、正在生产任务信息
	$DispatchUtil.refreshWorkingInvoice();
	$DispatchUtil.refreshReadyInvoice();
	$DispatchUtil.adjustHeight();
}

// 更新工程的总计划、票面、生产、车数
function updateProjectAndTotalNums() {

	$(".fn_planQuantity").html("0");
	$(".fn_finishQuantity").html("0");
	$(".fn_billNum").html("0");
	$(".fn_carNum").html("0");
	
	$("#totalPlanQuantity").html("0");
	$("#totalFinishQuantity").html("0");
	$("#totalBillNum").html("0");
	$("#totalCarNum").html("0");
	
	$("#totalReadyInvoiceNum").html($('.readyInvoiceInTask').size());
	$("#totalWorkingInvoiceNum").html($('.workingInvoiceInTask').size());
	$("#totalOutInvoiceNum").html($('.outInvoiceInTask').size());
	$("#totalOrder").html($('.div_order').size());

	$('.div_order').each(
		function() {
			var projectObj = $("#projectInfo_" + $(this).attr("data-s_project_id")).find(".fn_planQuantity");
			projectObj.text((parseFloat(projectObj.text()) + parseFloat($(this).find(".one_fn_planQuantity").text())).toFixed(1));
			
			projectObj = $("#projectInfo_" + $(this).attr("data-s_project_id")).find(".fn_finishQuantity");
			projectObj.html((parseFloat(projectObj.text()) + parseFloat($(this).find(".one_fn_finishQuantity").text())).toFixed(1));
			
			projectObj = $("#projectInfo_" + $(this).attr("data-s_project_id")).find(".fn_billNum");
			projectObj.text((parseFloat(projectObj.text()) + parseFloat($(this).find(".one_fn_billNum").text())).toFixed(1));
			
			projectObj = $("#projectInfo_" + $(this).attr("data-s_project_id")).find(".fn_carNum");
			projectObj.text((parseFloat(projectObj.text()) + parseFloat($(this).find(".one_fn_carNum").text())).toFixed(0));
			
			$("#totalPlanQuantity").html((parseFloat($("#totalPlanQuantity").html()) + parseFloat($(this).find(".one_fn_planQuantity").text())).toFixed(1));
			$("#totalFinishQuantity").html((parseFloat($("#totalFinishQuantity").html()) + parseFloat($(this).find(".one_fn_finishQuantity").text())).toFixed(1));
			$("#totalBillNum").html((parseFloat($("#totalBillNum").html()) + parseFloat($(this).find(".one_fn_billNum").text())).toFixed(1));
			$("#totalCarNum").html((parseFloat($("#totalCarNum").html()) + parseFloat($(this).find(".one_fn_carNum").text())).toFixed(0));
		}
	);
}

// 计算票面方量
function calDVBillNum() {
	var amount=0;
	amount= $("#dVProduceCNum").val()-(-$("#dVInNum").val())-(-$("#dVProduceMNum").val())-(-$("#dVXfNum").val());
	$('#dVBillNum').val(amount);
}

//修改订单
function funcModifyOrder(orderId) {
	if (!hasDDCZPermission) {
		layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
		return;
	}
	editRow("修改订单", "/toEditOrder?id=" + orderId, 1000, 550);
}
//指定车辆
function funcSetVehicle(orderId) {
	if (!hasDDCZPermission) {
		layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
		return;
	}
	editRow("车辆指定", "/common/vehicleSelect?id=" + orderId, 600, 400);
}


//获得配比匹配情况，并填充到调度界面
function funcGetRecipeMatching(order_id) {
	//先清空显示信息
	for ( var i = 0; i < mgrStations.length; i++) {
		$("#stationRecipe" + mgrStations[i].id).html("");
	}
	
	$.reqUrl($.url('/listRecipeMatching'), {'order_id': order_id}, function(rst) {
			if (rst.state) {
				var recipeMapC = rst.data.recipeMapC;
				var recipeMapM = rst.data.recipeMapM;
				
				//主料和砂浆返回的数组长度肯定一致
				for(var key in recipeMapC) {
					var htmlVar = "";
					if(recipeMapC[key] == ""){
						htmlVar += "主<span class='span_pass'/>";
					}else{
						htmlVar += "主<span class='span_noPass a_tooltip' data-type='recipe'  data-id='"+recipeMapC[key]+"'></span>";	
					}
					
					if(recipeMapM[key] == ""){
						htmlVar += "&nbsp;砂<span class='span_pass'/>";
					}else{
						htmlVar += "&nbsp;砂<span class='span_noPass a_tooltip' data-type='recipe'  data-id='"+recipeMapM[key]+"'></span>";	
					}
					
					$("#stationRecipe" + key).html(htmlVar);
				}
				
				$(".span_noPass").each(function() {
					$(this).qtip({
						content: {
							text: $(this).attr("data-id"),
							title: "配方信息"
						}, 	
						style: {
							width: 240,
							classes: 'ui-tooltip-red ui-tooltip-shadow ui-tooltip-rounded'
						}
						
					});
				});
			}
		}
	);
}

//车辆回站公共函数
function funcVehicleBack(vNo, sStatus) {

		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}

		var params = {'vNo' : vNo, "sStatus" : sStatus};
		$.reqUrlEx("/changeVehicleStatus", params, 
			function(rst) {
				if (rst.state) {
					$DispatchUtil.refreshVehicle();
				}
			}, "您确定要将 " + vNo + " 号车回站吗？"
		);
}

//原车辆ID， 原车辆NO， 发货单ID
function funcVehicleChangeVehicle(vId, vNo, invoiceId) {

		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}

		$("#cVInvoiceId").val(invoiceId);
		$("#cVOldVehicle").html(vNo);
		$("#cVOldVehicleNo").val(vNo);
		
		var options = $("#enableVehicleTD").find('.vehicle').map(function(i, dom) {
							var selected = $(this).is(".selectVehicle") ? "selected": "";
							return '<option value="'+ $(dom).attr("data-v_id")+ '" '+ selected+ ' >'+ $(dom).attr("data-v_no") + '</option>';
						}).get().join('');
						
		$('#cVNewVehicle').empty().append(options);
		var vId = $('#cVNewVehicle').val();
		//联动司机
		funcGetDriversByVId(vId, "cVNewDriverId");
		
		DVSubDialog = $.ligerDialog.open({ title:'车辆更换', target: $("#ChangeVehicle"), width:400, height:250 });
		return false;
}


//改派 发货单ID，车号
function funcVehicleChangeOrder(invoiceId, invoiceStatus) {

		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}

		//如果发货单还未发送到搅拌楼生产，则要求用户直接取消生产，再改派
		if (invoiceStatus == 0) {
			layer.alert("该生产任务尚未启动生产，如需进行剩料与改派操作，请直接取消该生产任务！");
			return false;
		}
		
		subDialog = $.ligerDialog.open({title: '剩料与改派', url: '/toInvoiceReassignment?id=' + invoiceId, width : 900,height: 580,isResize: false,isHidden : false});
		
		return false;
}

function dVIsWaterChange(){
	if ($("#dVIsWater").attr("checked") == "checked") {
		$("#produceInfoDiv").removeClass("hide");
		$("#produceInfoDiv").addClass("hide");
	} else {
		$("#produceInfoDiv").removeClass("hide");
	}
}

function dispatchIsNaN(val) {
	if (typeof(val) == "undefined" || null == val || "" == val) {
		return true;
	}
	return isNaN(val);
}

// 图形调度打开
// 如果带了vehicleFromInvoice参数，表示是加料生产
function showDVDispatch(orderId, vehicleFromInvoice) {

	if (!hasDDCZPermission) {
		layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
		return;
	}

	// 当前选中的订单
	var selectedOrder = $('#' + orderId);
	
	if (selectedOrder.hasClass("pauseClass"))
	{
		layer.alert("当前订单已经被实验室暂停！");
		return false;
	}
	
	//工程信息
	$("#dVProjectName").html(selectedOrder.parents(".one_project").find(".projectNameCls").attr("title"));
	
	$("#dVSCustomerId").val(selectedOrder.attr("data-s_customer_id"));
	$("#dVSProjectId").val(selectedOrder.attr("data-s_project_id"));
	
	//订单信息
	$("#dVOrderName").html(selectedOrder.find(".order_basic_info").text());
	$("#dVOrderId").val(selectedOrder.attr("data-order_id"));
	$("#dVLProductId").val(selectedOrder.attr("data-l_product_id"));
	$("#dVPUnloadWayId").val(selectedOrder.attr("data-p_unload_way_id"));
	$("#dVWorkPart").val(selectedOrder.attr("data-work_part"));
	
	//是否送水，默认设置为否
	$("#dVIsWater").attr("checked",false);
	$("#produceInfoDiv").removeClass("hide");
	
	//车辆设置，如果是加料生产，则直接取传过来的车辆对象，如果不是，则取选中的车辆
	var selectedVehicle = vehicleFromInvoice;
	if (typeof(vehicleFromInvoice) === "undefined") {
		// 车辆需要根据黑白名单进行处理
		var suitableVNo = "";
		$.reqUrlSync("/getSuitableVehicle", {'order_id': selectedOrder.attr("data-order_id")}, function(rst){
			suitableVNo = rst.data;
		});
		selectedVehicle = $DispatchUtil.getSelectedVehicle(suitableVNo);
	}
	
	//如果是加料生产，可以不管车
	if (!selectedVehicle.attr("data-v_id")) {
		layer.alert("没有可用车辆，请等待车辆回站或手工回站车辆！<br/><br/>注：如果该订单您指定了车辆，但暂时没有您指定的车辆可用，请手工点击合适的车辆后调度！");
		return;
	}
	$("#dVVId").val(selectedVehicle.attr("data-v_id"));
	$("#dVVNo").val(selectedVehicle.attr("data-v_no"));
	$("#dVSourceVId").val(selectedVehicle.attr("data-v_id")); //剩料来源车辆ID
	
	if (selectedVehicle.attr("data-v_memo") == selectedVehicle.attr("data-load_volume") + "方 "
		|| selectedVehicle.attr("data-v_memo") == selectedVehicle.attr("data-load_volume") + ".0方 ") {
		// 没写备注，就不出来，免得有些站不需要这个东西的觉得调度界面复杂
		$("#vehicleMemoDiv").hide();
	} else {
		$('#vehicleMemo').text(selectedVehicle.attr("data-v_memo"));
		$("#vehicleMemoDiv").show();
	}
	
	//司机设置
	funcGetDriversByVId(selectedVehicle.attr("data-v_id"), "dVTongDriverId");
	
	//查询配比情况
	funcGetRecipeMatching(selectedOrder.attr("data-order_id"));
	
	//各方量初始值设置
	// 加料转发时，没有data-load_volume，此时填0即可
	$("#dVProduceCNum").val(!selectedVehicle.attr("data-load_volume") ? '0' : selectedVehicle.attr("data-load_volume"));
	$("#dVInNum").val(!selectedVehicle.attr("data-bill_num") ? '0' : selectedVehicle.attr("data-bill_num"));
	
	// 加料生产时，自动勾上作废原发货单
	if (!selectedVehicle.attr("data-bill_num")) {
		//隐藏剩料处理功能
		$("#dVRemainP").removeClass("hide");
		$("#dVRemainP").addClass("hide");
		//设置作废剩料车辆的记录的checkbox为不选中
		$("#dvIsSetOutInvoiceToZero").attr("checked",false);
	} else {
		//展示剩料处理功能
		$("#dVRemainP").removeClass("hide");
		//设置作废剩料车辆的记录的checkbox为选中
		$("#dvIsSetOutInvoiceToZero").attr("checked",true);
	}


	$("#dVProduceMNum").val(0);
	$("#dVXfNum").val(0);
	calDVBillNum();
	
	//额外费用初始值设置
	$("#dVExtraCharge").val(0);
	$("#dVExtraChargeDesc").val("");
	//每个订单的第一车（第一车的概念是该订单没有未作废的发货单有额外费用），要判断
	$.reqUrlSync("/getOrderExtraChargeForDispatch", {'order_id': selectedOrder.attr("data-order_id")}, function(rst){
		// i_extra_charge 表示该订单下面是否已经有发货单已设置了额外费用
		if (rst.data.i_extra_charge * 1 == 0) {
			$("#dVExtraCharge").val(rst.data.extra_charge);
			$("#dVExtraChargeDesc").val(rst.data.extra_charge_desc);
		}
	});
		
	$("#dVVExtraCharge").val(0);
	$("#dVVExtraChargeDesc").val("");
	
	
	//备注信息
	$("#dVMemoInfo").html(selectedOrder.find(".order_basic_info").attr("title").replace("<br/>", "&nbsp;&nbsp;|&nbsp;&nbsp;"));
	
	//使用几号线生产初始值清空(如果是单站，则不清空)
	if ($("input:radio[name='dVStationId']").length > 1) {
		$("input:radio[name='dVStationId']").attr("checked",false);
		$(".dVStationText").removeClass("dVStationSelText");
		
		// 如果固定了几号线生产，则默认选中固定的生产线
		if(selectedOrder.attr("data-fix_station")*1 > 0) {
			$('#dVStation_' + selectedOrder.attr("data-fix_station")).attr("checked", true);
			$("#dVStationLabel_" + selectedOrder.attr("data-fix_station")).addClass("dVStationSelText");
		}
	} else {
		$("input:radio[name='dVStationId']").attr("checked",true);
		$(".dVStationText").addClass("dVStationSelText");
	}
	
	DVSubDialog = $.ligerDialog.open({ title:'调度', target: $("#DispatchVehicle"), width:900, height:500 });
	$("#dVProduceCNum").select();
}

//图形调度提交
function dvSubmit(){
	//防止意外，重新计算一下票面方量
	calDVBillNum();
	var produceCNum = $('#dVProduceCNum').val();
	var inNum = $('#dVInNum').val();
	var produceMNum = $('#dVProduceMNum').val();
	var xfNum = $('#dVXfNum').val();
	var billNum = $('#dVBillNum').val();
	var extraCharge = $('#dVExtraCharge').val();
	var vExtraCharge = $('#dVVExtraCharge').val();
	
	if ($('#dVVNo').val() == "") {
		layer.alert("送货车辆必须选择！");
		return false;
	}
	
	if (dispatchIsNaN(produceCNum) || dispatchIsNaN(inNum) || dispatchIsNaN(produceMNum) || dispatchIsNaN(xfNum) || dispatchIsNaN(extraCharge) || dispatchIsNaN(vExtraCharge)) {
		layer.alert("生产主料方量、生产砂浆方量、上一工地剩料方量、虚方方量、额外费用请输入数字！");
		return false;
	}
	if (produceCNum < 0 || inNum < 0 || produceMNum < 0 || xfNum < 0) {
		layer.alert("生产主料方量、生产砂浆方量、上一工地剩料方量、虚方方量不能小于0！");
		return false;
	}
	if ((produceCNum * 1 + produceMNum * 1) == 0 && $("#dVIsWater").attr("checked") != "checked") {
		layer.alert("生产主料方量、生产砂浆方量不能同时为0！");
		return false;
	}
	if ((produceCNum * 1 + produceMNum * 1 + xfNum*1 + inNum*1) >= 30) {
		layer.alert("方量已经超过30，请确认！");
		return false;
	}
	
	if(!$('#DispatchVehicle .dVStationRadio:checked').length){
		layer.alert("请选择生产线！");
		return false;
	}
	
	if (extraCharge * 1 > 0) {
		if ($('#dVExtraChargeDesc').val() == '') {
			layer.alert("工地额外费用不为0时，请输入额外费用说明！");
			return false;
		}
	}
	
	if (vExtraCharge * 1 > 0) {
		if ($('#dVVExtraChargeDesc').val() == '') {
			layer.alert("车辆额外费用不为0时，请输入额外费用说明！");
			return false;
		}
	}
	
	
	if(!$("#DVForm").validate()) return false;
	
	var tipTitleAdd = "";
	if (isDispatchInsertControl == 1 && $("#dVIsWater").attr("checked") == "checked") {
		tipTitleAdd = "<br/>本车是送水，请您电话/对讲机通知搅拌楼放水。";
	} 
	
	layer.confirm("您确定要提交吗？" + tipTitleAdd, function(){
		layer.closeAll();
		params = $("#DVForm").vals();
		$.reqUrlEx("/addInvoice", params, dvSubmitCallBack, false);
	}, '确认');	
}

//图形调度提交回调函数
function dvSubmitCallBack(rst) {
	if (rst && rst.state) {
		layer.close(manager);
		
		RefreshDispatchPageInfo();
		
		//刷新列表
		$grid.load("gridBox");
		
		
		manager = layer.msg('系统处理成功！', 0, 1);
		//图形调度中，调度车辆的Div不能够close，只能够hide 
		DVSubDialog.hide();
	} else {
		layer.close(manager);
		layer.alert(rst.msg);
	}
}

//设置开盘订单提交
function soidSubmit(){
	layer.confirm("您确定要提交吗？", function(){
		layer.closeAll();
		var checkedOrders = $('.setdisplay_all_orders:checked');
		
		var orderIdAndClassifys = "";
		$('.dispatch_classify').each(function() {
			orderIdAndClassifys += this.id.replace("dispatch_classify_","") * 1 + "-" + $(this).val() + ",";
		});
		
		var orderIds = -1;
		if (checkedOrders.length) {
			orderIds = checkedOrders.map(function() {
				return $(this).val();
			}).get().join(",");
		}
		$.reqUrlEx("/setOrderDisplay", {
			orderIds : orderIds,
			orderIdAndClassifys : orderIdAndClassifys
		}, soidSubmitCallBack, false);
	}, '确认');	
}

//设置开盘订单提交回调函数
function soidSubmitCallBack(rst) {
	if (rst && rst.state) {
		//直接刷新整个页面
		location.reload();
		/*
		layer.close(manager);
		
		manager = layer.msg('系统处理成功！', 0, 1);
		//图形调度中，设置开盘订单的Div不能够close，只能够hide 
		DVSubDialog.hide();
		
		RefreshDispatchPageInfo();
		*/
	} else {
		layer.close(manager);
		layer.alert(rst.msg);
	}
}


//更换车辆提交
function cvSubmit(){
	if ($("#cVNewVehicle").val() == null || $("#cVNewVehicle").val() == "") {
		layer.msg("没有可用车辆，请等待车辆回站或手工回站车辆！");
		return;
	}
	if ($("#cVNewVehicle").find("option:selected").text() == $("#cVOldVehicleNo").val()) {
		layer.msg("新旧车辆一致，不能更换！");
		return;
	}
	
	$("#cVNewDriverName").val($("#cVNewDriverId").find("option:selected").text());

	var params = $('#ChangeVehicle').vals();
	params.cvNewVehicleNo = $("#cVNewVehicle").find("option:selected").text();
	
	layer.confirm("您确定要更换车辆吗？", function(){
		layer.closeAll();
		$.reqUrlEx("/changeVehicle", params, cvSubmitCallBack, false);
	}, '确认');	
}

//更换车辆提交回调函数
function cvSubmitCallBack(rst) {
	if (rst && rst.state) {
		layer.close(manager);
		
		manager = layer.msg('系统处理成功！', 0, 1);
		
		DVSubDialog.hide();
		
		RefreshDispatchPageInfo();
		//刷新列表
		$grid.load("gridBox");
	} else {
		layer.close(manager);
		layer.alert(rst.msg);
	}
}


function f_selectCommonOK(data, code) {
	if(code == "user") {
		$("#dVQserName").val(data.user_real_name);
		$("#dVQserId").val(data.id);			
	}
	
	selectDialog.close();
}

function myFuncFinishOrder(order_id, p_name, product_name, work_part) {
		if (!hasDDCZPermission) {
			layer.alert("由于没有调度权限，您不能进行本界面上的操作！");
			return;
		}

		var params = {'id' : order_id, "status" : 9};
		$.reqUrlEx("/finishOrder", params, 
			function(rst) {
				if (rst.state) {
					//加载订单信息
					$DispatchUtil.refreshOrders();
				}
			}, "您确定要结束该订单吗？<br/><br/>[" + p_name + "-" + product_name + "-" + work_part + "]"
		);
}



//查看订单对应的发货单
function funcViewOrderInvoice(orderId) {
	var param={"orderId": orderId};
	var opt = {"page": "1", "newPage": "1"};
	$grid.load("gridBox", param, opt);
}

