package com.jeeyoh.service.search;

import java.util.List;

import com.jeeyoh.model.search.DealModel;
import com.jeeyoh.persistence.domain.Deals;

public interface IUserDealsSearch {

	public List<DealModel> search(String userEmail);
}
