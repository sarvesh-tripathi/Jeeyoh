package com.jeeyoh.persistence.domain;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

public class Timeline implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer timeLineId;
	private String timeLineType;
	private Time startTime;
	private Time endTime;
	private Set funBoard = new HashSet(0);
	/**
	 * @return the timeLineId
	 */
	public Integer getTimeLineId() {
		return timeLineId;
	}
	/**
	 * @param timeLineId the timeLineId to set
	 */
	public void setTimeLineId(Integer timeLineId) {
		this.timeLineId = timeLineId;
	}
	/**
	 * @return the timeLineType
	 */
	public String getTimeLineType() {
		return timeLineType;
	}
	/**
	 * @param timeLineType the timeLineType to set
	 */
	public void setTimeLineType(String timeLineType) {
		this.timeLineType = timeLineType;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the funBoard
	 */
	public Set getFunBoard() {
		return funBoard;
	}
	/**
	 * @param funBoard the funBoard to set
	 */
	public void setFunBoard(Set funBoard) {
		this.funBoard = funBoard;
	}
	
	public String toString() {
		return "timeLineType : " + timeLineType;
	}

}
