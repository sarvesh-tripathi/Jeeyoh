<!DOCTYPE html>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0;">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<link href="<c:out value='${pageContext.servletContext.contextPath}'/>/styles/common.css" type="text/css" rel="stylesheet" />
		<title><tiles:insertAttribute name="title" ignore="true" /></title>
	
	</head>
	<body class="containerBg">
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
	</body>
</html>