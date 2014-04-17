package com.jeeyoh.service.addgroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.AddGroupButtonResponse;
import com.jeeyoh.model.response.AddGroupResponse;
import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.search.JeeyohGroupModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.utils.Utils;

@Component("addGroupService")
public class AddGroupService implements IAddGroupService{
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IBusinessDAO businessDAO;
	
	@Override
	@Transactional
	public AddGroupResponse addGroup(AddGroupModel addGroupModel) {
		AddGroupResponse addGroupResponse =new AddGroupResponse();
		// Check privacy id
		Privacy privacyObj = userDAO.getUserPrivacyType(addGroupModel.getPrivacy());
		User user = userDAO.getUserById(addGroupModel.getUserId());
		
		Notificationpermission notificationPermission = userDAO.getDafaultNotification();
		Jeeyohgroup jeeyohGroup = new Jeeyohgroup();
		JeeyohGroupModel jeeyohGroupModel = new JeeyohGroupModel();
		jeeyohGroup.setAbout(addGroupModel.getGroupName());
		jeeyohGroup.setCreatedtime(Utils.getCurrentDate());
		jeeyohGroup.setGroupName(addGroupModel.getGroupName());
		jeeyohGroup.setGroupType(addGroupModel.getCategory());
		jeeyohGroup.setPrivacy(privacyObj);
		jeeyohGroup.setUpdatedtime(Utils.getCurrentDate());
		jeeyohGroup.setUserByCreatorId(user);
		jeeyohGroup.setUserByOwnerId(user);
		ArrayList<Integer> groupMembers = addGroupModel.getMember();
		if(groupMembers!=null)
		{
			Set<Groupusermap> groupUserMapSet = new HashSet<Groupusermap>();
			for(Integer memberId:groupMembers)
			{
				logger.debug("member id  =====>"+memberId);
				User contactUser = userDAO.getUserById(memberId);
				Groupusermap groupUserMap = new Groupusermap();
				groupUserMap.setCreatedtime(Utils.getCurrentDate());
				groupUserMap.setJeeyohgroup(jeeyohGroup);
				groupUserMap.setUpdatedtime(Utils.getCurrentDate());
				groupUserMap.setNotificationpermission(notificationPermission);
				groupUserMap.setUser(contactUser);
				groupUserMapSet.add(groupUserMap);
			}
			jeeyohGroup.setGroupusermaps(groupUserMapSet);
		}
		
		/*String[] contactUserList = members.split(",");
		for(int i=0;i<contactUserList.length;i++)
		{
			logger.debug("contactUserList =>"+contactUserList[i]);
			List<User> contactUser = userDAO.getUserById(Integer.parseInt(contactUserList[i]));
			logger.debug("contactUser =>"+ contactUser.get(0).getFirstName());
			Groupusermap groupUserMap = new Groupusermap();
			groupUserMap.setCreatedtime(Utils.getCurrentDate());
			groupUserMap.setJeeyohgroup(jeeyohGroup);
			groupUserMap.setUpdatedtime(Utils.getCurrentDate());
			groupUserMap.setNotificationpermission(notificationPermission);
			groupUserMap.setUser(contactUser.get(0));
			//userDAO.saveGroupUserMap(groupUserMap);
		}*/
		
		userDAO.saveJeeyohGroup(jeeyohGroup);
		logger.debug("Data saved successfully");
		if(jeeyohGroup!=null)
		{
			jeeyohGroupModel.setAbout(jeeyohGroup.getAbout());
			jeeyohGroupModel.setCreatedtime(jeeyohGroup.getCreatedtime());
			jeeyohGroupModel.setGroupName(jeeyohGroup.getGroupName());
			jeeyohGroupModel.setGroupType(jeeyohGroup.getGroupType());
			jeeyohGroupModel.setPrivacy(jeeyohGroup.getPrivacy().getPrivacyType());
			jeeyohGroupModel.setUpdatedtime(jeeyohGroup.getUpdatedtime());
			jeeyohGroupModel.setUserByCreatorId(jeeyohGroup.getUserByCreatorId().getUserId());
			jeeyohGroupModel.setUserByOwnerId(jeeyohGroup.getUserByOwnerId().getUserId());
			addGroupResponse.setAddGroupDetails(jeeyohGroupModel);
		}
		return addGroupResponse;
	}
	
	
	@Override
	@Transactional
	public AddGroupButtonResponse addGroupPage(int userId)
	{
		logger.debug("addGroupPage =====>"+userId);
		AddGroupButtonResponse addGroupButtonResponse = new AddGroupButtonResponse();
		
		List<Businesstype> businessTypeList = businessDAO.getBusinesstypes();
		if(businessTypeList!=null)
		{
			List<String> categoryList = new ArrayList<String>();
			for(Businesstype businessType:businessTypeList)
			{
				logger.debug("businessTypeList ===>"+businessType.getBusinessType());
				categoryList.add(businessType.getBusinessType());
			}
			addGroupButtonResponse.setCategory(categoryList);
		}
		
		List<User> usersList = userDAO.getUsers();
		logger.debug("usersList =====>"+usersList);
		if(usersList!=null)
		{
			List<UserModel> userModelList = new ArrayList<UserModel>();
			for(User user: usersList)
			{
				if(user.getUserId()!=userId)
				{
					UserModel userModel = new UserModel();
					//userModel.setAddressline1(user.getAddressline1());
					//userModel.setBirthDate(user.getBirthDate());
					//userModel.setBirthMonth(user.getBirthMonth());
					//userModel.setBirthYear(user.getBirthYear());
					//userModel.setCity(user.getCity());
					//userModel.setConfirmationId(user.getConfirmationId());
				//	userModel.setCountry(user.getCountry());
					//userModel.setCreatedtime(user.getCreatedtime());
					userModel.setEmailId(user.getEmailId());
					userModel.setFirstName(user.getFirstName());
					//userModel.setGender(user.getGender());
					//userModel.setIsActive(user.getIsActive());
					//userModel.setIsDeleted(user.getIsDeleted());
					//userModel.setLastName(user.getLastName());
					//userModel.setMiddleName(user.getMiddleName());
					//userModel.setPassword(user.getPassword());
					//userModel.setSessionId(user.getSessionId());
					//userModel.setState(user.getState());
					//userModel.setStreet(user.getStreet());
					//userModel.setUpdatedtime(user.getUpdatedtime());
					//userModel.setUserId(user.getUserId());
					//userModel.setZipcode(user.getZipcode());
					userModelList.add(userModel);
				}
				
			}
			addGroupButtonResponse.setUserModel(userModelList);
		}
		
		
		return addGroupButtonResponse;
		
	}
	
}
