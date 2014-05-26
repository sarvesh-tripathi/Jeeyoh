package com.jeeyoh.model.search;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class JeeyohGroupModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private int groupId;
	@JsonProperty
	private int userId;
	@JsonProperty
	private String privacy;
	@JsonProperty
	private int userByCreatorId;
	@JsonProperty
	private String groupName;
	@JsonProperty
	private String about;
	@JsonProperty
	private String groupType;
	@JsonProperty
	private Date createdtime;
	@JsonProperty
	private Date updatedtime;
	@JsonProperty
	private List<UserModel> groupMembers;
	@JsonProperty
	private int totalMembers;
	
	/**
	 * @return the groupId
	 */
	public int getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public int getUserByCreatorId() {
		return userByCreatorId;
	}
	public void setUserByCreatorId(int userByCreatorId) {
		this.userByCreatorId = userByCreatorId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public Date getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}
	public Date getUpdatedtime() {
		return updatedtime;
	}
	public void setUpdatedtime(Date updatedtime) {
		this.updatedtime = updatedtime;
	}
	/**
	 * @return the groupMembers
	 */
	public List<UserModel> getGroupMembers() {
		return groupMembers;
	}
	/**
	 * @return the totalMembers
	 */
	public int getTotalMembers() {
		return totalMembers;
	}
	/**
	 * @param groupMembers the groupMembers to set
	 */
	public void setGroupMembers(List<UserModel> groupMembers) {
		this.groupMembers = groupMembers;
	}
	/**
	 * @param totalMembers the totalMembers to set
	 */
	public void setTotalMembers(int totalMembers) {
		this.totalMembers = totalMembers;
	}    

}
