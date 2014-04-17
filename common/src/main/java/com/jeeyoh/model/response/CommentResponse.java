package com.jeeyoh.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.jeeyoh.model.funboard.CommentModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CommentResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CommentModel comment;
	/**
	 * @return the comment
	 */
	public CommentModel getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(CommentModel comment) {
		this.comment = comment;
	}

}
