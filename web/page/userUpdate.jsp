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
<title>用户管理</title>

<!-- CSS -->
<link href="style/css/transdmin.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie6.css" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" media="screen" href="style/css/ie7.css" /><![endif]-->

<!-- JavaScripts-->
<script type="text/javascript" src="style/js/jquery.js"></script>
<script type="text/javascript" src="style/js/jNice.js"></script>
<script type="text/javascript">
$(
		function(){
			// Get a reference to the content div (into which we will load content).
			var jContent = $( "#content" );
			// Hook up link click events to load content.
			$('form :input').blur(
					function(){

						  var id = document.getElementById("id").value;  
						    var passwd = document.getElementById("passwd").value;  
						if($(this).is('#passwd')){
						// Launch AJAX request.
							$.ajax(
								{
									// The link we are accessing.
									url: 'check.do?action=checkLoginPassword',
								
									// The type of request.
									type: "post",
									
									data:{id:id,passwd:passwd}, 
									
									// The type of data that is getting returned.
									dataType: "text",
									
									error: function(){
										// Load the content in to the page.
										jContent.html( "<font style=\"color:red\">Page Not Found!!</font>" );
									},
									
									complete: function(){
									},
									success: function( strData ){
										// Load the content in to the page.
										alert(strData);
										jContent.html( "<font style=\"color:red\">"+strData +"</font>");
									}
								}							
								);
						
						// Prevent default click.
						return( false );					
					}
		}
				);
			
		}
		);


</script>
<script type="text/javascript">
	
	        function getdelete(){
              var bool= confirm( "确定要删除吗？"); 
              return bool;
        }
        
         function getValidate(){
         	   var userName = document.getElementById("userName").value;
         	   
         	  var passwd = document.getElementById("passwd").value;
         	  
         	   var passwd1 = document.getElementById("passwd1").value;
         	   var passwd2 = document.getElementById("passwd2").value;
         	   var desc = document.getElementById("desc").value;
              if(userName ==""){
                alert("用户名称不能为空.");
                return false;
              }
              if(passwd ==""){
                  alert("原密码不能为空.");
                  return false;
                }
              
              if(passwd1 =="" || passwd2 ==""){
                alert("密码不能为空.");
                return false;
              }
              if(passwd1 != passwd2){
                alert("密码不一致.");
                return false;
              }
              if(desc ==""){
                alert("用户描述不能为空");
                return false;
              }
         	
         	
         	
         	}
        
</script>

</head>

<body>
	<div id="wrapper">
    	<!-- h1 tag stays for the logo, you can use the a tag for linking the index page -->
    	<h1><a href="#"><span>Transdmin Light</span></a></h1>
        
        <!-- You can name the links with lowercase, they will be transformed to uppercase by CSS, we prefered to name them with uppercase to have the same effect with disabled stylesheet -->
        <ul id="mainNav">
        	<li><a href="#">主页</a></li>
        	<li><a href="/page/shortInfo.do?action=queryList">数据管理</a></li> <!-- Use the "active" class for the active menu item  -->
        	<li><a href="/page/shortInfoHistory.do?action=queryList">历史数据查询</a></li>
        	<li><a href="/page/infoSourceCfg.do?action=queryList">业务配置</a></li>
        	<li><a href="/page/config.do?action=queryList" class="active">系统配置</a></li>
        	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
        </ul>
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container">
        		<div id="sidebar">
                	<ul class="sideNav">        
                    	<li><a href="/page/config.do?action=queryList">系统配置</a></li>
                    	<li><a href="/page/user.do?action=queryList" class="active">用户管理</a></li>
                  </ul>
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->
                
                <div id="main">
                	<form action="/page/user.do?action=update" method="post" class="jNice" onSubmit="return getValidate()">
				                       
				       	<h3>>>更新用户</h3>
                    	<fieldset>
                    		  <input name="id" type="hidden" value="${requestScope.result.data.id}">
                    			<p><label>编号:</label>${requestScope.result.data.id}</p>
                        	<p><label>用户名:</label><input id="userName" name="userName" type="text" class="text-long" value="${requestScope.result.data.userName}"/></p>
                        	<p><label>原密码:</label><input id="passwd" name="passwd" type="password" class="text-long" value=""/><div id ="content"></div></p>
                        	<p><label>密码:</label><input id="passwd1" name="passwd1" type="password" class="text-long" value="${requestScope.result.data.passWord}"/></p>
                        	<p><label>确认密码:</label><input id="passwd2" name="passwd2" type="password" class="text-long" value="${requestScope.result.data.passWord}"/></p>
                        	<p><label>描述:</label><input id="desc" name="desc" type="text" class="text-long" value="${requestScope.result.data.des}"/></p>                        
                          <input class="button-submit" type="submit"  value="提交" />
                          <input class="button-submit" type="reset" value="重置" />
                          <input class="button-submit" type="button" value="返回" onclick="window.history.back(-1)"/>
                        </fieldset>
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
