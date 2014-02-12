<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Events Suggestion</title>
</head>
<body>

<h1>Events Search</h1>


<div class="orangeColor errorCode height25" id="errormsg"></div>
<form name="f" action="eventsSearch" method="GET">
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
		<div align="center"><select name="eventCategory">
			<option value="Concert">Concerts</option>
			<option value="Theater">Theater</option>
			<!--<option value="NIGHT LIFE">NIGHT LIFE</option>
			-->
			<!--<option value="EVENT">EVENT</option>
			<option value="GETAWAYS">GETAWAYS</option>
			-->
			<option value="Sport">Sport</option>
		</select></div>
		</td>

	</tr>
	<tr>
		<td>Location :</td>
		<td><input type='text' id='location' name='location' value='' /></td>
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
		<td><b>Description</b></td>
		<td><b>Venue Name</b></td>
		<td><b>City</b></td>
		<td><b>EventDate</b></td>
		<td><b>Title</b></td>
		<td><b>AncestorGenreDescriptions</b></td>
		<td><b>TotalTickets</b></td>
	</tr>
	<c:forEach items="${mainModel.eventsList}" var="mainModelObj">
		<tr>
			<c:if test="${mainModel.isUser}">

				<td>${mainModelObj.suggestionType}</td>

			</c:if>

			<td>${mainModelObj.description}</td>
			<td>${mainModelObj.venue_name}</td>
			<td>${mainModelObj.city}</td>
			<td>${mainModelObj.event_date}</td>
			<td>${mainModelObj.title}</td>
			<td>${mainModelObj.ancestorGenreDescriptions}</td>
			<td>${mainModelObj.totalTickets}</td>
		</tr>

	</c:forEach>
</table>

</form>
</body>
</html>