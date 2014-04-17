package com.jeeyoh.model.funboard;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FunBoardModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private int funBoardId;
	@JsonProperty
	private String type;
	@JsonProperty
	private int itemId;
	@JsonProperty
	private String category;
	@JsonProperty
	private String timeLine;
	@JsonProperty
	private String imageUrl;
	@JsonProperty
	private int notificationCount;
	@JsonProperty
	private Double rating;
	@JsonProperty
	private String description;
	@JsonProperty
	private String title;
	@JsonProperty
	private List<UserModel> attendingUserList;
	@JsonProperty
	private List<CommentModel> comments;
	@JsonProperty
	private List<CommentModel> jAlerts;
	@JsonProperty
	private List<MediaContenModel> images;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
	private String source;
	@JsonProperty
	private String scheduledDate;
	
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
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	/**
	 * @return the timeLine
	 */
	public String getTimeLine() {
		return timeLine;
	}
	/**
	 * @param timeLine the timeLine to set
	 */
	public void setTimeLine(String timeLine) {
		this.timeLine = timeLine;
	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	/**
	 * @return the notificationCount
	 */
	public int getNotificationCount() {
		return notificationCount;
	}
	/**
	 * @param notificationCount the notificationCount to set
	 */
	public void setNotificationCount(int notificationCount) {
		this.notificationCount = notificationCount;
	}
	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}
	/**
	 * @return the attendingUserList
	 */
	public List<UserModel> getAttendingUserList() {
		return attendingUserList;
	}
	/**
	 * @param attendingUserList the attendingUserList to set
	 */
	public void setAttendingUserList(List<UserModel> attendingUserList) {
		this.attendingUserList = attendingUserList;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the comments
	 */
	public List<CommentModel> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}
	/**
	 * @return the jAlerts
	 */
	public List<CommentModel> getjAlerts() {
		return jAlerts;
	}
	/**
	 * @param jAlerts the jAlerts to set
	 */
	public void setjAlerts(List<CommentModel> jAlerts) {
		this.jAlerts = jAlerts;
	}
	/**
	 * @return the images
	 */
	public List<MediaContenModel> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<MediaContenModel> images) {
		this.images = images;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the scheduledDate
	 */
	public String getScheduledDate() {
		return scheduledDate;
	}
	/**
	 * @param scheduledDate the scheduledDate to set
	 */
	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

}
