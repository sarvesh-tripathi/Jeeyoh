package com.jeeyoh.service.addfriend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.FriendListResponse;
import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.Usercontacts;

@Component("addFriendService")
public class AddFriendService implements IAddFriendService{
	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;
	
	@Autowired
	private IUserDAO userDAO;

	@Override
	@Transactional
	public FriendListResponse searchFriend(String location, String firstName, String lastName, int userId) {
		FriendListResponse friendListResponse = new FriendListResponse();
		try
		{
			logger.debug("in searchFriend ====> location :: "+location+" ; name :: "+firstName+ " ; userId :: "+userId);
			List<UserModel> userModelList = new ArrayList<UserModel>();
			List<Integer> friendsId = new ArrayList<Integer>();
			List<Integer> addedIds = new ArrayList<Integer>();
			StringBuilder friendIdsStr = new StringBuilder();
			StringBuilder addedIdsStr = new StringBuilder();
			// Get user contacts
			List<User> allUserFriendsList =  userDAO.getUserContacts(userId);
			logger.debug("Contact:::  "+allUserFriendsList);
			addedIds.add(userId);
			addedIdsStr.append(userId+",");
			if(allUserFriendsList!=null)
			{
				for(User friends:allUserFriendsList)
				{
					friendsId.add(friends.getUserId());
					addedIds.add(friends.getUserId());
					friendIdsStr.append(friends.getUserId()+",");
					addedIdsStr.append(friends.getUserId()+",");
				}
			}

			//Search by location and name and should not be in friend list
			if(location!=null && !location.equals(""))
			{
				logger.debug("searching by location and name and should not be in friend list====> ");
				List<User> searchUserByLocationAndNameList = userDAO.getUserByNameAndLocation(location, firstName, lastName, userId, friendsId);
				if(searchUserByLocationAndNameList!=null && !searchUserByLocationAndNameList.equals(""))
				{
					logger.debug("searchUserByLocationAndNameList ===>"+searchUserByLocationAndNameList.size());
					for(User user:searchUserByLocationAndNameList)
					{
						logger.debug("adding in user model"+user.getUserId()+"; name: "+user.getFirstName());
						UserModel userModel = new UserModel();
						userModel.setAddressline1(user.getAddressline1());
						userModel.setBirthDate(user.getBirthDate());
						userModel.setBirthMonth(user.getBirthMonth());
						userModel.setBirthYear(user.getBirthYear());
						userModel.setCity(user.getCity());
						userModel.setConfirmationId(user.getConfirmationId());
						userModel.setCountry(user.getCountry());
						userModel.setCreatedtime(user.getCreatedtime().toString());
						userModel.setEmailId(user.getEmailId());
						userModel.setFirstName(user.getFirstName());
						userModel.setGender(user.getGender());
						userModel.setIsActive(user.getIsActive());
						userModel.setIsDeleted(user.getIsDeleted());
						userModel.setLastName(user.getLastName());
						userModel.setMiddleName(user.getMiddleName());
						userModel.setPassword(user.getPassword());
						userModel.setSessionId(user.getSessionId());
						userModel.setState(user.getState());
						userModel.setStreet(user.getStreet());
						userModel.setUpdatedtime(user.getUpdatedtime().toString());
						userModel.setUserId(user.getUserId());
						userModel.setZipcode(user.getZipcode());
						if(user.getImageUrl() != null)
							userModel.setImageUrl(hostPath + user.getImageUrl());
						addedIds.add(user.getUserId());
						addedIdsStr.append(user.getUserId()+",");
						userModelList.add(userModel);
					}
				}

			}	

			// Search by friends of friends
			logger.debug("searching in friends of friends ====> ");
			logger.debug("allUserFriendsList ====>"+allUserFriendsList.size());
			if(!friendIdsStr.equals("") && friendIdsStr.length() > 0)
				friendIdsStr.deleteCharAt(friendIdsStr.length() - 1);
			if(!addedIdsStr.equals("") && addedIdsStr.length() > 0)
				addedIdsStr.deleteCharAt(addedIdsStr.length() - 1);
			logger.debug("addedIdsStr ==>"+addedIdsStr+ "friendIdsStr ==>"+friendIdsStr);
			List<User> searchMutualFriendsList = userDAO.findInMutualFriends(firstName, lastName, friendIdsStr.toString(), addedIdsStr.toString());
			if(searchMutualFriendsList!=null && !searchMutualFriendsList.equals(""))
			{
				logger.debug("searchMutualFriendList ===>"+searchMutualFriendsList.size());
				for(User user:searchMutualFriendsList)
				{
					logger.debug("adding in user model"+user.getUserId()+"; name "+user.getFirstName());
					UserModel userModel = new UserModel();
					userModel.setAddressline1(user.getAddressline1());
					userModel.setBirthDate(user.getBirthDate());
					userModel.setBirthMonth(user.getBirthMonth());
					userModel.setBirthYear(user.getBirthYear());
					userModel.setCity(user.getCity());
					userModel.setConfirmationId(user.getConfirmationId());
					userModel.setCountry(user.getCountry());
					userModel.setCreatedtime(user.getCreatedtime().toString());
					userModel.setEmailId(user.getEmailId());
					userModel.setFirstName(user.getFirstName());
					userModel.setGender(user.getGender());
					userModel.setIsActive(user.getIsActive());
					userModel.setIsDeleted(user.getIsDeleted());
					userModel.setLastName(user.getLastName());
					userModel.setMiddleName(user.getMiddleName());
					userModel.setPassword(user.getPassword());
					userModel.setSessionId(user.getSessionId());
					userModel.setState(user.getState());
					userModel.setStreet(user.getStreet());
					userModel.setUpdatedtime(user.getUpdatedtime().toString());
					userModel.setUserId(user.getUserId());
					userModel.setZipcode(user.getZipcode());
					if(user.getImageUrl() != null)
						userModel.setImageUrl(hostPath + user.getImageUrl());
					addedIds.add(user.getUserId());
					userModelList.add(userModel);
				}
			}

			// Search others
			logger.debug("searching others ====> ");

			if((firstName != null && !firstName.trim().equals("")) || (lastName != null && !lastName.trim().equals("")))
			{
				List<User> searchOtherThanFriendsList = userDAO.findOtherThanMutualFriends(firstName, lastName, addedIds);
				if(searchOtherThanFriendsList!=null && !searchOtherThanFriendsList.equals(""))
				{
					logger.debug("searchOtherThanFriendsList ===>"+searchOtherThanFriendsList.size());
					for(User user:searchOtherThanFriendsList)
					{
						logger.debug("adding in user model"+user.getUserId()+"; name "+user.getFirstName());
						UserModel userModel = new UserModel();
						userModel.setAddressline1(user.getAddressline1());
						userModel.setBirthDate(user.getBirthDate());
						userModel.setBirthMonth(user.getBirthMonth());
						userModel.setBirthYear(user.getBirthYear());
						userModel.setCity(user.getCity());
						userModel.setConfirmationId(user.getConfirmationId());
						userModel.setCountry(user.getCountry());
						userModel.setCreatedtime(user.getCreatedtime().toString());
						userModel.setEmailId(user.getEmailId());
						userModel.setFirstName(user.getFirstName());
						userModel.setGender(user.getGender());
						userModel.setIsActive(user.getIsActive());
						userModel.setIsDeleted(user.getIsDeleted());
						userModel.setLastName(user.getLastName());
						userModel.setMiddleName(user.getMiddleName());
						userModel.setPassword(user.getPassword());
						userModel.setSessionId(user.getSessionId());
						userModel.setState(user.getState());
						userModel.setStreet(user.getStreet());
						userModel.setUpdatedtime(user.getUpdatedtime().toString());
						userModel.setUserId(user.getUserId());
						userModel.setZipcode(user.getZipcode());
						if(user.getImageUrl() != null)
							userModel.setImageUrl(hostPath + user.getImageUrl());
						userModelList.add(userModel);
					}
				}
			}

			friendListResponse.setUser(userModelList);
			friendListResponse.setStatus(ServiceAPIStatus.OK.getStatus());
			logger.debug("Done ==>");
		}
		catch(Exception e)
		{
			logger.debug("Error:::: "+e.getMessage());
			friendListResponse.setStatus(ServiceAPIStatus.FAILED.getStatus());
		}
		return friendListResponse;
	}

