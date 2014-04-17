package com.jeeyoh.service.memorycard;

import com.jeeyoh.model.response.MemoryCardResponse;
import com.jeeyoh.model.search.MemoryCardModel;

public interface IMemoryCardService {
	
	public MemoryCardResponse getMemoryCardDetails(MemoryCardModel memoryCardModel);

}
