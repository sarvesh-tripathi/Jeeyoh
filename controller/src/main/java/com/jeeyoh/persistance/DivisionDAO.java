package com.jeeyoh.persistance;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

//import com.jeeyoh.domain.Groupondeals;
//import com.jeeyoh.domain.Groupondivision;


public class DivisionDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	/*public void addGrouponDivision(Groupondivision division) {
		sessionFactory.getCurrentSession().saveOrUpdate(division);		
	}*/
	
	/*public List<Groupondivision> getGrouponDivisions() {
		List<Groupondivision> divisions = null;
		String query = "from Groupondivision";
		try
		{
			divisions = (List<Groupondivision>)sessionFactory.getCurrentSession().createQuery(query).list();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return divisions;
	}*/
	
	/*public List<Groupondivision> getGrouponDivisionsByCountry(String country) {
		List<Groupondivision> divisions = null;
		String hqlQuery = "from Groupondivision a where a.country = :country";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("country", country);
			divisions = (List<Groupondivision>)query.list();
			//divisions = (List<Groupondivision>)sessionFactory.getCurrentSession().createQuery(query).list();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return divisions;
	}*/

}
