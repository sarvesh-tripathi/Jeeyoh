package com.jeeyoh.persistence.dao.stubhub;

import java.util.List;

import com.jeeyoh.persistence.domain.StubhubEvent;

public interface IStubhubDAO {
	
	void save(StubhubEvent stunhub, int count);
	public List<StubhubEvent> getStubhubEvents();

}
