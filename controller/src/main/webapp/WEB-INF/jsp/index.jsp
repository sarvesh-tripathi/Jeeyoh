<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
	<body>
		Successfully updated!!</br>
		In Response ---Status is "OK"</br></br></br></br>
		<font face="verdana,arial">If you reset Password----New Password is <b>${resetPassword}</b></br>
		Password Reset (Please note down new password)..Click <a href="signOut">here</a> to login with new credentials.</font>
		
		</br></br></br>
		<font face="verdana,arial">If you changed Password----New Password is <b>${newPassword}</b></br>
		Password Changed (Please note down new password)..Click <a href="userProfile">here</a> to go Dashboard.</font>
	</body>
</html>