	@Override
	@Transactional
	public BaseResponse sendFriendRequest(int userId, int contactId)
	{

		BaseResponse response = new BaseResponse();
		Usercontacts userContact = userDAO.isUsercontactExists(userId, contactId);
		if(userContact == null)
		{
			User user = userDAO.getUserById(userId);
			User contact = userDAO.getUserById(contactId);
			// Add new row in user contact table
			Usercontacts userContacts = new Usercontacts();
			if(userId!=0 && contactId!=0)
			{
				userContacts.setUserByUserId(user);
				userContacts.setUserByContactId(contact);
				userContacts.setIsApproved(false);
				userContacts.setIsBlock(false);
				userContacts.setIsDeleted(false);
				userContacts.setIsDeny(false);
				userContacts.setIsStar(false);
				userContacts.setIsActive(true);
				userContacts.setUpdatedtime(new Date());
				userContacts.setCreatedtime(new Date());
				userDAO.saveUsercontacts(userContacts);
				response.setStatus("Friend request sent");
			}
		}
		else
		{
			if(userContact.getUserByUserId().getUserId() == userId)
				response.setStatus("Friend request already sent");
			else
				response.setStatus("You have pending request from this user");
		}
		return response;
	}

	@Override
	@Transactional
	public BaseResponse acceptFriendRequest(int userId, int contactId) {
		BaseResponse response = new BaseResponse();
		Usercontacts userContact = userDAO.isUsercontactExists(userId, contactId);
		logger.debug("acceptFriendRequest ==>"+userContact.getUserByUserId().getFirstName());
		if(userContact!=null && !userContact.equals(""))
		{
			logger.debug("first if ====>");
			if(!userContact.getIsApproved())
			{
				logger.debug("second if ====>");
				userContact.setIsApproved(true);
				userDAO.updateUsercontacts(userContact);
				response.setStatus("Friend request accepted");
			}
			else
			{
				response.setStatus("Request is already accepted");
			}
		}
		return response;
	}

