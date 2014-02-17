<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Page</title>
</head>
<body>

<h1>Spot Search</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="spotSearchResult" method="GET">
<table>

	<tr>
		<td>Search Text:</td>
		<td><input type='text' id='searchText' name='searchText'></td>
	</tr>
	<tr>
		<td colspan='2'><input name="submit" type="submit"
			onClick="validateUser()"></td>
	</tr>
</table>
<table cellpadding="15">
	<tr>
		<td><b>Description</b></td>
		<td><b>City</b></td>
		<td><b>Type</b></td>
	</tr>
	<c:forEach items="${mainModel.searchResult}" var="mainModelObj">
		<tr>
			<td>${mainModelObj.name}</td>
			<td>${mainModelObj.city}</td>
			<td>${mainModelObj.type}</td>
		</tr>

	</c:forEach>
</table>

</form>
</body>
</html>