package com.jeeyoh.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;

@Repository("userDAO")
public class UserDAO implements IUserDAO {

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<User> getUserContacts(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Page> getUserCommunities(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Page> getUserContactsCommunities(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Jeeyohgroup> getUserGroups(int userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Jeeyohgroup> getUserContactGroups(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}

}
