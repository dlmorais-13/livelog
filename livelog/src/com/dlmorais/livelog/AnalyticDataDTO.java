package com.dlmorais.livelog;

/**
 * DTO for the analytics data.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
public class AnalyticDataDTO {

	/** Pattern grouped. */
	private String pattern;

	/** Count of ocurrences. */
	private Long count;

	public AnalyticDataDTO(final String pattern, final Long count) {
		this.pattern = pattern;
		this.count = count;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return this.pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return this.count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(final Long count) {
		this.count = count;
	}

	/**
	 * Increment the count on this DTO.
	 */
	public void increment() {
		this.count++;
	}

}
