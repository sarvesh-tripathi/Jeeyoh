package com.jeeyoh.persistence;

import java.util.List;

import com.jeeyoh.persistence.domain.Groupusermap;
import com.jeeyoh.persistence.domain.Jeeyohgroup;

public interface IGroupDAO {
	
	public void saveJeeyohGroup(Jeeyohgroup jeeyohGroup);
	public void saveGroupUserMap(Groupusermap groupUserMap);
	public Jeeyohgroup getGroupByGroupId(Integer groupId);
	public List<Jeeyohgroup> getUserGroups(int userId);
	public List<Jeeyohgroup> getUserContactGroups(int contactId);
	public List<Jeeyohgroup> getGroupsByCreator(int userId);
	public Jeeyohgroup isGroupExists(String groupName);
	public void updateJeeyohGroup(Jeeyohgroup group);
	public Groupusermap isContactInGroup(int userId, int groupId);
	public List<Integer> getGroupMembers(int groupId);

}
