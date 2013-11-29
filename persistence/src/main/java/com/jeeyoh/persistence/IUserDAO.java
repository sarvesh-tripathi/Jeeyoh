package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Jeeyohgroup;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.User;

public interface IUserDAO {
	public List<User> getUsers();
	
	public List<User> getUserContacts(int userId);
	
	public List<Page> getUserCommunities(int userId);
	
	public List<Page> getUserContactsCommunities(int contactId);
	
	public List<Jeeyohgroup> getUserGroups(int userId);
	
	public List<Jeeyohgroup> getUserContactGroups(int contactId);
}
