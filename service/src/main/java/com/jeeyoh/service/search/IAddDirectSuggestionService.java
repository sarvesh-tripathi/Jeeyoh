package com.jeeyoh.service.search;

import java.util.ArrayList;
import java.util.List;

import com.jeeyoh.model.response.BaseResponse;

public interface IAddDirectSuggestionService 
{
	public BaseResponse addSuggestions(int userId, ArrayList<Integer> friendsIdList, List<Integer> groups, int suggestionId,String category, String suggestionType, String suggestedTime);
}
