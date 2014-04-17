package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class FunboardComments implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private Funboard funboard;
	private String comment;
	private boolean isComment;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the isComment
	 */
	public boolean getIsComment() {
		return isComment;
	}
	/**
	 * @param isComment the isComment to set
	 */
	public void setIsComment(boolean isComment) {
		this.isComment = isComment;
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
