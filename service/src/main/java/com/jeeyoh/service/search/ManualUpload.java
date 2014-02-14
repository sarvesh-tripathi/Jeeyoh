package com.jeeyoh.service.search;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jeeyoh.persistence.IBusinessDAO;
import com.jeeyoh.persistence.IDealsDAO;
import com.jeeyoh.persistence.IEventsDAO;
import com.jeeyoh.persistence.IUserDAO;
import com.jeeyoh.persistence.domain.Business;
import com.jeeyoh.persistence.domain.Businesstype;
import com.jeeyoh.persistence.domain.Deals;
import com.jeeyoh.persistence.domain.Events;
import com.jeeyoh.persistence.domain.ExcelUpload;
import com.jeeyoh.persistence.domain.Page;
import com.jeeyoh.persistence.domain.Pagetype;
import com.jeeyoh.persistence.domain.User;

@Component("manualUpload")
public class ManualUpload implements IManualUpload {
	static final Logger logger = LoggerFactory.getLogger("debugLogger");
	@Autowired
	private IEventsDAO eventsDAO;
	@Autowired
	private IDealsDAO dealsDAO;
	@Autowired
	private IBusinessDAO businessDAO;
	@Autowired
	private IUserDAO userDAO;

	@Override
	public ArrayList<ExcelUpload> parseData(String filename) {
		ExcelUpload excelUpload;
		ArrayList<ExcelUpload> excelUploadList = new ArrayList<ExcelUpload>();
		System.out.println("File name:" + filename);
		try {
			if (filename != null && !filename.equals("")) {
				Workbook wb1 = new XSSFWorkbook(filename);
				Sheet sheet = wb1.getSheetAt(0);
				Row row;
				Cell cell;
				int rows; // No of rows
				rows = sheet.getPhysicalNumberOfRows();
				System.out.println("the total number of rows are" + rows);
				int cols = 0; // No of columns
				int tmp = 0;
				for (int i = 0; i < rows; i++) {
					row = sheet.getRow(i);
					if (row != null) {
						tmp = sheet.getRow(i).getPhysicalNumberOfCells();
						if (tmp > cols)
							cols = tmp;
					}
				}

				for (int r1 = 1; r1 < rows; r1++) {
					row = sheet.getRow(r1);
					if (row != null) {
						excelUpload = new ExcelUpload();
						System.out.println("inside if condition");
						for (int c = 0; c < cols; c++) {
							cell = row.getCell(c);
							System.out.println("inside for loop");
							if (cell != null) {
								switch (c) {
								case 0:
									excelUpload.setEventType(cell.toString());
									break;
								case 1:
									excelUpload.setDescription(cell.toString());
									break;
								case 2:
									excelUpload.setPlace(cell.toString());
									break;
								case 3: 
									cell.setCellType(Cell.CELL_TYPE_STRING); 
									excelUpload.setZipCode(cell.toString());
									break;
								case 4:
									excelUpload.setOriginalPrice(cell.toString());
									break;
								case 5:
									excelUpload.setDiscountedPrice(cell.toString());
									break;
								case 6:
									excelUpload.setPictureUrl(cell.toString());
									break;
								case 7: 
									excelUpload.setValidityStartDate(cell.toString());
									break;
								case 8:
									excelUpload.setValidityEndDate(cell.toString());
									break;
								case 9:
									excelUpload.setValidityTiming(cell.toString());
									break;
								case 10:
									excelUpload.setIfDeal(cell.toString());
									break;
								case 11:
									excelUpload.setHighlight(cell.toString());
									break;
								case 12: 
									excelUpload.setFinePrints(cell.toString());
									break;
								case 13:
									excelUpload.setOfferDetails(cell.toString());
									break;
								case 14:
									excelUpload.setVendorDealProvider(cell.toString());
									break;
								case 15:
									excelUpload.setAddressVendor(cell.toString());
									break;
								case 16:
									excelUpload.setContactVendor(cell.toString());
									break;
								case 17:
									excelUpload.setPaymentOption(cell.toString());
									break;
								case 18:
									excelUpload.setOffers(cell.toString());
									break;
								case 19:
									excelUpload.setUploadByUsername(cell.toString());
									break;
								case 20:
									excelUpload.setTimeStamp(cell.toString());
									break;
								case 21:
									excelUpload.setChangeUserUpdate(cell.toString());
									break;
								}

							}
							
						}
						excelUploadList.add(excelUpload);
					}
				}
			}
			return excelUploadList;
		} catch (Exception e) {
			System.out.print(e.getStackTrace());
			return null;
		}
	}

