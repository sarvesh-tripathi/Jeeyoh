package com.jeeyoh.persistance;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.jeeyoh.domain.Groupondeals;

@Repository("dealsDAO")
public class DealsDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Adds a groupon deal into the database
	 * @param deal
	 */
	/*public void addDeals(Groupondeals deal) {
		sessionFactory.getCurrentSession().saveOrUpdate(deal);		
	}*/
	
	/**
	 * Returns the list of deals
	 * @return
	 */
	/*public List<Groupondeals> getDeals() {
		return null;
	}*/
	
	/**
	 * Returns a specific deal by dealID
	 * @param dealId
	 * @return
	 */
	/*public Groupondeals getDealById(String dealId) {
		return null;
	}*/
	
	/**
	 * Returns the list of deals by division.
	 * @param divisionId
	 * @return
	 */
	/*public List<Groupondeals> getDealsByDivision(String divisionId)
	{
		return null;
	}*/
	
	

}
