package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.DealModel;

public interface ISearchDeals {

	List<DealModel> getDeals(String keyword, String category, String location , String emailId);

	List<DealModel> getUserDeals(String emailId);

	List<DealModel> getUserContactAndCommunityDeals(String emailId);

}
