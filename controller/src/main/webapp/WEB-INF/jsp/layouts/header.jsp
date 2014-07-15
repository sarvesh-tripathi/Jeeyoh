<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<header id="landingHeader">
<script>
FB.init({
	appId: '${oauthModel.fbAppId}',
	cookie:true,
	status:true,
	xfbml:true
	});
	function FacebookInviteFriends()
	{
	FB.ui({
	method: 'apprequests',
	title: 'Storistic Friend Invitation',
	message: '${mainModel.user.fullName} would like to connect with you on Storistic.',
	new_style_message: true,
	}, callback);
	}
	function callback(response) {
		//console.log("Recepent User : " + response.to+ " Request object Id : "+response.request);
		/*FB.api('/100003804763263', function(response) {
		      console.log('Good to see you, ' + response.name + '.');
		    });
			console.log(JSON.stringify(response));
				console.log("Recepent User : " + response.to + " Request object Id : " + response.request);*/
				console.log(JSON.stringify(response));
				if(response != null)
				{
					// Ajax call to invited FB user.
					inviteFBFriend(JSON.stringify(response))
				}
				/*var userIds = response.to;
				//var userArr = userIds.split(",");
				for(var i=0; i<userIds.length; i++)
				{
					FB.api('/'+userIds[i], function(response) {
					  console.log('Good to see you, ' + response.name + '.');
					});
				}
			   console.log("Recepent User : " + response.to + "Recepent User Name: " + response.from + " Request object Id : " + response.request);
			   */
	 }
