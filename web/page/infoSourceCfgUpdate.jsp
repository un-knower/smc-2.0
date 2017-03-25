<%@ page contentType="text/html; charset=utf-8" language="java"  isELIgnored="false"%>
<%@ taglib prefix="n" uri="/WEB-INF/n.tld"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
<title>业务配置</title>

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
									url: 'check.do?action=checkPassword',
								
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
	
        function getValidate(){
        	
           var id = document.getElementById("id").value;  
         	    if(id ==""){
                alert("编号不可为空.");
                return false;
              }
               if (isNaN(id)){
				alert("编号必须是数字.");
					return false;
			    }
                 var sourceName = document.getElementById("sourceName").value;  
         	    if(sourceName ==""){
                alert("来源名称不可为空.");
                return false;
              }
                var userName = document.getElementById("userName").value;  
         	    if(userName ==""){
                alert("用户名不可为空.");
                return false;
              }
         	    
                var passwd = document.getElementById("passwd").value;  
         	    if(passwd ==""){
                alert("原密码不可为空.");
                return false;
              }
              var passwd1 = document.getElementById("passwd1").value;  
         	    if(passwd1 ==""){
                alert("密码不可为空.");
                return false;
              }
                          var passwd2 = document.getElementById("passwd2").value;  
         	    if(passwd2 ==""){
                alert("密码不可为空.");
                return false;
              }
              
               if(passwd2 !=passwd1){
                alert("密码输入不一致.");
                return false;
              }
                 var IP = document.getElementById("IP").value;             
               return checkIP(IP);
        }
        
						//校验IP地址格式   
						function checkIP(ip) 
						{ 
							var arr=ip.split(',');
							//ip地址
							var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
							for (i=0;i<=arr.length;i++)
								{
									var reg = ip.match(arr[i]); 
									if(reg==null) 
									{ 
									 alert("IP地址不合法！");
									 return false;     
									} 
								}
						    return true;         
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
        	<li><a href="/page/infoSourceCfg.do?action=queryList" class="active">业务配置</a></li>
        	<li><a href="/page/config.do?action=queryList">系统配置</a></li>
        	<li class="logout"><a href="/auth.do?exit="exit">退出</a></li>
        </ul>
        <!-- // #end mainNav -->
        
        <div id="containerHolder">
			<div id="container">
        		<div id="sidebar">
                	<ul class="sideNav">        
                    	<li><a href="/page/infoSourceCfg.do?action=queryList"  class="active">短消息来源配置</a></li>                   
                    	<li><a href="/page/infoStrategyCfg.do?action=queryList">短消息发送策略配置</a></li>
                    	<li><a href="/page/infoLevelCfg.do?action=queryList">短消息级别查询</a></li>
                    	<li><a href="/page/infoToUserCfg.do?action=queryList">短消息接收用户配置</a></li>
                    	<li><a href="/page/infoUserGroupCfg.do?action=queryList">短消息接收用户分组配置</a></li>
                  </ul>
                    <!-- // .sideNav -->
                </div>    
                <!-- // #sidebar -->
                
                <!-- h2 stays for breadcrumbs -->
                
                <div id="main">
                	<form action="/page/infoSourceCfg.do?action=save"  method="post" class="jNice" onSubmit="return getValidate()">
                        
					<h3>>>更新短消息来源</h3>
                    	<fieldset>
                    	<p><label>信息来源编号:</label>
                    	   <input id="id" name="id" type="text" class="text-long"  value="${requestScope.source.srcid}" /></p>
                    		<p><label>信息来源名称:</label><input id="sourceName" name="sourceName" type="text" class="text-long" value="${requestScope.source.name}" /></p>                    		  
                        	<p><label>用户名:</label><input id="userName" name="userName" type="text" class="text-long" value="${requestScope.source.user}"/></p>
                        	
                        	<p><label>原密码:</label><input id="passwd" name="passwd" type="password" class="text-long" value=""/><div id ="content"></div></p>
                        	
                        	<p><label>密码:</label><input id="passwd1" name="passwd1" type="password" class="text-long" value=""/></p>
                        	<p><label>确认密码:</label><input id="passwd2" name="passwd2"type="password" class="text-long" value=""/></p>
                        	<p><label>IP:</label><input id="IP" name="IP" type="text" class="text-long" value="${requestScope.source.ipList}"/></p>
                        	<p><label>描述:</label><input id="desc" name="desc" type="text" class="text-long" value="${requestScope.source.description}"/></p>                        
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
