package com.jeeyoh.persistence.dao.groupon;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Gdivision;

@Repository("divisionDAO")
public class DivisionDAO implements IDivisionDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addDivision(Gdivision division) {
		sessionFactory.getCurrentSession().saveOrUpdate(division);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gdivision> getDivisions() {
		List<Gdivision> divisionList = null;
		String query = "from Gdivision";
		try {
			divisionList = (List<Gdivision>) sessionFactory.getCurrentSession()
					.createQuery(query).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return divisionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gdivision> getDivisionsByCountry(String country) {
		List<Gdivision> divisionList = null;
		//Session session = sessionFactory.openSession();
	     Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "from Gdivision a where a.country = :country";
		try {
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("country", country);
			divisionList = (List<Gdivision>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*finally
		{
			session.close();
		}*/
		return divisionList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Gdivision> getDivisionsById(int id) {
		// TODO Auto-generated method stub
		List<Gdivision> divisionList = null;
		Session session = sessionFactory.openSession();
		String hqlQuery = "from Gdivision a where a.id = :id";
		try {
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("id", id);
			divisionList = (List<Gdivision>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return divisionList;
		
	}
}
