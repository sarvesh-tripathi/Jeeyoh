package com.jeeyoh.service.userservice;

import java.io.InputStream;

import com.jeeyoh.model.response.UploadMediaServerResponse;

public interface IMediaService {
	
	public UploadMediaServerResponse uploadOnServer(InputStream uploadedInputStream, String fileName, String userId);

}
