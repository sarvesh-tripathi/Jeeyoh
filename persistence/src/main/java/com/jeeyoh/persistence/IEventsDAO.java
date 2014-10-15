package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.CommunityComments;
import com.jeeyoh.persistence.domain.CommunityReviewMap;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Eventuserlikes;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.Pageuserlikes;
import com.jeeyoh.persistence.domain.PopularCommunity;

public interface IEventsDAO {
	
	public void saveEvents(Events events, int batch_size);
	public void saveEvents(List<Events> events, int batch_size);
	public List<Events> getEventsByCriteria(String userEmail, String searchText,
			String category, String location);
	public List<Page> getCommunities();
	public List<Events> getEventsByCommunityId(int pageId);
	public List<Events> getEventsByCommunityType(int pageId, int pageType);
	public List<Events> getEventsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating);
	public List<Events> getEventsBySearchKeyword(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, boolean forExactMatch);
	public List<Page> getCommunityByLikeSearchKeywordForBusiness(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating);
	public List<Page> getCommunityByLikeSearchKeywordForEvents(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating);
	public List<Page> getCommunityBySearchKeywordForBusiness(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, boolean forExactMatch);
	public List<Page> getCommunityBySearchKeywordForEvents(String searchText,String category, String location, int offset, int limit, double lat, double lon, int distance, double rating, boolean forExactMatch);
	public List<Page> getCommunityPageByCategoryType(String category, int userId);
	public List<Page> getUserFavourites(Integer userId);
	public Page getCommunityById(int pageId);
	public Page getPageDetailsByID(int pageId);
	public List<Events> getCurrentEvents(int pageId, int offset, int limit);
	public List<Events> getUpcomingEvents(int pageId, int offset, int limit);
	public List<Events> getPastEvents(int pageId, int offset, int limit);
	public List<Events> getBookedEvents(int userId, String category);
	public List<Events> getUserEventsSuggestions(String userEmail,
			int offset, int limit, String category, String suggestionType, double lat, double lon, int distance, double rating);
	public List<Object[]> getEventLikeCountByPage(String idsStr);
	public List<Events> getEventsByuserLikes(String likekeyword,
			   String itemCategory, String providerName, double latitude, double longitude);
	public Events getEventById(int eventId);
	public int getTotalEventsBySearchKeyWord(String searchText,String category, String location, double lat, double lon, int distance, double rating);
	public int getTotalCommunityBySearchKeyWordForBusiness(String searchText,String category, String location, double lat, double lon, int distance, double rating);
	public int getTotalCommunityBySearchKeyWordForEvent(String searchText,String category, String location, double lat, double lon, int distance, double rating);
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
	public double getCommunityReviewByPageId(int pageId);
	public Object[] getRecentEventDate(int pageId);
	public Object[] getRecentEventDetails(int pageId);
	public List<Events> getEventsByuserLikesForCurrentWeekend(String likekeyword,
			String itemCategory, String providerName, double latitude, double longitude);
	public List<Page> getCommunityBySearchKeyword(String searchText, String category, int offset, int limit, String abbreviation);
	public int getRecentEvent(int pageId);
	public List<Integer> getEventsByPgaeId(int pageId);
	public Object[] getPagesAvergeRatingAndDetails(int pageId);
	public List<PopularCommunity> getPopularCommunityList();
	public Pagetype getPopularComunityType(int communityId);
	public int getTotalCommunityForCommunitySearch(String searchText,
			String category, String location, String abbreviation);

}
