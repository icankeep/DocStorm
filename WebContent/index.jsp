<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Document Format</title>
</head>
<body>
<span style="color:red">${errorMsg}</span>
<h3>请上传文件</h3>
	<form method="POST" enctype="multipart/form-data" action="/pdf2doc">
  		File to upload: <input type="file" name="upfile"><br/>
  		<br/>
 		<input type="submit" value="Press"> to upload the file!
	</form>
</body>
</html>