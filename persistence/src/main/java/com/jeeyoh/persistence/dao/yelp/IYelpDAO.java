package com.jeeyoh.persistence.dao.yelp;

import java.util.List;

import com.jeeyoh.persistence.domain.Ybusiness;
import com.jeeyoh.persistence.domain.Ycategoryfilter;
import com.jeeyoh.persistence.domain.Ydeal;
import com.jeeyoh.persistence.domain.Ydealoption;
import com.jeeyoh.persistence.domain.Yreview;

public interface IYelpDAO {
	public Ycategoryfilter getCategories(String category);
	public void saveBusiness(Ybusiness business,int count);
	public List<Ybusiness> getBusiness();
	public void saveDeals(Ydeal ydeal, int count);
	public void saveReviews(Yreview yreview, int count);
	public List<Ydealoption> filterdDealByDiscount();
	public List<Ybusiness> getFilterBusiness();

}
