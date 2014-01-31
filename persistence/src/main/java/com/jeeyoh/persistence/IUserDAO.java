package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

public interface IUserDAO {
	public List<User> getUsers();
	
	public User getUsersById(String id);
	
	public List<User> getUserContacts(int userId);
	
	public List<Page> getUserCommunities(int userId);
	
	public List<Page> getUserContactsCommunities(int contactId);
	
	public List<Jeeyohgroup> getUserGroups(int userId);
	
	public List<Jeeyohgroup> getUserContactGroups(int contactId);
	public List<Pageuserlikes> getUserPageProperties(int userId, int pageId);
	public List<Pagetype> getCommunityType(int pageId);
	public void saveNonDealSuggestions(Usernondealsuggestion suggestion, int batch_size);
	public List<User> getUserById(int userid);
	public List<Events> getUserCommunityEvents(int userId, int pageId);
	public List<Eventuserlikes> getUserEventProperties(int userId, int eventId);
	public List<Usernondealsuggestion> isNonDealSuggestionExists(int userId, int businessId);
	public List<Usernondealsuggestion> getuserNonDealSuggestionsByEmailId(String emailId);
	public List<Page> getUserCommunitiesByEmailId(String emailId);
	public List<UserCategory> getUserCategoryLikesById(int userId);
	public List<UserCategory> getUserCategoryLikesByType(int userId, String category);
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType);
	public int userCategoryLikeCount(Integer userCategoryId);
	public List<Usercontacts> getAllUserContacts(int userId);
	public void saveEventsSuggestions(Usereventsuggestion suggestion, int batch_size);
	public List<Events> getUserLikesEvents(int userId);
	public List<Events> getUserLikesEventsByType(int userId, String pageType);
	public List<Usereventsuggestion> isEventSuggestionExists(int userId, int eventId);

	public List<Userdealssuggestion> isDealSuggestionExists(Integer userId,
			Integer id);

}
