package com.jeeyoh.model.response;



import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.funboard.FunBoardModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FunBoardResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<FunBoardModel> funBoards;
	
	@JsonProperty
	private List<FunBoardModel> fridayActivefunBoards;
	
	@JsonProperty
	private List<FunBoardModel> saturdayActivefunBoards;
	
	@JsonProperty
	private List<FunBoardModel> sundayActivefunBoards;
	/**
	 * @return the funBoards
	 */
	public List<FunBoardModel> getFunBoards() {
		return funBoards;
	}
	/**
	 * @param funBoards the funBoards to set
	 */
	public void setFunBoards(List<FunBoardModel> funBoards) {
		this.funBoards = funBoards;
	}
	/**
	 * @return the fridayActivefunBoards
	 */
	public List<FunBoardModel> getFridayActivefunBoards() {
		return fridayActivefunBoards;
	}
	/**
	 * @param fridayActivefunBoards the fridayActivefunBoards to set
	 */
	public void setFridayActivefunBoards(List<FunBoardModel> fridayActivefunBoards) {
		this.fridayActivefunBoards = fridayActivefunBoards;
	}
	/**
	 * @return the saturdayActivefunBoards
	 */
	public List<FunBoardModel> getSaturdayActivefunBoards() {
		return saturdayActivefunBoards;
	}
	/**
	 * @param saturdayActivefunBoards the saturdayActivefunBoards to set
	 */
	public void setSaturdayActivefunBoards(
			List<FunBoardModel> saturdayActivefunBoards) {
		this.saturdayActivefunBoards = saturdayActivefunBoards;
	}
	/**
	 * @return the sundayActivefunBoards
	 */
	public List<FunBoardModel> getSundayActivefunBoards() {
		return sundayActivefunBoards;
	}
	/**
	 * @param sundayActivefunBoards the sundayActivefunBoards to set
	 */
	public void setSundayActivefunBoards(List<FunBoardModel> sundayActivefunBoards) {
		this.sundayActivefunBoards = sundayActivefunBoards;
	}
	
	

}
