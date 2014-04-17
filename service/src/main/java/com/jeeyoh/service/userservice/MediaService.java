package com.jeeyoh.service.userservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jeeyoh.model.response.UploadMediaServerResponse;
import com.jeeyoh.utils.Utils;

@Component("mediaService")
public class MediaService implements IMediaService{

	private static Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${app.uploaded.media.server.path}")
	private String uploadedMediaServerPATH;
	
	@Value("${app.media.uploaded.folder}")
	private String folderName;
	
	@Value("${host.path}")
	private String hostPath;

	@Override
	public UploadMediaServerResponse uploadOnServer(
			InputStream uploadedInputStream, String fileName, String userId) {
		String randomNumber = Utils.getRandomCode();
		fileName = userId + "_" + randomNumber + "_" + fileName;
		File folder = new File(uploadedMediaServerPATH);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
		String filePathOnServer = uploadedMediaServerPATH + fileName;
		File mediaFileObject = null;
		UploadMediaServerResponse uploadMediaServerResponse = new UploadMediaServerResponse();
		try
		{
			mediaFileObject = new File(filePathOnServer);
			OutputStream out = new FileOutputStream(mediaFileObject);
			long startTime = System.currentTimeMillis();
			logger.debug("uploadMediaOnServer mobile app => fileName => " + fileName + " upload start time: "
					+ startTime);
			int read = 0;
			byte[] bytes = new byte[1024];

			// out = new FileOutputStream(mediaFileObject);
			while ((read = uploadedInputStream.read(bytes)) != -1)
			{
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			long endTime = System.currentTimeMillis();
			logger.debug("uploadMediaOnServer mobile app => fileName => " + fileName + " upload end time: " + endTime);
			logger.debug("uploadMediaOnServer mobile app => fileName => " + fileName + " file size: "
					+ mediaFileObject.length() + " upload total time: " + (endTime - startTime));
		}
		catch (IOException e)
		{
			logger.debug("Error:  "+e.getLocalizedMessage());
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		/*String mediaURL = null;
		if (mediaFileObject != null && fileName != null)
		{
			// Code for replacing special characters from file name
			Pattern pattern = Pattern.compile("[^a-zA-Z0-9._]");
			Matcher match = pattern.matcher(fileName);
			while (match.find())
			{
				String s = match.group();
				fileName = fileName.replaceAll("\\" + s, "");
			}
			logger.debug("uploadOnServer --> file name after removing special characters:: " + fileName);

			mediaURL = uploadMediaOnAS3(fileName, mediaFileObject, Integer.parseInt(userId));
			if (mediaURL != null)
			{
				mediaFileObject.delete();
			}
			uploadMediaServerResponse.setMediaUrl(mediaURL);
			uploadMediaServerResponse.setTimeStamp(randomNumber);
		}*/
		String imageUrl = folderName + "/" +fileName;
		uploadMediaServerResponse.setMediaUrl(imageUrl);
		uploadMediaServerResponse.setTimeStamp(randomNumber);
		return uploadMediaServerResponse;
	}



}
