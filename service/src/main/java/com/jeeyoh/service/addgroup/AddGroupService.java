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

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.AddGroupButtonResponse;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.GroupDetailResponse;
import com.jeeyoh.model.response.GroupListResponse;
import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.search.JeeyohGroupModel;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IGroupDAO;
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

	@Autowired
	private IGroupDAO groupDAO;

	@Override
	@Transactional
	public BaseResponse addGroup(AddGroupModel addGroupModel) {
		BaseResponse baseResponse =new BaseResponse();
		Jeeyohgroup jeeyohgroup = groupDAO.isGroupExists(addGroupModel.getGroupName());
		if(jeeyohgroup == null)
		{
			// Check privacy id
			Privacy privacyObj = userDAO.getUserPrivacyType(addGroupModel.getPrivacy());
			User user = userDAO.getUserById(addGroupModel.getUserId());

			Notificationpermission notificationPermission = userDAO.getDafaultNotification();
			Jeeyohgroup jeeyohGroup = new Jeeyohgroup();
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
					//User contactUser = userDAO.getUserById(memberId);
					User contactUser = new User();
					contactUser.setUserId(memberId);
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

			groupDAO.saveJeeyohGroup(jeeyohGroup);
			logger.debug("Data saved successfully");
			/*if(jeeyohGroup!=null)
			{
				JeeyohGroupModel jeeyohGroupModel = new JeeyohGroupModel();
				jeeyohGroupModel.setAbout(jeeyohGroup.getAbout());
				jeeyohGroupModel.setCreatedtime(jeeyohGroup.getCreatedtime());
				jeeyohGroupModel.setGroupName(jeeyohGroup.getGroupName());
				jeeyohGroupModel.setGroupType(jeeyohGroup.getGroupType());
				jeeyohGroupModel.setPrivacy(jeeyohGroup.getPrivacy().getPrivacyType());
				jeeyohGroupModel.setUpdatedtime(jeeyohGroup.getUpdatedtime());
				jeeyohGroupModel.setUserByCreatorId(jeeyohGroup.getUserByCreatorId().getUserId());
				jeeyohGroupModel.setUserId(jeeyohGroup.getUserByOwnerId().getUserId());*/
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			//baseResponse.setAddGroupDetails(jeeyohGroupModel);
			//}
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Group name already exists");
		}

		return baseResponse;
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

		/*List<User> usersList = userDAO.getUserContacts(userId);
		logger.debug("usersList =====>"+usersList);
		if(usersList!=null)
		{
			List<UserModel> userModelList = new ArrayList<UserModel>();
			for(User user: usersList)
			{
				if(user.getUserId()!=userId)
				{
					UserModel userModel = new UserModel();
					userModel.setUserId(user.getUserId());
					userModel.setEmailId(user.getEmailId());
					userModel.setFirstName(user.getFirstName());
					userModel.setLastName(user.getLastName());
					userModelList.add(userModel);
				}

			}
			addGroupButtonResponse.setUserModel(userModelList);
		}
*/

		return addGroupButtonResponse;

	}


	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public GroupDetailResponse getGroupDetails(int groupId) {
		Jeeyohgroup jeeyohgroup = groupDAO.getGroupByGroupId(groupId);
		GroupDetailResponse groupDetailResponse = new GroupDetailResponse();
		if(jeeyohgroup != null)
		{
			JeeyohGroupModel jeeyohGroupModel = new JeeyohGroupModel();
			jeeyohGroupModel.setGroupId(jeeyohgroup.getGroupId());
			jeeyohGroupModel.setGroupName(jeeyohgroup.getGroupName());
			jeeyohGroupModel.setUserId(jeeyohgroup.getUserByCreatorId().getUserId());
			jeeyohGroupModel.setGroupType(jeeyohgroup.getGroupType());
			jeeyohGroupModel.setPrivacy(jeeyohgroup.getPrivacy().getPrivacyType());
			List<UserModel> groupMembers = new ArrayList<UserModel>();
			Set<Groupusermap> groupusermaps   = jeeyohgroup.getGroupusermaps();
			for (Groupusermap groupusermap : groupusermaps)
			{
				User user = groupusermap.getUser();
				if(user.getUserId() != jeeyohGroupModel.getUserId())
				{
					UserModel userModel = new UserModel();
					userModel.setUserId(user.getUserId());
					userModel.setEmailId(user.getEmailId());
					userModel.setFirstName(user.getFirstName());
					userModel.setLastName(user.getLastName());
					groupMembers.add(userModel);
				}
			}
			jeeyohGroupModel.setGroupMembers(groupMembers);
			groupDetailResponse.setGroupDetail(jeeyohGroupModel);
			groupDetailResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			groupDetailResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			groupDetailResponse.setError("Error");
		}
		return groupDetailResponse;
	}


	@Transactional
	@Override
	public GroupListResponse getGroupList(int userId) {
		logger.debug("getGroupList =====>"+userId);
		List<Jeeyohgroup> groupList = groupDAO.getGroupsByCreator(userId);
		List<JeeyohGroupModel>	groupModels = new ArrayList<JeeyohGroupModel>();
		if(groupList != null)
		{
			for(Jeeyohgroup jeeyohGroup : groupList) 
			{
				JeeyohGroupModel jeeyohGroupModel = new JeeyohGroupModel();
				jeeyohGroupModel.setGroupName(jeeyohGroup.getGroupName());
				jeeyohGroupModel.setGroupId(jeeyohGroup.getGroupId());
				jeeyohGroupModel.setTotalMembers(jeeyohGroup.getGroupusermaps().size());
				groupModels.add(jeeyohGroupModel);
			}
		}

		GroupListResponse groupListResponse = new GroupListResponse();
		groupListResponse.setGroups(groupModels);
		groupListResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		return groupListResponse;
	}

	@Transactional
	@Override
	public BaseResponse editGroup(AddGroupModel addGroupModel) {
		BaseResponse baseResponse =new BaseResponse();
		Jeeyohgroup jeeyohGroup = groupDAO.getGroupByGroupId(addGroupModel.getGroupId());
		if(jeeyohGroup != null)
		{
			// Check privacy id
			Privacy privacyObj = userDAO.getUserPrivacyType(addGroupModel.getPrivacy());

			Notificationpermission notificationPermission = userDAO.getDafaultNotification();
			jeeyohGroup.setAbout(addGroupModel.getGroupName());
			jeeyohGroup.setGroupName(addGroupModel.getGroupName());
			jeeyohGroup.setGroupType(addGroupModel.getCategory());
			jeeyohGroup.setPrivacy(privacyObj);
			jeeyohGroup.setUpdatedtime(Utils.getCurrentDate());
			ArrayList<Integer> groupMembers = addGroupModel.getMember();
			if(groupMembers!=null)
			{
				Set<Groupusermap> groupUserMapSet = new HashSet<Groupusermap>();
				for(Integer memberId:groupMembers)
				{
					logger.debug("member id  =====>"+memberId);
					Groupusermap groupUserMapDetails = groupDAO.isContactInGroup(memberId, addGroupModel.getGroupId());
					if(groupUserMapDetails == null)
					{
						User contactUser = new User();
						contactUser.setUserId(memberId);
						
						Groupusermap groupUserMap = new Groupusermap();
						groupUserMap.setCreatedtime(Utils.getCurrentDate());
						groupUserMap.setJeeyohgroup(jeeyohGroup);
						groupUserMap.setUpdatedtime(Utils.getCurrentDate());
						groupUserMap.setNotificationpermission(notificationPermission);
						groupUserMap.setUser(contactUser);
						groupUserMapSet.add(groupUserMap);
					}
				}
				jeeyohGroup.setGroupusermaps(groupUserMapSet);
			}

			groupDAO.updateJeeyohGroup(jeeyohGroup);
			logger.debug("Data updated successfully");
			baseResponse.setStatus(ServiceAPIStatus.OK.getStatus());
		}
		else
		{
			baseResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
			baseResponse.setError("Group not found");
		}

		return baseResponse;
	}

}
