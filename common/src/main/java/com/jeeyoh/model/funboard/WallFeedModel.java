package com.jeeyoh.model.funboard;

import java.util.List;

import com.jeeyoh.model.search.SuggestionModel;

public class WallFeedModel {
	int wallFeedId;
	List<SuggestionModel> items;
	List<CommentModel> comments;
	
	public List<CommentModel> getComments() {
		return comments;
	}
	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}
	public int getWallFeedId() {
		return wallFeedId;
	}
	public void setWallFeedId(int wallFeedId) {
		this.wallFeedId = wallFeedId;
	}
	public List<SuggestionModel> getItems() {
		return items;
	}
	public void setItems(List<SuggestionModel> items) {
		this.items = items;
	}
	
	

}
