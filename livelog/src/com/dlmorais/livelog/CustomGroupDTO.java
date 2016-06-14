package com.dlmorais.livelog;

/**
 * DTO for custom grouping information.
 *
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
public class CustomGroupDTO {

	/** Name of the grouping. */
	private String name;

	/** Regex of the grouping. */
	private String regex;

	/** CSS compatible color of the grouping. */
	private String color;

	/** Current count of this grouping. */
	private Integer count;

	/**
	 * Empty constructor for serialization.
	 */
	public CustomGroupDTO() {
		// Empty constructor for serialization.
	}

	/**
	 * Constructor with all necessary data.
	 *
	 * @param name
	 *            Name of the grouping.
	 * @param regex
	 *            Regex for the grouping.
	 */
	public CustomGroupDTO(final String name, final String regex) {
		this.name = name;
		this.regex = regex;
		this.count = 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return this.regex;
	}

	/**
	 * @param regex
	 *            the regex to set
	 */
	public void setRegex(final String regex) {
		this.regex = regex;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(final String color) {
		this.color = color;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return this.count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(final Integer count) {
		this.count = count;
	}

	/**
	 * Increment the count on this grouping.
	 */
	public void increment() {
		this.count++;
	}

}
