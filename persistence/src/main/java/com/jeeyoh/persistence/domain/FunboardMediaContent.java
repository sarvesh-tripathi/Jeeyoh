package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class FunboardMediaContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private Funboard funboard;
	private String mediaType;
	private String mediaPathUrl;
	private Date createdTime;
	private Date updatedTime;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the funboard
	 */
	public Funboard getFunboard() {
		return funboard;
	}
	/**
	 * @param funboard the funboard to set
	 */
	public void setFunboard(Funboard funboard) {
		this.funboard = funboard;
	}
	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}
	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	/**
	 * @return the mediaPathUrl
	 */
	public String getMediaPathUrl() {
		return mediaPathUrl;
	}
	/**
	 * @param mediaPathUrl the mediaPathUrl to set
	 */
	public void setMediaPathUrl(String mediaPathUrl) {
		this.mediaPathUrl = mediaPathUrl;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @return the updatedTime
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
