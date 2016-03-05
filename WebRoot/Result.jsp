<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String content = request.getParameter("content");
content=new String(content.getBytes("iso8859-1"),"UTF-8"); 

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Result.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="bootstrap/css/jquery-ui.min.css">
   
   <script src="bootstrap/js/jquery-2.1.1.js"></script>
   <script type="text/javascript" src="bootstrap/js/jquery-ui.js"></script>
   <script src="bootstrap/js/bootstrap.min.js"></script>
   <script type="text/javascript">
      function loadData(){
          var line = $("#line").text();
          $.ajax({
          type:'get',
          data:{line:line},
          dataType:'json',
          url:'ajax/QueryWord',
          success:function(data){
                  
                   var messnode = $("#mess");
                   $.each(data,function(i,n){
                   var tr = $("<tr></tr>");
                   var adiv = $("<div></div>"); 
                   var anode = $("<a></a>");
                   anode.text(data[i].title);
                   anode.attr({
                       href:data[i].url
                      });
                   adiv.css("font-size", 20 );
                   var snode = $("<span></span>"); 
                   snode.text("rank:"+data[i].rank);
                   var cdiv = $("<div></div>");
                   cdiv.text(data[i].content.substr(0,50)+"\n");
                   var bnode = $("<br/>");
                   cdiv.append(bnode);
                   adiv.append(anode);
                   adiv.append(snode);
                   tr.append(adiv);
                   tr.append(cdiv);
                   messnode.append(tr);
         
               });
          }
          
          });
      }
      
      
   
   </script>

  </head>
   
  <body onload="loadData()">
     <div>您的查询条件是:<span id="line"><%=content %></span></div>
     <div>
     <table id="mess">
     </table>
     </div>
  
  </body>
</html>
