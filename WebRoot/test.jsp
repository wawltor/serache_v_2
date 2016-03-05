<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
   <link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">
   <link rel="stylesheet" href="bootstrap/css/jquery-ui.min.css">
   
   <script src="bootstrap/js/jquery-2.1.1.js"></script>
   <script type="text/javascript" src="bootstrap/js/jquery-ui.js"></script>
   <script src="bootstrap/js/bootstrap.min.js"></script>
   <script type="text/javascript">
      $(document).ready(function(){
         
         $("#query").autocomplete({
             source:function(request,response){
             $.ajax({
                type:'get',
                dataType:'json',
                data:{
                   query : $("#query").val()
                },
                url: 'ajax/Suggestion',
                success:function(data){
                    response($.map(data,function(item){
                        return {value:item};
                    }));
                }
             });
             },
             minLength:1
          }); 
         
         
      });
      
      function serache(){
         
         var content = $("#query").val();
         location.href="Result.jsp?content="+content;
      }
   
   </script>
</head>
<body>
<div class="container">
<div class="row">
	<h1>Serache Demo</h1>
	 <div class="well well-lg">
	    <form action="queryResult.jsp">
	      <label>Serache</label>
	      <input id="query" name="query"></input>
	      <input type="button" value="搜索" onclick="serache()">
	    </form>
	  </div>
</div>
</div>

</body>
</html>

