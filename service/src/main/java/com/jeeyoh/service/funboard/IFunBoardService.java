package com.jeeyoh.service.funboard;

import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.user.UserModel;

public interface IFunBoardService {
	
	public BaseResponse saveFunBoardItem(FunBoardRequest request);
	public FunBoardResponse getUserFunBoardItems(UserModel user);

}