</script>
	<div class="wrapper">	
		<!-- HIDDEN FIELDS FOR FACEBOOK -->
		<input type="hidden" name="FBTokenId" id="FBTokenId" value="${oauthModel.fbToken}"/>
		<input type="hidden" name="fbAppId" id="fbAppId" value="${oauthModel.fbAppId}"/>
		<input type="hidden" name="fbRedirectUri" id="fbRedirectUri" value="${oauthModel.fbRedirectUri}"/>
		
		<!-- HIDDEN FIELDS FOR GOOGLE PLUS -->
		<input type="hidden" name="gPlusClientId" id="gPlusClientId" value="${oauthModel.gPlusClientId}"/>
		<input type="hidden" name="gPlusRedirectUri" id="gPlusRedirectUri" value="${oauthModel.gPlusRedirectUri}"/> 
	
		<spring:message code="label.email.address" var="emailLabel"/>
		<spring:message code="label.fullname" var="fullName"/> 
		
		<!-- Delete User Group Confirmation PopUp --> 
		<div class="deleteGroup">
			
		</div>	
		
		<!-- Delete friend Confirmation PopUp --> 
		<div class="deleteFriendPopup">
				<h3>
			  	  <span class="deleteConfirm"></span>  <spring:message code="label.deleteFriend.confirmation"/>
			   </h3>
			   <div id="deleteFriendID">
			  </div>
		</div>
		
		<!-- Delete Social site Confirmation PopUp --> 
		<div class="deleteSocialSitePopup">
				<h3>
			  	  <span class="deleteConfirm"></span>  <spring:message code="label.socialSite.confirmation"/>
			   </h3>
			   <div id="deleteSocialSiteID">
			  </div>
		</div>
		
		<h1><a href="landing"><img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/storistic_logo.png" width="174" height="37" alt="Storistic"></a></h1>
		<nav id="userOptions">
			<ul>
				<li>
					<span class="search"></span>
					<!--Search Result-->
					<div class="menuBlock">
						<span class="menuHover" style="right:114px"></span>
						<form action="searchContent" method="GET">
							<div id="searchResult">
								<ul>
									<li>
										<input name="includeStroyline" type="checkbox" id="check-1">
										<label for="check-1">Storylines</label>
									</li>
									<li>
										<input name="includeMoment" type="checkbox" id="check-2">
										<label for="check-2">Moments</label>
									</li>
									<li>
										<input name="includeFriends" type="checkbox" id="check-3">
										<label for="check-3">Include Friends</label>
									</li>
								</ul>
								<div class="searchBlock">
									<input name="query" type="text" required="required">
									<input type="submit" value="">
								</div>
							</div>
						</form>
					</div>
				</li>
				<li>
					<span class="flag"></span>
					<div class="menuBlock">
						<span class="menuHover" style="right:85px"></span>
						<div class="notificationText">
							<h3> Notifications</h3>
							<div class="notificationScroll">
								<div class="scrollbar">
									<div class="track">
										<div class="thumb">
											<div class="end"></div>
										</div>
									</div>
								</div>
								<div class="viewport">
									<div class="overview" id="notificationsList">
										<!-- List of notifications from Ajax call -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</li>
				<li>
					<span class="friends"></span>
					<div class="menuBlock">
					   <div id="loader" style="display:none; left: 200px; position: absolute; top: 70px;">
							<img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/ajax-loader-white.gif" width="100" height="100" alt="Add Group">
						</div>
						<span class="menuHover" style="right:42px"></span>
						<div class="menuTab">
							<ul>
								<li><a href="#my_friends"><spring:message code="label.myfriends.text"/></a></li>
								<li><a href="#invite_friends"><spring:message code="label.inviteFriends.text"/></a></li>
								<li><a href="#inviteGroup" onClick="getUserGroupData();"><spring:message code="label.groups.text"/></a></li>
							</ul>
						</div>
						<div class="searchBlock fLeft">
						<form id="searchFriendForm" name="searchFriendForm" action="searchFriends" method="GET">
							<input id="friendSearchQuery" name="friendSearchQuery" type="text" placeholder="Search your friends"> 
							<input type="submit" value="" class="search">
						</form>	
						</div>
						<div id="friendBlock"><!--My Friends-->
							<div id="my_friends">
								<div class="scrollbar">
									<div class="track">
										<div class="thumb">
											<div class="end"></div>
										</div>
									</div>
								</div>
								<div class="viewport">
									<div class="overview">
										<!-- Add Direct Contact -->
										<div class="addGroup">
												<a href="javascript:showonlyone('addContactID');">
													<img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/addbutton.png" width="16" height="16" alt="Add Contact">
												</a>
												<span> Add Contact </span>
										</div>
										<!-- Add new Contact -->
									<form:form name="inviteFriendForm" id="inviteFriendForm" action="addDirectContact" method="POST" commandName="inviteUserFormBean">
										<div class="addContactID" id="addContactID" style="display: none;">
												<!--Invite Friends-->
												<div  id="invite_friends">
														<dl>
																<dt></dt>
																<dd>
																	<form:input path="fullName" tabindex="1" type="text" required="required" placeholder="${fullName}"/>
																	<form:errors path="fullName" cssClass="error" />	
																</dd>
														
																<dt></dt>
																<dd>
																	<form:input path="email" tabindex="1" type="email" required="required" placeholder="${emailLabel}"/>
																	<form:errors path="email" cssClass="error" />
																</dd>
															<!--
																<dt><spring:message code="label.addToGroup.text"/></dt>
																<dd>
																	<div class="customSelectNumber">
																		<form:select path="groupNameId" cssClass="styled category">
																			<form:option value="">--SELECT GROUP--</form:option>				
																			 <c:forEach var="momentType" items="${mainModel.momentTypeList}">	
																				<form:option value="${momentType.momentTypeId}">${momentType.momentType}</form:option>
																			</c:forEach> 
																		</form:select>
																	</div>
																</dd>
															-->
															</dl>
															
															<div class="margin125">
																<input type="submit" value="Add Contact" class="submitButton">
																<span id="addContactResponse"></span>
															</div>
															<div class="borderBTM"></div>
													</form:form>
													
												</div>
										</div>
										
										<ul>
											<c:forEach var="userContacts" items="${mainModel.userContactList}">	
												<li id="contactLi${userContacts.contactId}">
													<span class="thumbIcon"> 
														<c:choose>
															<c:when test="${empty userContacts.profilePhoto}">
																<img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/thumb_icon.png" width="40" height="40" alt="">
															</c:when>
															<c:otherwise>
																<img src="${userContacts.profilePhoto}" width="40" height="40" alt="">
															</c:otherwise>
														</c:choose>
													</span> 
													<label class="nameTitle" id="lblContactName${userContacts.contactId}">${userContacts.fullName}</label> 											
													<label class="editColum"> 
													<!-- 	<a href="javascript:showonlyone('editFriend${userContacts.contactId}');" class="edit"></a>  --> 
														<a href="javascript:void(0);" class="deleteFriend" onClick="deleteContactById(this, '${userContacts.contactId}');"></a> </label> <!--edit friend-->
														<!--edit friend-->
														<div class="editFriend" id="editFriend${userContacts.contactId}">
															<a href="javascript:void(0);" class="close hide"></a>
														<form name="editContactForm"  id="editContactForm${userContacts.contactId}">
															<input type="hidden" name="contactId" value="${userContacts.contactId}">
																<span class="thumbIcon"> 
																	<img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/thumb_icon.png" width="40" height="40" alt=""> 
																</span>
																<dl>
																	<dt><spring:message code="label.fullName"/></dt>
																	<dd><input type="text" name="fullName" id="fullName${userContacts.contactId}" value="${userContacts.fullName}"></dd>
														
																													
																	<dt><spring:message code="label.email.text"/></dt>
																	<dd><input type="text" name="email" value="${userContacts.email}"></dd>
														        <!-- 
																	<dt><spring:message code="label.addTo.text"/></dt>
																	<dd>
																	<div class="customSelectNumber">
																		<select name="addGroup" id="addGroup${userContacts.contactId}" >
																			<option value="">--SELECT CONTACT--</option>				
																			<c:forEach var="userGroup" items="${mainModel.userGroupList}">
																				<option value="${userGroup.contactGroupId}">${userGroup.contactGroupName}</option>
																			</c:forEach> 
																		</select>
																	</div>
																	</dd>
														
																	<dt><spring:message code="label.removeFrom.text"/></dt>
																	<dd>
																	<div class="customSelectNumber">
																		<select  name="removeGroup" id="addGroup${userContacts.contactId}" >
																				<option value="">--SELECT CONTACT--</option>				
																				<c:forEach var="userGroup" items="${mainModel.userGroupList}">
																					<option value="${userGroup.contactGroupId}">${userGroup.contactGroupName}</option>
																				</c:forEach> 
																		</select>
																	</div>
																	</dd>
															   -->
																</dl>
																<!-- <div class="borderBTM"></div>-->
																<div class="margin175">
																	<input type="button" value="Save" class="submitButton" onClick="updateContactInfo('${userContacts.contactId}');">
																	
																</div>
																<span id="editContactInfo${userContacts.contactId}" style="float:none;" class="margin175 clr"></span>
															</form>
														</div>
													</li>
												</c:forEach>
											</ul>
										</div>
									</div>
								</div>
		
								<!--Invite Friends-->
								<div class="overview" id="invite_friends">
									<dl>
										<div class="facebookButton">
											<input name="" tabindex="1" type="button" value="Invite Facebook Friends" class="sendButton" onclick="FacebookInviteFriends();"/>
										</div>
										<dt></dt>
										<dd>
											<input name="friendName" id="friendName" tabindex="2" type="text" placeholder="${fullName}"/>
										</dd>
										<dt></dt>
										<dd>
											<input name="inviteEmail" id="inviteEmail" tabindex="3" type="email" required="required" placeholder="${emailLabel}"/>
										</dd>
									</dl>
									<!-- div class="borderBTM"></div> -->
									<div class="margin125">
										<input type="submit" value="Invite Friend" class="submitButton" onClick="inviteFriend($('#friendName').val(), $('#inviteEmail').val(), '', 'inviteFriend');">
										<span class="clr" style="color:#47B4B8;" id="InviteFriendResponse"></span>
									</div>
								</div>
		
								<!--Add Group-->
							<!-- <div id="addGroupID">	 -->
								<div id="inviteGroup">
									<div class="scrollbar">
										<div class="track">
											<div class="thumb">
												<div class="end"></div>
											</div>
										</div>
									</div>
									<div class="viewport">
										<div class="overview">
											<div class="addGroup">
												<a href="javascript:showonlyone('addNewgroup');">
													<img src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/addbutton.png" width="16" height="16" alt="Add Group">
												</a>
												<span> <spring:message code="label.newGroup.text"/> </span>
											</div>
											<!-- Add new group -->
											<div class="addNewgroup" id="addNewgroup">
												<!--<div class="addGroup">
													<span><spring:message code="label.newGroup.text"/></span>
												</div> -->
												<form:form  name="addGroupForm" id="addGroupForm" action="addNewGroup" method="POST" commandName="addGroupFormBean">
													<dl>
														<dt><spring:message code="label.groupName.text"/> </dt>
														<dd><form:input path="contactGroupName" tabindex="1" type="text" required="required"/></dd>
												
														<%-- <dt><spring:message code="label.privacy.text"/></dt>
														<dd>
														<div class="customSelectNumber"><select class="styled category"
															name="">
															<option name="">Group 1</option>
															<option name="">Group 2</option>
															<option name="">Group 3</option>
															<option name="">Group 4</option>
														</select></div>
														</dd> --%>
												
														<dt><spring:message code="label.addMember.text"/></dt>
														<dd>
														<div class="customSelectNumber fLeft">
															<form:select path="contactId" multiple="true" cssStyle="height:65px; float:left; margin-top:5px">
																<form:option value="">--Add Member--</form:option>				
															 	<c:forEach var="userContact" items="${mainModel.userContactList}">	
																	<form:option value="${userContact.contactId}">${userContact.fullName}</form:option>
																</c:forEach> 
															</form:select>
														</div>
														<a href="#" class="add hide" id="hide"><img
															src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/addbutton.png" width="16" height="16" alt="Add">
														<spring:message code="label.add.text"/></a></dd>
													</dl>
													<!-- <div class="borderBTM"></div> -->
													<div class="margin125">
														<input type="submit" class="submitButton" value="Add Group">
														<span id="addGroupResponse"></span>
													</div>
												</form:form>
											</div>
											<ul>
												<c:forEach var="userGroup" items="${mainModel.userGroupList}">
													<li id="groupLi${userGroup.contactGroupId}">
														<label class="nameTitle"> 
															<span class="groupName">${userGroup.contactGroupName}</span>
															<span class="member">(${fn:length(userGroup.contactList)} <spring:message code="label.member.text"/>)</span> 
														</label>
														<label class="editColum">
															<a href="javascript:void(0);" class="edit"></a> 
															<a href="javascript:void(0);" class="deleteUserGroup" onClick="deleteGroup(this,'${userGroup.contactGroupName}','${userGroup.contactGroupId}');"></a>
														</label>
												 		<!--Edit group-->
														<div class="editGroup" id="editgroup123">
															<a class="close hide" href="javascript:void(0);"></a>
															<h3>${userGroup.contactGroupName} 
																<span>(${fn:length(userGroup.contactList)} <spring:message code="label.member.text"/>)</span>
															</h3>
															<div class="customSelectNumber">
																<select id="selectedId${userGroup.contactGroupId}" onChange="addContactsToGroup(this,'${userGroup.contactGroupId}','${userGroup.contactGroupName}','${pageContext.servletContext.contextPath}');">
																	<option value="">--Add Member--</option>				
																	<c:forEach var="userContact" items="${mainModel.userContactList}">	
																		<option value="${userContact.contactId}">${userContact.fullName}</option>
																	</c:forEach> 
																</select>
																<span class="editGroupError" id="addContactError${userGroup.contactGroupId}"></span>
															</div>
															<ul id="groupContactsULID${userGroup.contactGroupId}">
																<c:forEach var="userContact" items="${userGroup.contactList}">
																	<li id="Li${userGroup.contactGroupId}${userContact.contactId}">
																		<span>${userContact.fullName}</span>
																		<a href="javascript:void(0);" class=" " onClick="deleteGroupContact(this,'${userGroup.contactGroupId}','${userContact.contactId}','${userGroup.contactGroupName}');"><img
																			src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/delete_icon.png">
																		</a>
																		<div class="errorMsg"></div>
																	</li>
														        </c:forEach>			
															</ul>
														</div>
													</li>
												</c:forEach> 
											</ul>
										</div>
									</div>
								</div>
							<!-- </div> -->
								<!--Search list-->
								 <div id="searchList">
									<div class="scrollbar">
										<div class="track">
											<div class="thumb">
												<div class="end"></div>
											</div>
										</div>
									</div>
									<div class="viewport">
										<div class="overview">
											<ul>
												<li>
													<!--  elements will be added by Ajax Call -->
												</li>
											</ul>
										</div>
									</div>
								</div> 
							</div>
						</div>
					</li>
					
					<li>
						<span class="settings"></span>
						<!--Account Setting-->
						<div class="menuBlock">
							<span class="menuHover" style="right:1px"></span>
							<h3 class="h3Title"><span class="account"></span>Settings</h3>
							<div id="accountSetting">
								<div class="overview">
									<ul>
										<li>
										 <form:form id="profilePhotoForm" name="profilePhotoForm" method="POST" commandName="profilePhotoFormBean" enctype="multipart/form-data">
										<c:choose>  
					    					 <c:when test="${not empty mainModel.user.profilePhoto}">  
					        					<img id="profilePhoto" width="79" height="78" alt="" src='${mainModel.user.profilePhoto}'>
					     					</c:when>  
					    	     			<c:otherwise>
								 				<img id="profilePhoto" width="79" height="78" alt="" src="<c:out value='${pageContext.servletContext.contextPath}'/>/images/social_thumb.png">
								 			</c:otherwise>  
										</c:choose> 
											<label class="editColum">
												<a class="edit" href="javascript:void(0);"></a>
												<form:input path="profilePhotoPath" name="profilePhotoPath" type="file" onchange="editProfilePhoto();" value=""/>
											</label>
										</form:form>	
										</li>
										<li>
											<div id="displayName">
												<label class="nameTitle"><span class="fLeft" id="FULLNAME">${mainModel.user.fullName}</span></label>		
												<label class="editColum">
													<a class="edit" href="javascript:editSettings('editName', 'displayName');"></a>
												</label>
											</div>
											<div id="editName" class="editSettings">
												<a class="close" href="javascript:void(0);" onclick="javascript:displaySettings('displayName', 'editName');"></a>
												<form action="javascript:void(0);" name="">
													<dl>														
														<dd>
															<input type="text" value="${mainModel.user.fullName}" id="updatedFullName">
															<input type="button" class="submitBTN" value="Update" onClick="updateUser('${mainModel.user.email}','FULLNAME',$('#updatedFullName').val());">
														</dd>
													</dl>
												</form>
												<span id="messageFullnameChanged"></span>
											</div>
										</li>
										
										<li>
											<div id="displayEmail">
												<label class="nameTitle"><span class="fLeft" id="EMAIL">${mainModel.user.email}</span></label>
												<label class="editColum">
													<a class="edit" href="javascript:editSettings('editEmail', 'displayEmail');"></a>
												</label>
											</div>
											<div id="editEmail" class="editSettings">
												<a class="close" href="javascript:void(0);" onclick="javascript:displaySettings('displayEmail', 'editEmail');"></a>
												<form action="" name="">
													<dl>														
														<dd>
															<input type="text" value="${mainModel.user.email}" id="updatedEmail">
															<input type="button" class="submitBTN" value="Update" onClick="updateUser('${mainModel.user.email}','EMAIL',$('#updatedEmail').val());">
														</dd>
													</dl>
												</form>
												<span id="messageEmailChanged"></span>
											</div>
										</li>
										<li>
											<label class="nameTitle">Password</label>
											
											<label class="editColum">
												<a class="edit" href="javascript:showonlyone('editPassword');"></a>
											</label>
											<div id="editPassword" class="editSettings">
												<a class="close hide" href="javascript:void(0);"></a>
												<form action="javascript:void(0);" name="">
													<dl>														
														<dd>
															<input type="password" id="oldpassword" placeholder="Old password">
														</dd>
													</dl>
													<dl>													
														<dd>
															<input type="password" id="newPassword" placeholder="New password">
														</dd>
													</dl>
													<dl>													
														<dd>
															<input type="password" id="confirmNewPassword" placeholder="Confirm password">
														</dd>
													</dl>
													
													<a href="javascript:void(0);" onClick="updatePassword('${mainModel.user.email}',$('#oldpassword').val(),$('#newPassword').val(),$('#confirmNewPassword').val());" class="updateButton">Update</a>
												</form>
												<span id="messagePasswordChanged"></span>
											</div>
										</li>
										<li>
											<div id="socialProfiles">
												<!-- content of this div will be coming from Ajax response socialProfiles.jsp-->
											</div>	
										</li>
										<li>
											<label class="nameTitle">Tutorial</label>
											<label class="editColum">
												<span id="tutorialOption">
												<a class="tutorialIcon"></a>
												</span>
											</label>
										</li>
										<li>
											<a href="j_spring_security_logout" onclick="return removeCookies();"  class="logOutButton">Logout</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
					</li>
				</ul>
			</nav>
	</div>
	<nav id="mainMenu">
		<ul>
			<li id="myStorylineMenu">
				<a href="myStorylines?filter=myStorylines" title="StoryLines"><spring:message code="label.storyLines.text"/></a>
				
				<c:if test = "${requestScope['javax.servlet.forward.query_string'] == 'filter=myStorylines'}">
					 <!--Story submenu -->
					<div class="storylineSubmenu">
						 <ul>
							 <li><a class="createStoryPop" href="javascript:void(0);">Add Storyline</a></li>
							 <li><a class="addStoryBeat" href="javascript:void(0);">Add Story Beat</a></li>
						 </ul>
					</div>
				</c:if>	
                
                <c:if test = "${requestScope['javax.servlet.forward.query_string'] == null}">
                 <!--Story submenu for landing-->
                    <div class="storylineSubmenu landingStorySubMenu">
                         <ul>
                             <li><a class="createStoryPop" href="javascript:void(0);">Add Storyline</a></li>
                         </ul>
                    </div> 
                </c:if>
            </li>
			<li id="myMomentsMenu">
				<a href="myMoments?filter=myMoments" title="Moments" >					
					<spring:message code="label.moments.text"/>
				</a>
				<!--Moments submenu
				 <div class="momentMenu">
                     <ul>
                         <li class="ahe"><a href="javascript:void(0);"></a></li>
                         <li class="feeling"><a href="javascript:void(0);"></a></li>
                         <li class="gratitude"><a href="javascript:void(0);"></a></li>
                         <li class="blindSpot"><a href="javascript:void(0);"></a></li>
                         <li class="personal"><a href="javascript:void(0);"></a></li>
                     </ul>
                 </div>
                 -->
                 <c:if test = "${requestScope['javax.servlet.forward.query_string'] == 'filter=myMoments'}">
					<!--Moments submenu -->
					 <div class="momentSubmenu">
						 <ul>
							 <li><a class="createNewMoment" href="javascript:void(0);">Add Moment</a></li>
						 </ul>
					 </div>	
				</c:if>	
                
                 <c:if test = "${requestScope['javax.servlet.forward.query_string'] == null}">
                 <!--Moments submenu for landing -->
                    <div class="momentSubmenu landingMomentSubMenu">
                        <ul>
						 <li><a class="createNewMoment" href="javascript:void(0);">Add Moment</a></li>
					 </ul>
                    </div> 
                </c:if>
                
			</li>
			<li id="myFavoritesMenu">
				<a href="myFavorites?filter=favorite" title="Favorites" >					
					<spring:message code="label.favorites.text"/> 
				</a>
			</li>
			<li id="fiendsLatestMenu">
				<a href="friendsLatest?filter=friends" title="Friends" ><spring:message code="label.friends.text"/></a>
			</li>
		</ul>
	</nav>
</header>