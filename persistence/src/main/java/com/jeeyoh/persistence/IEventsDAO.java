package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.CommunityReview;
import com.jeeyoh.persistence.domain.CommunityReviewMap;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;

public interface IEventsDAO {
	
	public void saveEvents(Events events, int batch_size);
	public List<Events> getEventsByCriteria(String userEmail, String searchText,
			String category, String location);
	public List<Page> getCommunities();
	public List<Events> getEventsByCommunityId(int pageId);
	public List<Events> getEventsByCommunityType(int pageId, int pageType);
	public List<Events> getEventsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Events> getEventsBySearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Page> getCommunityByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Page> getCommunityBySearchKeyword(String searchText,String category, String location, int offset, int limit);
	public List<Page> getCommunityPageByCategoryType(String category, int userId);
	public List<Page> getUserFavourites(Integer userId);
	public Page getCommunityById(int pageId);
	public Page getPageDetailsByID(int pageId);
	public List<Events> getCurrentEvents(int pageId, int offset, int limit);
	public List<Events> getUpcomingEvents(int pageId, int offset, int limit);
	public List<Events> getPastEvents(int pageId, int offset, int limit);
	public List<Events> getBookedEvents(int userId);
	public List<Events> getUserEventsSuggestions(String userEmail,
			int offset, int limit);
	public List<Object[]> getEventLikeCountByPage(String idsStr);
	public List<Events> getEventsByuserLikes(String likekeyword,
			   String itemCategory, String providerName, double latitude, double longitude);
	public Events getEventById(int eventId);
	public int getTotalEventsBySearchKeyWord(String searchText,String category, String location);
	public int getTotalCommunityBySearchKeyWord(String searchText,String category, String location);
	public Pagetype getPageTypeByName(String pageType);
	public Page getPageByAbout(String genre_parent_name);
	public void savePage(Page page, int batch_size);
	public List<CommunityComments> getCommunityCommentsByPageId(int pageId);
	public void saveCommunityComments(CommunityComments communityComments);
	public Page getPageByBusinessId(Integer itemId);
	public Eventuserlikes isEventExistInUserProfile(int userId, int eventId);
	public boolean updateUserEvents(Eventuserlikes eventuserlikes);
	public boolean saveUserEvents(Eventuserlikes eventuserlikes);
	public boolean updatePageUserLikes(Pageuserlikes pageuserlikes);
	public boolean savePageUserLikes(Pageuserlikes pageuserlikes);
	public void saveCommunityReview(CommunityReviewMap communityReviewMap);
	public List<CommunityReview> getCommunityReviewByPageId(int pageId);

}
