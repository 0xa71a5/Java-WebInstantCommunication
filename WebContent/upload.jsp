<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>文件上传</title>
  </head>
  
  <body>    
       <form name="frm_test" action="${pageContext.request.contextPath }/fileServlet?method=upload" method="post" enctype="multipart/form-data">
            <%--<input type="hidden" name="method" value="upload">--%>
            
            用户名：<input type="text" name="userName">  <br/>
           文件：   <input type="file" name="file_img">   <br/>
           
           <input type="submit" value="提交">
        </form>
  </body>
</html>