package com.jeeyoh.service.search;

import java.util.ArrayList;

import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.ExcelUpload;

public interface IManualUpload {
	
	public ArrayList<ExcelUpload> parseData(String filename);
	public void uploadExcel(String filename);
}
