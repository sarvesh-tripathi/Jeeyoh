package com.jeeyoh.persistence;

import com.jeeyoh.persistence.domain.BetaListUser;

public interface IBetaListUserDAO {
	
	public void registerUser(BetaListUser betaListUser);
	public void confirmUser(BetaListUser betaListUser);
	public BetaListUser getUser(String emailId, String confirmationId);
	public BetaListUser isUserExist(String emailId);

}
