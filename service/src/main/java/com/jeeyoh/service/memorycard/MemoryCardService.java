package com.jeeyoh.service.memorycard;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.response.MemoryCardResponse;
import com.jeeyoh.model.search.MemoryCardModel;

@Component("memoryCard")
public class MemoryCardService implements IMemoryCardService{

	@Transactional
	@Override
	public MemoryCardResponse getMemoryCardDetails(MemoryCardModel memoryCardModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
