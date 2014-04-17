<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friend Search</title>
</head>
<body>

<h1>Friend Search</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="friendSearch" method="GET">
<table>

	<tr>
		<td>Location:</td>
		<td><input type='text' id='location' name='location'></td>
	</tr>
	<tr>
		<td>Name:</td>
		<td><input type='text' id='name' name='name'></td>
	</tr>
	<tr>
		<td>User Id:</td>
		<td><input type='text' id='userId' name='userId'></td>
	</tr>
	<tr>
		<td colspan='2'><input name="submit" type="submit"
			onClick="validateUser()"></td>
	</tr>
</table>
<table cellpadding="15">
	<tr>
		

		<td><b>UserId</b></td>
		<td><b>Name</b></td>
		<td><b>Location</b></td>
		
	</tr>
	<c:forEach items="${mainModel.usersList}" var="mainModelObj">
		<tr>
			
			<td>${mainModelObj.userId}</td>
			<td>${mainModelObj.firstName}</td>
			<td>${mainModelObj.addressline1}</td>
		</tr>

	</c:forEach>
</table>

</form>
</body>
</html>