package com.jeeyoh.persistence;

import java.util.List;
import java.util.Set;

import com.jeeyoh.model.user.UserModel;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Notificationpermission;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.Privacy;
import com.jeeyoh.persistence.domain.Profiletype;
import com.jeeyoh.persistence.domain.Topcommunitysuggestion;
import com.jeeyoh.persistence.domain.Topdealssuggestion;
import com.jeeyoh.persistence.domain.Topeventsuggestion;
import com.jeeyoh.persistence.domain.Topnondealsuggestion;
import com.jeeyoh.persistence.domain.User;
import com.jeeyoh.persistence.domain.UserCategory;
import com.jeeyoh.persistence.domain.UserCategoryLikes;
import com.jeeyoh.persistence.domain.Usercontacts;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.persistence.domain.Usereventsuggestion;
import com.jeeyoh.persistence.domain.Usernondealsuggestion;

public interface IUserDAO {
	public List<User> getUsers();

	public User getUserByEmailId(String emailId);

	public List<User> getUserContacts(int userId);

	public List<Page> getUserCommunities(int userId, double latitude, double longitude);

	public List<Page> getUserContactsCommunities(int contactId);

	public List<Jeeyohgroup> getUserGroups(int userId);

	public List<Jeeyohgroup> getUserContactGroups(int contactId);
	public Pageuserlikes getUserPageProperties(int userId, int pageId);
	public List<Pagetype> getCommunityType(int pageId);
	public void saveNonDealSuggestions(Usernondealsuggestion suggestion, int batch_size);
	public User getUserById(int userid);
	public List<Events> getUserCommunityEvents(int userId, int pageId);
	public List<Eventuserlikes> getUserEventProperties(int userId, int eventId);
	public List<Usernondealsuggestion> isNonDealSuggestionExists(int userId, int businessId);
	public List<Usernondealsuggestion> getuserNonDealSuggestionsByEmailId(String emailId);
	public List<Page> getUserCommunitiesByEmailId(String emailId);
	public List<UserCategory> getUserCategoryLikesById(int userId);
	public List<UserCategory> getUserCategoryLikesByType(int userId, String category);
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType, double latitude, double longitude);
	public int userCategoryLikeCount(Integer userCategoryId);
	public List<Object[]> getAllUserContacts(int userId);
	public void saveEventsSuggestions(Usereventsuggestion suggestion, int batch_size);
	public List<Events> getUserLikesEvents(int userId, double latitud, double longitude);
	public List<Events> getUserLikesEventsByType(int userId, String pageType, double latitud, double longitude);
	public List<Usereventsuggestion> isEventSuggestionExists(int userId, int eventId);

	public List<Userdealssuggestion> isDealSuggestionExists(Integer userId,
			Integer id);
	public void registerUser(User user);

	public List<Dealsusage> getUserDealUsageByType(Integer userId,
			String groupType, double latitude, double longitude);

	public User loginUser(UserModel user);
	public List<Events> getCommunityAllEvents(int pageId, double latitud, double longitude);

	public Privacy getUserPrivacyType(String string);

	public Profiletype getUserprofileType(String string);
	public void updateUser(User user);

	public void confirmUser(String confirmationCode);

	public Pageuserlikes isPageExistInUserProfile(int userId, int pageId);

	public void saveUserCommunity(Pageuserlikes pageUserLike);

	public void updateUserCommunity(Pageuserlikes pageuserlikes);

	public Notificationpermission getDafaultNotification();

	public void deleteUserFavourity(int id, int userId);
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForCommunity(int userId, String category);
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForJeeyoh(int userId, String category);
	public List<Usernondealsuggestion> getUserNonDealsSuggestionByUserIdForFriends(int userId, int contactId, String category);
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForFriends(int userId, int contactId, String category);
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForJeeyoh(int userId, String category);
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForFriends(int userId, int contactId, String category);
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForJeeyoh(int userId, String category);
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForCommunity(int userId, String category);
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForCommunity(int userId, String category);
	public List<Object[]> userNonDealSuggestionCount(String pageIdsStr, int limit);
	public List<Object[]> userDealSuggestionCount(String dealIdsStr, int limit);
	public List<Object[]> userEventSuggestionCount(String eventIdsStr, int limit);
	public void saveTopNonDealSuggestions(Topnondealsuggestion topnondealsuggestion);
	public void saveTopDealSuggestions(Topdealssuggestion topdealssuggestion);
	public void saveTopEventSuggestions(Topeventsuggestion topeventsuggestion);

	public boolean isUserActive(String emailId);	
	public List<Topnondealsuggestion> isTopNonDealSuggestionExists(int userId, int businessId);
	public List<Topdealssuggestion> isTopDealSuggestionExists(int userId, int dealId);
	public List<Topeventsuggestion> isTopEventSuggestionExists(int userId, int eventId);
	public List<Object[]> getuserCommunitySuggestionsByLikesCount(int userId, String category);
	public void saveTopCommunitySuggestions(Topcommunitysuggestion topcommunitysuggestion);
	public List<Usereventsuggestion> getUserEventsSuggestionByUserIdForFriendsCommunity(int userId, int contactid, String category);
	public List<Userdealssuggestion> getUserDealSuggestionByUserIdForFriendsCommunity(int userId, int contactid, String category);
	public List<User> getStarFriends(int userId, Double lattitude,Double longitude);
	public long getUserPageFavouriteCount(String pageType, int userId);

	public List<UserCategory> getUserNonLikeCategories(int userId, String categoryType);

	public UserCategory getCategory(Integer userCategoryId);

	public void saveUserCategoryLike(UserCategoryLikes categoryLikes);

	public List<Topnondealsuggestion> getTopNonDealSuggestions(String userEmail, String suggestionType);
	public List<Topdealssuggestion> getTopDealSuggestions(String userEmail, String suggestionType);
	public List<Topeventsuggestion> getTopEventSuggestions(String userEmail, String suggestionType);
	public List<Topcommunitysuggestion> getTopCommunitySuggestions(String userEmail);
	public int getDealWightCount(int userId,int itemId);
	public int getEventWightCount(int userId,int itemId);
	public int getBusinessWightCount(int userId,int itemId);
	public int getPageWightCount(int userId,int itemId);
	public List<User> getAttendingUsersForBusiness(int id, int userId);
	public List<User> getAttendingUsersForDeals(int id, int userId);
	public List<User> getAttendingUsersForEvents(int id, int userId);
	public List<User> getAttendingUsersForpage(int id, int userId);
	public int getTotalUserNonDealSuggestions(Integer userId);
	public int getTotalUserDealSuggestions(Integer userId);
	public int getTotalUserEventSuggestions(Integer userId);
	public void saveJeeyohGroup(Jeeyohgroup jeeyohGroup);
	public void saveGroupUserMap(Groupusermap groupUserMap);
	public List<Page> getUserCommunitiesByPageType(int userId, String pageType);
	public List<Page> getUserCommunities(int userId);
	public List<Dealsusage> getUseDealUsage(Integer userId, double latitude, double longitude);
	public UserCategoryLikes getUserCategoryLikes(int userId, int categoryId);
	public Dealsusage getUserLikeDeal(Integer userId,Integer itemId);
	public Eventuserlikes getUserLikeEvent(Integer userId,Integer itemId);
	public Pageuserlikes getUserLikeCommunity(Integer userId,Integer itemId);
	public List<User> getUserByNameAndLocation(String location, String name, int userId, List<Integer> friendsIds);
	public void saveUsercontacts(Usercontacts userContacts);
	public void updateUsercontacts(Usercontacts userContacts);
	public List<User> findInMutualFriends(String name, String friendsIds, String alreadyExistingIds);
	public List<User> findOtherThanMutualFriends(String name, List<Integer> alreadyExistingIds);
	public Usercontacts isUsercontactExists(int userId, int contactId);
}
