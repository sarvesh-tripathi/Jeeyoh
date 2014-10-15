package com.jeeyoh.persistence;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.jeeyoh.persistence.domain.Funboard;
import com.jeeyoh.persistence.domain.FunboardComments;
import com.jeeyoh.persistence.domain.FunboardMediaContent;
import com.jeeyoh.persistence.domain.Timeline;

public interface IFunBoardDAO {
	
	public void saveFunBoard(Funboard funboard, int batch_size);
	public void deleteFunBoard(int id, int userId);
	public void saveFunBoardComment(FunboardComments funboardComments);
	public void saveFunBoardMediaContent(FunboardMediaContent funboardMediaContent);
	public List<Funboard> getUserFunBoardItems(int userId);
	public int getNotificationCount(int id);
	public List<FunboardComments> getComments(int funBoardId);
	public List<FunboardMediaContent> getmediaContent(int funBoardId);
	public void deleteFunBoardMediaContent(int funBoardId);
	public void deleteFunBoardComments(int funBoardId);
	public Timeline getTimeLine(Time funboardCreationTime);
	public Funboard isFunBoardExists(int userId, int itemId, Date scheduledDate);
	public List<Funboard> getUserFunBoardItemsByCurrentsDate(int userId);
	public Funboard getFunboardById(int funBoardId);
	public int getFunboardComment(Integer funBoardId, Integer userId);
	public Timeline getDefaultTimeLine();
	public void updateFunBoard(Funboard funboard);
	public List<Funboard> getUserFunBoardItemsForCurrentWeekend(int userId, String category, boolean forUser);
	
}
