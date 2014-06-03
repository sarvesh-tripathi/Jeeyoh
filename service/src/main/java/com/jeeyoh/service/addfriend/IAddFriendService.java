package com.jeeyoh.service.addfriend;

import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.FriendListResponse;

public interface IAddFriendService 
{
	public FriendListResponse searchFriend(String location, String name, int userId);
	public BaseResponse sendFriendRequest(int userId, int contactId);
	public BaseResponse acceptFriendRequest(int userId, int contactId);
	public BaseResponse denyFriendRequest(int userId, int contactId);
	public BaseResponse blockFriendRequest(int userId, int contactId);
	
}
