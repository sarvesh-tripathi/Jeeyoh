<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Upload</title>
</head>
<body>
<form name="f" method="POST" action="uploadFile"
	enctype="multipart/form-data">
<p>Please specify a file: <br>
<input type="file" name="file" size="50" /></p>
<div><input type="submit" value="Upload" name="upload"></div>
</form>
</body>
</html>