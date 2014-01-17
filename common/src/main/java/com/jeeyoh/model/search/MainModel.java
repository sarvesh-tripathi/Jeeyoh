package com.jeeyoh.model.search;

<<<<<<< HEAD
import java.util.Set;

import com.jeeyoh.persistence.domain.Deals;

public class MainModel {
	
	Set<Deals> dealModel;

	public Set<Deals> getDealModel() {
		return dealModel;
	}

	public void setDealModel(Set<Deals> dealModel) {
		this.dealModel = dealModel;
	}

	

=======
import java.util.List;

public class MainModel {
	
	List<DealModel> dealModel;

	public List<DealModel> getDealModel() {
		return dealModel;
	}

	public void setDealModel(List<DealModel> dealModel) {
		this.dealModel = dealModel;
	}

>>>>>>> 1846fe671c0cdaf33ff00c596a7334be53d58b17
}
