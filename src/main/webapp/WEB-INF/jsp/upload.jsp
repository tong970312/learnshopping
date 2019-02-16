<%--
  Created by IntelliJ IDEA.
  User: tong
  Date: 2019/2/14
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片上传</title>
</head>
<body>
<%--文件上传一定是post--%>
<%--enctype="multipart/form-data 文件以二进制的形式上传--%>
<%--action为空 提交到当前路径(页面当前路径)--%>
<form action="" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="上传">
</form>
</body>
</html>
