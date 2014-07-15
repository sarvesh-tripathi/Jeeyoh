package com.jeeyoh.service.memorycard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.model.enums.ServiceAPIStatus;
import com.jeeyoh.model.funboard.MediaContenModel;
import com.jeeyoh.model.response.MemoryCardResponse;
import com.jeeyoh.model.search.MemoryCardModel;
import com.jeeyoh.persistence.IWallFeedDAO;
import com.jeeyoh.utils.Utils;

@Component("memoryCard")
public class MemoryCardService implements IMemoryCardService{

	static final Logger logger = LoggerFactory.getLogger("debugLogger");

	@Value("${host.path}")
	private String hostPath;
	
	@Autowired
	IWallFeedDAO wallFeedDAO;

	@Transactional
	@Override
	public MemoryCardResponse getMemoryCardDetails(MemoryCardModel memoryCardModel) 
	{
		MemoryCardResponse response = new MemoryCardResponse();
		try
		{
			SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date startDate = simple.parse(memoryCardModel.getStartDate());
			Date endDate = simple.parse(memoryCardModel.getEndDate());
			Date end = null;
			List<String> list = null;
			
			MemoryCardModel memoryCardModels = new MemoryCardModel();
			Set<MediaContenModel> mediaContenModels = new HashSet<MediaContenModel>();
			List<Date> weekendList =  Utils.findWeekendsBetweenTwoDates(startDate, endDate);
			//logger.debug("Weekend"+weekendList.size());
			if(weekendList.isEmpty() || weekendList.size() == 1)
			{
				list = wallFeedDAO.getMemoryCard(memoryCardModel.getUserId(), memoryCardModel.getSearchText(), startDate, endDate, memoryCardModel.getCategory());
				if(list != null && !list.isEmpty())
				{
					setImagesForMemoryCard(list,mediaContenModels);
				}
			}
			else
			{
				for(int i = 0; i < weekendList.size(); i++)
				{
					//logger.debug("Weekend"+weekendList.get(i));
					if(i == weekendList.size() -1)
					{
						end = endDate;
					}
					else
					{
						Calendar cal = Calendar.getInstance();
						cal.setTime(weekendList.get(i));
						Utils.setTimeFields(cal);
						end = cal.getTime();
					}
					
					list = wallFeedDAO.getMemoryCard(memoryCardModel.getUserId(), memoryCardModel.getSearchText(), startDate, end, memoryCardModel.getCategory());
					if(list != null && !list.isEmpty())
					{	
						setImagesForMemoryCard(list,mediaContenModels);
					}
					Calendar start = Calendar.getInstance();
					start.setTime(weekendList.get(i));
					start.add(Calendar.DATE, 1);
					Utils.setTimeFieldsToStart(start);
					startDate = start.getTime();
				}
			}
			List<MediaContenModel> mediaContentList = new ArrayList<MediaContenModel>();
			mediaContentList.addAll(mediaContenModels);
			memoryCardModels.setImages(mediaContentList);
			response.setMemoryCardItems(memoryCardModels);
			response.setStatus(ServiceAPIStatus.OK.getStatus());
			return response;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			response.setStatus(ServiceAPIStatus.FAILED.getStatus());
			response.setError("Error");
			return response;
		}
	}
	
	
	/**
	 * Set image of wall feed for memory card in the response
	 * @param imageList
	 * @param memoryCardModels
	 */
	private void setImagesForMemoryCard(List<String> imageList, Set<MediaContenModel> mediaContenModels)
	{
		
		//List<MediaContenModel> mediaContent = new ArrayList<MediaContenModel>();
		MediaContenModel mediaContenModel = null;
		
		for(String imageUrl : imageList)
		{
			logger.debug("ImageUrl: "+ imageUrl);
			mediaContenModel = new MediaContenModel();
			mediaContenModel.setImageUrl(hostPath + imageUrl);
			mediaContenModels.add(mediaContenModel);
			//mediaContent.add(mediaContenModel);
		}
		//memoryCardModels.setImages(mediaContent);
		//memoryCardModels.add(memoryCardModel);
	}
}
