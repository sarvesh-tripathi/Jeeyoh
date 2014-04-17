package com.jeeyoh.model.response;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.search.CategoryModel;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CategoryLikesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty
	private List<CategoryModel> categoryModel;
	/**
	 * @param categoryModel the categoryModel to set
	 */
	public void setCategoryModel(List<CategoryModel> categoryModel) {
		this.categoryModel = categoryModel;
	}
	/**
	 * @return the categoryModel
	 */
	public List<CategoryModel> getCategoryModel() {
		return categoryModel;
	}

}
