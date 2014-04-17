package com.jeeyoh.persistence.domain;
// Generated Oct 10, 2013 9:08:11 AM by Hibernate Tools 3.2.1.GA



/**
 * Review generated by hbm2java
 */
public class Review  implements java.io.Serializable {


     private Integer id;
     private Business business;
     private String reviewId;
     private Long rating;
     private String excerpt;
     private String creationTime;
     private String ratingImageUrl;
     private String ratingSmallImageUrl;
     private String ratingLargeImageUrl;
     private String userId;
     private String userName;
     private String userImageUrl;

    public Review() {
    }

	
    public Review(Business business) {
        this.business = business;
    }
    public Review(Business business, String reviewId, Long rating, String excerpt, String creationTime, String ratingImageUrl, String ratingSmallImageUrl, String ratingLargeImageUrl, String userId, String userName, String userImageUrl) {
       this.business = business;
       this.reviewId = reviewId;
       this.rating = rating;
       this.excerpt = excerpt;
       this.creationTime = creationTime;
       this.ratingImageUrl = ratingImageUrl;
       this.ratingSmallImageUrl = ratingSmallImageUrl;
       this.ratingLargeImageUrl = ratingLargeImageUrl;
       this.userId = userId;
       this.userName = userName;
       this.userImageUrl = userImageUrl;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Business getBusiness() {
        return this.business;
    }
    
    public void setBusiness(Business business) {
        this.business = business;
    }
    public String getReviewId() {
        return this.reviewId;
    }
    
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
    public Long getRating() {
        return this.rating;
    }
    
    public void setRating(Long rating) {
        this.rating = rating;
    }
    public String getExcerpt() {
        return this.excerpt;
    }
    
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
    public String getCreationTime() {
        return this.creationTime;
    }
    
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    public String getRatingImageUrl() {
        return this.ratingImageUrl;
    }
    
    public void setRatingImageUrl(String ratingImageUrl) {
        this.ratingImageUrl = ratingImageUrl;
    }
    public String getRatingSmallImageUrl() {
        return this.ratingSmallImageUrl;
    }
    
    public void setRatingSmallImageUrl(String ratingSmallImageUrl) {
        this.ratingSmallImageUrl = ratingSmallImageUrl;
    }
    public String getRatingLargeImageUrl() {
        return this.ratingLargeImageUrl;
    }
    
    public void setRatingLargeImageUrl(String ratingLargeImageUrl) {
        this.ratingLargeImageUrl = ratingLargeImageUrl;
    }
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserImageUrl() {
        return this.userImageUrl;
    }
    
    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }




}


