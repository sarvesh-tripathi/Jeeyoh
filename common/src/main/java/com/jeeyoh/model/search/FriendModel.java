package com.jeeyoh.model.search;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FriendModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private int contactId;
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String location;
	@JsonProperty
	private boolean isDeny;
	@JsonProperty
	private boolean isApproved;
	@JsonProperty
	private boolean isBlock;
	@JsonProperty
	private String lastName;
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the contactId
	 */
	public int getContactId() {
		return contactId;
	}
	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the isDeny
	 */
	public boolean isDeny() {
		return isDeny;
	}
	/**
	 * @param isDeny the isDeny to set
	 */
	public void setDeny(boolean isDeny) {
		this.isDeny = isDeny;
	}
	/**
	 * @return the isApproved
	 */
	public boolean isApproved() {
		return isApproved;
	}
	/**
	 * @param isApproved the isApproved to set
	 */
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	/**
	 * @return the isBlock
	 */
	public boolean isBlock() {
		return isBlock;
	}
	/**
	 * @param isBlock the isBlock to set
	 */
	public void setBlock(boolean isBlock) {
		this.isBlock = isBlock;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

}
