<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Show My Deals</title>
</head>
<body>

<h1>Show My Deals</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="myDealsSearch" method="GET">
<table>

	<tr>
		<td>User Email:</td>
		<td><input type='text' id='userEmail' name='userEmail'></td>
	</tr>
	<tr>
		<td colspan='2'><input name="submit" type="submit"
			value="Get Deals" onClick="validateUser()"></td>
	</tr>
</table>
<table cellpadding="15">
	<tr>
		<td><b>Title</b></td>
		<td><b>DealUrl</b></td>
		<td><b>StartAt</b></td>
		<td><b>EndAt</b></td>
	</tr>
	<c:forEach items="${mainModel.dealList}" var="mainModelObj">
		<tr>
			<td>${mainModelObj.title}</td>
			<td>${mainModelObj.dealUrl}</td>
			<td>${mainModelObj.startAt}</td>
			<td>${mainModelObj.endAt}</td>
		</tr>

	</c:forEach>
</table>

</form>
</body>
</html>