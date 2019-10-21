/**
 * 
 */
package org.desz.inttoword.language;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author des
 *
 *         Builder pattern implementation.
 * 
 *         Enforces the creation of a valid IntWordMapping instance where nulls
 *         for instance variables are not allowed.
 * 
 */
public final class IntWordMapping {

	private String milln;
	private String thoud;
	private String hund;
	private String and;
	private String billn;
	private Map<String, String> intToWordMap;

	private IntWordMapping(Builder builder) {
		this.milln = builder.milln;
		this.thoud = builder.thoud;
		this.hund = builder.hund;
		this.and = builder.and;
		this.billn = builder.billn;
		this.intToWordMap = builder.intToWordMap;
	}

	/**
	 * @return the milln
	 */
	public String getMilln() {
		return milln;
	}

	/**
	 * @return the thoud
	 */
	public String getThoud() {
		return thoud;
	}

	/**
	 * @return the hund
	 */
	public String getHund() {
		return hund;
	}

	/**
	 * @return the and
	 */
	public String getAnd() {
		return and;
	}

	/**
	 * @return the billn
	 */
	public String getBilln() {
		return billn;
	}

	/**
	 * @return word for num orElse -> empty String.
	 */
	public String wordForNum(final int num) {
		final String s = String.valueOf(num);
		return intToWordMap.containsKey(s) ? intToWordMap.get(s) : StringUtils.EMPTY;

	}

	public static class Builder {

		private String milln;
		private String thoud;
		private String hund;
		private String and;
		private String billn;
		private Map<String, String> intToWordMap;

		public Builder withMilln(String milln) {
			this.milln = milln;
			return this;
		}

		public Builder withThoud(String thoud) {
			this.thoud = thoud;
			return this;
		}

		public Builder withHund(String hund) {
			this.hund = hund;
			return this;
		}

		public Builder withAnd(String and) {
			this.and = and;
			return this;
		}

		public Builder withBilln(String billn) {
			this.billn = billn;
			return this;
		}

		public Builder withIntToWordMap(Map<String, String> intToWordMap) {
			this.intToWordMap = intToWordMap;
			return this;
		}

		public IntWordMapping build() {
			return new IntWordMapping(this);
		}

	}

}
