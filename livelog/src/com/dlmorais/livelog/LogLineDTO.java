package com.dlmorais.livelog;

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

}
