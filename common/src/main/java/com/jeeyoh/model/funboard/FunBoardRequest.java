package com.jeeyoh.model.funboard;

import java.io.Serializable;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FunBoardRequest implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private int userId;
	@JsonProperty
	private String emailId;
	@JsonProperty
	private FunBoardModel funBoard;
	@JsonProperty
	private int funBoardId;
	/*@JsonProperty
	private ArrayList<FunBoardModel> funBoardList;*/
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
	 * @param EmailId the EmailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	/**
	 * @return the EmailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param funBoard the funBoard to set
	 */
	public void setFunBoard(FunBoardModel funBoard) {
		this.funBoard = funBoard;
	}
	/**
	 * @return the funBoard
	 */
	public FunBoardModel getFunBoard() {
		return funBoard;
	}
	/**
	 * @return the funBoardId
	 */
	public int getFunBoardId() {
		return funBoardId;
	}
	/**
	 * @param funBoardId the funBoardId to set
	 */
	public void setFunBoardId(int funBoardId) {
		this.funBoardId = funBoardId;
	}
	
}
