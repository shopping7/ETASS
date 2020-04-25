<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>首页</title>

  <!-- 1. 导入CSS的全局样式 -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
  <script src="js/jquery-2.1.0.min.js"></script>
  <!-- 3. 导入bootstrap的js文件 -->
  <script src="js/bootstrap.min.js"></script>
  <script type="text/javascript">
  </script>
</head>
<body>


<div >${user.username},欢迎您</div>
<div align="center">
<%--  <a--%>
<%--          href="${pageContext.request.contextPath}/findUserByPageServlet" style="text-decoration:none;font-size:33px">查询所有用户信息--%>
<%--  </a>--%>
  <div style="width: 40%">
    <h1>上传文件</h1>
    <form action="${pageContext.request.contextPath}/loginServlet">
      <div class="form-group">
        <label for="exampleInputEmail1">文件关键词</label>
        <input type="text" class="form-control" id="exampleInputEmail1">
      </div>
      <div class="form-group">
        <label for="exampleInputPassword1">文件访问策略</label>
        <input type="text" class="form-control" id="exampleInputPassword1">
      </div>
<%--      <div class="form-group">--%>
<%--        <label for="exampleInputFile">File input</label>--%>
<%--        <input type="file" id="exampleInputFile">--%>
<%--        <p class="help-block">Example block-level help text here.</p>--%>
<%--      </div>--%>
      <div class="form-group">
        <label for="exampleInputPassword1">加密信息</label>
        <input type="text" class="form-control" id="exampleInputPassword2">
      </div>
      <button type="submit" class="btn btn-default">提交</button>
    </form>
  </div>

  <div>
    <h1>提取文件</h1>
    <form>
      <div class="form-group">
        <label for="exampleInputEmail1">文件关键词</label>
        <input type="text" class="form-control" >
      </div>


      <button type="submit" class="btn btn-default">提交</button>
    </form>
  </div>

</div>


</body>
</html>