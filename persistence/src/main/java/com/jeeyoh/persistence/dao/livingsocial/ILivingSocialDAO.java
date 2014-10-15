package com.jeeyoh.persistence.dao.livingsocial;


import java.util.List;

import com.jeeyoh.persistence.domain.Lcategory;
import com.jeeyoh.persistence.domain.Ldeal;
import com.jeeyoh.persistence.domain.LdealCategory;
import com.jeeyoh.persistence.domain.LdealOption;

public interface ILivingSocialDAO {
	public void addLdeals(Ldeal ldeal, int batch_size);
	public void addLdealOptions(LdealOption ldealOption);
	public void addLdealCategory(LdealCategory ldealCategory);
	public Ldeal getLdeal(int dealId);
	public List<LdealOption> getDeals();
	public List<Ldeal> getLDeals();
	public List<LdealOption> getDealOptions(int dealId);
	public List<Lcategory> getCategoryParent(String categoryList);
	//public Lcategory getLCategory(String categoryName);
}
