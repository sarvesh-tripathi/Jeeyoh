package com.jeeyoh.model.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private Integer userId; 
	@JsonProperty
    private String emailId;
	@JsonProperty
    private String password;
	@JsonProperty
    private String firstName;
	@JsonProperty
    private String middleName;
	@JsonProperty
    private String lastName;
	@JsonProperty
    private Integer birthDate;
	@JsonProperty
    private Integer birthMonth;
	@JsonProperty
    private Integer birthYear;
    @JsonProperty
    private Character gender;
    @JsonProperty
    private Boolean isActive;
    @JsonProperty
    private Boolean isDeleted;
    @JsonProperty
    private String addressline1;
    @JsonProperty
    private String street;
    @JsonProperty
    private String city;
    @JsonProperty
    private String state;
    @JsonProperty
    private String country;
    @JsonProperty
    private String zipcode;
    @JsonProperty
    private String createdtime;
    @JsonProperty
    private String updatedtime;

    @JsonProperty
    private String confirmationId;
    
    @JsonProperty
    private String sessionId;
    @JsonProperty
    private String imageUrl;
    @JsonProperty
	private int offset;
	@JsonProperty
	private int limit;
	@JsonProperty
	private Boolean isShareProfileWithFriend;
	@JsonProperty
	private Boolean isShareProfileWithGroup;
	@JsonProperty
	private Boolean isShareCommunity;
    
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Integer birthDate) {
		this.birthDate = birthDate;
	}
	public Integer getBirthMonth() {
		return birthMonth;
	}
	public void setBirthMonth(Integer birthMonth) {
		this.birthMonth = birthMonth;
	}
	public Integer getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}
	public Character getGender() {
		return gender;
	}
	public void setGender(Character gender) {
		this.gender = gender;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getAddressline1() {
		return addressline1;
	}
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}
	public String getUpdatedtime() {
		return updatedtime;
	}
	public void setUpdatedtime(String updatedtime) {
		this.updatedtime = updatedtime;
	}
	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}
	public String getConfirmationId() {
		return confirmationId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
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
	 * @return the isShareProfileWithFriend
	 */
	public Boolean getIsShareProfileWithFriend() {
		return isShareProfileWithFriend;
	}
	/**
	 * @param isShareProfileWithFriend the isShareProfileWithFriend to set
	 */
	public void setIsShareProfileWithFriend(Boolean isShareProfileWithFriend) {
		this.isShareProfileWithFriend = isShareProfileWithFriend;
	}
	/**
	 * @return the isShareProfileWithGroup
	 */
	public Boolean getIsShareProfileWithGroup() {
		return isShareProfileWithGroup;
	}
	/**
	 * @param isShareProfileWithGroup the isShareProfileWithGroup to set
	 */
	public void setIsShareProfileWithGroup(Boolean isShareProfileWithGroup) {
		this.isShareProfileWithGroup = isShareProfileWithGroup;
	}
	/**
	 * @return the isShareCommunity
	 */
	public Boolean getIsShareCommunity() {
		return isShareCommunity;
	}
	/**
	 * @param isShareCommunity the isShareCommunity to set
	 */
	public void setIsShareCommunity(Boolean isShareCommunity) {
		this.isShareCommunity = isShareCommunity;
	}

}
