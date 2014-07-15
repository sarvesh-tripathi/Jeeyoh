<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="mainContent">
	<section id="outerLogin">
		<div class="outerLoginBg">
			<div class="wrapper">
				<section id="loginOptions">
					<section id="outerForm">
						<form:form action="registerUser" commandName="userFormBean" method="POST">
							<div class="fieldsContainer">
								<h1><spring:message code="label.signup.form.heading"/></h1>										
								<spring:message code="label.firstname" var="firstNameLabel"/> 
								<spring:message code="label.lastname" var="lastNameLabel"/> 
								<spring:message code="label.email.address" var="emailLabel"/>
								<label>
									<form:input path="firstName" tabindex="1" type="text" placeholder="${firstNameLabel}" required="required"/>
								</label>	
								<form:errors path="firstName" cssClass="error" />
								<label>
									<form:input path="lastName" tabindex="1" type="text" placeholder="${lastNameLabel}" required="required"/>
								</label>	
								<form:errors path="lastName" cssClass="error" />
								<label>																		 
									<form:input path="email" tabindex="2" type="email" placeholder="${emailLabel}" required="required"/>
								</label>
								<form:errors path="email" cssClass="error" />
                                <div class="center">
									 <input type="submit" tabindex="8" value='<spring:message code="label.button.signup"/>' />
								</div>								
							</div>							
						</form:form>
					</section>
				</section>
			</div>
		</div>
	</section>	
	<div class="push"></div>
</div>
		
			
		