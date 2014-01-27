package com.jeeyoh.service.search;

import java.util.Set;

import com.jeeyoh.persistence.domain.Deals;

public interface ISearchDeals {

	Set<Deals> getDeals(String keyword, String category, String location , String emailId);

	Set<Deals> getUserDeals(String emailId);

	Set<Deals> getUserContactAndCommunityDeals(String emailId);

}
