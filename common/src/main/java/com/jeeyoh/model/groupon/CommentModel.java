package com.jeeyoh.model.groupon;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CommentModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private String websiteContentHtml;
	
	@JsonProperty
	private String emailContentHtml;
	
	@JsonProperty
	private String websiteContent;
	
	@JsonProperty
	private String title;
	
	@JsonProperty
	private String id;
	
	@JsonProperty
	private String emailContent;

	public String getWebsiteContentHtml() {
		return websiteContentHtml;
	}

	public void setWebsiteContentHtml(String websiteContentHtml) {
		this.websiteContentHtml = websiteContentHtml;
	}

	public String getEmailContentHtml() {
		return emailContentHtml;
	}

	public void setEmailContentHtml(String emailContentHtml) {
		this.emailContentHtml = emailContentHtml;
	}

	public String getWebsiteContent() {
		return websiteContent;
	}

	public void setWebsiteContent(String websiteContent) {
		this.websiteContent = websiteContent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

}
