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
	private String userEmail;
	@JsonProperty
	private ArrayList<FunBoardModel> funBoardList;
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
	/**
	 * @param funBoardList the funBoardList to set
	 */
	public void setFunBoardList(ArrayList<FunBoardModel> funBoardList) {
		this.funBoardList = funBoardList;
	}
	/**
	 * @return the funBoardList
	 */
	public ArrayList<FunBoardModel> getFunBoardList() {
		return funBoardList;
	}

}
