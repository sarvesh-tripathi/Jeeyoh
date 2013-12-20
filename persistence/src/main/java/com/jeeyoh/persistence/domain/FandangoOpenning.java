package com.jeeyoh.persistence.domain;

public class FandangoOpenning {
	 int id;	
	 String title;
	 String author;
	 String link;
	 String description;
	 String publishDate;
	 
	 public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

}
