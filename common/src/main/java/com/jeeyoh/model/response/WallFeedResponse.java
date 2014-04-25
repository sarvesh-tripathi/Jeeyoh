package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.WallFeedModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class WallFeedResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<CommentModel> wallFeedComments;
	@JsonProperty
	List<WallFeedModel> wallFeedModel;
	
	/**
	 * @return the wallFeedModel
	 */
	public List<WallFeedModel> getWallFeedModel() {
		return wallFeedModel;
	}
	/**
	 * @param wallFeedModel the wallFeedModel to set
	 */
	public void setWallFeedModel(List<WallFeedModel> wallFeedModel) {
		this.wallFeedModel = wallFeedModel;
	}
	/**
	 * @return the wallFeedComments
	 */
	public List<CommentModel> getWallFeedComments() {
		return wallFeedComments;
	}
	/**
	 * @param wallFeedComments the wallFeedComments to set
	 */
	public void setWallFeedComments(List<CommentModel> wallFeedComments) {
		this.wallFeedComments = wallFeedComments;
	}

}
