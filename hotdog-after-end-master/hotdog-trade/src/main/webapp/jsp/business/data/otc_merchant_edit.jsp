<%--
  Created by IntelliJ IDEA.
  User: abc18
  Date: 2019/8/29
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
    <div class="contbox_a1">
        <div class="box_a1">
            <form class="wanwu_form"  method="post" action="${rootPath}/member/otcMerchantEdit" id="dwanwuForm">
                <input type="hidden" name="id" id="id" value="${info.id}" />
                <div class="itembox">
                    <div class="searchField" style="margin-top: 10px">
                        <div class="searchFieldCtr">
                            <span>会员账号：</span>
                            <input name="mName" type="text" style="height: 20px;width: 167px" class="sel" disabled value="${info == null ? 0 : info.mName}" />
                        </div>
                    </div>
                    <div class="searchField" style="margin-top: 10px" >
                        <div class="searchFieldCtr">
                            <span>vip(非vip默认为0,值越大,otc订单显示越靠前)：</span>
                            <input name="vip" type="text" style="height: 20px;width: 167px" class="txt number required" value="${info == null ? 0 : info.vip}" />
                        </div>
                    </div>
                </div>
                <p class="p_block p_btn">
                    <input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>







</script>