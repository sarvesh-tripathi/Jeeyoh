package com.jeeyoh.service.addgroup;

import com.jeeyoh.model.response.AddGroupButtonResponse;
import com.jeeyoh.model.response.AddGroupResponse;
import com.jeeyoh.model.search.AddGroupModel;

public interface IAddGroupService {
	public AddGroupResponse addGroup(AddGroupModel addGroupModel);

	public AddGroupButtonResponse addGroupPage(int userId);
}
