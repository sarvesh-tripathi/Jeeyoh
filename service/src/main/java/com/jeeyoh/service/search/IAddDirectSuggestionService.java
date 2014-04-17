package com.jeeyoh.service.search;

import java.util.ArrayList;

import com.jeeyoh.model.response.BaseResponse;

public interface IAddDirectSuggestionService 
{
	public BaseResponse addSuggestions(int userId, ArrayList<Integer> friendsIdList,int suggetstionId,String category, String suggestionType);
}
