package com.dlmorais.livelog;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class LogLineDTO {

	private Long line;
	
	private String content;

	@JsonFormat(shape = Shape.NUMBER)
	private Date date;

	/**
	 * @return the line
	 */
	public Long getLine() {
		return line;
	}

	/**
	 * @param line2 the line to set
	 */
	public void setLine(Long line) {
		this.line = line;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
