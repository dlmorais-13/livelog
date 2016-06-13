package com.dlmorais.livelog;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * DTO of a log line.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
public class LogLineDTO {

	/** Line number. */
	private Long line;

	/** Line content. */
	private String content;

	/** Line date. */
	@JsonFormat(shape = Shape.NUMBER)
	private Date date;

	/**
	 * @return the line
	 */
	public Long getLine() {
		return this.line;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(final Long line) {
		this.line = line;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

}
