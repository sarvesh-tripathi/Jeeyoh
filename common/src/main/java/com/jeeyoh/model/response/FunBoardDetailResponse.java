package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.funboard.FunBoardModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FunBoardDetailResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FunBoardModel funBoard;
	/**
	 * @return the funBoard
	 */
	public FunBoardModel getFunBoard() {
		return funBoard;
	}
	/**
	 * @param funBoard the funBoard to set
	 */
	public void setFunBoard(FunBoardModel funBoard) {
		this.funBoard = funBoard;
	}
	

}
