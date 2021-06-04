<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.sql.*" %>
<%
    String content="";
    String cname=request.getParameter("txtN");
    String tableName = request.getParameter("txtDb");
    if(tableName!=null&&!tableName.equals("")){    
    Connection conn=null;
    Connection conn1=null;
    try{
    String driver="com.mysql.jdbc.Driver";
    String  url="jdbc:mysql://localhost:3306/pmzhongguo_trade";
    String username="root";
    String password="kuaiyou";
    Class.forName(driver); 
    	String commentSql = "SELECT COLUMN_COMMENT FROM information_schema.columns WHERE table_schema ='pmzhongguo_trade'  AND table_name = '" + tableName + "' AND column_name = '";
		conn = DriverManager.getConnection(url,username,password);
		conn1 = DriverManager.getConnection(url,username,password);
		Statement stmt = conn.createStatement();
		Statement stmt1 = conn.createStatement();

		ResultSet rs = stmt.executeQuery("select * from "+tableName);
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount=rsmd.getColumnCount();

		int index=0;
		//if(rs.next()){
			for(int i=1;i<columnCount+1;i++){//从1开始
				String colName=rsmd.getColumnName(i);
				String colType = rsmd.getColumnTypeName(i);
				
				
				ResultSet rs2 = stmt1.executeQuery(commentSql+colName+"'");
				
				String comments = "";
				
				while(rs2.next()) {
					comments = rs2.getString("COLUMN_COMMENT");
					if(comments != null)
						comments = comments.replaceAll(" ","");
					else
						comments = "";
				}
				if(colType.equalsIgnoreCase("NUMBER")||colType.equalsIgnoreCase("INTEGER")||colType.equalsIgnoreCase("int")) colType="Integer";
				else  if(colType.equalsIgnoreCase("VARCHAR")||colType.equalsIgnoreCase("VARCHAR2")|colType.equalsIgnoreCase("CHAR")||colType.equalsIgnoreCase("LONG VARCHAR")) colType="String";
				else  if(colType.equalsIgnoreCase("BIGINT"))colType="Long";
				else  if(colType.equalsIgnoreCase("DECIMAL"))colType="BigDecimal";
				else  if(colType.equalsIgnoreCase("DOUBLE"))colType="Double";
				else  if(colType.equalsIgnoreCase("FLOAT"))colType="Float";
				else  if(colType.equalsIgnoreCase("TEXT"))colType="String";
				else  if(colType.equalsIgnoreCase("DATE"))colType="String";
				else  if(colType.equalsIgnoreCase("DATETIME"))colType="Date";
				else colType="不支持类型";
				//out.println(colName+"&nbsp;"+colType+"<br>");
				if(i==columnCount){
					content+=colName+" "+colType+" "+comments;
				}else{
					content+=colName+" "+colType+" "+comments+"\n";
				}
			}
                        rs.close();
                        stmt.close();
                        conn.close();
                        stmt1.close();
                        conn1.close();
		//}
		}catch(Exception e){
		System.out.println("----1234----Error: 有例外出现---------");
			e.printStackTrace();
                        conn.close();
                        conn1.close();
			out.println("--------Error: 有例外出现---------");
			throw new Exception(e.getMessage());
		}
    }
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>New Page 1</title>
</head>