	@Override
	@Transactional
	public void uploadExcel(String filename)
	{
		ArrayList<ExcelUpload> exUpload = parseData(filename);
		Events event;
		Businesstype businessType;
		Business business;
		Page page;
		Pagetype pageType;
		Deals deals;
		User user;
		for(int i=0;i<exUpload.size();i++)
		{
			event = new Events();
			businessType = new Businesstype();
			business = new Business();
			page = new Page();
			deals = new Deals();
			pageType =  new Pagetype();
			user = new User();
			String place = exUpload.get(i).getPlace();
			String[] placeArray = null;
			String city = null;
			if(place.contains(","))
			{
				placeArray = place.split(",");
				city = placeArray[1];
				business.setCity(city.replaceAll("\\s", ""));
				event.setCity(city.replaceAll("\\s", ""));
			}
			else
			{
				business.setCity(place);
				event.setCity(place);
			}
			businessType.setBusinessType("Sport");
			businessType.setBusinessTypeId(8);
			business.setBusinesstype(businessType);
			business.setBusinessId(exUpload.get(i).getDescription());
			business.setName(exUpload.get(i).getDescription());
			business.setRatingImgUrl(exUpload.get(i).getPictureUrl());
			business.setDisplayAddress(exUpload.get(i).getPlace());
			logger.debug("ZIP TYPE HERE "+exUpload.get(i).getZipCode());
			business.setPostalCode(exUpload.get(i).getZipCode());
			business.setSource("jeeyoh");
			businessDAO.saveBusiness(business);
			
			/*user = userDAO.getUsersById(exUpload.get(i).getUploadByUsername());
			int creatorId = user.getUserId();*/
			// using this creator id we will find page id
			page.setPageId(12);
			
			
			event.setDescription(exUpload.get(i).getDescription());
			event.setVenue_name(place);
			event.setZip(exUpload.get(i).getZipCode());
			event.setEvent_time_local(exUpload.get(i).getTimeStamp());
			event.setChannel(exUpload.get(i).getEventType()+" tickets");
			event.setTitle(exUpload.get(i).getDescription());
			String newOriginalPrice = "";
			String discountPrice = "";
			if (exUpload.get(i).getOriginalPrice().contains("Rs"))
			{
				event.setCurrency_code("IND");
				newOriginalPrice = exUpload.get(i).getOriginalPrice().replace("Rs", "");
				event.setMaxPrice(Double.parseDouble(newOriginalPrice.replaceAll("\\s", "")));
			}
			else if(exUpload.get(i).getOriginalPrice().contains("$"))
			{
				event.setCurrency_code("USD");
				newOriginalPrice = exUpload.get(i).getOriginalPrice().replace("$", "");
				event.setMaxPrice(Double.parseDouble(newOriginalPrice.replaceAll("\\s", "")));
			}
			if (exUpload.get(i).getDiscountedPrice().contains("Rs"))
			{
				discountPrice = exUpload.get(i).getDiscountedPrice().replace("Rs", "");
				event.setMinPrice(Double.parseDouble(discountPrice.replaceAll("\\s", "")));
			}
			else if(exUpload.get(i).getOriginalPrice().contains("$"))
			{
				discountPrice = exUpload.get(i).getDiscountedPrice().replace("$", "");
				event.setMinPrice(Double.parseDouble(discountPrice.replaceAll("\\s", "")));
			}			
			eventsDAO.saveEvents(event,i);
			
			if(exUpload.get(i).getIfDeal().equals("Yes"))
			{
				deals.setBusiness(business);
				deals.setDealId(exUpload.get(i).getDescription());
				deals.setTitle(exUpload.get(i).getDescription());
				deals.setHighlightsHtml(exUpload.get(i).getHighlight());
				deals.setSidebarImageUrl(exUpload.get(i).getPictureUrl());
				deals.setSmallImageUrl(exUpload.get(i).getPictureUrl());
				deals.setMediumImageUrl(exUpload.get(i).getPictureUrl());
				deals.setLargeImageUrl(exUpload.get(i).getPictureUrl());
				deals.setAnnouncementTitle(exUpload.get(i).getFinePrints());
				deals.setDealType("jeeyoh");
				deals.setDealSource("jeeyoh");
				deals.setStatus("open");
				deals.setIsSoldOut(false);
				dealsDAO.saveDeal(deals);
			}
		}
		
	}
}
