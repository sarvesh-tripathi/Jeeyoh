package com.jeeyoh.service.addgroup;

import com.jeeyoh.model.response.AddGroupButtonResponse;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.GroupDetailResponse;
import com.jeeyoh.model.response.GroupListResponse;
import com.jeeyoh.model.search.AddGroupModel;

public interface IAddGroupService {
	
	public BaseResponse addGroup(AddGroupModel addGroupModel);
	public AddGroupButtonResponse addGroupPage(int userId);
	public GroupDetailResponse getGroupDetails(int groupId);
	public GroupListResponse getGroupList(int userId);
	public BaseResponse editGroup(AddGroupModel addGroupModel);
}
