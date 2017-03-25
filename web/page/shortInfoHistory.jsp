<%@ page contentType="text/html; charset=utf-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="n" uri="/WEB-INF/n.tld" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>数据管理</title>

<!-- CSS -->
<link href="style/css/transdmin.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie6.css" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie7.css" /><![endif]-->

<!-- JavaScripts-->
<script type="text/javascript" src="style/js/jquery.js"></script>
<script type="text/javascript" src="style/js/jNice.js"></script>


</head>

<body>
	<div id="wrapper">
    	<!-- h1 tag stays for the logo, you can use the a tag for linking the index page -->
    	<h1><a href="#"><span>Transdmin Light</span></a></h1>
        
        <!-- You can name the links with lowercase, they will be transformed to uppercase by CSS, we prefered to name them with uppercase to have the same effect with disabled stylesheet -->
        <ul id="mainNav">
        	<li><a href="#">主页</a></li>
        	<li><a href="/page/shortInfo.do?action=queryList">数据管理</a></li> 
        	<li><a href="/page/shortInfoHistory.do?action=queryList" class="active">历史数据查询</a></li>
        	<li><a href="/page/infoSourceCfg.do?action=queryList">业务配置</a></li>
        	<li><a href="/page/config.do?action=queryList">系统配置</a></li>
        	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
        </ul>
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container">
        		<div id="sidebar">
                	<ul class="sideNav">
                		  <li><a href="/page/shortInfoHistory.do?action=queryList" class="active">短消息历史数据</a></li>     
                		  <li><a href="/page/shortInfoReciveHistory.do?action=queryList">短消息接收历史数据</a></li>   
                			<li><a href="/page/expresstInfoHistory.do?action=queryList">快递短消息历史数据</a></li>                    	
                  </ul>
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->
                
                <div id="main">
                	<form action="" class="jNice">
				          	<h3>>>短消息历史信息</h3>
                <table cellpadding="0" cellspacing="0">
							
                                <th style="width:5%">编号</th>    
                                <th style="width:15%">消息来源编号</th>  
                                <th style="width:10%">消息级别</th>  
                                <th style="width:20%">添加历史表时间</th>  
                                <th style="width:10%">发送状态</th>  <!--
                                <th style="width:15%">消息生成时间</th>  
                                <th style="width:15%">消息内容</th>                                  
                                <th style="width:15%">原因</th>  -->
                                <th style="width:15%">操作</th>  
             
				          <c:forEach items="${requestScope.result.data.datas}" var="x">
						     		 <c:if test="${x!=null}">                                        
										  <tr class="odd">
				                                <td align="center">${x.id}</td>   
				                                <td align="center">${x.srcId}</td>   
				                                <td align="center">${x.levelId}</td>
				                                <td align="center">${x.stampTimeTmp}</td>
				                                <td align="center">${x.sentResult}</td>
				                                <td class="action"><a href="shortInfoHistory.do?action=viewInfo&id=${x.id}" class="view">详情</a></td>
				                         </tr>            
				              </c:if>
				          </c:forEach>     
				         
                        </table>
                                
                         	<div align="center">								
															<n:nav pageCount="${requestScope.result.wparam.pageCount}"
															pageIndex="${requestScope.result.wparam.pageIndex}" navigationUrl="/page/shortInfoHistory.do"
															params="${requestScope.result.wparam.params}"></n:nav>
											     </div>
                    </form>
                </div>
                <!-- // #main -->
                
                <div class="clear"></div>
            </div>
            <!-- // #container -->
        </div>	
        <!-- // #containerHolder -->
        
        <%@ include file="/page/footer.html"%>
    </div>
    <!-- // #wrapper -->
</body>
</html>
