package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.persistence.domain.Deals;

public interface IUserDealsSearch {

	public List<Deals> search(String userEmail);
}
