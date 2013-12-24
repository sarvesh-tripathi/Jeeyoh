package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.util.Date;

public class Eventuserlikes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer eventUserLikesId;
    private User user;
    private Events event;
    private Boolean isFavorite;
    private Boolean isFollowing;
    private Boolean isLike;
    private Boolean isVisited;
    private Boolean isProfileHidden;
    private Boolean isProfileDetailsHidden;
    private String displayName;
    private Date createdtime;
    private Date updatedtime;

   public Eventuserlikes() {
   }

   public Eventuserlikes(User user, Events event, Notificationpermission notificationpermission, Date createdtime, Date updatedtime) {
       this.user = user;
       this.event = event;
       this.createdtime = createdtime;
       this.updatedtime = updatedtime;
   }
   public Eventuserlikes(User user, Events event, Notificationpermission notificationpermission, Boolean isFavorite, Boolean isFollowing, Boolean isLike, Boolean isVisited, Boolean isProfileHidden, Boolean isProfileDetailsHidden, String displayName, Date createdtime, Date updatedtime) {
      this.user = user;
      this.event = event;
      this.isFavorite = isFavorite;
      this.isFollowing = isFollowing;
      this.isLike = isLike;
      this.isVisited = isVisited;
      this.isProfileHidden = isProfileHidden;
      this.isProfileDetailsHidden = isProfileDetailsHidden;
      this.displayName = displayName;
      this.createdtime = createdtime;
      this.updatedtime = updatedtime;
   }
  
   public Integer getPageUserLikesId() {
       return this.eventUserLikesId;
   }
   
   public void setPageUserLikesId(Integer eventUserLikesId) {
       this.eventUserLikesId = eventUserLikesId;
   }
   public User getUser() {
       return this.user;
   }
   
   public void setUser(User user) {
       this.user = user;
   }
   public Events getPage() {
       return this.event;
   }
   
   public void setPage(Events event) {
       this.event = event;
   }
   public Boolean getIsFavorite() {
       return this.isFavorite;
   }
   
   public void setIsFavorite(Boolean isFavorite) {
       this.isFavorite = isFavorite;
   }
   public Boolean getIsFollowing() {
       return this.isFollowing;
   }
   
   public void setIsFollowing(Boolean isFollowing) {
       this.isFollowing = isFollowing;
   }
   public Boolean getIsLike() {
       return this.isLike;
   }
   
   public void setIsLike(Boolean isLike) {
       this.isLike = isLike;
   }
   public Boolean getIsVisited() {
       return this.isVisited;
   }
   
   public void setIsVisited(Boolean isVisited) {
       this.isVisited = isVisited;
   }
   public Boolean getIsProfileHidden() {
       return this.isProfileHidden;
   }
   
   public void setIsProfileHidden(Boolean isProfileHidden) {
       this.isProfileHidden = isProfileHidden;
   }
   public Boolean getIsProfileDetailsHidden() {
       return this.isProfileDetailsHidden;
   }
   
   public void setIsProfileDetailsHidden(Boolean isProfileDetailsHidden) {
       this.isProfileDetailsHidden = isProfileDetailsHidden;
   }
   public String getDisplayName() {
       return this.displayName;
   }
   
   public void setDisplayName(String displayName) {
       this.displayName = displayName;
   }
   public Date getCreatedtime() {
       return this.createdtime;
   }
   
   public void setCreatedtime(Date createdtime) {
       this.createdtime = createdtime;
   }
   public Date getUpdatedtime() {
       return this.updatedtime;
   }
   
   public void setUpdatedtime(Date updatedtime) {
       this.updatedtime = updatedtime;
   }


}
