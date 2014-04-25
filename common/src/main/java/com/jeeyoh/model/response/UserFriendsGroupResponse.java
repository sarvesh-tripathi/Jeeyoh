package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.AddGroupModel;
import com.jeeyoh.model.user.UserModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class UserFriendsGroupResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	List<UserModel> friends ;
	@JsonProperty
	List<AddGroupModel> groups;

	public List<UserModel> getFriends() {
		return friends;
	}

	public void setFriends(List<UserModel> friends) {
		this.friends = friends;
	}

	public List<AddGroupModel> getGroups() {
		return groups;
	}

	public void setGroups(List<AddGroupModel> groups) {
		this.groups = groups;
	}
	

}
