package com.jeeyoh.service.search;

<<<<<<< HEAD
import java.util.Set;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.domain.Deals;

public interface ISearchDeals {

	Set<Deals> getDeals(String keyword, String category, String location , String emailId);

	Set<Deals> getUserDeals(String emailId);

	Set<Deals> getUserContactAndCommunityDeals(String emailId);
=======
import java.util.List;

import com.jeeyoh.model.search.DealModel;

public interface ISearchDeals {

	List<DealModel> getDeals(String keyword, String category, String location , String emailId);

	List<DealModel> getUserDeals(String emailId);

	List<DealModel> getUserContactAndCommunityDeals(String emailId);
>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17

}
