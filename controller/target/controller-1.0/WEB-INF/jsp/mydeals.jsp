<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<title>Deal Search</title>
</head>
<body>	
	<form action="getmydeals">  

  User Id <input type ="text" name = "emailId" > 
   <input type="submit" value="Get My Deals"><br>
</form>
	<div id="searchContentSection">
	                <div>						
						<c:if test="${empty mainModel.dealModel}">
						   NO RESULT FOUND
						</c:if>
						<c:if test="${not empty mainModel.dealModel}">
						   <table border="0" width="100%">
							<tr>
								<th width="20%">Title</th>
								<th width="20%">Deal URL</th>
								<th width="10%">Status</th>
								<th width="10%">Start_At</th>
								<th width="10%">End_AT</th>
							</tr>
							</table>
							<table border="0" width="100%">	
								<c:forEach var="searchedContent" items="${mainModel.dealModel}">
								<tr>
									<td width="20%">${searchedContent.title }</td>
									<td width="20%">${searchedContent.dealUrl }</td>
									<td width="10%">${searchedContent.status }</td>
									<td width="10%">${searchedContent.startAt }</td>
									<td width="10%">${searchedContent.endAt }</td>
								</tr>
							 </c:forEach>   

						</table>
						</c:if>
						
					</div>
	                
	         </div>  
	</body>
</html>