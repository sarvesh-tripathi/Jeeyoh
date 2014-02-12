package com.jeeyoh.service.search;

import java.util.Set;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.domain.Deals;

public interface ISearchDeals {

	Set<DealModel> getDeals(String keyword, String category, String location , String emailId);

	Set<DealModel> getUserDeals(String emailId);

	Set<DealModel> getUserContactAndCommunityDeals(String emailId);

}
