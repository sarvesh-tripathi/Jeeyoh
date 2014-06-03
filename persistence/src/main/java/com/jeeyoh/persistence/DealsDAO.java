package com.jeeyoh.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Dealoption;
import com.jeeyoh.persistence.domain.Dealredemptionlocation;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Dealsusage;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.Userdealssuggestion;
import com.jeeyoh.utils.Utils;


@Repository("dealsDAO")
public class DealsDAO implements IDealsDAO {

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Deals> getDeals() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByCategory(String category) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.businessTypeId = :category";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessTypeId", category);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}



	@Override
	public List<Deals> getDealsByCategory(String category, String location,
			boolean isZipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Deals> getDealsByZipCode(String zipCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDeal(Deals deals, int batch_size) 
	{
		logger.debug("saveDeal ==>");
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.saveOrUpdate(deals);	

			if( batch_size % 20 == 0 ) {
				session.flush();
				session.clear();
			}

			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			logger.error("EXCEPTION IN saveDeal==>  "+e);
		}
		finally
		{
			session.close();
		}
	}

	@Override
	public void saveDeal(Deals deals)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(deals);        
	}

	@Override
	public void saveFilterdDeal(Deals deal) {

		//sessionFactory.getCurrentSession().saveOrUpdate(deal);
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try
		{
			tx = session.beginTransaction();
			session.save(deal);
			tx.commit();
		}
		catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}
		finally
		{
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByBusinessId(String businessId) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.businessId = :businessId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("businessId", businessId);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}

	@Override
	public void saveSuggestions(Userdealssuggestion dealSuggestion) {

		sessionFactory.getCurrentSession().save(dealSuggestion);
		/*Session session = sessionFactory.openSession();
    		Transaction tx = null;
    		try
    		{
    			tx = session.beginTransaction();
    			session.save(dealSuggestion);
    			tx.commit();
    		}
    		catch (HibernateException e) {
    			if (tx!=null) tx.rollback();
    			e.printStackTrace(); 
    		}
    		finally
    		{
    			session.close();
    		}*/

	}

	@Override
	public int getDealsLikeCounts(Integer id) {
		// TODO Auto-generated method stub
		int rowCount = 0;
		String hqlQuery = "select count(a.dealUsageId) from Dealsusage a where a.deals.id = :id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("id", id);
			rowCount = ((Number)query.uniqueResult()).intValue();						

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByKeywords(String keyword, String category,
			String location) {
		logger.error("getDealsByKeywords ==== > "+location);
		logger.error("getDealsByKeywords ==== > "+location.isEmpty());
		// TODO Auto-generated method stub

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		//criteria.setProjection(Projections.distinct(Projections.property("dealId")));
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(category != null && category.isEmpty() == false)
		{
			logger.debug("IN CATEGORY CHECKING ::: ");
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			logger.debug("IN LOCATION CHECKING ::: ");
			criteria.add(Restrictions.eq("business.postalCode", location.trim()));
		}
		if(keyword != null && keyword.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("dealUrl", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("dealId", "%" + keyword.trim() + "%")));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getDealsSuggestion(int id)
	{
		logger.debug("IN user deal suggestion ::: "+id);
		List<Userdealssuggestion> dealSuggestionList = null;
		String hqlQuery = "from Userdealssuggestion a where a.user.userId = :id";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("id", id);
			dealSuggestionList = query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		return dealSuggestionList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getUserDeals(Integer userId) {
		// TODO Auto-generated method stub

		List<Deals> deals= null;
		String hqlQuery = "from Deals where id in(select d.deals.id from Dealsusage d where d.user.userId =:userId)";
		try
		{
			Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
			query.setParameter("userId", userId);
			deals = query.list();
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
		}
		logger.debug("Value === "+deals);
		return deals;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByBusinessId(Integer id) {
		// TODO Auto-generated method stub
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.business.id = :id and a.endAt >= :currentDate";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("id", id);
			query.setParameter("currentDate", Utils.getCurrentDate());
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType,String providerName, double latitude, double longitude) {
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(providerName != null)
		{
			criteria.add(Restrictions.eq("business.name", providerName));
		}
		if(itemCategory != null)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", itemCategory));
		}

		if(itemType != null)
		{
			criteria.add(Restrictions.like("title", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealUrl", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealId", "%" + itemType + "%"));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		List<Deals> dealsList = criteria.list();*/
		List<Deals> dealsList = null;
		String hqlQuery = "select a from Deals a, Business b, Businesstype c where c.businessType = :category and a.endAt >= :currentDate and a.business.id = b.id and b.businesstype.businessTypeId = c.businessTypeId and (a.title like '%"+ itemType + "%' or a.dealUrl like '%" + itemType + "%' or a.dealId like '%" + itemType + "%') and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 group by a.dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return dealsList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserEmail(String userEmail) {
		List<Deals> dealsList = null;
		String hqlQuery = "select distinct a from Deals a, Dealsusage b, User c where c.emailId = :emailId and c.userId = b.user.userId and a.id = b.deals.id";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("emailId", userEmail);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> userDealsSuggestedByJeeyoh(String keyword, String category,
			String location, String emailId) {
		logger.error("getDealsByKeywords ==== > "+location);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("deals", "deals");			
		criteria.createAlias("deals.business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", emailId));
		//criteria.createAlias("tags", "tags");
		if(category != null && category.isEmpty() == false)
		{
			logger.debug("IN CATEGORY CHECKING ::: ");
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			logger.debug("IN LOCATION CHECKING ::: ");
			criteria.add(Restrictions.eq("business.postalCode", location.trim()));
		}
		if(keyword != null && keyword.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("deals.title", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("deals.dealUrl", "%" + keyword.trim() + "%"))
					.add(Restrictions.like("deals.dealId", "%" + keyword.trim() + "%")));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		List<Userdealssuggestion> dealsList = criteria.list();
		return dealsList;
	}

	@Override
	public int userCategoryLikeCount(Integer userCategoryId) {
		int rowCount = 0;
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "select count(a.userCategoryLikesId) from UserCategoryLikes a where a.userCategory.userCategoryId=:userCategoryId";
		try
		{
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("userCategoryId", userCategoryId);
			rowCount = ((Number)query.uniqueResult()).intValue();	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByLikeSearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.error("getDealsByLikeSearchKeyword ==== > "+searchText);


		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}

		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + searchText + "%"))
					.add(Restrictions.like("dealUrl", "%" + searchText + "%"))
					.add(Restrictions.like("dealId", "%" + searchText + "%"))).add(Restrictions.conjunction().add(Restrictions.ne("title", searchText))
							.add(Restrictions.ne("dealUrl", searchText))
							.add(Restrictions.ne("dealId", searchText)));

		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));
		//criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsBySearchKeyword(String searchText,String category, String location, int offset, int limit) {
		logger.error("getDealsByLikeSearchKeyword ==== > "+searchText);


		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location))
					.add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%")));
		}

		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.eq("title", searchText))
					.add(Restrictions.eq("dealUrl", searchText))
					.add(Restrictions.eq("dealId", searchText)));

		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));
		//criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		criteria.setFirstResult(offset)
		.setMaxResults(limit);
		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Userdealssuggestion> getUserDealSuggestions(String userEmail, int offset,
			int limit) {
		logger.error("getUserDealSuggestions ==== > "+userEmail +" : "+offset +" : "+limit);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Userdealssuggestion.class,"userdealssuggestion");
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.createAlias("userdealssuggestion.user", "user");
		criteria.add(Restrictions.eq("user.emailId", userEmail));
		criteria.createAlias("userdealssuggestion.deals", "deals");
		criteria.add(Restrictions.conjunction().add(Restrictions.ge("deals.endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("deals.endAt", Utils.getNearestThursday())));
		criteria.add(Restrictions.disjunction().add(Restrictions.isNull("userdealssuggestion.suggestedTime"))
				.add(Restrictions.ge("userdealssuggestion.suggestedTime", Utils.getCurrentDate())));
		criteria.setFirstResult(offset*10)
		.setMaxResults(limit);

		List<Userdealssuggestion> dealsList = criteria.list();
		return dealsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getTopDealsByRating(String idsStr) {
		logger.debug("getTopDealsByRating: "+idsStr);
		List<Object[]> rows = null;
		String hqlQuery = "select distinct a.id,b.rating from Deals a, Business b where a.id in ("+idsStr+") and a.business.id = b.id and b.rating is not null order by b.rating desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);

			rows = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.debug("Error: "+e.getMessage());
			e.printStackTrace();
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDealLikeCountByPage(String idsStr) {
		logger.debug("getDealLikeCountByPage => ");
		List<Object[]> rows = null;
		String hqlQuery = "select distinct b.id, count(b.id) as num from Page a, Deals b, Pageuserlikes c where a.pageId = c.page.pageId and b.business.id = a.business.id and b.id in ("+idsStr+") group by  a.about, b.id, b.title order by num desc";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			rows = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Deals getDealById(int dealId) {
		List<Deals> dealsList = null;
		String hqlQuery = "from Deals a where a.id = :dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealId", dealId);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList != null && !dealsList.isEmpty() ? dealsList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int isDealExists(String dealId) {
		logger.error("isDealExists ==== > ");
		List<Integer> dealList = null;
		/*Connection conn = null;
		   Statement stmt = null;
		   ResultSet rs = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jeeyoh?zeroDateTimeBehavior=convertToNull", "root", "");

		      //STEP 4: Execute a query
		      System.out.println("Creating database...");
		     // stmt = conn.createStatement();
		      long startTime = System.currentTimeMillis();
				logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + startTime);
		      String sql = "select id from deals where dealId = ?";
		      PreparedStatement st = conn.prepareStatement(sql);
		      st.setString(1, dealId);
		      rs = st.executeQuery();
		      rs.next();
		      long endTime = System.currentTimeMillis();
		      logger.debug("DealId:::  "+rs.getInt(1));
			logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + endTime + " total time ==> " + (endTime-startTime));

		   }catch(SQLException se){
			   logger.debug("Error1:::  "+se.getMessage());
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
			   logger.debug("Error2:::  "+e.getMessage());
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
*/		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "select id from Deals a where a.dealId = :dealId";
		try{
			long startTime = System.currentTimeMillis();
			logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + startTime);
			Query query = session.createQuery(
					hqlQuery);
			query.setParameter("dealId", dealId);
			dealList = (List<Integer>)query.list();
			long endTime = System.currentTimeMillis();
			logger.debug("DealsDAO ==> isDealExists ==> startTime ==> " + endTime + " total time ==> " + (endTime-startTime));

		}
		catch (HibernateException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			return rs != null && !rs.next() ? rs.getInt(1) : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}*/
		return dealList != null && !dealList.isEmpty() ? dealList.get(0) : 0;
	}

	@Override
	public int getTotalDealsBySearchKeyWord(String searchText, String category,
			String location) {
		logger.error("getTotalDealsBySearchKeyWord ==== > "+searchText);


		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.setProjection(Projections.count("id"));
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");

		if(category != null && category.isEmpty() == false)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", category));
		}
		if(location != null && location.isEmpty()== false)
		{
			criteria.add(Restrictions.disjunction().add(Restrictions.like("business.city", "%" + location + "%"))
					.add(Restrictions.like("business.state", "%" + location + "%"))
					.add(Restrictions.like("business.stateCode", "%" + location + "%"))
					.add(Restrictions.like("business.displayAddress", "%" + location + "%"))
					.add(Restrictions.like("business.businessId", "%" + location + "%"))
					.add(Restrictions.eq("business.postalCode", location)));
		}

		if(searchText != null && searchText.isEmpty() == false)
		{
			logger.debug("IN KEYWORD CHECKING ::: ");
			criteria.add(Restrictions.disjunction().add(Restrictions.like("title", "%" + searchText + "%"))
					.add(Restrictions.like("dealUrl", "%" + searchText + "%"))
					.add(Restrictions.like("dealId", "%" + searchText + "%")));

		}

		criteria.add(Restrictions.conjunction().add(Restrictions.ge("endAt", Utils.getCurrentDate()))
				.add(Restrictions.gt("endAt", Utils.getNearestThursday())));
		//criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		//int rowCount = criteria.list().size();
		int rowCount = Integer.parseInt(criteria.uniqueResult().toString());
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByUserCategory(String itemCategory,
			String itemType, String providerName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Deals.class);
		criteria.createAlias("business", "business");
		criteria.createAlias("business.businesstype", "businesstype");
		//criteria.createAlias("tags", "tags");
		if(providerName != null)
		{
			criteria.add(Restrictions.eq("business.name", providerName));
		}
		if(itemCategory != null)
		{
			criteria.add(Restrictions.eq("businesstype.businessType", itemCategory));
		}

		if(itemType != null)
		{
			criteria.add(Restrictions.like("title", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealUrl", "%" + itemType + "%"));
			criteria.add(Restrictions.like("dealId", "%" + itemType + "%"));
			//criteria.add(Restrictions.like("tags.name", "%" + keyword + "%"));

		}

		criteria.add(Restrictions.ge("endAt", Utils.getCurrentDate()));

		List<Deals> dealsList = criteria.list();
		return dealsList;
	}

	@Override
	public boolean  saveDealUserLikes(Dealsusage dealsusage) {
		try{
			sessionFactory.getCurrentSession().save(dealsusage);        
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean updateDealUserLikes(Dealsusage dealsusage) {
		try{
			sessionFactory.getCurrentSession().update(dealsusage);        
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.toString());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Dealoption getDealOptionByDealId(int dealId) {
		List<Dealoption> dealsList = null;
		String hqlQuery = "from Dealoption a where a.deals.id = :dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealId", dealId);
			dealsList = (List<Dealoption>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dealsList != null && !dealsList.isEmpty() ? dealsList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dealredemptionlocation getRedemptionLocationByDealOption(
			int dealOptionId) {
		List<Dealredemptionlocation> locationList = null;
		String hqlQuery = "from Dealredemptionlocation a where a.dealoption.id = :dealOptionId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("dealOptionId", dealOptionId);
			locationList = (List<Dealredemptionlocation>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return locationList != null && !locationList.isEmpty() ? locationList.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getBookedDealList(int userId, String category) {
		logger.debug("getBookedDealList => ");
		List<Deals> dealList = null;
		logger.debug("get next weekend =>"+Utils.getNearestWeekend(null));
		//String hqlQuery = "select a from Deals a where a.id in (select b.deals.id from Dealsusage b where b.user.userId=:userId and b.isBooked=1) and endAt>=:currentDate and endAt<=:nearestFriday";
		String hqlQuery = "select b from Dealsusage a, Deals b, Business c, Businesstype d where a.user.userId = :userId and a.isBooked is true and a.deals.id = b.id and (b.endAt >= :currentDate and  b.endAt <= :nearestFriday) and d.businessType = :category and b.business.businessId = c.id and c.businesstype.businessTypeId = d.businessTypeId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("nearestFriday", Utils.getNearestFriday());
			query.setParameter("category", category);
			dealList = (List<Deals>) query.list();
			logger.debug("getBookedDealList dealList => "+dealList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.toString());
			logger.debug(e.getLocalizedMessage());
		}
		return dealList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Deals> getDealsByuserLikesForCurrentWeekend(String itemCategory,
			String itemType,String providerName, double latitude, double longitude) {
		List<Deals> dealsList = null;
		String hqlQuery = "select a from Deals a, Business b, Businesstype c where c.businessType = :category and (a.endAt >= :currentDate and a.endAt <= :weekendDate) and a.business.id = b.id and b.businesstype.businessTypeId = c.businessTypeId and (a.title like '%"+ itemType + "%' or a.dealUrl like '%" + itemType + "%' or a.dealId like '%" + itemType + "%') and (((acos(sin(((:latitude)*pi()/180)) * sin((b.lattitude*pi()/180))+cos(((:latitude)*pi()/180)) * cos((b.lattitude*pi()/180)) * cos((((:longitude)- b.longitude)*pi()/180))))*180/pi())*60*1.1515) <=50 group by a.dealId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("category", itemCategory);
			query.setParameter("currentDate", Utils.getCurrentDate());
			query.setParameter("weekendDate", Utils.getNearestWeekend(null));
			query.setDouble("latitude", latitude);
			query.setDouble("longitude", longitude);
			dealsList = (List<Deals>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return dealsList;
	}

	@Override
	public void updateDeal(Deals deals) {
		try {
			sessionFactory.getCurrentSession().update(deals);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	}

}