	@Override
	@Transactional
	public BaseResponse denyFriendRequest(int userId, int contactId) {
		BaseResponse response = new BaseResponse();
		Usercontacts userContact = userDAO.isUsercontactExists(userId, contactId);
		if(userContact!=null && !userContact.equals(""))
		{
			if(!userContact.getIsDeny())
			{
				userContact.setIsDeny(true);
				userContact.setIsApproved(false);
				userContact.setIsBlock(false);
				userDAO.updateUsercontacts(userContact);
				response.setStatus("Friend request denied");
			}
			else
			{
				response.setStatus("Request is already denied");
			}
		}
		return response;
	}

	@Override
	@Transactional
	public BaseResponse blockFriendRequest(int userId, int contactId) {
		BaseResponse response = new BaseResponse();
		Usercontacts userContact = userDAO.isUsercontactExists(userId, contactId);
		if(userContact!=null && !userContact.equals(""))
		{
			if(!userContact.getIsBlock())
			{
				userContact.setIsBlock(true);
				userContact.setIsDeny(false);
				userContact.setIsApproved(false);
				userDAO.updateUsercontacts(userContact);
				response.setStatus("Request blocked by this user");
			}
			else
			{
				response.setStatus("USer already blocked");
			}
		}
		return response;
	}
}
