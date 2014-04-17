package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.MemoryCardModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class MemoryCardResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<MemoryCardModel> memoryCardItems;
	/**
	 * @return the memoryCardItems
	 */
	public List<MemoryCardModel> getMemoryCardItems() {
		return memoryCardItems;
	}
	/**
	 * @param memoryCardItems the memoryCardItems to set
	 */
	public void setMemoryCardItems(List<MemoryCardModel> memoryCardItems) {
		this.memoryCardItems = memoryCardItems;
	}

}
