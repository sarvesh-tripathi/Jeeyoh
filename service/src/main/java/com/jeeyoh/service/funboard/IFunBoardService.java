package com.jeeyoh.service.funboard;

import com.jeeyoh.model.funboard.CommentModel;
import com.jeeyoh.model.funboard.FunBoardModel;
import com.jeeyoh.model.funboard.FunBoardRequest;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.BaseResponse;
import com.jeeyoh.model.response.CommentResponse;
import com.jeeyoh.model.response.FunBoardDetailResponse;
import com.jeeyoh.model.response.FunBoardResponse;
import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.model.user.UserModel;

public interface IFunBoardService {
	
	public BaseResponse saveFunBoardItem(FunBoardRequest request);
	public FunBoardResponse getUserFunBoardItems(UserModel user);
	public BaseResponse deleteFunBoarditem(FunBoardRequest request);
	public CommentResponse addFunBoardComment(CommentModel commentModel);
	public FunBoardDetailResponse getFunBoardItem(FunBoardRequest request);
	public UploadMediaServerResponse uploadMediaContent(MediaContenModel mediaContenModel);
	//public BaseResponse updateTimeLine(FunBoardModel funBoardModel);
	//public BaseResponse updateTag(FunBoardModel funBoardModel);
	public BaseResponse updateFunBoard(FunBoardModel funBoardModel);

}