<script language="javascript">
//.toLowerCase(); 
//.toUpperCase(); 
	function create(){
		var index=0;
		var strAll=myform.txt.value;
		var arr=strAll.split("\n");
		var arrInfo=new Array();
		for(var i=0;i<arr.length;i++){
			var dsf = arr[i].split(" ");
			var strStart=dsf[0];
			var strEnd=dsf[1];
			var strComment=dsf[2];
			//if(i!=arr.length-1){
			//	strEnd=strEnd.substring(0,strEnd.length-1).replace(/ /,"");
			//}else{
			//	strEnd=strEnd.substring(0,strEnd.length).replace(/ /,"");
			//}
			var attrName = strStart.toLowerCase();
			var attrName_t = "";
			/*
			if(attrName.indexOf("_")>=0) {
				var attrNames = attrName.split("_");
				for(var k=0;k<attrNames.length;k++) {
					//alert(attrNames[k]);
					var at = attrNames[k];
					if(k>0) {
						at = attrNames[k].substring(0,1).toUpperCase() + attrNames[k].substring(1,attrNames[k].length);
					}
					attrName_t = attrName_t + at;
				}
			}
			*/
			if(attrName_t != "") {
				attrName=attrName_t;
			}
			arr[i]=attrName+":"+strEnd+":"+strStart+":"+strComment;
		}

		var strP="";
		var strC="";
		var strPar="";
		var strCon="";
		var strM="";
		var strPre="";
		var strInfo="";
		var strSel="";
		var strSel2="";
		var strIn="INSERT INTO "+myform.txtDb.value+"(";
		var strSel="SELECT ";
		var strSelPro="";
		var strUp="UPDATE "+myform.txtDb.value+" SET ";
		var strPro="";

		var strFind="";
		var strArr="";
		var strSet="";

		var strResult="";
		var strtoString="";

		
		for(var i=0;i<arr.length;i++){
			p_t = arr[i].split(":")[0].substring(0,1).toUpperCase() + arr[i].split(":")[0].substring(1,arr[i].split(":")[0].length);
			strP+="private "+arr[i].split(":")[1]+" "+arr[i].split(":")[0]+";//"+arr[i].split(":")[3]+"\n";
			strPar+=arr[i].split(":")[1]+" "+arr[i].split(":")[0]+",";
			strCon+="    this."+arr[i].split(":")[0]+"="+arr[i].split(":")[0]+";\n"
			//strM+="public "+arr[i].split(":")[1]+" get"+p_t+"(){\n  return "+arr[i].split(":")[0]+";\n}\n";
			strPre+="pstmt.set"+(arr[i].split(":")[1]=="Float"?"String":arr[i].split(":")[1])+"("+(i)+",info.get"+arr[i].split(":")[0]+"());\n";
			strInfo+="        info.set"+arr[i].split(":")[0]+"(rs.get"+(arr[i].split(":")[1]=="int"?"Int":arr[i].split(":")[1])+"(\""+arr[i].split(":")[0]+"\"));\n"
			strIn+=arr[i].split(":")[2]+",";
			strPro+="#\{"+arr[i].split(":")[0]+"\},";
			strSel+=arr[i].split(":")[2]+",";
			//strSel2+=arr[i].split(":")[0]+" as "+arr[i].split(":")[0]+"_1,";
			strUp+=arr[i].split(":")[2]+"=#\{"+arr[i].split(":")[0]+"\},";

			strFind+="this."+arr[i].split(":")[0]+" = "+myform.txtN.value.toLowerCase()+".get"+arr[i].split(":")[0]+"();\n";
			strArr+="info[i]["+i+"]="+myform.txtN.value.toLowerCase()+".get"+arr[i].split(":")[0]+"();\n";
			//strSet+=myform.txtN.value+".set"+arr[i].split(":")[0]+"("+(arr[i].split(":")[1]=="Float"?"String.valueOf(":"")+"rs.get"+(arr[i].split(":")[1]=="int"?"Int":arr[i].split(":")[1])+"(\""+arr[i].split(":")[0]+"\"))"+(arr[i].split(":")[1]=="Float"?")":"")+";\n";

			strResult+="<result property=\""+arr[i].split(":")[0]+"\" column=\""+arr[i].split(":")[2]+"\"/>\n";
			strtoString+="\","+arr[i].split(":")[0]+"=\"+"+arr[i].split(":")[0]+"+"

		}
		
		
		strPro=strPro.substring(0,strPro.length-1);
		strIn=strIn.substring(0,strIn.length-1)+") VALUES("+strPro+")";
		strSel=strSel.substring(0,strSel.length-1)+" FROM "+myform.txtDb.value;
		strUp=strUp.substring(0,strUp.length-1);

		myform.txtContent.value=strPre;
		myform.txtM.value=strInfo;
		strCC="public "+myform.txtN.value+"("+strPar.substring(0,strPar.length-1)+")\n{\n"+strCon+"\n}\n"
		myform.txtAll.value=strP+"\n"+strM+"\n\n\n\n\n\n\n\n\n";
		myform.txtIn.value=strIn+"\n\n"+strSel+"\n\n"+strUp+"\n\n"+strSel2;
		myform.txtF.value=strFind;
		myform.txtA.value=strArr;
		myform.txtSet.value=strSet;
		myform.txtresultmap.value=strResult;
		myform.toString.value=strtoString;


	}
</script>
<body>
<form name="myform" action="create.jsp" method="post">

<div align="left">
  <table border="1" width="763" height="244">
    <tr>
      <td width="386" height="329">
      类名
          <input name="txtN" type="text" value="<%=cname%>">
          <p> 表名
            <input type="text" value="<%=tableName%>" name="txtDb" size="20">
            <input onclick="create()" type="submit" name="btn2" value="生成">
          </p>
          <p>
            <textarea rows="19" name="txtAll" cols="51"></textarea>
		  <P style="display:none">
			<textarea rows="19" name="txtresultmap" cols="51"></textarea>
		  </P>
		  <p style="display:none"><textarea rows="19" name="toString" cols="51"></textarea></p>
		  <p style="display:none">&nbsp;
            <textarea rows="15" name="txt" cols="41"><%=content%></textarea>
          </p>
        </td>
      <td width="361" height="329">
	    <p><textarea rows="20" name="txtIn" cols="60"></textarea></p>
	    <p style="display:none"><textarea rows="11" name="txtContent" cols="48"></textarea></P>
        <p style="display:none"><textarea rows="10" name="txtM" cols="48"></textarea></p>
        <p style="display:none">　<textarea rows="8" name="txtF" cols="48"></textarea></p>
        <p style="display:none"><textarea rows="8" name="txtA" cols="48"></textarea></p>
        <p style="display:none"><textarea rows="10" name="txtSet" cols="48"></textarea></p>
      </td>
    </tr>
    <tr>
        <td width="747" height="7" colspan="2">&nbsp;</td>
    </tr>
  </table>
</div>
</form>
<script language="JavaScript">
create();
</script>
</body>

</html>
