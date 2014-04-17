<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Community Search</title>
</head>
<body>

<h1>Community Search</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="communitySearch" method="GET">
<table>

	<tr>
		<td>User Email:</td>
		<td><input type='text' id='userEmail' name='userEmail'></td>
	</tr>
	<tr>
		<td colspan='2'><input name="submit" type="submit"
			value="Get Community" onClick="validateUser()"></td>
	</tr>
</table>
<c:choose>
	<c:when test="${empty mainModel.pageList}">No result found</c:when>
	<c:otherwise>
		<table cellpadding="15">
			<tr>
				<td><b>About</b></td>
				<td><b>PageUrl</b></td>
				<td><b>Owner name</b></td>
				<td><b>CreatedDate</b></td>
				<td><b>PageType</b></td>
			</tr>
			<c:forEach items="${mainModel.pageList}" var="mainModelObj">
				<tr>
					<td>${mainModelObj.about}</td>
					<td>${mainModelObj.pageUrl}</td>
					<td>${mainModelObj.owner}</td>
					<td>${mainModelObj.createdDate}</td>
					<td>${mainModelObj.pageType}</td>
				</tr>
			</c:forEach>
		</table>
	</c:otherwise>
</c:choose></form>
</body>
</html>