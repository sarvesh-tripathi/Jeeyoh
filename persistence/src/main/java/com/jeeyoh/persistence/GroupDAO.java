package com.jeeyoh.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;

@Repository("groupDAO")
public class GroupDAO implements IGroupDAO{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void saveJeeyohGroup(Jeeyohgroup jeeyohGroup) {
		logger.debug("saveJeeyohGroup => ");
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(jeeyohGroup);
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}

	}

	@Override
	public void saveGroupUserMap(Groupusermap groupUserMap) {
		logger.debug("saveGroupUserMap => ");
		Session session =  sessionFactory.getCurrentSession();
		try
		{
			session.save(groupUserMap);
		}
		catch(HibernateException e)
		{
			e.printStackTrace();
			logger.error("ERROR IN DAO :: = > "+e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Jeeyohgroup getGroupByGroupId(Integer groupId) {
		List<Jeeyohgroup> Jeeyohgroups = null;
		String hqlQuery = "from Jeeyohgroup a where a.groupId =:groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("groupId", groupId);
			Jeeyohgroups = (List<Jeeyohgroup>) query.list();
			logger.debug("userList => " + Jeeyohgroups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jeeyohgroups != null && !Jeeyohgroups.isEmpty() ? Jeeyohgroups.get(0) : null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Jeeyohgroup> getUserGroups(int userId) {
		List<Jeeyohgroup> groupList = null;
		String hqlQuery = "select b from User a, Jeeyohgroup b, Groupusermap c where a.userId = :userId and c.user.userId = a.userId and b.groupId = c.jeeyohgroup.groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			groupList = (List<Jeeyohgroup>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupList;
	}
	

	@Override
	public List<Jeeyohgroup> getUserContactGroups(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Jeeyohgroup> getGroupsByCreator(int userId) {
		List<Jeeyohgroup> Jeeyohgroups = null;
		String hqlQuery = "from Jeeyohgroup a where a.userByCreatorId.userId = :userId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("userId", userId);
			Jeeyohgroups = (List<Jeeyohgroup>) query.list();
			logger.debug("groupList => " + Jeeyohgroups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jeeyohgroups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Jeeyohgroup isGroupExists(String groupName) {
		List<Jeeyohgroup> Jeeyohgroups = null;
		String hqlQuery = "from Jeeyohgroup a where a.groupName = :groupName";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("groupName", groupName);
			Jeeyohgroups = (List<Jeeyohgroup>) query.list();
			logger.debug("groupList => " + Jeeyohgroups);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jeeyohgroups != null && !Jeeyohgroups.isEmpty() ? Jeeyohgroups.get(0) : null;
	}

	@Override
	public void updateJeeyohGroup(Jeeyohgroup group) {
		try
		{
			sessionFactory.getCurrentSession().saveOrUpdate(group);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Groupusermap isContactInGroup(int userId, int groupId) {
		List<Groupusermap> groupusermap = null;
		String hqlQuery = "from Groupusermap a where a.user.userId = :userId and a.jeeyohgroup.groupId = :groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("userId", userId);
			query.setParameter("groupId", groupId);
			groupusermap = (List<Groupusermap>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupusermap != null && !groupusermap.isEmpty() ? groupusermap.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getGroupMembers(int groupId) {
		List<Integer> members = null;
		String hqlQuery = "select a.user.userId from Groupusermap a where a.jeeyohgroup.groupId = :groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);	
			query.setParameter("groupId", groupId);
			members = (List<Integer>) query.list();
			logger.debug("groupList => " + members);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	@Override
	public void deleteGroupMember(Groupusermap groupUserMap) {
		try
		{
			sessionFactory.getCurrentSession().delete(groupUserMap);
		}
		catch(HibernateException e)
		{
			logger.error(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Jeeyohgroup> getUserGroupsByCategory(int userId, String category) {
		List<Jeeyohgroup> groupList = null;
		String hqlQuery = "select b from User a, Jeeyohgroup b, Groupusermap c where a.userId = :userId and b.groupTyep = :category  and c.user.userId = a.userId and b.groupId = c.jeeyohgroup.groupId";
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("category", category);
			groupList = (List<Jeeyohgroup>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupList;
	}
}
