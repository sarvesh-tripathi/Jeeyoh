<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Non Deal Suggestion</title>
</head>
<body>

<h1>Non Deal Search</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="nonDealSearch" method="GET">
<table>

	<tr>
		<td>User Email:</td>
		<td><input type='text' id='userEmail' name='userEmail'></td>
	</tr>
	<tr>
		<td>Search Text:</td>
		<td><input type='text' id='searchText' name='searchText'></td>
	</tr>
	<tr>
		<td>Category:</td>
		<td>
		<div align="center"><select name="businessCategory">

			<option value=""></option>
			<option value="MOVIE">MOVIE</option>
			<option value="RESTAURANT">RESTAURANT</option>
			<!--<option value="NIGHT LIFE">NIGHT LIFE</option>
			-->
			<!--<option value="EVENT">EVENT</option>
			<option value="GETAWAYS">GETAWAYS</option>
			-->
			<option value="SPORT">SPORT</option>
		</select></div>
		</td>

	</tr>
	<tr>
		<td>Location :</td>
		<td><input type='text' id='location' name='location' value='' /></td>
	</tr>
	<tr>
		<td>Rating :</td>
		<td>
		<div align="center"><select name="businessRating">

			<option value=""></option>
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
		</select></div>
		</td>
	</tr>
	<tr>
		<td colspan='2'><input name="submit" type="submit"
			onClick="validateUser()"></td>
	</tr>
</table>
<table cellpadding="15">
	<tr>
		<c:if test="${mainModel.isUser}">
			<td><b>Suggestion Type</b></td>
		</c:if>

		<td><b>Name</b></td>
		<td><b>WebsiteUrl</b></td>
		<td><b>City</b></td>
		<td><b>Address</b></td>
		<td><b>BusinessType</b></td>
	</tr>
	<c:forEach items="${mainModel.businessList}" var="mainModelObj">
		<tr>
			<c:if test="${mainModel.isUser}">
				<td>${mainModelObj.suggestionType}</td>
			</c:if>
			<td>${mainModelObj.name}</td>
			<td>${mainModelObj.websiteUrl}</td>
			<td>${mainModelObj.city}</td>
			<td>${mainModelObj.displayAddress}</td>
			<td>${mainModelObj.businessType}</td>
		</tr>

	</c:forEach>
</table>

</form>
</body>
</html>